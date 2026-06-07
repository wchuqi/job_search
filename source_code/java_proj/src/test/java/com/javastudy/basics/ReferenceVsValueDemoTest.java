package com.javastudy.basics;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class ReferenceVsValueDemoTest {

    @Test
    void testPrimitivesCompareByValue() {
        assertTrue(ReferenceVsValueDemo.comparePrimitivesWithDoubleEquals(5, 5));
        assertFalse(ReferenceVsValueDemo.comparePrimitivesWithDoubleEquals(5, 6));
    }

    @Test
    void testStringDoubleEqualsWithLiterals() {
        // 字面量共享intern池
        assertTrue(ReferenceVsValueDemo.compareStringsWithDoubleEquals("hello", "hello"));
    }

    @Test
    void testStringDoubleEqualsWithNew() {
        // new String 创建新对象
        String s1 = new String("hello");
        String s2 = new String("hello");
        assertFalse(ReferenceVsValueDemo.compareStringsWithDoubleEquals(s1, s2));
    }

    @Test
    void testStringEqualsComparesContent() {
        String s1 = new String("hello");
        String s2 = new String("hello");
        assertTrue(ReferenceVsValueDemo.compareStringsWithEquals(s1, s2));
    }

    @Test
    void testNewStringsComparison() {
        var result = ReferenceVsValueDemo.compareNewStrings("test");
        assertFalse(result.doubleEquals(), "== should be false for new String");
        assertTrue(result.equalsResult(), "equals should be true for same content");
    }

    @Test
    void testLiteralStringsAreSameReference() {
        assertTrue(ReferenceVsValueDemo.compareLiteralStrings());
    }
}
