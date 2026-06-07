"""Tests for modules_packages module (05-模块包和导入机制, 面试04)."""

import sys
import pytest
import importlib
from py_proj.modules_packages import (
    add_to_search_path,
    check_module_cache,
    check_name_shadowing,
    circular_import_strategies,
    describe_src_layout,
    get_loaded_modules,
    get_module_info,
    get_search_path,
    is_main_module,
    lazy_import_demo,
    main_guard_demo,
    reload_module,
    safe_import,
)


class TestSysPath:
    def test_get_search_path(self):
        path = get_search_path()
        assert isinstance(path, list)
        assert len(path) > 0

    def test_add_to_search_path(self):
        original_len = len(sys.path)
        test_path = "/tmp/test_path_12345"
        try:
            add_to_search_path(test_path)
            assert test_path in sys.path
        finally:
            if test_path in sys.path:
                sys.path.remove(test_path)


class TestSysModules:
    def test_check_loaded(self):
        result = check_module_cache("json")
        assert result["loaded"] is True
        assert result["name"] == "json"

    def test_check_not_loaded(self):
        result = check_module_cache("nonexistent_module_xyz_12345")
        assert result["loaded"] is False

    def test_get_loaded_modules(self):
        count = get_loaded_modules()
        assert count > 10  # many modules loaded by default


class TestSafeImport:
    def test_import_success(self):
        result = safe_import("json")
        assert result["success"] is True
        assert result["name"] == "json"

    def test_import_failure(self):
        result = safe_import("nonexistent_module_xyz")
        assert result["success"] is False
        assert "error" in result


class TestReloadModule:
    def test_reload_not_loaded(self):
        result = reload_module("nonexistent_module_xyz")
        assert result["success"] is False

    def test_reload_success(self):
        result = reload_module("json")
        assert result["success"] is True

    def test_reload_failure(self, monkeypatch):
        def fail_reload(module):
            raise RuntimeError("reload failed")

        monkeypatch.setattr(importlib, "reload", fail_reload)
        result = reload_module("json")
        assert result["success"] is False
        assert "reload failed" in result["error"]


class TestCircularImports:
    def test_strategies(self):
        result = circular_import_strategies()
        assert len(result) == 4
        assert "extract_shared" in result


class TestLazyImport:
    def test_lazy_import(self):
        mod = lazy_import_demo("json")
        assert mod.__name__ == "json"


class TestMainGuard:
    def test_main_guard_helpers(self):
        assert is_main_module() is False
        assert "main script" in main_guard_demo()


class TestModuleNameShadowing:
    def test_no_shadowing(self):
        import tempfile
        with tempfile.TemporaryDirectory() as d:
            result = check_name_shadowing(d)
            assert result == []

    def test_detects_shadowing(self):
        import tempfile, os
        with tempfile.TemporaryDirectory() as d:
            with open(os.path.join(d, "json.py"), "w") as f:
                f.write("# shadow")
            result = check_name_shadowing(d)
            assert "json" in result

    def test_shadowing_ignores_bad_directory(self):
        assert check_name_shadowing("\0bad") == []


class TestModuleInfo:
    def test_get_module_info(self):
        import json
        info = get_module_info(json)
        assert info["name"] == "json"
        assert info["file"] is not None


class TestSrcLayout:
    def test_describe(self):
        result = describe_src_layout()
        assert "src" in result
        assert "tests" in result
        assert "pyproject" in result
