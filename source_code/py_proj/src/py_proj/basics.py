"""Module 01: Basics and Runtime Model (01-语法基础和运行模型, 面试01-基础语法和类型)

Knowledge points covered:
- Indentation as syntax (4 spaces)
- Variable assignment as name binding
- Expressions vs statements
- Comments and docstrings
- Operators (arithmetic, comparison, logical, membership, identity, bitwise)
- is vs == (identity vs value)
- Python 3 vs Python 2 key differences
- Variable-object relationship (names are bindings)
- Truthiness rules
"""

from __future__ import annotations

import sys


# ---------------------------------------------------------------------------
# Variable assignment as name binding
# ---------------------------------------------------------------------------

def demonstrate_binding() -> tuple[int, int]:
    """Show that assignment binds a name to an object, not a memory box."""
    a = [1, 2, 3]
    b = a  # b and a refer to the same object
    b.append(4)
    return a, b  # both are [1, 2, 3, 4]


def demonstrate_is_vs_eq() -> dict[str, bool]:
    """is checks identity (same object); == checks value (same content)."""
    a = [1, 2, 3]
    b = [1, 2, 3]
    c = a
    return {
        "a == b": a == b,      # True  — same value
        "a is b": a is b,      # False — different objects
        "a is c": a is c,      # True  — same object
        "None is None": None is None,  # always use `is` for None
    }


def identity_with_none(x: int | None) -> bool:
    """Always use `is` to check for None, not ==."""
    return x is None


# ---------------------------------------------------------------------------
# Operators
# ---------------------------------------------------------------------------

def arithmetic_ops(a: float, b: float) -> dict[str, float]:
    """Demonstrate arithmetic operators including true division."""
    return {
        "add": a + b,
        "sub": a - b,
        "mul": a * b,
        "truediv": a / b,       # Python 3 true division
        "floordiv": a // b,     # floor division
        "mod": a % b,
        "pow": a ** b,
    }


def comparison_ops(a: int, b: int) -> dict[str, bool]:
    """Demonstrate comparison operators (chaining supported in Python 3)."""
    return {
        "eq": a == b,
        "ne": a != b,
        "lt": a < b,
        "le": a <= b,
        "gt": a > b,
        "ge": a >= b,
        # Python 3 chaining: 1 < 2 < 3 is equivalent to 1 < 2 and 2 < 3
        "chain": 1 < a < b < 10,
    }


def logical_ops(x: bool, y: bool) -> dict[str, bool]:
    """Demonstrate logical operators (short-circuit evaluation)."""
    return {
        "and": x and y,
        "or": x or y,
        "not": not x,
    }


def membership_ops(item: int, collection: list[int] | set[int]) -> bool:
    """`in` checks membership; O(1) for set, O(n) for list."""
    return item in collection


def bitwise_ops(a: int, b: int) -> dict[str, int]:
    """Demonstrate bitwise operators."""
    return {
        "and": a & b,
        "or": a | b,
        "xor": a ^ b,
        "not": ~a,
        "lshift": a << 1,
        "rshift": a >> 1,
    }


# ---------------------------------------------------------------------------
# Truthiness
# ---------------------------------------------------------------------------

FALSY_VALUES = [False, None, 0, 0.0, 0j, "", [], {}, set(), frozenset()]


def is_truthy(value: object) -> bool:
    """Check truthiness: False/None/0/empty containers are falsy."""
    return bool(value)


def demonstrate_truthiness() -> list[tuple[object, bool]]:
    """Return truthiness of common values."""
    test_values: list[object] = [False, None, 0, 1, -1, 0.0, 1.5, "", "hello",
                                  [], [1], {}, {"a": 1}, set(), {1}, b"", b"x"]
    return [(v, bool(v)) for v in test_values]


# ---------------------------------------------------------------------------
# Python 3 vs Python 2 key differences
# ---------------------------------------------------------------------------

def python3_differences_demo() -> dict[str, str]:
    """Summarize key Python 3 vs Python 2 differences."""
    return {
        "strings": "Python 3: str is Unicode, bytes is raw. Python 2: str is bytes, unicode is text.",
        "print": "Python 3: print() function. Python 2: print statement.",
        "division": "Python 3: / is true division, // is floor. Python 2: / is integer for ints.",
        "iterables": "Python 3: range(), map(), filter() return iterators. Python 2: return lists.",
        "input": "Python 3: input() always returns str. Python 2: raw_input() vs input().",
    }


# ---------------------------------------------------------------------------
# Expressions vs Statements
# ---------------------------------------------------------------------------

def expression_vs_statement() -> dict[str, str]:
    """Expressions produce values; statements perform actions."""
    # Expression: produces a value
    expr_example = 2 + 3  # "2 + 3" is an expression
    # Statement: performs an action (assignment is a statement)
    # if, for, while, def, class, import, return, raise — all statements
    return {
        "expression": f"2 + 3 = {expr_example} (produces a value)",
        "statement": "x = 5 (assignment statement, does not produce a value)",
        "note": "In Python 3, := (walrus operator) is an expression assignment",
    }


def walrus_operator_demo(items: list[int]) -> list[int]:
    """Python 3.8+ walrus operator := assigns and returns value in expression."""
    # Filter items where the square is > 10
    return [y for x in items if (y := x ** 2) > 10]


# ---------------------------------------------------------------------------
# sys and version info
# ---------------------------------------------------------------------------

def get_python_version_info() -> dict[str, str]:
    """Return current Python version information."""
    return {
        "version": sys.version,
        "major": str(sys.version_info.major),
        "minor": str(sys.version_info.minor),
        "implementation": sys.implementation.name,
    }
