"""Module 10: Virtual Environments, Dependencies and Project Structure (10-虚拟环境依赖和项目结构, 面试04)

Knowledge points covered:
- venv (standard library virtual environment tool)
- pip (package install/uninstall/list)
- requirements.txt (dependency list)
- pyproject.toml (modern build system and tool config)
- src layout (source under src/package_name/)
- entry points (console scripts or python -m)
- version constraints
- python -m pip to ensure correct interpreter
- dependency resolution challenges
"""

from __future__ import annotations

from typing import Any


# ---------------------------------------------------------------------------
# venv — virtual environments
# ---------------------------------------------------------------------------

def venv_guide() -> dict[str, str]:
    """Guide for Python virtual environments."""
    return {
        "create": "python -m venv .venv",
        "activate_windows": ".venv\\Scripts\\activate",
        "activate_linux": "source .venv/bin/activate",
        "deactivate": "deactivate",
        "purpose": "Isolate project dependencies from system Python",
        "why_venv": "Different projects can use different package versions",
        "best_practice": "Always use venv, never install packages globally",
    }


# ---------------------------------------------------------------------------
# pip
# ---------------------------------------------------------------------------

def pip_guide() -> dict[str, str]:
    """Guide for pip package management."""
    return {
        "install": "python -m pip install package_name",
        "install_version": "python -m pip install 'package>=1.0,<2.0'",
        "install_dev": "python -m pip install -e '.[dev]'",
        "uninstall": "python -m pip uninstall package_name",
        "list": "python -m pip list",
        "freeze": "python -m pip freeze > requirements.txt",
        "why_m": "Using 'python -m pip' ensures correct interpreter",
    }


# ---------------------------------------------------------------------------
# requirements.txt
# ---------------------------------------------------------------------------

def requirements_txt_guide() -> dict[str, str]:
    """Guide for requirements.txt format."""
    return {
        "exact": "package==1.2.3",
        "minimum": "package>=1.2.0",
        "range": "package>=1.2.0,<2.0.0",
        "compatible": "package~=1.2.0 (>=1.2.0,<1.3.0)",
        "from_vcs": "package @ git+https://github.com/user/repo",
        "use_case": "Simple projects, application deployment",
        "limitation": "No build system info, no tool configuration",
    }


# ---------------------------------------------------------------------------
# pyproject.toml
# ---------------------------------------------------------------------------

def pyproject_guide() -> dict[str, str]:
    """Guide for pyproject.toml structure."""
    return {
        "[project]": "Name, version, description, dependencies, requires-python",
        "[project.scripts]": "Console entry points (CLI commands)",
        "[build-system]": "Build backend (setuptools, hatchling, etc.)",
        "[tool.pytest.ini_options]": "pytest configuration",
        "[tool.mypy]": "mypy type checking configuration",
        "[tool.ruff]": "Ruff linter/formatter configuration",
        "benefit": "Single file for build config, dependencies, and tool settings",
    }


# ---------------------------------------------------------------------------
# src layout
# ---------------------------------------------------------------------------

def src_layout_guide() -> dict[str, str]:
    """Guide for src layout project structure."""
    return {
        "structure": """
project/
├── pyproject.toml
├── README.md
├── src/
│   └── package_name/
│       ├── __init__.py
│       └── module.py
├── tests/
│   └── test_module.py
└── .venv/
""",
        "benefit1": "Prevents accidental import of uninstalled source code",
        "benefit2": "Tests run against installed package, not local source",
        "benefit3": "Clear separation of source, tests, and config",
    }


# ---------------------------------------------------------------------------
# Entry points
# ---------------------------------------------------------------------------

def entry_points_guide() -> dict[str, str]:
    """Guide for defining CLI entry points."""
    return {
        "console_scripts": "[project.scripts]\nmycli = 'package.module:main'",
        "python_m": "python -m package.module (uses __name__ == '__main__' guard)",
        "console_script_benefit": "Creates a CLI command in the user's PATH",
        "module_benefit": "No installation needed, works with source layout",
    }


# ---------------------------------------------------------------------------
# Version constraints
# ---------------------------------------------------------------------------

def version_constraints_guide() -> dict[str, str]:
    """Guide for dependency version constraints."""
    return {
        "exact": "==1.2.3 — pin exact version (reproducibility)",
        "minimum": ">=1.2.0 — allow newer versions (flexibility)",
        "range": ">=1.2.0,<2.0.0 — allow minor updates, not major",
        "compatible": "~=1.2.0 — allow patch updates within minor version",
        "application": "Lock exact versions for reproducible deployment",
        "library": "Declare broad ranges for compatibility with many consumers",
    }


# ---------------------------------------------------------------------------
# Dependency resolution
# ---------------------------------------------------------------------------

def dependency_resolution_info() -> dict[str, str]:
    """Challenges in dependency resolution."""
    return {
        "transitive": "Dependencies of dependencies can conflict",
        "platform": "Some packages only work on specific OS/architecture",
        "python_version": "Packages require specific Python version ranges",
        "resolution": "pip resolver finds compatible version combination",
        "lock_file": "Lock file records exact resolved versions",
        "reproducibility": "Same lock file → same installed versions everywhere",
    }
