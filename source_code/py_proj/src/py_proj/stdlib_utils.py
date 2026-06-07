"""Module 09: Standard Library Utilities (09-标准库常用能力)

Knowledge points covered:
- pathlib/shutil/tempfile (paths, file operations, temp files)
- json/csv/configparser/tomllib (data serialization)
- datetime/time/zoneinfo (dates, timestamps, timezones)
- argparse (CLI argument parsing)
- logging (leveled logging, formatters, handlers)
- collections/heapq/bisect (Counter, deque, heap, binary search)
- itertools (efficient combinatorial iteration)
- threading/multiprocessing/concurrent.futures/asyncio (concurrency models)
- standard-library-first philosophy
- pathlib.Path over string path concatenation
"""

from __future__ import annotations

import bisect
import collections
import datetime
import heapq
import itertools
import json
import logging
import time
import zoneinfo
from collections import Counter, deque
from pathlib import Path, PurePosixPath
from typing import Any


# ---------------------------------------------------------------------------
# pathlib — cross-platform path handling
# ---------------------------------------------------------------------------

def pathlib_demo() -> dict[str, Any]:
    """Demonstrate pathlib.Path usage."""
    p = PurePosixPath("/home/user/documents/report.txt")
    return {
        "parent": str(p.parent),
        "name": p.name,
        "stem": p.stem,
        "suffix": p.suffix,
        "parts": list(p.parts),
        "is_absolute": p.is_absolute(),
        "with_suffix": str(p.with_suffix(".csv")),
        "joinpath": str(Path.home() / "documents"),
    }


# ---------------------------------------------------------------------------
# datetime / time / zoneinfo
# ---------------------------------------------------------------------------

def datetime_demo() -> dict[str, Any]:
    """Demonstrate datetime module usage."""
    now = datetime.datetime.now()
    utc_now = datetime.datetime.now(datetime.timezone.utc)
    specific = datetime.datetime(2025, 1, 15, 10, 30, 0)
    delta = now - specific

    return {
        "now": now.isoformat(),
        "utc_now": utc_now.isoformat(),
        "specific": specific.isoformat(),
        "delta_days": delta.days,
        "weekday": now.strftime("%A"),
        "formatted": now.strftime("%Y-%m-%d %H:%M:%S"),
    }


def time_demo() -> dict[str, float]:
    """Demonstrate time module for performance measurement."""
    start = time.perf_counter()
    # simulate work
    total = sum(range(10000))
    elapsed = time.perf_counter() - start
    return {
        "perf_counter": elapsed,
        "time_time": time.time(),
        "total": float(total),
    }


def timezone_demo() -> dict[str, str]:
    """Demonstrate zoneinfo for timezone handling (Python 3.9+)."""
    try:
        utc = zoneinfo.ZoneInfo("UTC")
        shanghai = zoneinfo.ZoneInfo("Asia/Shanghai")
        available = str(len(zoneinfo.available_timezones()))
    except zoneinfo.ZoneInfoNotFoundError:
        utc = datetime.timezone.utc
        shanghai = datetime.timezone(datetime.timedelta(hours=8), "Asia/Shanghai")
        available = "system tzdata unavailable"
    now_utc = datetime.datetime.now(utc)
    now_shanghai = now_utc.astimezone(shanghai)
    return {
        "utc": now_utc.isoformat(),
        "shanghai": now_shanghai.isoformat(),
        "available": available,
    }


# ---------------------------------------------------------------------------
# collections — Counter, deque, defaultdict, namedtuple
# ---------------------------------------------------------------------------

def counter_demo(items: list[str]) -> dict[str, int]:
    """Counter: count occurrences of elements."""
    return dict(Counter(items))


def deque_demo() -> dict[str, list[int]]:
    """deque: O(1) append/pop from both ends."""
    d: deque[int] = deque([1, 2, 3])
    d.appendleft(0)
    d.append(4)
    left = d.popleft()
    right = d.pop()
    return {"remaining": list(d), "popped_left": [left], "popped_right": [right]}


def deque_maxlen_demo() -> list[int]:
    """deque with maxlen: automatically drops oldest items."""
    d: deque[int] = deque(maxlen=3)
    for i in range(5):
        d.append(i)
    return list(d)  # [2, 3, 4]


