"""Module 17: Descriptors, Attribute Lookup and Metaclasses (17-描述符属性查找和元类, 面试06)

Knowledge points covered:
- attribute lookup priority (data descriptors → instance __dict__ → non-data descriptors → class → __getattr__)
- descriptor protocol (__get__, __set__, __delete__, __set_name__)
- data descriptor vs non-data descriptor
- property as built-in data descriptor
- method binding (non-data descriptor, __get__ returns bound method)
- @staticmethod (disables auto-binding)
- @classmethod (binds class as first arg)
- __getattribute__ (called every access) vs __getattr__ (fallback on miss)
- metaclasses (classes that create classes, default is type)
- RegistryMeta.__new__ for auto-registration
- __init_subclass__ as lightweight metaclass alternative
- class creation order
- when to use property vs descriptor vs class decorator vs __init_subclass__ vs metaclass
"""

from __future__ import annotations

from abc import ABC, abstractmethod
from typing import Any


# ---------------------------------------------------------------------------
# Descriptor protocol
# ---------------------------------------------------------------------------

class Validated:
    """Data descriptor: implements __get__ and __set__."""

    def __set_name__(self, owner: type, name: str) -> None:
        self.name = name
        self.storage_name = f"_validated_{name}"

    def __get__(self, obj: Any, objtype: type | None = None) -> Any:
        if obj is None:
            return self
        return getattr(obj, self.storage_name, None)

    def __set__(self, obj: Any, value: Any) -> None:
        if not isinstance(value, (int, float)):
            raise TypeError(f"{self.name} must be a number, got {type(value).__name__}")
        if value < 0:
            raise ValueError(f"{self.name} must be non-negative, got {value}")
        setattr(obj, self.storage_name, value)


class NonDataDescriptor:
    """Non-data descriptor: only implements __get__ (no __set__)."""

    def __get__(self, obj: Any, objtype: type | None = None) -> str:
        if obj is None:
            return "class access"
        return f"instance access for {type(obj).__name__}"


# ---------------------------------------------------------------------------
# Attribute lookup priority
# ---------------------------------------------------------------------------

class DataDescriptorOwner:
    """Demonstrate attribute lookup: data descriptor > instance __dict__ > non-data descriptor."""
    validated_x = Validated()  # data descriptor (has __get__ and __set__)
    non_data = NonDataDescriptor()  # non-data descriptor (only __get__)

    def __init__(self, x: float) -> None:
        self.validated_x = x  # goes through descriptor __set__


def attribute_lookup_demo() -> dict[str, Any]:
    """Show the attribute lookup priority chain."""
    obj = DataDescriptorOwner(42)
    return {
        "data_descriptor": obj.validated_x,      # descriptor __get__
        "non_data_descriptor": obj.non_data,      # descriptor __get__
        "instance_dict": obj.__dict__,            # shows _validated_x
        "mro": [cls.__name__ for cls in type(obj).__mro__],
    }


# ---------------------------------------------------------------------------
# property — built-in data descriptor
# ---------------------------------------------------------------------------

class Temperature:
    """property: built-in data descriptor for computed attributes."""

    def __init__(self, celsius: float) -> None:
        self._celsius = celsius

    @property
    def celsius(self) -> float:
        return self._celsius

    @celsius.setter
    def celsius(self, value: float) -> None:
        if value < -273.15:
            raise ValueError("Temperature below absolute zero")
        self._celsius = value

    @property
    def fahrenheit(self) -> float:
        return self._celsius * 9 / 5 + 32

    @fahrenheit.setter
    def fahrenheit(self, value: float) -> None:
        self.celsius = (value - 32) * 5 / 9


# ---------------------------------------------------------------------------
# __getattribute__ vs __getattr__
# ---------------------------------------------------------------------------

class SmartObject:
    """Demonstrate __getattribute__ (every access) vs __getattr__ (fallback)."""

    def __init__(self, data: dict[str, Any]) -> None:
        self._data = data

    def __getattribute__(self, name: str) -> Any:
        """Called for EVERY attribute access. Be careful with recursion!"""
        # Must use super() or object.__getattribute__ to avoid infinite recursion
        try:
            return super().__getattribute__(name)
        except AttributeError:
            # Fall through to __getattr__ for missing attributes
            raise

    def __getattr__(self, name: str) -> Any:
        """Called ONLY when normal lookup fails (attribute not found)."""
        if name.startswith("dynamic_"):
            key = name[8:]  # remove "dynamic_" prefix
            return self._data.get(key, f"default_{key}")
        raise AttributeError(f"'{type(self).__name__}' has no attribute '{name}'")


