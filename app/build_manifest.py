import csv
import json
from pathlib import Path


ROOT = Path(__file__).resolve().parents[1]

repositories = json.loads(
    (ROOT / "repositories.json").read_text(
        encoding="utf-8"
    )
)

repo_by_project = {
    r["project_id"]: r
    for r in repositories
    if r.get("project_id")
}

rows = []

for path in sorted(
    (ROOT / "datasets" / "minimum-context").glob(
        "data-PA*.jsonl"
    )
):
    project_id = path.stem.replace("data-", "")

    repo = repo_by_project.get(
        project_id,
        {}
    )

    with path.open(
        "r",
        encoding="utf-8",
    ) as f:
        for line in f:
            if not line.strip():
                continue

            task = json.loads(line)

            code = task.get(
                "code",
                ""
            )

            rows.append({
                "task_id":
                    task.get("task_id"),

                "project_id":
                    project_id,

                "repo":
                    repo.get("full_name"),

                "commit_sha":
                    repo.get("commit_sha"),

                "target":
                    task.get("target"),

                "loc":
                    len(code.splitlines()),

                "todo_count":
                    code.count("// TODO"),
            })


out = ROOT / "datasets" / "manifest.csv"

with out.open(
    "w",
    encoding="utf-8",
    newline="",
) as f:
    writer = csv.DictWriter(
        f,
        fieldnames=[
            "task_id",
            "project_id",
            "repo",
            "commit_sha",
            "target",
            "loc",
            "todo_count",
        ],
    )

    writer.writeheader()
    writer.writerows(rows)


print(f"Wrote {len(rows)} rows to {out}")
