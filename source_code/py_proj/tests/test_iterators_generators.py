"""Tests for iterators_generators module (07-迭代器生成器和推导式)."""

import pytest
from py_proj.iterators_generators import (
    CountDown,
    CountDownIterator,
    chunk_reader,
    count_down_gen,
    dict_comprehension_demo,
    fibonacci,
    flatten,
    generator_expression_demo,
    generator_statefulness_demo,
    iteration_protocol_demo,
    lazy_pipeline_demo,
    list_comprehension_demo,
    process_large_data_stream,
    read_lines_lazy,
    set_comprehension_demo,
)


class TestIterableVsIterator:
    def test_countdown(self):
        cd = CountDown(5)
        assert list(cd) == [5, 4, 3, 2, 1]

    def test_iterator_iter_returns_self(self):
        iterator = CountDownIterator(2)
        assert iter(iterator) is iterator
        assert list(iterator) == [2, 1]

    def test_multiple_iterations(self):
        cd = CountDown(3)
        assert list(cd) == [3, 2, 1]
        assert list(cd) == [3, 2, 1]  # iterable creates new iterator

    def test_iteration_protocol(self):
        result = iteration_protocol_demo(5)
        assert result == [0, 1, 2, 3, 4]

    def test_iterator_statefulness(self):
        result = generator_statefulness_demo()
        assert result["first"] == [3, 2, 1]
        assert result["second"] == []  # consumed


class TestGenerators:
    def test_count_down_gen(self):
        assert list(count_down_gen(5)) == [5, 4, 3, 2, 1]

    def test_fibonacci(self):
        result = list(fibonacci(20))
        assert result == [0, 1, 1, 2, 3, 5, 8, 13]

    def test_fibonacci_empty(self):
        assert list(fibonacci(0)) == []

    def test_read_lines_lazy(self):
        lines = ["  hello  ", "", "  world  ", "  ", "foo"]
        result = list(read_lines_lazy(lines))
        assert result == ["hello", "world", "foo"]


class TestGeneratorExpressions:
    def test_generator_expression(self):
        result = generator_expression_demo(5)
        assert result["list_result"] == [0, 1, 4, 9, 16]
        assert result["gen_sum"] == 30

    def test_generator_type(self):
        result = generator_expression_demo(3)
        assert "generator" in result["gen_type"]


class TestComprehensions:
    def test_list_comprehension(self):
        result = list_comprehension_demo([1, 2, 3, 4, 5])
        assert result["squares"] == [1, 4, 9, 16, 25]
        assert result["even_only"] == [2, 4]
        assert 4 in result["transform_and_filter"]

    def test_dict_comprehension(self):
        result = dict_comprehension_demo(["a", "b", "c"], [1, -2, 3])
        assert result == {"a": 1, "c": 3}

    def test_set_comprehension(self):
        result = set_comprehension_demo([1, 2, 3, 4, 5, 6])
        assert result == {0, 1, 2}


class TestLazyEvaluation:
    def test_lazy_pipeline(self):
        result = lazy_pipeline_demo(["  hello  ", "", " world ", "  ", "FOO"])
        assert result == ["HELLO", "WORLD", "FOO"]

    def test_large_data_stream(self):
        data = iter([1, 2, 3, 4, 5])
        result = process_large_data_stream(data)
        assert result["total"] == 15
        assert result["count"] == 5
        assert result["max"] == 5


class TestChunkReader:
    def test_exact_chunks(self):
        lines = ["a", "b", "c", "d", "e", "f"]
        chunks = list(chunk_reader(lines, 3))
        assert chunks == [["a", "b", "c"], ["d", "e", "f"]]

    def test_partial_chunk(self):
        lines = ["a", "b", "c", "d", "e"]
        chunks = list(chunk_reader(lines, 3))
        assert chunks == [["a", "b", "c"], ["d", "e"]]


class TestFlatten:
    def test_nested(self):
        result = list(flatten([1, [2, 3], [4, [5, 6]]]))
        assert result == [1, 2, 3, 4, 5, 6]

    def test_flat(self):
        result = list(flatten([1, 2, 3]))
        assert result == [1, 2, 3]

    def test_empty(self):
        result = list(flatten([]))
        assert result == []
