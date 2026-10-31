import argparse
import datetime
import json
import os
import re
import shutil
import subprocess
import sys
import tempfile
import time
from pathlib import Path
from urllib.parse import parse_qs, urlparse

import requests
from dotenv import load_dotenv

load_dotenv()

GITHUB_TOKEN = os.getenv("GITHUB_TOKEN")

API_BASE = "https://api.github.com"
HEADERS = {
    "Accept": "application/vnd.github+json",
    "X-GitHub-Api-Version": "2026-03-10",
}

if GITHUB_TOKEN:
    HEADERS["Authorization"] = f"Bearer {GITHUB_TOKEN}"
else:
    print("Токен не задан")

BASE_DIR = Path(__file__).resolve().parent
PROJECTS_DIR = BASE_DIR / "projects"
TODO_RUNNER_DIR = BASE_DIR / "todo_runner" / "ToDoRunner"

SKELETON_RUNNER_DIR = (
    BASE_DIR
    / "skeleton_runner"
    / "SkeletonRunner"
)

DESCRIPTOR_GENERATOR_DIR = (
    BASE_DIR
    / "descriptor-generator"
)

DESCRIPTOR_DIR = (
    BASE_DIR
    / "datasets"
    / "descriptor"
)

MIN_STARS = 1000
MAX_REPOS_TO_CHECK = 1000
TARGET_PROJECT_COUNT = 100
PROJECT_ID_START = 1

OUTPUT_FILE = (
    BASE_DIR
    / "repositories.json"
)

SLEEP_BETWEEN_REQUESTS = 0.0

# Pre-validation before JavaBench generation.
TARGET_JAVA = 22
MAX_JAVA_FILES = 100000
COMPILE_TIMEOUT_SEC = 5 * 60
TEST_TIMEOUT_SEC = 15 * 60

PROJECT_VALIDATION_LOG_DIR = (
    BASE_DIR
    / "datasets"
    / "project_validation_logs"
)


DATASET_CONFIGS = [
    (
        "selective-context",
        "stripped-context",
        None,
    ),
    (
        "minimum-context",
        "no-context",
        None,
    ),
    (
        "maximum-context",
        "full-context",
        8192,
    ),
]


TEST_PATHS = [
    "src/test/java",
    "src/test",
    "test",
    "tests",
]


TEST_FILE_MARKERS = [
    "junit",
    "testng",
    "mockito",
    "assertj",
    "hamcrest",
    "usejunitplatform",
    "testimplementation",
]


ALLOWED_LICENSES = {
    "MIT",
    "Apache-2.0",
    "BSD-2-Clause",
    "BSD-3-Clause",
    "ISC",
}


# ---------------------------------------------------------------------
# CLI helpers
# ---------------------------------------------------------------------

def non_negative_int(value):
    parsed = int(value)

    if parsed < 0:
        raise argparse.ArgumentTypeError(
            "value must be non-negative"
        )

    return parsed


def positive_int(value):
    parsed = int(value)

    if parsed < 1:
        raise argparse.ArgumentTypeError(
            "value must be positive"
        )

    return parsed


def parse_args(argv=None):
    parser = argparse.ArgumentParser(
        description=(
            "Searching for Java repositories "
            "and generating JavaBench datasets"
        )
    )

    parser.add_argument(
        "--min-stars",
        type=non_negative_int,
        default=MIN_STARS,
        help=(
            f"minimum number of GitHub stars "
            f"(default: {MIN_STARS})"
        ),
    )

    parser.add_argument(
        "--max-repos-to-check",
        type=positive_int,
        default=MAX_REPOS_TO_CHECK,
        help=(
            f"maximum number of repositories "
            f"to check "
            f"(default: {MAX_REPOS_TO_CHECK})"
        ),
    )

    parser.add_argument(
        "--target-project-count",
        type=positive_int,
        default=TARGET_PROJECT_COUNT,
        help=(
            f"number of projects to select "
            f"(default: {TARGET_PROJECT_COUNT})"
        ),
    )

    parser.add_argument(
        "--project-id-start",
        type=positive_int,
        default=PROJECT_ID_START,
        help=(
            f"starting number for PAxx identifiers "
            f"(default: {PROJECT_ID_START})"
        ),
    )

    parser.add_argument(
        "--target-java",
        type=positive_int,
        default=TARGET_JAVA,
        help=(
            "maximum Java language version allowed "
            f"for selected projects (default: {TARGET_JAVA})"
        ),
    )

    parser.add_argument(
        "--max-java-files",
        type=positive_int,
        default=MAX_JAVA_FILES,
        help=(
            "reject a repository before building if it contains "
            "more Java files than this value "
            f"(default: {MAX_JAVA_FILES})"
        ),
    )

    parser.add_argument(
        "--compile-timeout",
        type=positive_int,
        default=COMPILE_TIMEOUT_SEC,
        help=(
            "baseline compile timeout in seconds "
            f"(default: {COMPILE_TIMEOUT_SEC})"
        ),
    )

    parser.add_argument(
        "--test-timeout",
        type=positive_int,
        default=TEST_TIMEOUT_SEC,
        help=(
            "baseline test timeout in seconds "
            f"(default: {TEST_TIMEOUT_SEC})"
        ),
    )

    return parser.parse_args(argv)

