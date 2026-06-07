package com.javastudy.basics;

/**
 * 知识点：实例字段 vs 静态字段
 * 实例字段属于对象，静态字段属于类
 */
public class InstanceVsStaticDemo {

    // 静态字段：所有实例共享
    private static int counter = 0;

    // 实例字段：每个对象独立
    private int instanceValue;

    public InstanceVsStaticDemo(int instanceValue) {
        this.instanceValue = instanceValue;
        counter++; // 每次创建对象，计数器加1
    }

    /**
     * 获取静态计数器值
     */
    public static int getCounter() {
        return counter;
    }

    /**
     * 重置静态计数器
     */
    public static void resetCounter() {
        counter = 0;
    }

    /**
     * 获取实例字段值
     */
    public int getInstanceValue() {
        return instanceValue;
    }

    /**
     * 修改实例字段
     */
    public void setInstanceValue(int value) {
        this.instanceValue = value;
    }

    /**
     * 静态方法：只能访问静态成员
     */
    public static String staticMethod() {
        return "I am static, counter=" + counter;
        // 不能访问 instanceValue (编译错误)
    }

    /**
     * 实例方法：可以访问静态和实例成员
     */
    public String instanceMethod() {
        return "instance=" + instanceValue + ", static counter=" + counter;
    }
}
