"""Module 08: Functional Tools, Decorators and Context Managers
(08-函数式工具装饰器和上下文管理器, 面试02)

Knowledge points covered:
- first-class functions (assignable, passable, returnable)
- closures (inner function referencing outer variables)
- decorators (callable that receives and returns a function)
- functools.wraps (preserves original function metadata)
- functools.lru_cache (caching pure function results)
- context manager protocol (__enter__/__exit__)
- contextlib.contextmanager decorator for generator-based context managers
- decorator call order
- decorators with parameters (three-layer function pattern)
- __exit__ returning True suppresses exceptions
"""

from __future__ import annotations

import contextlib
import functools
import time
from typing import Any, Callable, Iterator, TypeVar

F = TypeVar("F", bound=Callable[..., Any])


# ---------------------------------------------------------------------------
# First-class functions
# ---------------------------------------------------------------------------

def apply(func: Callable[[int], int], value: int) -> int:
    """Functions are first-class: can be passed as arguments."""
    return func(value)


def make_multiplier(factor: int) -> Callable[[int], int]:
    """Functions can be returned from other functions."""
    def multiply(x: int) -> int:
        return x * factor
    return multiply


def first_class_demo() -> dict[str, Any]:
    """Demonstrate first-class function capabilities."""
    # Assign function to variable
    square = lambda x: x ** 2  # noqa: E731
    # Pass function as argument
    result = apply(square, 5)
    # Return function from function
    double = make_multiplier(2)
    return {
        "assigned": square(3),
        "passed": result,
        "returned": double(5),
    }


# ---------------------------------------------------------------------------
# Closures
# ---------------------------------------------------------------------------

def make_adder(x: int) -> Callable[[int], int]:
    """Closure: inner function captures enclosing variable x."""
    def adder(y: int) -> int:
        return x + y  # captures x from enclosing scope
    return adder


def make_counter(start: int = 0) -> dict[str, Callable[[], int]]:
    """Closure with mutable state."""
    count = start

    def increment() -> int:
        nonlocal count
        count += 1
        return count

    def get() -> int:
        return count

    return {"increment": increment, "get": get}


# ---------------------------------------------------------------------------
# Decorators
# ---------------------------------------------------------------------------

def simple_decorator(func: F) -> F:
    """Basic decorator: wraps a function to add behavior."""
    @functools.wraps(func)
    def wrapper(*args: Any, **kwargs: Any) -> Any:
        print(f"Calling {func.__name__}")
        result = func(*args, **kwargs)
        print(f"Finished {func.__name__}")
        return result
    return wrapper  # type: ignore


def timer_decorator(func: F) -> F:
    """Decorator that measures execution time."""
    @functools.wraps(func)
    def wrapper(*args: Any, **kwargs: Any) -> Any:
        start = time.perf_counter()
        result = func(*args, **kwargs)
        elapsed = time.perf_counter() - start
        wrapper.last_elapsed = elapsed  # type: ignore
        return result
    return wrapper  # type: ignore


def retry(max_attempts: int = 3, delay: float = 0.0) -> Callable[[F], F]:
    """Decorator with parameters: three-layer function pattern.

    Layer 1: parameters → returns decorator
    Layer 2: decorator → returns wrapper
    Layer 3: wrapper → calls function with retry logic
    """
    def decorator(func: F) -> F:
        @functools.wraps(func)
        def wrapper(*args: Any, **kwargs: Any) -> Any:
            last_exc: Exception | None = None
            for attempt in range(max_attempts):
                try:
                    return func(*args, **kwargs)
                except Exception as exc:
                    last_exc = exc
                    if delay > 0 and attempt < max_attempts - 1:
                        time.sleep(delay)
            raise last_exc  # type: ignore
        return wrapper  # type: ignore
    return decorator


def log_calls(level: str = "INFO") -> Callable[[F], F]:
    """Another parameterized decorator example."""
    def decorator(func: F) -> F:
        @functools.wraps(func)
        def wrapper(*args: Any, **kwargs: Any) -> Any:
            # In real code, use logging module
            result = func(*args, **kwargs)
            return result
        return wrapper  # type: ignore
    return decorator


