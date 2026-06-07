"""Module 06: Exceptions and File IO (06-异常处理和文件IO, 面试05)

Knowledge points covered:
- try/except/else/finally/raise
- custom exceptions
- exception chaining (raise ... from exc)
- precise exception catching (no bare except)
- with statement for resource management
- file encoding (encoding="utf-8")
- pathlib.Path for cross-platform paths
- JSON reading/writing
- CSV reading with csv.DictReader
- newline="" for CSV
- exception propagation and call stack
- exception boundary design
- external data validation
"""

from __future__ import annotations

import csv
import io
import json
from pathlib import Path
from typing import Any


# ---------------------------------------------------------------------------
# Custom exceptions
# ---------------------------------------------------------------------------

class AppError(Exception):
    """Base exception for the application."""

    def __init__(self, message: str, code: int = 1) -> None:
        super().__init__(message)
        self.code = code


class ValidationError(AppError):
    """Raised when input data fails validation."""

    def __init__(self, field: str, value: Any, reason: str) -> None:
        self.field = field
        self.value = value
        self.reason = reason
        super().__init__(f"Validation failed for '{field}': {reason}", code=2)


class DataNotFoundError(AppError):
    """Raised when requested data doesn't exist."""

    def __init__(self, key: str) -> None:
        self.key = key
        super().__init__(f"Data not found: {key}", code=3)


# ---------------------------------------------------------------------------
# try/except/else/finally
# ---------------------------------------------------------------------------

def safe_divide(a: float, b: float) -> dict[str, Any]:
    """Demonstrate try/except/else/finally."""
    result: float | None = None
    error: str | None = None
    cleaned = False
    try:
        result = a / b
    except ZeroDivisionError:
        error = "division by zero"
        result = None
    except TypeError as exc:
        error = f"type error: {exc}"
        result = None
    else:
        # runs only if no exception occurred
        pass
    finally:
        # always runs, even if exception or return
        cleaned = True
    return {"result": result, "error": error, "cleaned": cleaned}


# ---------------------------------------------------------------------------
# Exception chaining
# ---------------------------------------------------------------------------

def parse_int_safe(value: str) -> int:
    """Demonstrate exception chaining with 'raise ... from exc'."""
    try:
        return int(value)
    except ValueError as exc:
        raise ValidationError("number", value, "not a valid integer") from exc


# ---------------------------------------------------------------------------
# Precise exception catching (no bare except)
# ---------------------------------------------------------------------------

def precise_catch_demo(data: dict[str, Any], key: str) -> Any:
    """Always catch specific exceptions, never bare 'except'."""
    try:
        return data[key]
    except KeyError:
        raise DataNotFoundError(key)
    # NOT: except: — catches SystemExit, KeyboardInterrupt, etc.


# ---------------------------------------------------------------------------
# Context manager / with statement
# ---------------------------------------------------------------------------

class Timer:
    """Simple context manager for timing code blocks."""

    def __init__(self) -> None:
        self.elapsed: float = 0.0

    def __enter__(self) -> Timer:
        import time
        self._start = time.perf_counter()
        return self

    def __exit__(self, exc_type: type | None, exc_val: BaseException | None,
                 exc_tb: Any) -> bool:
        import time
        self.elapsed = time.perf_counter() - self._start
        return False  # don't suppress exceptions


# ---------------------------------------------------------------------------
# pathlib.Path — cross-platform path handling
# ---------------------------------------------------------------------------

def path_demo(base: str = "/tmp/demo") -> dict[str, str]:
    """Demonstrate pathlib.Path for cross-platform path handling."""
    p = Path(base)
    return {
        "parent": str(p.parent),
        "name": p.name,
        "stem": p.stem,
        "suffix": p.suffix,
        "parts": str(p.parts),
        "is_absolute": str(p.is_absolute()),
        "joinpath": str(p / "subdir" / "file.txt"),
    }


# ---------------------------------------------------------------------------
# JSON reading/writing
# ---------------------------------------------------------------------------

def write_json(data: Any, path: Path) -> None:
    """Write data to JSON file with UTF-8 encoding."""
    path.parent.mkdir(parents=True, exist_ok=True)
    with open(path, "w", encoding="utf-8") as f:
        json.dump(data, f, ensure_ascii=False, indent=2)


def read_json(path: Path) -> Any:
    """Read JSON file with UTF-8 encoding."""
    with open(path, "r", encoding="utf-8") as f:
        return json.load(f)


def json_roundtrip(data: Any) -> Any:
    """Demonstrate JSON serialization/deserialization in memory."""
    json_str = json.dumps(data, ensure_ascii=False)
    return json.loads(json_str)


# ---------------------------------------------------------------------------
# CSV reading/writing
# ---------------------------------------------------------------------------

def write_csv(rows: list[dict[str, Any]], path: Path) -> None:
    """Write CSV with newline='' (required by csv module for cross-platform)."""
    path.parent.mkdir(parents=True, exist_ok=True)
    with open(path, "w", newline="", encoding="utf-8") as f:
        if rows:
            writer = csv.DictWriter(f, fieldnames=list(rows[0].keys()))
            writer.writeheader()
            writer.writerows(rows)


def read_csv(path: Path) -> list[dict[str, str]]:
    """Read CSV using csv.DictReader."""
    with open(path, "r", newline="", encoding="utf-8") as f:
        reader = csv.DictReader(f)
        return list(reader)


def csv_roundtrip(rows: list[dict[str, Any]]) -> list[dict[str, str]]:
    """Demonstrate CSV roundtrip in memory."""
    output = io.StringIO()
    if rows:
        writer = csv.DictWriter(output, fieldnames=list(rows[0].keys()))
        writer.writeheader()
        writer.writerows(rows)
    output.seek(0)
    reader = csv.DictReader(output)
    return list(reader)


# ---------------------------------------------------------------------------
# Exception boundary design
# ---------------------------------------------------------------------------

def validate_user_data(data: dict[str, Any]) -> None:
    """Lower layer: raises specific exceptions with context."""
    if "name" not in data or not data["name"]:
        raise ValidationError("name", data.get("name"), "name is required")
    if "age" in data:
        age = data["age"]
        if not isinstance(age, int) or age < 0 or age > 150:
            raise ValidationError("age", age, "must be integer between 0 and 150")
    if "email" in data:
        email = data["email"]
        if not isinstance(email, str) or "@" not in email:
            raise ValidationError("email", email, "must be a valid email")


def process_user(data: dict[str, Any]) -> dict[str, Any]:
    """Middle layer: catches specific exceptions, adds business context."""
    try:
        validate_user_data(data)
    except ValidationError as exc:
        raise AppError(f"Cannot process user: {exc}", code=exc.code) from exc
    return {"status": "ok", "user": data}


# ---------------------------------------------------------------------------
# External data validation
# ---------------------------------------------------------------------------

def validate_positive_int(value: Any, name: str = "value") -> int:
    """Validate that value is a positive integer."""
    if not isinstance(value, int):
        raise ValidationError(name, value, "must be an integer")
    if value <= 0:
        raise ValidationError(name, value, "must be positive")
    return value


def validate_string_length(value: str, min_len: int = 1, max_len: int = 100,
                           name: str = "value") -> str:
    """Validate string length constraints."""
    if not isinstance(value, str):
        raise ValidationError(name, value, "must be a string")
    if len(value) < min_len:
        raise ValidationError(name, value, f"must be at least {min_len} characters")
    if len(value) > max_len:
        raise ValidationError(name, value, f"must be at most {max_len} characters")
    return value
