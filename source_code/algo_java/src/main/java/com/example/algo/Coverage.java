package com.example.algo;

import java.util.EnumSet;
import java.util.Set;

/**
 * 简单的进程内知识点覆盖追踪器。
 *
 * <p>算法源码或测试执行到某个知识点时调用 {@link #hit(CoverageTopic)}。
 * 最后由 TestRunner 检查是否还有遗漏主题。</p>
 */
public final class Coverage {
    // EnumSet 专门用于 enum 集合，比 HashSet 更轻量，也能表达“哪些主题被命中过”。
    private static final EnumSet<CoverageTopic> HIT = EnumSet.noneOf(CoverageTopic.class);

    private Coverage() {
    }

    /** Mark a knowledge topic as exercised by production code or a test. */
    public static void hit(CoverageTopic topic) {
        HIT.add(topic);
    }

    /** Clear previous marks before a test run. */
    public static void reset() {
        HIT.clear();
    }

    /** Return topics that have not been hit in the current test run. */
    public static Set<CoverageTopic> missedTopics() {
        EnumSet<CoverageTopic> missed = EnumSet.allOf(CoverageTopic.class);
        missed.removeAll(HIT);
        return missed;
    }
}
