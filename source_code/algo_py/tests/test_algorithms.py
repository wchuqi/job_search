from __future__ import annotations

import random
import unittest

from algo_py.algorithms import (
    ComplexityClass,
    FenwickTree,
    Interval,
    LRUCache,
    ListNode,
    MedianFinder,
    MinStack,
    RandomizedSet,
    SegmentTree,
    StructureChoice,
    TreeNode,
    Trie,
    UnionFind,
    adjacency_list,
    analyze_two_sum,
    apply_range_adds,
    bfs_distances,
    binary_search_closed,
    can_finish_courses,
    can_jump,
    can_partition,
    choose_structure,
    climb_stairs,
    coin_change,
    combination_sum,
    connected_components,
    count_primes,
    count_subsets_with_mask,
    daily_temperatures,
    dijkstra,
    erase_overlap_intervals,
    fisher_yates,
    fits_input_size,
    frequency_count,
    gcd,
    hamming_weight,
    has_complete_explanation,
    has_cycle,
    java21_template_awareness,
    kth_largest,
    largest_rectangle_area,
    length_of_lis,
    level_order,
    list_to_array,
    longest_common_subsequence,
    longest_substring_without_repeat,
    lower_bound,
    lowest_common_ancestor,
    matrix_chain_min_cost,
    max_depth,
    max_subarray,
    merge_intervals,
    merge_two_lists,
    min_eating_speed,
    min_path_sum,
    min_subarray_len_positive,
    minimum_spanning_tree_weight,
    move_zeroes,
    num_islands,
    permute,
    prefix_sums,
    python_templates_demo,
    range_sum,
    remove_duplicates_sorted,
    review,
    reverse_list,
    rob_tree,
    safe_middle,
    shortest_path_in_grid,
    single_number,
    sliding_window_max,
    subarray_sum_equals_k,
    subarrays_div_by_k,
    three_sum,
    top_k_frequent,
    two_sum,
    valid_parentheses,
    weighted_pick,
)
from algo_py.knowledge_coverage import Coverage, CoverageTopic


