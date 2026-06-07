"""Tests for stdlib_utils module (09-标准库常用能力)."""

import pytest
from py_proj.stdlib_utils import (
    bisect_demo,
    counter_demo,
    datetime_demo,
    defaultdict_demo,
    deque_demo,
    deque_maxlen_demo,
    heap_demo,
    itertools_demo,
    json_demo,
    logging_demo,
    pathlib_demo,
    stdlib_first_examples,
    time_demo,
    timezone_demo,
)


class TestPathlib:
    def test_pathlib_demo(self):
        result = pathlib_demo()
        assert result["parent"] == "/home/user/documents"
        assert result["name"] == "report.txt"
        assert result["stem"] == "report"
        assert result["suffix"] == ".txt"
        assert result["is_absolute"] is True

    def test_with_suffix(self):
        result = pathlib_demo()
        assert result["with_suffix"] == "/home/user/documents/report.csv"


class TestDatetime:
    def test_datetime_demo(self):
        result = datetime_demo()
        assert "T" in result["now"]
        assert "T" in result["utc_now"]
        assert result["delta_days"] >= 0
        assert len(result["weekday"]) > 0

    def test_time_demo(self):
        result = time_demo()
        assert result["perf_counter"] > 0
        assert result["time_time"] > 0

    def test_timezone_demo(self):
        result = timezone_demo()
        assert "T" in result["utc"]
        assert "T" in result["shanghai"]

    def test_timezone_zoneinfo_available_path(self, monkeypatch):
        import datetime

        class FakeZoneInfo(datetime.tzinfo):
            def __init__(self, name: str):
                self.name = name

            def utcoffset(self, dt):
                return datetime.timedelta(hours=8 if self.name == "Asia/Shanghai" else 0)

            def dst(self, dt):
                return datetime.timedelta(0)

        monkeypatch.setattr("py_proj.stdlib_utils.zoneinfo.ZoneInfo", FakeZoneInfo)
        monkeypatch.setattr("py_proj.stdlib_utils.zoneinfo.available_timezones", lambda: {"UTC", "Asia/Shanghai"})
        result = timezone_demo()
        assert result["available"] == "2"


class TestCollections:
    def test_counter(self):
        result = counter_demo(["a", "b", "a", "c", "a", "b"])
        assert result["a"] == 3
        assert result["b"] == 2
        assert result["c"] == 1

    def test_deque(self):
        result = deque_demo()
        assert result["popped_left"] == [0]
        assert result["popped_right"] == [4]
        assert result["remaining"] == [1, 2, 3]

    def test_deque_maxlen(self):
        result = deque_maxlen_demo()
        assert result == [2, 3, 4]

    def test_defaultdict(self):
        pairs = [("a", 1), ("b", 2), ("a", 3)]
        result = defaultdict_demo(pairs)
        assert result["a"] == [1, 3]
        assert result["b"] == [2]


class TestHeapq:
    def test_heap(self):
        result = heap_demo([5, 3, 1, 4, 2])
        assert result["smallest"] == 1
        assert result["three_largest"] == [5, 4, 3]
        assert result["three_smallest"] == [1, 2, 3]


class TestBisect:
    def test_bisect(self):
        result = bisect_demo([1, 3, 5, 7, 9], 5)
        assert result["insert_left"] == 2
        assert result["insert_right"] == 3


class TestItertools:
    def test_chain(self):
        result = itertools_demo()
        assert result["chain"] == [1, 2, 3, 4]

    def test_product(self):
        result = itertools_demo()
        assert len(result["product"]) == 4

    def test_combinations(self):
        result = itertools_demo()
        assert len(result["combinations"]) == 3

    def test_groupby(self):
        result = itertools_demo()
        assert result["groupby"]["a"] == ["a", "a", "a"]

    def test_accumulate(self):
        result = itertools_demo()
        assert result["accumulate"] == [1, 3, 6, 10]


class TestLogging:
    def test_logging_demo(self):
        result = logging_demo()
        assert "INFO" in result["log_output"]
        assert "WARNING" in result["log_output"]
        assert "ERROR" in result["log_output"]


class TestJSON:
    def test_json_demo(self):
        data = {"key": "value", "num": 42}
        result = json_demo(data)
        assert '"key"' in result["compact"]
        assert result["roundtrip"] == data


class TestStdlibFirst:
    def test_examples(self):
        result = stdlib_first_examples()
        assert "pathlib" in result["paths"]
        assert "argparse" in result