# ---------------------------------------------------------------------
# GitHub API
# ---------------------------------------------------------------------

def github_get(
    url,
    params=None,
    allow_404=False,
):
    time.sleep(
        SLEEP_BETWEEN_REQUESTS
    )

    resp = requests.get(
        url,
        headers=HEADERS,
        params=params,
        timeout=30,
    )

    if (
        allow_404
        and resp.status_code == 404
    ):
        return None

    if resp.status_code == 403:
        remaining = resp.headers.get(
            "X-RateLimit-Remaining"
        )

        reset_ts = resp.headers.get(
            "X-RateLimit-Reset"
        )

        reset_dt = None

        if reset_ts:
            try:
                reset_dt = (
                    datetime.datetime
                    .fromtimestamp(
                        int(reset_ts)
                    )
                )
            except (
                TypeError,
                ValueError,
            ):
                reset_dt = reset_ts

        raise RuntimeError(
            "403. Possible request limit exceeded.\n"
            f"Remaining: {remaining}, "
            f"Reset at: {reset_dt}"
        )

    resp.raise_for_status()

    return resp


def search_java_repositories(
    min_stars=MIN_STARS,
    total_limit=MAX_REPOS_TO_CHECK,
):
    url = (
        f"{API_BASE}"
        f"/search/repositories"
    )

    query = (
        f"language:Java "
        f"stars:>={min_stars} "
        f"archived:false "
        f"fork:false"
    )

    repos = []

    page = 1
    per_page = 100

    while len(repos) < total_limit:
        params = {
            "q": query,
            "sort": "stars",
            "order": "desc",
            "per_page": per_page,
            "page": page,
        }

        resp = github_get(
            url,
            params=params,
        )

        items = (
            resp.json()
            .get(
                "items",
                [],
            )
        )

        if not items:
            break

        repos.extend(items)

        if len(items) < per_page:
            break

        page += 1

    return repos[:total_limit]


def get_repo_root_contents(
    owner,
    repo,
):
    url = (
        f"{API_BASE}"
        f"/repos/{owner}/{repo}/contents"
    )

    resp = github_get(url)

    return resp.json()


def path_exists(
    owner,
    repo,
    path,
):
    url = (
        f"{API_BASE}"
        f"/repos/{owner}/{repo}"
        f"/contents/{path}"
    )

    resp = github_get(
        url,
        allow_404=True,
    )

    return resp is not None


def get_text_file(
    owner,
    repo,
    path,
):
    url = (
        f"{API_BASE}"
        f"/repos/{owner}/{repo}"
        f"/contents/{path}"
    )

    resp = github_get(
        url,
        allow_404=True,
    )

    if resp is None:
        return None

    data = resp.json()

    if (
        isinstance(data, dict)
        and data.get("type") == "file"
        and data.get("download_url")
    ):
        file_resp = requests.get(
            data["download_url"],
            timeout=30,
        )

        file_resp.raise_for_status()

        return file_resp.text

    return None


def get_repo_details(
    owner,
    repo,
):
    url = (
        f"{API_BASE}"
        f"/repos/{owner}/{repo}"
    )

    resp = github_get(url)

    return resp.json()


def get_license_spdx_from_details(
    repo_details,
):
    license_info = (
        repo_details.get("license")
    )

    if not license_info:
        return None

    spdx_id = (
        license_info.get("spdx_id")
    )

    if (
        not spdx_id
        or spdx_id == "NOASSERTION"
    ):
        return None

    return spdx_id


def is_license_allowed(
    spdx_id,
):
    return (
        spdx_id
        in ALLOWED_LICENSES
    )


def get_default_branch_from_details(
    repo_details,
):
    return repo_details.get(
        "default_branch"
    )


def get_recursive_tree(
    owner,
    repo,
    tree_ref,
):
    url = (
        f"{API_BASE}"
        f"/repos/{owner}/{repo}"
        f"/git/trees/{tree_ref}"
    )

    resp = github_get(
        url,
        params={
            "recursive": 1,
        },
        allow_404=True,
    )

    if resp is None:
        return None

    return resp.json()


# ---------------------------------------------------------------------
# Repository filtering
# ---------------------------------------------------------------------

def detect_build_tool(
    tree_data,
):
    if (
        not tree_data
        or "tree" not in tree_data
    ):
        return None

    build_files = {
        item.get(
            "path",
            "",
        )
        for item
        in tree_data["tree"]
        if item.get("type") == "blob"
    }

    if (
        "build.gradle"
        in build_files
        or "build.gradle.kts"
        in build_files
    ):
        return "gradle"

    if "pom.xml" in build_files:
        return "maven"

    if any(
        path.endswith(
            "/build.gradle"
        )
        or path.endswith(
            "/build.gradle.kts"
        )
        for path
        in build_files
    ):
        return "gradle"

    if any(
        path.endswith(
            "/pom.xml"
        )
        for path
        in build_files
    ):
        return "maven"

    return None


