package com.javastudy.basics;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class IntegerOverflowDemoTest {

    @Test
    void testIntOverflow() {
        assertEquals(Integer.MIN_VALUE, IntegerOverflowDemo.intOverflow());
    }

    @Test
    void testIntUnderflow() {
        assertEquals(Integer.MAX_VALUE, IntegerOverflowDemo.intUnderflow());
    }

    @Test
    void testLongOverflow() {
        assertEquals(Long.MIN_VALUE, IntegerOverflowDemo.longOverflow());
    }

    @Test
    void testSafeAddOverflow() {
        assertThrows(ArithmeticException.class,
            () -> IntegerOverflowDemo.safeAdd(Integer.MAX_VALUE, 1));
    }

    @Test
    void testSafeAddNormal() {
        assertEquals(300, IntegerOverflowDemo.safeAdd(100, 200));
    }

    @Test
    void testSafeMultiplyOverflow() {
        assertThrows(ArithmeticException.class,
            () -> IntegerOverflowDemo.safeMultiply(Integer.MAX_VALUE, 2));
    }

    @Test
    void testWillOverflow() {
        assertTrue(IntegerOverflowDemo.willOverflow(Integer.MAX_VALUE, 1));
        assertFalse(IntegerOverflowDemo.willOverflow(100, 200));
    }
}
