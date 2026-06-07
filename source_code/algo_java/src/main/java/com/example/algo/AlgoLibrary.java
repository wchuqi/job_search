package com.example.algo;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Deque;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.PriorityQueue;
import java.util.Random;
import java.util.Set;

/**
 * 算法源码库。
 *
 * <p>这个文件按学习文档章节组织为多个静态内部类。这样初学者阅读时可以从
 * “知识点名称 -> 源码分组 -> 测试方法” 直接对应。</p>
 */
public final class AlgoLibrary {
    private AlgoLibrary() {
    }

    public enum ComplexityClass {
        CONSTANT, LOG_N, LINEAR, N_LOG_N, QUADRATIC, EXPONENTIAL
    }

    public enum StructureChoice {
        ARRAY, HASH_MAP, ARRAY_DEQUE, PRIORITY_QUEUE, GRAPH_ADJACENCY_LIST
    }

    // record 是 Java 16+ 的不可变数据载体，这里用于表达面试题解分析结果。
    public record ProblemAnalysis(String bruteForce, String optimized, ComplexityClass time, ComplexityClass space) {
    }

    // 错题复盘记录：保存错因、正确模型、不变量和边界用例。
    public record ReviewRecord(String failureReason, String correctModel, String invariant, List<String> edgeCases) {
    }

    // 区间题常见结构：start/end 表示左闭右闭或题目约定的区间端点。
    public record Interval(int start, int end) {
    }

    /** 复杂度判断、数据结构选择和基础 Java 模板。 */
    public static final class ComplexityBasics {
        private ComplexityBasics() {
        }

        public static boolean fitsInputSize(long n, ComplexityClass complexity) {
            Coverage.hit(CoverageTopic.COMPLEXITY_ANALYSIS);
            // 面试中常用数据范围快速排除明显会超时的复杂度。
            return switch (complexity) {
                case CONSTANT, LOG_N, LINEAR, N_LOG_N -> n <= 1_000_000_000L;
                case QUADRATIC -> n <= 10_000L;
                case EXPONENTIAL -> n <= 25L;
            };
        }

        public static Map<Integer, Integer> frequencyCount(int[] nums) {
            Coverage.hit(CoverageTopic.DATA_STRUCTURE_CHOICE);
            Map<Integer, Integer> freq = new HashMap<>();
            for (int x : nums) {
                // merge 是 Java 21 中写计数器的简洁方式。
                freq.merge(x, 1, Integer::sum);
            }
            return freq;
        }

        public static StructureChoice chooseStructure(String operationNeed) {
            Coverage.hit(CoverageTopic.DATA_STRUCTURE_CHOICE);
            return switch (operationNeed) {
                case "random-access" -> StructureChoice.ARRAY;
                case "key-lookup" -> StructureChoice.HASH_MAP;
                case "fifo-lifo" -> StructureChoice.ARRAY_DEQUE;
                case "top-k" -> StructureChoice.PRIORITY_QUEUE;
                case "graph-walk" -> StructureChoice.GRAPH_ADJACENCY_LIST;
                default -> throw new IllegalArgumentException("unknown need: " + operationNeed);
            };
        }

        public static int safeMiddle(int left, int right) {
            Coverage.hit(CoverageTopic.OVERFLOW_HANDLING);
            // 避免 (left + right) 在 int 中溢出。
            return left + (right - left) / 2;
        }
    }

    /** 数组、字符串、双指针和滑动窗口。 */
    public static final class ArraysStrings {
        private ArraysStrings() {
        }

        public static int[] twoSum(int[] nums, int target) {
            Coverage.hit(CoverageTopic.HASH_TABLE);
            Coverage.hit(CoverageTopic.LEETCODE_CLASSICS);
            Map<Integer, Integer> seen = new HashMap<>();
            for (int i = 0; i < nums.length; i++) {
                // 当前数是 nums[i]，如果之前见过 need，就能组成答案。
                int need = target - nums[i];
                if (seen.containsKey(need)) {
                    return new int[]{seen.get(need), i};
                }
                // 先查再放，避免 target = 2 * nums[i] 时复用自己。
                seen.put(nums[i], i);
            }
            return new int[0];
        }

        public static int longestSubstringWithoutRepeat(String s) {
            Coverage.hit(CoverageTopic.SLIDING_WINDOW);
            Coverage.hit(CoverageTopic.ARRAYS_STRINGS);
            Coverage.hit(CoverageTopic.LEETCODE_CLASSICS);
            Map<Character, Integer> last = new HashMap<>();
            int left = 0;
            int answer = 0;
            for (int right = 0; right < s.length(); right++) {
                // right 是窗口右边界，逐个字符向右扩张。
                char c = s.charAt(right);
                if (last.containsKey(c)) {
                    // left 只能右移，不能被旧重复字符拉回去。
                    left = Math.max(left, last.get(c) + 1);
                }
                last.put(c, right);
                answer = Math.max(answer, right - left + 1);
            }
            return answer;
        }

        public static List<List<Integer>> threeSum(int[] nums) {
            Coverage.hit(CoverageTopic.TWO_POINTERS);
            Coverage.hit(CoverageTopic.SORTING);
            Coverage.hit(CoverageTopic.LEETCODE_CLASSICS);
            Arrays.sort(nums);
            List<List<Integer>> answer = new ArrayList<>();
            for (int i = 0; i < nums.length - 2; i++) {
                // 固定第一个数，剩下两个数用左右指针寻找。
                if (i > 0 && nums[i] == nums[i - 1]) {
                    continue;
                }
                if (nums[i] > 0) {
                    break;
                }
                int left = i + 1;
                int right = nums.length - 1;
                while (left < right) {
                    int sum = nums[i] + nums[left] + nums[right];
                    if (sum == 0) {
                        answer.add(List.of(nums[i], nums[left], nums[right]));
                        left++;
                        right--;
                        while (left < right && nums[left] == nums[left - 1]) {
                            left++;
                        }
                        while (left < right && nums[right] == nums[right + 1]) {
                            right--;
                        }
                    } else if (sum < 0) {
                        left++;
                    } else {
                        right--;
                    }
                }
            }
            return answer;
        }

