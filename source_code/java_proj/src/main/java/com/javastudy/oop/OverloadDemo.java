package com.javastudy.oop;

/**
 * 知识点：方法重载 (Overloading)
 * 同名方法，不同参数类型/数量
 */
public class OverloadDemo {

    public String print(String value) {
        return "String: " + value;
    }

    public String print(int value) {
        return "int: " + value;
    }

    public String print(double value) {
        return "double: " + value;
    }

    public String print(String prefix, int value) {
        return prefix + ": " + value;
    }
}
