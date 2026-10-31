from __future__ import annotations

import argparse
import json
import subprocess
import sys
from pathlib import Path
from typing import Any, Dict, Iterable, List, Optional, Tuple


BASE_DIR = Path(__file__).resolve().parent.parent if Path(__file__).resolve().parent.name == "app" else Path(__file__).resolve().parent
if str(BASE_DIR) not in sys.path:
    sys.path.insert(0, str(BASE_DIR))

DATASETS_DIR = BASE_DIR / "datasets"
PROJECTS_DIR = BASE_DIR / "projects"

DEFAULT_SUBSET = DATASETS_DIR / "subset100.json"
DEFAULT_SAMPLES = DATASETS_DIR / "_gold_samples.jsonl"
DEFAULT_SCORE = DATASETS_DIR / "_gold_score.json"
DEFAULT_REPORT = DATASETS_DIR / "gold_validation.json"


# -----------------------------------------------------------------------------
# CLI
# -----------------------------------------------------------------------------

def parse_args(argv: Optional[List[str]] = None) -> argparse.Namespace:
    parser = argparse.ArgumentParser(
        description=(
            "Validate selected JavaBench tasks with the gold Solution while preserving "
            "the exact Solution-project prefix used by evaluation.py."
        )
    )
    parser.add_argument(
        "--subset",
        type=Path,
        default=DEFAULT_SUBSET,
        help=f"selected task file (default: {DEFAULT_SUBSET})",
    )
    parser.add_argument(
        "--samples",
        type=Path,
        default=DEFAULT_SAMPLES,
        help=f"temporary gold samples JSONL (default: {DEFAULT_SAMPLES})",
    )
    parser.add_argument(
        "--score",
        type=Path,
        default=DEFAULT_SCORE,
        help=f"evaluation output JSON (default: {DEFAULT_SCORE})",
    )
    parser.add_argument(
        "--report",
        type=Path,
        default=DEFAULT_REPORT,
        help=f"validation report JSON (default: {DEFAULT_REPORT})",
    )
    parser.add_argument(
        "--python",
        default=sys.executable,
        help="Python executable used to run evaluation.py",
    )
    parser.add_argument(
        "--timeout",
        type=int,
        default=0,
        help="evaluation timeout in seconds; 0 disables timeout",
    )
    parser.add_argument(
        "--allow-zero-tests",
        action="store_true",
        help="allow successful test runs whose reports contain zero executed tests",
    )
    parser.add_argument(
        "--skip-evaluation",
        action="store_true",
        help="only run the exact-reconstruction precheck",
    )
    return parser.parse_args(argv)


# -----------------------------------------------------------------------------
# JSON helpers
# -----------------------------------------------------------------------------

def load_json(path: Path) -> Any:
    with path.open("r", encoding="utf-8") as f:
        return json.load(f)


def write_json(path: Path, value: Any) -> None:
    path.parent.mkdir(parents=True, exist_ok=True)
    with path.open("w", encoding="utf-8") as f:
        json.dump(value, f, ensure_ascii=False, indent=2)
        f.write("\n")


def write_jsonl(path: Path, rows: Iterable[Dict[str, Any]]) -> None:
    path.parent.mkdir(parents=True, exist_ok=True)
    with path.open("w", encoding="utf-8") as f:
        for row in rows:
            f.write(json.dumps(row, ensure_ascii=False) + "\n")


# -----------------------------------------------------------------------------
# Selected-task loading
# -----------------------------------------------------------------------------

def _task_id_from_item(item: Any) -> Optional[str]:
    if isinstance(item, str):
        value = item.strip()
        return value or None

    if not isinstance(item, dict):
        return None

    for key in ("task_id", "id", "task"):
        value = item.get(key)
        if isinstance(value, str) and value.strip():
            return value.strip()
        if isinstance(value, dict):
            nested = _task_id_from_item(value)
            if nested:
                return nested

    return None


def load_selected_task_ids(path: Path) -> List[str]:
    if not path.exists():
        raise FileNotFoundError(f"Subset file not found: {path}")

    data = load_json(path)

    candidates: Any = data
    if isinstance(data, dict):
        for key in (
            "tasks",
            "selected_tasks",
            "selected",
            "task_ids",
            "items",
        ):
            if key in data:
                candidates = data[key]
                break

    if isinstance(candidates, dict):
        possible_ids = []
        for key, value in candidates.items():
            nested = _task_id_from_item(value)
            possible_ids.append(nested or key)
        candidates = possible_ids

    if not isinstance(candidates, list):
        raise ValueError(
            f"Unsupported subset format in {path}. Expected a list or an object "
            "containing tasks/selected_tasks/selected/task_ids."
        )

    task_ids: List[str] = []
    seen = set()
    for item in candidates:
        task_id = _task_id_from_item(item)
        if not task_id:
            continue
        if task_id not in seen:
            seen.add(task_id)
            task_ids.append(task_id)

    if not task_ids:
        raise ValueError(f"No task_id values found in {path}")

    return task_ids


