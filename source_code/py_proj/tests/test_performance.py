"""Tests for performance module (21-性能剖析内存优化和大规模数据处理, 面试05)."""

import pytest
from py_proj.performance import (
    caching_strategy_guide,
    concurrency_overhead_info,
    counter_vs_manual_dict,
    deque_vs_list_pop_left,
    generator_vs_list,
    heap_nlargest_vs_sort,
    join_vs_concat,
    measure,
    memory_profile,
    precompile_regex,
    process_stream_aggregate,
    production_metrics_guide,
    profiling_workflow,
    profile_function,
    set_vs_list_membership,
    stream_lines,
)


class TestMeasure:
    def test_measure_decorator(self):
        @measure
        def slow():
            return sum(range(1000))
        result = slow()
        assert result == sum(range(1000))
        assert slow.last_elapsed >= 0


class TestProfile:
    def test_profile_function(self):
        result = profile_function(sum, range(100))
        assert result["result"] == sum(range(100))
        assert "profile_output" in result

    def test_memory_profile(self):
        result = memory_profile(lambda: list(range(100)))
        assert result["result"] == list(range(100))
        assert result["total_bytes"] > 0


class TestAlgorithmPriority:
    def test_set_vs_list(self):
        result = set_vs_list_membership(1000)
        assert result["set_time"] < result["list_time"]

    def test_counter_vs_manual(self):
        items = ["a", "b", "a", "c", "b", "a"]
        result = counter_vs_manual_dict(items)
        assert result["manual"] == result["counter"]

    def test_deque_vs_list(self):
        result = deque_vs_list_pop_left(1000)
        assert result["deque_time"] < result["list_time"]

    def test_heap_nlargest(self):
        data = [5, 3, 8, 1, 9, 2, 7]
        result = heap_nlargest_vs_sort(data, 3)
        assert result["nlargest"] == result["sorted_top_n"]

    def test_join_vs_concat(self):
        result = join_vs_concat(1000)
        assert result["join_time"] < result["concat_time"]


class TestStreaming:
    def test_stream_lines(self):
        lines = [" hello ", "", " world ", "  "]
        result = list(stream_lines(lines))
        assert result == ["hello", "world"]

    def test_process_stream(self):
        lines = ["hello", "world", "foo"]
        result = process_stream_aggregate(lines)
        assert result["count"] == 3


class TestCaching:
    def test_cached_computation(self):
        from py_proj.performance import cached_computation
        result1 = cached_computation(100)
        result2 = cached_computation(100)
        assert result1 == result2

    def test_strategy_guide(self):
        result = caching_strategy_guide()
        assert "cache_key" in result
        assert "invalidation" in result


class TestGeneratorVsList:
    def test_comparison(self):
        result = generator_vs_list(100)
        assert result["list_sum"] == result["gen_sum"]


class TestRegex:
    def test_precompile(self):
        result = precompile_regex()
        assert result["match"] == "2025-01-15"


class TestGuides:
    def test_profiling_workflow(self):
        result = profiling_workflow()
        assert len(result) == 7

    def test_production_metrics(self):
        result = production_metrics_guide()
        assert "latency_p50" in result
        assert "throughput" in result

    def test_concurrency_overhead(self):
        result = concurrency_overhead_info()
        assert "context_switching" in result