def has_java_source_tree(
    tree_data,
):
    if (
        not tree_data
        or "tree" not in tree_data
    ):
        return False

    source_roots = {
        item.get(
            "path",
            "",
        )
        for item
        in tree_data["tree"]
        if item.get("type") == "tree"
    }

    has_main = any(
        path == "src/main/java"
        or path.endswith(
            "/src/main/java"
        )
        for path
        in source_roots
    )

    has_test = any(
        path == "src/test/java"
        or path.endswith(
            "/src/test/java"
        )
        for path
        in source_roots
    )

    return (
        has_main
        and has_test
    )


def has_gradle_wrapper(
    owner,
    repo,
):
    has_script = (
        path_exists(
            owner,
            repo,
            "gradlew",
        )
        or path_exists(
            owner,
            repo,
            "gradlew.bat",
        )
    )

    has_wrapper_props = (
        path_exists(
            owner,
            repo,
            "gradle/wrapper/"
            "gradle-wrapper.properties",
        )
    )

    return (
        has_script
        and has_wrapper_props
    )


def has_maven_wrapper(
    owner,
    repo,
):
    has_script = (
        path_exists(
            owner,
            repo,
            "mvnw",
        )
        or path_exists(
            owner,
            repo,
            "mvnw.cmd",
        )
    )

    has_wrapper_props = (
        path_exists(
            owner,
            repo,
            ".mvn/wrapper/"
            "maven-wrapper.properties",
        )
    )

    return (
        has_script
        and has_wrapper_props
    )


def has_build_wrapper(
    owner,
    repo,
    build_tool,
):
    if build_tool == "gradle":
        return has_gradle_wrapper(
            owner,
            repo,
        )

    if build_tool == "maven":
        return has_maven_wrapper(
            owner,
            repo,
        )

    return False


def has_unit_tests(
    owner,
    repo,
    tree_data=None,
):
    if (
        tree_data
        and "tree" in tree_data
    ):
        paths = {
            item.get(
                "path",
                "",
            )
            for item
            in tree_data["tree"]
        }

        if any(
            path == "src/test/java"
            or path.endswith(
                "/src/test/java"
            )
            for path
            in paths
        ):
            return True

    for test_path in TEST_PATHS:
        try:
            if path_exists(
                owner,
                repo,
                test_path,
            ):
                return True

        except Exception:
            pass

    try:
        pom = get_text_file(
            owner,
            repo,
            "pom.xml",
        )

        if pom:
            low = pom.lower()

            if any(
                marker in low
                for marker
                in TEST_FILE_MARKERS
            ):
                return True

    except Exception:
        pass

    for gradle_name in (
        "build.gradle",
        "build.gradle.kts",
    ):
        try:
            gradle = get_text_file(
                owner,
                repo,
                gradle_name,
            )

            if gradle:
                low = gradle.lower()

                if any(
                    marker in low
                    for marker
                    in TEST_FILE_MARKERS
                ):
                    return True

        except Exception:
            pass

    return False


# ---------------------------------------------------------------------
# Repository statistics
# ---------------------------------------------------------------------

def parse_last_page_from_link_header(
    link_header,
):
    if not link_header:
        return None

    parts = [
        p.strip()
        for p
        in link_header.split(",")
    ]

    for part in parts:
        if 'rel="last"' not in part:
            continue

        start = part.find("<")
        end = part.find(">")

        if (
            start == -1
            or end == -1
        ):
            continue

        url = part[
            start + 1:end
        ]

        parsed = urlparse(url)

        qs = parse_qs(
            parsed.query
        )

        page_vals = qs.get(
            "page"
        )

        if page_vals:
            try:
                return int(
                    page_vals[0]
                )
            except ValueError:
                return None

    return None


def estimate_commit_count(
    owner,
    repo,
):
    url = (
        f"{API_BASE}"
        f"/repos/{owner}/{repo}"
        f"/commits"
    )

    resp = github_get(
        url,
        params={
            "per_page": 1,
        },
        allow_404=True,
    )

    if resp is None:
        return None

    link = resp.headers.get(
        "Link"
    )

    last_page = (
        parse_last_page_from_link_header(
            link
        )
    )

    if last_page is not None:
        return last_page

    data = resp.json()

    if isinstance(data, list):
        return len(data)

    return None


def estimate_file_count_from_tree(
    tree_data,
):
    if (
        not tree_data
        or "tree" not in tree_data
    ):
        return None

    return sum(
        1
        for item
        in tree_data["tree"]
        if item.get("type") == "blob"
    )


# ---------------------------------------------------------------------
# Local command helpers
# ---------------------------------------------------------------------

