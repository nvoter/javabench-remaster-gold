import os
from typing import List, Dict
from tree_sitter import Language, Parser
from tree_sitter_languages import get_parser, get_language


JAVA_LANGUAGE = get_language("java")
QUERY_METHOD = JAVA_LANGUAGE.query("""
(constructor_declaration) @constructor
(method_declaration) @method
""")
QUERY_COMMENT = JAVA_LANGUAGE.query("""
(line_comment) @comment
""")

parser = get_parser("java")

def get_todo_methods(source: str, todo_only=True, error_tolerance=True) -> List[Dict]:
    tree = parser.parse(bytes(source, "utf8"))
    captures = QUERY_METHOD.captures(tree.root_node)
    seqs = {}
    method_declarations = []

    for method_node, _ in captures:
        has_todo = False
        comment_captures = QUERY_COMMENT.captures(method_node)
        for comment_node, _ in comment_captures:
            if 'TODO' in comment_node.text.decode():
                has_todo = True
                break

        name = method_node.child_by_field_name('name').text.decode()
        seq = seqs.get(name, 0)
        seqs[name] = seq + 1

        body_node = method_node.child_by_field_name('body')

        if body_node is not None:
            method_declarations.append({
                'name': name,
                'seq': seq,
                'node': body_node,
                'body_start': body_node.start_byte,
                'body_end': body_node.end_byte,
                'has_todo': has_todo,
            })
    
    if todo_only:
        return list(filter(lambda decl: decl["has_todo"], method_declarations))
    return method_declarations

def retain_todo_method(source: str, method: str, seq: int) -> str:
    todo_methods = get_todo_methods(source)
    for todo_method in reversed(todo_methods):
        if todo_method['name'] != method or todo_method['seq'] != seq:
            source = source[:todo_method['body_start']+1] + source[todo_method['body_end']-1:]
    return source

def replace_method(source: str, target: str, method: str, seq: int) -> str:
    source_todo_methods = get_todo_methods(source)
    target_methods = get_todo_methods(target, todo_only=False)

    source_todo_method = next(filter(lambda decl: decl["name"] == method and decl["seq"] == seq, source_todo_methods), None)
    target_method = next(filter(lambda decl: decl["name"] == method and decl["seq"] == seq, target_methods), None)

    if source_todo_method is not None and target_method is not None and not target_method["node"].has_error:
        source = source[:source_todo_method['body_start']+1] + target[target_method['body_start']+1:target_method['body_end']-1] + source[source_todo_method['body_end']-1:]
    return source