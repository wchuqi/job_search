package com.javastudy.functional;

import com.javastudy.Generated;

import java.util.List;
import java.util.Optional;

/**
 * 知识点：reduce 归约操作
 *
 * reduce(identity, accumulator)
 *   identity: 初始值（恒等值），也是流为空时的默认返回值
 *   accumulator: (result, element) -> newResult
 *
 * identity 的重要性：
 *   - 有 identity -> 返回 T，流为空时返回 identity
 *   - 无 identity  -> 返回 Optional<T>，流为空时返回 Optional.empty()
 */
public class ReduceDemo {

    // ── 带 identity 的 reduce ────────────────────────────────────────

    /** 求和：identity = 0 */
    public static int sum(List<Integer> numbers) {
        return numbers.stream()
                .reduce(0, Integer::sum);
    }

    /** 求积：identity = 1 */
    public static int product(List<Integer> numbers) {
        return numbers.stream()
                .reduce(1, (a, b) -> a * b);
    }

    /** 字符串拼接：identity = "" */
    public static String joinWithComma(List<String> items) {
        return items.stream()
                .reduce("", (a, b) -> a.isEmpty() ? b : a + ", " + b);
    }

    /** 求最大值 */
    public static int findMax(List<Integer> numbers) {
        return numbers.stream()
                .reduce(Integer::max)
                .orElse(Integer.MIN_VALUE);
    }

    // ── 无 identity 的 reduce（返回 Optional） ───────────────────────

    /** 无 identity 的求和 */
    public static Optional<Integer> sumOptional(List<Integer> numbers) {
        return numbers.stream()
                .reduce(Integer::sum);
    }

    /** 无 identity 的拼接 */
    public static Optional<String> joinOptional(List<String> items) {
        return items.stream()
                .reduce((a, b) -> a + ", " + b);
    }

    // ── identity 的重要性演示 ─────────────────────────────────────────

    /** 空流 + 有 identity -> 返回 identity */
    public static int sumEmptyWithIdentity() {
        return List.<Integer>of().stream()
                .reduce(0, Integer::sum); // 返回 0
    }

    /** 空流 + 无 identity -> 返回 Optional.empty() */
    public static Optional<Integer> sumEmptyWithoutIdentity() {
        return List.<Integer>of().stream()
                .reduce(Integer::sum); // 返回 Optional.empty()
    }

    // ── 三参数 reduce（带 combiner，用于并行流） ──────────────────────

    /** 三参数 reduce：combiner 用于并行流合并各子结果 */
    public static int sumParallel(List<Integer> numbers) {
        return numbers.parallelStream()
                .reduce(0, Integer::sum, Integer::sum);
        // accumulator: 各线程内部累加
        // combiner:    各线程结果合并
    }

    /** 用 reduce 实现 map（教学用途，实际应使用 map） */
    @Generated
    public static List<String> toUpperCaseViaReduce(List<String> items) {
        return items.parallelStream()
                .reduce(
                        new java.util.ArrayList<String>(),
                        (list, item) -> {
                            var newList = new java.util.ArrayList<>(list);
                            newList.add(item.toUpperCase());
                            return newList;
                        },
                        (list1, list2) -> {
                            var merged = new java.util.ArrayList<>(list1);
                            merged.addAll(list2);
                            return merged;
                        }
                );
    }
}
