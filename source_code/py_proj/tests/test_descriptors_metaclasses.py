"""Tests for descriptors_metaclasses module (17-描述符属性查找和元类, 面试06)."""

import pytest
from py_proj.descriptors_metaclasses import (
    Circle,
    ConcreteA,
    ConcreteB,
    DataDescriptorOwner,
    MethodDemo,
    PluginBase,
    JsonPlugin,
    CsvPlugin,
    RegistryMeta,
    RegisteredBase,
    SmartObject,
    Square,
    Temperature,
    attribute_lookup_demo,
    class_creation_order,
    draw_something,
    selection_guide,
)


class TestDescriptors:
    def test_data_descriptor(self):
        obj = DataDescriptorOwner(42)
        assert obj.validated_x == 42

    def test_data_descriptor_validation(self):
        obj = DataDescriptorOwner(42)
        with pytest.raises(ValueError):
            obj.validated_x = -1
        with pytest.raises(TypeError):
            obj.validated_x = "not a number"

    def test_non_data_descriptor(self):
        obj = DataDescriptorOwner(42)
        assert "instance" in obj.non_data

    def test_descriptor_class_access(self):
        assert DataDescriptorOwner.validated_x is not None
        assert DataDescriptorOwner.non_data == "class access"

    def test_attribute_lookup(self):
        result = attribute_lookup_demo()
        assert result["data_descriptor"] == 42
        assert "instance" in result["non_data_descriptor"]


class TestProperty:
    def test_celsius(self):
        t = Temperature(100)
        assert t.celsius == 100

    def test_fahrenheit(self):
        t = Temperature(100)
        assert t.fahrenheit == pytest.approx(212.0)

    def test_set_fahrenheit(self):
        t = Temperature(0)
        t.fahrenheit = 212
        assert t.celsius == pytest.approx(100.0)

    def test_validation(self):
        t = Temperature(0)
        with pytest.raises(ValueError):
            t.celsius = -300


class TestGetattributeVsGetattr:
    def test_normal_access(self):
        obj = SmartObject({"x": 42})
        assert obj._data == {"x": 42}

    def test_dynamic_attribute(self):
        obj = SmartObject({"x": 42})
        assert obj.dynamic_x == 42

    def test_missing_dynamic(self):
        obj = SmartObject({})
        assert obj.dynamic_y == "default_y"

    def test_missing_raises(self):
        obj = SmartObject({})
        with pytest.raises(AttributeError):
            obj.nonexistent


class TestMethodBinding:
    def test_instance_method(self):
        obj = MethodDemo(42)
        assert "42" in obj.instance_method()

    def test_class_method(self):
        assert "MethodDemo" in MethodDemo.class_method()

    def test_static_method(self):
        assert "no binding" in MethodDemo.static_method()

    def test_unbound(self):
        obj = MethodDemo(42)
        # Method is a descriptor — access through class returns function
        assert callable(MethodDemo.instance_method)


class TestAbstractPolymorphism:
    def test_circle_square_area_and_draw(self):
        circle = Circle(2)
        square = Square(3)
        assert circle.area() == pytest.approx(12.56636)
        assert square.area() == 9
        assert draw_something(circle) == "circle radius=2"
        assert draw_something(square) == "square side=3"


class TestMetaclass:
    def test_registry(self):
        registry = RegistryMeta.get_registry()
        assert "ConcreteA" in registry
        assert "ConcreteB" in registry
        assert registry["ConcreteA"] is ConcreteA

    def test_inheritance(self):
        assert issubclass(ConcreteA, RegisteredBase)  # type: ignore


class TestInitSubclass:
    def test_plugin_registration(self):
        plugins = PluginBase.get_plugins()
        assert "json" in plugins
        assert "csv" in plugins
        assert plugins["json"] is JsonPlugin
        assert plugins["csv"] is CsvPlugin


class TestClassCreationOrder:
    def test_order(self):
        steps = class_creation_order()
        assert len(steps) == 7
        assert "Parse" in steps[0]
        assert "metaclass" in steps[3]


class TestSelectionGuide:
    def test_guide(self):
        result = selection_guide()
        assert "property" in result
        assert "descriptor" in result
        assert "metaclass" in result