def run_command(
    command,
    cwd,
):
    result = subprocess.run(
        command,
        cwd=cwd,
        text=True,
        capture_output=True,
    )

    if result.returncode != 0:
        stdout = (
            result.stdout
            or ""
        ).strip()

        stderr = (
            result.stderr
            or ""
        ).strip()

        combined = "\n".join(
            part
            for part
            in (
                stdout,
                stderr,
            )
            if part
        )

        raise RuntimeError(
            "Команда завершилась "
            f"с кодом {result.returncode}: "
            f"{' '.join(command)}\n"
            f"{combined}"
        )

    return result


def get_git_commit(
    repo_dir,
):
    result = run_command(
        [
            "git",
            "rev-parse",
            "HEAD",
        ],
        repo_dir,
    )

    return (
        result.stdout
        .strip()
    )


def get_git_branch(
    repo_dir,
):
    result = run_command(
        [
            "git",
            "rev-parse",
            "--abbrev-ref",
            "HEAD",
        ],
        repo_dir,
    )

    return (
        result.stdout
        .strip()
    )


def utc_now_iso():
    return (
        datetime.datetime
        .now(
            datetime.timezone.utc
        )
        .isoformat()
    )


# ---------------------------------------------------------------------
# Project pre-validation
# ---------------------------------------------------------------------

def _iter_source_files_limited(
    project_dir,
    suffix,
    limit,
):
    """Yield source files while skipping generated/VCS directories."""
    skipped_dirs = {
        ".git",
        ".gradle",
        ".idea",
        "build",
        "target",
        "out",
        "node_modules",
    }

    count = 0

    for root, dirs, files in os.walk(project_dir):
        dirs[:] = [
            name
            for name in dirs
            if name not in skipped_dirs
        ]

        for name in files:
            if not name.endswith(suffix):
                continue

            yield Path(root) / name
            count += 1

            if count >= limit:
                return


def count_java_files_up_to(
    project_dir,
    limit,
):
    count = 0

    for _ in _iter_source_files_limited(
        project_dir,
        ".java",
        limit + 1,
    ):
        count += 1

    return count


def detect_required_java_from_root_build_files(
    project_dir,
):
    """
    Cheap static prefilter.

    Only root build files are inspected deliberately: scanning every nested
    build file can produce false positives from optional profiles/examples.
    The following compile step remains the authoritative validation.
    """
    candidates = [
        project_dir / "build.gradle",
        project_dir / "build.gradle.kts",
        project_dir / "gradle.properties",
        project_dir / "pom.xml",
    ]

    patterns = [
        r"JavaLanguageVersion\.of\(\s*(\d+)\s*\)",
        r"JavaVersion\.VERSION_(\d+)",
        r"<java\.version>\s*(\d+)\s*</java\.version>",
        r"<maven\.compiler\.release>\s*(\d+)\s*</maven\.compiler\.release>",
        r"<maven\.compiler\.source>\s*(\d+)\s*</maven\.compiler\.source>",
        r"<release>\s*(\d+)\s*</release>",
        r"sourceCompatibility\s*=\s*[\"']?(\d+)",
        r"targetCompatibility\s*=\s*[\"']?(\d+)",
    ]

    versions = []

    for path in candidates:
        if not path.is_file():
            continue

        content = path.read_text(
            encoding="utf-8",
            errors="ignore",
        )

        for pattern in patterns:
            for match in re.findall(
                pattern,
                content,
                flags=re.IGNORECASE,
            ):
                try:
                    versions.append(int(match))
                except ValueError:
                    pass

    return max(versions) if versions else None


def run_validation_command(
    command,
    cwd,
    timeout_sec,
):
    started = time.monotonic()

    try:
        result = subprocess.run(
            command,
            cwd=cwd,
            text=True,
            stdout=subprocess.PIPE,
            stderr=subprocess.STDOUT,
            timeout=timeout_sec,
            env=os.environ.copy(),
        )

        return {
            "ok": result.returncode == 0,
            "return_code": result.returncode,
            "duration_sec": round(
                time.monotonic() - started,
                3,
            ),
            "output": result.stdout or "",
            "timed_out": False,
        }

    except subprocess.TimeoutExpired as exc:
        output = ""

        if exc.stdout:
            if isinstance(exc.stdout, bytes):
                output += exc.stdout.decode(
                    "utf-8",
                    errors="replace",
                )
            else:
                output += exc.stdout

        if exc.stderr:
            if isinstance(exc.stderr, bytes):
                output += exc.stderr.decode(
                    "utf-8",
                    errors="replace",
                )
            else:
                output += exc.stderr

        return {
            "ok": False,
            "return_code": None,
            "duration_sec": round(
                time.monotonic() - started,
                3,
            ),
            "output": output,
            "timed_out": True,
        }


