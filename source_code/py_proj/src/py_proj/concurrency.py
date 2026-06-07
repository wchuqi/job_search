"""Module 12: Concurrency, Async and Performance (12-并发异步和性能, 20-并发异步事件循环和GIL深入, 面试05)

Knowledge points covered:
- threads (blocking IO, shared memory, locks)
- processes (CPU-bound, inter-process communication cost)
- coroutines (large-scale async IO, cannot call blocking in event loop)
- GIL (CPython global interpreter lock)
- caching (repeated computation, invalidation, memory)
- streaming processing (large files/data streams)
- concurrent.futures.ThreadPoolExecutor and ProcessPoolExecutor
- asyncio.gather
- time.perf_counter for measurement
- bottleneck-driven model selection
- thread model (Lock, RLock, Semaphore, Event, Queue)
- thread safety pitfalls
- asyncio tasks, cancellation, timeout
- blocking calls destroying event loops
- asyncio.to_thread
- concurrency limiting and backpressure (Semaphore, bounded queues)
- asyncio.TaskGroup (Python 3.11+)
- selection table (sync/thread/async/process)
"""

from __future__ import annotations

import asyncio
import concurrent.futures
import functools
import threading
import time
from typing import Any, Callable, TypeVar

F = TypeVar("F", bound=Callable[..., Any])


# ---------------------------------------------------------------------------
# GIL — Global Interpreter Lock
# ---------------------------------------------------------------------------

def gil_info() -> dict[str, str]:
    """GIL: CPython allows only one thread to execute Python bytecode at a time.

    Impact: CPU-bound multi-threading limited; IO-bound threads still beneficial.
    Bypass: use multiprocessing or C extensions that release GIL.
    """
    return {
        "mechanism": "CPython interpreter lock, one thread executes bytecode at a time",
        "cpu_bound": "Multi-threading provides no speedup for pure Python CPU-bound work",
        "io_bound": "Multi-threading still beneficial for IO-bound tasks (lock released during IO)",
        "bypass": "Use multiprocessing or C extensions that release GIL",
        "note": "GIL is a CPython implementation detail, not a language requirement",
    }


# ---------------------------------------------------------------------------
# Threading
# ---------------------------------------------------------------------------

class ThreadSafeCounter:
    """Thread-safe counter using Lock."""

    def __init__(self) -> None:
        self._value = 0
        self._lock = threading.Lock()

    def increment(self) -> None:
        with self._lock:
            self._value += 1

    @property
    def value(self) -> int:
        return self._value


def thread_pool_demo(n_tasks: int = 10) -> dict[str, Any]:
    """ThreadPoolExecutor for IO-bound tasks."""
    import io

    def io_task(task_id: int) -> str:
        time.sleep(0.01)  # simulate IO
        return f"task_{task_id}_done"

    results: list[str] = []
    with concurrent.futures.ThreadPoolExecutor(max_workers=4) as executor:
        futures = [executor.submit(io_task, i) for i in range(n_tasks)]
        for future in concurrent.futures.as_completed(futures):
            results.append(future.result())

    return {"completed": len(results), "results": sorted(results)}


def thread_safety_pitfall() -> dict[str, int]:
    """GIL does NOT guarantee business-level atomicity.

    This demonstrates a race condition even with GIL.
    """
    shared_counter = 0
    n_iterations = 10000

    def increment_many() -> None:
        nonlocal shared_counter
        for _ in range(n_iterations):
            shared_counter += 1  # NOT atomic: read + add + write

    threads = [threading.Thread(target=increment_many) for _ in range(2)]
    for t in threads:
        t.start()
    for t in threads:
        t.join()

    expected = n_iterations * 2
    return {"actual": shared_counter, "expected": expected, "lost": expected - shared_counter}


def thread_safety_with_lock() -> dict[str, int]:
    """Fix race condition with Lock."""
    shared_counter = 0
    lock = threading.Lock()
    n_iterations = 10000

    def increment_many() -> None:
        nonlocal shared_counter
        for _ in range(n_iterations):
            with lock:
                shared_counter += 1

    threads = [threading.Thread(target=increment_many) for _ in range(2)]
    for t in threads:
        t.start()
    for t in threads:
        t.join()

    return {"actual": shared_counter, "expected": n_iterations * 2}


# ---------------------------------------------------------------------------
# RLock, Semaphore, Event, Queue
# ---------------------------------------------------------------------------

def rlock_demo() -> str:
    """RLock: reentrant lock — same thread can acquire multiple times."""
    lock = threading.RLock()
    with lock:
        with lock:  # would deadlock with Lock, works with RLock
            return "nested lock acquired"


def semaphore_demo(n: int = 3) -> list[str]:
    """Semaphore: limit concurrent access to n."""
    sem = threading.Semaphore(n)
    results: list[str] = []
    results_lock = threading.Lock()

    def worker(worker_id: int) -> None:
        with sem:
            time.sleep(0.01)
            with results_lock:
                results.append(f"worker_{worker_id}")

    threads = [threading.Thread(target=worker, args=(i,)) for i in range(6)]
    for t in threads:
        t.start()
    for t in threads:
        t.join()

    return sorted(results)


