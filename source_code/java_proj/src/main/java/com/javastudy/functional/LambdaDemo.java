package com.javastudy.functional;

import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * 知识点：Lambda 表达式语法与类型推断
 *
 * 语法形式：
 *   (parameters) -> expression          // 单表达式，自动返回
 *   (parameters) -> { statements; }     // 多语句块，需要显式 return
 *
 * 类型推断：
 *   - 编译器根据目标类型（函数式接口）推断参数类型
 *   - 单参数可省略括号：x -> x + 1
 *   - 无参数用空括号：() -> value
 */
public class LambdaDemo {

    // ── 基本语法 ──────────────────────────────────────────────────────

    /** 无参数 lambda */
    public static String noArgs() {
        Runnable r = () -> System.out.println("hello");
        r.run();
        return "executed";
    }

    /** 单参数 lambda（省略括号） */
    public static String singleArg(String input) {
        java.util.function.Function<String, String> transformer = (String s) -> s.toUpperCase();
        return transformer.apply(input);
    }

    /** 多参数 lambda */
    public static int multiArgs(int a, int b) {
        java.util.function.IntBinaryOperator calculator = (int x, int y) -> x + y;
        return calculator.applyAsInt(a, b);
    }

    /** 多语句 lambda（需要花括号和 return） */
    public static String multiStatement(List<String> items) {
        java.util.function.Function<List<String>, String> joiner = (List<String> list) -> {
            var sb = new StringBuilder();
            for (int i = 0; i < list.size(); i++) {
                if (i > 0) sb.append(", ");
                sb.append(list.get(i));
            }
            return sb.toString();
        };
        return joiner.apply(items);
    }

    // ── 类型推断 ──────────────────────────────────────────────────────

    /** 编译器从 Comparator<String> 推断参数类型 */
    public static List<String> sortWithInferredType(List<String> words) {
        var sorted = new java.util.ArrayList<>(words);
        // 不写 (String a, String b)，编译器从 Comparator 推断为 String
        sorted.sort((a, b) -> a.length() - b.length());
        return sorted;
    }

    /** 从目标类型推断 lambda 的参数类型 */
    public static int inferredFromTarget(int x) {
        // 目标类型是 java.util.function.IntUnaryOperator，推断 a 为 int
        java.util.function.IntUnaryOperator op = a -> a * a;
        return op.applyAsInt(x);
    }

    // ── 变量捕获 ──────────────────────────────────────────────────────

    /**
     * lambda 可以捕获 effectively final 的局部变量。
     * "effectively final" = 初始化后从未被重新赋值。
     */
    public static String captureEffectivelyFinal(String prefix) {
        // prefix 是 effectively final
        java.util.function.Function<String, String> transformer = (String s) -> prefix + ": " + s;
        return transformer.apply("hello");
    }

    /**
     * lambda 不能捕获可变局部变量（编译错误演示用字符串代替）
     * 这里演示用 final 数组来模拟可变状态
     */
    public static int[] captureMutableState() {
        final int[] counter = {0}; // 数组引用是 final，内容可变
        Runnable r = () -> counter[0]++;
        r.run();
        r.run();
        return counter;
    }

    // ── 实际应用：排序、Runnable ──────────────────────────────────────

    /** 用 lambda 做自定义排序 */
    public static List<String> sortByLength(List<String> words) {
        var sorted = new java.util.ArrayList<>(words);
        sorted.sort(Comparator.comparingInt(String::length));
        return sorted;
    }

    /** 用 lambda 创建 Runnable */
    public static String runWithLambda() {
        var result = new String[1];
        Runnable r = () -> result[0] = "done";
        r.run();
        return result[0];
    }
}
