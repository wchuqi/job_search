package com.javastudy.basics;

/**
 * 知识点：整数溢出 (Integer Overflow)
 * 静默回绕，不抛异常
 */
public class IntegerOverflowDemo {

    /**
     * int 溢出: MAX_VALUE + 1 = MIN_VALUE
     */
    public static int intOverflow() {
        return Integer.MAX_VALUE + 1; // 回绕到 Integer.MIN_VALUE
    }

    /**
     * int 下溢: MIN_VALUE - 1 = MAX_VALUE
     */
    public static int intUnderflow() {
        return Integer.MIN_VALUE - 1; // 回绕到 Integer.MAX_VALUE
    }

    /**
     * long 溢出
     */
    public static long longOverflow() {
        return Long.MAX_VALUE + 1L;
    }

    /**
     * Math.addExact 溢出时抛出 ArithmeticException
     */
    public static int safeAdd(int a, int b) {
        return Math.addExact(a, b); // 溢出时抛 ArithmeticException
    }

    /**
     * Math.multiplyExact 溢出时抛出 ArithmeticException
     */
    public static int safeMultiply(int a, int b) {
        return Math.multiplyExact(a, b);
    }

    /**
     * 检测溢出的方法：比较运算前后的符号
     */
    public static boolean willOverflow(int a, int b) {
        // 正数相加变负数 -> 溢出
        if (a > 0 && b > 0 && a + b < 0) return true;
        // 负数相加变正数 -> 溢出
        if (a < 0 && b < 0 && a + b > 0) return true;
        return false;
    }
}
