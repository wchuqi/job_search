"""Module 21: Performance Profiling, Memory Optimization and Large Data (21-性能剖析内存优化和大规模数据处理, 面试05)

Knowledge points covered:
- "measure first, then optimize" workflow
- 7-step profiling process
- time measurement (perf_counter, cProfile, pstats)
- memory profiling (tracemalloc)
- common memory problems
- algorithm and data structure priority
- IO reduction
- streaming large file processing
- caching strategy (lru_cache)
- avoiding unnecessary object allocation
- concurrency is not a panacea
- production metrics (P50/P95/P99)
"""

from __future__ import annotations

import cProfile
import functools
import io
import pstats
import time
import tracemalloc
from collections import Counter, deque
from typing import Any, Callable, Iterator, TypeVar

F = TypeVar("F", bound=Callable[..., Any])


# ---------------------------------------------------------------------------
# Performance measurement
# ---------------------------------------------------------------------------

def measure(func: F) -> F:
    """Decorator to measure function execution time."""
    @functools.wraps(func)
    def wrapper(*args: Any, **kwargs: Any) -> Any:
        start = time.perf_counter()
        result = func(*args, **kwargs)
        elapsed = time.perf_counter() - start
        wrapper.last_elapsed = elapsed  # type: ignore
        return result
    return wrapper  # type: ignore


def profile_function(func: Callable[..., Any], *args: Any, **kwargs: Any) -> dict[str, Any]:
    """Profile a function using cProfile."""
    profiler = cProfile.Profile()
    profiler.enable()
    result = func(*args, **kwargs)
    profiler.disable()

    stream = io.StringIO()
    stats = pstats.Stats(profiler, stream=stream)
    stats.sort_stats("cumulative")
    stats.print_stats(10)

    return {
        "result": result,
        "profile_output": stream.getvalue(),
    }


# ---------------------------------------------------------------------------
# Memory profiling
# ---------------------------------------------------------------------------

def memory_profile(func: Callable[..., Any], *args: Any, **kwargs: Any) -> dict[str, Any]:
    """Profile memory usage using tracemalloc."""
    tracemalloc.start()
    result = func(*args, **kwargs)
    snapshot = tracemalloc.take_snapshot()
    stats = snapshot.statistics("lineno")
    tracemalloc.stop()

    return {
        "result": result,
        "top_allocations": [
            {"file": str(s), "size_bytes": s.size}
            for s in stats[:5]
        ],
        "total_bytes": sum(s.size for s in stats),
    }


# ---------------------------------------------------------------------------
# Algorithm and data structure priority
# ---------------------------------------------------------------------------

def set_vs_list_membership(n: int = 10000) -> dict[str, float]:
    """x in set is O(1), x in list is O(n)."""
    data_list = list(range(n))
    data_set = set(range(n))
    target = n - 1

    start = time.perf_counter()
    for _ in range(1000):
        target in data_list
    list_time = time.perf_counter() - start

    start = time.perf_counter()
    for _ in range(1000):
        target in data_set
    set_time = time.perf_counter() - start

    return {"list_time": list_time, "set_time": set_time}


def counter_vs_manual_dict(items: list[str]) -> dict[str, Any]:
    """Counter is faster than manual dict counting."""
    # Manual
    manual: dict[str, int] = {}
    for item in items:
        manual[item] = manual.get(item, 0) + 1

    # Counter
    counted = dict(Counter(items))

    return {"manual": manual, "counter": counted}


def deque_vs_list_pop_left(n: int = 10000) -> dict[str, float]:
    """deque.popleft() is O(1), list.pop(0) is O(n)."""
    dq: deque[int] = deque(range(n))
    lst = list(range(n))

    start = time.perf_counter()
    for _ in range(min(n, 1000)):
        dq.popleft()
    deque_time = time.perf_counter() - start

    lst = list(range(n))
    start = time.perf_counter()
    for _ in range(min(n, 1000)):
        lst.pop(0)
    list_time = time.perf_counter() - start

    return {"deque_time": deque_time, "list_time": list_time}


def heap_nlargest_vs_sort(data: list[int], n: int = 5) -> dict[str, Any]:
    """heapq.nlargest is O(n log k), sorted is O(n log n)."""
    import heapq
    return {
        "nlargest": heapq.nlargest(n, data),
        "sorted_top_n": sorted(data, reverse=True)[:n],
    }


