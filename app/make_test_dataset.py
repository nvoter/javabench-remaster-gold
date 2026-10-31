import subprocess
from pathlib import Path
import pandas as pd
import networkx as nx
import matplotlib.pyplot as plt
from io import StringIO
from app.util.io import write_jsonl


def jdeps(main, test, *, recursive=False):
    cmd_awk = ["awk", '$4 == "main" || $4 == "test" || $4 == "classes" || $4 == "test-classes"']
    cmd_jdeps = [
        "jdeps",
        "-v",
        "-cp",
        main,
        test,
    ]
    if recursive:
        cmd_jdeps.insert(1, "-R")

    p1 = subprocess.Popen(cmd_jdeps, stdout=subprocess.PIPE)
    p2 = subprocess.Popen(cmd_awk, stdin=p1.stdout, stdout=subprocess.PIPE)
    p1.stdout.close()
    output = p2.communicate()[0]
    return output.decode("utf-8")


def draw_graph(g):
    pos = nx.shell_layout(g)
    # pos = nx.spring_layout(g, k=3, iterations=50)

    colors = ["red" if node.endswith("Test") else "skyblue" for node in g.nodes]
    nx.draw_networkx_nodes(g, pos, node_size=1000, node_color=colors, alpha=0.9)

    nx.draw_networkx_edges(g, pos, width=2, edge_color="gray", arrowsize=25)

    labels = {node: node.rsplit(".", 1)[1] for node in g.nodes}
    nx.draw_networkx_labels(
        g, pos, labels=labels, font_size=20, font_family="sans-serif"
    )
    plt.show()


def get_identity(node):
    splits = node.rsplit(".", 1)
    return splits[-1]


def is_todo(node, project):
    todos = [key.rsplit(".")[0] for key in project["tasks"].keys()]
    return any(node.endswith(todo) for todo in todos)

def get_class_dirs(project_id: str):
    project_root = Path(f"projects/{project_id}-Solution")
    candidates = [
        (
            project_root / "build/classes/java/main",
            project_root / "build/classes/java/test",
        ),
        (
            project_root / "target/classes",
            project_root / "target/test-classes",
        ),
    ]

    for main_dir, test_dir in candidates:
        if main_dir.is_dir() and test_dir.is_dir():
            return str(main_dir), str(test_dir)

    raise FileNotFoundError(
        f"Cannot find compiled class directories for {project_id}. "
        "Run Gradle testClasses or Maven test-compile first."
    )

def main(project_id: str, project: dict):
    MAIN, TEST = get_class_dirs(project_id)
    tests = jdeps(main=MAIN, test=TEST, recursive=False)
    test_data = pd.read_csv(StringIO(tests), header=None, sep="\s+")
    test_set = test_data.iloc[:, 0].drop_duplicates()

    deps = jdeps(main=MAIN, test=TEST, recursive=True)
    dep_data = pd.read_csv(StringIO(deps), header=None, sep="\s+")
    dep_entries = dep_data.iloc[:, [0, 2]]

    dep_graph = nx.DiGraph()
    for _, row in dep_entries.iterrows():
        dep_graph.add_edge(row[0], row[2])
    nodes_to_ignore = [node for node in dep_graph.nodes if not node.endswith("Test") and not node.endswith("Tests")]
    dep_table = {}
    for test in test_set:
        if test in nodes_to_ignore:
            continue
        dep_table[test] = set(nx.dfs_postorder_nodes(dep_graph, test))
        dep_table[test].remove(test)
    subset_graph = nx.DiGraph()
    subset_graph.add_nodes_from(dep_table.keys())
    for test1, successors1 in dep_table.items():
        for test2, successors2 in dep_table.items():
            if test1 == test2:
                continue
            if successors1 < successors2:
                subset_graph.add_edge(test1, test2)
    # draw_graph(subset_graph)

    output_deps = []
    for node in nx.topological_sort(subset_graph):
        if node in nodes_to_ignore:
            continue
        parents = list(subset_graph.predecessors(node))
        full_deps = dep_table[node]
        incremental_deps = dep_table[node]
        for parent in parents:
            incremental_deps = incremental_deps - dep_table[parent]
        
        full_deps = list(set(filter(lambda x: x not in test_set and is_todo(x, project), full_deps)))
        incremental_deps = list(set(filter(lambda x: x not in test_set and is_todo(x, project), incremental_deps)))
        if len(full_deps) == 0:
            continue

        output_deps.append(
            {
                "test_id": f"{project_id}/{get_identity(node)}",
                "target": node,
                "parents": list(map(get_identity, parents)),
                "full_deps": full_deps,
                "incremental_deps": incremental_deps,
            }
        )

    write_jsonl(f"data/test-{project_id}.jsonl", output_deps)

if __name__ == "__main__":
    from datasets.descriptor.pa19 import project as project19
    main("PA19", project19)
    from datasets.descriptor.pa20 import project as project20
    main("PA20", project20)
    from datasets.descriptor.pa21 import project as project21
    main("PA21", project21)
    from datasets.descriptor.pa22 import project as project22
    main("PA22", project22)
