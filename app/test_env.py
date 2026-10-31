from io import StringIO
import subprocess
import shutil
import os
import re
from pathlib import Path
import xml.etree.ElementTree as ET
import pandas as pd

from typing import List, Optional
from app.schema.schemas import CompilerError
from app.static_analyzer.class_compose_tool import get_todo_methods


def to_code_path(root, code_path):
    normalized = code_path.replace("\\", "/")
    if normalized.startswith("src/main/java/") or "/src/main/java/" in normalized:
        return os.path.join(root, code_path)
    return os.path.join(root, "src", "main", "java", code_path)

def detect_build_tool(root: str):
    if os.path.exists(os.path.join(root, "gradlew")) or os.path.exists(os.path.join(root, "build.gradle")) or os.path.exists(os.path.join(root, "build.gradle.kts")):
        return "gradle"
    if os.path.exists(os.path.join(root, "mvnw")) or os.path.exists(os.path.join(root, "pom.xml")):
        return "maven"
    raise FileNotFoundError(f"Cannot detect Gradle or Maven project in {root}")

def build_runner(root: str, build_tool: str):
    if build_tool == "gradle":
        return "./gradlew" if os.path.exists(os.path.join(root, "gradlew")) else "gradle"
    if build_tool == "maven":
        return "./mvnw" if os.path.exists(os.path.join(root, "mvnw")) else "mvn"
    raise ValueError(f"Unknown build tool: {build_tool}")

def _has_placeholder(node):
    if node.type == "line_comment":
        return re.fullmatch(rb"//\s*TODO\s*", node.text) is not None
    return any(_has_placeholder(child) for child in node.children)


def check_todo(todo_code, com_code):
    completions = {
        (method["name"], method["seq"]): method
        for method in get_todo_methods(com_code, todo_only=False)
    }
    for method in get_todo_methods(todo_code):
        if not _has_placeholder(method["node"]):
            continue
        completion = completions.get((method["name"], method["seq"]))
        if completion is None:
            return True
        body = completion["node"]
        if _has_placeholder(body):
            return True
    return False


def test_report_paths(root):
    root = Path(root)
    return sorted(set(root.glob("**/target/surefire-reports/TEST-*.xml")) |
                  set(root.glob("**/build/test-results/**/TEST-*.xml")))


def read_test_report_counts(paths):
    passed = total = skipped_total = 0
    for path in paths:
        report = ET.parse(path).getroot()
        suites = [report] if report.tag == "testsuite" else report.findall(".//testsuite")
        # Aggregate wrappers can repeat counts of their child suites.
        for suite in suites:
            if suite.find("testsuite") is not None:
                continue
            count = int(suite.attrib["tests"])
            failures, errors, skipped = [int(suite.get(key, "0")) for key in ("failures", "errors", "skipped")]
            unsuccessful = failures + errors + skipped
            if min(count, failures, errors, skipped) < 0 or unsuccessful > count:
                raise ValueError(f"Invalid test counts in {path}")
            total += count - skipped
            skipped_total += skipped
            passed += count - unsuccessful
    return passed, total, skipped_total


def read_test_reports(paths):
    return read_test_report_counts(paths)[:2]

