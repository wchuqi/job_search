package com.javastudy.generics;

import java.util.List;

/**
 * 知识点：泛型方法
 * 类型参数在方法级别声明
 */
public class GenericMethodDemo {

    /**
     * 基本泛型方法：返回列表第一个元素
     */
    public static <T> T first(List<T> list) {
        if (list == null || list.isEmpty()) {
            return null;
        }
        return list.get(0);
    }

    /**
     * 有界泛型方法：T 必须实现 Comparable
     */
    public static <T extends Comparable<T>> T max(List<T> list) {
        if (list == null || list.isEmpty()) {
            return null;
        }
        T max = list.get(0);
        for (T item : list) {
            if (item.compareTo(max) > 0) {
                max = item;
            }
        }
        return max;
    }

    /**
     * 多个类型参数
     */
    public static <K, V> String pairToString(K key, V value) {
        return key + "=" + value;
    }
}
