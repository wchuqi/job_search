package com.javastudy.basics;

/**
 * 知识点：引用类型 vs 值类型, == vs equals
 * == 比较引用地址, equals 比较内容
 */
public class ReferenceVsValueDemo {

    /**
     * 演示 == 对基本类型比较值
     */
    public static boolean comparePrimitivesWithDoubleEquals(int a, int b) {
        return a == b;
    }

    /**
     * 演示 == 对String比较引用（字面量intern池行为）
     */
    public static boolean compareStringsWithDoubleEquals(String s1, String s2) {
        return s1 == s2;
    }

    /**
     * 演示 equals 对String比较内容
     */
    public static boolean compareStringsWithEquals(String s1, String s2) {
        return s1.equals(s2);
    }

    /**
     * new String 创建新对象，== 为false，equals 为true
     */
    public static StringComparisonResult compareNewStrings(String text) {
        String s1 = new String(text);
        String s2 = new String(text);
        return new StringComparisonResult(
            s1 == s2,       // false: 不同对象
            s1.equals(s2)   // true: 内容相同
        );
    }

    /**
     * 字面量String共享intern池，== 为true
     */
    public static boolean compareLiteralStrings() {
        String s1 = "hello";
        String s2 = "hello";
        return s1 == s2; // true: 同一个intern池对象
    }

    public record StringComparisonResult(boolean doubleEquals, boolean equalsResult) {}
}
