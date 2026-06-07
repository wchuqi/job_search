"""Tests for data_structures module (02-内置类型和数据结构, 面试01)."""

import copy
import pytest
from py_proj.data_structures import (
    deep_copy_demo,
    dict_comprehension_demo,
    dict_hash_requirement,
    dict_operations,
    hashing_demo,
    list_comprehension_demo,
    list_operations,
    nested_list_trap,
    set_comprehension_demo,
    set_deduplication,
    set_membership_test,
    set_operations_demo,
    shallow_copy_demo,
    shared_reference_pitfall,
    str_encoding_demo,
    str_formatting,
    str_immutability_demo,
    str_join_vs_concat,
    str_slicing,
    tuple_demo,
    tuple_hashability,
    unpacking_demo,
)


class TestString:
    def test_slicing(self):
        result = str_slicing("abcdef")
        assert result["first3"] == "abc"
        assert result["last3"] == "def"
        assert result["middle"] == "bcde"
        assert result["reverse"] == "fedcba"
        assert result["every_other"] == "ace"

    def test_formatting(self):
        result = str_formatting("Alice", 30, 95.678)
        assert "Alice" in result["fstring"]
        assert "30" in result["fstring"]
        assert "95.7" in result["fstring"]

    def test_immutability(self):
        upper = str_immutability_demo()
        assert upper == "HELLO"

    def test_join_vs_concat(self):
        items = ["a", "b", "c", "d"]
        joined, concat = str_join_vs_concat(items)
        assert joined == "abcd"
        assert concat == "abcd"
        assert joined == concat

    def test_encoding(self):
        result = str_encoding_demo("hello")
        assert result["utf8_bytes"] == b"hello"
        assert result["utf8_decoded"] == "hello"


class TestList:
    def test_operations(self):
        result = list_operations()
        assert result["original"] == [3, 1, 4, 1, 5, 9]
        assert result["sorted"] == [1, 1, 3, 4, 5, 9]

    def test_append_is_fast(self):
        lst = [1, 2, 3]
        lst.append(4)
        assert lst == [1, 2, 3, 4]

    def test_comprehension(self):
        result = list_comprehension_demo()
        assert result["squares"] == [0, 1, 4, 9, 16, 25, 36, 49, 64, 81]
        assert result["even_squares"] == [0, 4, 16, 36, 64]

    def test_unpacking(self):
        result = unpacking_demo(1, 2, 3)
        assert result["first"] == 1
        assert result["middle"] == [2, 3, 4]
        assert result["last"] == 5


class TestTuple:
    def test_properties(self):
        result = tuple_demo()
        assert result["immutable"] == (1, 2, 3)
        assert isinstance(result["hashable"], int)
        assert result["single_element_trap"] == (1,)

    def test_hashability(self):
        result = tuple_hashability()
        assert result["hashable_tuple"] is True
        assert result["tuple_with_list"] is False


class TestDict:
    def test_operations(self):
        result = dict_operations()
        assert result["get_with_default"] == 0
        assert len(result["items"]) == 3
        assert "a" in result["keys"]

    def test_comprehension(self):
        result = dict_comprehension_demo()
        assert result == {0: 0, 1: 1, 2: 4, 3: 9, 4: 16}

    def test_hash_requirement(self):
        result = dict_hash_requirement()
        assert result["invalid_works"] is False

    def test_merge_operator(self):
        result = dict_operations()
        assert "d" in result["merged"]
        assert "e" in result["dict_merge_operator"]


class TestSet:
    def test_operations(self):
        a = {1, 2, 3, 4}
        b = {3, 4, 5, 6}
        result = set_operations_demo(a, b)
        assert result["union"] == {1, 2, 3, 4, 5, 6}
        assert result["intersection"] == {3, 4}
        assert result["difference"] == {1, 2}
        assert result["symmetric_difference"] == {1, 2, 5, 6}

    def test_deduplication(self):
        result = set_deduplication([1, 2, 2, 3, 3, 3])
        assert result == [1, 2, 3]

    def test_comprehension(self):
        assert set_comprehension_demo() == {0, 1, 2}

    def test_membership_performance(self):
        result = set_membership_test()
        assert result["set_time"] < result["list_time"]


class TestCopy:
    def test_shallow_copy(self):
        result = shallow_copy_demo()
        assert result["same_inner"] is True
        assert result["different_outer"] is False
        assert result["original"] == [[99, 2], [3, 4]]

    def test_deep_copy(self):
        result = deep_copy_demo()
        assert result["same_inner"] is False
        assert result["original"] == [[1, 2], [3, 4]]
        assert result["deep"] == [[99, 2], [3, 4]]

    def test_nested_trap(self):
        result = nested_list_trap()
        assert result["bad"] == [[1, 0, 0], [1, 0, 0]]  # both rows modified
        assert result["good"] == [[1, 0, 0], [0, 0, 0]]  # only first


class TestSharedReference:
    def test_shared_reference(self):
        result = shared_reference_pitfall()
        assert result["same_object"] is True
        assert result["a"]["x"] == 99


class TestHashing:
    def test_hash_types(self):
        result = hashing_demo()
        assert isinstance(result["int_hash"], int)
        assert isinstance(result["str_hash"], int)
        assert isinstance(result["tuple_hash"], int)
