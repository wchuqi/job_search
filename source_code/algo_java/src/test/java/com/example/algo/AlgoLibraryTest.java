package com.example.algo;

import com.example.algo.AlgoLibrary.AdvancedStructures;
import com.example.algo.AlgoLibrary.ArraysStrings;
import com.example.algo.AlgoLibrary.BacktrackingSearch;
import com.example.algo.AlgoLibrary.BinarySortSelect;
import com.example.algo.AlgoLibrary.BitMathRandomized;
import com.example.algo.AlgoLibrary.ComplexityBasics;
import com.example.algo.AlgoLibrary.DynamicProgramming;
import com.example.algo.AlgoLibrary.GreedyGraph;
import com.example.algo.AlgoLibrary.HashPrefixDiff;
import com.example.algo.AlgoLibrary.Interval;
import com.example.algo.AlgoLibrary.Java21Templates;
import com.example.algo.AlgoLibrary.LinkedTreeGraph;
import com.example.algo.AlgoLibrary.ListNode;
import com.example.algo.AlgoLibrary.StackQueueHeap;
import com.example.algo.AlgoLibrary.StructureChoice;
import com.example.algo.AlgoLibrary.TreeNode;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Random;

final class AlgoLibraryTest {
    private AlgoLibraryTest() {
    }

    static void runAll() {
        // 每个私有方法是一组测试场景，按学习文档章节顺序组织。
        complexityAndTemplates();
        arraysStringsHashPrefixDiff();
        stackQueueHeapAndBinarySearch();
        linkedTreeGraphSearch();
        dynamicProgrammingGreedyAndGraphs();
        bitMathRandomAndAdvancedStructures();
        interviewWorkflowAndCoverage();
    }

    private static void complexityAndTemplates() {
        // 数据规模测试：线性复杂度能处理大输入，平方复杂度不能处理 10^5。
        Assertions.isTrue(ComplexityBasics.fitsInputSize(100_000, AlgoLibrary.ComplexityClass.LINEAR));
        Assertions.isFalse(ComplexityBasics.fitsInputSize(100_000, AlgoLibrary.ComplexityClass.QUADRATIC));
        Assertions.equals(Map.of(1, 2, 2, 1), ComplexityBasics.frequencyCount(new int[]{1, 2, 1}));
        Assertions.equals(StructureChoice.ARRAY, ComplexityBasics.chooseStructure("random-access"));
        Assertions.equals(StructureChoice.HASH_MAP, ComplexityBasics.chooseStructure("key-lookup"));
        Assertions.equals(StructureChoice.ARRAY_DEQUE, ComplexityBasics.chooseStructure("fifo-lifo"));
        Assertions.equals(StructureChoice.PRIORITY_QUEUE, ComplexityBasics.chooseStructure("top-k"));
        Assertions.equals(StructureChoice.GRAPH_ADJACENCY_LIST, ComplexityBasics.chooseStructure("graph-walk"));
        Assertions.equals(1_500_000_000, ComplexityBasics.safeMiddle(1_000_000_000, 2_000_000_000));

        // Java 21 模板测试：邻接表、区间排序、Python 易错点提示。
        List<List<Integer>> graph = Java21Templates.adjacencyTemplate(3, new int[][]{{0, 1}, {1, 2}});
        Assertions.equals(List.of(1), graph.get(0));
        List<int[]> intervals = new ArrayList<>(List.of(new int[]{3, 4}, new int[]{1, 2}));
        Java21Templates.sortIntervalsTemplate(intervals);
        Assertions.equals(1, intervals.getFirst()[0]);
        Assertions.isTrue(Java21Templates.pythonPitfallReminder().contains("recursion"));
    }