        public static void moveZeroes(int[] nums) {
            Coverage.hit(CoverageTopic.TWO_POINTERS);
            int write = 0;
            // write 始终指向下一个非零元素应该写入的位置。
            for (int x : nums) {
                if (x != 0) {
                    nums[write++] = x;
                }
            }
            while (write < nums.length) {
                nums[write++] = 0;
            }
        }

        public static int removeDuplicatesSorted(int[] nums) {
            Coverage.hit(CoverageTopic.TWO_POINTERS);
            if (nums.length == 0) {
                Coverage.hit(CoverageTopic.EDGE_CASES);
                return 0;
            }
            int write = 1;
            for (int read = 1; read < nums.length; read++) {
                if (nums[read] != nums[read - 1]) {
                    nums[write++] = nums[read];
                }
            }
            return write;
        }

        public static int minSubArrayLenPositive(int target, int[] nums) {
            Coverage.hit(CoverageTopic.SLIDING_WINDOW);
            int left = 0;
            int sum = 0;
            int answer = Integer.MAX_VALUE;
            for (int right = 0; right < nums.length; right++) {
                // 正数数组中，窗口和会随着 right 右移而单调增加。
                sum += nums[right];
                while (sum >= target) {
                    answer = Math.min(answer, right - left + 1);
                    sum -= nums[left++];
                }
            }
            return answer == Integer.MAX_VALUE ? 0 : answer;
        }
    }

    /** 哈希表、前缀和、差分数组和取模前缀。 */
    public static final class HashPrefixDiff {
        private HashPrefixDiff() {
        }

        public static int[] prefixSums(int[] nums) {
            Coverage.hit(CoverageTopic.PREFIX_SUM);
            int[] prefix = new int[nums.length + 1];
            for (int i = 0; i < nums.length; i++) {
                // prefix[i + 1] 表示 nums[0..i] 的和。
                prefix[i + 1] = prefix[i] + nums[i];
            }
            return prefix;
        }

        public static int rangeSum(int[] prefix, int leftInclusive, int rightInclusive) {
            Coverage.hit(CoverageTopic.PREFIX_SUM);
            return prefix[rightInclusive + 1] - prefix[leftInclusive];
        }

        public static int subarraySumEqualsK(int[] nums, int k) {
            Coverage.hit(CoverageTopic.PREFIX_SUM);
            Coverage.hit(CoverageTopic.HASH_TABLE);
            Map<Integer, Integer> count = new HashMap<>();
            count.put(0, 1);
            int prefix = 0;
            int answer = 0;
            for (int x : nums) {
                // 如果历史上出现过 prefix-k，则这一段子数组和为 k。
                prefix += x;
                answer += count.getOrDefault(prefix - k, 0);
                count.merge(prefix, 1, Integer::sum);
            }
            return answer;
        }

        public static int[] applyRangeAdds(int length, int[][] updates) {
            Coverage.hit(CoverageTopic.DIFFERENCE_ARRAY);
            int[] diff = new int[length + 1];
            for (int[] update : updates) {
                // 差分思想：区间开头加 delta，区间结束后一位减 delta。
                int left = update[0];
                int right = update[1];
                int delta = update[2];
                diff[left] += delta;
                if (right + 1 < diff.length) {
                    diff[right + 1] -= delta;
                }
            }
            int[] answer = new int[length];
            int running = 0;
            for (int i = 0; i < length; i++) {
                running += diff[i];
                answer[i] = running;
            }
            return answer;
        }

        public static int subarraysDivByK(int[] nums, int k) {
            Coverage.hit(CoverageTopic.PREFIX_SUM);
            int[] count = new int[k];
            count[0] = 1;
            int prefix = 0;
            int answer = 0;
            for (int x : nums) {
                prefix = Math.floorMod(prefix + x, k);
                answer += count[prefix]++;
            }
            return answer;
        }
    }

    /** 栈、队列、双端队列、堆、单调栈和单调队列。 */
    public static final class StackQueueHeap {
        private StackQueueHeap() {
        }

        public static boolean validParentheses(String s) {
            Coverage.hit(CoverageTopic.STACK);
            Deque<Character> stack = new ArrayDeque<>();
            for (char c : s.toCharArray()) {
                // 遇到左括号就入栈，等待后续右括号匹配。
                if (c == '(' || c == '[' || c == '{') {
                    stack.push(c);
                } else {
                    if (stack.isEmpty()) {
                        return false;
                    }
                    char open = stack.pop();
                    if ((c == ')' && open != '(') || (c == ']' && open != '[') || (c == '}' && open != '{')) {
                        return false;
                    }
                }
            }
            return stack.isEmpty();
        }

        public static int[] topKFrequent(int[] nums, int k) {
            Coverage.hit(CoverageTopic.HEAP);
            Map<Integer, Integer> freq = ComplexityBasics.frequencyCount(nums);
            PriorityQueue<int[]> heap = new PriorityQueue<>(Comparator.comparingInt(a -> a[1]));
            for (Map.Entry<Integer, Integer> entry : freq.entrySet()) {
                // 小顶堆只保留频率最高的 k 个元素。
                heap.offer(new int[]{entry.getKey(), entry.getValue()});
                if (heap.size() > k) {
                    heap.poll();
                }
            }
            int[] answer = new int[heap.size()];
            for (int i = answer.length - 1; i >= 0; i--) {
                answer[i] = heap.poll()[0];
            }
            return answer;
        }

        public static int[] dailyTemperatures(int[] temperatures) {
            Coverage.hit(CoverageTopic.MONOTONIC_STRUCTURE);
            int[] answer = new int[temperatures.length];
            Deque<Integer> stack = new ArrayDeque<>();
            for (int i = 0; i < temperatures.length; i++) {
                // 栈里保存还没找到更高温度的下标。
                while (!stack.isEmpty() && temperatures[i] > temperatures[stack.peek()]) {
                    int prev = stack.pop();
                    answer[prev] = i - prev;
                }
                stack.push(i);
            }
            return answer;
        }

