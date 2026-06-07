package com.javastudy.functional;

import java.util.function.Function;

/**
 * 知识点：@FunctionalInterface 注解与自定义函数式接口
 *
 * 函数式接口：只包含一个抽象方法的接口（可以有多个 default / static 方法）。
 * @FunctionalInterface 让编译器强制校验这一点，违反则编译报错。
 */
public class FunctionalInterfaceDemo {

    // ── 自定义函数式接口 ──────────────────────────────────────────────

    /** 将字符串转换为大写 */
    @FunctionalInterface
    public interface StringTransformer {
        String transform(String input);

        // 允许 default 方法
        default StringTransformer andThen(StringTransformer after) {
            return input -> after.transform(this.transform(input));
        }
    }

    /** 计算两个 int 的结果 */
    @FunctionalInterface
    public interface IntCalculator {
        int calculate(int a, int b);

        // 允许 static 方法
        static IntCalculator add() {
            return Integer::sum;
        }

        static IntCalculator multiply() {
            return (a, b) -> a * b;
        }
    }

    /** 无参无返回值 */
    @FunctionalInterface
    public interface Action {
        void execute();
    }

    // ── 演示方法 ──────────────────────────────────────────────────────

    /**
     * 使用自定义 StringTransformer 将字符串转大写
     */
    public static String toUpperCase(String input) {
        StringTransformer transformer = String::toUpperCase;
        return transformer.transform(input);
    }

    /**
     * 使用 andThen 链式组合两个变换
     */
    public static String chainTransform(String input, StringTransformer first, StringTransformer second) {
        return first.andThen(second).transform(input);
    }

    /**
     * 使用自定义 IntCalculator 做加法
     */
    public static int add(int a, int b) {
        return IntCalculator.add().calculate(a, b);
    }

    /**
     * 使用自定义 IntCalculator 做乘法
     */
    public static int multiply(int a, int b) {
        return IntCalculator.multiply().calculate(a, b);
    }

    /**
     * 演示：lambda 赋值给 Function 类型（JDK 内置函数式接口）
     */
    public static Function<String, Integer> lengthFunction() {
        return String::length;
    }

    /**
     * 使用自定义 Action（无参无返回值的函数式接口）
     */
    public static int executeAndCount(Action action) {
        int[] counter = {0};
        Action counted = () -> {
            action.execute();
            counter[0]++;
        };
        counted.execute();
        return counter[0];
    }
}