# -----------------------------------------------------------------------------
# JavaBench path/reconstruction helpers
# -----------------------------------------------------------------------------

def split_task_id(task_id: str) -> Tuple[str, str]:
    if "/" not in task_id:
        raise ValueError(f"Invalid task_id (expected PAxx/path): {task_id}")
    project_id, target = task_id.split("/", 1)
    if not project_id or not target:
        raise ValueError(f"Invalid task_id: {task_id}")
    return project_id, target


def resolve_code_path(root: Path, target: str) -> Path:
    normalized = target.replace("\\", "/")

    direct = root / normalized
    if direct.exists():
        return direct

    if not (
        normalized.startswith("src/main/java/")
        or "/src/main/java/" in normalized
    ):
        conventional = root / "src" / "main" / "java" / normalized
        if conventional.exists():
            return conventional

    return direct


def read_text(path: Path) -> str:
    return path.read_text(encoding="utf-8")


def first_public_offset(source: str) -> int:
    return source.find("public")


def reconstruct_exactly_as_evaluator(solution_source: str, completion_source: str) -> Tuple[Optional[str], str]:
    solution_start = first_public_offset(solution_source)
    completion_start = first_public_offset(completion_source)

    if solution_start < 0:
        return None, "solution_has_no_public_token"
    if completion_start < 0:
        return None, "completion_has_no_public_token"

    return solution_source[:solution_start] + completion_source[completion_start:], "ok"


def line_ending_normalized(text: str) -> str:
    return text.replace("\r\n", "\n").replace("\r", "\n")


def first_difference(a: str, b: str) -> Dict[str, Any]:
    aa = line_ending_normalized(a).splitlines()
    bb = line_ending_normalized(b).splitlines()
    limit = max(len(aa), len(bb))

    for i in range(limit):
        left = aa[i] if i < len(aa) else None
        right = bb[i] if i < len(bb) else None
        if left != right:
            return {
                "line": i + 1,
                "reconstructed": left,
                "solution": right,
            }

    return {}


def precheck_task(task_id: str) -> Dict[str, Any]:
    project_id, target = split_task_id(task_id)

    todo_root = PROJECTS_DIR / project_id
    solution_root = PROJECTS_DIR / f"{project_id}-Solution"

    todo_path = resolve_code_path(todo_root, target)
    solution_path = resolve_code_path(solution_root, target)

    result: Dict[str, Any] = {
        "task_id": task_id,
        "project_id": project_id,
        "target": target,
        "todo_path": str(todo_path),
        "solution_path": str(solution_path),
        "precheck_ok": False,
        "precheck_reason": None,
    }

    if not todo_path.is_file():
        result["precheck_reason"] = "todo_file_missing"
        return result

    if not solution_path.is_file():
        result["precheck_reason"] = "solution_file_missing"
        return result

    solution_source = read_text(solution_path)

    reconstructed, reason = reconstruct_exactly_as_evaluator(solution_source, solution_source)
    if reconstructed is None:
        result["precheck_reason"] = reason
        return result

    reconstructed_norm = line_ending_normalized(reconstructed)
    solution_norm = line_ending_normalized(solution_source)

    if reconstructed_norm != solution_norm:
        result.update(
            {
                "precheck_reason": "evaluator_cannot_reconstruct_solution_exactly",
                "first_difference": first_difference(reconstructed_norm, solution_norm),
            }
        )
        return result

    result["precheck_ok"] = True
    result["precheck_reason"] = "exact_reconstruction"
    result["solution_source"] = solution_source
    return result


# -----------------------------------------------------------------------------
# Gold sample/evaluation helpers
# -----------------------------------------------------------------------------

def fenced_java(source: str) -> str:
    source = source.rstrip("\n")
    return f"```java\n{source}\n```\n"


def make_gold_sample(precheck: Dict[str, Any]) -> Dict[str, Any]:
    return {
        "task_id": precheck["task_id"],
        "target": precheck["target"],
        "completion": fenced_java(precheck["solution_source"]),
    }


