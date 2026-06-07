"""Module 18: Type Hints, Generics and Static Analysis (18-类型提示泛型和静态分析, 面试06)

Knowledge points covered:
- type hints purpose (readability, tool checking, interface contracts)
- basic annotations (int, str, list[str], dict[str, int], str | None, Callable)
- Any vs object
- generics with TypeVar
- Protocol (structural subtyping)
- TypedDict (typed dictionary)
- Literal (constrained values)
- dataclass vs NamedTuple vs dict vs TypedDict
- type narrowing (isinstance, is None, control flow)
- runtime validation still needed
- tool chain (Ruff, Black, mypy, pyright, pytest)
- gradual typing strategy
"""

from __future__ import annotations

from dataclasses import dataclass
from typing import Any, Callable, Generic, NamedTuple, Protocol, TypeVar, TypedDict, Literal, runtime_checkable


# ---------------------------------------------------------------------------
# Basic type annotations
# ---------------------------------------------------------------------------

def add(a: int, b: int) -> int:
    """Basic type annotations on function parameters and return."""
    return a + b


def process_items(items: list[str], config: dict[str, int]) -> dict[str, Any]:
    """Compound type annotations: list[str], dict[str, int]."""
    return {
        "count": len(items),
        "total": sum(config.values()),
        "items": items,
    }


def optional_param(name: str, default: str | None = None) -> str:
    """str | None union syntax (Python 3.10+)."""
    return default if default is not None else name


def callable_annotation(func: Callable[[str], int], value: str) -> int:
    """Callable type hint for function parameters."""
    return func(value)


# ---------------------------------------------------------------------------
# Any vs object
# ---------------------------------------------------------------------------

def any_demo(value: Any) -> Any:
    """Any: escape hatch, skips all type checking.

    Use at boundaries (external input, untyped libraries).
    """
    return value


def object_demo(value: object) -> str:
    """object: any object, but can only use methods available on all objects."""
    return str(value)  # str() works on any object


# ---------------------------------------------------------------------------
# Generics with TypeVar
# ---------------------------------------------------------------------------

T = TypeVar("T")
K = TypeVar("K")
V = TypeVar("V")


def first(items: list[T]) -> T:
    """Generic function: works with any type, preserves type info."""
    if not items:
        raise ValueError("empty list")
    return items[0]


def merge_dicts(d1: dict[K, V], d2: dict[K, V]) -> dict[K, V]:
    """Generic function with multiple type variables."""
    return {**d1, **d2}


@dataclass
class Container(Generic[T]):
    """Generic class."""
    items: list[T]

    def add(self, item: T) -> None:
        self.items.append(item)

    def get_all(self) -> list[T]:
        return list(self.items)


# ---------------------------------------------------------------------------
# Protocol — structural subtyping
# ---------------------------------------------------------------------------

@runtime_checkable
class Drawable(Protocol):
    """Protocol: structural subtyping (duck typing interfaces)."""
    def draw(self) -> str: ...


@runtime_checkable
class Resizable(Protocol):
    def resize(self, factor: float) -> None: ...


class Circle:
    """Implements Drawable protocol without explicit inheritance."""
    def __init__(self, radius: float) -> None:
        self.radius = radius

    def draw(self) -> str:
        return f"Circle(r={self.radius})"


class Square:
    def __init__(self, side: float) -> None:
        self.side = side

    def draw(self) -> str:
        return f"Square(s={self.side})"

    def resize(self, factor: float) -> None:
        self.side *= factor


def draw_something(d: Drawable) -> str:
    """Accepts any object that has a draw() method (structural typing)."""
    return d.draw()


# ---------------------------------------------------------------------------
# TypedDict — typed dictionary for JSON/config
# ---------------------------------------------------------------------------

class UserProfile(TypedDict):
    """TypedDict: dictionary with known string keys and typed values."""
    name: str
    age: int
    email: str
    active: bool


class ApiConfig(TypedDict, total=False):
    """total=False makes all keys optional."""
    base_url: str
    timeout: int
    retries: int


def create_profile(name: str, age: int, email: str) -> UserProfile:
    """TypedDict provides type checking for dict operations."""
    return UserProfile(name=name, age=age, email=email, active=True)


# ---------------------------------------------------------------------------
# Literal — constrained values
# ---------------------------------------------------------------------------

def set_role(user: str, role: Literal["admin", "member", "guest"]) -> dict[str, str]:
    """Literal: restrict to specific values."""
    return {"user": user, "role": role}


def handle_status(status: Literal[200, 404, 500]) -> str:
    """Literal with integer values."""
    match status:
        case 200:
            return "OK"
        case 404:
            return "Not Found"
        case 500:
            return "Internal Server Error"


# ---------------------------------------------------------------------------
# Type narrowing
# ---------------------------------------------------------------------------

def process_value(value: str | int | None) -> str:
    """Type narrowing with isinstance and is None checks."""
    if value is None:
        return "got nothing"
    if isinstance(value, str):
        return f"string: {value.upper()}"  # narrowed to str
    if isinstance(value, int):
        return f"int: {value + 1}"  # narrowed to int
    return "unreachable"


def narrow_with_guard(value: Any) -> int:
    """Type narrowing with guard conditions."""
    if not isinstance(value, int):
        raise TypeError(f"expected int, got {type(value).__name__}")
    return value * 2  # narrowed to int after guard


# ---------------------------------------------------------------------------
# dataclass vs NamedTuple vs dict vs TypedDict
# ---------------------------------------------------------------------------

@dataclass
class UserDataclass:
    """dataclass: mutable, has __init__/__repr__/__eq__."""
    name: str
    age: int


class UserNamedTuple(NamedTuple):
    """NamedTuple: immutable, tuple-like, has field names."""
    name: str
    age: int


def compare_data_containers() -> dict[str, str]:
    """Guide for choosing data container types."""
    return {
        "dataclass": "Mutable data, need methods, Python 3.7+",
        "NamedTuple": "Immutable records, tuple-compatible, lightweight",
        "TypedDict": "JSON/config data, dict-compatible, type-checked keys",
        "dict": "Dynamic keys, no type checking, flexible",
    }


# ---------------------------------------------------------------------------
# Gradual typing strategy
# ---------------------------------------------------------------------------

def typing_strategy() -> dict[str, str]:
    """Gradual typing: adopt incrementally."""
    return {
        "step1": "Annotate public interfaces (function signatures)",
        "step2": "Limit Any to external boundaries",
        "step3": "Add Protocol for interface contracts",
        "step4": "Use TypedDict for structured dicts",
        "step5": "Integrate mypy/pyright in CI",
        "step6": "Progressively reduce Any usage",
        "principle": "Types are for tools and humans, not runtime enforcement",
    }
