import argparse
from typing import Dict, List

from static_analyzer.class_compose_tool import get_todo_methods, replace_method
from util.io import extract_code, write_jsonl, stream_jsonl

def transform(
    samples: List[Dict],
    tasks: List[Dict],
    output_file: str,
):
    task_map = {}
    transformed_samples = []

    for task in tasks:
        task_map[task["task_id"]] = task

    for sample in samples:
        task = task_map[sample["task_id"]]
        target = sample["completion"]
        sample["completion"] = task["code"]

        todo_methods = get_todo_methods(task["code"])
        for todo_method in todo_methods:
            sample["completion"] = replace_method(
                source=sample["completion"], 
                target=extract_code(target), 
                method=todo_method["name"], 
                seq=todo_method["seq"]
            )
        transformed_samples.append(sample)

    write_jsonl(output_file, transformed_samples)

if __name__ == "__main__":
    parser = argparse.ArgumentParser()
    parser.add_argument("--sample_json", type=str, required=True)
    parser.add_argument("--task_json", type=str, required=True)
    parser.add_argument("--output", type=str, required=True)
    args = parser.parse_args()

    transform(
        samples=list(stream_jsonl(args.sample_json)),
        tasks=list(stream_jsonl(args.task_json)),
        output_file=args.sample_json,
    )
