import argparse
import json
import os
import re
import subprocess
import time
from pathlib import Path


ROOT = Path(__file__).resolve().parents[1]

DEFAULT_PROJECTS_DIR = ROOT / "projects"
DEFAULT_OUTPUT = ROOT / "datasets" / "project_validation.json"
DEFAULT_LOGS_DIR = ROOT / "datasets" / "project_validation_logs"

DEFAULT_TIMEOUT_SEC = 15 * 60


# ---------------------------------------------------------------------
# Project discovery
# ---------------------------------------------------------------------

def project_id_from_path(path: Path):
    name = path.name

    if not name.endswith("-Solution"):
        return None

    return name[:-len("-Solution")]


def discover_projects(projects_dir: Path):
    projects = []

    for path in sorted(projects_dir.glob("PA*-Solution")):
        if not path.is_dir():
            continue

        project_id = project_id_from_path(path)

        if project_id:
            projects.append({
                "project_id": project_id,
                "path": path,
            })

    return projects


# ---------------------------------------------------------------------
# Build-system detection
# ---------------------------------------------------------------------

def detect_build_system(project_root: Path):
    gradlew = project_root / "gradlew"
    mvnw = project_root / "mvnw"

    if gradlew.is_file():
        return "gradle"

    if mvnw.is_file():
        return "maven"

    if (
        (project_root / "build.gradle").is_file()
        or (project_root / "build.gradle.kts").is_file()
        or (project_root / "settings.gradle").is_file()
        or (project_root / "settings.gradle.kts").is_file()
    ):
        return "gradle_no_wrapper"

    if (project_root / "pom.xml").is_file():
        return "maven_no_wrapper"

    return "unknown"


# ---------------------------------------------------------------------
# Commands
# ---------------------------------------------------------------------

def ensure_executable(path: Path):
    try:
        mode = path.stat().st_mode
        path.chmod(mode | 0o111)
    except OSError:
        pass


def build_command(
    project_root: Path,
    build_system: str,
):
    if build_system == "gradle":
        wrapper = project_root / "gradlew"
        ensure_executable(wrapper)

        return [
            str(wrapper),
            "test",
            "--console=plain",
            "--no-daemon",
        ]

    if build_system == "maven":
        wrapper = project_root / "mvnw"
        ensure_executable(wrapper)

        return [
            str(wrapper),
            "test",
            "-B",
        ]

    return None


# ---------------------------------------------------------------------
# Failure classification
# ---------------------------------------------------------------------

