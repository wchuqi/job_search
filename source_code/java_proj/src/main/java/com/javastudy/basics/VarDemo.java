package com.javastudy.basics;

import java.util.List;
import java.util.Map;

/**
 * 知识点：var 局部变量类型推断 (JDK 10+)
 * 编译期推断，非动态类型
 */
public class VarDemo {

    /**
     * var 推断基本类型
     */
    public static int varWithInt() {
        var x = 42; // 推断为 int
        return x;
    }

    /**
     * var 推断String
     */
    public static String varWithString() {
        var name = "hello"; // 推断为 String
        return name;
    }

    /**
     * var 推断泛型集合
     */
    public static List<String> varWithList() {
        var list = List.of("a", "b", "c"); // 推断为 List<String>
        return list;
    }

    /**
     * var 推断Map
     */
    public static Map<String, Integer> varWithMap() {
        var map = Map.of("key", 1); // 推断为 Map<String, Integer>
        return map;
    }

    /**
     * var 只能用于局部变量，不能用于字段、参数、返回类型
     * var 必须有初始化器
     */
    public static String varInForLoop() {
        var items = List.of("x", "y", "z");
        var sb = new StringBuilder();
        for (var item : items) { // var 在增强for循环中也可用
            sb.append(item);
        }
        return sb.toString();
    }
}
