"""Anchors, boundaries, flags, lookaround, and assertion examples."""

from __future__ import annotations

import re


def contains_date(text: str) -> bool:
    return re.search(r"\d{4}-\d{2}-\d{2}", text) is not None


def full_date(text: str) -> bool:
    return re.fullmatch(r"\d{4}-\d{2}-\d{2}", text) is not None


def absolute_whole_string_abc(text: str) -> bool:
    return re.search(r"\Aabc\Z", text) is not None


def dollar_allows_final_newline(text: str) -> bool:
    return re.match(r"^abc$", text) is not None


def find_error_lines(text: str) -> list[str]:
    return re.findall(r"^error\b.*", text, flags=re.IGNORECASE | re.MULTILINE)


def dotall_block(text: str) -> str | None:
    match = re.search(r"<body>.*</body>", text, flags=re.DOTALL)
    return match.group(0) if match else None


def verbose_log_parse(text: str) -> dict[str, str] | None:
    pattern = re.compile(
        r"""
        ^
        (?P<date>\d{4}-\d{2}-\d{2}) \s+
        (?P<level>INFO|WARN|ERROR) \s+
        (?P<message>.+)
        $
        """,
        re.VERBOSE,
    )
    match = pattern.fullmatch(text)
    return match.groupdict() if match else None


def whole_word_cat(text: str) -> list[str]:
    return re.findall(r"\bcat\b", text)


def numbers_before_unit(text: str, unit: str = "yuan") -> list[str]:
    return re.findall(rf"\d+(?={re.escape(unit)})", text)


def foo_not_followed_by_bar(text: str) -> list[str]:
    return re.findall(r"foo(?!bar)", text)


def numbers_after_currency(text: str) -> list[str]:
    return re.findall(r"(?<=USD)\d+", text)


def numbers_not_after_minus(text: str) -> list[str]:
    return re.findall(r"(?<![-\d])\d+", text)


def identifiers_not_test_prefix(values: list[str]) -> list[str]:
    pattern = re.compile(r"^(?!test_)[A-Za-z_][A-Za-z0-9_]*$")
    return [value for value in values if pattern.fullmatch(value)]


def valid_password(value: str) -> bool:
    return re.fullmatch(r"^(?=.*[A-Za-z])(?=.*\d)[A-Za-z\d@$!%*#?&]{8,20}$", value) is not None


def filename_not_tmp(value: str) -> bool:
    return re.fullmatch(r"^(?!.*\.tmp$).+", value) is not None


def parenthesized_content(text: str) -> list[str]:
    return re.findall(r"(?<=\()[^)]+(?=\))", text)