        public static int[] slidingWindowMax(int[] nums, int k) {
            Coverage.hit(CoverageTopic.MONOTONIC_STRUCTURE);
            Coverage.hit(CoverageTopic.QUEUE_DEQUE);
            int[] answer = new int[nums.length - k + 1];
            Deque<Integer> deque = new ArrayDeque<>();
            for (int right = 0; right < nums.length; right++) {
                // 先移除窗口左侧已经过期的下标。
                while (!deque.isEmpty() && deque.peekFirst() <= right - k) {
                    deque.pollFirst();
                }
                while (!deque.isEmpty() && nums[deque.peekLast()] <= nums[right]) {
                    deque.pollLast();
                }
                deque.offerLast(right);
                if (right >= k - 1) {
                    answer[right - k + 1] = nums[deque.peekFirst()];
                }
            }
            return answer;
        }

        public static int largestRectangleArea(int[] heights) {
            Coverage.hit(CoverageTopic.MONOTONIC_STRUCTURE);
            int answer = 0;
            Deque<Integer> stack = new ArrayDeque<>();
            for (int i = 0; i <= heights.length; i++) {
                int current = i == heights.length ? 0 : heights[i];
                while (!stack.isEmpty() && current < heights[stack.peek()]) {
                    int height = heights[stack.pop()];
                    int leftLess = stack.isEmpty() ? -1 : stack.peek();
                    answer = Math.max(answer, height * (i - leftLess - 1));
                }
                stack.push(i);
            }
            return answer;
        }

        public static final class MinStack {
            private final Deque<Integer> values = new ArrayDeque<>();
            private final Deque<Integer> mins = new ArrayDeque<>();

            public void push(int value) {
                Coverage.hit(CoverageTopic.STACK);
                values.push(value);
                // mins 栈同步保存“当前位置之前的最小值”。
                mins.push(mins.isEmpty() ? value : Math.min(value, mins.peek()));
            }

            public int pop() {
                mins.pop();
                return values.pop();
            }

            public int min() {
                return mins.peek();
            }
        }

        public static final class MedianFinder {
            private final PriorityQueue<Integer> small = new PriorityQueue<>(Comparator.reverseOrder());
            private final PriorityQueue<Integer> large = new PriorityQueue<>();

            public void addNum(int num) {
                Coverage.hit(CoverageTopic.HEAP);
                // small 是大顶堆，large 是小顶堆；两边保持数量平衡。
                small.offer(num);
                large.offer(small.poll());
                if (large.size() > small.size()) {
                    small.offer(large.poll());
                }
            }

            public double findMedian() {
                if (small.size() == large.size()) {
                    return (small.peek() + large.peek()) / 2.0;
                }
                return small.peek();
            }
        }
    }

    /** 二分查找、二分答案、排序和快速选择。 */
    public static final class BinarySortSelect {
        private BinarySortSelect() {
        }

        public static int binarySearchClosed(int[] nums, int target) {
            Coverage.hit(CoverageTopic.BINARY_SEARCH);
            int left = 0;
            int right = nums.length - 1;
            while (left <= right) {
                int mid = ComplexityBasics.safeMiddle(left, right);
                if (nums[mid] == target) {
                    return mid;
                } else if (nums[mid] < target) {
                    left = mid + 1;
                } else {
                    right = mid - 1;
                }
            }
            return -1;
        }

        public static int lowerBound(int[] nums, int target) {
            Coverage.hit(CoverageTopic.BINARY_SEARCH);
            int left = 0;
            int right = nums.length;
            while (left < right) {
                // lower_bound 找第一个 >= target 的位置。
                int mid = left + (right - left) / 2;
                if (nums[mid] < target) {
                    left = mid + 1;
                } else {
                    right = mid;
                }
            }
            return left;
        }

        public static int minEatingSpeed(int[] piles, int hours) {
            Coverage.hit(CoverageTopic.BINARY_ANSWER);
            int left = 1;
            int right = Arrays.stream(piles).max().orElse(1);
            while (left < right) {
                // 二分答案：速度越大，需要小时数越少，具备单调性。
                int mid = left + (right - left) / 2;
                long need = 0;
                for (int pile : piles) {
                    need += (pile + mid - 1L) / mid;
                }
                if (need <= hours) {
                    right = mid;
                } else {
                    left = mid + 1;
                }
            }
            return left;
        }

        public static int kthLargest(int[] nums, int k) {
            Coverage.hit(CoverageTopic.QUICK_SELECT);
            int target = nums.length - k;
            int left = 0;
            int right = nums.length - 1;
            while (left <= right) {
                // partition 后 pivot 左侧都 <= pivot，右侧都 >= pivot。
                int pivot = partition(nums, left, right);
                if (pivot == target) {
                    return nums[pivot];
                } else if (pivot < target) {
                    left = pivot + 1;
                } else {
                    right = pivot - 1;
                }
            }
            throw new IllegalArgumentException("invalid k");
        }

        private static int partition(int[] nums, int left, int right) {
            int pivotValue = nums[right];
            int store = left;
            for (int i = left; i < right; i++) {
                if (nums[i] <= pivotValue) {
                    swap(nums, store++, i);
                }
            }
            swap(nums, store, right);
            return store;
        }

        private static void swap(int[] nums, int i, int j) {
            int tmp = nums[i];
            nums[i] = nums[j];
            nums[j] = tmp;
        }

        public static List<Interval> mergeIntervals(List<Interval> intervals) {
            Coverage.hit(CoverageTopic.SORTING);
            if (intervals.isEmpty()) {
                Coverage.hit(CoverageTopic.EDGE_CASES);
                return List.of();
            }
            List<Interval> sorted = new ArrayList<>(intervals);
            sorted.sort(Comparator.comparingInt(Interval::start));
            List<Interval> answer = new ArrayList<>();
            int start = sorted.getFirst().start();
            int end = sorted.getFirst().end();
            for (int i = 1; i < sorted.size(); i++) {
                Interval current = sorted.get(i);
                if (current.start() <= end) {
                    end = Math.max(end, current.end());
                } else {
                    answer.add(new Interval(start, end));
                    start = current.start();
                    end = current.end();
                }
            }
            answer.add(new Interval(start, end));
            return answer;
        }
    }