def classify_validation_failure(output):
    low = output.lower()

    required_java = None
    java_patterns = [
        r"should be built with java\s+(\d+)\s+or above",
        r"languageversion\s*=\s*(\d+)",
        r"requires java\s+(\d+)",
        r"java\s+(\d+)\s+or newer",
        r"java\s+(\d+)\s+or later",
    ]

    for pattern in java_patterns:
        match = re.search(
            pattern,
            output,
            flags=re.IGNORECASE,
        )

        if match:
            try:
                required_java = int(match.group(1))
            except (ValueError, IndexError):
                pass

            break

    if (
        "should be built with java" in low
        or (
            "cannot find a java installation" in low
            and "languageversion" in low
        )
        or "unsupported class file major version" in low
        or "invalid source release" in low
    ):
        return (
            "unsupported_java_version",
            required_java,
        )

    if (
        "there were failing tests" in low
        or re.search(
            r"\d+\s+tests?\s+completed,\s+\d+\s+failed",
            low,
        )
        or (
            "tests run:" in low
            and "failures:" in low
            and not re.search(
                r"failures:\s*0",
                low,
            )
        )
    ):
        return (
            "baseline_test_failure",
            required_java,
        )

    dependency_markers = [
        "could not resolve",
        "could not download",
        "could not get resource",
        "failed to read artifact descriptor",
        "could not transfer artifact",
        "connection timed out",
        "connection refused",
        "unknown host",
        "temporary failure in name resolution",
    ]

    if any(
        marker in low
        for marker in dependency_markers
    ):
        return (
            "dependency_resolution",
            required_java,
        )

    if (
        "compilation failed" in low
        or "compilation failure" in low
        or "compilejava failed" in low
        or "compilation error" in low
    ):
        return (
            "baseline_compile_failure",
            required_java,
        )

    return (
        "build_failure",
        required_java,
    )


def validation_commands(
    project_dir,
    build_tool,
):
    if build_tool == "gradle":
        wrapper = project_dir / "gradlew"

        try:
            wrapper.chmod(
                wrapper.stat().st_mode | 0o111
            )
        except OSError:
            pass

        return (
            [
                "./gradlew",
                "compileJava",
                "--no-daemon",
                "--console=plain",
            ],
            [
                "./gradlew",
                "test",
                "--no-daemon",
                "--console=plain",
            ],
        )

    if build_tool == "maven":
        wrapper = project_dir / "mvnw"

        try:
            wrapper.chmod(
                wrapper.stat().st_mode | 0o111
            )
        except OSError:
            pass

        return (
            [
                "./mvnw",
                "-B",
                "-DskipTests",
                "compile",
            ],
            [
                "./mvnw",
                "-B",
                "test",
            ],
        )

    raise ValueError(
        f"Unsupported build tool: {build_tool}"
    )


def safe_log_stem(repo):
    value = (
        repo.get("full_name")
        or repo.get("name")
        or "repository"
    )

    return re.sub(
        r"[^A-Za-z0-9_.-]+",
        "__",
        value,
    )


def write_validation_log(
    repo,
    stage,
    output,
):
    PROJECT_VALIDATION_LOG_DIR.mkdir(
        parents=True,
        exist_ok=True,
    )

    path = (
        PROJECT_VALIDATION_LOG_DIR
        / f"{safe_log_stem(repo)}.{stage}.log"
    )

    path.write_text(
        output,
        encoding="utf-8",
        errors="replace",
    )

    return str(path)


def validate_project_before_generation(
    repo,
    project_dir,
    build_tool,
    target_java,
    max_java_files,
    compile_timeout,
    test_timeout,
):
    result = {
        "valid": False,
        "failure_type": None,
        "required_java": None,
        "java_file_count": None,
        "compile_duration_sec": None,
        "test_duration_sec": None,
        "compile_log": None,
        "test_log": None,
    }

    # Stage 1: very cheap local/static checks.
    java_file_count = count_java_files_up_to(
        project_dir,
        max_java_files,
    )

    result["java_file_count"] = (
        java_file_count
    )

    if java_file_count > max_java_files:
        result["failure_type"] = (
            "project_too_large"
        )

        return result

    required_java = (
        detect_required_java_from_root_build_files(
            project_dir
        )
    )

    result["required_java"] = required_java

    if (
        required_java is not None
        and required_java > target_java
    ):
        result["failure_type"] = (
            "unsupported_java_version"
        )

        return result

    compile_command, test_command = (
        validation_commands(
            project_dir,
            build_tool,
        )
    )

    # Stage 2: compile once for the untouched repository.
    compile_result = run_validation_command(
        compile_command,
        project_dir,
        compile_timeout,
    )

    result["compile_duration_sec"] = (
        compile_result["duration_sec"]
    )

    result["compile_log"] = (
        write_validation_log(
            repo,
            "compile",
            compile_result["output"],
        )
    )

    if not compile_result["ok"]:
        if compile_result["timed_out"]:
            result["failure_type"] = (
                "compile_timeout"
            )

            return result

        failure_type, detected_java = (
            classify_validation_failure(
                compile_result["output"]
            )
        )

        result["failure_type"] = failure_type

        if detected_java is not None:
            result["required_java"] = (
                detected_java
            )

        return result

    # Stage 3: tests only for repositories that already compile.
    test_result = run_validation_command(
        test_command,
        project_dir,
        test_timeout,
    )

    result["test_duration_sec"] = (
        test_result["duration_sec"]
    )

    result["test_log"] = (
        write_validation_log(
            repo,
            "test",
            test_result["output"],
        )
    )

    if not test_result["ok"]:
        if test_result["timed_out"]:
            result["failure_type"] = (
                "test_timeout"
            )

            return result

        failure_type, detected_java = (
            classify_validation_failure(
                test_result["output"]
            )
        )

        result["failure_type"] = failure_type

        if detected_java is not None:
            result["required_java"] = (
                detected_java
            )

        return result

    result["valid"] = True
    return result


