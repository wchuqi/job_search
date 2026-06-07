"""Tests for production module (22-生产安全配置可观测性和可靠性, 面试06)."""

import os
import pytest
from py_proj.production import (
    ErrorBoundary,
    dangerous_apis_info,
    deployment_checklist,
    load_config,
    load_secret_from_env,
    log_levels_guide,
    mask_secrets,
    observability_guide,
    request_guidelines,
    retry_with_backoff,
    safe_path_join,
    structured_log_event,
    supply_chain_guide,
    validate_filename,
)


class TestConfigLayering:
    def test_defaults(self):
        config = load_config(defaults={"host": "localhost", "port": "8080"})
        assert config["host"] == "localhost"
        assert config["port"] == "8080"

    def test_cli_overrides(self):
        config = load_config(
            cli_args={"host": "remote"},
            defaults={"host": "localhost"},
        )
        assert config["host"] == "remote"

    def test_env_override(self):
        os.environ["APP_HOST"] = "envhost"
        try:
            config = load_config(defaults={"host": "localhost"})
            assert config["host"] == "envhost"
        finally:
            del os.environ["APP_HOST"]


class TestSecrets:
    def test_mask(self):
        data = {"username": "alice", "password": "secret123", "token": "abc"}
        result = mask_secrets(data)
        assert result["username"] == "alice"
        assert result["password"] == "***MASKED***"
        assert result["token"] == "***MASKED***"

    def test_mask_nested(self):
        data = {"user": {"name": "alice", "api_key": "secret"}}
        result = mask_secrets(data)
        assert result["user"]["api_key"] == "***MASKED***"

    def test_load_secret(self):
        os.environ["TEST_SECRET"] = "value"
        try:
            assert load_secret_from_env("TEST_SECRET") == "value"
        finally:
            del os.environ["TEST_SECRET"]

    def test_load_missing(self):
        assert load_secret_from_env("NONEXISTENT_SECRET") is None


class TestDangerousAPIs:
    def test_dangerous_list(self):
        result = dangerous_apis_info()
        assert "eval" in result
        assert "pickle.loads" in result
        assert "shell_concatenation" in result


class TestPathSecurity:
    def test_safe_join(self):
        result = safe_path_join("/tmp/base", "file.txt")
        assert str(result).endswith("file.txt")

    def test_traversal_blocked(self):
        with pytest.raises(ValueError, match="traversal"):
            safe_path_join("/tmp/base", "../../../etc/passwd")

    def test_validate_filename(self):
        assert validate_filename("report.csv") == "report.csv"

    def test_validate_empty(self):
        with pytest.raises(ValueError):
            validate_filename("")

    def test_validate_slash(self):
        with pytest.raises(ValueError):
            validate_filename("../etc/passwd")

    def test_validate_dot(self):
        with pytest.raises(ValueError):
            validate_filename(".hidden")

    def test_validate_null_byte(self):
        with pytest.raises(ValueError):
            validate_filename("bad\0name")


class TestRetryWithBackoff:
    def test_retry_success_after_failure(self, monkeypatch):
        monkeypatch.setattr("py_proj.production.time.sleep", lambda _: None)
        monkeypatch.setattr("random.random", lambda: 0.0)
        calls = {"count": 0}

        def flaky() -> str:
            calls["count"] += 1
            if calls["count"] < 2:
                raise RuntimeError("temporary")
            return "ok"

        assert retry_with_backoff(flaky, max_retries=3, base_delay=0.0) == "ok"
        assert calls["count"] == 2

    def test_retry_raises_last_exception(self, monkeypatch):
        monkeypatch.setattr("py_proj.production.time.sleep", lambda _: None)
        monkeypatch.setattr("random.random", lambda: 0.0)

        def always_fails() -> str:
            raise RuntimeError("failed")

        with pytest.raises(RuntimeError, match="failed"):
            retry_with_backoff(always_fails, max_retries=2, base_delay=0.0)


class TestLogDesign:
    def test_structured_event(self):
        result = structured_log_event("request.handled", request_id="abc", latency_ms=42.5)
        assert result["event"] == "request.handled"
        assert result["request_id"] == "abc"
        assert result["latency_ms"] == 42.5
        assert "timestamp" in result

    def test_log_levels(self):
        result = log_levels_guide()
        assert "DEBUG" in result
        assert "CRITICAL" in result


class TestErrorBoundary:
    def test_success(self):
        result = ErrorBoundary.entry_layer({"id": 42, "name": "test"})
        assert result["success"] is True
        assert result["exit_code"] == 0

    def test_failure(self):
        result = ErrorBoundary.entry_layer({})
        assert result["success"] is False
        assert result["exit_code"] == 1


class TestGuides:
    def test_request_guidelines(self):
        result = request_guidelines()
        assert "timeout" in result

    def test_observability(self):
        result = observability_guide()
        assert "logs" in result
        assert "metrics" in result

    def test_supply_chain(self):
        result = supply_chain_guide()
        assert "venv_isolation" in result

    def test_deployment_checklist(self):
        result = deployment_checklist()
        assert len(result) >= 8