    /** 单链表节点，模拟 LeetCode 给定的链表结构。 */
    public static final class ListNode {
        public int val;
        public ListNode next;

        public ListNode(int val) {
            this.val = val;
        }
    }

    /** 二叉树节点，模拟 LeetCode 给定的树结构。 */
    public static final class TreeNode {
        public int val;
        public TreeNode left;
        public TreeNode right;

        public TreeNode(int val) {
            this.val = val;
        }
    }

    /** 链表、树、图、DFS、BFS 和 visited 状态。 */
    public static final class LinkedTreeGraph {
        private LinkedTreeGraph() {
        }

        public static ListNode reverseList(ListNode head) {
            Coverage.hit(CoverageTopic.LINKED_LIST);
            ListNode prev = null;
            ListNode cur = head;
            while (cur != null) {
                // 修改 cur.next 前先保存 next，否则会丢失后续链表。
                ListNode next = cur.next;
                cur.next = prev;
                prev = cur;
                cur = next;
            }
            return prev;
        }

        public static ListNode mergeTwoLists(ListNode a, ListNode b) {
            Coverage.hit(CoverageTopic.LINKED_LIST);
            ListNode dummy = new ListNode(0);
            ListNode tail = dummy;
            while (a != null && b != null) {
                if (a.val <= b.val) {
                    tail.next = a;
                    a = a.next;
                } else {
                    tail.next = b;
                    b = b.next;
                }
                tail = tail.next;
            }
            tail.next = a != null ? a : b;
            return dummy.next;
        }

        public static boolean hasCycle(ListNode head) {
            Coverage.hit(CoverageTopic.LINKED_LIST);
            ListNode slow = head;
            ListNode fast = head;
            while (fast != null && fast.next != null) {
                slow = slow.next;
                fast = fast.next.next;
                if (slow == fast) {
                    return true;
                }
            }
            return false;
        }

        public static List<List<Integer>> levelOrder(TreeNode root) {
            Coverage.hit(CoverageTopic.TREE_TRAVERSAL);
            Coverage.hit(CoverageTopic.BFS);
            Coverage.hit(CoverageTopic.LEETCODE_CLASSICS);
            if (root == null) {
                Coverage.hit(CoverageTopic.EDGE_CASES);
                return List.of();
            }
            List<List<Integer>> answer = new ArrayList<>();
            Deque<TreeNode> queue = new ArrayDeque<>();
            queue.offer(root);
            while (!queue.isEmpty()) {
                // 当前 queue.size() 就是这一层节点数量。
                int size = queue.size();
                List<Integer> level = new ArrayList<>();
                for (int i = 0; i < size; i++) {
                    TreeNode node = queue.poll();
                    level.add(node.val);
                    if (node.left != null) {
                        queue.offer(node.left);
                    }
                    if (node.right != null) {
                        queue.offer(node.right);
                    }
                }
                answer.add(level);
            }
            return answer;
        }

        public static int maxDepth(TreeNode root) {
            Coverage.hit(CoverageTopic.TREE_TRAVERSAL);
            Coverage.hit(CoverageTopic.DFS);
            if (root == null) {
                return 0;
            }
            return 1 + Math.max(maxDepth(root.left), maxDepth(root.right));
        }

        public static TreeNode lowestCommonAncestor(TreeNode root, TreeNode p, TreeNode q) {
            Coverage.hit(CoverageTopic.TREE_TRAVERSAL);
            if (root == null || root == p || root == q) {
                return root;
            }
            TreeNode left = lowestCommonAncestor(root.left, p, q);
            TreeNode right = lowestCommonAncestor(root.right, p, q);
            if (left != null && right != null) {
                return root;
            }
            return left != null ? left : right;
        }

        public static List<List<Integer>> adjacencyList(int n, int[][] edges, boolean directed) {
            Coverage.hit(CoverageTopic.GRAPH_TRAVERSAL);
            List<List<Integer>> graph = new ArrayList<>();
            for (int i = 0; i < n; i++) {
                graph.add(new ArrayList<>());
            }
            for (int[] edge : edges) {
                graph.get(edge[0]).add(edge[1]);
                if (!directed) {
                    graph.get(edge[1]).add(edge[0]);
                }
            }
            return graph;
        }

        public static int[] bfsDistances(List<List<Integer>> graph, int start) {
            Coverage.hit(CoverageTopic.BFS);
            int[] dist = new int[graph.size()];
            Arrays.fill(dist, -1);
            Deque<Integer> queue = new ArrayDeque<>();
            queue.offer(start);
            dist[start] = 0;
            while (!queue.isEmpty()) {
                // BFS 按层扩展，第一次到达某点就是无权最短距离。
                int cur = queue.poll();
                for (int next : graph.get(cur)) {
                    if (dist[next] != -1) {
                        continue;
                    }
                    dist[next] = dist[cur] + 1;
                    queue.offer(next);
                }
            }
            return dist;
        }

        public static int connectedComponents(List<List<Integer>> graph) {
            Coverage.hit(CoverageTopic.DFS);
            boolean[] visited = new boolean[graph.size()];
            int count = 0;
            for (int i = 0; i < graph.size(); i++) {
                if (!visited[i]) {
                    count++;
                    dfsGraph(i, graph, visited);
                }
            }
            return count;
        }

        private static void dfsGraph(int cur, List<List<Integer>> graph, boolean[] visited) {
            visited[cur] = true;
            for (int next : graph.get(cur)) {
                if (!visited[next]) {
                    dfsGraph(next, graph, visited);
                }
            }
        }