# ---------------------------------------------------------------------------
# Method binding
# ---------------------------------------------------------------------------

class MethodDemo:
    """Demonstrate method binding via non-data descriptor."""

    def __init__(self, value: int) -> None:
        self.value = value

    def instance_method(self) -> str:
        """Regular method: receives self (bound to instance)."""
        return f"instance_method: value={self.value}"

    @classmethod
    def class_method(cls) -> str:
        """Class method: receives class (bound to class)."""
        return f"class_method: {cls.__name__}"

    @staticmethod
    def static_method() -> str:
        """Static method: no auto-binding."""
        return "static_method: no binding"


# ---------------------------------------------------------------------------
# Abstract base class polymorphism
# ---------------------------------------------------------------------------

class Shape(ABC):
    """Abstract base class that defines a small drawing contract."""

    @abstractmethod
    def area(self) -> float:
        """Return the shape area."""

    @abstractmethod
    def draw(self) -> str:
        """Return a textual drawing command."""


class Circle(Shape):
    """Concrete implementation of the Shape contract."""

    def __init__(self, radius: float) -> None:
        self.radius = radius

    def area(self) -> float:
        return 3.14159 * self.radius ** 2

    def draw(self) -> str:
        return f"circle radius={self.radius}"


class Square(Shape):
    """Concrete implementation of the Shape contract."""

    def __init__(self, side: float) -> None:
        self.side = side

    def area(self) -> float:
        return self.side ** 2

    def draw(self) -> str:
        return f"square side={self.side}"


def draw_something(shape: Shape) -> str:
    """Use polymorphism: the caller depends on the Shape contract only."""
    return shape.draw()


# ---------------------------------------------------------------------------
# Metaclasses
# ---------------------------------------------------------------------------

class RegistryMeta(type):
    """Metaclass that auto-registers all subclasses."""

    _registry: dict[str, type] = {}

    def __new__(
        mcs,
        name: str,
        bases: tuple[type, ...],
        namespace: dict[str, Any],
        **kwargs: Any,
    ) -> RegistryMeta:
        cls = super().__new__(mcs, name, bases, namespace)
        if bases:  # don't register the base class itself
            RegistryMeta._registry[name] = cls
        return cls

    @classmethod
    def get_registry(mcs) -> dict[str, type]:
        return dict(mcs._registry)


class RegisteredBase(metaclass=RegistryMeta):
    """Base class using RegistryMeta metaclass."""
    pass


class ConcreteA(RegisteredBase):
    pass


class ConcreteB(RegisteredBase):
    pass


# ---------------------------------------------------------------------------
# __init_subclass__ — lightweight metaclass alternative
# ---------------------------------------------------------------------------

class PluginBase:
    """Using __init_subclass__ instead of metaclass for auto-registration."""
    _plugins: dict[str, type] = {}

    def __init_subclass__(cls, plugin_name: str | None = None, **kwargs: Any) -> None:
        super().__init_subclass__(**kwargs)
        name = plugin_name or cls.__name__
        PluginBase._plugins[name] = cls

    @classmethod
    def get_plugins(cls) -> dict[str, type]:
        return dict(cls._plugins)


class JsonPlugin(PluginBase, plugin_name="json"):
    pass


class CsvPlugin(PluginBase, plugin_name="csv"):
    pass


# ---------------------------------------------------------------------------
# Class creation order
# ---------------------------------------------------------------------------

def class_creation_order() -> list[str]:
    """Describe the class creation order in Python."""
    return [
        "1. Parse class body",
        "2. Prepare namespace (metaclass __prepare__ if defined)",
        "3. Execute class body in namespace",
        "4. Call metaclass(name, bases, namespace) → __new__ then __init__",
        "5. __set_name__ called on each descriptor",
        "6. __init_subclass__ called on parent classes",
        "7. Class is bound to its name in the enclosing module",
    ]


# ---------------------------------------------------------------------------
# When to use what
# ---------------------------------------------------------------------------

def selection_guide() -> dict[str, str]:
    """Guide for choosing between property, descriptor, decorator, etc."""
    return {
        "property": "Single class: computed attributes, validation, lazy loading",
        "descriptor": "Multiple classes: reusable attribute logic (validation, type checking)",
        "class_decorator": "Simple class modification after creation (add methods, register)",
        "__init_subclass__": "Lightweight: auto-register subclasses, validate structure",
        "metaclass": "Heavy: control class creation itself, modify namespace, enforce contracts",
    }
