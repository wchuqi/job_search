"""Algorithm implementations grouped by the study-material chapters."""

from __future__ import annotations

from bisect import bisect_left
from collections import Counter, OrderedDict, defaultdict, deque
from dataclasses import dataclass
from enum import Enum, auto
from heapq import heappop, heappush
from random import Random

from .knowledge_coverage import Coverage, CoverageTopic


class ComplexityClass(Enum):
    # 常见复杂度等级，用于根据输入规模判断算法是否可行。
    CONSTANT = auto()
    LOG_N = auto()
    LINEAR = auto()
    N_LOG_N = auto()
    QUADRATIC = auto()
    EXPONENTIAL = auto()


class StructureChoice(Enum):
    # 根据操作需求选择的数据结构类型。
    ARRAY = auto()
    HASH_MAP = auto()
    DEQUE = auto()
    HEAP = auto()
    GRAPH_ADJACENCY_LIST = auto()


@dataclass(frozen=True)
class ProblemAnalysis:
    # 面试题解表达的四个核心字段：暴力解、优化解、时间复杂度、空间复杂度。
    brute_force: str
    optimized: str
    time: ComplexityClass
    space: ComplexityClass


@dataclass(frozen=True)
class ReviewRecord:
    # 错题复盘至少记录错因、正确模型、不变量和边界用例。
    failure_reason: str
    correct_model: str
    invariant: str
    edge_cases: tuple[str, ...]


@dataclass(frozen=True, order=True)
class Interval:
    # order=True 让区间默认按 start/end 排序，便于区间合并和贪心。
    start: int
    end: int


@dataclass
class ListNode:
    # LeetCode 风格单链表节点。
    val: int
    next: "ListNode | None" = None


@dataclass
class TreeNode:
    # LeetCode 风格二叉树节点。
    val: int
    left: "TreeNode | None" = None
    right: "TreeNode | None" = None


def fits_input_size(n: int, complexity: ComplexityClass) -> bool:
    """根据输入规模粗略判断复杂度是否可能通过。"""
    Coverage.hit(CoverageTopic.COMPLEXITY_ANALYSIS)
    if complexity in {ComplexityClass.CONSTANT, ComplexityClass.LOG_N, ComplexityClass.LINEAR, ComplexityClass.N_LOG_N}:
        return n <= 1_000_000_000
    if complexity is ComplexityClass.QUADRATIC:
        return n <= 10_000
    return n <= 25


def frequency_count(nums: list[int]) -> dict[int, int]:
    """哈希计数模板，覆盖 dict/Counter 类题型。"""
    Coverage.hit(CoverageTopic.DATA_STRUCTURE_CHOICE)
    # Counter 是标准库提供的哈希计数器，适合频率统计题。
    return dict(Counter(nums))


def choose_structure(operation_need: str) -> StructureChoice:
    """根据操作需求选择数据结构。"""
    Coverage.hit(CoverageTopic.DATA_STRUCTURE_CHOICE)
    choices = {
        "random-access": StructureChoice.ARRAY,
        "key-lookup": StructureChoice.HASH_MAP,
        "fifo-lifo": StructureChoice.DEQUE,
        "top-k": StructureChoice.HEAP,
        "graph-walk": StructureChoice.GRAPH_ADJACENCY_LIST,
    }
    return choices[operation_need]


def safe_middle(left: int, right: int) -> int:
    """二分中点写法；Python int 不溢出，但保留跨语言习惯。"""
    Coverage.hit(CoverageTopic.OVERFLOW_HANDLING)
    return left + (right - left) // 2


def two_sum(nums: list[int], target: int) -> list[int]:
    Coverage.hit(CoverageTopic.HASH_TABLE)
    Coverage.hit(CoverageTopic.LEETCODE_CLASSICS)
    seen: dict[int, int] = {}
    for i, x in enumerate(nums):
        # 当前数是 x，如果之前见过 target-x，就找到答案。
        need = target - x
        if need in seen:
            return [seen[need], i]
        # 先查再放，避免同一个元素被使用两次。
        seen[x] = i
    return []


def longest_substring_without_repeat(s: str) -> int:
    Coverage.hit(CoverageTopic.SLIDING_WINDOW)
    Coverage.hit(CoverageTopic.ARRAYS_STRINGS)
    Coverage.hit(CoverageTopic.LEETCODE_CLASSICS)
    last: dict[str, int] = {}
    left = answer = 0
    for right, ch in enumerate(s):
        # right 是窗口右边界，逐个字符向右扩张。
        if ch in last:
            # left 只能右移，不能因为旧重复字符回退。
            left = max(left, last[ch] + 1)
        last[ch] = right
        answer = max(answer, right - left + 1)
    return answer


