"""Character, escaping, character class, anchor, and flag examples."""

from __future__ import annotations

import re

DATE_SHAPE = re.compile(r"\d{4}-\d{2}-\d{2}")
DATE_FULL = re.compile(r"^\d{4}-\d{2}-\d{2}$")
SIX_DIGIT_CODE = re.compile(r"^\d{6}$")
HEX_COLOR = re.compile(r"^#[A-Fa-f0-9]{6}$")
IDENTIFIER = re.compile(r"^[A-Za-z_][A-Za-z0-9_]*$")
WINDOWS_PATH_WITH_BACKSLASH = re.compile(r"^[A-Za-z]:\\(?:[^\\/:*?\"<>|\r\n]+\\?)+$")

SPECIAL_CHARS = ".^$*+?{}[]\\|()"


def escape_literal(value: str) -> str:
    """Return a regex that matches the text literally."""
    return re.escape(value)


def find_date_shapes(text: str) -> list[str]:
    return DATE_SHAPE.findall(text)


def is_date_shape(value: str) -> bool:
    return DATE_FULL.fullmatch(value) is not None


def is_six_digit_code(value: str) -> bool:
    return SIX_DIGIT_CODE.fullmatch(value) is not None


def is_hex_color(value: str) -> bool:
    return HEX_COLOR.fullmatch(value) is not None


def is_identifier(value: str) -> bool:
    return IDENTIFIER.fullmatch(value) is not None


def is_windows_path_shape(value: str) -> bool:
    return WINDOWS_PATH_WITH_BACKSLASH.fullmatch(value) is not None


def dot_matches(value: str, dotall: bool = False) -> bool:
    flags = re.DOTALL if dotall else 0
    return re.fullmatch(r"a.b", value, flags) is not None


def char_class_examples(value: str) -> dict[str, bool]:
    return {
        "in_abc": re.fullmatch(r"[abc]", value) is not None,
        "hex_digit": re.fullmatch(r"[A-Fa-f0-9]", value) is not None,
        "not_digit": re.fullmatch(r"[^0-9]", value) is not None,
        "hyphen_literal": re.fullmatch(r"[-a-z]", value) is not None,
        "right_bracket_literal": re.fullmatch(r"[\]]", value) is not None,
    }


def predefined_class_report(value: str) -> dict[str, bool]:
    return {
        "digit": re.fullmatch(r"\d", value) is not None,
        "not_digit": re.fullmatch(r"\D", value) is not None,
        "word_unicode": re.fullmatch(r"\w", value) is not None,
        "word_ascii": re.fullmatch(r"\w", value, re.ASCII) is not None,
        "space": re.fullmatch(r"\s", value) is not None,
        "not_space": re.fullmatch(r"\S", value) is not None,
    }


def literal_special_char_matches(char: str) -> bool:
    return re.fullmatch(escape_literal(char), char) is not None and char in SPECIAL_CHARS