# ---------------------------------------------------------------------------
# functools.wraps — preserves metadata
# ---------------------------------------------------------------------------

def check_wraps_preservation() -> dict[str, Any]:
    """functools.wraps preserves __name__, __doc__, __module__."""
    @simple_decorator
    def documented_function(x: int) -> int:
        """This is the docstring."""
        return x * 2

    return {
        "name": documented_function.__name__,
        "doc": documented_function.__doc__,
        "value": documented_function(3),
    }


# ---------------------------------------------------------------------------
# functools.lru_cache
# ---------------------------------------------------------------------------

@functools.lru_cache(maxsize=128)
def fibonacci_cached(n: int) -> int:
    """LRU cache: caches results of pure functions."""
    if n < 2:
        return n
    return fibonacci_cached(n - 1) + fibonacci_cached(n - 2)


def lru_cache_demo() -> dict[str, Any]:
    """Demonstrate lru_cache performance benefits."""
    # First call: computes
    result = fibonacci_cached(30)
    # Second call: cached
    cache_info = fibonacci_cached.cache_info()
    return {
        "result": result,
        "hits": cache_info.hits,
        "misses": cache_info.misses,
        "size": cache_info.currsize,
    }


# ---------------------------------------------------------------------------
# Context managers (__enter__/__exit__)
# ---------------------------------------------------------------------------

class Indenter:
    """Context manager that increases indentation level."""

    def __init__(self, prefix: str = "  ") -> None:
        self.prefix = prefix
        self.level = 0

    def __enter__(self) -> Indenter:
        self.level += 1
        return self

    def __exit__(self, exc_type: type | None, exc_val: BaseException | None,
                 exc_tb: Any) -> bool:
        self.level -= 1
        return False  # don't suppress exceptions

    def indent(self, text: str) -> str:
        return self.prefix * self.level + text


class ExceptionSuppressor:
    """Context manager where __exit__ returns True to suppress exceptions."""

    def __init__(self, exc_type: type[BaseException]) -> None:
        self.exc_type = exc_type
        self.suppressed = False

    def __enter__(self) -> ExceptionSuppressor:
        return self

    def __exit__(self, exc_type: type | None, exc_val: BaseException | None,
                 exc_tb: Any) -> bool:
        if exc_type is not None and issubclass(exc_type, self.exc_type):
            self.suppressed = True
            return True  # suppress the exception
        return False


# ---------------------------------------------------------------------------
# contextlib.contextmanager — generator-based context managers
# ---------------------------------------------------------------------------

@contextlib.contextmanager
def timer_context() -> Iterator[dict[str, float]]:
    """Generator-based context manager using @contextmanager."""
    start = time.perf_counter()
    result: dict[str, float] = {"elapsed": 0.0}
    try:
        yield result
    finally:
        result["elapsed"] = time.perf_counter() - start


@contextlib.contextmanager
def managed_resource(name: str) -> Iterator[str]:
    """Simulate resource management with @contextmanager."""
    resource = f"resource:{name}:open"
    try:
        yield resource
    finally:
        # cleanup always runs
        pass


# ---------------------------------------------------------------------------
# Decorator call order
# ---------------------------------------------------------------------------

def decorator_a(func: F) -> F:
    @functools.wraps(func)
    def wrapper(*args: Any, **kwargs: Any) -> Any:
        return f"A({func(*args, **kwargs)})"
    return wrapper  # type: ignore


def decorator_b(func: F) -> F:
    @functools.wraps(func)
    def wrapper(*args: Any, **kwargs: Any) -> Any:
        return f"B({func(*args, **kwargs)})"
    return wrapper  # type: ignore


@decorator_a
@decorator_b
def decorated_func(x: str) -> str:
    """Decorator order: @a @b f → a(b(f(x))) — bottom-up application."""
    return x