# ---------------------------------------------------------------------
# Pipeline dependencies
# ---------------------------------------------------------------------

def ensure_pipeline_dependencies():
    required_dirs = [
        PROJECTS_DIR,
        TODO_RUNNER_DIR,
        SKELETON_RUNNER_DIR,
        DESCRIPTOR_GENERATOR_DIR,
    ]

    for directory in required_dirs:
        if not directory.exists():
            raise RuntimeError(
                f"Не найдена директория "
                f"{directory}"
            )

    print(
        "  - компиляция ToDoRunner"
    )

    run_command(
        [
            "mvn",
            "-q",
            "-DskipTests",
            "compile",
        ],
        TODO_RUNNER_DIR,
    )

    print(
        "  - компиляция SkeletonRunner"
    )

    run_command(
        [
            "mvn",
            "-q",
            "-DskipTests",
            "compile",
        ],
        SKELETON_RUNNER_DIR,
    )

    print(
        "  - компиляция "
        "descriptor-generator"
    )

    run_command(
        [
            "./gradlew",
            "classes",
        ],
        DESCRIPTOR_GENERATOR_DIR,
    )


# ---------------------------------------------------------------------
# Project IDs
# ---------------------------------------------------------------------

def project_id_for_index(
    index,
):
    return f"PA{index:02d}"


def next_free_project_index(
    start=PROJECT_ID_START,
):
    index = start

    while True:
        project_id = (
            project_id_for_index(
                index
            )
        )

        if not any(
            (
                PROJECTS_DIR
                / name
            ).exists()
            for name
            in (
                project_id,
                f"{project_id}-Context",
                f"{project_id}-Solution",
            )
        ):
            return index

        index += 1


# ---------------------------------------------------------------------
# Clone + provenance metadata
# ---------------------------------------------------------------------

def clone_repository(
    repo,
    destination,
):
    if destination.exists():
        print(
            "  - репозиторий уже "
            f"склонирован: {destination}"
        )

        return

    destination.parent.mkdir(
        parents=True,
        exist_ok=True,
    )

    print(
        "  - клонирование в "
        f"{destination}"
    )

    run_command(
        [
            "git",
            "clone",
            "--depth",
            "1",
            repo["clone_url"],
            str(destination),
        ],
        BASE_DIR,
    )


def process_selected_repo(
    repo,
    project_index,
    target_java,
    max_java_files,
    compile_timeout,
    test_timeout,
):
    project_id = (
        project_id_for_index(
            project_index
        )
    )

    solution_path = (
        PROJECTS_DIR
        / f"{project_id}-Solution"
    )

    result = {
        "project_id": None,
        "solution_path": None,
        "processing_status": "pending",
        "commit_sha": None,
        "cloned_branch": None,
        "cloned_at": None,
        "validation": None,
    }

    temp_root = Path(
        tempfile.mkdtemp(
            prefix=".candidate-",
            dir=PROJECTS_DIR,
        )
    )

    candidate_path = temp_root / "repo"

    try:
        clone_repository(
            repo,
            candidate_path,
        )

        commit_sha = get_git_commit(
            candidate_path
        )

        cloned_branch = get_git_branch(
            candidate_path
        )

        cloned_at = utc_now_iso()

        print(
            "  - pre-validation: "
            "static check -> compile -> tests"
        )

        validation = (
            validate_project_before_generation(
                repo=repo,
                project_dir=candidate_path,
                build_tool=repo["build_tool"],
                target_java=target_java,
                max_java_files=max_java_files,
                compile_timeout=compile_timeout,
                test_timeout=test_timeout,
            )
        )

        result["validation"] = validation
        result["commit_sha"] = commit_sha
        result["cloned_branch"] = cloned_branch
        result["cloned_at"] = cloned_at

        if not validation["valid"]:
            result["processing_status"] = (
                "rejected_validation"
            )

            result["processing_error"] = (
                validation["failure_type"]
            )

            return result

        if solution_path.exists():
            raise RuntimeError(
                "Destination already exists: "
                f"{solution_path}"
            )

        candidate_path.rename(
            solution_path
        )

        result["project_id"] = project_id
        result["solution_path"] = str(
            solution_path
        )
        result["processing_status"] = "cloned"

    except Exception as exc:
        result["processing_status"] = "failed"
        result["processing_error"] = str(exc)

    finally:
        # If candidate_path was renamed, temp_root is now empty.
        shutil.rmtree(
            temp_root,
            ignore_errors=True,
        )

    return result


