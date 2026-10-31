from __future__ import annotations

import re
from typing import List
from langchain_core.messages import BaseMessage


class GenerateResult:
    def __init__(
        self,
        *,
        messages: List[BaseMessage],
        completed_code: str,
        summary_token_count: int,
        total_token_count: int,
    ) -> None:
        self.messages = messages
        self.completed_code = completed_code
        self.summary_token_count = summary_token_count
        self.total_token_count = total_token_count


class TaskDescriptor:
    def __init__(
        self,
        name: str,
        *,
        target: str,
        related_source_list: List[str],
    ):
        self.name = name
        self.target = target
        self.related_source_list = related_source_list


class ProjectDescriptor:
    def __init__(
        self,
        *,
        todo_root: str,
        context_root: str,
        requirements: str,
        tasks: List[TaskDescriptor],
    ):
        self.todo_root = todo_root
        self.context_root = context_root
        self.requirements = requirements
        self.tasks = tasks


class CompilerError:
    def __init__(self, source: str, line: int, message: str, content: str):
        self.source = source
        self.line = line
        self.message = message
        self.content = content

    def equals(self, error: CompilerError):
        return (
            self.source == error.source
            and self.message == error.message
            and self.content == error.content
        )

    @classmethod
    def parse(cls, error_string: str):
        lines = error_string.split("\n")
        header = lines[0]
        maven_match = re.match(r"(.+):\[(\d+),\d+\]\s*(.*)", header)
        if maven_match:
            source = maven_match.group(1)
            line = int(maven_match.group(2))
            message = maven_match.group(3).strip()
        else:
            source = header.split(":")[0]
            line = int(header.split(":")[1])
            message = ":".join(header.split(":")[2:]).strip()
        content = "\n".join(lines[1:])
        return cls(source=source, line=line, message=message, content=content)

    @classmethod
    def parse_errors(cls, err_string: str):
        err_lines = [
            re.sub(r"^\[ERROR\]\s*", "", line)
            for line in err_string.split("\n")
        ]
        start_line = None
        end_line = None
        for i, line in enumerate(err_lines):
            if line.startswith("/") and start_line is None:
                start_line = i
            if re.search("\d+ errors?", line) and end_line is None:
                end_line = i
            if start_line is not None and end_line is not None:
                break
        if start_line is None or end_line is None:
            return []

        merged = []
        buffer = []
        for line in err_lines[start_line:end_line]:
            if line.startswith("/") and buffer:
                merged.append("\n".join(buffer))
                buffer = []
            buffer.append(line)
        if buffer:
            merged.append("\n".join(buffer))
        return [cls.parse(e) for e in merged]
