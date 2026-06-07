"""Module 02: Built-in Types and Data Structures (02-内置类型和数据结构, 面试01)

Knowledge points covered:
- str (immutable, slicing, formatting, encoding)
- list (mutable, ordered, fast append, slow head insert)
- tuple (immutable, hashable if elements are)
- dict (O(1) average lookup, hash-based)
- set (O(1) membership test, deduplication, set operations)
- bool/None truthiness
- mutability vs immutability
- hashing
- slicing
- unpacking
- shallow copy vs deep copy
- shared reference pitfalls
- string immutability and ''.join()
- nested list shallow copy trap
- dict.get() and dict.items()
"""

from __future__ import annotations

import copy
from typing import Any


# ---------------------------------------------------------------------------
# str — immutable, slicing, formatting, encoding
# ---------------------------------------------------------------------------

def str_slicing(s: str) -> dict[str, str]:
    """Demonstrate string slicing (returns new string, original unchanged)."""
    return {
        "original": s,
        "first3": s[:3],
        "last3": s[-3:],
        "middle": s[1:-1],
        "reverse": s[::-1],
        "every_other": s[::2],
    }


def str_formatting(name: str, age: int, score: float) -> dict[str, str]:
    """Demonstrate string formatting methods."""
    return {
        "fstring": f"{name} is {age} years old, score: {score:.1f}",
        "format": "{} is {} years old, score: {:.1f}".format(name, age, score),
        "template": f"{'Name':>10}: {name:<20} Age: {age:03d}",
    }


def str_immutability_demo() -> str:
    """Strings are immutable — every operation creates a new string."""
    s = "hello"
    original_id = id(s)
    s_upper = s.upper()
    # s is unchanged, s_upper is a new object
    assert id(s) == original_id
    assert s == "hello"
    return s_upper


def str_join_vs_concat(items: list[str]) -> tuple[str, str]:
    """''.join() is O(n), += in a loop is O(n²) due to string immutability."""
    # Good: join
    joined = "".join(items)
    # Bad pattern (for demonstration only): concatenation in loop
    result = ""
    for item in items:
        result += item  # creates a new string each time
    return joined, result


def str_encoding_demo(text: str) -> dict[str, bytes | str]:
    """Demonstrate string encoding/decoding."""
    utf8_bytes = text.encode("utf-8")
    latin1_bytes = text.encode("latin-1", errors="replace")
    return {
        "original": text,
        "utf8_bytes": utf8_bytes,
        "utf8_decoded": utf8_bytes.decode("utf-8"),
        "latin1_bytes": latin1_bytes,
    }


# ---------------------------------------------------------------------------
# list — mutable, ordered
# ---------------------------------------------------------------------------

def list_operations() -> dict[str, list[int]]:
    """Demonstrate common list operations."""
    lst = [3, 1, 4, 1, 5, 9]
    return {
        "original": lst.copy(),
        "append": [*lst, 6],          # fast: O(1) amortized
        "insert_head": [0, *lst],     # slow: O(n) — shifts all elements
        "sorted": sorted(lst),
        "reversed": list(reversed(lst)),
        "sliced": lst[1:4],
        "unpacked_first_rest": list(lst),  # unpacking: first, *rest = lst
    }


def list_comprehension_demo() -> dict[str, list[int]]:
    """List comprehensions are faster and more Pythonic than manual loops."""
    data = range(10)
    return {
        "squares": [x ** 2 for x in data],
        "even_squares": [x ** 2 for x in data if x % 2 == 0],
        "nested": [x * y for x in range(3) for y in range(3)],
    }


def unpacking_demo(a: int, b: int, c: int) -> dict[str, Any]:
    """Demonstrate various unpacking patterns."""
    lst = [a, b, c, 4, 5]
    first, *middle, last = lst
    return {
        "first": first,
        "middle": middle,
        "last": last,
        "swap": (b, a),  # a, b = b, a swaps values
    }


# ---------------------------------------------------------------------------
# tuple — immutable, hashable if elements are
# ---------------------------------------------------------------------------

def tuple_demo() -> dict[str, Any]:
    """Demonstrate tuple properties."""
    t = (1, 2, 3)
    nested_mutable = (1, [2, 3], 4)
    return {
        "immutable": t,
        "hashable": hash(t),  # works because all elements are immutable
        "nested_mutable": nested_mutable,  # tuple with list — tuple is "frozen" but list inside is mutable
        "single_element_trap": (1,),  # comma required for single-element tuple, not (1)
        "packing": (1, 2, 3),  # tuple packing
    }


def tuple_hashability() -> dict[str, bool]:
    """Tuples are hashable only if all elements are hashable."""
    t1 = (1, 2, "hello")
    t2 = (1, [2, 3])
    results: dict[str, bool] = {}
    results["hashable_tuple"] = hash(t1) is not None
    try:
        hash(t2)
        results["tuple_with_list"] = True  # pragma: no cover - tuple contains list and cannot be hashable
    except TypeError:
        results["tuple_with_list"] = False  # list is not hashable
    return results