    private static void arraysStringsHashPrefixDiff() {
        // 两数之和：验证正常命中和找不到答案两种情况。
        Assertions.arrayEquals(new int[]{0, 1}, ArraysStrings.twoSum(new int[]{2, 7, 11, 15}, 9));
        Assertions.arrayEquals(new int[0], ArraysStrings.twoSum(new int[]{1, 2}, 9));
        Assertions.equals(3, ArraysStrings.longestSubstringWithoutRepeat("abcabcbb"));
        Assertions.isTrue(AlgoLibrary.sameTripletsIgnoreOrder(
                List.of(List.of(-1, -1, 2), List.of(-1, 0, 1)),
                ArraysStrings.threeSum(new int[]{-1, 0, 1, 2, -1, -4})));
        int[] zeroes = {0, 1, 0, 3, 12};
        ArraysStrings.moveZeroes(zeroes);
        Assertions.arrayEquals(new int[]{1, 3, 12, 0, 0}, zeroes);
        int[] duplicates = {1, 1, 2, 2, 3};
        Assertions.equals(3, ArraysStrings.removeDuplicatesSorted(duplicates));
        Assertions.equals(0, ArraysStrings.removeDuplicatesSorted(new int[0]));
        Assertions.equals(2, ArraysStrings.minSubArrayLenPositive(7, new int[]{2, 3, 1, 2, 4, 3}));

        // 前缀和和差分：验证区间查询、子数组计数、区间批量加法。
        int[] prefix = HashPrefixDiff.prefixSums(new int[]{2, -1, 3});
        Assertions.arrayEquals(new int[]{0, 2, 1, 4}, prefix);
        Assertions.equals(4, HashPrefixDiff.rangeSum(prefix, 0, 2));
        Assertions.equals(2, HashPrefixDiff.subarraySumEqualsK(new int[]{1, 1, 1}, 2));
        Assertions.arrayEquals(new int[]{2, 5, 5, 3, 0}, HashPrefixDiff.applyRangeAdds(5, new int[][]{{0, 2, 2}, {1, 3, 3}}));
        Assertions.equals(7, HashPrefixDiff.subarraysDivByK(new int[]{4, 5, 0, -2, -3, 1}, 5));
    }

    private static void stackQueueHeapAndBinarySearch() {
        // 栈和堆类测试：括号匹配、Top K、单调栈、单调队列、最小栈、中位数。
        Assertions.isTrue(StackQueueHeap.validParentheses("([]){}"));
        Assertions.isFalse(StackQueueHeap.validParentheses("([)]"));
        int[] top = StackQueueHeap.topKFrequent(new int[]{1, 1, 1, 2, 2, 3}, 2);
        Arrays.sort(top);
        Assertions.arrayEquals(new int[]{1, 2}, top);
        Assertions.arrayEquals(new int[]{1, 1, 4, 2, 1, 1, 0, 0}, StackQueueHeap.dailyTemperatures(new int[]{73, 74, 75, 71, 69, 72, 76, 73}));
        Assertions.arrayEquals(new int[]{3, 3, 5, 5, 6, 7}, StackQueueHeap.slidingWindowMax(new int[]{1, 3, -1, -3, 5, 3, 6, 7}, 3));
        Assertions.equals(10, StackQueueHeap.largestRectangleArea(new int[]{2, 1, 5, 6, 2, 3}));
        StackQueueHeap.MinStack minStack = new StackQueueHeap.MinStack();
        minStack.push(3);
        minStack.push(1);
        minStack.push(2);
        Assertions.equals(1, minStack.min());
        Assertions.equals(2, minStack.pop());
        StackQueueHeap.MedianFinder medianFinder = new StackQueueHeap.MedianFinder();
        medianFinder.addNum(1);
        medianFinder.addNum(2);
        Assertions.equals(1.5, medianFinder.findMedian());
        medianFinder.addNum(3);
        Assertions.equals(2.0, medianFinder.findMedian());

        // 二分和排序选择：普通二分、lower_bound、二分答案、快速选择、区间合并。
        Assertions.equals(3, BinarySortSelect.binarySearchClosed(new int[]{1, 3, 5, 7, 9}, 7));
        Assertions.equals(-1, BinarySortSelect.binarySearchClosed(new int[]{1, 3}, 2));
        Assertions.equals(1, BinarySortSelect.lowerBound(new int[]{1, 3, 3, 5}, 3));
        Assertions.equals(4, BinarySortSelect.minEatingSpeed(new int[]{3, 6, 7, 11}, 8));
        Assertions.equals(5, BinarySortSelect.kthLargest(new int[]{3, 2, 1, 5, 6, 4}, 2));
        Assertions.equals(List.of(new Interval(1, 6), new Interval(8, 10)), BinarySortSelect.mergeIntervals(List.of(new Interval(1, 3), new Interval(2, 6), new Interval(8, 10))));
        Assertions.equals(List.of(), BinarySortSelect.mergeIntervals(List.of()));
    }