        public static int numIslands(char[][] grid) {
            Coverage.hit(CoverageTopic.GRAPH_TRAVERSAL);
            Coverage.hit(CoverageTopic.LEETCODE_CLASSICS);
            int rows = grid.length;
            int cols = grid[0].length;
            int answer = 0;
            for (int r = 0; r < rows; r++) {
                for (int c = 0; c < cols; c++) {
                    if (grid[r][c] == '1') {
                        answer++;
                        sink(grid, r, c);
                    }
                }
            }
            return answer;
        }

        private static void sink(char[][] grid, int r, int c) {
            if (r < 0 || r >= grid.length || c < 0 || c >= grid[0].length || grid[r][c] != '1') {
                return;
            }
            grid[r][c] = '0';
            sink(grid, r + 1, c);
            sink(grid, r - 1, c);
            sink(grid, r, c + 1);
            sink(grid, r, c - 1);
        }
    }

    /** 回溯、排列、组合和网格搜索。 */
    public static final class BacktrackingSearch {
        private BacktrackingSearch() {
        }

        public static List<List<Integer>> permute(int[] nums) {
            Coverage.hit(CoverageTopic.BACKTRACKING);
            Coverage.hit(CoverageTopic.LEETCODE_CLASSICS);
            List<List<Integer>> answer = new ArrayList<>();
            boolean[] used = new boolean[nums.length];
            backtrackPermute(nums, used, new ArrayList<>(), answer);
            return answer;
        }

        private static void backtrackPermute(int[] nums, boolean[] used, List<Integer> path, List<List<Integer>> answer) {
            if (path.size() == nums.length) {
                // 必须复制 path，不能把同一个可变 List 直接放进答案。
                answer.add(new ArrayList<>(path));
                return;
            }
            for (int i = 0; i < nums.length; i++) {
                if (used[i]) {
                    continue;
                }
                used[i] = true;
                path.add(nums[i]);
                backtrackPermute(nums, used, path, answer);
                path.removeLast();
                used[i] = false;
            }
        }

        public static List<List<Integer>> combinationSum(int[] candidates, int target) {
            Coverage.hit(CoverageTopic.BACKTRACKING);
            Arrays.sort(candidates);
            List<List<Integer>> answer = new ArrayList<>();
            backtrackCombination(candidates, target, 0, new ArrayList<>(), answer);
            return answer;
        }

        private static void backtrackCombination(int[] candidates, int remain, int start, List<Integer> path, List<List<Integer>> answer) {
            if (remain == 0) {
                answer.add(new ArrayList<>(path));
                return;
            }
            for (int i = start; i < candidates.length && candidates[i] <= remain; i++) {
                path.add(candidates[i]);
                backtrackCombination(candidates, remain - candidates[i], i, path, answer);
                path.removeLast();
            }
        }

        public static int shortestPathInGrid(int[][] grid) {
            Coverage.hit(CoverageTopic.BFS);
            int rows = grid.length;
            int cols = grid[0].length;
            if (grid[0][0] == 1 || grid[rows - 1][cols - 1] == 1) {
                return -1;
            }
            int[][] dirs = {{1, 0}, {-1, 0}, {0, 1}, {0, -1}};
            boolean[][] visited = new boolean[rows][cols];
            Deque<int[]> queue = new ArrayDeque<>();
            queue.offer(new int[]{0, 0, 0});
            visited[0][0] = true;
            while (!queue.isEmpty()) {
                int[] cur = queue.poll();
                if (cur[0] == rows - 1 && cur[1] == cols - 1) {
                    return cur[2];
                }
                for (int[] dir : dirs) {
                    int nr = cur[0] + dir[0];
                    int nc = cur[1] + dir[1];
                    if (nr >= 0 && nr < rows && nc >= 0 && nc < cols && grid[nr][nc] == 0 && !visited[nr][nc]) {
                        visited[nr][nc] = true;
                        queue.offer(new int[]{nr, nc, cur[2] + 1});
                    }
                }
            }
            return -1;
        }
    }

    /** 动态规划：一维、二维、背包、子序列、区间、树形和状态压缩。 */
    public static final class DynamicProgramming {
        private DynamicProgramming() {
        }

        public static int climbStairs(int n) {
            Coverage.hit(CoverageTopic.DYNAMIC_PROGRAMMING);
            Coverage.hit(CoverageTopic.ONE_DIMENSION_DP);
            if (n <= 2) {
                return n;
            }
            int prev2 = 1;
            int prev1 = 2;
            for (int i = 3; i <= n; i++) {
                int cur = prev1 + prev2;
                prev2 = prev1;
                prev1 = cur;
            }
            return prev1;
        }

        public static int maxSubArray(int[] nums) {
            Coverage.hit(CoverageTopic.DYNAMIC_PROGRAMMING);
            Coverage.hit(CoverageTopic.LEETCODE_CLASSICS);
            int cur = nums[0];
            int answer = nums[0];
            for (int i = 1; i < nums.length; i++) {
                // cur 表示“必须以当前位置结尾”的最大子数组和。
                cur = Math.max(nums[i], cur + nums[i]);
                answer = Math.max(answer, cur);
            }
            return answer;
        }

        public static int coinChange(int[] coins, int amount) {
            Coverage.hit(CoverageTopic.KNAPSACK_DP);
            int inf = amount + 1;
            int[] dp = new int[amount + 1];
            Arrays.fill(dp, inf);
            dp[0] = 0;
            for (int coin : coins) {
                // 完全背包：正序遍历 sum，允许同一个 coin 被重复使用。
                for (int sum = coin; sum <= amount; sum++) {
                    dp[sum] = Math.min(dp[sum], dp[sum - coin] + 1);
                }
            }
            return dp[amount] == inf ? -1 : dp[amount];
        }

