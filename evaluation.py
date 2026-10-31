import json
import json.tool
import os
import sys
import click
import hashlib
from pathlib import Path

from app.test_env import TestEnv
from app.util.io import extract_code, stream_jsonl

def evaluate_test_suite(
    sample_file: str,
    output: str,
    *,
    mode: str = "full",
    test_file: str = "data/test.jsonl",
):
    samples = stream_jsonl(sample_file)
    grouped_samples = {}
    for sample in samples:
        target = sample["target"].rsplit(".")[0].replace("/", ".")
        if not grouped_samples.get(target):
            grouped_samples[target] = []
        grouped_samples[target].append(sample)

    tests = list(stream_jsonl(test_file))
    test_results = {}
    for test_index, test in enumerate(tests):
        if mode == "inc" and len(test["incremental_deps"]) == 0:
            continue
        if mode == "full":
            target_samples = [grouped_samples[dep] for dep in test["full_deps"]]
        elif mode == "inc":
            target_samples = [grouped_samples[dep] for dep in test["incremental_deps"]]
        else:
            raise ValueError(f"Unknown mode {mode}")
        
        if len(target_samples) == 0:
            continue

        result = []
        project_id, test_id = test["test_id"].split("/")
        max_k = min(len(l) for l in grouped_samples.values())
        test_env = TestEnv(
            root=f"/tmp/pre-coder/{os.getpid()}-{mode}/{project_id}-{test_id}",
            todo_src=f"projects/{project_id}",
            src=f"projects/{project_id}-Solution",
        )
        for k in range(max_k):
            print(f"[{os.getpid()}/{mode}] Running test {test_index + 1}/{len(tests)}: {test['test_id']} k={k + 1}")
            samples_to_replace = [target_sample[k] for target_sample in target_samples]

            has_todo = False
            can_replace = True
            for sample in samples_to_replace:
                code = extract_code(sample["completion"])
                replace_result = test_env.replace(sample["target"], code)

                has_todo = has_todo or replace_result["has_todo"]
                can_replace = can_replace and replace_result["can_replace"]

            compilable = len(test_env.compile()) == 0
            if compilable:
                (n_pass, n_total), _ = test_env.run_test(test["target"])
            else:
                n_pass, n_total = 0, 0

            result.append(
                dict(
                    test_id=test["test_id"],
                    compilable=compilable,
                    test_success=compilable and test_env.test_success,
                    skipped_tests=test_env.skipped_tests,
                    test_returncode=test_env.test_returncode,
                    n_pass=[n_pass, n_total],
                    has_todo=has_todo,
                    can_replace=can_replace,
                )
            )
        test_results[test["test_id"]] = result
        test_env.destory()


    if os.path.dirname(output):
        os.makedirs(os.path.dirname(output), exist_ok=True)
    with open(output, "w") as fp:
        fp.write((json.dumps(test_results, indent=4) + "\n"))

@click.command()
@click.argument("data")
@click.option('--output', required=True, help="Output file for evalution")
@click.option('--test', required=True, help="Test configuration for evaluation")
def test_wise(data: str, output: str, test: str):
    evaluate_test_suite(data, output, test_file=test, mode="full")


def evaluate_single_class(
    sample_file: str,
    output: str,
):
    result = []
    samples = list(stream_jsonl(sample_file))
    for sample_index, sample in enumerate(samples):
        print(f"[{os.getpid()}/single-class] Running class {sample_index + 1}/{len(samples)}: {sample['task_id']}")
        project_id = sample["task_id"].split("/")[0]
        test_env = TestEnv(
            root=f"/tmp/pre-coder/{os.getpid()}-single_class/{sample['task_id'].rsplit('.')[0]}",
            todo_src=f"projects/{project_id}",
            src=f"projects/{project_id}-Solution",
        )
        try:
            replace_result = test_env.replace(sample["target"], extract_code(sample["completion"]))
            compile_result = len(test_env.compile())
            test_result = (0, 0)
            test_log = ""
            if compile_result == 0:
                test_result, test_log = test_env.run_test(None)

            log_dir = Path(output).parent / (Path(output).stem + "_logs")
            log_dir.mkdir(parents=True, exist_ok=True)
            task_hash = hashlib.sha256(sample["task_id"].encode()).hexdigest()[:16]
            log_path = log_dir / f"{sample_index:04d}-{task_hash}.log"
            log_path.write_text(test_env.compile_log + "\n--- TEST ---\n" + test_log, encoding="utf-8")
            result.append(dict(
                task_id=sample["task_id"],
                compile_errors=compile_result,
                compile_returncodes=test_env.compile_returncodes,
                test_result=test_result,
                skipped_tests=test_env.skipped_tests,
                test_returncode=test_env.test_returncode,
                test_success=test_env.test_success,
                test_report_error=test_env.test_report_error,
                log_path=str(log_path.resolve()),
                **replace_result,
            ))
        finally:
            test_env.destory()
    if os.path.dirname(output):
        os.makedirs(os.path.dirname(output), exist_ok=True)
    with open(output, "w") as fp:
        fp.write((json.dumps(result, indent=4) + "\n"))


@click.command()
@click.argument("data")
@click.option('--output', required=True, help="Output file for evalution")
def class_wise(data: str, output: str):
    evaluate_single_class(data, output)

@click.command()
@click.argument("data")
@click.option('--output', required=True, help="Output file for evalution")
def project_wise(data: str, output: str):
    samples = stream_jsonl(data)
    grouped_samples = {}
    for sample in samples:
        target = sample["target"].rsplit(".")[0].replace("/", ".")
        if not grouped_samples.get(target):
            grouped_samples[target] = []
        grouped_samples[target].append(sample)

    project_id = sample["task_id"].split("/")[0]
    result = []
    max_k = min(len(l) for l in grouped_samples.values())
    test_env = TestEnv(
        root=f"/tmp/pre-coder/{os.getpid()}-project",
        todo_src=f"projects/{project_id}",
        src=f"projects/{project_id}-Solution",
    )
    for k in range(max_k):
        print(f"[{os.getpid()}/project] Running project {k + 1}...")
        samples_to_replace = [target_sample[k] for target_sample in grouped_samples.values()]

        has_todo = False
        can_replace = True
        for sample in samples_to_replace:
            code = extract_code(sample["completion"])
            replace_result = test_env.replace(sample["target"], code)

            has_todo = has_todo or replace_result["has_todo"]
            can_replace = can_replace and replace_result["can_replace"]

        compile_error = test_env.compile()
        if len(compile_error) == 0:
            (n_pass, n_total), log = test_env.run_test(None)
        else:
            n_pass, n_total = 0, 0

        result.append(
            dict(
                compile_error=len(compile_error),
                test_success=len(compile_error) == 0 and test_env.test_success,
                skipped_tests=test_env.skipped_tests,
                test_returncode=test_env.test_returncode,
                n_pass=[n_pass, n_total],
                has_todo=has_todo,
                can_replace=can_replace,
            )
        )
    test_env.destory()

    if os.path.dirname(output):
        os.makedirs(os.path.dirname(output), exist_ok=True)
    with open(output, "w") as fp:
        fp.write((json.dumps(result, indent=4) + "\n"))

@click.group()
def evaluation():
    pass

if __name__ == '__main__':
    evaluation.add_command(test_wise)
    evaluation.add_command(class_wise)
    evaluation.add_command(project_wise)
    evaluation()