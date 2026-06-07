"""Module 07: Iterators, Generators and Comprehensions (07-迭代器生成器和推导式)

Knowledge points covered:
- iterable objects (can be passed to iter())
- iterators (implement __iter__ and __next__, remember position)
- generator functions (use yield, pause/resume)
- generator expressions (lazy list comprehensions)
- list/dict/set comprehensions
- lazy evaluation
- for loop iteration protocol (iter() + next() + StopIteration)
- generators for large file processing
- iterator statefulness (consumed after traversal)
- comprehension readability limits
"""

from __future__ import annotations

from typing import Any, Iterator


# ---------------------------------------------------------------------------
# Iterable vs Iterator
# ---------------------------------------------------------------------------

class CountDown:
    """Custom iterable: creates a new iterator each time __iter__ is called."""

    def __init__(self, start: int) -> None:
        self.start = start

    def __iter__(self) -> Iterator[int]:
        return CountDownIterator(self.start)


class CountDownIterator:
    """Iterator: remembers position, implements __next__."""

    def __init__(self, current: int) -> None:
        self.current = current

    def __iter__(self) -> CountDownIterator:
        return self

    def __next__(self) -> int:
        if self.current <= 0:
            raise StopIteration
        value = self.current
        self.current -= 1
        return value


def iteration_protocol_demo(stop: int = 5) -> list[int]:
    """for loop uses iter() + next() + StopIteration internally."""
    result = []
    it = iter(range(stop))  # calls range.__iter__()
    while True:
        try:
            value = next(it)  # calls it.__next__()
            result.append(value)
        except StopIteration:
            break
    return result


# ---------------------------------------------------------------------------
# Generator functions (yield)
# ---------------------------------------------------------------------------

def count_down_gen(start: int) -> Iterator[int]:
    """Generator function: uses yield to produce values lazily."""
    current = start
    while current > 0:
        yield current
        current -= 1


def fibonacci(limit: int) -> Iterator[int]:
    """Generator: Fibonacci sequence up to limit."""
    a, b = 0, 1
    while a < limit:
        yield a
        a, b = b, a + b


def read_lines_lazy(lines: list[str]) -> Iterator[str]:
    """Simulate lazy file reading with generators."""
    for line in lines:
        stripped = line.strip()
        if stripped:
            yield stripped


def generator_statefulness_demo() -> dict[str, Any]:
    """Generators are stateful — consumed after traversal."""
    gen = count_down_gen(3)
    first_pass = list(gen)
    second_pass = list(gen)  # empty — already consumed
    return {"first": first_pass, "second": second_pass}


# ---------------------------------------------------------------------------
# Generator expressions (lazy version of list comprehensions)
# ---------------------------------------------------------------------------

def generator_expression_demo(n: int) -> dict[str, Any]:
    """Generator expressions are lazy — don't build the full list in memory."""
    # List comprehension: builds full list
    squares_list = [x ** 2 for x in range(n)]
    # Generator expression: produces values one at a time
    squares_gen = (x ** 2 for x in range(n))
    return {
        "list_result": squares_list,
        "gen_type": str(type(squares_gen)),
        "gen_sum": sum(squares_gen),  # consumes the generator
    }


# ---------------------------------------------------------------------------
# Comprehensions
# ---------------------------------------------------------------------------

def list_comprehension_demo(data: list[int]) -> dict[str, list[Any]]:
    """List comprehensions: [expr for item in iterable if condition]."""
    return {
        "squares": [x ** 2 for x in data],
        "even_only": [x for x in data if x % 2 == 0],
        "transform_and_filter": [x ** 2 for x in data if x > 0],
        "nested": [(x, y) for x in range(3) for y in range(3) if x != y],
    }


def dict_comprehension_demo(keys: list[str], values: list[int]) -> dict[str, int]:
    """Dict comprehension: {key_expr: value_expr for item in iterable}."""
    return {k: v for k, v in zip(keys, values) if v > 0}


def set_comprehension_demo(data: list[int]) -> set[int]:
    """Set comprehension: {expr for item in iterable}."""
    return {x % 3 for x in data}


# ---------------------------------------------------------------------------
# Lazy evaluation
# ---------------------------------------------------------------------------

def lazy_pipeline_demo(items: list[str]) -> list[str]:
    """Generators enable lazy evaluation pipelines — no intermediate lists."""
    # Each step is lazy; full list only materialized at the end
    stripped = (s.strip() for s in items)
    non_empty = (s for s in stripped if s)
    upper = (s.upper() for s in non_empty)
    return list(upper)


# ---------------------------------------------------------------------------
# Generators for large file processing
# ---------------------------------------------------------------------------

def chunk_reader(lines: list[str], chunk_size: int = 3) -> Iterator[list[str]]:
    """Generator that yields chunks of lines — simulates large file processing."""
    chunk: list[str] = []
    for line in lines:
        chunk.append(line)
        if len(chunk) >= chunk_size:
            yield chunk
            chunk = []
    if chunk:
        yield chunk


def process_large_data_stream(data: Iterator[int]) -> dict[str, Any]:
    """Process data stream without loading everything into memory."""
    total = 0
    count = 0
    maximum = float("-inf")
    for value in data:
        total += value
        count += 1
        maximum = max(maximum, value)
    return {"total": total, "count": count, "max": maximum}


# ---------------------------------------------------------------------------
# Yield from (delegating generators)
# ---------------------------------------------------------------------------

def flatten(nested: list[Any]) -> Iterator[Any]:
    """Recursively flatten nested lists using yield from."""
    for item in nested:
        if isinstance(item, list):
            yield from flatten(item)
        else:
            yield item
