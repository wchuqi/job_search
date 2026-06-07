"""Tests for basics module (01-语法基础和运行模型, 面试01-基础语法和类型)."""

import pytest
from py_proj.basics import (
    arithmetic_ops,
    bitwise_ops,
    comparison_ops,
    demonstrate_binding,
    demonstrate_is_vs_eq,
    demonstrate_truthiness,
    expression_vs_statement,
    get_python_version_info,
    identity_with_none,
    is_truthy,
    logical_ops,
    membership_ops,
    python3_differences_demo,
    walrus_operator_demo,
)


class TestBinding:
    def test_shared_binding(self):
        a, b = demonstrate_binding()
        assert a is b
        assert a == [1, 2, 3, 4]

    def test_names_are_labels(self):
        x = [1, 2]
        y = x
        y.append(3)
        assert x == [1, 2, 3]


class TestIsVsEq:
    def test_value_equality(self):
        result = demonstrate_is_vs_eq()
        assert result["a == b"] is True

    def test_identity_different_objects(self):
        result = demonstrate_is_vs_eq()
        assert result["a is b"] is False

    def test_identity_same_object(self):
        result = demonstrate_is_vs_eq()
        assert result["a is c"] is True

    def test_none_identity(self):
        assert identity_with_none(None) is True
        assert identity_with_none(0) is False
        assert identity_with_none("") is False


class TestArithmeticOps:
    def test_basic_ops(self):
        result = arithmetic_ops(10, 3)
        assert result["add"] == 13
        assert result["sub"] == 7
        assert result["mul"] == 30
        assert result["truediv"] == pytest.approx(10 / 3)
        assert result["floordiv"] == 3
        assert result["mod"] == 1
        assert result["pow"] == 1000

    def test_true_division(self):
        assert arithmetic_ops(7, 2)["truediv"] == 3.5
        assert arithmetic_ops(7, 2)["floordiv"] == 3


class TestComparisonOps:
    def test_all_comparisons(self):
        result = comparison_ops(3, 5)
        assert result["eq"] is False
        assert result["ne"] is True
        assert result["lt"] is True
        assert result["le"] is True
        assert result["gt"] is False
        assert result["ge"] is False

    def test_chaining(self):
        result = comparison_ops(3, 5)
        assert result["chain"] is True  # 1 < 3 < 5 < 10


class TestLogicalOps:
    def test_and(self):
        assert logical_ops(True, True)["and"] is True
        assert logical_ops(True, False)["and"] is False

    def test_or(self):
        assert logical_ops(False, True)["or"] is True
        assert logical_ops(False, False)["or"] is False

    def test_not(self):
        assert logical_ops(True, False)["not"] is False
        assert logical_ops(False, True)["not"] is True


class TestMembershipOps:
    def test_in_list(self):
        assert membership_ops(3, [1, 2, 3]) is True
        assert membership_ops(4, [1, 2, 3]) is False

    def test_in_set(self):
        assert membership_ops(3, {1, 2, 3}) is True
        assert membership_ops(4, {1, 2, 3}) is False


class TestBitwiseOps:
    def test_bitwise(self):
        result = bitwise_ops(0b1010, 0b1100)
        assert result["and"] == 0b1000
        assert result["or"] == 0b1110
        assert result["xor"] == 0b0110
        assert result["lshift"] == 0b10100
        assert result["rshift"] == 0b0101


class TestTruthiness:
    def test_falsy_values(self):
        for val in [False, None, 0, 0.0, "", [], {}, set()]:
            assert is_truthy(val) is False, f"{val!r} should be falsy"

    def test_truthy_values(self):
        for val in [True, 1, -1, 1.5, "hello", [1], {"a": 1}, {1}]:
            assert is_truthy(val) is True, f"{val!r} should be truthy"

    def test_demonstrate_truthiness(self):
        results = demonstrate_truthiness()
        assert len(results) > 0
        assert all(isinstance(v, bool) for _, v in results)


class TestWalrusOperator:
    def test_walrus(self):
        result = walrus_operator_demo([1, 2, 3, 4, 5])
        assert result == [16, 25]  # 4²=16, 5²=25


class TestPython3Differences:
    def test_has_all_differences(self):
        diffs = python3_differences_demo()
        assert "strings" in diffs
        assert "print" in diffs
        assert "division" in diffs
        assert "iterables" in diffs
        assert "input" in diffs


class TestVersionInfo:
    def test_version_info(self):
        info = get_python_version_info()
        assert "3" in info["major"]
        assert "cpython" in info["implementation"].lower() or info["implementation"] == "cpython"


class TestExpressionsVsStatements:
    def test_walrus_in_expression(self):
        result = expression_vs_statement()
        assert "expression" in result
        assert "statement" in result
