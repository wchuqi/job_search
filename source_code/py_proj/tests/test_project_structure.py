"""Tests for project_structure module (10-虚拟环境依赖和项目结构, 面试04)."""

from py_proj.project_structure import (
    dependency_resolution_info,
    entry_points_guide,
    pip_guide,
    pyproject_guide,
    requirements_txt_guide,
    src_layout_guide,
    venv_guide,
    version_constraints_guide,
)


class TestVenvGuide:
    def test_create_command(self):
        result = venv_guide()
        assert "python -m venv" in result["create"]

    def test_activation(self):
        result = venv_guide()
        assert "Scripts" in result["activate_windows"]
        assert "source" in result["activate_linux"]


class TestPipGuide:
    def test_install(self):
        result = pip_guide()
        assert "python -m pip" in result["install"]

    def test_why_m(self):
        result = pip_guide()
        assert "interpreter" in result["why_m"]


class TestRequirementsTxt:
    def test_formats(self):
        result = requirements_txt_guide()
        assert "==" in result["exact"]
        assert ">=" in result["minimum"]


class TestPyprojectToml:
    def test_sections(self):
        result = pyproject_guide()
        assert "[project]" in result
        assert "[build-system]" in result
        assert "[tool.pytest.ini_options]" in result


class TestSrcLayout:
    def test_structure(self):
        result = src_layout_guide()
        assert "src" in result["structure"]
        assert "tests" in result["structure"]
        assert "pyproject.toml" in result["structure"]


class TestEntryPoints:
    def test_guide(self):
        result = entry_points_guide()
        assert "console_scripts" in result
        assert "__name__" in result["python_m"]


class TestVersionConstraints:
    def test_formats(self):
        result = version_constraints_guide()
        assert "==" in result["exact"]
        assert "~=" in result["compatible"]


class TestDependencyResolution:
    def test_challenges(self):
        result = dependency_resolution_info()
        assert "transitive" in result
        assert "lock_file" in result