# ---------------------------------------------------------------------
# JavaBench generation pipeline
# ---------------------------------------------------------------------

def run_todo_runner(
    project_ids,
):
    for project_id in project_ids:
        print(
            "  - запуск ToDoRunner "
            f"для {project_id}"
        )

        run_command(
            [
                "mvn",
                "exec:java",
                "-Dexec.mainClass=cs.hse.Main",
                (
                    "-Dexec.args="
                    "--projects-root ../../projects "
                    f"--project-id {project_id}"
                ),
            ],
            TODO_RUNNER_DIR,
        )


def run_skeleton_runner(
    project_ids,
):
    for project_id in project_ids:
        print(
            "  - запуск SkeletonRunner "
            f"для {project_id}"
        )

        run_command(
            [
                "mvn",
                "exec:java",
                "-Dexec.mainClass=cs.hse.Main",
                (
                    "-Dexec.args="
                    "--projects-root ../../projects "
                    f"--project-id {project_id}"
                ),
            ],
            SKELETON_RUNNER_DIR,
        )


def run_descriptor_generator(
    project_ids,
):
    DESCRIPTOR_DIR.mkdir(
        parents=True,
        exist_ok=True,
    )

    for project_id in project_ids:
        print(
            "  - генерация descriptor "
            f"для {project_id}"
        )

        run_command(
            [
                "./gradlew",
                "run",
                (
                    "--args="
                    f"--projects-root "
                    f"{PROJECTS_DIR} "
                    f"--out-dir "
                    f"{DESCRIPTOR_DIR} "
                    f"--project-id "
                    f"{project_id}"
                ),
            ],
            DESCRIPTOR_GENERATOR_DIR,
        )


def run_make_dataset():
    for (
        dataset_name,
        mode,
        max_context_len,
    ) in DATASET_CONFIGS:

        dataset_dir = (
            BASE_DIR
            / "datasets"
            / dataset_name
        )

        dataset_dir.mkdir(
            parents=True,
            exist_ok=True,
        )

        print(
            "  - генерация датасета "
            f"{mode} в {dataset_dir}"
        )

        command = [
            sys.executable,
            "-m",
            "app.make_dataset",
            "--descriptor-dir",
            str(DESCRIPTOR_DIR),
            "--out-dir",
            str(dataset_dir),
            "--skip-missing-projects",
            "--mode",
            mode,
        ]

        if (
            max_context_len
            is not None
        ):
            command += [
                "--max-context-len",
                str(max_context_len),
            ]

        run_command(
            command,
            BASE_DIR,
        )


def run_javabench_pipeline(
    project_ids,
):
    run_todo_runner(
        project_ids
    )

    run_skeleton_runner(
        project_ids
    )

    run_descriptor_generator(
        project_ids
    )

    run_make_dataset()


# ---------------------------------------------------------------------
# Repository metadata
# ---------------------------------------------------------------------

def build_repo_record(
    repo,
    commits=None,
    files=None,
    build_tool=None,
    license_spdx=None,
    default_branch=None,
):
    return {
        "name":
            repo.get("name"),

        "full_name":
            repo.get("full_name"),

        "url":
            repo.get("html_url"),

        "clone_url":
            repo.get("clone_url"),

        "stars":
            repo.get(
                "stargazers_count"
            ),

        "build_tool":
            build_tool,

        "license":
            license_spdx,

        "default_branch":
            default_branch,

        "commits":
            commits,

        "files":
            files,
    }


# ---------------------------------------------------------------------
# Main
# ---------------------------------------------------------------------

