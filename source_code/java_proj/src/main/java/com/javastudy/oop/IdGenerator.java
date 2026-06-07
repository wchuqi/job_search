package com.javastudy.oop;

/**
 * 知识点：静态成员
 * 静态可变计数器 + 静态方法 next()
 * 所有调用共享同一个计数器状态
 */
public class IdGenerator {
    private static int counter = 0;

    /**
     * 获取下一个ID
     */
    public static int next() {
        return ++counter;
    }

    /**
     * 重置计数器（测试用）
     */
    public static void reset() {
        counter = 0;
    }

    /**
     * 获取当前计数器值
     */
    public static int current() {
        return counter;
    }
}
