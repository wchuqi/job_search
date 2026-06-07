"""Regex dialect, source literal, API, Unicode, and replacement differences."""

from __future__ import annotations

import re
from dataclasses import dataclass


@dataclass(frozen=True)
class FeatureSupport:
    java: bool
    javascript: bool
    python_re: bool
    pcre: bool
    dotnet: bool
    re2: bool


FEATURES = {
    "named_group": FeatureSupport(True, True, True, True, True, True),
    "backreference": FeatureSupport(True, True, True, True, True, False),
    "lookahead": FeatureSupport(True, True, True, True, True, False),
    "lookbehind": FeatureSupport(True, True, True, True, True, False),
    "possessive_quantifier": FeatureSupport(True, False, True, True, True, False),
    "atomic_group": FeatureSupport(True, False, True, True, True, False),
}

REPLACEMENTS = {
    "javascript": ("$1", "$<name>"),
    "java": ("$1", "${name}"),
    "python": (r"\1 or \g<1>", r"\g<name>"),
    "dotnet": ("$1", "${name}"),
}


def source_literal(pattern: str, target: str) -> str:
    escaped = pattern.replace("\\", "\\\\")
    if target == "java":
        return f'"{escaped}"'
    if target == "javascript_literal":
        return f"/{pattern}/"
    if target == "javascript_constructor":
        return f'new RegExp("{escaped}")'
    if target == "python":
        return f'r"{pattern}"'
    if target == "json":
        return f'"{escaped}"'
    return pattern


def feature_supported(feature: str, engine: str) -> bool:
    return getattr(FEATURES[feature], engine)


def replacement_syntax(language: str) -> tuple[str, str]:
    return REPLACEMENTS[language]


def python_api_results(pattern: str, text: str) -> dict[str, object]:
    return {
        "match": re.match(pattern, text).group(0) if re.match(pattern, text) else None,
        "search": re.search(pattern, text).group(0) if re.search(pattern, text) else None,
        "fullmatch": re.fullmatch(pattern, text).group(0) if re.fullmatch(pattern, text) else None,
        "findall_no_group": re.findall(pattern, text),
        "findall_group": re.findall(r"([a-z]+)(\d+)", text),
    }


class JavaScriptGlobalDigit:
    def __init__(self) -> None:
        self.last_index = 0

    def test(self, text: str) -> bool:
        match = re.search(r"\d", text[self.last_index :])
        if match is None:
            self.last_index = 0
            return False
        self.last_index += match.end()
        return True


def unicode_word_report(value: str) -> dict[str, bool]:
    return {
        "python_unicode_word": re.fullmatch(r"\w+", value) is not None,
        "python_ascii_word": re.fullmatch(r"\w+", value, re.ASCII) is not None,
        "word_boundary_finds": re.search(r"\b", value) is not None,
    }


def migration_checklist(pattern: str, source: str, target: str) -> tuple[str, ...]:
    checks = [
        f"source engine: {source}",
        f"target engine: {target}",
        "check source string escaping",
        "check API match/search/fullmatch semantics",
        "check flags m/s/i/u/x",
        "check replacement syntax",
        "check Unicode behavior",
        "add target runtime tests",
    ]
    if "(?<" in pattern or "(?P<" in pattern:
        checks.append("check named group syntax")
    if "\\1" in pattern:
        checks.append("check backreference support")
    return tuple(checks)
