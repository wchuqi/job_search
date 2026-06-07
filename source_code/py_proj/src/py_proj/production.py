"""Module 22: Production Safety, Configuration, Observability (22-生产安全配置可观测性和可靠性, 面试06)

Knowledge points covered:
- configuration layering (CLI args > env vars > config file > defaults)
- secrets management (never in code/logs)
- input validation (untrusted external data, dangerous APIs)
- path security (directory traversal prevention)
- external requests (mandatory timeouts, retry with backoff)
- log design (event name, timestamp, request/task ID, context)
- error boundaries and exit codes
- observability (logs, metrics, traces)
- supply chain security
- deployment checklist
"""

from __future__ import annotations

import logging
import os
import pathlib
import time
from typing import Any


# ---------------------------------------------------------------------------
# Configuration layering
# ---------------------------------------------------------------------------

def load_config(
    cli_args: dict[str, Any] | None = None,
    env_prefix: str = "APP_",
    defaults: dict[str, Any] | None = None,
) -> dict[str, Any]:
    """Configuration layering: CLI args > env vars > defaults."""
    config = dict(defaults or {})
    # Layer: environment variables
    for key in list(config.keys()):
        env_key = f"{env_prefix}{key.upper()}"
        env_val = os.environ.get(env_key)
        if env_val is not None:
            config[key] = env_val
    # Layer: CLI args (highest priority)
    if cli_args:
        config.update({k: v for k, v in cli_args.items() if v is not None})
    return config


# ---------------------------------------------------------------------------
# Secrets management
# ---------------------------------------------------------------------------

def mask_secrets(data: dict[str, Any], sensitive_keys: set[str] | None = None) -> dict[str, Any]:
    """Mask sensitive values in data for logging.

    NEVER log raw secrets. Use masking or secret management services.
    """
    if sensitive_keys is None:
        sensitive_keys = {"password", "secret", "token", "api_key", "authorization"}
    masked = {}
    for key, value in data.items():
        if key.lower() in sensitive_keys:
            masked[key] = "***MASKED***"
        elif isinstance(value, dict):
            masked[key] = mask_secrets(value, sensitive_keys)
        else:
            masked[key] = value
    return masked


def load_secret_from_env(key: str) -> str | None:
    """Load secret from environment variable — never hardcode."""
    return os.environ.get(key)


# ---------------------------------------------------------------------------
# Input validation — dangerous APIs
# ---------------------------------------------------------------------------

def dangerous_apis_info() -> dict[str, str]:
    """APIs that should never be used with untrusted input."""
    return {
        "eval": "executes arbitrary Python code — NEVER use with user input",
        "exec": "executes arbitrary Python code — NEVER use with user input",
        "pickle.loads": "deserializes arbitrary objects — can execute code",
        "shell_concatenation": "os.system(cmd) with user input — command injection",
        "unrestricted_file_io": "open(user_path) without validation — path traversal",
        "untrusted_archive": "zipfile.extractall without path check — zip slip",
    }


# ---------------------------------------------------------------------------
# Path security — directory traversal prevention
# ---------------------------------------------------------------------------

def safe_path_join(base: str, user_path: str) -> pathlib.Path:
    """Prevent directory traversal by resolving and checking the path.

    NEVER trust user-provided paths without validation.
    """
    base_path = pathlib.Path(base).resolve()
    target = (base_path / user_path).resolve()
    if not str(target).startswith(str(base_path)):
        raise ValueError(f"Path traversal detected: {user_path}")
    return target


def validate_filename(filename: str) -> str:
    """Validate filename — prevent path traversal and special characters."""
    if not filename:
        raise ValueError("Filename cannot be empty")
    if "/" in filename or "\\" in filename:
        raise ValueError("Filename cannot contain path separators")
    if filename.startswith("."):
        raise ValueError("Filename cannot start with dot")
    # Remove null bytes
    if "\0" in filename:
        raise ValueError("Filename cannot contain null bytes")
    return filename


# ---------------------------------------------------------------------------
# External requests — timeouts and retries
# ---------------------------------------------------------------------------

def retry_with_backoff(
    func: Any,
    max_retries: int = 3,
    base_delay: float = 1.0,
    max_delay: float = 30.0,
    *args: Any,
    **kwargs: Any,
) -> Any:
    """Retry with exponential backoff and max retries.

    Always set timeouts for external requests.
    """
    import random
    last_exc: Exception | None = None
    for attempt in range(max_retries):
        try:
            return func(*args, **kwargs)
        except Exception as exc:
            last_exc = exc
            delay = min(base_delay * (2 ** attempt) + random.random(), max_delay)
            time.sleep(delay)
    raise last_exc  # type: ignore