# ---------------------------------------------------------------------------
# dict — O(1) average lookup, hash-based, insertion order (3.7+)
# ---------------------------------------------------------------------------

def dict_operations() -> dict[str, Any]:
    """Demonstrate common dict operations."""
    d = {"a": 1, "b": 2, "c": 3}
    return {
        "original": d,
        "get_with_default": d.get("z", 0),  # safe access with default
        "items": list(d.items()),
        "keys": list(d.keys()),
        "values": list(d.values()),
        "merged": {**d, "d": 4},  # dict unpacking merge (3.5+)
        "dict_merge_operator": d | {"e": 5},  # Python 3.9+ merge operator
    }


def dict_comprehension_demo() -> dict[str, int]:
    """Dict comprehension creates dicts from iterables."""
    return {x: x ** 2 for x in range(5)}


def dict_hash_requirement() -> dict[str, Any]:
    """Dict keys must be hashable (immutable and have __hash__)."""
    valid = {(1, 2): "tuple_key", "str": "string_key", 42: "int_key"}
    try:
        invalid = {[1, 2]: "list_key"}  # type: ignore
        return {"valid": valid, "invalid_works": True}  # pragma: no cover - list keys always raise TypeError
    except TypeError:
        return {"valid": valid, "invalid_works": False}


# ---------------------------------------------------------------------------
# set — O(1) membership, deduplication, set operations
# ---------------------------------------------------------------------------

def set_operations_demo(a: set[int], b: set[int]) -> dict[str, set[int]]:
    """Demonstrate set operations."""
    return {
        "union": a | b,
        "intersection": a & b,
        "difference": a - b,
        "symmetric_difference": a ^ b,
        "subset": a <= b if a else set(),
        "superset": a >= b if a else set(),
    }


def set_comprehension_demo() -> set[int]:
    """Set comprehension deduplicates values while computing derived data."""
    return {x % 3 for x in range(10)}


def set_deduplication(items: list[int]) -> list[int]:
    """Use set to remove duplicates (order not guaranteed in < 3.7)."""
    return list(dict.fromkeys(items))  # preserves order (Python 3.7+)


def set_membership_test() -> dict[str, float]:
    """Demonstrate O(1) set membership vs O(n) list membership."""
    import time
    n = 100_000
    lst = list(range(n))
    st = set(range(n))
    target = n - 1

    start = time.perf_counter()
    for _ in range(100):
        target in lst
    list_time = time.perf_counter() - start

    start = time.perf_counter()
    for _ in range(100):
        target in st
    set_time = time.perf_counter() - start

    return {"list_time": list_time, "set_time": set_time}


# ---------------------------------------------------------------------------
# Shallow copy vs deep copy
# ---------------------------------------------------------------------------

def shallow_copy_demo() -> dict[str, Any]:
    """Shallow copy copies the container but shares inner references."""
    original = [[1, 2], [3, 4]]
    shallow = copy.copy(original)
    shallow[0][0] = 99  # modifies original too!
    return {
        "original": original,      # [[99, 2], [3, 4]]
        "shallow": shallow,        # [[99, 2], [3, 4]]
        "same_inner": original[0] is shallow[0],  # True — shared reference
        "different_outer": original is shallow,     # False — different containers
    }


def deep_copy_demo() -> dict[str, Any]:
    """Deep copy copies everything recursively — fully independent."""
    original = [[1, 2], [3, 4]]
    deep = copy.deepcopy(original)
    deep[0][0] = 99  # does NOT modify original
    return {
        "original": original,  # [[1, 2], [3, 4]]
        "deep": deep,           # [[99, 2], [3, 4]]
        "same_inner": original[0] is deep[0],  # False — independent
    }


def nested_list_trap() -> dict[str, Any]:
    """[[0]*3]*2 creates shared references — modifying one row affects all."""
    # BAD: shared references
    bad = [[0] * 3] * 2
    bad[0][0] = 1
    # GOOD: independent rows
    good = [[0] * 3 for _ in range(2)]
    good[0][0] = 1
    return {
        "bad": bad,    # [[1, 0, 0], [1, 0, 0]] — both rows modified!
        "good": good,  # [[1, 0, 0], [0, 0, 0]] — only first row modified
    }


# ---------------------------------------------------------------------------
# Shared reference pitfall
# ---------------------------------------------------------------------------

def shared_reference_pitfall() -> dict[str, Any]:
    """Multiple names can bind the same mutable object."""
    a = {"x": 1}
    b = a  # b is NOT a copy — same object
    b["x"] = 99
    return {
        "a": a,           # {"x": 99} — also modified!
        "b": b,           # {"x": 99}
        "same_object": a is b,  # True
    }


# ---------------------------------------------------------------------------
# Hashing
# ---------------------------------------------------------------------------

def hashing_demo() -> dict[str, int]:
    """Demonstrate hashing for immutable types."""
    return {
        "int_hash": hash(42),
        "str_hash": hash("hello"),
        "tuple_hash": hash((1, 2, 3)),
        "float_hash": hash(3.14),
        "bool_hash": hash(True),
        "none_hash": hash(None),
    }
