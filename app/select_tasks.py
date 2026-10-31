import argparse
import json
import random
from collections import defaultdict
from pathlib import Path


def main():
    parser = argparse.ArgumentParser()

    parser.add_argument(
        "--validation",
        default="datasets/validation.json",
    )

    parser.add_argument(
        "--count",
        type=int,
        default=100,
    )

    parser.add_argument(
        "--max-per-project",
        type=int,
        default=25,
    )

    parser.add_argument(
        "--seed",
        type=int,
        default=42,
    )

    parser.add_argument(
        "--out",
        default="datasets/subset100.json",
    )

    args = parser.parse_args()

    data = json.loads(
        Path(args.validation).read_text(
            encoding="utf-8"
        )
    )

    valid = data["valid"]

    by_project = defaultdict(list)

    for task in valid:
        by_project[task["project_id"]].append(task)

    rng = random.Random(args.seed)

    for tasks in by_project.values():
        rng.shuffle(tasks)

    selected = []

    # round-robin, чтобы один проект не забрал весь датасет
    while len(selected) < args.count:
        added = False

        for project_id in sorted(by_project):
            already = sum(
                1
                for t in selected
                if t["project_id"] == project_id
            )

            if already >= args.max_per_project:
                continue

            if not by_project[project_id]:
                continue

            selected.append(
                by_project[project_id].pop()
            )

            added = True

            if len(selected) >= args.count:
                break

        if not added:
            break

    if len(selected) < args.count:
        raise RuntimeError(
            f"Requested {args.count} tasks, "
            f"but only {len(selected)} could be selected "
            f"with max_per_project={args.max_per_project}"
        )

    result = {
        "count": len(selected),
        "seed": args.seed,
        "max_per_project": args.max_per_project,
        "task_ids": [
            t["task_id"]
            for t in selected
        ],
    }

    out = Path(args.out)

    out.write_text(
        json.dumps(
            result,
            ensure_ascii=False,
            indent=2,
        ),
        encoding="utf-8",
    )

    print(f"Selected: {len(selected)}")
    print(f"Wrote: {out}")

    counts = defaultdict(int)

    for task in selected:
        counts[task["project_id"]] += 1

    for project_id in sorted(counts):
        print(
            f"{project_id}: {counts[project_id]}"
        )


if __name__ == "__main__":
    main()