    private static void linkedTreeGraphSearch() {
        // 链表测试：反转、合并、环检测。
        ListNode one = new ListNode(1);
        one.next = new ListNode(2);
        one.next.next = new ListNode(3);
        Assertions.arrayEquals(new int[]{3, 2, 1}, AlgoLibrary.listToArray(LinkedTreeGraph.reverseList(one)));

        ListNode a = new ListNode(1);
        a.next = new ListNode(3);
        ListNode b = new ListNode(2);
        b.next = new ListNode(4);
        Assertions.arrayEquals(new int[]{1, 2, 3, 4}, AlgoLibrary.listToArray(LinkedTreeGraph.mergeTwoLists(a, b)));

        ListNode cycle = new ListNode(1);
        cycle.next = new ListNode(2);
        cycle.next.next = cycle;
        Assertions.isTrue(LinkedTreeGraph.hasCycle(cycle));
        Assertions.isFalse(LinkedTreeGraph.hasCycle(new ListNode(1)));

        // 树测试：层序遍历、空树边界、最大深度、最近公共祖先。
        TreeNode root = AlgoLibrary.node(3,
                AlgoLibrary.node(9, null, null),
                AlgoLibrary.node(20, new TreeNode(15), new TreeNode(7)));
        Assertions.matrixEquals(List.of(List.of(3), List.of(9, 20), List.of(15, 7)), LinkedTreeGraph.levelOrder(root));
        Assertions.equals(List.of(), LinkedTreeGraph.levelOrder(null));
        Assertions.equals(3, LinkedTreeGraph.maxDepth(root));
        Assertions.equals(root, LinkedTreeGraph.lowestCommonAncestor(root, root.left, root.right.right));

        // 图测试：邻接表、BFS 最短距离、DFS 连通块、岛屿数量。
        List<List<Integer>> graph = LinkedTreeGraph.adjacencyList(4, new int[][]{{0, 1}, {1, 2}}, false);
        Assertions.arrayEquals(new int[]{0, 1, 2, -1}, LinkedTreeGraph.bfsDistances(graph, 0));
        Assertions.equals(2, LinkedTreeGraph.connectedComponents(graph));
        char[][] islandGrid = {
                {'1', '1', '0'},
                {'0', '1', '0'},
                {'1', '0', '1'}
        };
        Assertions.equals(3, LinkedTreeGraph.numIslands(islandGrid));

        // 回溯和网格 BFS：排列、组合总和、最短路可达/不可达。
        Assertions.equals(6, BacktrackingSearch.permute(new int[]{1, 2, 3}).size());
        Assertions.equals(List.of(List.of(2, 2, 3), List.of(7)), BacktrackingSearch.combinationSum(new int[]{2, 3, 6, 7}, 7));
        Assertions.equals(4, BacktrackingSearch.shortestPathInGrid(new int[][]{{0, 0, 0}, {1, 1, 0}, {0, 0, 0}}));
        Assertions.equals(-1, BacktrackingSearch.shortestPathInGrid(new int[][]{{1}}));
    }

    private static void dynamicProgrammingGreedyAndGraphs() {
        // DP 测试：一维、Kadane、完全背包、LIS、LCS、01 背包、二维路径。
        Assertions.equals(8, DynamicProgramming.climbStairs(5));
        Assertions.equals(6, DynamicProgramming.maxSubArray(new int[]{-2, 1, -3, 4, -1, 2, 1, -5, 4}));
        Assertions.equals(3, DynamicProgramming.coinChange(new int[]{1, 2, 5}, 11));
        Assertions.equals(-1, DynamicProgramming.coinChange(new int[]{2}, 3));
        Assertions.equals(4, DynamicProgramming.lengthOfLIS(new int[]{10, 9, 2, 5, 3, 7, 101, 18}));
        Assertions.equals(3, DynamicProgramming.longestCommonSubsequence("abcde", "ace"));
        Assertions.isTrue(DynamicProgramming.canPartition(new int[]{1, 5, 11, 5}));
        Assertions.isFalse(DynamicProgramming.canPartition(new int[]{1, 2, 3, 5}));
        Assertions.equals(7, DynamicProgramming.minPathSum(new int[][]{{1, 3, 1}, {1, 5, 1}, {4, 2, 1}}));
        Assertions.equals(18_000, DynamicProgramming.matrixChainMinCost(new int[]{10, 20, 30, 40}));
        TreeNode tree = AlgoLibrary.node(3, AlgoLibrary.node(2, null, new TreeNode(3)), AlgoLibrary.node(3, null, new TreeNode(1)));
        Assertions.equals(7, DynamicProgramming.robTree(tree));
        Assertions.equals(8, DynamicProgramming.countSubsetsWithMask(new int[]{1, 2, 3}));

        // 贪心和图算法：跳跃游戏、区间删除、并查集、拓扑、最短路、MST。
        Assertions.isTrue(GreedyGraph.canJump(new int[]{2, 3, 1, 1, 4}));
        Assertions.isFalse(GreedyGraph.canJump(new int[]{3, 2, 1, 0, 4}));
        Assertions.equals(1, GreedyGraph.eraseOverlapIntervals(List.of(new Interval(1, 2), new Interval(2, 3), new Interval(1, 3))));
        GreedyGraph.UnionFind uf = new GreedyGraph.UnionFind(3);
        Assertions.isTrue(uf.union(0, 1));
        Assertions.isFalse(uf.union(0, 1));
        Assertions.equals(uf.find(0), uf.find(1));
        Assertions.isTrue(GreedyGraph.canFinishCourses(2, new int[][]{{0, 1}}));
        Assertions.isFalse(GreedyGraph.canFinishCourses(2, new int[][]{{0, 1}, {1, 0}}));
        Assertions.arrayEquals(new int[]{0, 2, 3}, GreedyGraph.dijkstra(3, new int[][]{{0, 1, 2}, {1, 2, 1}, {0, 2, 5}}, 0));
        Assertions.equals(3, GreedyGraph.minimumSpanningTreeWeight(3, new int[][]{{0, 1, 1}, {1, 2, 2}, {0, 2, 5}}));
    }

