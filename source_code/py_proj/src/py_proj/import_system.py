"""Module 19: Import System, Packaging and Dependency Resolution (19-导入系统打包发布和依赖解析, 面试04)

Knowledge points covered:
- import 4-step process
- sys.path search order
- sys.modules and partially initialized modules
- circular import resolution
- __init__.py execution cost
- relative vs absolute imports
- namespace packages
- modern src layout
- pyproject.toml structure
- wheel vs sdist vs editable install
- dependency resolution
- requires-python
- troubleshooting import issues
"""

from __future__ import annotations

import importlib
import sys
from typing import Any


# ---------------------------------------------------------------------------
# Import 4-step process
# ---------------------------------------------------------------------------

def import_steps() -> list[str]:
    """The 4-step import process."""
    return [
        "1. Check sys.modules cache — return if already loaded",
        "2. Find module spec via finders (sys.meta_path)",
        "3. Load and execute module via loader (creates module object, executes code)",
        "4. Write module to sys.modules cache",
    ]


def demonstrate_import_cache() -> dict[str, Any]:
    """Show that imports are cached in sys.modules."""
    # First import
    import json
    first_id = id(sys.modules["json"])
    # Second import — returns cached
    import json
    second_id = id(sys.modules["json"])
    return {
        "cached": first_id == second_id,
        "module_count": len(sys.modules),
    }


# ---------------------------------------------------------------------------
# sys.path search order
# ---------------------------------------------------------------------------

def sys_path_order() -> list[str]:
    """sys.path search order."""
    return [
        "1. Script directory (or '' for current dir)",
        "2. PYTHONPATH environment variable",
        "3. Standard library directory",
        "4. site-packages (pip installed packages)",
        ".pth files (easy-install, custom paths)",
    ]


# ---------------------------------------------------------------------------
# Circular imports
# ---------------------------------------------------------------------------

def circular_import_strategies() -> dict[str, str]:
    """Strategies to resolve circular imports."""
    return {
        "extract_shared": "Move shared code to a third module both can import",
        "lazy_import": "Import inside function body instead of module top level",
        "reverse": "Restructure dependencies so only one imports the other",
        "callbacks": "Pass functions/objects as arguments instead of importing",
        "partial_init": "sys.modules entry exists before module body finishes — can work but fragile",
    }


# ---------------------------------------------------------------------------
# Relative vs absolute imports
# ---------------------------------------------------------------------------

def import_styles() -> dict[str, str]:
    """Import styles and when to use each."""
    return {
        "absolute": "from package.subpackage import module — clear, unambiguous",
        "relative": "from . import module — shorter within same package",
        "parent_relative": "from ..other_package import module — cross-package",
        "recommendation": "Prefer absolute imports for clarity; use relative for intra-package",
        "note": "Relative imports only work within packages, not in scripts",
    }


# ---------------------------------------------------------------------------
# Namespace packages
# ---------------------------------------------------------------------------

def namespace_packages_info() -> dict[str, str]:
    """Namespace packages: no __init__.py, multiple path contributions."""
    return {
        "definition": "Package split across multiple directories",
        "no_init": "No __init__.py required",
        "use_case": "Large organizations splitting a package across repos",
        "example": "google.cloud — contributed by multiple packages",
        "risk": "Accidental namespace packages from missing __init__.py",
    }


# ---------------------------------------------------------------------------
# Packaging formats
# ---------------------------------------------------------------------------

def packaging_formats() -> dict[str, str]:
    """Different packaging and distribution formats."""
    return {
        "sdist": "Source distribution — contains source code, built on install",
        "wheel": "Built distribution — pre-compiled, faster install",
        "editable": "pip install -e . — links to source, changes reflected immediately",
        "wheel_benefit": "No build step during install, deterministic",
        "sdist_use": "When wheel isn't available or for development",
    }


# ---------------------------------------------------------------------------
# Import troubleshooting
# ---------------------------------------------------------------------------

def import_troubleshooting() -> dict[str, str]:
    """Common import problems and solutions."""
    return {
        "ModuleNotFoundError": "Check: correct venv? package installed? sys.path? module name?",
        "wrong_module": "Check: local file shadowing stdlib? __pycache__ stale?",
        "relative_import_fail": "Must be inside a package; script can't use relative imports",
        "circular_import": "Module partially initialized; extract shared code or lazy import",
        "install_ok_test_fail": "Editable install: test runs against source, not installed",
        "dependency_conflict": "Check: pip check, version constraints, transitive deps",
    }


# ---------------------------------------------------------------------------
# requires-python
# ---------------------------------------------------------------------------

def requires_python_info() -> dict[str, str]:
    """requires-python in pyproject.toml."""
    return {
        "purpose": "Declares which Python versions the package supports",
        "example": 'requires-python = ">=3.10"',
        "pip_behavior": "pip won't install if current Python doesn't match",
        "best_practice": "Set conservatively — test on all claimed versions",
    }