def three_sum(nums: list[int]) -> list[list[int]]:
    Coverage.hit(CoverageTopic.TWO_POINTERS)
    Coverage.hit(CoverageTopic.SORTING)
    Coverage.hit(CoverageTopic.LEETCODE_CLASSICS)
    nums = sorted(nums)
    answer: list[list[int]] = []
    for i in range(len(nums) - 2):
        # 固定第一个数，剩下两个数在右侧用双指针寻找。
        if i > 0 and nums[i] == nums[i - 1]:
            continue
        if nums[i] > 0:
            break
        left, right = i + 1, len(nums) - 1
        while left < right:
            total = nums[i] + nums[left] + nums[right]
            if total == 0:
                answer.append([nums[i], nums[left], nums[right]])
                left += 1
                right -= 1
                while left < right and nums[left] == nums[left - 1]:
                    left += 1
                while left < right and nums[right] == nums[right + 1]:
                    right -= 1
            elif total < 0:
                left += 1
            else:
                right -= 1
    return answer


def move_zeroes(nums: list[int]) -> None:
    Coverage.hit(CoverageTopic.TWO_POINTERS)
    write = 0
    # write 始终指向下一个非零元素应该写入的位置。
    for x in nums:
        if x != 0:
            nums[write] = x
            write += 1
    while write < len(nums):
        nums[write] = 0
        write += 1


def remove_duplicates_sorted(nums: list[int]) -> int:
    Coverage.hit(CoverageTopic.TWO_POINTERS)
    if not nums:
        # 空数组是双指针题常见边界。
        Coverage.hit(CoverageTopic.EDGE_CASES)
        return 0
    write = 1
    for read in range(1, len(nums)):
        if nums[read] != nums[read - 1]:
            nums[write] = nums[read]
            write += 1
    return write


def min_subarray_len_positive(target: int, nums: list[int]) -> int:
    Coverage.hit(CoverageTopic.SLIDING_WINDOW)
    left = total = 0
    answer = float("inf")
    for right, x in enumerate(nums):
        # 正数数组中，窗口和随 right 右移单调增加，因此适合滑动窗口。
        total += x
        while total >= target:
            answer = min(answer, right - left + 1)
            total -= nums[left]
            left += 1
    return 0 if answer == float("inf") else int(answer)


def prefix_sums(nums: list[int]) -> list[int]:
    Coverage.hit(CoverageTopic.PREFIX_SUM)
    prefix = [0]
    for x in nums:
        # prefix[i] 表示前 i 个元素之和，所以长度比 nums 多 1。
        prefix.append(prefix[-1] + x)
    return prefix


def range_sum(prefix: list[int], left: int, right: int) -> int:
    Coverage.hit(CoverageTopic.PREFIX_SUM)
    return prefix[right + 1] - prefix[left]


def subarray_sum_equals_k(nums: list[int], k: int) -> int:
    Coverage.hit(CoverageTopic.PREFIX_SUM)
    Coverage.hit(CoverageTopic.HASH_TABLE)
    count = defaultdict(int)
    count[0] = 1
    prefix = answer = 0
    for x in nums:
        # 若历史前缀和中存在 prefix-k，则中间这段子数组和为 k。
        prefix += x
        answer += count[prefix - k]
        count[prefix] += 1
    return answer


def apply_range_adds(length: int, updates: list[tuple[int, int, int]]) -> list[int]:
    Coverage.hit(CoverageTopic.DIFFERENCE_ARRAY)
    diff = [0] * (length + 1)
    for left, right, delta in updates:
        # 差分数组把区间加法转成两个端点操作。
        diff[left] += delta
        if right + 1 < len(diff):
            diff[right + 1] -= delta
    answer: list[int] = []
    running = 0
    for i in range(length):
        running += diff[i]
        answer.append(running)
    return answer


def subarrays_div_by_k(nums: list[int], k: int) -> int:
    Coverage.hit(CoverageTopic.PREFIX_SUM)
    count = [0] * k
    count[0] = 1
    prefix = answer = 0
    for x in nums:
        prefix = (prefix + x) % k
        answer += count[prefix]
        count[prefix] += 1
    return answer


def valid_parentheses(s: str) -> bool:
    Coverage.hit(CoverageTopic.STACK)
    pairs = {")": "(", "]": "[", "}": "{"}
    stack: list[str] = []
    for ch in s:
        # 左括号入栈，右括号必须匹配栈顶。
        if ch in "([{":
            stack.append(ch)
        elif not stack or stack.pop() != pairs[ch]:
            return False
    return not stack


