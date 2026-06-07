"""Module 11: Testing, Debugging, Logging and Quality Tools (11-测试调试日志和质量工具, 面试05)

Knowledge points covered:
- unit testing (test individual functions/classes)
- integration testing (multi-module collaboration)
- breakpoint() and pdb debugging
- logging module (levels, formatters, handlers, context)
- code formatting tools (Black, Ruff format)
- static analysis (Ruff, flake8)
- type checking (mypy, pyright)
- test-driven behavior verification
- log field design
- mocking external dependencies
"""

from __future__ import annotations

import io
import logging
import unittest.mock
from typing import Any


# ---------------------------------------------------------------------------
# Logging
# ---------------------------------------------------------------------------

def create_logger(
    name: str,
    level: int = logging.INFO,
    format_str: str | None = None,
) -> logging.Logger:
    """Create a configured logger with handler and formatter."""
    logger = logging.getLogger(name)
    if not logger.handlers:
        handler = logging.StreamHandler()
        if format_str is None:
            format_str = "%(asctime)s %(name)s %(levelname)s %(message)s"
        handler.setFormatter(logging.Formatter(format_str))
        logger.addHandler(handler)
    logger.setLevel(level)
    return logger


def log_with_context(logger: logging.Logger, event: str, **context: Any) -> None:
    """Log with structured context fields."""
    extra = {k: v for k, v in context.items()}
    logger.info("%s %s", event, extra)


def log_levels_demo() -> dict[int, str]:
    """Demonstrate logging levels and when to use them."""
    return {
        logging.DEBUG: "Detailed diagnostic information",
        logging.INFO: "Confirmation that things are working",
        logging.WARNING: "Something unexpected happened",
        logging.ERROR: "Something failed",
        logging.CRITICAL: "The program itself may be unable to continue",
    }


# ---------------------------------------------------------------------------
# Mocking
# ---------------------------------------------------------------------------

def fetch_data(url: str) -> dict[str, Any]:
    """Simulate external API call (to be mocked in tests)."""
    import urllib.request
    try:
        with urllib.request.urlopen(url, timeout=5) as response:
            return {"status": response.status, "data": response.read().decode()}
    except Exception as exc:
        return {"status": 0, "error": str(exc)}


def process_api_response(url: str) -> str:
    """Higher-level function that depends on fetch_data."""
    result = fetch_data(url)
    if result.get("error"):
        return f"Error: {result['error']}"
    return f"Success: status={result['status']}"


def mock_demo() -> dict[str, Any]:
    """Demonstrate unittest.mock for testing external dependencies."""
    with unittest.mock.patch("py_proj.testing_logging.fetch_data") as mock_fetch:
        mock_fetch.return_value = {"status": 200, "data": "mocked"}
        result = fetch_data("http://example.com")
    return {"mocked_result": result}


# ---------------------------------------------------------------------------
# Test patterns
# ---------------------------------------------------------------------------

def add(a: int, b: int) -> int:
    """Simple function for testing demonstrations."""
    return a + b


def divide(a: float, b: float) -> float:
    """Function that can raise exceptions — good for testing error cases."""
    if b == 0:
        raise ZeroDivisionError("division by zero")
    return a / b


def validate_age(age: int) -> bool:
    """Validation function — test both valid and invalid inputs."""
    if not isinstance(age, int):
        raise TypeError("age must be an integer")
    if age < 0 or age > 150:
        raise ValueError("age must be between 0 and 150")
    return True


# ---------------------------------------------------------------------------
# Quality tools guide
# ---------------------------------------------------------------------------

def quality_tools_guide() -> dict[str, str]:
    """Python code quality tools."""
    return {
        "formatter": "Black or Ruff format — consistent code style",
        "linter": "Ruff or flake8 — catch bugs and style issues",
        "type_checker": "mypy or pyright — static type checking",
        "test_runner": "pytest — run unit and integration tests",
        "coverage": "pytest-cov — measure test coverage",
        "debugger": "breakpoint() or pdb — interactive debugging",
    }


# ---------------------------------------------------------------------------
# breakpoint() debugging
# ---------------------------------------------------------------------------

def debuggable_function(items: list[int]) -> int:
    """Function that can be debugged with breakpoint().

    Call breakpoint() to enter interactive debugger (pdb).
    In production, use PYTHONBREAKPOINT=0 to disable.
    """
    total = 0
    for i, item in enumerate(items):
        # breakpoint()  # uncomment to debug
        total += item * (i + 1)
    return total