def classify_failure(log: str):
    low = log.lower()

    # --------------------------------------------------
    # Java/toolchain incompatibility
    # --------------------------------------------------

    java_required_patterns = [
        r"should be built with java\s+(\d+)\s+or above",
        r"languageversion[=:\s]+(\d+)",
        r"requires java\s+(\d+)",
        r"java\s+(\d+)\s+or newer",
        r"java\s+(\d+)\s+or later",
    ]

    required_java = None

    for pattern in java_required_patterns:
        match = re.search(
            pattern,
            log,
            flags=re.IGNORECASE,
        )

        if match:
            try:
                required_java = int(match.group(1))
            except (
                ValueError,
                IndexError,
            ):
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
        return {
            "failure_type": "unsupported_java_version",
            "required_java": required_java,
        }

    # --------------------------------------------------
    # Tests fail in the untouched/gold project
    # --------------------------------------------------

    if (
        "there were failing tests" in low
        or re.search(
            r"\d+\s+tests?\s+completed,\s+\d+\s+failed",
            low,
        )
        or "tests run:" in low
        and "failures:" in low
        and not re.search(
            r"failures:\s*0",
            low,
        )
    ):
        return {
            "failure_type": "baseline_test_failure",
            "required_java": required_java,
        }

    # --------------------------------------------------
    # Dependency/network problems
    # --------------------------------------------------

    dependency_markers = [
        "could not resolve",
        "could not download",
        "could not get resource",
        "could not get",
        "failed to read artifact descriptor",
        "dependency resolution",
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
        return {
            "failure_type": "dependency_resolution",
            "required_java": required_java,
        }

    # --------------------------------------------------
    # Plugin/configuration problems
    # --------------------------------------------------

    if (
        "plugin with id" in low
        and "not found" in low
    ) or (
        "plugin" in low
        and "could not be resolved" in low
    ):
        return {
            "failure_type": "plugin_resolution",
            "required_java": required_java,
        }

    if (
        "a problem occurred configuring" in low
        or "could not compile settings file" in low
        or "could not compile build file" in low
    ):
        return {
            "failure_type": "build_configuration",
            "required_java": required_java,
        }

    # --------------------------------------------------
    # Compilation
    # --------------------------------------------------

    if (
        "compilation failed" in low
        or "compilation failure" in low
        or "compilejava failed" in low
        or "compilation error" in low
        or re.search(
            r"\berror:\s",
            low,
        )
    ):
        return {
            "failure_type": "baseline_compile_failure",
            "required_java": required_java,
        }

    return {
        "failure_type": "build_failure",
        "required_java": required_java,
    }


# ---------------------------------------------------------------------
# Environment information
# ---------------------------------------------------------------------

def run_version_command(command):
    try:
        result = subprocess.run(
            command,
            text=True,
            stdout=subprocess.PIPE,
            stderr=subprocess.STDOUT,
            timeout=30,
        )

        return (
            result.stdout
            or ""
        ).strip()

    except Exception as exc:
        return f"ERROR: {exc}"


def environment_info():
    return {
        "java_home": os.environ.get("JAVA_HOME"),
        "java_version": run_version_command(
            ["java", "-version"]
        ),
        "python_version": run_version_command(
            ["python", "--version"]
        ),
    }


# ---------------------------------------------------------------------
# Single-project validation
# ---------------------------------------------------------------------

def validate_project(
    project_id: str,
    project_root: Path,
    logs_dir: Path,
    timeout_sec: int,
):
    build_system = detect_build_system(
        project_root
    )

    base_record = {
        "project_id": project_id,
        "project_path": str(project_root),
        "build_system": build_system,
        "valid": False,
        "failure_type": None,
        "required_java": None,
        "return_code": None,
        "duration_sec": None,
        "log_path": None,
    }

    if build_system == "unknown":
        base_record[
            "failure_type"
        ] = "unknown_build_system"

        return base_record

    if build_system in {
        "gradle_no_wrapper",
        "maven_no_wrapper",
    }:
        base_record[
            "failure_type"
        ] = "missing_wrapper"

        return base_record

    command = build_command(
        project_root,
        build_system,
    )

    logs_dir.mkdir(
        parents=True,
        exist_ok=True,
    )

    log_path = (
        logs_dir
        / f"{project_id}.log"
    )

    base_record[
        "log_path"
    ] = str(log_path)

    started = time.monotonic()

    print(
        f"[{project_id}] "
        f"{build_system}: "
        f"{' '.join(command)}"
    )

    try:
        result = subprocess.run(
            command,
            cwd=project_root,
            text=True,
            stdout=subprocess.PIPE,
            stderr=subprocess.STDOUT,
            timeout=timeout_sec,
            env=os.environ.copy(),
        )

        duration = (
            time.monotonic()
            - started
        )

        output = (
            result.stdout
            or ""
        )

        log_path.write_text(
            output,
            encoding="utf-8",
            errors="replace",
        )

        base_record[
            "return_code"
        ] = result.returncode

        base_record[
            "duration_sec"
        ] = round(
            duration,
            3,
        )

        if result.returncode == 0:
            base_record["valid"] = True
            base_record[
                "failure_type"
            ] = None

            print(
                f"[{project_id}] PASS "
                f"({duration:.1f}s)"
            )

            return base_record

        classification = classify_failure(
            output
        )

        base_record.update(
            classification
        )

        print(
            f"[{project_id}] FAIL "
            f"{base_record['failure_type']} "
            f"({duration:.1f}s)"
        )

        if base_record.get(
            "required_java"
        ):
            print(
                "  required Java: "
                f"{base_record['required_java']}"
            )

        return base_record

    except subprocess.TimeoutExpired as exc:
        duration = (
            time.monotonic()
            - started
        )

        output = ""

        if exc.stdout:
            if isinstance(
                exc.stdout,
                bytes,
            ):
                output += exc.stdout.decode(
                    "utf-8",
                    errors="replace",
                )
            else:
                output += exc.stdout

        if exc.stderr:
            if isinstance(
                exc.stderr,
                bytes,
            ):
                output += exc.stderr.decode(
                    "utf-8",
                    errors="replace",
                )
            else:
                output += exc.stderr

        log_path.write_text(
            output,
            encoding="utf-8",
            errors="replace",
        )

        base_record.update({
            "duration_sec":
                round(duration, 3),

            "failure_type":
                "timeout",
        })

        print(
            f"[{project_id}] TIMEOUT "
            f"after {timeout_sec}s"
        )

        return base_record

    except Exception as exc:
        duration = (
            time.monotonic()
            - started
        )

        message = (
            f"{type(exc).__name__}: "
            f"{exc}"
        )

        log_path.write_text(
            message,
            encoding="utf-8",
        )

        base_record.update({
            "duration_sec":
                round(duration, 3),

            "failure_type":
                "validator_error",

            "error":
                message,
        })

        print(
            f"[{project_id}] ERROR: "
            f"{message}"
        )

        return base_record


# ---------------------------------------------------------------------
# Main
# ---------------------------------------------------------------------

def main():
    parser = argparse.ArgumentParser(
        description=(
            "Validate JavaBench source projects "
            "before selecting benchmark tasks."
        )
    )

    parser.add_argument(
        "--projects-dir",
        default=str(
            DEFAULT_PROJECTS_DIR
        ),
    )

    parser.add_argument(
        "--output",
        default=str(
            DEFAULT_OUTPUT
        ),
    )

    parser.add_argument(
        "--logs-dir",
        default=str(
            DEFAULT_LOGS_DIR
        ),
    )

    parser.add_argument(
        "--timeout",
        type=int,
        default=DEFAULT_TIMEOUT_SEC,
        help=(
            "Per-project timeout in seconds "
            f"(default: {DEFAULT_TIMEOUT_SEC})"
        ),
    )

    parser.add_argument(
        "--only",
        default="",
        help=(
            "Optional comma-separated project IDs, "
            "for example PA01,PA03"
        ),
    )

    args = parser.parse_args()

    projects_dir = Path(
        args.projects_dir
    )

    output_path = Path(
        args.output
    )

    logs_dir = Path(
        args.logs_dir
    )

    if not projects_dir.is_dir():
        raise RuntimeError(
            "Projects directory does not exist: "
            f"{projects_dir}"
        )

    projects = discover_projects(
        projects_dir
    )

    only = {
        value.strip()
        for value
        in args.only.split(",")
        if value.strip()
    }

    if only:
        projects = [
            p
            for p
            in projects
            if p["project_id"]
            in only
        ]

    if not projects:
        raise RuntimeError(
            "No PAxx-Solution projects found."
        )

    print(
        f"Projects to validate: "
        f"{len(projects)}"
    )

    env_info = environment_info()

    print()
    print(
        "Java environment:"
    )

    print(
        env_info["java_version"]
    )

    if env_info["java_home"]:
        print(
            f"JAVA_HOME="
            f"{env_info['java_home']}"
        )

    print()

    results = []

    for index, project in enumerate(
        projects,
        start=1,
    ):
        print(
            f"=== "
            f"{index}/{len(projects)} "
            f"{project['project_id']} "
            f"==="
        )

        result = validate_project(
            project_id=
                project["project_id"],

            project_root=
                project["path"],

            logs_dir=
                logs_dir,

            timeout_sec=
                args.timeout,
        )

        results.append(
            result
        )

        print()

    valid = [
        r
        for r
        in results
        if r["valid"]
    ]

    invalid = [
        r
        for r
        in results
        if not r["valid"]
    ]

    failure_counts = {}

    for row in invalid:
        key = (
            row.get(
                "failure_type"
            )
            or "unknown"
        )

        failure_counts[key] = (
            failure_counts.get(
                key,
                0,
            )
            + 1
        )

    report = {
        "environment": env_info,

        "project_count":
            len(results),

        "valid_count":
            len(valid),

        "invalid_count":
            len(invalid),

        "valid_project_ids": [
            r["project_id"]
            for r
            in valid
        ],

        "invalid_project_ids": [
            r["project_id"]
            for r
            in invalid
        ],

        "failure_counts":
            failure_counts,

        "projects":
            results,
    }

    output_path.parent.mkdir(
        parents=True,
        exist_ok=True,
    )

    output_path.write_text(
        json.dumps(
            report,
            ensure_ascii=False,
            indent=2,
        ),
        encoding="utf-8",
    )

    print(
        "=============================="
    )

    print(
        f"PASS: {len(valid)}"
    )

    print(
        f"FAIL: {len(invalid)}"
    )

    if failure_counts:
        print(
            "\nFailure categories:"
        )

        for key, count in sorted(
            failure_counts.items()
        ):
            print(
                f"  {key}: {count}"
            )

    if valid:
        print(
            "\nValid projects:"
        )

        for row in valid:
            print(
                f"  {row['project_id']}"
            )

    if invalid:
        print(
            "\nInvalid projects:"
        )

        for row in invalid:
            extra = ""

            if row.get(
                "required_java"
            ):
                extra = (
                    " "
                    f"(requires Java "
                    f"{row['required_java']})"
                )

            print(
                f"  {row['project_id']}: "
                f"{row['failure_type']}"
                f"{extra}"
            )

    print(
        f"\nWrote: {output_path}"
    )

    print(
        f"Logs:  {logs_dir}"
    )


if __name__ == "__main__":
    main()
    