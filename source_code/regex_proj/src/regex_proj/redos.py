"""ReDoS audit, attack sample, and rewrite examples."""

from __future__ import annotations

import re
from dataclasses import dataclass


@dataclass(frozen=True)
class RedosAudit:
    pattern: str
    risk: str
    findings: tuple[str, ...]
    mitigations: tuple[str, ...]


def has_nested_quantifier(pattern: str) -> bool:
    return re.search(r"\([^)]*[+*][^)]*\)[+*{]", pattern) is not None


def has_overlapping_a_branch(pattern: str) -> bool:
    return "(a|aa)" in pattern or "(aa|a)" in pattern


def has_multiple_dot_star(pattern: str) -> bool:
    return pattern.count(".*") >= 2


def has_late_failure_shape(pattern: str) -> bool:
    return pattern.endswith("$") and any(token in pattern for token in ("+", "*", "?"))


def audit_regex(
    pattern: str,
    user_controlled: bool,
    max_length: int | None,
    has_timeout: bool,
) -> RedosAudit:
    findings = []
    if has_nested_quantifier(pattern):
        findings.append("nested_quantifier")
    if has_overlapping_a_branch(pattern):
        findings.append("overlapping_branch")
    if has_multiple_dot_star(pattern):
        findings.append("multiple_dot_star")
    if "\\1" in pattern or "\\g<" in pattern:
        findings.append("backreference")
    if user_controlled and max_length is None:
        findings.append("unbounded_user_input")
    if not has_timeout:
        findings.append("no_timeout")
    if has_late_failure_shape(pattern):
        findings.append("late_failure_possible")
    risk = "exponential" if {"nested_quantifier", "late_failure_possible"} <= set(findings) else "review"
    risk = "quadratic" if risk == "review" and "multiple_dot_star" in findings else risk
    risk = "low" if risk == "review" and not findings else risk
    mitigations = (
        "limit input length",
        "make branches mutually exclusive",
        "replace nested quantifiers",
        "use parser or string API when clearer",
        "use a timeout or linear engine for untrusted input",
    )
    return RedosAudit(pattern, risk, tuple(findings), mitigations)


def attack_sample(pattern: str, length: int = 16) -> str:
    if "a" in pattern:
        return "a" * length + "!"
    if r"\w" in pattern:
        return "word" * length + "!"
    return "x" * length + "!"


def rewrite(pattern: str) -> str:
    rewrites = {
        r"^(a+)+$": r"^a+$",
        r"^(a|aa)+$": r"^a+$",
        r"^(.+)+@example\.com$": r"^[^@\s]+@example\.com$",
        r"^(a?){30}a{30}$": r"^a{30,60}$",
        r"^.*foo.*bar$": "use text.find('foo') before text.find('bar')",
    }
    return rewrites.get(pattern, "add explicit bounds, length limits, and performance tests")


def risk_conditions(user_controlled: bool, many_paths: bool, late_failure: bool) -> bool:
    return user_controlled and many_paths and late_failure
