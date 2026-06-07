"""Backtracking engine traces and risk intuition helpers."""

from __future__ import annotations

import re
from dataclasses import dataclass


@dataclass(frozen=True)
class EngineComparison:
    engine: str
    backtracking: bool
    supports_backreference: bool
    redos_risk: str


def trace_a_dot_star_b(text: str) -> list[dict[str, object]]:
    end = len(text)
    last_b = text.rfind("b")
    return [
        {"step": 1, "token": "a", "pos": 0, "action": "match a"},
        {"step": 2, "token": ".*", "pos": 1, "action": f"greedily consume to {end}"},
        {"step": 3, "token": "b", "pos": end, "action": "fail at end"},
        {"step": 4, "token": ".*", "pos": last_b, "action": "backtrack to last b"},
        {"step": 5, "token": "b", "pos": last_b, "action": "match b"},
    ]


def unanchored_start_attempts(pattern: str, text: str) -> list[int]:
    match = re.search(pattern, text)
    return list(range(match.start() + 1)) if match else list(range(len(text)))


def first_branch_match(pattern: str, text: str) -> str | None:
    match = re.search(pattern, text)
    return match.group(0) if match else None


def capture_after_backtracking(text: str) -> str | None:
    match = re.fullmatch(r"(a|ab)c", text)
    return match.group(1) if match else None


def nested_quantifier_partitions(length: int) -> list[tuple[int, ...]]:
    partitions: list[tuple[int, ...]] = []
    for mask in range(1 << (length - 1)):
        current = 1
        parts: list[int] = []
        for bit in range(length - 1):
            if mask & (1 << bit):
                parts.append(current)
                current = 1
            else:
                current += 1
        parts.append(current)
        partitions.append(tuple(parts))
    return partitions


def nested_quantifier_path_count(length: int) -> int:
    return len(nested_quantifier_partitions(length))


def engine_comparisons() -> list[EngineComparison]:
    return [
        EngineComparison("backtracking NFA", True, True, "high for ambiguous patterns"),
        EngineComparison("RE2-style linear", False, False, "low by design"),
    ]
