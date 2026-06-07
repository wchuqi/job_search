package com.javastudy.functional;

import com.javastudy.Generated;

import java.util.List;
import java.util.stream.Stream;

/**
 * 知识点：Stream 基础 — stream(), filter, map, toList, 惰性求值
 *
 * Stream 特点：
 * - 不存储数据，按需计算（惰性求值）
 * - 不修改源数据，返回新 Stream
 * - 一次消费后不可重用
 * - 中间操作返回 Stream，终端操作触发计算
 */
public class StreamBasicsDemo {

    // ── 创建 Stream ──────────────────────────────────────────────────

    /** 从集合创建 */
    public static List<String> fromCollection(List<String> input) {
        return input.stream().toList();
    }

    /** Stream.of 创建 */
    public static List<Integer> fromOf() {
        return Stream.of(1, 2, 3, 4, 5).toList();
    }

    /** Stream.iterate 创建无限流（limit 截断） */
    public static List<Integer> iterateStream(int size) {
        return Stream.iterate(1, n -> n + 1).limit(size).toList();
    }

    /** Stream.generate 创建无限流 */
    public static List<Double> generateRandoms(int size) {
        return Stream.generate(Math::random).limit(size).toList();
    }

    // ── filter: 过滤 ─────────────────────────────────────────────────

    /** 过滤偶数 */
    public static List<Integer> filterEven(List<Integer> numbers) {
        return numbers.stream()
                .filter(n -> n % 2 == 0)
                .toList();
    }

    /** 过滤非空字符串 */
    public static List<String> filterNonNull(List<String> items) {
        return items.stream()
                .filter(s -> s != null && !s.isBlank())
                .toList();
    }

    /** 链式过滤 */
    public static List<Integer> filterPositiveEven(List<Integer> numbers) {
        return numbers.stream()
                .filter(n -> n > 0)
                .filter(n -> n % 2 == 0)
                .toList();
    }

    // ── map: 映射转换 ────────────────────────────────────────────────

    /** 字符串转大写 */
    public static List<String> toUpperCase(List<String> items) {
        return items.stream()
                .map(String::toUpperCase)
                .toList();
    }

    /** 字符串转长度 */
    public static List<Integer> toLengths(List<String> items) {
        return items.stream()
                .map(String::length)
                .toList();
    }

    /** 链式 map */
    public static List<String> trimAndLower(List<String> items) {
        return items.stream()
                .map(String::trim)
                .map(String::toLowerCase)
                .toList();
    }

    // ── 惰性求值（Lazy Evaluation） ──────────────────────────────────

    /**
     * 证明 Stream 的惰性：中间操作不执行，终端操作才触发。
     * 使用计数器记录 filter 被调用的次数。
     */
    public static long countFilterExecutions(List<Integer> numbers) {
        long[] callCount = {0};
        List<Integer> result = numbers.stream()
                .filter(n -> {
                    callCount[0]++;
                    return n > 0;
                })
                .toList();
        // callCount 只计算到收集时实际遍历的元素数
        return callCount[0];
    }

    /**
     * short-circuit 演示：limit 只取前 N 个，后续元素不会被处理。
     */
    public static long countWithLimit(List<Integer> numbers, int limit) {
        long[] callCount = {0};
        List<Integer> result = numbers.stream()
                .filter(n -> {
                    callCount[0]++;
                    return n > 0;
                })
                .limit(limit)
                .toList();
        return callCount[0];
    }

    // ── Stream 一次性 ────────────────────────────────────────────────

    /**
     * Stream 只能消费一次，第二次终端操作会抛 IllegalStateException。
     */
    @Generated
    public static IllegalStateException demonstrateSingleUse() {
        Stream<String> stream = Stream.of("a", "b", "c");
        stream.toList(); // 第一次消费
        try {
            stream.toList(); // 第二次消费 -> 异常
            return null;
        } catch (IllegalStateException e) {
            return e;
        }
    }
}