def run_evaluation(
    python_executable: str,
    samples_path: Path,
    score_path: Path,
    timeout_sec: int,
) -> None:
    evaluation_py = BASE_DIR / "evaluation.py"
    if not evaluation_py.is_file():
        raise FileNotFoundError(f"evaluation.py not found: {evaluation_py}")

    from app.test_env import check_todo
    from app.util.io import extract_code

    with samples_path.open(encoding="utf-8") as stream:
        samples = [json.loads(line) for line in stream if line.strip()]
    representatives = {}
    for sample in samples:
        project_id, target = split_task_id(sample["task_id"])
        source = read_text(resolve_code_path(PROJECTS_DIR / f"{project_id}-Solution", target))
        reconstructed, reason = reconstruct_exactly_as_evaluator(source, extract_code(sample["completion"]))
        if reconstructed is None or line_ending_normalized(reconstructed).rstrip() != line_ending_normalized(source).rstrip():
            raise ValueError(f"Gold sample does not reconstruct Solution: {sample['task_id']} ({reason})")
        representatives.setdefault(project_id, sample)
    project_samples = samples_path.with_name(samples_path.stem + "_projects.jsonl")
    project_score = score_path.with_name(score_path.stem + "_projects.json")
    write_jsonl(project_samples, representatives.values())

    command = [
        python_executable,
        str(evaluation_py),
        "class-wise",
        str(project_samples),
        "--output",
        str(project_score),
    ]

    print("\nRunning gold evaluation:")
    print(" ".join(command))
    print()

    kwargs: Dict[str, Any] = {
        "cwd": str(BASE_DIR),
        "check": True,
    }
    if timeout_sec > 0:
        kwargs["timeout"] = timeout_sec

    subprocess.run(command, **kwargs)
    scores = {split_task_id(row["task_id"])[0]: row for row in load_json(project_score)}
    expanded = []
    for sample in samples:
        project_id, target = split_task_id(sample["task_id"])
        row = dict(scores[project_id])
        row["build_representative_task_id"] = row["task_id"]
        row["task_id"] = sample["task_id"]
        todo = read_text(resolve_code_path(PROJECTS_DIR / project_id, target))
        row["has_todo"] = check_todo(todo, extract_code(sample["completion"]))
        expanded.append(row)
    write_json(score_path, expanded)


def classify_evaluation_result(row: Dict[str, Any], allow_zero_tests: bool) -> Tuple[bool, str]:
    if not row.get("can_replace", False):
        return False, "cannot_replace"

    if row.get("has_todo", False):
        return False, "gold_still_has_todo"

    compile_errors = row.get("compile_errors")
    if compile_errors is None:
        return False, "missing_compile_result"
    if int(compile_errors) != 0:
        return False, "compile_failed"

    returncodes = row.get("compile_returncodes")
    if not returncodes:
        return False, "missing_build_exit_status"
    if any(code != 0 for code in returncodes):
        return False, "compile_failed"
    if row.get("test_returncode") is None:
        return False, "missing_test_exit_status"
    if row["test_returncode"] != 0:
        return False, "test_command_failed"
    if row.get("test_report_error"):
        return False, "test_report_error"

    test_result = row.get("test_result")
    if not isinstance(test_result, list) or len(test_result) != 2:
        return False, "missing_test_result"

    passed, total = test_result
    try:
        passed = int(passed)
        total = int(total)
    except (TypeError, ValueError):
        return False, "invalid_test_result"

    if total < 0 or passed < 0 or passed > total:
        return False, "invalid_test_result"

    if total == 0:
        if allow_zero_tests:
            return True, "compile_ok_zero_tests_allowed"
        return False, "zero_tests"

    if passed != total:
        return False, "tests_failed"

    return True, "gold_pass"


# -----------------------------------------------------------------------------
# Main
# -----------------------------------------------------------------------------

