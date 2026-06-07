package com.javastudy.basics;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class MethodDemoTest {

    @Test
    void testAdd() {
        assertEquals(5, MethodDemo.add(2, 3));
        assertEquals(-1, MethodDemo.add(2, -3));
    }

    @Test
    void testSumVarargs() {
        assertEquals(15, MethodDemo.sum(1, 2, 3, 4, 5));
        assertEquals(0, MethodDemo.sum()); // 空参数
        assertEquals(42, MethodDemo.sum(42));
    }

    @Test
    void testFormat() {
        assertEquals("Hello, World!", MethodDemo.format("Hello, %s!", "World"));
        assertEquals("Age: 25", MethodDemo.format("Age: %d", 25));
    }

    @Test
    void testSumEmpty() {
        assertEquals(0, MethodDemo.sumEmpty());
    }
}
