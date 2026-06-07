"""Tests for import_system module (19-导入系统打包发布和依赖解析, 面试04)."""

from py_proj.import_system import (
    circular_import_strategies,
    demonstrate_import_cache,
    import_steps,
    import_styles,
    import_troubleshooting,
    namespace_packages_info,
    packaging_formats,
    requires_python_info,
    sys_path_order,
)


class TestImportSteps:
    def test_steps(self):
        steps = import_steps()
        assert len(steps) == 4
        assert "sys.modules" in steps[0]
        assert "spec" in steps[1]
        assert "Load" in steps[2]
        assert "sys.modules" in steps[3]


class TestImportCache:
    def test_cache(self):
        result = demonstrate_import_cache()
        assert result["cached"] is True
        assert result["module_count"] > 0


class TestSysPath:
    def test_order(self):
        result = sys_path_order()
        assert len(result) >= 4
        assert "Script" in result[0]
        assert "PYTHONPATH" in result[1]


class TestCircularImports:
    def test_strategies(self):
        result = circular_import_strategies()
        assert len(result) >= 4
        assert "lazy_import" in result


class TestImportStyles:
    def test_styles(self):
        result = import_styles()
        assert "absolute" in result
        assert "relative" in result
        assert "recommendation" in result


class TestNamespacePackages:
    def test_info(self):
        result = namespace_packages_info()
        assert "no_init" in result
        assert "google" in result["example"]


class TestPackagingFormats:
    def test_formats(self):
        result = packaging_formats()
        assert "sdist" in result
        assert "wheel" in result
        assert "editable" in result


class TestImportTroubleshooting:
    def test_solutions(self):
        result = import_troubleshooting()
        assert "ModuleNotFoundError" in result
        assert "circular_import" in result


class TestRequiresPython:
    def test_info(self):
        result = requires_python_info()
        assert ">=3.10" in result["example"]
        assert "pip" in result["pip_behavior"]
