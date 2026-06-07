"""Tests for functional module (08-函数式工具装饰器和上下文管理器)."""

import pytest
from py_proj.functional import (
    ExceptionSuppressor,
    Indenter,
    apply,
    check_wraps_preservation,
    decorated_func,
    first_class_demo,
    lru_cache_demo,
    log_calls,
    make_adder,
    make_counter,
    make_multiplier,
    managed_resource,
    retry,
    simple_decorator,
    timer_context,
    timer_decorator,
)


class TestFirstClassFunctions:
    def test_apply(self):
        assert apply(lambda x: x * 2, 5) == 10

    def test_make_multiplier(self):
        triple = make_multiplier(3)
        assert triple(4) == 12

    def test_first_class_demo(self):
        result = first_class_demo()
        assert result["assigned"] == 9
        assert result["passed"] == 25
        assert result["returned"] == 10


class TestClosures:
    def test_make_adder(self):
        add10 = make_adder(10)
        assert add10(5) == 15
        assert add10(0) == 10

    def test_make_counter(self):
        ctr = make_counter(0)
        assert ctr["get"]() == 0
        assert ctr["increment"]() == 1
        assert ctr["increment"]() == 2
        assert ctr["get"]() == 2


class TestDecorators:
    def test_simple_decorator(self):
        @simple_decorator
        def hello(x: int) -> int:
            return x * 2
        assert hello(5) == 10

    def test_wraps_preservation(self):
        result = check_wraps_preservation()
        assert result["name"] == "documented_function"
        assert result["doc"] == "This is the docstring."
        assert result["value"] == 6

    def test_retry_eventual_success(self, monkeypatch):
        monkeypatch.setattr("py_proj.functional.time.sleep", lambda _: None)
        calls = {"count": 0}

        @retry(max_attempts=3, delay=0.01)
        def flaky() -> str:
            calls["count"] += 1
            if calls["count"] < 2:
                raise RuntimeError("temporary")
            return "ok"

        assert flaky() == "ok"
        assert calls["count"] == 2

    def test_retry_raises_last_exception(self):
        @retry(max_attempts=2)
        def always_fails() -> str:
            raise RuntimeError("failed")

        with pytest.raises(RuntimeError, match="failed"):
            always_fails()

    def test_log_calls(self):
        @log_calls("DEBUG")
        def double(x: int) -> int:
            return x * 2

        assert double(4) == 8

    def test_timer_decorator(self):
        @timer_decorator
        def slow():
            total = 0
            for i in range(10000):
                total += i
            return total

        result = slow()
        assert result == sum(range(10000))
        assert hasattr(slow, "last_elapsed")
        assert slow.last_elapsed >= 0

    def test_decorator_order(self):
        # @decorator_a @decorator_b f(x) → A(B(f(x)))
        result = decorated_func("x")
        assert result == "A(B(x))"


class TestLRUCache:
    def test_lru_cache(self):
        result = lru_cache_demo()
        assert result["result"] == 832040  # fib(30)
        assert result["hits"] > 0


class TestContextManagers:
    def test_indenter(self):
        ind = Indenter()
        assert ind.indent("hello") == "hello"
        with ind:
            assert ind.indent("hello") == "  hello"
            with ind:
                assert ind.indent("hello") == "    hello"
        assert ind.indent("hello") == "hello"

    def test_exception_suppressor(self):
        with ExceptionSuppressor(ValueError) as es:
            raise ValueError("test")
        assert es.suppressed is True

    def test_exception_not_suppressed(self):
        with pytest.raises(TypeError):
            with ExceptionSuppressor(ValueError):
                raise TypeError("not suppressed")


class TestContextlibContextmanager:
    def test_timer_context(self):
        with timer_context() as t:
            total = sum(range(1000))
        assert t["elapsed"] > 0
        assert total == sum(range(1000))

    def test_managed_resource(self):
        with managed_resource("db") as r:
            assert "db" in r