def defaultdict_demo(pairs: list[tuple[str, int]]) -> dict[str, list[int]]:
    """defaultdict: auto-creates default values for missing keys."""
    dd: dict[str, list[int]] = collections.defaultdict(list)
    for key, value in pairs:
        dd[key].append(value)
    return dict(dd)


# ---------------------------------------------------------------------------
# heapq — heap / priority queue
# ---------------------------------------------------------------------------

def heap_demo(data: list[int]) -> dict[str, Any]:
    """heapq: min-heap operations."""
    heap = data.copy()
    heapq.heapify(heap)
    smallest = heapq.heappop(heap)
    heapq.heappush(heap, 0)
    return {
        "smallest": smallest,
        "three_largest": heapq.nlargest(3, data),
        "three_smallest": heapq.nsmallest(3, data),
    }


# ---------------------------------------------------------------------------
# bisect — binary search
# ---------------------------------------------------------------------------

def bisect_demo(sorted_list: list[int], target: int) -> dict[str, int]:
    """bisect: binary search in sorted lists."""
    return {
        "insert_left": bisect.bisect_left(sorted_list, target),
        "insert_right": bisect.bisect_right(sorted_list, target),
        "insort": sorted_list.copy(),  # would be modified by insort_left
    }


# ---------------------------------------------------------------------------
# itertools
# ---------------------------------------------------------------------------

def itertools_demo() -> dict[str, Any]:
    """Demonstrate key itertools functions."""
    return {
        "chain": list(itertools.chain([1, 2], [3, 4])),
        "product": list(itertools.product([1, 2], ["a", "b"])),
        "combinations": list(itertools.combinations([1, 2, 3], 2)),
        "permutations": list(itertools.permutations([1, 2, 3], 2)),
        "groupby": {k: list(v) for k, v in itertools.groupby("aaabbc")},
        "islice": list(itertools.islice(range(10), 2, 7, 2)),
        "accumulate": list(itertools.accumulate([1, 2, 3, 4])),
        "zip_longest": list(itertools.zip_longest([1, 2], ["a", "b", "c"])),
    }


# ---------------------------------------------------------------------------
# logging
# ---------------------------------------------------------------------------

def setup_logger(name: str, level: int = logging.INFO) -> logging.Logger:
    """Set up a logger with formatter and handler."""
    logger = logging.getLogger(name)
    if not logger.handlers:
        handler = logging.StreamHandler()
        formatter = logging.Formatter(
            "%(asctime)s %(name)s %(levelname)s %(message)s"
        )
        handler.setFormatter(formatter)
        logger.addHandler(handler)
    logger.setLevel(level)
    return logger


def logging_demo() -> dict[str, str]:
    """Demonstrate logging levels and formatting."""
    logger = setup_logger("demo_logger", logging.DEBUG)
    import io
    stream = io.StringIO()
    handler = logging.StreamHandler(stream)
    handler.setFormatter(logging.Formatter("%(levelname)s:%(message)s"))
    logger.addHandler(handler)

    logger.debug("Debug message")
    logger.info("Info message")
    logger.warning("Warning message")
    logger.error("Error message")
    logger.critical("Critical message")

    return {"log_output": stream.getvalue()}


# ---------------------------------------------------------------------------
# JSON serialization
# ---------------------------------------------------------------------------

def json_demo(data: Any) -> dict[str, Any]:
    """Demonstrate JSON serialization with various options."""
    return {
        "compact": json.dumps(data),
        "pretty": json.dumps(data, indent=2, sort_keys=True),
        "ensure_ascii": json.dumps({"中文": "值"}, ensure_ascii=False),
        "roundtrip": json.loads(json.dumps(data)),
    }


# ---------------------------------------------------------------------------
# Standard-library-first philosophy
# ---------------------------------------------------------------------------

def stdlib_first_examples() -> dict[str, str]:
    """Prefer standard library solutions when available."""
    return {
        "paths": "Use pathlib.Path, not os.path string manipulation",
        "json": "Use json module, not manual string formatting",
        "csv": "Use csv module, not manual split/join",
        "datetime": "Use datetime/zoneinfo, not time.strftime",
        "collections": "Use Counter/defaultdict/deque, not manual dict/list",
        "heapq": "Use heapq.nlargest, not sorted()[:n]",
        "itertools": "Use itertools.chain, not manual loops to concatenate",
        "logging": "Use logging module, not print()",
        "argparse": "Use argparse, not manual sys.argv parsing",
        "tempfile": "Use tempfile, not hardcoded temp paths",
    }
