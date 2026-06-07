package com.javastudy.functional;

import java.util.IntSummaryStatistics;
import java.util.List;
import java.util.stream.IntStream;

/**
 * 知识点：mapToInt 转为基本类型流，sum() 等聚合操作
 *
 * Stream<Integer> -> IntStream（避免装箱开销）
 * IntStream 提供 sum(), average(), min(), max(), summaryStatistics()
 *
 * 同理：mapToDouble -> DoubleStream, mapToLong -> LongStream
 */
public class MapToIntDemo {

    // ── 基本 mapToInt + sum ───────────────────────────────────────────

    /** 字符串长度之和 */
    public static int sumLengths(List<String> words) {
        return words.stream()
                .mapToInt(String::length)
                .sum();
    }

    /** 整数列表求和 */
    public static int sumIntegers(List<Integer> numbers) {
        return numbers.stream()
                .mapToInt(Integer::intValue)
                .sum();
    }

    // ── IntStream 聚合方法 ────────────────────────────────────────────

    /** 最大值 */
    public static int maxOf(List<Integer> numbers) {
        return numbers.stream()
                .mapToInt(Integer::intValue)
                .max()
                .orElse(0);
    }

    /** 最小值 */
    public static int minOf(List<Integer> numbers) {
        return numbers.stream()
                .mapToInt(Integer::intValue)
                .min()
                .orElse(0);
    }

    /** 平均值 */
    public static double averageOf(List<Integer> numbers) {
        return numbers.stream()
                .mapToInt(Integer::intValue)
                .average()
                .orElse(0.0);
    }

    /** 汇总统计 */
    public static IntSummaryStatistics statistics(List<Integer> numbers) {
        return numbers.stream()
                .mapToInt(Integer::intValue)
                .summaryStatistics();
    }

    // ── IntStream.range / rangeClosed ─────────────────────────────────

    /** 1 + 2 + ... + n */
    public static int sumRange(int n) {
        return IntStream.rangeClosed(1, n).sum();
    }

    /** 生成 [start, end) 范围的偶数列表 */
    public static List<Integer> evenNumbers(int start, int end) {
        return IntStream.range(start, end)
                .filter(n -> n % 2 == 0)
                .boxed()
                .toList();
    }

    // ── mapToInt 配合 filter ─────────────────────────────────────────

    /** 过滤后求和 */
    public static int sumPositive(List<Integer> numbers) {
        return numbers.stream()
                .mapToInt(Integer::intValue)
                .filter(n -> n > 0)
                .sum();
    }

    /** 字符串长度 > threshold 的总长度 */
    public static int sumLengthsAbove(List<String> words, int threshold) {
        return words.stream()
                .mapToInt(String::length)
                .filter(len -> len > threshold)
                .sum();
    }
}