# ---------------------------------------------------------------------------
# Process pool — CPU-bound tasks
# ---------------------------------------------------------------------------

def cpu_bound_task(n: int) -> int:
    """CPU-bound computation."""
    return sum(i * i for i in range(n))


def process_pool_demo(n_tasks: int = 4) -> dict[str, Any]:
    """ProcessPoolExecutor for CPU-bound tasks (bypasses GIL)."""
    with concurrent.futures.ProcessPoolExecutor(max_workers=2) as executor:
        futures = [executor.submit(cpu_bound_task, 10000) for _ in range(n_tasks)]
        results = [f.result() for f in concurrent.futures.as_completed(futures)]
    return {"completed": len(results), "sample_result": results[0] if results else 0}


# ---------------------------------------------------------------------------
# Async / await
# ---------------------------------------------------------------------------

async def async_task(task_id: int, delay: float = 0.01) -> str:
    """Coroutine: async IO task."""
    await asyncio.sleep(delay)
    return f"async_task_{task_id}"


async def async_gather_demo(n: int = 5) -> list[str]:
    """asyncio.gather: run multiple coroutines concurrently."""
    results = await asyncio.gather(*(async_task(i) for i in range(n)))
    return list(results)


async def async_with_timeout() -> str | None:
    """asyncio.wait_for: add timeout to coroutines."""
    try:
        result = await asyncio.wait_for(async_task(0, delay=0.01), timeout=1.0)
        return result
    except TimeoutError:
        return None


async def async_cancellation_demo() -> dict[str, Any]:
    """Demonstrate task cancellation with cleanup."""
    cleaned_up = False

    async def long_task() -> str:
        nonlocal cleaned_up
        try:
            await asyncio.sleep(10)
            return "completed"  # pragma: no cover - demo cancels before completion
        except asyncio.CancelledError:
            cleaned_up = True
            raise

    task = asyncio.create_task(long_task())
    await asyncio.sleep(0.01)
    task.cancel()
    try:
        await task
    except asyncio.CancelledError:
        pass

    return {"cancelled": True, "cleaned_up": cleaned_up}


async def async_semaphore_demo(n: int = 2) -> list[str]:
    """asyncio.Semaphore: limit concurrent async tasks."""
    sem = asyncio.Semaphore(n)
    results: list[str] = []

    async def worker(wid: int) -> None:
        async with sem:
            await asyncio.sleep(0.01)
            results.append(f"worker_{wid}")

    await asyncio.gather(*(worker(i) for i in range(5)))
    return sorted(results)


async def async_to_thread_demo() -> str:
    """asyncio.to_thread: run blocking function in thread pool (Python 3.9+)."""
    result = await asyncio.to_thread(cpu_bound_task, 1000)
    return f"result={result}"


async def async_taskgroup_demo() -> list[str]:
    """asyncio.TaskGroup: structured concurrency (Python 3.11+)."""
    tasks: list[asyncio.Task[str]] = []
    async with asyncio.TaskGroup() as tg:
        for i in range(3):
            tasks.append(tg.create_task(async_task(i)))
    return [t.result() for t in tasks]





# ---------------------------------------------------------------------------
# Caching
# ---------------------------------------------------------------------------

def caching_info() -> dict[str, str]:
    """Caching strategies and considerations."""
    return {
        "lru_cache": "functools.lru_cache for pure functions with hashable args",
        "ttl_cache": "TTL cache for time-sensitive data (third-party cachetools)",
        "invalidation": "Cache invalidation is the hardest problem in CS",
        "memory": "Always set maxsize to prevent unbounded memory growth",
        "hashability": "Cached function args must be hashable",
    }


# ---------------------------------------------------------------------------
# Streaming processing
# ---------------------------------------------------------------------------

def stream_process_lines(lines: list[str]) -> dict[str, Any]:
    """Process data as a stream — avoid loading everything into memory."""
    total_length = 0
    count = 0
    for line in lines:
        total_length += len(line)
        count += 1
    return {"count": count, "total_length": total_length, "avg_length": total_length / max(count, 1)}


# ---------------------------------------------------------------------------
# Performance measurement
# ---------------------------------------------------------------------------

def measure_performance(func: Callable[..., Any], *args: Any, **kwargs: Any) -> dict[str, Any]:
    """Measure function execution time using perf_counter."""
    start = time.perf_counter()
    result = func(*args, **kwargs)
    elapsed = time.perf_counter() - start
    return {"result": result, "elapsed_seconds": elapsed}


# ---------------------------------------------------------------------------
# Model selection table
# ---------------------------------------------------------------------------

def model_selection_guide() -> dict[str, str]:
    """When to use which concurrency model."""
    return {
        "simple_sync": "Simple IO, few connections — use synchronous code",
        "thread_pool": "Multiple file/network IO (blocking) — ThreadPoolExecutor",
        "asyncio": "Many async connections (non-blocking) — asyncio",
        "process_pool": "CPU-bound work — ProcessPoolExecutor",
        "mixed": "Mixed workload — layered: asyncio + to_thread for blocking",
    }
