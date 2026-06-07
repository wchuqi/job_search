"""Tests for oop module (04-面向对象和数据模型, 面试03)."""

import pytest
from py_proj.oop import (
    Animal,
    BadConfig,
    Car,
    Config,
    CountDown,
    Date,
    Dog,
    Engine,
    FrozenPoint,
    GoodConfig,
    IterableRange,
    ManagedResource,
    Point,
    Vector,
    attribute_lookup_demo,
    mro_demo,
)


class TestAnimal:
    def test_speak(self):
        a = Animal("Cat", "Meow")
        assert a.speak() == "Cat says Meow"

    def test_str(self):
        a = Animal("Dog", "Woof")
        assert str(a) == "Animal(Dog)"

    def test_repr(self):
        a = Animal("Dog", "Woof")
        assert "Dog" in repr(a)
        assert "Woof" in repr(a)

    def test_equality(self):
        a1 = Animal("Cat", "Meow")
        a2 = Animal("Cat", "Meow")
        a3 = Animal("Dog", "Woof")
        assert a1 == a2
        assert a1 != a3
        assert Animal.__eq__(a1, object()) is NotImplemented

    def test_hash(self):
        a1 = Animal("Cat", "Meow")
        a2 = Animal("Cat", "Meow")
        assert hash(a1) == hash(a2)

    def test_class_attribute(self):
        initial = Animal.species_count
        a = Animal("X", "Y")
        assert Animal.species_count == initial + 1


class TestDate:
    def test_from_string(self):
        d = Date.from_string("2025-01-15")
        assert d.year == 2025
        assert d.month == 1
        assert d.day == 15

    def test_is_valid(self):
        assert Date.is_valid(2025, 1, 15) is True
        assert Date.is_valid(2025, 13, 1) is False
        assert Date.is_valid(2025, 1, 32) is False
        assert Date.is_valid(0, 1, 1) is False

    def test_repr(self):
        assert repr(Date(2025, 1, 15)) == "Date(2025, 1, 15)"


class TestAttributeLookup:
    def test_lookup_order(self):
        result = attribute_lookup_demo()
        assert result["z"] == "child_z"
        assert result["y"] == "middle_y"
        assert result["x"] == 10
        assert "Base" in result["mro"]
        assert "Child" in result["mro"]


class TestMRO:
    def test_mro(self):
        result = mro_demo()
        assert result["d.who()"] == "B"
        assert result["D.__mro__"][0] == "D"
        assert result["D.__mro__"][-1] == "object"

    def test_base_methods_directly(self):
        assert mro_demo()["d.who()"] == "B"
        from py_proj.oop import A, C

        assert A().who() == "A"
        assert C().who() == "C"


class TestComposition:
    def test_car_has_engine(self):
        engine = Engine(200)
        car = Car("Toyota", engine)
        assert "Toyota" in car.start()
        assert "200HP" in car.start()


class TestInheritance:
    def test_dog_is_animal(self):
        dog = Dog("Rex")
        assert dog.speak() == "Rex says Woof"
        assert dog.fetch("ball") == "Rex fetches ball"
        assert isinstance(dog, Animal)


class TestVector:
    def test_add(self):
        v = Vector(1, 2) + Vector(3, 4)
        assert v.x == 4
        assert v.y == 6

    def test_sub(self):
        v = Vector(5, 7) - Vector(2, 3)
        assert v.x == 3
        assert v.y == 4

    def test_mul(self):
        v = Vector(2, 3) * 2
        assert v.x == 4
        assert v.y == 6

    def test_rmul(self):
        v = 3 * Vector(2, 3)
        assert v.x == 6
        assert v.y == 9

    def test_abs(self):
        v = Vector(3, 4)
        assert abs(v) == pytest.approx(5.0)

    def test_bool(self):
        assert bool(Vector(1, 0)) is True
        assert bool(Vector(0, 0)) is False

    def test_len(self):
        assert len(Vector(1, 2)) == 2

    def test_equality(self):
        assert Vector(1, 2) == Vector(1, 2)
        assert Vector(1, 2) != Vector(3, 4)

    def test_repr(self):
        assert repr(Vector(1, 2)) == "Vector(1, 2)"

    def test_str_hash_and_notimplemented(self):
        v = Vector(1, 2)
        assert str(v) == "(1, 2)"
        assert hash(v) == hash(Vector(1, 2))
        assert Vector.__eq__(v, object()) is NotImplemented


class TestIterableRange:
    def test_iteration(self):
        r = IterableRange(0, 5)
        assert list(r) == [0, 1, 2, 3, 4]

    def test_len(self):
        assert len(IterableRange(3, 7)) == 4

    def test_repr(self):
        assert repr(IterableRange(1, 3)) == "IterableRange(1, 3)"

    def test_multiple_iterations(self):
        r = IterableRange(0, 3)
        assert list(r) == [0, 1, 2]
        assert list(r) == [0, 1, 2]  # iterable creates new iterator each time


class TestCountDown:
    def test_iterator_consumes_state(self):
        counter = CountDown(3)
        assert iter(counter) is counter
        assert list(counter) == [3, 2, 1]
        assert list(counter) == []


class TestManagedResource:
    def test_context_manager(self):
        r = ManagedResource("test")
        assert r.is_open is False
        with r:
            assert r.is_open is True
        assert r.is_open is False

    def test_as_variable(self):
        with ManagedResource("db") as r:
            assert r.name == "db"
            assert r.is_open is True

    def test_repr(self):
        assert repr(ManagedResource("db")) == "ManagedResource('db', open=False)"


class TestDataclass:
    def test_point(self):
        p = Point(3.0, 4.0)
        assert p.x == 3.0
        assert p.distance(Point(0, 0)) == pytest.approx(5.0)

    def test_frozen(self):
        fp = FrozenPoint(1.0, 2.0)
        with pytest.raises(AttributeError):
            fp.x = 3.0  # type: ignore
        assert hash(fp) is not None

    def test_config_default_factory(self):
        c1 = Config(name="a")
        c2 = Config(name="b")
        c1.add_tag("x")
        assert c1.tags == ["x"]
        assert c2.tags == []  # independent list

    def test_config_repr(self):
        c = Config(name="test", tags=["a"])
        assert "test" in repr(c)


class TestMutableClassAttribute:
    def test_bad_config_shared(self):
        a = BadConfig()
        b = BadConfig()
        a.add("x")
        # Mutable class attribute is shared
        assert "x" in b.items

    def test_good_config_independent(self):
        a = GoodConfig()
        b = GoodConfig()
        a.add("x")
        assert "x" not in b.items
