"""Module 04: OOP and Data Model (04-面向对象和数据模型, 面试03)

Knowledge points covered:
- class and instance
- instance methods (self)
- class methods (@classmethod, alternative constructors)
- static methods (@staticmethod)
- attribute lookup order (instance, class, MRO parents)
- dataclass (frozen=True, field(default_factory=...))
- special/dunder methods (__str__, __repr__, __len__, __iter__, __enter__, __exit__)
- Python data model (syntax → special method calls)
- inheritance vs composition
- MRO basics
- class attribute vs instance attribute
- mutable class attribute shared state pitfall
"""

from __future__ import annotations

import dataclasses
from dataclasses import dataclass, field
from typing import Any, Iterator


# ---------------------------------------------------------------------------
# Basic class and instance
# ---------------------------------------------------------------------------

class Animal:
    """Base class demonstrating class/instance basics."""

    species_count: int = 0  # class attribute — shared by all instances

    def __init__(self, name: str, sound: str) -> None:
        self.name = name      # instance attribute — per-object
        self.sound = sound
        Animal.species_count += 1

    def speak(self) -> str:
        """Instance method — receives self as first argument."""
        return f"{self.name} says {self.sound}"

    def __str__(self) -> str:
        """User-facing string representation."""
        return f"Animal({self.name})"

    def __repr__(self) -> str:
        """Developer/debug representation — should be unambiguous."""
        return f"Animal(name={self.name!r}, sound={self.sound!r})"

    def __eq__(self, other: object) -> bool:
        """Value equality."""
        if not isinstance(other, Animal):
            return NotImplemented
        return self.name == other.name and self.sound == other.sound

    def __hash__(self) -> int:
        """Must define __hash__ if __eq__ is defined and object should be hashable."""
        return hash((self.name, self.sound))


# ---------------------------------------------------------------------------
# classmethod and staticmethod
# ---------------------------------------------------------------------------

class Date:
    """Demonstrate @classmethod as alternative constructor and @staticmethod."""

    def __init__(self, year: int, month: int, day: int) -> None:
        self.year = year
        self.month = month
        self.day = day

    @classmethod
    def from_string(cls, date_str: str) -> Date:
        """Alternative constructor: parse from string."""
        parts = date_str.split("-")
        return cls(int(parts[0]), int(parts[1]), int(parts[2]))

    @staticmethod
    def is_valid(year: int, month: int, day: int) -> bool:
        """Static method: utility that doesn't need instance or class."""
        return 1 <= month <= 12 and 1 <= day <= 31 and year > 0

    def __repr__(self) -> str:
        return f"Date({self.year}, {self.month}, {self.day})"


# ---------------------------------------------------------------------------
# Attribute lookup order
# ---------------------------------------------------------------------------

class Base:
    """Demonstrate attribute lookup: instance → class → MRO parents."""
    x = 10
    y = "base_y"


class Middle(Base):
    y = "middle_y"
    z = "middle_z"


class Child(Middle):
    z = "child_z"


def attribute_lookup_demo() -> dict[str, Any]:
    """Show attribute lookup priority."""
    obj = Child()
    obj.instance_attr = "from_instance"
    return {
        "instance_attr": obj.instance_attr,  # instance __dict__
        "z": obj.z,                          # Child class
        "y": obj.y,                          # Middle (via MRO)
        "x": obj.x,                          # Base (via MRO)
        "mro": [cls.__name__ for cls in Child.__mro__],
    }


# ---------------------------------------------------------------------------
# MRO — Method Resolution Order (C3 linearization)
# ---------------------------------------------------------------------------

class A:
    def who(self) -> str:
        return "A"


class B(A):
    def who(self) -> str:
        return "B"


class C(A):
    def who(self) -> str:
        return "C"


class D(B, C):
    pass


def mro_demo() -> dict[str, Any]:
    """Demonstrate MRO (C3 linearization)."""
    d = D()
    return {
        "d.who()": d.who(),  # "B" — follows MRO
        "D.__mro__": [cls.__name__ for cls in D.__mro__],  # D, B, C, A, object
    }


# ---------------------------------------------------------------------------
# Inheritance vs Composition
# ---------------------------------------------------------------------------

class Engine:
    """Component for composition example."""

    def __init__(self, horsepower: int) -> None:
        self.horsepower = horsepower

    def start(self) -> str:
        return f"Engine({self.horsepower}HP) started"


