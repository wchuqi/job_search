"""Tests for control_flow module (03-控制流函数和作用域, 面试02)."""

import pytest
from py_proj.control_flow import (
    args_kwargs_demo,
    break_continue_demo,
    classify_number,
    closure_in_loop_trap,
    for_loop_demo,
    forward_args,
    generic_sum,
    keyword_args,
    make_adder,
    make_counter,
    match_demo,
    modify_mutable,
    mixed_args,
    mutable_default_demonstration,
    mutable_default_trap,
    nonlocal_keyword_demo,
    positional_args,
    rebind_local,
    return_none_by_default,
    return_single,
    return_tuple,
    typed_function,
    while_demo,
)


class TestControlFlow:
    def test_classify_positive(self):
        assert classify_number(5) == "positive"

    def test_classify_negative(self):
        assert classify_number(-3) == "negative"

    def test_classify_zero(self):
        assert classify_number(0) == "zero"

    def test_for_loop(self):
        result = for_loop_demo([1, 2, 3, 4])
        assert result["total"] == 10
        assert len(result["indexed"]) == 4
        assert result["zipped"]["a"] == 1

    def test_while(self):
        result = while_demo(5)
        assert result == [0, 1, 2, 3, 4]

    def test_break_continue(self):
        result = break_continue_demo([1, 2, 3, 4, 5])
        assert 2 in result["evens"]
        assert result["before_three"] == [2]


class TestMatch:
    def test_positive_int(self):
        assert "positive" in match_demo(42)

    def test_negative_int(self):
        assert "negative" in match_demo(-1)

    def test_zero(self):
        assert match_demo(0) == "zero"

    def test_string(self):
        assert "hello" in match_demo("hello")

    def test_empty_list(self):
        assert match_demo([]) == "empty list"

    def test_single_list(self):
        assert "42" in match_demo([42])

    def test_two_list(self):
        assert "1" in match_demo([1, 2])

    def test_long_list(self):
        assert "rest has 2" in match_demo([1, 2, 3])

    def test_dict_pattern(self):
        result = match_demo({"name": "Alice", "age": 30})
        assert "Alice" in result
        assert "30" in result

    def test_other(self):
        assert "other" in match_demo(3.14)


class TestFunctionParameters:
    def test_positional(self):
        assert positional_args(1, 2, 3) == 6

    def test_keyword(self):
        assert keyword_args(a=1, b=2, c=3) == 6
        assert keyword_args() == 60

    def test_mixed_args(self):
        result = mixed_args(1, 2, 3, 4, x=5, y=6)
        assert result["a"] == 1
        assert result["b"] == 2
        assert result["args"] == (3, 4)
        assert result["kwargs"] == {"x": 5, "y": 6}

    def test_args_kwargs(self):
        result = args_kwargs_demo(1, 2, 3, x=4, y=5)
        assert result["args"] == (1, 2, 3)
        assert result["kwargs"] == {"x": 4, "y": 5}

    def test_forward_args(self):
        result = forward_args(1, 2, key="value")
        assert result["args"] == (1, 2)
        assert result["kwargs"] == {"key": "value"}


class TestReturnValues:
    def test_single_return(self):
        assert return_single(5) == 10

    def test_tuple_return(self):
        assert return_tuple(1, 2) == (2, 1)

    def test_none_return(self):
        assert return_none_by_default(-1) is None
        assert return_none_by_default(5) == 5


class TestScope:
    def test_legb_and_global_keyword(self):
        from py_proj import control_flow

        legb = control_flow.legb_demo()
        assert "L:local" in legb["result"]
        old = control_flow.global_keyword_demo()
        assert old in {"global", "modified_global"}

    def test_nonlocal(self):
        count, _ = nonlocal_keyword_demo()
        assert count == 2

    def test_make_counter(self):
        inc, get = make_counter(10)
        assert get() == 10
        assert inc() == 11
        assert inc() == 12
        assert get() == 12

    def test_make_adder(self):
        add5 = make_adder(5)
        assert add5(3) == 8
        assert add5(10) == 15


class TestMutableDefault:
    def test_none_sentinel(self):
        r1 = mutable_default_trap()
        r2 = mutable_default_trap()
        assert r1 is not r2
        assert r1 == [1]
        assert r2 == [1]

    def test_shared_default(self):
        result = mutable_default_demonstration()
        # The bad pattern shares the default list
        assert result["bad_r1"] == [1, 2]
        assert result["bad_r2"] == [1, 2]
        assert result["bad_r1"] is result["bad_r2"]


class TestClosuresInLoop:
    def test_trap_and_fix(self):
        bad, good = closure_in_loop_trap()
        assert bad == [2, 2, 2]  # trap: all see final i
        assert good == [0, 1, 2]  # fix: captured by default arg


class TestFunctionArgPassing:
    def test_modify_mutable(self):
        lst = [1, 2]
        result = modify_mutable(lst)
        assert result == [1, 2, 999]
        assert lst == [1, 2, 999]  # modification visible to caller

    def test_rebind_local(self):
        lst = [1, 2]
        result = rebind_local(lst)
        assert result == [999, 999]
        assert lst == [1, 2]  # rebinding NOT visible to caller


class TestTypeHints:
    def test_typed_function(self):
        result = typed_function("Alice", [90, 80, 70])
        assert result["name"] == "Alice"
        assert result["average"] == 80.0
        assert result["active"] is True

    def test_generic_sum(self):
        assert generic_sum([1, 2, 3]) == 6
        assert generic_sum([1.5, 2.5]) == 4.0
