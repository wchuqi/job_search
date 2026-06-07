"""Module 16: CPython Runtime, Object Model and Memory (16-CPython运行时对象模型和内存管理, 面试06)

Knowledge points covered:
- source-to-execution pipeline (parse to AST, compile to code object/bytecode)
- ast.parse, compile, dis tools
- code object vs function object vs frame object
- object identity (id()), type (type()), value
- is vs ==
- reference counting (sys.getrefcount)
- what increases references
- cyclic references and gc.collect()
- __del__ caveats
- weakref.ref
- small integer caching
- string interning
- list memory model (dynamic array, amortized O(1) append)
- dict hash table and insertion order guarantee
- __slots__ (fixed attributes, reduced memory)
- memory troubleshooting with tracemalloc
- gc.get_referrers
- distinguishing Python heap vs OS RSS
"""

from __future__ import annotations

import dis
import gc
import sys
import tracemalloc
import weakref
from typing import Any


# ---------------------------------------------------------------------------
# Source to execution pipeline
# ---------------------------------------------------------------------------

def compile_demo(code_str: str) -> dict[str, Any]:
    """Demonstrate source → AST → code object → bytecode pipeline."""
    import ast
    tree = ast.parse(code_str)
    code = compile(code_str, "<demo>", "exec")
    bytecode = dis.Bytecode(code)
    return {
        "ast": ast.dump(tree),
        "code_type": str(type(code)),
        "co_consts": code.co_consts,
        "co_names": code.co_names,
        "instructions": [str(instr) for instr in bytecode],
    }


def inspect_code_object(func: Any) -> dict[str, Any]:
    """Inspect a function's code object attributes."""
    code = func.__code__
    return {
        "co_varnames": code.co_varnames,
        "co_consts": code.co_consts,
        "co_names": code.co_names,
        "co_filename": code.co_filename,
        "co_firstlineno": code.co_firstlineno,
        "co_stacksize": code.co_stacksize,
    }


def disassemble_function(func: Any) -> list[str]:
    """Disassemble a function to bytecode instructions."""
    return [str(instr) for instr in dis.Bytecode(func)]


# ---------------------------------------------------------------------------
# Object identity, type, value
# ---------------------------------------------------------------------------

def object_identity_demo() -> dict[str, Any]:
    """Demonstrate id(), type(), and is vs ==."""
    a = [1, 2, 3]
    b = [1, 2, 3]
    c = a
    return {
        "id_a": id(a),
        "id_b": id(b),
        "id_c": id(c),
        "a_is_b": a is b,      # False — different objects
        "a_is_c": a is c,      # True  — same object
        "a_eq_b": a == b,      # True  — same value
        "type_a": type(a).__name__,
    }


# ---------------------------------------------------------------------------
# Reference counting
# ---------------------------------------------------------------------------

def reference_counting_demo() -> dict[str, Any]:
    """Demonstrate reference counting with sys.getrefcount."""
    a = [1, 2, 3]
    ref_count_before = sys.getrefcount(a)  # +1 for the argument to getrefcount
    b = a
    ref_count_after = sys.getrefcount(a)
    c = [a, a, a]  # 3 more references
    ref_count_container = sys.getrefcount(a)
    return {
        "ref_count_before": ref_count_before,
        "ref_count_after_assign": ref_count_after,
        "ref_count_in_container": ref_count_container,
        "note": "getrefcount adds 1 for its own parameter",
    }


def what_increases_references() -> dict[str, str]:
    """What increases reference count of an object."""
    return {
        "variable_binding": "x = obj",
        "containers": "lst.append(obj), d[key] = obj",
        "function_arguments": "func(obj)",
        "closures": "inner function referencing outer variable",
        "default_args": "def f(x=obj):",
        "global_caches": "module-level dict/list holding obj",
    }


# ---------------------------------------------------------------------------
# Cyclic references and GC
# ---------------------------------------------------------------------------

def cyclic_reference_demo() -> dict[str, Any]:
    """Demonstrate cyclic references and garbage collection."""
    gc.collect()  # clean up first
    count_before = len(gc.get_objects())

    # Create a cycle
    a: list[Any] = []
    b: list[Any] = [a]
    a.append(b)  # cycle: a → b → a

    # Delete references
    del a
    del b

    # Objects may still exist due to cycle
    collected = gc.collect()
    count_after = len(gc.get_objects())
    return {
        "objects_before": count_before,
        "objects_after": count_after,
        "collected": collected,
    }


def gc_info() -> dict[str, Any]:
    """Get garbage collector information."""
    return {
        "thresholds": gc.get_threshold(),
        "counts": gc.get_count(),
        "isenabled": gc.isenabled(),
    }


# ---------------------------------------------------------------------------
# __del__ caveats
# ---------------------------------------------------------------------------

def del_caveats() -> dict[str, str]:
    """__del__ has many caveats and should be avoided in favor of context managers."""
    return {
        "order": "Order of __del__ calls is not guaranteed",
        "exceptions": "Exceptions in __del__ are ignored (printed to stderr)",
        "resurrection": "Object can resurrect itself in __del__ by creating new reference",
        "cycle": "Objects in reference cycles may never have __del__ called (pre-3.4)",
        "alternative": "Use context managers (__enter__/__exit__) or weakref.finalize",
    }


# ---------------------------------------------------------------------------
# weakref
# ---------------------------------------------------------------------------