        public static int lengthOfLIS(int[] nums) {
            Coverage.hit(CoverageTopic.SUBSEQUENCE_DP);
            Coverage.hit(CoverageTopic.BINARY_SEARCH);
            Coverage.hit(CoverageTopic.LEETCODE_CLASSICS);
            int[] tails = new int[nums.length];
            int size = 0;
            for (int x : nums) {
                // tails[i] 是长度 i+1 的递增子序列的最小可能结尾。
                int i = BinarySortSelect.lowerBound(Arrays.copyOf(tails, size), x);
                tails[i] = x;
                if (i == size) {
                    size++;
                }
            }
            return size;
        }

        public static int longestCommonSubsequence(String a, String b) {
            Coverage.hit(CoverageTopic.TWO_DIMENSION_DP);
            Coverage.hit(CoverageTopic.SUBSEQUENCE_DP);
            int[][] dp = new int[a.length() + 1][b.length() + 1];
            for (int i = 1; i <= a.length(); i++) {
                for (int j = 1; j <= b.length(); j++) {
                    if (a.charAt(i - 1) == b.charAt(j - 1)) {
                        dp[i][j] = dp[i - 1][j - 1] + 1;
                    } else {
                        dp[i][j] = Math.max(dp[i - 1][j], dp[i][j - 1]);
                    }
                }
            }
            return dp[a.length()][b.length()];
        }

        public static boolean canPartition(int[] nums) {
            Coverage.hit(CoverageTopic.KNAPSACK_DP);
            int sum = Arrays.stream(nums).sum();
            if ((sum & 1) == 1) {
                return false;
            }
            int target = sum / 2;
            boolean[] dp = new boolean[target + 1];
            dp[0] = true;
            for (int x : nums) {
                for (int s = target; s >= x; s--) {
                    dp[s] |= dp[s - x];
                }
            }
            return dp[target];
        }

        public static int minPathSum(int[][] grid) {
            Coverage.hit(CoverageTopic.TWO_DIMENSION_DP);
            int rows = grid.length;
            int cols = grid[0].length;
            int[][] dp = new int[rows][cols];
            dp[0][0] = grid[0][0];
            for (int r = 1; r < rows; r++) {
                dp[r][0] = dp[r - 1][0] + grid[r][0];
            }
            for (int c = 1; c < cols; c++) {
                dp[0][c] = dp[0][c - 1] + grid[0][c];
            }
            for (int r = 1; r < rows; r++) {
                for (int c = 1; c < cols; c++) {
                    dp[r][c] = Math.min(dp[r - 1][c], dp[r][c - 1]) + grid[r][c];
                }
            }
            return dp[rows - 1][cols - 1];
        }

        public static int matrixChainMinCost(int[] dims) {
            Coverage.hit(CoverageTopic.INTERVAL_DP);
            int n = dims.length - 1;
            int[][] dp = new int[n][n];
            for (int len = 2; len <= n; len++) {
                for (int left = 0; left + len - 1 < n; left++) {
                    int right = left + len - 1;
                    dp[left][right] = Integer.MAX_VALUE;
                    for (int split = left; split < right; split++) {
                        int cost = dp[left][split] + dp[split + 1][right] + dims[left] * dims[split + 1] * dims[right + 1];
                        dp[left][right] = Math.min(dp[left][right], cost);
                    }
                }
            }
            return dp[0][n - 1];
        }

        public static int robTree(TreeNode root) {
            Coverage.hit(CoverageTopic.TREE_DP);
            int[] result = robTreeState(root);
            return Math.max(result[0], result[1]);
        }

        private static int[] robTreeState(TreeNode root) {
            if (root == null) {
                return new int[]{0, 0};
            }
            int[] left = robTreeState(root.left);
            int[] right = robTreeState(root.right);
            int rob = root.val + left[0] + right[0];
            int skip = Math.max(left[0], left[1]) + Math.max(right[0], right[1]);
            return new int[]{skip, rob};
        }

        public static int countSubsetsWithMask(int[] nums) {
            Coverage.hit(CoverageTopic.STATE_COMPRESSION);
            int count = 0;
            for (int mask = 0; mask < (1 << nums.length); mask++) {
                count++;
            }
            return count;
        }
    }

    /** 贪心、并查集、拓扑排序、最短路和最小生成树。 */
    public static final class GreedyGraph {
        private GreedyGraph() {
        }

        public static boolean canJump(int[] nums) {
            Coverage.hit(CoverageTopic.GREEDY);
            int farthest = 0;
            for (int i = 0; i < nums.length; i++) {
                // 如果当前位置已经超过能到达的最远点，说明断开了。
                if (i > farthest) {
                    return false;
                }
                farthest = Math.max(farthest, i + nums[i]);
            }
            return true;
        }

        public static int eraseOverlapIntervals(List<Interval> intervals) {
            Coverage.hit(CoverageTopic.GREEDY);
            List<Interval> sorted = new ArrayList<>(intervals);
            sorted.sort(Comparator.comparingInt(Interval::end));
            int removed = 0;
            int end = Integer.MIN_VALUE;
            for (Interval interval : sorted) {
                if (interval.start() >= end) {
                    end = interval.end();
                } else {
                    removed++;
                }
            }
            return removed;
        }

        public static final class UnionFind {
            private final int[] parent;
            private final int[] size;

            public UnionFind(int n) {
                Coverage.hit(CoverageTopic.UNION_FIND);
                parent = new int[n];
                size = new int[n];
                Arrays.fill(size, 1);
                for (int i = 0; i < n; i++) {
                    parent[i] = i;
                }
            }

            public int find(int x) {
                if (parent[x] != x) {
                    parent[x] = find(parent[x]);
                }
                return parent[x];
            }

            public boolean union(int a, int b) {
                int rootA = find(a);
                int rootB = find(b);
                if (rootA == rootB) {
                    return false;
                }
                if (size[rootA] < size[rootB]) {
                    int tmp = rootA;
                    rootA = rootB;
                    rootB = tmp;
                }
                parent[rootB] = rootA;
                size[rootA] += size[rootB];
                return true;
            }
        }

