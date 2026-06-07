"""Module 03: Control Flow, Functions and Scope (03-控制流函数和作用域, 面试02)

Knowledge points covered:
- if/elif/else
- for (iterate over iterables)
- while (condition-based repetition)
- break/continue
- match (Python 3.10+ structural pattern matching)
- function parameters (positional, keyword, default, *args, **kwargs)
- return values
- LEGB scope rule
- global and nonlocal
- mutable default argument trap
- closure variable binding capture
- type hints on functions
- closures in loops and the default-argument fix
- function argument passing (object reference assignment)
"""

from __future__ import annotations

from typing import Any


# ---------------------------------------------------------------------------
# Control flow: if/elif/else, for, while, break/continue
# ---------------------------------------------------------------------------

def classify_number(n: int) -> str:
    """if/elif/else control flow."""
    if n > 0:
        return "positive"
    elif n < 0:
        return "negative"
    else:
        return "zero"


def for_loop_demo(items: list[int]) -> dict[str, Any]:
    """for iterates over any iterable."""
    total = 0
    for item in items:
        total += item
    # enumerate gives index + value
    indexed = [(i, v) for i, v in enumerate(items)]
    # zip iterates multiple iterables in parallel
    keys = ["a", "b", "c"]
    zipped = dict(zip(keys, items))
    return {"total": total, "indexed": indexed, "zipped": zipped}


def while_demo(limit: int) -> list[int]:
    """while repeats until condition is False."""
    result = []
    i = 0
    while i < limit:
        result.append(i)
        i += 1
    return result


def break_continue_demo(items: list[int]) -> dict[str, list[int]]:
    """break exits loop; continue skips to next iteration."""
    evens = []
    found_three: list[int] = []
    for item in items:
        if item == 3:
            break  # stop when we find 3
        if item % 2 != 0:
            continue  # skip odd numbers
        evens.append(item)
        found_three.append(item)
    return {"evens": evens, "before_three": found_three}


def match_demo(value: Any) -> str:
    """Python 3.10+ structural pattern matching (match/case)."""
    match value:
        case int() if value > 0:
            return f"positive int: {value}"
        case int() if value < 0:
            return f"negative int: {value}"
        case 0:
            return "zero"
        case str() if len(value) > 0:
            return f"non-empty string: {value}"
        case []:
            return "empty list"
        case [x]:
            return f"single-element list: {x}"
        case [x, y]:
            return f"two-element list: {x}, {y}"
        case [first, *rest]:
            return f"list starting with {first}, rest has {len(rest)} items"
        case {"name": str(name), "age": int(age)}:
            return f"person: {name}, age {age}"
        case _:
            return f"other: {type(value).__name__}"


# ---------------------------------------------------------------------------
# Function parameters
# ---------------------------------------------------------------------------

def positional_args(a: int, b: int, c: int) -> int:
    """Positional arguments: order matters."""
    return a + b + c


def keyword_args(a: int = 10, b: int = 20, c: int = 30) -> int:
    """Keyword arguments: can pass by name, order doesn't matter."""
    return a + b + c


def mixed_args(a: int, b: int = 20, *args: int, **kwargs: int) -> dict[str, Any]:
    """Demonstrate mixed parameter types.

    Order: positional, keyword, *args, **kwargs
    """
    return {
        "a": a,
        "b": b,
        "args": args,
        "kwargs": kwargs,
    }


def args_kwargs_demo(*args: Any, **kwargs: Any) -> dict[str, Any]:
    """*args collects positional args as tuple; **kwargs collects keyword args as dict."""
    return {"args": args, "kwargs": kwargs}


def forward_args(*args: Any, **kwargs: Any) -> dict[str, Any]:
    """Common pattern: forward args/kwargs to another function."""
    return args_kwargs_demo(*args, **kwargs)


# ---------------------------------------------------------------------------
# Return values
# ---------------------------------------------------------------------------

def return_single(x: int) -> int:
    """Functions return a single object."""
    return x * 2


def return_tuple(x: int, y: int) -> tuple[int, int]:
    """Return multiple values as a tuple (tuple packing)."""
    return y, x  # swap