def join_vs_concat(n: int = 10000) -> dict[str, float]:
    """''.join() is O(n), += in loop is O(n²) for strings."""
    parts = [str(i) for i in range(n)]

    start = time.perf_counter()
    result = ""
    for p in parts:
        result += p
    concat_time = time.perf_counter() - start

    start = time.perf_counter()
    "".join(parts)
    join_time = time.perf_counter() - start

    return {"concat_time": concat_time, "join_time": join_time}


# ---------------------------------------------------------------------------
# Streaming large file processing
# ---------------------------------------------------------------------------

def stream_lines(lines: list[str]) -> Iterator[str]:
    """Generator for streaming line processing."""
    for line in lines:
        stripped = line.strip()
        if stripped:
            yield stripped


def process_stream_aggregate(lines: list[str]) -> dict[str, Any]:
    """Process stream with small aggregate state (no full list in memory)."""
    total = 0
    count = 0
    for line in stream_lines(lines):
        total += len(line)
        count += 1
    return {"count": count, "total_length": total}


# ---------------------------------------------------------------------------
# Caching strategy
# ---------------------------------------------------------------------------

@functools.lru_cache(maxsize=256)
def cached_computation(n: int) -> int:
    """Pure function with lru_cache — only computes once per unique input."""
    total = 0
    for i in range(n):
        total += i * i
    return total


def caching_strategy_guide() -> dict[str, str]:
    """Questions to ask about caching."""
    return {
        "cache_key": "What uniquely identifies a cached result?",
        "invalidation": "When and how is the cache invalidated?",
        "maxsize": "What's the maximum memory for the cache?",
        "error_caching": "Should errors be cached or retried?",
        "ttl": "Does the cache need a time-to-live?",
        "thread_safety": "Is the cache accessed from multiple threads?",
    }


# ---------------------------------------------------------------------------
# Avoid unnecessary object allocation
# ---------------------------------------------------------------------------

def generator_vs_list(n: int = 1000) -> dict[str, Any]:
    """Generators avoid creating intermediate lists."""
    # Bad: creates full list in memory
    squares_list = [x ** 2 for x in range(n)]
    # Good: generates values lazily
    squares_gen = (x ** 2 for x in range(n))
    return {
        "list_memory": "O(n) — all values in memory",
        "gen_memory": "O(1) — one value at a time",
        "list_sum": sum(squares_list),
        "gen_sum": sum(squares_gen),
    }


def precompile_regex() -> dict[str, str]:
    """Pre-compile regex patterns used multiple times."""
    import re
    pattern = re.compile(r"\d{4}-\d{2}-\d{2}")
    match = pattern.search("date: 2025-01-15")
    return {
        "compiled": str(pattern),
        "match": match.group() if match else "no match",
        "benefit": "Pre-compilation avoids re-parsing the pattern each time",
    }


# ---------------------------------------------------------------------------
# Production metrics
# ---------------------------------------------------------------------------

def production_metrics_guide() -> dict[str, str]:
    """Key production metrics to track."""
    return {
        "latency_p50": "Median response time",
        "latency_p95": "95th percentile — catches tail latency",
        "latency_p99": "99th percentile — catches worst-case outliers",
        "throughput": "Requests/operations per second",
        "error_rate": "Percentage of failed operations",
        "cpu_usage": "Process/system CPU utilization",
        "memory_usage": "Heap, RSS, swap usage",
        "queue_length": "Pending work items",
        "dependency_latency": "External service response times",
    }


# ---------------------------------------------------------------------------
# 7-step profiling process
# ---------------------------------------------------------------------------

def profiling_workflow() -> list[str]:
    """The 7-step profiling workflow."""
    return [
        "1. Define the metric (latency, memory, throughput)",
        "2. Fix the input (reproducible test case)",
        "3. Measure the baseline",
        "4. Locate the hotspot (cProfile, tracemalloc)",
        "5. Make the minimal optimization",
        "6. Verify the improvement (regression test)",
        "7. Record the tradeoff (what was sacrificed)",
    ]


# ---------------------------------------------------------------------------
# Concurrency is not a panacea
# ---------------------------------------------------------------------------

def concurrency_overhead_info() -> dict[str, str]:
    """Concurrency has overhead — not always beneficial."""
    return {
        "context_switching": "Thread switching has CPU cost",
        "lock_contention": "Locks serialize and add overhead",
        "serialization": "Data must be serialized between processes (pickle)",
        "scheduling": "OS scheduler adds unpredictable latency",
        "rate_limiting": "External services may rate-limit concurrent requests",
        "memory": "Each thread/process has stack memory overhead",
    }
