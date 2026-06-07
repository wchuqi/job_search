"""Tests for testing_logging module (11-测试调试日志和质量工具, 面试05)."""

import logging
import pytest
from unittest.mock import patch, MagicMock
from py_proj.testing_logging import (
    add,
    create_logger,
    debuggable_function,
    divide,
    fetch_data,
    log_with_context,
    log_levels_demo,
    mock_demo,
    process_api_response,
    quality_tools_guide,
    validate_age,
)


class TestLogging:
    def test_create_logger(self):
        logger = create_logger("test_logger")
        assert logger.name == "test_logger"
        assert logger.level == logging.INFO

    def test_log_levels(self):
        result = log_levels_demo()
        assert logging.DEBUG in result
        assert logging.CRITICAL in result

    def test_logger_reuse(self):
        logger1 = create_logger("reuse_test")
        logger2 = create_logger("reuse_test")
        assert logger1 is logger2  # same logger returned

    def test_log_with_context(self, caplog):
        logger = create_logger("context_logger")
        with caplog.at_level(logging.INFO, logger="context_logger"):
            log_with_context(logger, "user.created", user_id=42)
        assert "user.created" in caplog.text
        assert "42" in caplog.text


class TestMocking:
    def test_fetch_data_success(self, monkeypatch):
        class Response:
            status = 200

            def __enter__(self):
                return self

            def __exit__(self, exc_type, exc, tb):
                return False

            def read(self):
                return b"ok"

        monkeypatch.setattr("urllib.request.urlopen", lambda url, timeout: Response())
        assert fetch_data("http://example.com") == {"status": 200, "data": "ok"}

    def test_fetch_data_error(self, monkeypatch):
        def fail(url, timeout):
            raise OSError("network down")

        monkeypatch.setattr("urllib.request.urlopen", fail)
        result = fetch_data("http://example.com")
        assert result["status"] == 0
        assert "network down" in result["error"]

    def test_mock_fetch(self):
        with patch("py_proj.testing_logging.fetch_data") as mock:
            mock.return_value = {"status": 200, "data": "ok"}
            result = process_api_response("http://example.com")
            assert result == "Success: status=200"
            mock.assert_called_once_with("http://example.com")

    def test_mock_error(self):
        with patch("py_proj.testing_logging.fetch_data") as mock:
            mock.return_value = {"status": 0, "error": "timeout"}
            result = process_api_response("http://example.com")
            assert "Error" in result

    def test_mock_demo(self):
        assert mock_demo()["mocked_result"] == {"status": 200, "data": "mocked"}


class TestAdd:
    def test_add_positive(self):
        assert add(2, 3) == 5

    def test_add_negative(self):
        assert add(-1, -2) == -3

    def test_add_zero(self):
        assert add(0, 0) == 0


class TestDivide:
    def test_divide(self):
        assert divide(10, 2) == 5.0

    def test_divide_zero(self):
        with pytest.raises(ZeroDivisionError):
            divide(10, 0)

    def test_divide_float(self):
        assert divide(7, 2) == pytest.approx(3.5)


class TestValidateAge:
    def test_valid(self):
        assert validate_age(25) is True
        assert validate_age(0) is True
        assert validate_age(150) is True

    def test_negative(self):
        with pytest.raises(ValueError):
            validate_age(-1)

    def test_too_high(self):
        with pytest.raises(ValueError):
            validate_age(151)

    def test_type_error(self):
        with pytest.raises(TypeError):
            validate_age("25")  # type: ignore


class TestQualityTools:
    def test_guide(self):
        result = quality_tools_guide()
        assert "formatter" in result
        assert "type_checker" in result
        assert "test_runner" in result


class TestDebuggable:
    def test_debuggable(self):
        result = debuggable_function([1, 2, 3, 4, 5])
        expected = sum(item * (i + 1) for i, item in enumerate([1, 2, 3, 4, 5]))
        assert result == expected
