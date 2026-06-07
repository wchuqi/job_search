package com.javastudy.functional;

import java.util.List;
import java.util.function.*;

/**
 * 知识点：JDK 内置函数式接口
 *
 * | 接口            | 抽象方法      | 签名             | 典型用途       |
 * |-----------------|---------------|------------------|----------------|
 * | Predicate&lt;T&gt;     | test(T)       | T -> boolean     | 过滤 / 条件判断 |
 * | Function&lt;T,R&gt;    | apply(T)      | T -> R           | 转换           |
 * | Consumer&lt;T&gt;      | accept(T)     | T -> void        | 消费（副作用）  |
 * | Supplier&lt;T&gt;      | get()         | () -> T          | 生产 / 延迟求值 |
 * | UnaryOperator&lt;T&gt; | apply(T)      | T -> T           | 一元变换       |
 * | BinaryOperator&lt;T&gt;| apply(T,T)    | (T,T) -> T       | 二元归约       |
 */
public class BuiltInInterfacesDemo {

    // ── Predicate<T>: T -> boolean ────────────────────────────────────

    /** 判断是否为偶数 */
    public static Predicate<Integer> isEven() {
        return n -> n % 2 == 0;
    }

    /** 判断字符串长度是否大于指定值 */
    public static Predicate<String> lengthGreaterThan(int len) {
        return s -> s.length() > len;
    }

    /** 组合 Predicate: and / or / negate */
    public static List<Integer> filterEvenPositive(List<Integer> numbers) {
        Predicate<Integer> isPositive = n -> n > 0;
        return numbers.stream()
                .filter(isEven().and(isPositive))
                .toList();
    }

    // ── Function<T,R>: T -> R ─────────────────────────────────────────

    /** 字符串转长度 */
    public static Function<String, Integer> toLength() {
        return String::length;
    }

    /** 组合 Function: andThen / compose */
    public static Function<Integer, Integer> doubleThenAddOne() {
        Function<Integer, Integer> doubleIt = n -> n * 2;
        Function<Integer, Integer> addOne = n -> n + 1;
        return doubleIt.andThen(addOne); // n -> n*2 + 1
    }

    // ── Consumer<T>: T -> void ────────────────────────────────────────

    /** 将元素逐个添加到 StringBuilder */
    public static Consumer<String> appendTo(StringBuilder sb) {
        return sb::append;
    }

    /** 组合 Consumer: andThen */
    public static Consumer<String> logAndAppend(StringBuilder sb, StringBuilder log) {
        Consumer<String> logIt = s -> log.append("logged:").append(s).append(";");
        Consumer<String> appendIt = sb::append;
        return logIt.andThen(appendIt);
    }

    // ── Supplier<T>: () -> T ──────────────────────────────────────────

    /** 延迟创建默认值 */
    public static Supplier<String> defaultValue() {
        return () -> "default";
    }

    /** 用 Supplier 做延迟求值 */
    public static String getOrDefault(String value, Supplier<String> defaultSupplier) {
        return value != null ? value : defaultSupplier.get();
    }

    // ── UnaryOperator<T>: T -> T ──────────────────────────────────────

    /** 字符串转大写 */
    public static UnaryOperator<String> toUpper() {
        return String::toUpperCase;
    }

    /** 应用 UnaryOperator */
    public static String applyUnary(String input, UnaryOperator<String> op) {
        return op.apply(input);
    }

    // ── BinaryOperator<T>: (T,T) -> T ────────────────────────────────

    /** 取较大值 */
    public static BinaryOperator<Integer> max() {
        return Integer::max;
    }

    /** 字符串拼接 */
    public static BinaryOperator<String> concat() {
        return (a, b) -> a + b;
    }
}
