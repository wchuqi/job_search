package com.javastudy.generics;

import java.util.List;

/**
 * 知识点：通配符 (Wildcards)
 * ? extends T (上界/生产者/读取)
 * ? super T (下界/消费者/写入)
 * PECS: Producer Extends, Consumer Super
 */
public class WildcardDemo {

    /**
     * ? extends Number: 只能读取，不能写入
     * 适用于生产者（提供数据的方法）
     */
    public static double sum(List<? extends Number> numbers) {
        double total = 0;
        for (Number n : numbers) {
            total += n.doubleValue();
        }
        return total;
    }

    /**
     * ? super Integer: 只能写入 Integer 或其子类，读取只能得到 Object
     * 适用于消费者（接收数据的方法）
     */
    public static void addIntegers(List<? super Integer> list) {
        list.add(1);
        list.add(2);
        list.add(3);
    }

    /**
     * 无界通配符 <?>: 只能读取 Object，不能写入
     */
    public static int size(List<?> list) {
        return list.size();
    }
}
