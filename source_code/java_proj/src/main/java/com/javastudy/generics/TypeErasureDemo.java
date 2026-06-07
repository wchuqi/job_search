package com.javastudy.generics;

import java.util.ArrayList;
import java.util.List;

/**
 * 知识点：类型擦除 (Type Erasure)
 * 运行时 List<String> 和 List<Integer> 的 Class 对象相同
 * 不能 new T()，不能用基本类型作泛型参数
 */
public class TypeErasureDemo {

    /**
     * 运行时 List<String>.class == List<Integer>.class
     */
    @SuppressWarnings("rawtypes")
    public static boolean listClassEquals() {
        List<String> strings = new ArrayList<>();
        List<Integer> integers = new ArrayList<>();
        return strings.getClass() == integers.getClass(); // true
    }

    /**
     * 不能直接 new T()，因为运行时 T 被擦除为 Object
     */
    public static <T> T createDefault(Class<T> clazz) throws Exception {
        return clazz.getDeclaredConstructor().newInstance();
    }

    /**
     * 泛型不能用基本类型，必须用包装类
     */
    public static Box<Integer> boxInt(int value) {
        return new Box<>(value); // 自动装箱 int -> Integer
    }

    /**
     * 运行时类型检查对泛型无效
     */
    @SuppressWarnings("unchecked")
    public static boolean instanceofCheck(Object obj) {
        // 不能写 obj instanceof List<String>
        return obj instanceof List<?>;
    }
}
