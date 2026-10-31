import tiktoken


def num_tokens_from_str(content):
    encoding = tiktoken.get_encoding("cl100k_base")
    return len(encoding.encode(content))
