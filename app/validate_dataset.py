import argparse
import json
from pathlib import Path

ROOT = Path(__file__).resolve().parents[1]

def read_jsonl(path):
    with path.open("r", encoding="utf-8") as f:
        for line in f:
            line = line.strip()
            if line:
                yield json.loads(line)


def main():
    parser = argparse.ArgumentParser()
    parser.add_argument(
        "--dataset-dir",
        default=str(ROOT / "datasets" / "minimum-context"),
    )

    parser.add_argument(
        "--projects-dir",
        default=str(ROOT / "projects"),
    )

    parser.add_argument(
        "--out",
        default=str(ROOT / "datasets" / "validation.json"),
    )

    args = parser.parse_args()

    dataset_dir = Path(args.dataset_dir)
    projects_dir = Path(args.projects_dir)

    valid = []
    invalid = []

    for dataset_path in sorted(dataset_dir.glob("data-PA*.jsonl")):
        project_id = dataset_path.stem.replace("data-", "")

        solution_root = projects_dir / f"{project_id}-Solution"

        for task in read_jsonl(dataset_path):
            problems = []

            task_id = task.get("task_id")
            target = task.get("target")
            code = task.get("code", "")
            code_context = task.get("code_context", "")

            if not task_id:
                problems.append("missing_task_id")

            if not target:
                problems.append("missing_target")

            if not code.strip():
                problems.append("empty_code")

            if code_context:
                problems.append("non_empty_context")

            if target:
                gold_file = solution_root / target

                if not gold_file.is_file():
                    problems.append("gold_target_missing")
                else:
                    gold_code = gold_file.read_text(
                        encoding="utf-8",
                        errors="replace",
                    )

                    if code == gold_code:
                        problems.append("skeleton_equals_gold")

                    if "// TODO" not in code:
                        problems.append("no_todo_in_skeleton")

            record = {
                "task_id": task_id,
                "project_id": project_id,
                "target": target,
                "problems": problems,
            }

            if problems:
                invalid.append(record)
            else:
                valid.append(record)

    result = {
        "valid_count": len(valid),
        "invalid_count": len(invalid),
        "valid": valid,
        "invalid": invalid,
    }

    out = Path(args.out)
    out.parent.mkdir(parents=True, exist_ok=True)

    out.write_text(
        json.dumps(
            result,
            ensure_ascii=False,
            indent=2,
        ),
        encoding="utf-8",
    )

    print(f"Valid tasks:   {len(valid)}")
    print(f"Invalid tasks: {len(invalid)}")
    print(f"Wrote: {out}")


if __name__ == "__main__":
    main()
