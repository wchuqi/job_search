package com.javastudy.functional;

import java.util.List;
import java.util.concurrent.atomic.LongAdder;
import java.util.stream.IntStream;

/**
 * 知识点：parallelStream 并行流
 *
 * 适用场景：
 *   - 大数据量（通常 > 10,000 元素）
 *   - 每个元素的处理相互独立（无共享可变状态）
 *   - CPU 密集型操作（IO 密集型用 CompletableFuture 更好）
 *
 * 不适用场景：
 *   - 小数据量（线程调度开销 > 并行收益）
 *   - 有顺序依赖（如 findFirst 在并行流中需要额外同步）
 *   - 操作有副作用（写共享变量）
 *
 * 原理：使用 ForkJoinPool.commonPool()，默认线程数 = CPU 核心数 - 1
 */
public class ParallelStreamDemo {

    // ── 基本使用 ─────────────────────────────────────────────────────

    /** 并行求和 */
    public static int parallelSum(List<Integer> numbers) {
        return numbers.parallelStream()
                .mapToInt(Integer::intValue)
                .sum(); // IntStream.sum() 内部是线程安全的
    }

    /** 并行过滤 + 收集 */
    public static List<Integer> parallelFilter(List<Integer> numbers) {
        return numbers.parallelStream()
                .filter(n -> n > 0)
                .toList(); // toList() 返回不可变列表，线程安全
    }

    // ── 正确使用：无副作用 ────────────────────────────────────────────

    /** 正确：使用 reduce 归约（无副作用） */
    public static long parallelCountCharacters(List<String> words) {
        return words.parallelStream()
                .mapToLong(String::length)
                .sum();
    }

    /** 正确：使用 collect（线程安全的收集器） */
    public static List<String> parallelTransform(List<String> words) {
        return words.parallelStream()
                .map(String::toUpperCase)
                .toList();
    }

    // ── 反模式：有副作用（不应使用 parallelStream） ───────────────────

    /**
     * 反模式：在并行流中修改共享可变状态。
     * 用 LongAdder 演示即使使用线程安全的计数器，forEach 顺序也不确定。
     */
    public static long[] parallelSideEffectDemo(List<Integer> numbers) {
        LongAdder adder = new LongAdder();
        numbers.parallelStream().forEach(n -> adder.increment());
        // 结果正确但顺序不确定
        return new long[]{adder.sum()};
    }

    // ── 何时使用 ─────────────────────────────────────────────────────

    /**
     * 适合并行的场景：大数据量 + 纯函数操作
     * 测试中用小数据量验证正确性
     */
    public static int parallelBigDataSum() {
        return IntStream.rangeClosed(1, 10000)
                .boxed()
                .parallel()
                .mapToInt(Integer::intValue)
                .sum();
    }

    /**
     * 不适合并行的场景：小数据量
     * parallelStream 的线程调度开销可能超过并行收益
     */
    public static int sequentialSmallData(List<Integer> numbers) {
        return numbers.stream() // 小数据量用串行
                .mapToInt(Integer::intValue)
                .sum();
    }
}
