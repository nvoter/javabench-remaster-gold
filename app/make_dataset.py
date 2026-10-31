import argparse
import glob
import importlib.util
import os
from pathlib import Path
from typing import Any, Dict

from tqdm import tqdm

from app.util.io import read_code, write_jsonl

def export_problems(project_id: str, project: Dict[str, Any], args: Dict[str, str]):
    def get_code_context(context_root: str, related_source_list: list[str]):
        context = []
        visited = {}

        for source in related_source_list:
            files = glob.glob(os.path.join(context_root, source))
            if len(files) == 0:
                raise FileNotFoundError(f"Cannot find {source} in {context_root}")
            for file in files:
                if visited.get(file) == True:
                    continue
                visited[file] = True
                context.append(read_code(file))

        return "\n".join(context)

    task_data = []
    for task_name, task in tqdm(project["tasks"].items()):
        task_id = f"{project_id}/{task_name}"
        code = read_code(os.path.join(project["todo_root"], task["target"]))

        if args.mode == "stripped-context":
            code_context = get_code_context(
                context_root=project["context_root"],
                related_source_list=task["related_source_list"],
            )
        elif args.mode == "full-context":
            code_context = get_code_context(
                context_root=project["todo_root"],
                related_source_list=task["related_source_list"],
            )[:args.max_context_len]
        elif args.mode == "no-context":
            code_context = ""
        task_data.append(
            dict(
                task_id=task_id,
                target=task["target"],
                code=code,
                code_context=code_context,
            )
        )

    write_jsonl(os.path.join(args.out_dir, f"data-{project_id}.jsonl"), task_data)

def iter_descriptor_projects(descriptor_dir: str):
    descriptor_root = Path(descriptor_dir)
    if not descriptor_root.is_dir():
        raise FileNotFoundError(f"Descriptor directory not found: {descriptor_root}")

    for descriptor_path in sorted(descriptor_root.glob("*.py")):
        if descriptor_path.name == "__init__.py":
            continue

        module_name = f"javabench_descriptor_{descriptor_path.stem}"
        spec = importlib.util.spec_from_file_location(module_name, descriptor_path)
        if spec is None or spec.loader is None:
            raise ImportError(f"Cannot load descriptor {descriptor_path}")

        module = importlib.util.module_from_spec(spec)
        spec.loader.exec_module(module)

        if not hasattr(module, "project"):
            raise AttributeError(f"Descriptor {descriptor_path} does not define project")

        yield descriptor_path.stem.upper(), module.project

def project_roots_exist(project: Dict[str, Any], mode: str) -> bool:
    if not os.path.isdir(project["todo_root"]):
        return False

    if mode == "stripped-context" and not os.path.isdir(project["context_root"]):
        return False

    return True

if __name__ == "__main__":
    parser = argparse.ArgumentParser()
    parser.add_argument("--out-dir", type=str, required=True)
    parser.add_argument("--descriptor-dir", type=str, default="datasets/descriptor")
    parser.add_argument("--skip-missing-projects", action="store_true")
    parser.add_argument("--max-context-len", type=int, default=8192)
    parser.add_argument(
        "--mode", 
        type=str,
        choices=["stripped-context", "full-context", "no-context"],
        default="stripped-context",
    )
    args = parser.parse_args()

    os.makedirs(args.out_dir, exist_ok=True)

    for project_id, project in iter_descriptor_projects(args.descriptor_dir):
        if args.skip_missing_projects and not project_roots_exist(project, args.mode):
            print(f"Skipping {project_id}: project roots are missing")
            continue

        export_problems(project_id, project, args)