def weakref_demo() -> dict[str, Any]:
    """Weak references don't prevent garbage collection."""
    class Payload:
        pass

    obj = Payload()
    obj.value = {"key": "value"}
    ref = weakref.ref(obj)
    strong_ref = ref()

    result = {
        "alive": ref() is not None,
        "value": strong_ref.value if strong_ref is not None else None,
    }

    del obj
    del strong_ref
    # Object may be collected
    gc.collect()
    result["alive_after_del"] = ref() is not None
    return result


# ---------------------------------------------------------------------------
# Small integer caching and string interning
# ---------------------------------------------------------------------------

def small_integer_caching() -> dict[str, Any]:
    """CPython caches small integers (-5 to 256).

    This is an implementation detail, NOT language semantics.
    Never use `is` for value comparison of arbitrary integers.
    """
    a = 256
    b = 256
    c = 257
    d = 257
    return {
        "256_is_cached": a is b,      # True — same cached object
        "257_may_not_cache": c is d,   # May be False in some contexts
        "lesson": "Always use == for value comparison, never is for non-None",
    }


def string_interning() -> dict[str, Any]:
    """CPython interns some strings (identifiers, small strings).

    Implementation detail, not language semantics.
    """
    a = "hello"
    b = "hello"
    c = "hello world!"
    d = "hello world!"
    return {
        "short_interned": a is b,      # likely True
        "long_may_not": c is d,         # may be False
        "sys.intern": sys.intern("custom") is sys.intern("custom"),
    }


# ---------------------------------------------------------------------------
# List memory model
# ---------------------------------------------------------------------------

def list_memory_model() -> dict[str, Any]:
    """Lists are dynamic arrays: O(1) amortized append, O(n) head insert."""
    import sys as sys_mod
    sizes = []
    lst: list[int] = []
    prev_size = sys_mod.getsizeof(lst)
    for i in range(20):
        lst.append(i)
        curr_size = sys_mod.getsizeof(lst)
        if curr_size != prev_size:
            sizes.append({"length": i + 1, "bytes": curr_size})
            prev_size = curr_size
    return {"growth_pattern": sizes}


# ---------------------------------------------------------------------------
# Dict hash table
# ---------------------------------------------------------------------------

def dict_memory_model() -> dict[str, Any]:
    """Dicts are hash tables: O(1) average lookup, insertion order guaranteed (3.7+)."""
    d: dict[str, int] = {}
    sizes = []
    import sys as sys_mod
    prev_size = sys_mod.getsizeof(d)
    for i in range(20):
        d[f"key_{i}"] = i
        curr_size = sys_mod.getsizeof(d)
        if curr_size != prev_size:
            sizes.append({"entries": i + 1, "bytes": curr_size})
            prev_size = curr_size
    return {
        "insertion_order": list(d.keys()),
        "growth_pattern": sizes,
    }


# ---------------------------------------------------------------------------
# __slots__
# ---------------------------------------------------------------------------

class SlottedPoint:
    """__slots__: fixed attributes, reduced memory, no dynamic attribute addition."""
    __slots__ = ("x", "y")

    def __init__(self, x: float, y: float) -> None:
        self.x = x
        self.y = y


class RegularPoint:
    """Regular class: uses __dict__, allows dynamic attributes."""

    def __init__(self, x: float, y: float) -> None:
        self.x = x
        self.y = y


def slots_memory_comparison() -> dict[str, Any]:
    """Compare memory usage of slotted vs regular classes."""
    slotted = SlottedPoint(1.0, 2.0)
    regular = RegularPoint(1.0, 2.0)
    return {
        "slotted_has_dict": hasattr(slotted, "__dict__"),
        "regular_has_dict": hasattr(regular, "__dict__"),
        "slotted_slots": SlottedPoint.__slots__,
    }


# ---------------------------------------------------------------------------
# tracemalloc — memory profiling
# ---------------------------------------------------------------------------

def tracemalloc_demo() -> dict[str, Any]:
    """Use tracemalloc to track memory allocations."""
    tracemalloc.start()
    # Allocate some memory
    data = [list(range(100)) for _ in range(100)]
    snapshot = tracemalloc.take_snapshot()
    stats = snapshot.statistics("lineno")
    tracemalloc.stop()

    return {
        "top_allocations": [
            {"file": str(s.traceback), "size": s.size}
            for s in stats[:3]
        ],
        "total_allocated": sum(s.size for s in stats),
    }


# ---------------------------------------------------------------------------
# gc.get_referrers
# ---------------------------------------------------------------------------

def get_referrers_demo() -> dict[str, Any]:
    """Use gc.get_referrers to find what's holding references to an object."""
    target = [1, 2, 3]
    container = [target, target]
    referrers = gc.get_referrers(target)
    return {
        "referrers_count": len(referrers),
        "container_is_referrer": container in referrers,
    }


# ---------------------------------------------------------------------------
# Python heap vs OS RSS
# ---------------------------------------------------------------------------

def memory_types_info() -> dict[str, str]:
    """Distinguish Python heap from OS RSS."""
    return {
        "python_heap": "Memory allocated by Python objects (tracked by Python allocator)",
        "os_rss": "Resident Set Size — total physical memory used by process",
        "difference": "RSS includes memory from C extensions, fragmentation, GC overhead",
        "measurement": "Use tracemalloc for Python heap, resource module or /proc for RSS",
    }