class AlgoPyTest(unittest.TestCase):
    @classmethod
    def setUpClass(cls) -> None:
        # 每次测试类开始前清空知识点覆盖记录。
        Coverage.reset()

    @classmethod
    def tearDownClass(cls) -> None:
        # 所有测试结束后统一检查是否有文档知识点没有被命中。
        missed = Coverage.missed_topics()
        if missed:
            names = ", ".join(sorted(topic.name for topic in missed))
            raise AssertionError(f"Knowledge coverage missed topics: {names}")
        print(f"Knowledge coverage: 100% ({len(CoverageTopic)} topics).")

    def test_complexity_and_templates(self) -> None:
        # 复杂度、结构选择、模板意识和溢出安全中点。
        self.assertTrue(fits_input_size(100_000, ComplexityClass.LINEAR))
        self.assertFalse(fits_input_size(100_000, ComplexityClass.QUADRATIC))
        self.assertEqual({1: 2, 2: 1}, frequency_count([1, 2, 1]))
        self.assertEqual(StructureChoice.ARRAY, choose_structure("random-access"))
        self.assertEqual(StructureChoice.HASH_MAP, choose_structure("key-lookup"))
        self.assertEqual(StructureChoice.DEQUE, choose_structure("fifo-lifo"))
        self.assertEqual(StructureChoice.HEAP, choose_structure("top-k"))
        self.assertEqual(StructureChoice.GRAPH_ADJACENCY_LIST, choose_structure("graph-walk"))
        self.assertEqual(1_500_000_000, safe_middle(1_000_000_000, 2_000_000_000))
        self.assertIn("deque", python_templates_demo())
        self.assertIn("PriorityQueue", java21_template_awareness())

    def test_arrays_strings_hash_prefix_diff(self) -> None:
        # 数组/字符串/哈希/前缀和/差分的典型场景和边界。
        self.assertEqual([0, 1], two_sum([2, 7, 11, 15], 9))
        self.assertEqual([], two_sum([1, 2], 9))
        self.assertEqual(3, longest_substring_without_repeat("abcabcbb"))
        self.assertEqual({(-1, -1, 2), (-1, 0, 1)}, {tuple(x) for x in three_sum([-1, 0, 1, 2, -1, -4])})
        zeroes = [0, 1, 0, 3, 12]
        move_zeroes(zeroes)
        self.assertEqual([1, 3, 12, 0, 0], zeroes)
        duplicates = [1, 1, 2, 2, 3]
        self.assertEqual(3, remove_duplicates_sorted(duplicates))
        self.assertEqual(0, remove_duplicates_sorted([]))
        self.assertEqual(2, min_subarray_len_positive(7, [2, 3, 1, 2, 4, 3]))
        prefix = prefix_sums([2, -1, 3])
        self.assertEqual([0, 2, 1, 4], prefix)
        self.assertEqual(4, range_sum(prefix, 0, 2))
        self.assertEqual(2, subarray_sum_equals_k([1, 1, 1], 2))
        self.assertEqual([2, 5, 5, 3, 0], apply_range_adds(5, [(0, 2, 2), (1, 3, 3)]))
        self.assertEqual(7, subarrays_div_by_k([4, 5, 0, -2, -3, 1], 5))

    def test_stack_queue_heap_and_binary_search(self) -> None:
        # 栈、堆、单调结构、二分、二分答案和选择算法。
        self.assertTrue(valid_parentheses("([]){}"))
        self.assertFalse(valid_parentheses("([)]"))
        self.assertEqual([1, 2], sorted(top_k_frequent([1, 1, 1, 2, 2, 3], 2)))
        self.assertEqual([1, 1, 4, 2, 1, 1, 0, 0], daily_temperatures([73, 74, 75, 71, 69, 72, 76, 73]))
        self.assertEqual([3, 3, 5, 5, 6, 7], sliding_window_max([1, 3, -1, -3, 5, 3, 6, 7], 3))
        self.assertEqual(10, largest_rectangle_area([2, 1, 5, 6, 2, 3]))
        min_stack = MinStack()
        min_stack.push(3)
        min_stack.push(1)
        min_stack.push(2)
        self.assertEqual(1, min_stack.min())
        self.assertEqual(2, min_stack.pop())
        median = MedianFinder()
        median.add_num(1)
        median.add_num(2)
        self.assertEqual(1.5, median.find_median())
        median.add_num(3)
        self.assertEqual(2.0, median.find_median())
        self.assertEqual(3, binary_search_closed([1, 3, 5, 7, 9], 7))
        self.assertEqual(-1, binary_search_closed([1, 3], 2))
        self.assertEqual(1, lower_bound([1, 3, 3, 5], 3))
        self.assertEqual(4, min_eating_speed([3, 6, 7, 11], 8))
        self.assertEqual(5, kth_largest([3, 2, 1, 5, 6, 4], 2))
        self.assertEqual([Interval(1, 6), Interval(8, 10)], merge_intervals([Interval(1, 3), Interval(2, 6), Interval(8, 10)]))
        self.assertEqual([], merge_intervals([]))

    def test_linked_tree_graph_search(self) -> None:
        # 链表、树、图、DFS/BFS、回溯和网格最短路。
        one = ListNode(1, ListNode(2, ListNode(3)))
        self.assertEqual([3, 2, 1], list_to_array(reverse_list(one)))
        a = ListNode(1, ListNode(3))
        b = ListNode(2, ListNode(4))
        self.assertEqual([1, 2, 3, 4], list_to_array(merge_two_lists(a, b)))
        cycle = ListNode(1)
        cycle.next = ListNode(2, cycle)
        self.assertTrue(has_cycle(cycle))
        self.assertFalse(has_cycle(ListNode(1)))
        root = TreeNode(3, TreeNode(9), TreeNode(20, TreeNode(15), TreeNode(7)))
        self.assertEqual([[3], [9, 20], [15, 7]], level_order(root))
        self.assertEqual([], level_order(None))
        self.assertEqual(3, max_depth(root))
        self.assertIs(root, lowest_common_ancestor(root, root.left, root.right.right))
        graph = adjacency_list(4, [(0, 1), (1, 2)])
        self.assertEqual([0, 1, 2, -1], bfs_distances(graph, 0))
        self.assertEqual(2, connected_components(graph))
        island_grid = [["1", "1", "0"], ["0", "1", "0"], ["1", "0", "1"]]
        self.assertEqual(3, num_islands(island_grid))
        self.assertEqual(6, len(permute([1, 2, 3])))
        self.assertEqual([[2, 2, 3], [7]], combination_sum([2, 3, 6, 7], 7))
        self.assertEqual(4, shortest_path_in_grid([[0, 0, 0], [1, 1, 0], [0, 0, 0]]))
        self.assertEqual(-1, shortest_path_in_grid([[1]]))

    def test_dynamic_programming_greedy_and_graphs(self) -> None:
        # DP、贪心、并查集、拓扑排序、最短路和最小生成树。
        self.assertEqual(8, climb_stairs(5))
        self.assertEqual(6, max_subarray([-2, 1, -3, 4, -1, 2, 1, -5, 4]))
        self.assertEqual(3, coin_change([1, 2, 5], 11))
        self.assertEqual(-1, coin_change([2], 3))
        self.assertEqual(4, length_of_lis([10, 9, 2, 5, 3, 7, 101, 18]))
        self.assertEqual(3, longest_common_subsequence("abcde", "ace"))
        self.assertTrue(can_partition([1, 5, 11, 5]))
        self.assertFalse(can_partition([1, 2, 3, 5]))
        self.assertEqual(7, min_path_sum([[1, 3, 1], [1, 5, 1], [4, 2, 1]]))
        self.assertEqual(18_000, matrix_chain_min_cost([10, 20, 30, 40]))
        tree = TreeNode(3, TreeNode(2, None, TreeNode(3)), TreeNode(3, None, TreeNode(1)))
        self.assertEqual(7, rob_tree(tree))
        self.assertEqual(8, count_subsets_with_mask([1, 2, 3]))
        self.assertTrue(can_jump([2, 3, 1, 1, 4]))
        self.assertFalse(can_jump([3, 2, 1, 0, 4]))
        self.assertEqual(1, erase_overlap_intervals([Interval(1, 2), Interval(2, 3), Interval(1, 3)]))
        uf = UnionFind(3)
        self.assertTrue(uf.union(0, 1))
        self.assertFalse(uf.union(0, 1))
        self.assertEqual(uf.find(0), uf.find(1))
        self.assertTrue(can_finish_courses(2, [(0, 1)]))
        self.assertFalse(can_finish_courses(2, [(0, 1), (1, 0)]))
        self.assertEqual([0, 2, 3], dijkstra(3, [(0, 1, 2), (1, 2, 1), (0, 2, 5)], 0))
        self.assertEqual(3, minimum_spanning_tree_weight(3, [(0, 1, 1), (1, 2, 2), (0, 2, 5)]))

    def test_bit_math_random_and_advanced_structures(self) -> None:
        # 位运算、数学、随机化和高级数据结构。
        self.assertEqual(4, single_number([2, 2, 1, 1, 4]))
        self.assertEqual(3, hamming_weight(0b1011))
        self.assertEqual(6, gcd(54, 24))
        self.assertEqual(4, count_primes(10))
        shuffled = fisher_yates([1, 2, 3], random.Random(1))
        self.assertEqual([1, 2, 3], sorted(shuffled))
        self.assertEqual(1, weighted_pick([1, 3, 6], 2))
        trie = Trie()
        trie.insert("algo")
        self.assertTrue(trie.search("algo"))
        self.assertFalse(trie.search("alg"))
        self.assertTrue(trie.starts_with("alg"))
        fenwick = FenwickTree(5)
        fenwick.add(0, 1)
        fenwick.add(3, 4)
        self.assertEqual(5, fenwick.prefix_sum(3))
        seg = SegmentTree([1, 2, 3, 4])
        self.assertEqual(5, seg.query(1, 2))
        lru = LRUCache(2)
        lru.put(1, 1)
        lru.put(2, 2)
        self.assertEqual(1, lru.get(1))
        lru.put(3, 3)
        self.assertEqual(-1, lru.get(2))
        randomized = RandomizedSet()
        self.assertTrue(randomized.insert(10))
        self.assertFalse(randomized.insert(10))
        self.assertTrue(randomized.insert(20))
        self.assertEqual(10, randomized.get_by_ticket(0))
        self.assertTrue(randomized.remove(10))
        self.assertFalse(randomized.remove(30))

    def test_interview_workflow_and_coverage(self) -> None:
        # 面试表达、错题复盘和覆盖主题数量检查。
        analysis = analyze_two_sum()
        self.assertTrue(has_complete_explanation(analysis))
        record = review("boundary missed", "sliding window", "window has no duplicate chars", ["empty", "single", "duplicate"])
        self.assertEqual("boundary missed", record.failure_reason)
        self.assertEqual(("empty", "single", "duplicate"), record.edge_cases)
        # 明确断言枚举数量，避免后续新增知识点却忘了补测试。
        self.assertEqual(51, len(CoverageTopic))


if __name__ == "__main__":
    unittest.main()
