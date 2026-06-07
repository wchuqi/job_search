"""Knowledge-point coverage tracker for the algorithm project."""

from __future__ import annotations

from enum import Enum, auto


class CoverageTopic(Enum):
    """从 `算法` 文档抽取出的 51 个知识点主题。"""

    PROBLEM_SOLVING_FLOW = auto()
    COMPLEXITY_ANALYSIS = auto()
    DATA_STRUCTURE_CHOICE = auto()
    JAVA21_TEMPLATES = auto()
    PYTHON_TEMPLATES = auto()
    ARRAYS_STRINGS = auto()
    TWO_POINTERS = auto()
    SLIDING_WINDOW = auto()
    HASH_TABLE = auto()
    PREFIX_SUM = auto()
    DIFFERENCE_ARRAY = auto()
    STACK = auto()
    QUEUE_DEQUE = auto()
    MONOTONIC_STRUCTURE = auto()
    HEAP = auto()
    BINARY_SEARCH = auto()
    BINARY_ANSWER = auto()
    SORTING = auto()
    QUICK_SELECT = auto()
    LINKED_LIST = auto()
    TREE_TRAVERSAL = auto()
    GRAPH_TRAVERSAL = auto()
    DFS = auto()
    BFS = auto()
    BACKTRACKING = auto()
    DYNAMIC_PROGRAMMING = auto()
    ONE_DIMENSION_DP = auto()
    TWO_DIMENSION_DP = auto()
    KNAPSACK_DP = auto()
    SUBSEQUENCE_DP = auto()
    INTERVAL_DP = auto()
    TREE_DP = auto()
    STATE_COMPRESSION = auto()
    GREEDY = auto()
    UNION_FIND = auto()
    TOPOLOGICAL_SORT = auto()
    SHORTEST_PATH = auto()
    MINIMUM_SPANNING_TREE = auto()
    BIT_OPERATION = auto()
    MATH = auto()
    RANDOMIZATION = auto()
    TRIE = auto()
    FENWICK_TREE = auto()
    SEGMENT_TREE = auto()
    LRU_CACHE = auto()
    RANDOMIZED_SET = auto()
    LEETCODE_CLASSICS = auto()
    INTERVIEW_EXPLANATION = auto()
    REVIEW_AND_ACCEPTANCE = auto()
    EDGE_CASES = auto()
    OVERFLOW_HANDLING = auto()


class Coverage:
    """简单的进程内知识点覆盖追踪器。"""

    _hit: set[CoverageTopic] = set()

    @classmethod
    def hit(cls, topic: CoverageTopic) -> None:
        """标记某个知识点已被源码执行或测试命中。"""
        cls._hit.add(topic)

    @classmethod
    def reset(cls) -> None:
        """每次测试运行前清空历史记录。"""
        cls._hit.clear()

    @classmethod
    def missed_topics(cls) -> set[CoverageTopic]:
        """返回当前测试运行中尚未命中的主题。"""
        return set(CoverageTopic) - cls._hit
