"""Tests for concurrency module (12-并发异步和性能, 20-并发异步事件循环和GIL深入, 面试05)."""

import asyncio
import pytest
from py_proj.concurrency import (
    ThreadSafeCounter,
    async_cancellation_demo,
    async_gather_demo,
    async_taskgroup_demo,
    async_semaphore_demo,
    async_task,
    async_to_thread_demo,
    async_with_timeout,
    caching_info,
    cpu_bound_task,
    gil_info,
    measure_performance,
    model_selection_guide,
    process_pool_demo,
    rlock_demo,
    semaphore_demo,
    stream_process_lines,
    thread_pool_demo,
    thread_safety_pitfall,
    thread_safety_with_lock,
)


class TestGIL:
    def test_gil_info(self):
        result = gil_info()
        assert "mechanism" in result
        assert "cpu_bound" in result
        assert "io_bound" in result


class TestThreadSafeCounter:
    def test_increment(self):
        c = ThreadSafeCounter()
        c.increment()
        c.increment()
        assert c.value == 2

    def test_thread_safety(self):
        c = ThreadSafeCounter()
        import threading
        threads = [threading.Thread(target=lambda: [c.increment() for _ in range(1000)]) for _ in range(10)]
        for t in threads:
            t.start()
        for t in threads:
            t.join()
        assert c.value == 10000


class TestThreadPool:
    def test_thread_pool(self):
        result = thread_pool_demo(8)
        assert result["completed"] == 8


class TestThreadingPrimitives:
    def test_rlock(self):
        assert rlock_demo() == "nested lock acquired"

    def test_semaphore(self):
        assert semaphore_demo(2) == [
            "worker_0",
            "worker_1",
            "worker_2",
            "worker_3",
            "worker_4",
            "worker_5",
        ]


class TestThreadSafety:
    def test_pitfall(self):
        """Race condition: actual may differ from expected."""
        result = thread_safety_pitfall()
        # Due to race condition, actual might be less than expected
        # We just verify the structure
        assert "actual" in result
        assert "expected" in result

    def test_with_lock(self):
        result = thread_safety_with_lock()
        assert result["actual"] == result["expected"]


class TestProcessPool:
    def test_cpu_bound(self):
        result = cpu_bound_task(100)
        expected = sum(i * i for i in range(100))
        assert result == expected

    @pytest.mark.slow
    def test_process_pool(self):
        result = process_pool_demo(2)
        assert result["completed"] == 2


class TestAsync:
    def test_async_task(self):
        result = asyncio.run(async_task(1, 0.001))
        assert result == "async_task_1"

    def test_async_gather(self):
        result = asyncio.run(async_gather_demo(3))
        assert len(result) == 3

    def test_async_with_timeout(self):
        result = asyncio.run(async_with_timeout())
        assert result is not None

    def test_async_timeout_branch(self, monkeypatch):
        async def fake_wait_for(coro, timeout):
            coro.close()
            raise TimeoutError

        monkeypatch.setattr("py_proj.concurrency.asyncio.wait_for", fake_wait_for)
        assert asyncio.run(async_with_timeout()) is None

    def test_cancellation(self):
        result = asyncio.run(async_cancellation_demo())
        assert result["cancelled"] is True
        assert result["cleaned_up"] is True

    def test_semaphore(self):
        result = asyncio.run(async_semaphore_demo(2))
        assert len(result) == 5

    def test_to_thread(self):
        result = asyncio.run(async_to_thread_demo())
        assert "result=" in result

    def test_taskgroup(self):
        assert asyncio.run(async_taskgroup_demo()) == [
            "async_task_0",
            "async_task_1",
            "async_task_2",
        ]


class TestStreaming:
    def test_stream_process(self):
        lines = ["hello", "world", "foo"]
        result = stream_process_lines(lines)
        assert result["count"] == 3
        assert result["total_length"] == 13


class TestMeasurement:
    def test_measure(self):
        result = measure_performance(sum, range(100))
        assert result["result"] == sum(range(100))
        assert result["elapsed_seconds"] >= 0


class TestCaching:
    def test_caching_info(self):
        result = caching_info()
        assert "lru_cache" in result
        assert "invalidation" in result


class TestModelSelection:
    def test_guide(self):
        result = model_selection_guide()
        assert "simple_sync" in result
        assert "asyncio" in result
        assert "process_pool" in result