def request_guidelines() -> dict[str, str]:
    """Guidelines for external requests."""
    return {
        "timeout": "ALWAYS set a timeout — never wait indefinitely",
        "retry": "Retry with exponential backoff and count limit",
        "idempotency": "Know if your request is idempotent before retrying",
        "logging": "Log target URL, latency, status code, errors",
        "circuit_breaker": "Stop retrying after sustained failures",
    }


# ---------------------------------------------------------------------------
# Log design
# ---------------------------------------------------------------------------

def structured_log_event(
    event: str,
    request_id: str | None = None,
    latency_ms: float | None = None,
    **context: Any,
) -> dict[str, Any]:
    """Create a structured log event with essential fields.

    Every log entry should have: event name, timestamp, context, result.
    """
    entry: dict[str, Any] = {
        "event": event,
        "timestamp": time.time(),
    }
    if request_id:
        entry["request_id"] = request_id
    if latency_ms is not None:
        entry["latency_ms"] = latency_ms
    entry.update(context)
    return entry


def log_levels_guide() -> dict[str, str]:
    """When to use each log level."""
    return {
        "DEBUG": "Detailed diagnostic info (variable values, intermediate states)",
        "INFO": "Normal operations (startup, request handled, job completed)",
        "WARNING": "Unexpected but recoverable (deprecated API, retry needed)",
        "ERROR": "Operation failed (request failed, data corruption)",
        "CRITICAL": "System-level failure (service down, data loss)",
    }


# ---------------------------------------------------------------------------
# Error boundaries and exit codes
# ---------------------------------------------------------------------------

class ErrorBoundary:
    """Error boundary: bottom raises specific, middle adds context, entry logs and exits."""

    @staticmethod
    def bottom_layer(data: dict[str, Any]) -> Any:
        """Bottom layer: raises specific exceptions with cause chain."""
        if "id" not in data:
            raise ValueError("Missing required field 'id'")
        return data["id"]

    @staticmethod
    def middle_layer(data: dict[str, Any]) -> Any:
        """Middle layer: catches specific, adds business context."""
        try:
            return ErrorBoundary.bottom_layer(data)
        except ValueError as exc:
            raise RuntimeError(f"Cannot process record: {exc}") from exc

    @staticmethod
    def entry_layer(data: dict[str, Any]) -> dict[str, Any]:
        """Entry layer: logs, returns user message and exit code."""
        try:
            result = ErrorBoundary.middle_layer(data)
            return {"success": True, "result": result, "exit_code": 0}
        except RuntimeError as exc:
            logger = logging.getLogger(__name__)
            logger.error("Processing failed: %s", exc, exc_info=True)
            return {"success": False, "error": str(exc), "exit_code": 1}


# ---------------------------------------------------------------------------
# Observability
# ---------------------------------------------------------------------------

def observability_guide() -> dict[str, str]:
    """Three pillars of observability."""
    return {
        "logs": "Structured events with timestamp, context, severity",
        "metrics": "Numerical measurements: latency, count, error rate, resource usage",
        "traces": "Request flow across services (distributed tracing)",
        "cli_tools": "Should record latency, count, failures, output location",
    }


# ---------------------------------------------------------------------------
# Supply chain security
# ---------------------------------------------------------------------------

def supply_chain_guide() -> dict[str, str]:
    """Supply chain security best practices."""
    return {
        "venv_isolation": "Always use virtual environments",
        "version_pinning": "Record exact Python and dependency versions",
        "regular_updates": "Periodically upgrade dependencies for security patches",
        "trusted_sources": "Only install from PyPI or trusted registries",
        "typosquatting": "Verify package names before installing",
        "lock_files": "Use lock files for reproducible builds",
        "no_untrusted": "Never install packages from untrusted URLs",
    }


# ---------------------------------------------------------------------------
# Deployment checklist
# ---------------------------------------------------------------------------

def deployment_checklist() -> list[str]:
    """Deployment checklist for Python applications."""
    return [
        "Run command documented and tested",
        "Python version specified and verified",
        "Dependencies installed from lock file",
        "Configuration loaded from env vars (not hardcoded)",
        "Log location and level configured",
        "Test command runs successfully",
        "Version identifier recorded (git hash, package version)",
        "Rollback method documented and tested",
        "Secrets loaded from secret management service",
        "Health check endpoint available",
    ]