        public static boolean canFinishCourses(int n, int[][] prerequisites) {
            Coverage.hit(CoverageTopic.TOPOLOGICAL_SORT);
            List<List<Integer>> graph = LinkedTreeGraph.adjacencyList(n, prerequisites, true);
            int[] indegree = new int[n];
            for (int[] edge : prerequisites) {
                indegree[edge[1]]++;
            }
            Deque<Integer> queue = new ArrayDeque<>();
            for (int i = 0; i < n; i++) {
                if (indegree[i] == 0) {
                    queue.offer(i);
                }
            }
            int seen = 0;
            while (!queue.isEmpty()) {
                // 入度为 0 的点表示当前没有前置依赖，可以学习。
                int cur = queue.poll();
                seen++;
                for (int next : graph.get(cur)) {
                    if (--indegree[next] == 0) {
                        queue.offer(next);
                    }
                }
            }
            return seen == n;
        }

        public static int[] dijkstra(int n, int[][] edges, int start) {
            Coverage.hit(CoverageTopic.SHORTEST_PATH);
            List<List<int[]>> graph = new ArrayList<>();
            for (int i = 0; i < n; i++) {
                graph.add(new ArrayList<>());
            }
            for (int[] edge : edges) {
                graph.get(edge[0]).add(new int[]{edge[1], edge[2]});
            }
            int[] dist = new int[n];
            Arrays.fill(dist, Integer.MAX_VALUE / 4);
            dist[start] = 0;
            PriorityQueue<int[]> pq = new PriorityQueue<>(Comparator.comparingInt(a -> a[1]));
            pq.offer(new int[]{start, 0});
            while (!pq.isEmpty()) {
                // 优先队列每次取当前距离最短的候选节点。
                int[] cur = pq.poll();
                if (cur[1] != dist[cur[0]]) {
                    continue;
                }
                for (int[] edge : graph.get(cur[0])) {
                    int nd = cur[1] + edge[1];
                    if (nd < dist[edge[0]]) {
                        dist[edge[0]] = nd;
                        pq.offer(new int[]{edge[0], nd});
                    }
                }
            }
            return dist;
        }

        public static int minimumSpanningTreeWeight(int n, int[][] edges) {
            Coverage.hit(CoverageTopic.MINIMUM_SPANNING_TREE);
            int[][] sorted = Arrays.copyOf(edges, edges.length);
            Arrays.sort(sorted, Comparator.comparingInt(a -> a[2]));
            UnionFind uf = new UnionFind(n);
            int total = 0;
            int used = 0;
            for (int[] edge : sorted) {
                if (uf.union(edge[0], edge[1])) {
                    total += edge[2];
                    used++;
                }
            }
            return used == n - 1 ? total : -1;
        }
    }

    /** 位运算、数学和随机化。 */
    public static final class BitMathRandomized {
        private BitMathRandomized() {
        }

        public static int singleNumber(int[] nums) {
            Coverage.hit(CoverageTopic.BIT_OPERATION);
            int answer = 0;
            for (int x : nums) {
                answer ^= x;
            }
            return answer;
        }

        public static int hammingWeight(int x) {
            Coverage.hit(CoverageTopic.BIT_OPERATION);
            int count = 0;
            while (x != 0) {
                // x & (x - 1) 会去掉最低位的 1。
                x &= x - 1;
                count++;
            }
            return count;
        }

        public static int gcd(int a, int b) {
            Coverage.hit(CoverageTopic.MATH);
            while (b != 0) {
                int t = a % b;
                a = b;
                b = t;
            }
            return Math.abs(a);
        }

        public static int countPrimes(int n) {
            Coverage.hit(CoverageTopic.MATH);
            if (n <= 2) {
                return 0;
            }
            boolean[] composite = new boolean[n];
            int count = 0;
            for (int i = 2; i < n; i++) {
                if (!composite[i]) {
                    count++;
                    if ((long) i * i < n) {
                        for (int j = i * i; j < n; j += i) {
                            composite[j] = true;
                        }
                    }
                }
            }
            return count;
        }

        public static int[] fisherYates(int[] nums, Random random) {
            Coverage.hit(CoverageTopic.RANDOMIZATION);
            int[] copy = Arrays.copyOf(nums, nums.length);
            for (int i = copy.length - 1; i > 0; i--) {
                // 在 [0, i] 中随机挑一个位置和 i 交换，保证每种排列等概率。
                int j = random.nextInt(i + 1);
                int tmp = copy[i];
                copy[i] = copy[j];
                copy[j] = tmp;
            }
            return copy;
        }

        public static int weightedPick(int[] weights, int ticket) {
            Coverage.hit(CoverageTopic.RANDOMIZATION);
            int[] prefix = HashPrefixDiff.prefixSums(weights);
            int total = prefix[prefix.length - 1];
            int target = Math.floorMod(ticket, total) + 1;
            return BinarySortSelect.lowerBound(prefix, target) - 1;
        }
    }

    /** Trie、树状数组、线段树、LRU 和 RandomizedSet 等设计题结构。 */
    public static final class AdvancedStructures {
        private AdvancedStructures() {
        }

        public static final class Trie {
            private final Map<Character, Trie> children = new HashMap<>();
            private boolean word;

            public void insert(String value) {
                Coverage.hit(CoverageTopic.TRIE);
                Trie node = this;
                // 逐字符向下走；没有节点就创建。
                for (char c : value.toCharArray()) {
                    node = node.children.computeIfAbsent(c, ignored -> new Trie());
                }
                node.word = true;
            }

            public boolean search(String value) {
                Trie node = find(value);
                return node != null && node.word;
            }

            public boolean startsWith(String prefix) {
                return find(prefix) != null;
            }

            private Trie find(String value) {
                Trie node = this;
                for (char c : value.toCharArray()) {
                    node = node.children.get(c);
                    if (node == null) {
                        return null;
                    }
                }
                return node;
            }
        }

        public static final class FenwickTree {
            private final int[] tree;

            public FenwickTree(int n) {
                Coverage.hit(CoverageTopic.FENWICK_TREE);
                tree = new int[n + 1];
            }

