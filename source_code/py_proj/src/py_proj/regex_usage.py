"""Module 18: Third-party regex package usage.

The `regex` package is a drop-in alternative to the standard-library `re`
module for many cases, and it also provides practical extras:

- Unicode properties such as `\\p{Letter}`
- overlapped matches
- repeated named captures
- variable-length lookbehind
- recursive patterns
- fuzzy matching
- grapheme cluster matching with `\\X`
"""

from __future__ import annotations

from dataclasses import dataclass
from typing import Any

import regex


@dataclass(frozen=True)
class FuzzyResult:
    """Result of fuzzy matching against an expected word."""

    matched: bool
    substitutions: int = 0
    insertions: int = 0
    deletions: int = 0


def unicode_words(text: str) -> list[str]:
    """Extract words with Unicode letter properties."""
    return regex.findall(r"\p{Letter}+", text)


def overlapped_substrings(text: str, pattern: str) -> list[str]:
    """Return overlapping matches, which stdlib `re.finditer` does not expose."""
    compiled = compile_user_pattern(pattern)
    return [match.group(0) for match in compiled.finditer(text, overlapped=True)]


def parse_phone_numbers(text: str) -> list[dict[str, str]]:
    """Use named captures and return structured phone-number records."""
    pattern = regex.compile(
        r"(?P<country>\+\d{1,3})\s+"
        r"(?P<area>\d{2,4})-"
        r"(?P<number>\d{6,8})"
    )
    return [match.groupdict() for match in pattern.finditer(text)]


def repeated_named_captures(csv_line: str) -> list[str]:
    """Collect every capture made by a repeated named group."""
    match = regex.fullmatch(r"(?P<item>[^,\s]+)(?:,\s*(?P<item>[^,\s]+))*", csv_line)
    if match is None:
        return []
    return match.captures("item")


def variable_length_lookbehind(text: str) -> list[str]:
    """Find order numbers preceded by a variable-length uppercase prefix."""
    return regex.findall(r"(?<=\b[A-Z]{2,10}-)\d+", text)


def has_balanced_parentheses(text: str) -> bool:
    """Validate nested parentheses with a recursive pattern."""
    return regex.fullmatch(r"\((?:[^()]++|(?R))*\)", text) is not None


def fuzzy_word_match(candidate: str, expected: str, max_errors: int = 1) -> FuzzyResult:
    """Match a candidate word against an expected word with edit-distance tolerance."""
    if max_errors < 0:
        raise ValueError("max_errors must be non-negative")

    pattern = regex.compile(
        rf"(?:{regex.escape(expected)}){{e<={max_errors}}}",
        flags=regex.BESTMATCH,
    )
    match = pattern.fullmatch(candidate)
    if match is None:
        return FuzzyResult(matched=False)

    substitutions, insertions, deletions = match.fuzzy_counts
    return FuzzyResult(
        matched=True,
        substitutions=substitutions,
        insertions=insertions,
        deletions=deletions,
    )


def grapheme_clusters(text: str) -> list[str]:
    """Split text into user-perceived characters instead of code points."""
    return regex.findall(r"\X", text)


def compile_user_pattern(pattern: str) -> regex.Pattern[str]:
    """Compile user-supplied regex and normalize compile errors."""
    try:
        return regex.compile(pattern)
    except regex.error as exc:
        raise ValueError(f"invalid regex pattern: {pattern}") from exc


def regex_feature_summary() -> dict[str, Any]:
    """Return compact examples covering the main regex package features."""
    fuzzy = fuzzy_word_match("colour", "color", max_errors=1)
    return {
        "unicode_words": unicode_words("Python, 正则, cafe"),
        "overlapped": overlapped_substrings("ababa", "aba"),
        "phones": parse_phone_numbers("+86 010-12345678; +1 415-5551234"),
        "captures": repeated_named_captures("red, green, blue"),
        "lookbehind": variable_length_lookbehind("ID-7 ORDER-20250608 AB-9"),
        "recursive": has_balanced_parentheses("(a(b)c)"),
        "fuzzy": fuzzy,
        "clusters": grapheme_clusters("e\u0301👍"),
    }
