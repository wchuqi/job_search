package com.javastudy.basics;

/**
 * 知识点：方法定义、返回值、Varargs
 */
public class MethodDemo {

    /**
     * 基本方法定义
     */
    public static int add(int a, int b) {
        return a + b;
    }

    /**
     * void 方法
     */
    public static void doNothing() {
        // 无返回值
    }

    /**
     * Varargs (可变参数)
     */
    public static int sum(int... numbers) {
        int total = 0;
        for (int n : numbers) {
            total += n;
        }
        return total;
    }

    /**
     * Varargs 与其他参数混合
     */
    public static String format(String template, Object... args) {
        return String.format(template, args);
    }

    /**
     * Varargs 可以传入空参数
     */
    public static int sumEmpty() {
        return sum(); // 合法，numbers 为空数组
    }
}