class TestEnv:
    def __init__(self, root: str, todo_src: str, src: str) -> None:
        self.root = root
        self.todo_src = todo_src
        self.src = src
        shutil.copytree(src, root)
        self.build_tool = detect_build_tool(self.root)
        self.runner = build_runner(self.root, self.build_tool)
        self.compile_returncodes = []
        self.test_returncode = None
        self.test_report_error = None
        self.test_success = False
        self.skipped_tests = 0
        self.compile_log = ""

    def destory(self):
        shutil.rmtree(self.root)

    def replace(self, target: str, content: str):
        code_path = to_code_path(self.root, target)
        todo_code_path = to_code_path(self.todo_src, target)
        solution_code_path = to_code_path(self.src, target)
        if not os.path.exists(code_path):
            print(f"warning: replace non-existed file {code_path}")
        # with open(code_path, "w") as fp:
        #     fp.write(content)

        with open(todo_code_path, "r") as fp:
            todo_content = fp.read()
        with open(solution_code_path, "r") as fp:
            solution_content = fp.read()
        solution_content_start = solution_content.find("public")
        content_start = content.find("public")

        if solution_content_start == -1 or content_start == -1:
            return {
                "has_todo": False,
                "can_replace": False
            }

        with open(code_path, "w") as fp:
            fp.write(solution_content[:solution_content_start] + content[content_start:])

        return {
            "has_todo": check_todo(todo_content, content),
            "can_replace": True
        }

    def recover(self, target: str):
        shutil.copy2(to_code_path(self.src, target), to_code_path(self.root, target))

    def compile(self) -> List[CompilerError]:
        if self.build_tool == "gradle":
            commands = [[self.runner, "compileJava", "compileTestJava", "--rerun-tasks"]]
        else:
            commands = [[self.runner, "-B", "-DskipTests=true", "-Dmaven.test.skip=false", "test-compile"]]

        self.compile_returncodes = []
        self.compile_log = ""
        self.test_returncode = None
        self.test_success = False
        self.skipped_tests = 0
        self.test_report_error = None
        errors = []
        for command in commands:
            try:
                process = subprocess.run(command, cwd=self.root, stdout=subprocess.PIPE,
                                         stderr=subprocess.STDOUT, text=True, errors="replace")
                output = process.stdout
                returncode = process.returncode
            except OSError as exc:
                output, returncode = str(exc), -1
            self.compile_returncodes.append(returncode)
            self.compile_log += output
            if returncode:
                try:
                    parsed = CompilerError.parse_errors(err_string=output)
                except (ValueError, IndexError):
                    parsed = []
                errors.extend(parsed or [CompilerError(
                    source=self.root, line=0,
                    message=f"Build failed (exit {returncode})", content=output,
                )])
        return errors

    def run_test(self, target: Optional[str]):
        self.test_returncode = None
        self.test_report_error = None
        self.test_success = False
        self.skipped_tests = 0
        # Copies of Solution projects may already contain reports. Only this run counts.
        for path in test_report_paths(self.root):
            path.unlink()

        if self.build_tool == "gradle":
            args = [self.runner, "test", "--rerun-tasks"]
            if target:
                args += ["--tests", target]
        else:
            args = [self.runner, "-B", "-DskipTests=false", "-Dmaven.test.skip=false", "test"]
            if target:
                args += [f"-Dtest={target}"]
        try:
            process = subprocess.run(args, cwd=self.root, stdout=subprocess.PIPE,
                                     stderr=subprocess.STDOUT, text=True, errors="replace")
            output = process.stdout
            self.test_returncode = process.returncode
        except OSError as exc:
            output = str(exc)
            self.test_returncode = -1

        paths = test_report_paths(self.root)
        result = (0, 0)
        if paths:
            try:
                passed, total, self.skipped_tests = read_test_report_counts(paths)
                result = (passed, total)
            except (ET.ParseError, OSError, ValueError, KeyError) as exc:
                self.test_report_error = str(exc)
        else:
            # Legacy JavaBench Gradle projects print an explicit suite summary.
            summaries = re.findall(
                r"Results: \w+ \((\d+) tests, (\d+) successes, \d+ failures, (\d+) skipped\)",
                output,
            ) if self.build_tool == "gradle" else []
            if summaries:
                self.skipped_tests = sum(int(skipped) for _, _, skipped in summaries)
                result = (sum(int(successes) for _, successes, _ in summaries),
                          sum(int(total) for total, _, _ in summaries) - self.skipped_tests)
            else:
                self.test_report_error = "No fresh test reports found"
        self.test_success = (self.test_returncode == 0 and self.test_report_error is None
                             and result[1] > 0 and result[0] == result[1])
        return result, output

    def run_dep_metrics(self):
        source_dir = "src/main/java"
        process = subprocess.Popen(
            [
                "java", "-jar", "./lib/java-sellotape.jar", "dep-metric",
                "--generated-project", os.path.join(self.root, source_dir),
                "--solution-project", os.path.join(self.src, source_dir),
                "--todo-project", os.path.join(self.todo_src, source_dir),
            ],
            stdout=subprocess.PIPE,
            stderr=subprocess.PIPE,
            env={"JAVA_TOOL_OPTIONS": "-Duser.language=en", "JAVA_HOME": "/usr/lib/jvm/default-runtime" },
        )
        out, err = process.communicate()
        out = out.decode()
        print(err.decode())
        
        out = pd.read_csv(StringIO(out), header="infer", sep="\s+")
        return out.iloc[0]
