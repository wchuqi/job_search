"""Tests for exceptions_io module (06-异常处理和文件IO, 面试05)."""

import json
import csv
import io
import pytest
from pathlib import Path
from py_proj.exceptions_io import (
    AppError,
    DataNotFoundError,
    ValidationError,
    Timer,
    csv_roundtrip,
    json_roundtrip,
    path_demo,
    parse_int_safe,
    precise_catch_demo,
    read_csv,
    read_json,
    safe_divide,
    validate_positive_int,
    validate_string_length,
    validate_user_data,
    process_user,
    write_csv,
    write_json,
)


class TestCustomExceptions:
    def test_app_error(self):
        exc = AppError("test error", code=5)
        assert str(exc) == "test error"
        assert exc.code == 5

    def test_validation_error(self):
        exc = ValidationError("name", "", "required")
        assert exc.field == "name"
        assert exc.value == ""
        assert exc.reason == "required"
        assert exc.code == 2

    def test_data_not_found(self):
        exc = DataNotFoundError("user:123")
        assert exc.key == "user:123"
        assert exc.code == 3

    def test_inheritance(self):
        assert issubclass(ValidationError, AppError)
        assert issubclass(DataNotFoundError, AppError)


class TestTryExceptElseFinally:
    def test_success(self):
        result = safe_divide(10, 3)
        assert result["result"] == pytest.approx(10 / 3)
        assert result["error"] is None
        assert result["cleaned"] is True

    def test_zero_division(self):
        result = safe_divide(10, 0)
        assert result["result"] is None
        assert "division by zero" in result["error"]
        assert result["cleaned"] is True

    def test_type_error(self):
        result = safe_divide(10, "x")
        assert result["result"] is None
        assert "type error" in result["error"]


class TestExceptionChaining:
    def test_parse_int_success(self):
        assert parse_int_safe("42") == 42

    def test_parse_int_failure(self):
        with pytest.raises(ValidationError) as exc_info:
            parse_int_safe("abc")
        assert exc_info.value.field == "number"
        assert exc_info.value.__cause__ is not None
        assert isinstance(exc_info.value.__cause__, ValueError)


class TestPreciseCatch:
    def test_found(self):
        data = {"key": "value"}
        assert precise_catch_demo(data, "key") == "value"

    def test_not_found(self):
        with pytest.raises(DataNotFoundError):
            precise_catch_demo({}, "missing")


class TestContextManager:
    def test_timer(self):
        with Timer() as t:
            sum(range(1000))
        assert t.elapsed > 0


class TestJSON:
    def test_roundtrip(self):
        data = {"name": "test", "values": [1, 2, 3], "nested": {"a": 1}}
        result = json_roundtrip(data)
        assert result == data

    def test_unicode(self):
        data = {"中文": "值"}
        result = json_roundtrip(data)
        assert result == data

    def test_file_roundtrip(self, tmp_path: Path):
        path = tmp_path / "nested" / "data.json"
        data = {"name": "Alice", "age": 30}
        write_json(data, path)
        assert read_json(path) == data


class TestCSV:
    def test_roundtrip(self):
        rows = [{"name": "Alice", "age": "30"}, {"name": "Bob", "age": "25"}]
        result = csv_roundtrip(rows)
        assert len(result) == 2
        assert result[0]["name"] == "Alice"
        assert result[1]["age"] == "25"

    def test_file_roundtrip_and_empty(self, tmp_path: Path):
        path = tmp_path / "nested" / "users.csv"
        rows = [{"name": "Alice", "age": "30"}]
        write_csv(rows, path)
        assert read_csv(path) == rows
        empty_path = tmp_path / "empty.csv"
        write_csv([], empty_path)
        assert read_csv(empty_path) == []


class TestPathDemo:
    def test_path_demo(self):
        result = path_demo("/tmp/demo.txt")
        assert result["name"] == "demo.txt"
        assert result["suffix"] == ".txt"


class TestValidation:
    def test_valid_user(self):
        data = {"name": "Alice", "age": 30, "email": "a@b.com"}
        validate_user_data(data)  # should not raise

    def test_missing_name(self):
        with pytest.raises(ValidationError):
            validate_user_data({"age": 30})

    def test_invalid_age(self):
        with pytest.raises(ValidationError):
            validate_user_data({"name": "Alice", "age": -1})

    def test_invalid_email(self):
        with pytest.raises(ValidationError):
            validate_user_data({"name": "Alice", "email": "no-at-sign"})

    def test_process_user_success(self):
        result = process_user({"name": "Alice", "age": 30})
        assert result["status"] == "ok"

    def test_process_user_failure(self):
        with pytest.raises(AppError):
            process_user({})


class TestValueValidation:
    def test_positive_int_valid(self):
        assert validate_positive_int(42) == 42

    def test_positive_int_negative(self):
        with pytest.raises(ValidationError):
            validate_positive_int(-1)

    def test_positive_int_type(self):
        with pytest.raises(ValidationError):
            validate_positive_int("42")  # type: ignore

    def test_string_length_valid(self):
        assert validate_string_length("hello") == "hello"

    def test_string_length_too_short(self):
        with pytest.raises(ValidationError):
            validate_string_length("", min_len=1)

    def test_string_length_too_long(self):
        with pytest.raises(ValidationError):
            validate_string_length("a" * 101, max_len=100)

    def test_string_length_type(self):
        with pytest.raises(ValidationError):
            validate_string_length(123)  # type: ignore[arg-type]
