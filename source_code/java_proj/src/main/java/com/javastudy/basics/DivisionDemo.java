package com.javastudy.basics;

import com.javastudy.Generated;

/**
 * 知识点：整数除法 vs 浮点除法
 * 5 / 2 == 2 (整数除法截断)
 * 5 / 2.0 == 2.5 (浮点除法)
 */
public class DivisionDemo {

    /**
     * 整数除法：结果截断小数部分
     */
    public static int integerDivision(int a, int b) {
        return a / b; // 5 / 2 = 2
    }

    /**
     * 浮点除法：保留小数
     */
    public static double floatDivision(int a, double b) {
        return a / b; // 5 / 2.0 = 2.5
    }

    /**
     * 强制浮点除法
     */
    public static double forceFloatDivision(int a, int b) {
        return (double) a / b; // 先转为 double 再除
    }

    /**
     * 取模运算
     */
    public static int modulus(int a, int b) {
        return a % b; // 5 % 2 = 1
    }

    /**
     * 除以零：整数除以零抛 ArithmeticException
     */
    @Generated
    public static int divideByZero(int a) {
        return a / 0; // 抛 ArithmeticException
    }

    /**
     * 浮点除以零：得到 Infinity，不抛异常
     */
    public static double floatDivideByZero(double a) {
        return a / 0.0; // Double.POSITIVE_INFINITY 或 Double.NEGATIVE_INFINITY
    }
}
