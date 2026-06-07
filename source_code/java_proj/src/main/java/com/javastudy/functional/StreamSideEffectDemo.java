package com.javastudy.functional;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 知识点：Stream 副作用反模式 vs 正确做法
 *
 * 反模式：在 stream 操作中修改外部集合
 *   - forEach + 外部 list.add() — 破坏了函数式编程的纯函数原则
 *   - 在并行流中尤其危险（ConcurrentModificationException / 数据丢失）
 *
 * 正确做法：用 collect(toList()) 让 Stream 内部管理收集
 */
public class StreamSideEffectDemo {

    // ── 反模式：forEach + 外部 add ────────────────────────────────────

    /**
     * 反模式：在 forEach 中向外部 list 添加元素。
     * 串行流可以工作，但：
     * - 破坏了函数式纯函数原则
     * - 如果换成 parallelStream，ArrayList 不是线程安全的
     */
    public static List<String> badSideEffect(List<String> items) {
        List<String> result = new ArrayList<>();
        items.stream()
                .filter(s -> s.length() > 3)
                .forEach(result::add); // 反模式！
        return result;
    }

    /**
     * 更危险的版本：parallelStream + 外部 add
     * 可能丢数据或 ConcurrentModificationException
     */
    public static List<String> badParallelSideEffect(List<String> items) {
        List<String> result = new ArrayList<>(); // 非线程安全
        items.parallelStream()
                .filter(s -> s.length() > 3)
                .forEach(result::add); // 严重反模式！
        return result;
    }

    // ── 正确做法：collect(toList) ────────────────────────────────────

    /**
     * 正确：使用 collect(toList())
     * - 无副作用，返回新集合
     * - 内部处理线程安全
     * - 函数式风格，可读性好
     */
    public static List<String> correctCollect(List<String> items) {
        return items.stream()
                .filter(s -> s.length() > 3)
                .toList();
    }

    /**
     * 正确：parallelStream + toList()
     * collect 内部处理线程安全的合并
     */
    public static List<String> correctParallelCollect(List<String> items) {
        return items.parallelStream()
                .filter(s -> s.length() > 3)
                .toList();
    }

    // ── 其他反模式：修改元素 ─────────────────────────────────────────

    /**
     * 反模式：map 中修改原始对象
     * Stream 应该是无副作用的转换
     */
    public static List<StringBuilder> badMutateInMap(List<String> items) {
        return items.stream()
                .map(s -> {
                    var sb = new StringBuilder(s);
                    sb.append("!"); // 这里创建了新对象，不算副作用
                    return sb;
                })
                .toList();
    }

    /**
     * 正确：纯函数转换
     */
    public static List<String> correctPureMap(List<String> items) {
        return items.stream()
                .map(s -> s + "!")
                .toList();
    }

    // ── 演示副作用的不确定性 ─────────────────────────────────────────

    /**
     * 收集到同步列表，证明并行流的执行顺序不确定。
     * 使用 Collections.synchronizedList 保证线程安全，但顺序仍然不确定。
     */
    public static List<String> parallelOrderUnpredictable(List<String> items) {
        List<String> result = Collections.synchronizedList(new ArrayList<>());
        items.parallelStream().forEach(result::add);
        return result;
    }
}
