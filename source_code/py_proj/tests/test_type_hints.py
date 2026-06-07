"""Tests for type_hints module (18-类型提示泛型和静态分析, 面试06)."""

import pytest
from py_proj.type_hints import (
    Circle,
    Container,
    Square,
    UserDataclass,
    UserNamedTuple,
    add,
    any_demo,
    callable_annotation,
    compare_data_containers,
    create_profile,
    draw_something,
    first,
    handle_status,
    merge_dicts,
    narrow_with_guard,
    object_demo,
    optional_param,
    process_items,
    process_value,
    set_role,
    typing_strategy,
)


class TestBasicAnnotations:
    def test_add(self):
        assert add(2, 3) == 5

    def test_process_items(self):
        result = process_items(["a", "b"], {"x": 10, "y": 20})
        assert result["count"] == 2
        assert result["total"] == 30

    def test_optional_param(self):
        assert optional_param("hello") == "hello"
        assert optional_param("hello", "world") == "world"


class TestCallableAnnotation:
    def test_callable(self):
        result = callable_annotation(len, "hello")
        assert result == 5


class TestAnyVsObject:
    def test_any(self):
        assert any_demo(42) == 42
        assert any_demo("hello") == "hello"
        assert any_demo([1, 2]) == [1, 2]

    def test_object(self):
        assert object_demo(42) == "42"
        assert object_demo("hello") == "hello"


class TestGenerics:
    def test_first(self):
        assert first([1, 2, 3]) == 1
        assert first(["a", "b"]) == "a"

    def test_first_empty(self):
        with pytest.raises(ValueError):
            first([])

    def test_merge_dicts(self):
        result = merge_dicts({"a": 1}, {"b": 2})
        assert result == {"a": 1, "b": 2}

    def test_container(self):
        c = Container[int](items=[1, 2])
        c.add(3)
        assert c.get_all() == [1, 2, 3]


class TestProtocol:
    def test_draw_circle(self):
        c = Circle(5)
        assert "Circle" in draw_something(c)

    def test_draw_square(self):
        s = Square(3)
        assert "Square" in draw_something(s)
        s.resize(2)
        assert "6" in draw_something(s)

    def test_protocol_check(self):
        assert isinstance(Circle(1), Circle)
        c = Circle(1)
        assert hasattr(c, "draw")


class TestTypedDict:
    def test_create_profile(self):
        result = create_profile("Alice", 30, "a@b.com")
        assert result["name"] == "Alice"
        assert result["age"] == 30
        assert result["active"] is True


class TestLiteral:
    def test_set_role(self):
        result = set_role("alice", "admin")
        assert result["role"] == "admin"

    def test_handle_status(self):
        assert handle_status(200) == "OK"
        assert handle_status(404) == "Not Found"
        assert handle_status(500) == "Internal Server Error"


class TestTypeNarrowing:
    def test_none(self):
        assert process_value(None) == "got nothing"

    def test_string(self):
        assert process_value("hello") == "string: HELLO"

    def test_int(self):
        assert process_value(42) == "int: 43"

    def test_unreachable_runtime_fallback(self):
        assert process_value(1.5) == "unreachable"  # type: ignore[arg-type]

    def test_guard(self):
        assert narrow_with_guard(5) == 10

    def test_guard_fails(self):
        with pytest.raises(TypeError):
            narrow_with_guard("not an int")


class TestDataContainers:
    def test_dataclass(self):
        u = UserDataclass("Alice", 30)
        assert u.name == "Alice"

    def test_namedtuple(self):
        u = UserNamedTuple("Bob", 25)
        assert u[0] == "Bob"
        assert u.name == "Bob"

    def test_compare(self):
        result = compare_data_containers()
        assert "dataclass" in result
        assert "TypedDict" in result


class TestTypingStrategy:
    def test_strategy(self):
        result = typing_strategy()
        assert len(result) >= 6
        assert "step1" in result
