"""Extraction, replacement, splitting, cleaning, and project-style log parsing."""

from __future__ import annotations

import re
from dataclasses import dataclass

SIMPLE_LOG = re.compile(
    r"^(?P<date>\d{4}-\d{2}-\d{2}) "
    r"(?P<time>\d{2}:\d{2}:\d{2}) "
    r"(?P<level>[A-Z]+) user=(?P<user>\w+) action=(?P<action>\w+)$"
)
METRIC_LINE = re.compile(r"^ip=(?P<ip>\d{1,3}(?:\.\d{1,3}){3}) status=(?P<status>\d{3}) cost=(?P<cost>\d+)ms$")
PROJECT_LOG = re.compile(
    r"^(?P<date>\d{4}-\d{2}-\d{2}) "
    r"(?P<time>\d{2}:\d{2}:\d{2}) "
    r"(?P<level>INFO|WARN|ERROR) "
    r"ip=(?P<ip>\d{1,3}(?:\.\d{1,3}){3}) "
    r"user=(?P<user>\S+) "
    r"action=(?P<action>\w+) "
    r"cost=(?P<cost>\d+)ms$"
)
NGINX_LOG = re.compile(
    r"^(?P<ip>\d{1,3}(?:\.\d{1,3}){3}) \S+ \S+ "
    r"\[(?P<time>[^\]]+)\] "
    r'"(?P<method>[A-Z]+) (?P<path>[^ ]+) HTTP/(?P<version>[^"]+)" '
    r"(?P<status>\d{3}) (?P<bytes>\d+|-)$"
)


@dataclass(frozen=True)
class ParseResult:
    records: tuple[dict[str, object], ...]
    invalid_lines: tuple[tuple[int, str], ...]


def parse_simple_log(line: str) -> dict[str, str] | None:
    match = SIMPLE_LOG.fullmatch(line)
    return match.groupdict() if match else None


def replace_date_to_us(text: str) -> str:
    return re.sub(r"(\d{4})-(\d{2})-(\d{2})", r"\2/\3/\1", text)


def split_on_comma_space(text: str) -> list[str]:
    return re.split(r"\s*,\s*", text)


def split_words(text: str) -> list[str]:
    return re.split(r"\s+", text.strip())


def collapse_whitespace(text: str) -> str:
    return re.sub(r"\s+", " ", text).strip()


def strip_trailing_horizontal_space(text: str) -> str:
    return re.sub(r"[ \t]+$", "", text, flags=re.MULTILINE)


def parse_metric_line(line: str) -> dict[str, object] | None:
    match = METRIC_LINE.fullmatch(line)
    if not match:
        return None
    data: dict[str, object] = match.groupdict()
    data["status"] = int(data["status"])
    data["cost"] = int(data["cost"])
    return data


def mask_phone(value: str) -> str:
    return re.sub(r"^(\d{3})\d{4}(\d{4})$", r"\1****\2", value)


def mask_email_user(value: str) -> str:
    return re.sub(r"^([^@\s])[^@\s]*(@[^@\s]+)$", r"\1***\2", value)


def parse_nginx_log(line: str) -> dict[str, object] | None:
    match = NGINX_LOG.fullmatch(line)
    if not match:
        return None
    data: dict[str, object] = match.groupdict()
    data["status"] = int(data["status"])
    data["bytes"] = None if data["bytes"] == "-" else int(data["bytes"])
    return data


def parse_project_line(line: str) -> dict[str, object] | None:
    match = PROJECT_LOG.fullmatch(line)
    if not match:
        return None
    data: dict[str, object] = match.groupdict()
    data["cost"] = int(data["cost"])
    data["user"] = mask_email_user(str(data["user"]))
    return data


def parse_project_lines(lines: list[str]) -> ParseResult:
    records: list[dict[str, object]] = []
    invalid: list[tuple[int, str]] = []
    for line_no, line in enumerate(lines, start=1):
        parsed = parse_project_line(line)
        if parsed is None:
            invalid.append((line_no, line))
        else:
            records.append(parsed)
    return ParseResult(tuple(records), tuple(invalid))


def extract_url_parts(url: str) -> dict[str, str | None] | None:
    match = re.fullmatch(r"(?P<scheme>https?)://(?P<host>[^/:?#]+)(?P<path>/[^?#]*)?", url)
    return match.groupdict() if match else None