def main(argv: Optional[List[str]] = None) -> None:
    args = parse_args(argv)

    subset_path = args.subset if args.subset.is_absolute() else BASE_DIR / args.subset
    samples_path = args.samples if args.samples.is_absolute() else BASE_DIR / args.samples
    score_path = args.score if args.score.is_absolute() else BASE_DIR / args.score
    report_path = args.report if args.report.is_absolute() else BASE_DIR / args.report

    selected_task_ids = load_selected_task_ids(subset_path)
    print(f"Selected tasks: {len(selected_task_ids)}")

    prechecks: List[Dict[str, Any]] = []
    gold_samples: List[Dict[str, Any]] = []

    for task_id in selected_task_ids:
        result = precheck_task(task_id)
        prechecks.append(result)
        if result["precheck_ok"]:
            gold_samples.append(make_gold_sample(result))

    precheck_ok_count = len(gold_samples)
    precheck_bad_count = len(prechecks) - precheck_ok_count

    print(f"Exact reconstruction: {precheck_ok_count}")
    print(f"Rejected before build: {precheck_bad_count}")

    for row in prechecks:
        if row["precheck_ok"]:
            continue
        print(f"  - {row['task_id']}: {row['precheck_reason']}")
        missing = row.get("missing_imports") or []
        if missing:
            preview = ", ".join(missing[:3])
            suffix = " ..." if len(missing) > 3 else ""
            print(f"      missing imports: {preview}{suffix}")

    write_jsonl(samples_path, gold_samples)
    print(f"Gold samples prepared: {len(gold_samples)}")

    report: Dict[str, Any] = {
        "validation_version": 2,
        "subset": str(subset_path),
        "selected_count": len(selected_task_ids),
        "precheck": {
            "exact_reconstruction": precheck_ok_count,
            "rejected": precheck_bad_count,
            "tasks": [
                {k: v for k, v in row.items() if k != "solution_source"}
                for row in prechecks
            ],
        },
        "evaluation": None,
        "valid_task_ids": [],
        "invalid_task_ids": [
            row["task_id"] for row in prechecks if not row["precheck_ok"]
        ],
    }

    if args.skip_evaluation:
        report["valid_task_ids"] = [
            row["task_id"] for row in prechecks if row["precheck_ok"]
        ]
        write_json(report_path, report)
        print(f"Wrote: {report_path}")
        return

    if not gold_samples:
        write_json(report_path, report)
        raise SystemExit("No tasks survived exact-reconstruction precheck.")

    run_evaluation(
        python_executable=args.python,
        samples_path=samples_path,
        score_path=score_path,
        timeout_sec=args.timeout,
    )

    raw_scores = load_json(score_path)
    if not isinstance(raw_scores, list):
        raise ValueError(f"Unexpected evaluation output format in {score_path}")

    score_by_task = {
        row.get("task_id"): row
        for row in raw_scores
        if isinstance(row, dict) and row.get("task_id")
    }

    evaluation_rows: List[Dict[str, Any]] = []
    valid_task_ids: List[str] = []
    invalid_task_ids: List[str] = list(report["invalid_task_ids"])

    for sample in gold_samples:
        task_id = sample["task_id"]
        score = score_by_task.get(task_id)

        if score is None:
            evaluation_rows.append(
                {
                    "task_id": task_id,
                    "valid": False,
                    "reason": "missing_evaluation_result",
                }
            )
            invalid_task_ids.append(task_id)
            continue

        valid, reason = classify_evaluation_result(score, args.allow_zero_tests)
        evaluation_rows.append(
            {
                "task_id": task_id,
                "valid": valid,
                "reason": reason,
                "compile_errors": score.get("compile_errors"),
                "test_result": score.get("test_result"),
                "skipped_tests": score.get("skipped_tests", 0),
                "has_todo": score.get("has_todo"),
                "can_replace": score.get("can_replace"),
                "compile_returncodes": score.get("compile_returncodes"),
                "test_returncode": score.get("test_returncode"),
                "test_report_error": score.get("test_report_error"),
                "log_path": score.get("log_path"),
                "build_representative_task_id": score.get("build_representative_task_id"),
            }
        )

        if valid:
            valid_task_ids.append(task_id)
        else:
            invalid_task_ids.append(task_id)

    invalid_seen = set()
    invalid_task_ids = [
        x for x in invalid_task_ids
        if not (x in invalid_seen or invalid_seen.add(x))
    ]

    report["evaluation"] = {
        "evaluated": len(evaluation_rows),
        "valid": len(valid_task_ids),
        "invalid": len(evaluation_rows) - len(valid_task_ids),
        "tasks": evaluation_rows,
    }
    report["valid_task_ids"] = valid_task_ids
    report["invalid_task_ids"] = invalid_task_ids

    write_json(report_path, report)

    print()
    print(f"Gold valid:   {len(valid_task_ids)}")
    print(f"Gold invalid: {len(invalid_task_ids)}")
    print(f"Wrote: {report_path}")

    if invalid_task_ids:
        print()
        print(
            "Some selected tasks failed gold validation. Inspect build/test logs and "
            "fix the environment or evaluator before replacing tasks in the subset."
        )


if __name__ == "__main__":
    main()