def main(argv=None):
    args = parse_args(argv)

    PROJECTS_DIR.mkdir(
        parents=True,
        exist_ok=True,
    )

    ensure_pipeline_dependencies()

    candidates = (
        search_java_repositories(
            min_stars=args.min_stars,
            total_limit=args.max_repos_to_check,
        )
    )

    selected_repos = []

    checked = 0

    next_project_index = (
        next_free_project_index(
            args.project_id_start
        )
    )

    for repo in candidates:
        cloned_so_far = len(
            [
                r
                for r
                in selected_repos
                if r.get(
                    "processing_status"
                ) == "cloned"
            ]
        )

        if (
            cloned_so_far
            >= args.target_project_count
        ):
            break

        checked += 1

        full_name = repo[
            "full_name"
        ]

        owner, name = (
            full_name.split(
                "/",
                1,
            )
        )

        print(
            f"[{checked}/"
            f"{len(candidates)}] "
            f"Проверка {full_name}"
        )

        try:
            repo_details = (
                get_repo_details(
                    owner,
                    name,
                )
            )

            license_spdx = (
                get_license_spdx_from_details(
                    repo_details
                )
            )

            if not is_license_allowed(
                license_spdx
            ):
                print(
                    "  - не подходит: "
                    f"лицензия "
                    f"{license_spdx or 'не указана'}"
                )

                continue

            default_branch = (
                get_default_branch_from_details(
                    repo_details
                )
            )

            if not default_branch:
                print(
                    "  - не подходит: "
                    "не удалось определить "
                    "default branch"
                )

                continue

            tree_data = (
                get_recursive_tree(
                    owner,
                    name,
                    default_branch,
                )
            )

            if (
                not tree_data
                or "tree"
                not in tree_data
            ):
                print(
                    "  - не подходит: "
                    "не удалось прочитать "
                    "дерево репозитория"
                )

                continue

            build_tool = (
                detect_build_tool(
                    tree_data
                )
            )

            if (
                build_tool
                not in {
                    "gradle",
                    "maven",
                }
            ):
                print(
                    "  - не подходит: "
                    "не найден pom.xml "
                    "или build.gradle(.kts)"
                )

                continue

            if not has_java_source_tree(
                tree_data
            ):
                print(
                    "  - не подходит: "
                    "не найден "
                    "src/main/java "
                    "и src/test/java"
                )

                continue

            if not has_build_wrapper(
                owner,
                name,
                build_tool,
            ):
                print(
                    "  - не подходит: "
                    "не найден wrapper "
                    "для запуска"
                )

                continue

            if not has_unit_tests(
                owner,
                name,
                tree_data,
            ):
                print(
                    "  - не подходит: "
                    "не найдены unit-тесты"
                )

                continue

            commits = None
            files = None

            try:
                commits = (
                    estimate_commit_count(
                        owner,
                        name,
                    )
                )

            except Exception as exc:
                print(
                    "  - не удалось "
                    "оценить количество "
                    f"коммитов: {exc}"
                )

            try:
                files = (
                    estimate_file_count_from_tree(
                        tree_data
                    )
                )

            except Exception as exc:
                print(
                    "  - не удалось "
                    "оценить количество "
                    f"файлов: {exc}"
                )

            record = (
                build_repo_record(
                    repo,
                    commits=commits,
                    files=files,
                    build_tool=build_tool,
                    license_spdx=license_spdx,
                    default_branch=default_branch,
                )
            )

            processing_result = (
                process_selected_repo(
                    record,
                    next_project_index,
                    target_java=args.target_java,
                    max_java_files=args.max_java_files,
                    compile_timeout=args.compile_timeout,
                    test_timeout=args.test_timeout,
                )
            )

            record.update(
                processing_result
            )

            if (
                record[
                    "processing_status"
                ]
                == "cloned"
            ):
                print(
                    "  + подходит, "
                    f"склонирован как "
                    f"{record['project_id']}"
                    "-Solution"
                )

                print(
                    "    commit: "
                    f"{record['commit_sha']}"
                )

                next_project_index = (
                    next_free_project_index(
                        next_project_index
                        + 1
                    )
                )

            elif (
                record[
                    "processing_status"
                ]
                == "rejected_validation"
            ):
                validation = (
                    record.get("validation")
                    or {}
                )

                print(
                    "  - не подходит после "
                    "baseline validation: "
                    f"{validation.get('failure_type')}"
                )

                if validation.get(
                    "required_java"
                ):
                    print(
                        "    required Java: "
                        f"{validation['required_java']}"
                    )

            else:
                print(
                    "  ! ошибка clone/validation: "
                    f"{record.get('processing_error')}"
                )

            selected_repos.append(
                record
            )

        except Exception as exc:
            print(
                "  ! ошибка при "
                f"обработке {full_name}: "
                f"{exc}"
            )


    cloned_count = len(
        [
            r
            for r
            in selected_repos
            if r.get(
                "processing_status"
            ) == "cloned"
        ]
    )

    if cloned_count > 0:
        cloned_project_ids = [
            r["project_id"]
            for r
            in selected_repos
            if r.get(
                "processing_status"
            ) == "cloned"
        ]

        try:
            run_javabench_pipeline(
                cloned_project_ids
            )

            for record in selected_repos:
                if (
                    record.get(
                        "processing_status"
                    )
                    == "cloned"
                ):
                    record[
                        "processing_status"
                    ] = "dataset_generated"

        except Exception as exc:
            print(
                "  ! ошибка JavaBench "
                f"pipeline: {exc}"
            )

            for record in selected_repos:
                if (
                    record.get(
                        "processing_status"
                    )
                    == "cloned"
                ):
                    record[
                        "processing_status"
                    ] = "pipeline_failed"

                    record[
                        "processing_error"
                    ] = str(exc)

    else:
        print(
            "Не найдено ни одного "
            "репозитория для "
            "генерации датасета"
        )


    with OUTPUT_FILE.open(
        "w",
        encoding="utf-8",
    ) as f:
        json.dump(
            selected_repos,
            f,
            ensure_ascii=False,
            indent=2,
        )


    print(
        f"\nОбработано кандидатов после API-фильтров: "
        f"{len(selected_repos)}"
    )

    print(
        f"Склонировано: "
        f"{cloned_count}"
    )

    print(
        "Результат записан в "
        f"{OUTPUT_FILE}"
    )


if __name__ == "__main__":
    main()
