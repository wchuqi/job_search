package com.javastudy.basics;

/**
 * 知识点：数值提升 (Numeric Promotion)
 * byte + byte = int, 需要显式转型
 */
public class NumericPromotionDemo {

    /**
     * byte + byte 自动提升为 int，赋值给 byte 需要强转
     */
    public static int addBytes(byte a, byte b) {
        // byte + byte -> int (自动提升)
        return a + b;
    }

    /**
     * 需要显式强转回 byte
     */
    public static byte addBytesWithCast(byte a, byte b) {
        return (byte) (a + b); // 必须强转，否则编译错误
    }

    /**
     * short 同理
     */
    public static int addShorts(short a, short b) {
        return a + b; // short + short -> int
    }

    /**
     * int 与 long 混合运算 -> long
     */
    public static long mixedArithmetic(int a, long b) {
        return a + b; // int + long -> long
    }

    /**
     * int 与 double 混合运算 -> double
     */
    public static double mixedArithmeticDouble(int a, double b) {
        return a + b; // int + double -> double
    }

    /**
     * char 参与算术运算时提升为 int
     */
    public static int charArithmetic(char c) {
        return c + 1; // char + int -> int
    }
}