class Car:
    """Composition: Car HAS-A Engine (not IS-A Engine)."""

    def __init__(self, make: str, engine: Engine) -> None:
        self.make = make
        self.engine = engine  # composition: has-a relationship

    def start(self) -> str:
        return f"{self.make}: {self.engine.start()}"


# Inheritance: Dog IS-A Animal
class Dog(Animal):
    def __init__(self, name: str) -> None:
        super().__init__(name, "Woof")

    def fetch(self, item: str) -> str:
        return f"{self.name} fetches {item}"


# ---------------------------------------------------------------------------
# Special / Dunder methods
# ---------------------------------------------------------------------------

class Vector:
    """Demonstrate special methods for custom data model."""

    def __init__(self, x: float, y: float) -> None:
        self.x = x
        self.y = y

    def __repr__(self) -> str:
        return f"Vector({self.x}, {self.y})"

    def __str__(self) -> str:
        return f"({self.x}, {self.y})"

    def __add__(self, other: Vector) -> Vector:
        return Vector(self.x + other.x, self.y + other.y)

    def __sub__(self, other: Vector) -> Vector:
        return Vector(self.x - other.x, self.y - other.y)

    def __mul__(self, scalar: float) -> Vector:
        return Vector(self.x * scalar, self.y * scalar)

    def __rmul__(self, scalar: float) -> Vector:
        return self.__mul__(scalar)

    def __eq__(self, other: object) -> bool:
        if not isinstance(other, Vector):
            return NotImplemented
        return self.x == other.x and self.y == other.y

    def __hash__(self) -> int:
        return hash((self.x, self.y))

    def __abs__(self) -> float:
        return (self.x ** 2 + self.y ** 2) ** 0.5

    def __bool__(self) -> bool:
        return self.x != 0 or self.y != 0

    def __len__(self) -> int:
        """len() calls this method."""
        return 2


class IterableRange:
    """Demonstrate __iter__ protocol."""

    def __init__(self, start: int, end: int) -> None:
        self.start = start
        self.end = end

    def __iter__(self) -> Iterator[int]:
        current = self.start
        while current < self.end:
            yield current
            current += 1

    def __len__(self) -> int:
        return max(0, self.end - self.start)

    def __repr__(self) -> str:
        return f"IterableRange({self.start}, {self.end})"


class CountDown:
    """Iterator object: __iter__ returns self and __next__ advances state."""

    def __init__(self, start: int) -> None:
        self.current = start

    def __iter__(self) -> CountDown:
        return self

    def __next__(self) -> int:
        if self.current <= 0:
            raise StopIteration
        value = self.current
        self.current -= 1
        return value


class ManagedResource:
    """Demonstrate context manager protocol (__enter__/__exit__)."""

    def __init__(self, name: str) -> None:
        self.name = name
        self.is_open = False

    def __enter__(self) -> ManagedResource:
        self.is_open = True
        return self

    def __exit__(self, exc_type: type | None, exc_val: BaseException | None,
                 exc_tb: Any) -> bool:
        self.is_open = False
        return False  # don't suppress exceptions

    def __repr__(self) -> str:
        return f"ManagedResource({self.name!r}, open={self.is_open})"


# ---------------------------------------------------------------------------
# dataclass
# ---------------------------------------------------------------------------

@dataclass
class Point:
    """Basic dataclass — auto-generates __init__, __repr__, __eq__."""
    x: float
    y: float

    def distance(self, other: Point) -> float:
        return ((self.x - other.x) ** 2 + (self.y - other.y) ** 2) ** 0.5


@dataclass(frozen=True)
class FrozenPoint:
    """frozen=True makes the dataclass immutable and hashable."""
    x: float
    y: float


@dataclass
class Config:
    """Demonstrate field(default_factory=...) for mutable defaults."""
    name: str
    tags: list[str] = field(default_factory=list)
    options: dict[str, Any] = field(default_factory=dict)

    def add_tag(self, tag: str) -> None:
        self.tags.append(tag)


# ---------------------------------------------------------------------------
# Mutable class attribute pitfall
# ---------------------------------------------------------------------------

class BadConfig:
    """WRONG: mutable class attribute shared by all instances."""
    items: list[str] = []  # shared by ALL instances!

    def add(self, item: str) -> None:
        self.items.append(item)


class GoodConfig:
    """CORRECT: initialize mutable attributes in __init__."""

    def __init__(self) -> None:
        self.items: list[str] = []  # per-instance

    def add(self, item: str) -> None:
        self.items.append(item)
