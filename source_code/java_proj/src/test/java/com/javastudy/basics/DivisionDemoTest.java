package com.javastudy.basics;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class DivisionDemoTest {

    @Test
    void testIntegerDivision() {
        assertEquals(2, DivisionDemo.integerDivision(5, 2));
        assertEquals(0, DivisionDemo.integerDivision(1, 3));
    }

    @Test
    void testFloatDivision() {
        assertEquals(2.5, DivisionDemo.floatDivision(5, 2.0), 0.001);
    }

    @Test
    void testForceFloatDivision() {
        assertEquals(2.5, DivisionDemo.forceFloatDivision(5, 2), 0.001);
    }

    @Test
    void testModulus() {
        assertEquals(1, DivisionDemo.modulus(5, 2));
        assertEquals(0, DivisionDemo.modulus(4, 2));
    }

    @Test
    void testDivideByZeroThrows() {
        assertThrows(ArithmeticException.class,
            () -> DivisionDemo.divideByZero(5));
    }

    @Test
    void testFloatDivideByZero() {
        assertEquals(Double.POSITIVE_INFINITY, DivisionDemo.floatDivideByZero(5.0));
        assertEquals(Double.NEGATIVE_INFINITY, DivisionDemo.floatDivideByZero(-5.0));
    }
}