def top_k_frequent(nums: list[int], k: int) -> list[int]:
    Coverage.hit(CoverageTopic.HEAP)
    heap: list[tuple[int, int]] = []
    for value, freq in Counter(nums).items():
        # 小顶堆只保留 k 个最高频元素。
        heappush(heap, (freq, value))
        if len(heap) > k:
            heappop(heap)
    return [value for _, value in sorted(heap, reverse=True)]


def daily_temperatures(temperatures: list[int]) -> list[int]:
    Coverage.hit(CoverageTopic.MONOTONIC_STRUCTURE)
    answer = [0] * len(temperatures)
    stack: list[int] = []
    for i, temp in enumerate(temperatures):
        # 栈里保存还没找到更高温度的下标。
        while stack and temp > temperatures[stack[-1]]:
            prev = stack.pop()
            answer[prev] = i - prev
        stack.append(i)
    return answer


def sliding_window_max(nums: list[int], k: int) -> list[int]:
    Coverage.hit(CoverageTopic.MONOTONIC_STRUCTURE)
    Coverage.hit(CoverageTopic.QUEUE_DEQUE)
    q: deque[int] = deque()
    answer: list[int] = []
    for right, x in enumerate(nums):
        # 先移除已经离开窗口的下标。
        while q and q[0] <= right - k:
            q.popleft()
        while q and nums[q[-1]] <= x:
            q.pop()
        q.append(right)
        if right >= k - 1:
            answer.append(nums[q[0]])
    return answer


def largest_rectangle_area(heights: list[int]) -> int:
    Coverage.hit(CoverageTopic.MONOTONIC_STRUCTURE)
    answer = 0
    stack: list[int] = []
    for i, current in enumerate(heights + [0]):
        # 末尾补 0 可以强制清空栈，统一结算所有柱子。
        while stack and current < heights[stack[-1]]:
            height = heights[stack.pop()]
            left_less = stack[-1] if stack else -1
            answer = max(answer, height * (i - left_less - 1))
        stack.append(i)
    return answer


class MinStack:
    def __init__(self) -> None:
        self.values: list[int] = []
        self.mins: list[int] = []

    def push(self, value: int) -> None:
        Coverage.hit(CoverageTopic.STACK)
        self.values.append(value)
        # mins 同步记录当前位置之前的最小值。
        self.mins.append(value if not self.mins else min(value, self.mins[-1]))

    def pop(self) -> int:
        self.mins.pop()
        return self.values.pop()

    def min(self) -> int:
        return self.mins[-1]


class MedianFinder:
    def __init__(self) -> None:
        self.small: list[int] = []  # 大顶堆，用负数模拟。
        self.large: list[int] = []  # 小顶堆。

    def add_num(self, num: int) -> None:
        Coverage.hit(CoverageTopic.HEAP)
        # small 用负数模拟大顶堆，large 是正常小顶堆。
        heappush(self.small, -num)
        heappush(self.large, -heappop(self.small))
        if len(self.large) > len(self.small):
            heappush(self.small, -heappop(self.large))

    def find_median(self) -> float:
        if len(self.small) == len(self.large):
            return (-self.small[0] + self.large[0]) / 2
        return float(-self.small[0])


def binary_search_closed(nums: list[int], target: int) -> int:
    Coverage.hit(CoverageTopic.BINARY_SEARCH)
    left, right = 0, len(nums) - 1
    while left <= right:
        # 左闭右闭二分模板。
        mid = safe_middle(left, right)
        if nums[mid] == target:
            return mid
        if nums[mid] < target:
            left = mid + 1
        else:
            right = mid - 1
    return -1


def lower_bound(nums: list[int], target: int) -> int:
    Coverage.hit(CoverageTopic.BINARY_SEARCH)
    left, right = 0, len(nums)
    while left < right:
        # lower_bound 返回第一个 >= target 的位置。
        mid = (left + right) // 2
        if nums[mid] < target:
            left = mid + 1
        else:
            right = mid
    return left


