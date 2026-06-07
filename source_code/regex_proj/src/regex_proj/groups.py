"""Grouping, capture, named capture, alternation, and references."""

from __future__ import annotations

import re

DATE_GROUPS = re.compile(r"(\d{4})-(\d{2})-(\d{2})")
DATE_NAMED = re.compile(r"(?P<year>\d{4})-(?P<month>\d{2})-(?P<day>\d{2})")
URL_PARTS = re.compile(r"^(?P<scheme>https?)://(?P<host>[^/:?#]+)(?P<path>/[^?#]*)?")
LOG_NAMED = re.compile(
    r"(?P<level>INFO|WARN|ERROR) (?P<date>\d{4}-\d{2}-\d{2}) (?P<message>.+)"
)


def parse_date_numbered(value: str) -> tuple[str, str, str] | None:
    match = DATE_GROUPS.fullmatch(value)
    return match.groups() if match else None


def parse_date_named(value: str) -> dict[str, str] | None:
    match = DATE_NAMED.fullmatch(value)
    return match.groupdict() if match else None


def supports_http_or_https(value: str) -> bool:
    return re.fullmatch(r"(?:https?)://[^ ]+", value) is not None


def parse_url(value: str) -> dict[str, str | None] | None:
    match = URL_PARTS.fullmatch(value)
    return match.groupdict() if match else None


def parse_level_date_message(value: str) -> dict[str, str] | None:
    match = LOG_NAMED.fullmatch(value)
    return match.groupdict() if match else None


def alternation_order(value: str, pattern: str = r"cat|catalog") -> str | None:
    match = re.search(pattern, value)
    return match.group(0) if match else None


def grey_or_gray(value: str) -> bool:
    return re.fullmatch(r"gr(?:a|e)y", value) is not None


def repeated_word(value: str) -> str | None:
    match = re.fullmatch(r"([A-Za-z]+)\s+\1", value)
    return match.group(1) if match else None


def matching_html_pair(value: str) -> str | None:
    match = re.fullmatch(r"<([a-z][a-z0-9]*)>.*?</\1>", value)
    return match.group(1) if match else None


def replace_date_to_us(value: str) -> str:
    return DATE_GROUPS.sub(r"\2/\3/\1", value)