    private static void bitMathRandomAndAdvancedStructures() {
        // 位运算、数学和随机化：异或、位计数、GCD、筛质数、洗牌、权重选择。
        Assertions.equals(4, BitMathRandomized.singleNumber(new int[]{2, 2, 1, 1, 4}));
        Assertions.equals(3, BitMathRandomized.hammingWeight(0b1011));
        Assertions.equals(6, BitMathRandomized.gcd(54, 24));
        Assertions.equals(4, BitMathRandomized.countPrimes(10));
        int[] shuffled = BitMathRandomized.fisherYates(new int[]{1, 2, 3}, new Random(1));
        Arrays.sort(shuffled);
        Assertions.arrayEquals(new int[]{1, 2, 3}, shuffled);
        Assertions.equals(1, BitMathRandomized.weightedPick(new int[]{1, 3, 6}, 2));

        // 高级结构：Trie、树状数组、线段树、LRU、RandomizedSet。
        AdvancedStructures.Trie trie = new AdvancedStructures.Trie();
        trie.insert("algo");
        Assertions.isTrue(trie.search("algo"));
        Assertions.isFalse(trie.search("alg"));
        Assertions.isTrue(trie.startsWith("alg"));
        AdvancedStructures.FenwickTree fenwick = new AdvancedStructures.FenwickTree(5);
        fenwick.add(0, 1);
        fenwick.add(3, 4);
        Assertions.equals(5, fenwick.prefixSum(3));
        AdvancedStructures.SegmentTree segmentTree = new AdvancedStructures.SegmentTree(new int[]{1, 2, 3, 4});
        Assertions.equals(5, segmentTree.query(1, 2));
        AdvancedStructures.LruCache lru = new AdvancedStructures.LruCache(2);
        lru.put(1, 1);
        lru.put(2, 2);
        Assertions.equals(1, lru.get(1));
        lru.put(3, 3);
        Assertions.equals(-1, lru.get(2));
        AdvancedStructures.RandomizedSet set = new AdvancedStructures.RandomizedSet();
        Assertions.isTrue(set.insert(10));
        Assertions.isFalse(set.insert(10));
        Assertions.isTrue(set.insert(20));
        Assertions.equals(10, set.getByTicket(0));
        Assertions.isTrue(set.remove(10));
        Assertions.isFalse(set.remove(30));
    }

    private static void interviewWorkflowAndCoverage() {
        // 面试表达测试：题解要包含暴力解、优化解、时间复杂度和空间复杂度。
        AlgoLibrary.ProblemAnalysis analysis = AlgoLibrary.InterviewWorkflow.analyzeTwoSum();
        Assertions.isTrue(AlgoLibrary.InterviewWorkflow.hasCompleteExplanation(analysis));
        AlgoLibrary.ReviewRecord review = AlgoLibrary.InterviewWorkflow.review(
                "boundary missed",
                "sliding window",
                "window contains no duplicate chars",
                List.of("empty", "single", "duplicate"));
        Assertions.equals("boundary missed", review.failureReason());
        Assertions.equals(List.of("empty", "single", "duplicate"), review.edgeCases());
    }
}
