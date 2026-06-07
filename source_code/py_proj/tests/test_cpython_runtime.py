"""Tests for cpython_runtime module (16-CPython运行时对象模型和内存管理, 面试06)."""

import gc
import sys
import pytest
from py_proj.cpython_runtime import (
    RegularPoint,
    SlottedPoint,
    compile_demo,
    cyclic_reference_demo,
    del_caveats,
    disassemble_function,
    dict_memory_model,
    gc_info,
    get_referrers_demo,
    inspect_code_object,
    list_memory_model,
    memory_types_info,
    object_identity_demo,
    reference_counting_demo,
    slots_memory_comparison,
    small_integer_caching,
    string_interning,
    tracemalloc_demo,
    weakref_demo,
    what_increases_references,
)


class TestCompileDemo:
    def test_compile(self):
        result = compile_demo("x = 1 + 2")
        assert result["co_consts"] is not None
        assert len(result["instructions"]) > 0

    def test_code_object(self):
        result = compile_demo("y = 'hello'")
        assert result["code_type"] == "<class 'code'>"


class TestCodeInspection:
    def test_inspect_code(self):
        def sample(x: int, y: int) -> int:
            z = x + y
            return z
        result = inspect_code_object(sample)
        assert "x" in result["co_varnames"]
        assert "y" in result["co_varnames"]
        assert "z" in result["co_varnames"]

    def test_disassemble(self):
        def add(a, b):
            return a + b
        instructions = disassemble_function(add)
        assert len(instructions) > 0


class TestObjectIdentity:
    def test_identity(self):
        result = object_identity_demo()
        assert result["a_is_b"] is False
        assert result["a_is_c"] is True
        assert result["a_eq_b"] is True
        assert result["type_a"] == "list"


class TestReferenceCounting:
    def test_ref_counting(self):
        result = reference_counting_demo()
        assert result["ref_count_after_assign"] > result["ref_count_before"]
        assert result["ref_count_in_container"] > result["ref_count_after_assign"]

    def test_what_increases(self):
        result = what_increases_references()
        assert "variable_binding" in result
        assert "containers" in result


class TestCyclicReferences:
    def test_cyclic_gc(self):
        result = cyclic_reference_demo()
        assert "collected" in result
        assert result["collected"] >= 0

    def test_gc_info(self):
        result = gc_info()
        assert len(result["thresholds"]) == 3
        assert result["isenabled"] is True


class TestDelCaveats:
    def test_caveats(self):
        result = del_caveats()
        assert "order" in result
        assert "alternative" in result


class TestWeakRef:
    def test_weakref_alive(self):
        result = weakref_demo()
        # After deletion, weak ref may or may not be alive depending on GC
        assert "alive" in result


class TestSmallIntegerCaching:
    def test_caching(self):
        result = small_integer_caching()
        assert result["256_is_cached"] is True
        assert "lesson" in result


class TestStringInterning:
    def test_interning(self):
        result = string_interning()
        assert result["short_interned"] is True
        assert result["sys.intern"] is True


class TestListMemory:
    def test_list_growth(self):
        result = list_memory_model()
        assert len(result["growth_pattern"]) > 0
        # Bytes should increase
        sizes = [s["bytes"] for s in result["growth_pattern"]]
        assert sizes == sorted(sizes)


class TestDictMemory:
    def test_dict_growth(self):
        result = dict_memory_model()
        assert len(result["insertion_order"]) == 20
        assert result["insertion_order"][0] == "key_0"


class TestSlots:
    def test_slotted_no_dict(self):
        result = slots_memory_comparison()
        assert result["slotted_has_dict"] is False
        assert result["regular_has_dict"] is True

    def test_slotted_cannot_add_attr(self):
        p = SlottedPoint(1, 2)
        with pytest.raises(AttributeError):
            p.z = 3

    def test_regular_can_add_attr(self):
        p = RegularPoint(1, 2)
        p.z = 3  # works fine
        assert p.z == 3


class TestTracemalloc:
    def test_tracemalloc(self):
        result = tracemalloc_demo()
        assert result["total_allocated"] > 0


class TestGetReferrers:
    def test_referrers(self):
        result = get_referrers_demo()
        assert result["referrers_count"] > 0
        assert result["container_is_referrer"] is True


class TestMemoryTypes:
    def test_memory_info(self):
        result = memory_types_info()
        assert "python_heap" in result
        assert "os_rss" in result
