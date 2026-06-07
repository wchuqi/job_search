package com.javastudy.stringdatetime;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class BigDecimalComparisonDemoTest {

    @Test
    void equalsConsidersScale() {
        BigDecimal a = new BigDecimal("1.0");
        BigDecimal b = new BigDecimal("1.00");
        assertFalse(BigDecimalComparisonDemo.equalsWithScale(a, b),
                "equals() returns false when scales differ");
    }

    @Test
    void equalsIsTrueWhenScaleMatches() {
        BigDecimal a = new BigDecimal("1.00");
        BigDecimal b = new BigDecimal("1.00");
        assertTrue(BigDecimalComparisonDemo.equalsWithScale(a, b));
    }

    @Test
    void compareToIgnoresScale() {
        BigDecimal a = new BigDecimal("1.0");
        BigDecimal b = new BigDecimal("1.00");
        assertEquals(0, BigDecimalComparisonDemo.compareToIgnoreScale(a, b),
                "compareTo() should return 0 for equal values regardless of scale");
    }

    @Test
    void compareToReturnsNegativeWhenLess() {
        BigDecimal a = new BigDecimal("1.0");
        BigDecimal b = new BigDecimal("2.0");
        assertTrue(BigDecimalComparisonDemo.compareToIgnoreScale(a, b) < 0);
    }

    @Test
    void compareToReturnsPositiveWhenGreater() {
        BigDecimal a = new BigDecimal("2.0");
        BigDecimal b = new BigDecimal("1.0");
        assertTrue(BigDecimalComparisonDemo.compareToIgnoreScale(a, b) > 0);
    }

    @Test
    void isEqualUsesCompareTo() {
        assertTrue(BigDecimalComparisonDemo.isEqual(
                new BigDecimal("1.0"), new BigDecimal("1.00")));
        assertFalse(BigDecimalComparisonDemo.isEqual(
                new BigDecimal("1.0"), new BigDecimal("2.0")));
    }

    @Test
    void isLessThan() {
        assertTrue(BigDecimalComparisonDemo.isLessThan(
                new BigDecimal("1"), new BigDecimal("2")));
        assertFalse(BigDecimalComparisonDemo.isLessThan(
                new BigDecimal("2"), new BigDecimal("1")));
        assertFalse(BigDecimalComparisonDemo.isLessThan(
                new BigDecimal("1"), new BigDecimal("1")));
    }

    @Test
    void isGreaterThan() {
        assertTrue(BigDecimalComparisonDemo.isGreaterThan(
                new BigDecimal("2"), new BigDecimal("1")));
        assertFalse(BigDecimalComparisonDemo.isGreaterThan(
                new BigDecimal("1"), new BigDecimal("2")));
    }

    @Test
    void signumForPositiveNegativeZero() {
        assertEquals(1, BigDecimalComparisonDemo.signum(new BigDecimal("5")));
        assertEquals(-1, BigDecimalComparisonDemo.signum(new BigDecimal("-5")));
        assertEquals(0, BigDecimalComparisonDemo.signum(new BigDecimal("0")));
    }

    @Test
    void isZero() {
        assertTrue(BigDecimalComparisonDemo.isZero(new BigDecimal("0")));
        assertTrue(BigDecimalComparisonDemo.isZero(new BigDecimal("0.00")));
        assertFalse(BigDecimalComparisonDemo.isZero(new BigDecimal("1")));
    }

    @Test
    void isPositive() {
        assertTrue(BigDecimalComparisonDemo.isPositive(new BigDecimal("1")));
        assertFalse(BigDecimalComparisonDemo.isPositive(new BigDecimal("0")));
        assertFalse(BigDecimalComparisonDemo.isPositive(new BigDecimal("-1")));
    }

    @Test
    void isNegative() {
        assertTrue(BigDecimalComparisonDemo.isNegative(new BigDecimal("-1")));
        assertFalse(BigDecimalComparisonDemo.isNegative(new BigDecimal("0")));
        assertFalse(BigDecimalComparisonDemo.isNegative(new BigDecimal("1")));
    }

    @Test
    void maxReturnsGreater() {
        assertEquals(new BigDecimal("5"),
                BigDecimalComparisonDemo.max(new BigDecimal("3"), new BigDecimal("5")));
    }

    @Test
    void minReturnsLesser() {
        assertEquals(new BigDecimal("3"),
                BigDecimalComparisonDemo.min(new BigDecimal("3"), new BigDecimal("5")));
    }

    @Test
    void maxMinHandleDifferentScales() {
        assertEquals(new BigDecimal("1.0"),
                BigDecimalComparisonDemo.max(new BigDecimal("1.0"), new BigDecimal("1.00")));
        assertEquals(new BigDecimal("1.0"),
                BigDecimalComparisonDemo.min(new BigDecimal("1.0"), new BigDecimal("1.00")));
    }
}
