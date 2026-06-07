package com.javastudy.functional;

import java.util.ArrayList;
import java.util.List;

/**
 * 知识点：peek 用于调试
 *
 * peek(Consumer) 是中间操作，对每个元素执行 Consumer（通常用于调试日志），
 * 然后将元素原样传递给下游。
 *
 * 注意：
 * - peek 主要用于调试，不应在生产代码中做业务逻辑
 * - 没有终端操作时，peek 不会执行（惰性求值）
 * - 不要在 peek 中修改元素状态（副作用反模式）
 */
public class PeekDemo {

    // ── 基本 peek ────────────────────────────────────────────────────

    /**
     * peek 查看流中的元素，不影响结果。
     * 用 List 收集 peek 的输出来验证。
     */
    public static List<String> peekToLog(List<String> items, List<String> log) {
        return items.stream()
                .peek(s -> log.add("before: " + s))
                .map(String::toUpperCase)
                .peek(s -> log.add("after: " + s))
                .toList();
    }

    /**
     * peek 在 filter 前后记录，观察哪些元素被过滤
     */
    public static List<Integer> peekFilter(List<Integer> numbers, List<String> log) {
        return numbers.stream()
                .peek(n -> log.add("input: " + n))
                .filter(n -> n > 0)
                .peek(n -> log.add("passed: " + n))
                .toList();
    }

    // ── peek 不执行的情况 ────────────────────────────────────────────

    /**
     * 只有 peek 没有终端操作 -> peek 不执行
     * 用计数器证明
     */
    public static long peekWithoutTerminal(List<String> items) {
        long[] counter = {0};
        items.stream()
                .peek(s -> counter[0]++); // 没有终端操作
        return counter[0]; // 返回 0
    }

    /**
     * 有终端操作 -> peek 执行
     */
    public static long peekWithTerminal(List<String> items) {
        long[] counter = {0};
        items.stream()
                .peek(s -> counter[0]++)
                .toList(); // 终端操作触发执行
        return counter[0]; // 返回 items.size()
    }

    // ── peek 在调试中的典型用法 ──────────────────────────────────────

    /**
     * 调试链式操作：在每一步之间插入 peek 查看中间结果
     */
    public static List<String> debugChain(List<String> items, List<String> debugLog) {
        return items.stream()
                .peek(s -> debugLog.add("0-原始: " + s))
                .filter(s -> !s.isBlank())
                .peek(s -> debugLog.add("1-过滤空串: " + s))
                .map(String::trim)
                .peek(s -> debugLog.add("2-trim: " + s))
                .map(String::toLowerCase)
                .peek(s -> debugLog.add("3-小写: " + s))
                .toList();
    }

    /**
     * 统计各阶段元素数量（调试用）
     */
    public static long[] countAtEachStage(List<String> items) {
        long[] counts = {0, 0, 0};
        items.stream()
                .peek(s -> counts[0]++)
                .filter(s -> s.length() > 2)
                .peek(s -> counts[1]++)
                .map(String::toUpperCase)
                .peek(s -> counts[2]++)
                .toList();
        return counts;
    }
}
