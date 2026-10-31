from app.rpc.pre_coder_pb2 import Range


def get_classname_from_path(path: str):
    return path.split("/")[-1].split(".")[0]


def substring_by_range(s: str, rng: Range):
    lines = s.split("\n")
    return "\n".join(lines[rng.start.line - 1 : rng.end.line])


def str_to_lines(s: str) -> str:
    return [e + "\n" for e in s.split("\n")]


def split_import(code: str) -> (str, str):
    import_lines = []
    other_lines = []
    for line in code.split("\n"):
        stripped = line.strip()
        if stripped.startswith("import "):
            import_lines.append(line)
        else:
            other_lines.append(line)
    return "\n".join(import_lines), "\n".join(other_lines)
