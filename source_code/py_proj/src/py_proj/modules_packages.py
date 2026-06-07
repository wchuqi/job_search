"""Module 05: Modules, Packages and Import Mechanism (05-模块包和导入机制, 面试04)

Knowledge points covered:
- module (single .py file)
- package (directory with __init__.py or namespace package)
- sys.path (module search path order)
- sys.modules (loaded module cache)
- absolute imports vs relative imports
- if __name__ == "__main__" entry guard
- module top-level code execution on first import
- src layout project structure
- python -m package.module execution
- circular import detection and resolution
- ModuleNotFoundError troubleshooting
- avoiding standard library name shadowing
"""

from __future__ import annotations

import importlib
import sys
from pathlib import Path
from typing import Any


# ---------------------------------------------------------------------------
# sys.path — module search path
# ---------------------------------------------------------------------------

def get_search_path() -> list[str]:
    """Return sys.path — the module search path order.

    Order: script dir → PYTHONPATH → stdlib → site-packages → .pth files
    """
    return list(sys.path)


def add_to_search_path(path: str) -> None:
    """Add a directory to sys.path at runtime."""
    if path not in sys.path:
        sys.path.insert(0, path)


# ---------------------------------------------------------------------------
# sys.modules — loaded module cache
# ---------------------------------------------------------------------------

def check_module_cache(module_name: str) -> dict[str, Any]:
    """Check if a module is already loaded in sys.modules."""
    if module_name in sys.modules:
        mod = sys.modules[module_name]
        return {
            "loaded": True,
            "name": module_name,
            "file": getattr(mod, "__file__", "builtin"),
        }
    return {"loaded": False, "name": module_name}


def get_loaded_modules() -> int:
    """Return count of currently loaded modules."""
    return len(sys.modules)


# ---------------------------------------------------------------------------
# Import mechanism demo
# ---------------------------------------------------------------------------

def safe_import(module_name: str) -> dict[str, Any]:
    """Safely import a module, handling ModuleNotFoundError."""
    try:
        mod = importlib.import_module(module_name)
        return {
            "success": True,
            "name": module_name,
            "file": getattr(mod, "__file__", "unknown"),
        }
    except ModuleNotFoundError as exc:
        return {
            "success": False,
            "name": module_name,
            "error": str(exc),
        }


def reload_module(module_name: str) -> dict[str, Any]:
    """Reload an already-imported module."""
    if module_name not in sys.modules:
        return {"success": False, "error": f"{module_name} not loaded"}
    try:
        mod = importlib.reload(sys.modules[module_name])
        return {"success": True, "name": module_name}
    except Exception as exc:
        return {"success": False, "error": str(exc)}


# ---------------------------------------------------------------------------
# if __name__ == "__main__" guard
# ---------------------------------------------------------------------------

def is_main_module() -> bool:
    """Check if current module is the entry point."""
    return __name__ == "__main__"


def main_guard_demo() -> str:
    """Demonstrate the __name__ guard pattern.

    This code only runs when the file is executed directly,
    not when imported as a module.
    """
    return "This runs only as main script"


# ---------------------------------------------------------------------------
# Module metadata
# ---------------------------------------------------------------------------

def get_module_info(module: Any) -> dict[str, Any]:
    """Extract common module metadata."""
    return {
        "name": getattr(module, "__name__", None),
        "file": getattr(module, "__file__", None),
        "doc": getattr(module, "__doc__", None),
        "package": getattr(module, "__package__", None),
        "spec": str(getattr(module, "__spec__", None)),
    }


# ---------------------------------------------------------------------------
# Circular import resolution strategies
# ---------------------------------------------------------------------------

def circular_import_strategies() -> dict[str, str]:
    """Strategies to resolve circular imports.

    1. Extract shared definitions into a third module
    2. Lazy import (import inside function)
    3. Reverse dependency direction
    4. Use interfaces/callbacks
    """
    return {
        "extract_shared": "Move shared code to a separate module that both can import",
        "lazy_import": "Import inside function body, not at module top level",
        "reverse_dependency": "Restructure so one module depends on the other, not both",
        "use_callbacks": "Pass functions/objects as arguments instead of importing",
    }


def lazy_import_demo(module_name: str) -> Any:
    """Lazy import: defer import to when actually needed."""
    # Import inside function — only triggered when function is called
    mod = importlib.import_module(module_name)
    return mod


# ---------------------------------------------------------------------------
# Standard library name shadowing
# ---------------------------------------------------------------------------

def check_name_shadowing(directory: str) -> list[str]:
    """Check if local files shadow standard library modules.

    Common mistakes: json.py, typing.py, random.py, email.py, etc.
    """
    stdlib_names = {
        "json", "typing", "random", "email", "collections", "copy",
        "csv", "datetime", "hashlib", "http", "io", "logging", "math",
        "os", "pathlib", "re", "socket", "sqlite3", "string", "sys",
        "tempfile", "threading", "time", "unittest", "xml", "zipfile",
    }
    shadowed = []
    try:
        p = Path(directory)
        for f in p.glob("*.py"):
            if f.stem in stdlib_names:
                shadowed.append(f.stem)
    except Exception:
        pass
    return shadowed


# ---------------------------------------------------------------------------
# src layout
# ---------------------------------------------------------------------------

def describe_src_layout() -> dict[str, str]:
    """Describe the recommended src layout for Python projects."""
    return {
        "root": "project/",
        "pyproject": "pyproject.toml — build config, dependencies, tool settings",
        "src": "src/package_name/ — source code lives here",
        "tests": "tests/ — test files mirror src structure",
        "readme": "README.md — project documentation",
        "venv": ".venv/ — virtual environment (gitignored)",
        "benefit": "Prevents accidental import of uninstalled code",
    }