            public void add(int index, int delta) {
                // i += i & -i 跳到下一个包含该位置的树状数组节点。
                for (int i = index + 1; i < tree.length; i += i & -i) {
                    tree[i] += delta;
                }
            }

            public int prefixSum(int index) {
                int sum = 0;
                // i -= i & -i 逐步汇总前缀区间。
                for (int i = index + 1; i > 0; i -= i & -i) {
                    sum += tree[i];
                }
                return sum;
            }
        }

        public static final class SegmentTree {
            private final int n;
            private final int[] tree;

            public SegmentTree(int[] nums) {
                Coverage.hit(CoverageTopic.SEGMENT_TREE);
                n = nums.length;
                tree = new int[n * 4];
                build(nums, 1, 0, n - 1);
            }

            private void build(int[] nums, int node, int left, int right) {
                if (left == right) {
                    tree[node] = nums[left];
                    return;
                }
                int mid = (left + right) / 2;
                build(nums, node * 2, left, mid);
                build(nums, node * 2 + 1, mid + 1, right);
                tree[node] = tree[node * 2] + tree[node * 2 + 1];
            }

            public int query(int ql, int qr) {
                return query(1, 0, n - 1, ql, qr);
            }

            private int query(int node, int left, int right, int ql, int qr) {
                if (ql <= left && right <= qr) {
                    return tree[node];
                }
                int mid = (left + right) / 2;
                int sum = 0;
                if (ql <= mid) {
                    sum += query(node * 2, left, mid, ql, qr);
                }
                if (qr > mid) {
                    sum += query(node * 2 + 1, mid + 1, right, ql, qr);
                }
                return sum;
            }
        }

        public static final class LruCache {
            private final int capacity;
            private final LinkedHashMap<Integer, Integer> map;

            public LruCache(int capacity) {
                Coverage.hit(CoverageTopic.LRU_CACHE);
                Coverage.hit(CoverageTopic.LEETCODE_CLASSICS);
                this.capacity = capacity;
                // accessOrder=true 让 LinkedHashMap 按访问顺序维护节点。
                this.map = new LinkedHashMap<>(capacity, 0.75f, true);
            }

            public int get(int key) {
                return map.getOrDefault(key, -1);
            }

            public void put(int key, int value) {
                map.put(key, value);
                if (map.size() > capacity) {
                    Integer eldest = map.keySet().iterator().next();
                    map.remove(eldest);
                }
            }
        }

        public static final class RandomizedSet {
            private final List<Integer> values = new ArrayList<>();
            private final Map<Integer, Integer> index = new HashMap<>();

            public boolean insert(int value) {
                Coverage.hit(CoverageTopic.RANDOMIZED_SET);
                // values 支持 O(1) 随机访问，index 支持 O(1) 定位。
                if (index.containsKey(value)) {
                    return false;
                }
                index.put(value, values.size());
                values.add(value);
                return true;
            }

            public boolean remove(int value) {
                Integer i = index.get(value);
                if (i == null) {
                    return false;
                }
                int last = values.getLast();
                values.set(i, last);
                index.put(last, i);
                values.removeLast();
                index.remove(value);
                return true;
            }

            public int getByTicket(int ticket) {
                return values.get(Math.floorMod(ticket, values.size()));
            }
        }
    }

    /** Java 21 常用写法和 Python 模板风险提示。 */
    public static final class Java21Templates {
        private Java21Templates() {
        }

        public static List<List<Integer>> adjacencyTemplate(int n, int[][] edges) {
            Coverage.hit(CoverageTopic.JAVA21_TEMPLATES);
            return LinkedTreeGraph.adjacencyList(n, edges, false);
        }

        public static List<int[]> sortIntervalsTemplate(List<int[]> intervals) {
            Coverage.hit(CoverageTopic.JAVA21_TEMPLATES);
            intervals.sort(Comparator.comparingInt(a -> a[0]));
            return intervals;
        }

        public static String pythonPitfallReminder() {
            Coverage.hit(CoverageTopic.PYTHON_TEMPLATE_AWARENESS);
            return "Python recursion depth and heapq min-heap defaults require attention.";
        }
    }

    /** 面试沟通、题解表达和错题复盘模型。 */
    public static final class InterviewWorkflow {
        private InterviewWorkflow() {
        }

        public static ProblemAnalysis analyzeTwoSum() {
            Coverage.hit(CoverageTopic.PROBLEM_SOLVING_FLOW);
            Coverage.hit(CoverageTopic.INTERVIEW_EXPLANATION);
            return new ProblemAnalysis("enumerate pairs", "hash map one pass", ComplexityClass.LINEAR, ComplexityClass.LINEAR);
        }

        public static ReviewRecord review(String failureReason, String correctModel, String invariant, List<String> edgeCases) {
            Coverage.hit(CoverageTopic.REVIEW_AND_ACCEPTANCE);
            Coverage.hit(CoverageTopic.EDGE_CASES);
            return new ReviewRecord(failureReason, correctModel, invariant, List.copyOf(edgeCases));
        }

        public static boolean hasCompleteExplanation(ProblemAnalysis analysis) {
            Coverage.hit(CoverageTopic.INTERVIEW_EXPLANATION);
            return !analysis.bruteForce().isBlank()
                    && !analysis.optimized().isBlank()
                    && analysis.time() != null
                    && analysis.space() != null;
        }
    }

    public static int[] listToArray(ListNode head) {
        List<Integer> values = new ArrayList<>();
        while (head != null) {
            values.add(head.val);
            head = head.next;
        }
        return values.stream().mapToInt(Integer::intValue).toArray();
    }

    public static TreeNode node(int value, TreeNode left, TreeNode right) {
        TreeNode node = new TreeNode(value);
        node.left = left;
        node.right = right;
        return node;
    }

    public static boolean sameTripletsIgnoreOrder(List<List<Integer>> left, List<List<Integer>> right) {
        Set<List<Integer>> a = new HashSet<>(left);
        Set<List<Integer>> b = new HashSet<>(right);
        return Objects.equals(a, b);
    }
}