def return_none_by_default(x: int) -> int | None:
    """Functions without explicit return return None."""
    if x > 0:
        return x
    # implicit return None


# ---------------------------------------------------------------------------
# LEGB scope rule
# ---------------------------------------------------------------------------

global_var = "global"


def legb_demo() -> dict[str, str]:
    """Demonstrate LEGB: Local, Enclosing, Global, Built-in."""
    local_var = "local"

    def inner() -> str:
        enclosing_var = "enclosing"  # would be in outer function
        return f"L:{local_var}, E:{enclosing_var}, G:{global_var}"

    return {"result": inner(), "builtin_example": "len is a builtin"}


def global_keyword_demo() -> str:
    """global allows modifying a module-level variable inside a function."""
    global global_var
    old = global_var
    global_var = "modified_global"
    return old


def nonlocal_keyword_demo() -> tuple[int, int]:
    """nonlocal allows modifying an enclosing function's variable."""
    counter = 0

    def increment() -> None:
        nonlocal counter
        counter += 1

    increment()
    increment()
    return counter, counter  # (2, 2)


# ---------------------------------------------------------------------------
# Mutable default argument trap
# ---------------------------------------------------------------------------

def mutable_default_trap(items: list[int] | None = None) -> list[int]:
    """WRONG: def f(items=[]): — default list is shared across calls.

    CORRECT: use None as sentinel and create new list inside.
    """
    if items is None:
        items = []
    items.append(1)
    return items


def mutable_default_demonstration() -> dict[str, list[int]]:
    """Show the difference between mutable default and None sentinel."""
    # BAD pattern (for demonstration):
    def bad_append(item: int, lst: list[int] = []) -> list[int]:  # type: ignore
        lst.append(item)
        return lst

    r1 = bad_append(1)
    r2 = bad_append(2)
    # r2 is [1, 2] — the default list was shared!
    return {"bad_r1": r1, "bad_r2": r2}


# ---------------------------------------------------------------------------
# Closures
# ---------------------------------------------------------------------------

def make_counter(start: int = 0) -> tuple[Any, Any]:
    """Closure: inner function captures enclosing scope's variable."""
    count = start

    def increment() -> int:
        nonlocal count
        count += 1
        return count

    def get() -> int:
        return count

    return increment, get


def closure_in_loop_trap() -> list[Any]:
    """TRAP: closures in loops capture the variable binding, not its value."""
    # BAD: all functions share the same `i`
    bad_funcs = []
    for i in range(3):
        bad_funcs.append(lambda: i)
    bad_results = [f() for f in bad_funcs]  # [2, 2, 2] — all see final i=2

    # FIX: use default argument to capture current value
    good_funcs = []
    for i in range(3):
        good_funcs.append(lambda i=i: i)  # default arg captures current i
    good_results = [f() for f in good_funcs]  # [0, 1, 2]

    return bad_results, good_results


def make_adder(n: int) -> Any:
    """Closure factory: returns a function that adds n to its argument."""
    def adder(x: int) -> int:
        return x + n
    return adder


# ---------------------------------------------------------------------------
# Type hints on functions
# ---------------------------------------------------------------------------

def typed_function(name: str, scores: list[int], active: bool = True) -> dict[str, Any]:
    """Demonstrate type hints on function parameters and return."""
    return {
        "name": name,
        "average": sum(scores) / len(scores) if scores else 0.0,
        "active": active,
        "count": len(scores),
    }


def generic_sum(items: list[int | float]) -> int | float:
    """Type hints with union types."""
    return sum(items)


# ---------------------------------------------------------------------------
# Function argument passing
# ---------------------------------------------------------------------------

def modify_mutable(lst: list[int]) -> list[int]:
    """Mutable object modification is visible to caller (pass by object reference)."""
    lst.append(999)
    return lst


def rebind_local(lst: list[int]) -> list[int]:
    """Rebinding (lst = [999]) is NOT visible to caller — only rebinds local name."""
    lst = [999, 999]
    return lst
