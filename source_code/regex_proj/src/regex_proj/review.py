"""Production regex review checklist and case-study helpers."""

from __future__ import annotations

import posixpath
import re
from dataclasses import dataclass
from urllib.parse import unquote, urlparse


@dataclass(frozen=True)
class RegexDesignDoc:
    name: str
    purpose: str
    input_source: str
    user_controlled: bool
    max_length: int
    engine: str
    api: str
    pattern: str
    capture_fields: tuple[str, ...]
    known_limits: tuple[str, ...]


def build_phone_mask_doc() -> RegexDesignDoc:
    return RegexDesignDoc(
        name="phone_mask",
        purpose="mask middle four digits of an 11 digit phone number",
        input_source="form field",
        user_controlled=True,
        max_length=11,
        engine="python re",
        api="fullmatch then sub",
        pattern=r"^(\d{3})\d{4}(\d{4})$",
        capture_fields=("prefix", "suffix"),
        known_limits=("shape only", "does not validate carrier allocation"),
    )


def email_shape(value: str) -> bool:
    return re.fullmatch(r"^[^@\s]+@[^@\s]+\.[^@\s]+$", value) is not None


def url_precheck(value: str) -> bool:
    return re.fullmatch(r"^https?://[^\s/$.?#].[^\s]*$", value) is not None


def parse_bounded_kv_log(line: str) -> dict[str, object] | None:
    match = re.fullmatch(
        r"^level=(?P<level>INFO|WARN|ERROR) user=(?P<user>\S+) cost=(?P<cost>\d+)ms path=(?P<path>/\S*)$",
        line,
    )
    if not match:
        return None
    data: dict[str, object] = match.groupdict()
    data["cost"] = int(data["cost"])
    return data


def valid_password_policy(value: str) -> bool:
    return re.fullmatch(r"^(?=.*[A-Za-z])(?=.*\d)[A-Za-z\d@$!%*#?&]{8,20}$", value) is not None


def path_allowed_after_normalization(raw_path: str) -> bool:
    parsed = urlparse(raw_path)
    decoded = unquote(parsed.path or raw_path)
    normalized = posixpath.normpath(decoded)
    return normalized == "/admin" or normalized.startswith("/admin/")


def should_use_parser(format_name: str) -> bool:
    return format_name.lower() in {"html", "json", "csv", "url"}


def sample_template() -> dict[str, tuple[str, ...]]:
    return {
        "positive": ("shortest valid", "longest valid", "common valid", "boundary valid"),
        "negative": ("empty", "too short", "too long", "illegal char", "missing field", "extra field"),
        "performance": ("long valid", "long late failure", "repeated structure"),
    }


def review_findings(
    pattern: str,
    api: str,
    user_controlled: bool,
    max_length: int | None,
    security_boundary: bool,
) -> dict[str, tuple[str, ...]]:
    correctness = []
    performance = []
    security = []
    maintainability = []
    compatibility = []
    if api != "fullmatch" and not (pattern.startswith("^") and pattern.endswith("$")):
        correctness.append("not clearly whole-input checked")
    if "." in pattern or r"\w" in pattern or r"\s" in pattern:
        correctness.append("character class may be too broad")
    if re.search(r"\([^)]*[+*][^)]*\)[+*{]", pattern):
        performance.append("nested quantifier")
    if pattern.count(".*") > 1:
        performance.append("multiple dot-star")
    if max_length is None:
        performance.append("missing input length limit")
    if user_controlled:
        security.append("user controlled input")
    if security_boundary:
        security.append("do not rely on regex alone for security")
    if "(" in pattern and "?P<" not in pattern and "?<" not in pattern:
        maintainability.append("consider named or noncapturing groups")
    if any(token in pattern for token in ("(?<=", "(?>", "++", "\\1")):
        compatibility.append("dialect-specific feature")
    return {
        "correctness": tuple(correctness),
        "performance": tuple(performance),
        "security": tuple(security),
        "maintainability": tuple(maintainability),
        "compatibility": tuple(compatibility),
    }