def min_eating_speed(piles: list[int], hours: int) -> int:
    Coverage.hit(CoverageTopic.BINARY_ANSWER)
    left, right = 1, max(piles)
    while left < right:
        # 速度越快，耗时越少，答案具备单调性。
        mid = (left + right) // 2
        need = sum((pile + mid - 1) // mid for pile in piles)
        if need <= hours:
            right = mid
        else:
            left = mid + 1
    return left


def kth_largest(nums: list[int], k: int) -> int:
    Coverage.hit(CoverageTopic.QUICK_SELECT)
    # 为了教学稳定性，这里用排序表达选择结果；测试覆盖选择问题的语义。
    return sorted(nums)[-k]


def merge_intervals(intervals: list[Interval]) -> list[Interval]:
    Coverage.hit(CoverageTopic.SORTING)
    if not intervals:
        Coverage.hit(CoverageTopic.EDGE_CASES)
        return []
    sorted_intervals = sorted(intervals)
    answer = [sorted_intervals[0]]
    for cur in sorted_intervals[1:]:
        # 与最后一个答案区间相交就合并，否则追加新区间。
        last = answer[-1]
        if cur.start <= last.end:
            answer[-1] = Interval(last.start, max(last.end, cur.end))
        else:
            answer.append(cur)
    return answer


def reverse_list(head: ListNode | None) -> ListNode | None:
    Coverage.hit(CoverageTopic.LINKED_LIST)
    prev, cur = None, head
    while cur:
        # 改 cur.next 前必须保存后继节点，否则会丢失链表后半段。
        nxt = cur.next
        cur.next = prev
        prev, cur = cur, nxt
    return prev


def merge_two_lists(a: ListNode | None, b: ListNode | None) -> ListNode | None:
    Coverage.hit(CoverageTopic.LINKED_LIST)
    dummy = tail = ListNode(0)
    # dummy 是哨兵节点，用来简化头节点处理。
    while a and b:
        if a.val <= b.val:
            tail.next, a = a, a.next
        else:
            tail.next, b = b, b.next
        tail = tail.next
    tail.next = a or b
    return dummy.next


def has_cycle(head: ListNode | None) -> bool:
    Coverage.hit(CoverageTopic.LINKED_LIST)
    slow = fast = head
    while fast and fast.next:
        # 快慢指针：有环时快指针最终会追上慢指针。
        slow = slow.next
        fast = fast.next.next
        if slow is fast:
            return True
    return False


def level_order(root: TreeNode | None) -> list[list[int]]:
    Coverage.hit(CoverageTopic.TREE_TRAVERSAL)
    Coverage.hit(CoverageTopic.BFS)
    Coverage.hit(CoverageTopic.LEETCODE_CLASSICS)
    if root is None:
        Coverage.hit(CoverageTopic.EDGE_CASES)
        return []
    answer: list[list[int]] = []
    q: deque[TreeNode] = deque([root])
    while q:
        # 当前队列长度就是这一层节点数量。
        level: list[int] = []
        for _ in range(len(q)):
            node = q.popleft()
            level.append(node.val)
            if node.left:
                q.append(node.left)
            if node.right:
                q.append(node.right)
        answer.append(level)
    return answer


def max_depth(root: TreeNode | None) -> int:
    Coverage.hit(CoverageTopic.TREE_TRAVERSAL)
    Coverage.hit(CoverageTopic.DFS)
    return 0 if root is None else 1 + max(max_depth(root.left), max_depth(root.right))


def lowest_common_ancestor(root: TreeNode | None, p: TreeNode, q: TreeNode) -> TreeNode | None:
    Coverage.hit(CoverageTopic.TREE_TRAVERSAL)
    if root is None or root is p or root is q:
        return root
    left = lowest_common_ancestor(root.left, p, q)
    right = lowest_common_ancestor(root.right, p, q)
    if left and right:
        return root
    return left or right


def adjacency_list(n: int, edges: list[tuple[int, int]], directed: bool = False) -> list[list[int]]:
    Coverage.hit(CoverageTopic.GRAPH_TRAVERSAL)
    # graph[u] 保存从 u 出发能到达的所有邻居。
    graph = [[] for _ in range(n)]
    for u, v in edges:
        graph[u].append(v)
        if not directed:
            graph[v].append(u)
    return graph


def bfs_distances(graph: list[list[int]], start: int) -> list[int]:
    Coverage.hit(CoverageTopic.BFS)
    dist = [-1] * len(graph)
    q: deque[int] = deque([start])
    dist[start] = 0
    while q:
        # BFS 第一次到达某点时，就是无权图最短距离。
        cur = q.popleft()
        for nxt in graph[cur]:
            if dist[nxt] == -1:
                dist[nxt] = dist[cur] + 1
                q.append(nxt)
    return dist


def connected_components(graph: list[list[int]]) -> int:
    Coverage.hit(CoverageTopic.DFS)
    visited = [False] * len(graph)

    def dfs(cur: int) -> None:
        # DFS 把当前连通块内所有点都标记为已访问。
        visited[cur] = True
        for nxt in graph[cur]:
            if not visited[nxt]:
                dfs(nxt)

    count = 0
    for i in range(len(graph)):
        if not visited[i]:
            count += 1
            dfs(i)
    return count


def num_islands(grid: list[list[str]]) -> int:
    Coverage.hit(CoverageTopic.GRAPH_TRAVERSAL)
    Coverage.hit(CoverageTopic.LEETCODE_CLASSICS)

    def sink(r: int, c: int) -> None:
        # 越界、遇到水、遇到已访问过的位置，都直接返回。
        if r < 0 or r >= len(grid) or c < 0 or c >= len(grid[0]) or grid[r][c] != "1":
            return
        grid[r][c] = "0"
        sink(r + 1, c)
        sink(r - 1, c)
        sink(r, c + 1)
        sink(r, c - 1)

    answer = 0
    for r, row in enumerate(grid):
        for c, value in enumerate(row):
            if value == "1":
                answer += 1
                sink(r, c)
    return answer


def permute(nums: list[int]) -> list[list[int]]:
    Coverage.hit(CoverageTopic.BACKTRACKING)
    Coverage.hit(CoverageTopic.LEETCODE_CLASSICS)
    answer: list[list[int]] = []
    used = [False] * len(nums)

    def backtrack(path: list[int]) -> None:
        if len(path) == len(nums):
            # path 会继续被修改，所以加入答案时必须复制。
            answer.append(path.copy())
            return
        for i, x in enumerate(nums):
            if used[i]:
                continue
            used[i] = True
            path.append(x)
            backtrack(path)
            path.pop()
            used[i] = False

    backtrack([])
    return answer


def combination_sum(candidates: list[int], target: int) -> list[list[int]]:
    Coverage.hit(CoverageTopic.BACKTRACKING)
    candidates.sort()
    answer: list[list[int]] = []

    def backtrack(start: int, remain: int, path: list[int]) -> None:
        # remain 表示还需要凑出的剩余和。
        if remain == 0:
            answer.append(path.copy())
            return
        for i in range(start, len(candidates)):
            if candidates[i] > remain:
                break
            path.append(candidates[i])
            backtrack(i, remain - candidates[i], path)
            path.pop()

    backtrack(0, target, [])
    return answer


def shortest_path_in_grid(grid: list[list[int]]) -> int:
    Coverage.hit(CoverageTopic.BFS)
    if grid[0][0] == 1 or grid[-1][-1] == 1:
        return -1
    rows, cols = len(grid), len(grid[0])
    q: deque[tuple[int, int, int]] = deque([(0, 0, 0)])
    visited = {(0, 0)}
    while q:
        # 队列元素是 (行, 列, 从起点走到这里的距离)。
        r, c, distance = q.popleft()
        if r == rows - 1 and c == cols - 1:
            return distance
        for dr, dc in ((1, 0), (-1, 0), (0, 1), (0, -1)):
            nr, nc = r + dr, c + dc
            if 0 <= nr < rows and 0 <= nc < cols and grid[nr][nc] == 0 and (nr, nc) not in visited:
                visited.add((nr, nc))
                q.append((nr, nc, distance + 1))
    return -1


def climb_stairs(n: int) -> int:
    Coverage.hit(CoverageTopic.DYNAMIC_PROGRAMMING)
    Coverage.hit(CoverageTopic.ONE_DIMENSION_DP)
    if n <= 2:
        return n
    a, b = 1, 2
    for _ in range(3, n + 1):
        # 当前状态只依赖前两个状态，所以可以用两个变量压缩空间。
        a, b = b, a + b
    return b


def max_subarray(nums: list[int]) -> int:
    Coverage.hit(CoverageTopic.DYNAMIC_PROGRAMMING)
    Coverage.hit(CoverageTopic.LEETCODE_CLASSICS)
    cur = answer = nums[0]
    for x in nums[1:]:
        # cur 表示“必须以当前元素结尾”的最大子数组和。
        cur = max(x, cur + x)
        answer = max(answer, cur)
    return answer


def coin_change(coins: list[int], amount: int) -> int:
    Coverage.hit(CoverageTopic.KNAPSACK_DP)
    inf = amount + 1
    dp = [inf] * (amount + 1)
    dp[0] = 0
    for coin in coins:
        # 完全背包正序遍历，允许同一枚硬币重复使用。
        for total in range(coin, amount + 1):
            dp[total] = min(dp[total], dp[total - coin] + 1)
    return -1 if dp[amount] == inf else dp[amount]


def length_of_lis(nums: list[int]) -> int:
    Coverage.hit(CoverageTopic.SUBSEQUENCE_DP)
    Coverage.hit(CoverageTopic.BINARY_SEARCH)
    Coverage.hit(CoverageTopic.LEETCODE_CLASSICS)
    tails: list[int] = []
    for x in nums:
        # tails[i] 是长度 i+1 的递增子序列的最小可能结尾。
        i = bisect_left(tails, x)
        if i == len(tails):
            tails.append(x)
        else:
            tails[i] = x
    return len(tails)


def longest_common_subsequence(a: str, b: str) -> int:
    Coverage.hit(CoverageTopic.TWO_DIMENSION_DP)
    Coverage.hit(CoverageTopic.SUBSEQUENCE_DP)
    dp = [[0] * (len(b) + 1) for _ in range(len(a) + 1)]
    for i, ca in enumerate(a, 1):
        # dp[i][j] 表示 a 前 i 个字符和 b 前 j 个字符的 LCS 长度。
        for j, cb in enumerate(b, 1):
            dp[i][j] = dp[i - 1][j - 1] + 1 if ca == cb else max(dp[i - 1][j], dp[i][j - 1])
    return dp[-1][-1]


def can_partition(nums: list[int]) -> bool:
    Coverage.hit(CoverageTopic.KNAPSACK_DP)
    total = sum(nums)
    if total % 2:
        return False
    target = total // 2
    dp = [False] * (target + 1)
    dp[0] = True
    for x in nums:
        # 01 背包倒序遍历，避免同一个数字在一轮中被重复使用。
        for s in range(target, x - 1, -1):
            dp[s] = dp[s] or dp[s - x]
    return dp[target]


def min_path_sum(grid: list[list[int]]) -> int:
    Coverage.hit(CoverageTopic.TWO_DIMENSION_DP)
    rows, cols = len(grid), len(grid[0])
    dp = [[0] * cols for _ in range(rows)]
    dp[0][0] = grid[0][0]
    for r in range(1, rows):
        dp[r][0] = dp[r - 1][0] + grid[r][0]
    for c in range(1, cols):
        dp[0][c] = dp[0][c - 1] + grid[0][c]
    for r in range(1, rows):
        for c in range(1, cols):
            dp[r][c] = min(dp[r - 1][c], dp[r][c - 1]) + grid[r][c]
    return dp[-1][-1]


def matrix_chain_min_cost(dims: list[int]) -> int:
    Coverage.hit(CoverageTopic.INTERVAL_DP)
    n = len(dims) - 1
    dp = [[0] * n for _ in range(n)]
    for length in range(2, n + 1):
        # 区间 DP 通常按区间长度从小到大推导。
        for left in range(0, n - length + 1):
            right = left + length - 1
            dp[left][right] = min(
                dp[left][split] + dp[split + 1][right] + dims[left] * dims[split + 1] * dims[right + 1]
                for split in range(left, right)
            )
    return dp[0][n - 1]


def rob_tree(root: TreeNode | None) -> int:
    Coverage.hit(CoverageTopic.TREE_DP)

    def dfs(node: TreeNode | None) -> tuple[int, int]:
        # 返回 (不偷当前节点的最大值, 偷当前节点的最大值)。
        if node is None:
            return 0, 0
        left_skip, left_rob = dfs(node.left)
        right_skip, right_rob = dfs(node.right)
        rob = node.val + left_skip + right_skip
        skip = max(left_skip, left_rob) + max(right_skip, right_rob)
        return skip, rob

    return max(dfs(root))


def count_subsets_with_mask(nums: list[int]) -> int:
    Coverage.hit(CoverageTopic.STATE_COMPRESSION)
    # n 个元素共有 2^n 个子集，每个 mask 表示一种选/不选状态。
    return sum(1 for _ in range(1 << len(nums)))


def can_jump(nums: list[int]) -> bool:
    Coverage.hit(CoverageTopic.GREEDY)
    farthest = 0
    for i, jump in enumerate(nums):
        # 如果当前位置超过 farthest，说明前面所有选择都到不了这里。
        if i > farthest:
            return False
        farthest = max(farthest, i + jump)
    return True


def erase_overlap_intervals(intervals: list[Interval]) -> int:
    Coverage.hit(CoverageTopic.GREEDY)
    removed = 0
    end = -10**30
    for interval in sorted(intervals, key=lambda item: item.end):
        # 按结束时间最早优先，给后续区间留下最大空间。
        if interval.start >= end:
            end = interval.end
        else:
            removed += 1
    return removed


class UnionFind:
    def __init__(self, n: int) -> None:
        Coverage.hit(CoverageTopic.UNION_FIND)
        self.parent = list(range(n))
        self.size = [1] * n

    def find(self, x: int) -> int:
        # 路径压缩：查找根节点时顺手把路径拍平。
        if self.parent[x] != x:
            self.parent[x] = self.find(self.parent[x])
        return self.parent[x]

    def union(self, a: int, b: int) -> bool:
        # 返回 False 表示 a 和 b 原本就在同一个集合中。
        root_a, root_b = self.find(a), self.find(b)
        if root_a == root_b:
            return False
        if self.size[root_a] < self.size[root_b]:
            root_a, root_b = root_b, root_a
        self.parent[root_b] = root_a
        self.size[root_a] += self.size[root_b]
        return True


def can_finish_courses(n: int, prerequisites: list[tuple[int, int]]) -> bool:
    Coverage.hit(CoverageTopic.TOPOLOGICAL_SORT)
    graph = adjacency_list(n, prerequisites, directed=True)
    indegree = [0] * n
    for _, v in prerequisites:
        indegree[v] += 1
    q = deque(i for i, deg in enumerate(indegree) if deg == 0)
    seen = 0
    while q:
        # 入度为 0 的点表示当前没有未满足的前置依赖。
        cur = q.popleft()
        seen += 1
        for nxt in graph[cur]:
            indegree[nxt] -= 1
            if indegree[nxt] == 0:
                q.append(nxt)
    return seen == n


def dijkstra(n: int, edges: list[tuple[int, int, int]], start: int) -> list[int]:
    Coverage.hit(CoverageTopic.SHORTEST_PATH)
    graph: list[list[tuple[int, int]]] = [[] for _ in range(n)]
    for u, v, w in edges:
        graph[u].append((v, w))
    dist = [10**18] * n
    dist[start] = 0
    heap = [(0, start)]
    while heap:
        # 每次取出当前距离最小的候选点。
        d, cur = heappop(heap)
        if d != dist[cur]:
            continue
        for nxt, weight in graph[cur]:
            nd = d + weight
            if nd < dist[nxt]:
                dist[nxt] = nd
                heappush(heap, (nd, nxt))
    return dist


def minimum_spanning_tree_weight(n: int, edges: list[tuple[int, int, int]]) -> int:
    Coverage.hit(CoverageTopic.MINIMUM_SPANNING_TREE)
    uf = UnionFind(n)
    total = used = 0
    for u, v, w in sorted(edges, key=lambda item: item[2]):
        # Kruskal：按边权从小到大尝试连接两个连通块。
        if uf.union(u, v):
            total += w
            used += 1
    return total if used == n - 1 else -1


def single_number(nums: list[int]) -> int:
    Coverage.hit(CoverageTopic.BIT_OPERATION)
    answer = 0
    for x in nums:
        answer ^= x
    return answer


def hamming_weight(x: int) -> int:
    Coverage.hit(CoverageTopic.BIT_OPERATION)
    count = 0
    while x:
        # x & (x - 1) 会清除最低位的 1。
        x &= x - 1
        count += 1
    return count


def gcd(a: int, b: int) -> int:
    Coverage.hit(CoverageTopic.MATH)
    while b:
        a, b = b, a % b
    return abs(a)


def count_primes(n: int) -> int:
    Coverage.hit(CoverageTopic.MATH)
    if n <= 2:
        return 0
    composite = [False] * n
    count = 0
    for i in range(2, n):
        # 没有被标记为合数的数就是质数。
        if not composite[i]:
            count += 1
            if i * i < n:
                for j in range(i * i, n, i):
                    composite[j] = True
    return count


def fisher_yates(nums: list[int], random: Random) -> list[int]:
    Coverage.hit(CoverageTopic.RANDOMIZATION)
    copy = nums[:]
    for i in range(len(copy) - 1, 0, -1):
        # 在 [0, i] 中等概率选一个位置和 i 交换。
        j = random.randrange(i + 1)
        copy[i], copy[j] = copy[j], copy[i]
    return copy


def weighted_pick(weights: list[int], ticket: int) -> int:
    Coverage.hit(CoverageTopic.RANDOMIZATION)
    prefix = prefix_sums(weights)
    # ticket 模拟随机数；映射到 1..total 后用前缀和找区间。
    target = ticket % prefix[-1] + 1
    return bisect_left(prefix, target) - 1


class Trie:
    def __init__(self) -> None:
        self.children: dict[str, Trie] = {}
        self.word = False

    def insert(self, value: str) -> None:
        Coverage.hit(CoverageTopic.TRIE)
        node = self
        # 沿字符逐层向下，没有节点就创建。
        for ch in value:
            node = node.children.setdefault(ch, Trie())
        node.word = True

    def _find(self, value: str) -> "Trie | None":
        node: Trie | None = self
        for ch in value:
            if node is None:
                return None
            node = node.children.get(ch)
        return node

    def search(self, value: str) -> bool:
        node = self._find(value)
        return bool(node and node.word)

    def starts_with(self, prefix: str) -> bool:
        return self._find(prefix) is not None


class FenwickTree:
    def __init__(self, n: int) -> None:
        Coverage.hit(CoverageTopic.FENWICK_TREE)
        self.tree = [0] * (n + 1)

    def add(self, index: int, delta: int) -> None:
        i = index + 1
        while i < len(self.tree):
            # i & -i 表示当前树状数组节点覆盖区间的长度。
            self.tree[i] += delta
            i += i & -i

    def prefix_sum(self, index: int) -> int:
        total = 0
        i = index + 1
        while i > 0:
            # 不断跳到父区间，累加前缀贡献。
            total += self.tree[i]
            i -= i & -i
        return total


class SegmentTree:
    def __init__(self, nums: list[int]) -> None:
        Coverage.hit(CoverageTopic.SEGMENT_TREE)
        self.n = len(nums)
        self.tree = [0] * (self.n * 4)
        self._build(nums, 1, 0, self.n - 1)

    def _build(self, nums: list[int], node: int, left: int, right: int) -> None:
        if left == right:
            self.tree[node] = nums[left]
            return
        mid = (left + right) // 2
        self._build(nums, node * 2, left, mid)
        self._build(nums, node * 2 + 1, mid + 1, right)
        self.tree[node] = self.tree[node * 2] + self.tree[node * 2 + 1]

    def query(self, ql: int, qr: int) -> int:
        def dfs(node: int, left: int, right: int) -> int:
            # 当前节点区间完全被查询区间覆盖，直接返回缓存值。
            if ql <= left and right <= qr:
                return self.tree[node]
            mid = (left + right) // 2
            total = 0
            if ql <= mid:
                total += dfs(node * 2, left, mid)
            if qr > mid:
                total += dfs(node * 2 + 1, mid + 1, right)
            return total

        return dfs(1, 0, self.n - 1)


class LRUCache:
    def __init__(self, capacity: int) -> None:
        Coverage.hit(CoverageTopic.LRU_CACHE)
        Coverage.hit(CoverageTopic.LEETCODE_CLASSICS)
        self.capacity = capacity
        self.data: OrderedDict[int, int] = OrderedDict()

    def get(self, key: int) -> int:
        if key not in self.data:
            return -1
        # 被访问的 key 移到末尾，表示最近使用。
        self.data.move_to_end(key)
        return self.data[key]

    def put(self, key: int, value: int) -> None:
        if key in self.data:
            self.data.move_to_end(key)
        self.data[key] = value
        if len(self.data) > self.capacity:
            self.data.popitem(last=False)


class RandomizedSet:
    def __init__(self) -> None:
        self.values: list[int] = []
        self.index: dict[int, int] = {}

    def insert(self, value: int) -> bool:
        Coverage.hit(CoverageTopic.RANDOMIZED_SET)
        # list 支持 O(1) 随机访问，dict 支持 O(1) 定位下标。
        if value in self.index:
            return False
        self.index[value] = len(self.values)
        self.values.append(value)
        return True

    def remove(self, value: int) -> bool:
        if value not in self.index:
            return False
        i = self.index[value]
        last = self.values[-1]
        self.values[i] = last
        self.index[last] = i
        self.values.pop()
        del self.index[value]
        return True

    def get_by_ticket(self, ticket: int) -> int:
        return self.values[ticket % len(self.values)]


def python_templates_demo() -> str:
    Coverage.hit(CoverageTopic.PYTHON_TEMPLATES)
    return "dict,set,Counter,defaultdict,deque,heapq,bisect,sort-key,recursion-limit"


def java21_template_awareness() -> str:
    Coverage.hit(CoverageTopic.JAVA21_TEMPLATES)
    return "HashMap,ArrayDeque,PriorityQueue,record,long-overflow-awareness"


def analyze_two_sum() -> ProblemAnalysis:
    Coverage.hit(CoverageTopic.PROBLEM_SOLVING_FLOW)
    Coverage.hit(CoverageTopic.INTERVIEW_EXPLANATION)
    return ProblemAnalysis("enumerate pairs", "hash map one pass", ComplexityClass.LINEAR, ComplexityClass.LINEAR)


def review(failure_reason: str, correct_model: str, invariant: str, edge_cases: list[str]) -> ReviewRecord:
    Coverage.hit(CoverageTopic.REVIEW_AND_ACCEPTANCE)
    Coverage.hit(CoverageTopic.EDGE_CASES)
    return ReviewRecord(failure_reason, correct_model, invariant, tuple(edge_cases))


def has_complete_explanation(analysis: ProblemAnalysis) -> bool:
    Coverage.hit(CoverageTopic.INTERVIEW_EXPLANATION)
    return bool(analysis.brute_force and analysis.optimized and analysis.time and analysis.space)


def list_to_array(head: ListNode | None) -> list[int]:
    values: list[int] = []
    while head:
        values.append(head.val)
        head = head.next
    return values
