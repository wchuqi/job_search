"""Quantifier behavior: greedy, lazy, possessive, and clear boundaries."""

from __future__ import annotations

import re


def basic_quantifier_matches() -> dict[str, bool]:
    return {
        "star_zero": re.fullmatch(r"a*", "") is not None,
        "plus_one_or_more": re.fullmatch(r"a+", "aaa") is not None,
        "question_optional": re.fullmatch(r"colou?r", "color") is not None,
        "exact_count": re.fullmatch(r"a{3}", "aaa") is not None,
        "range_count": re.fullmatch(r"a{2,5}", "aaaa") is not None,
        "at_least": re.fullmatch(r"a{2,}", "aaaaaa") is not None,
    }


def greedy_quoted_span(text: str) -> str | None:
    match = re.search(r'".+"', text)
    return match.group(0) if match else None


def lazy_quoted_spans(text: str) -> list[str]:
    return re.findall(r'".+?"', text)


def bounded_quoted_spans(text: str) -> list[str]:
    return re.findall(r'"[^"]*"', text)


def escaped_string_literals(text: str) -> list[str]:
    return re.findall(r'"(?:\\.|[^"\\])*"', text)


def repeat_scope_results() -> dict[str, bool]:
    return {
        "ab_plus": re.fullmatch(r"ab+", "abbb") is not None,
        "ab_plus_not_abab": re.fullmatch(r"ab+", "abab") is None,
        "group_ab_plus": re.fullmatch(r"(ab)+", "abab") is not None,
    }


def comma_fields(text: str) -> list[str]:
    return re.findall(r"[^,]+", text)


def possessive_quantifier_demo(value: str) -> dict[str, bool]:
    return {
        "greedy_can_backtrack": re.fullmatch(r"\d+\d", value) is not None,
        "possessive_will_not_backtrack": re.fullmatch(r"\d++\d", value) is not None,
        "atomic_will_not_backtrack": re.fullmatch(r"(?>\d+)\d", value) is not None,
    }
