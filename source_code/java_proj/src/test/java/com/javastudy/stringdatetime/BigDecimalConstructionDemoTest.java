package com.javastudy.stringdatetime;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class BigDecimalConstructionDemoTest {

    @Test
    void fromStringIsExact() {
        BigDecimal bd = BigDecimalConstructionDemo.fromString("0.1");
        assertEquals("0.1", bd.toPlainString());
        assertEquals(1, bd.scale());
    }

    @Test
    void fromDoubleIsInexact() {
        BigDecimal bd = BigDecimalConstructionDemo.fromDouble(0.1);
        // 0.1 in double is not exactly 0.1
        assertNotEquals(new BigDecimal("0.1"), bd);
        // The scale is much larger than 1
        assertTrue(bd.scale() > 1,
                "fromDouble(0.1) should have high scale due to floating-point representation");
    }

    @Test
    void valueOfDoubleIsExact() {
        BigDecimal bd = BigDecimalConstructionDemo.valueOfDouble(0.1);
        assertEquals(new BigDecimal("0.1"), bd);
    }

    @Test
    void valueOfLongIsExact() {
        BigDecimal bd = BigDecimalConstructionDemo.valueOfLong(12345);
        assertEquals(new BigDecimal("12345"), bd);
        assertEquals(0, bd.scale());
    }

    @Test
    void doubleVsStringPrecisionDiffers() {
        assertFalse(BigDecimalConstructionDemo.doubleVsStringPrecision(),
                "new BigDecimal(0.1) should NOT equal new BigDecimal(\"0.1\")");
    }

    @Test
    void valueOfMatchesStringConstructor() {
        assertTrue(BigDecimalConstructionDemo.valueOfVsStringConstructor(0.1));
        assertTrue(BigDecimalConstructionDemo.valueOfVsStringConstructor(1.5));
        assertTrue(BigDecimalConstructionDemo.valueOfVsStringConstructor(100.0));
    }

    @Test
    void zeroConstant() {
        assertEquals(0, BigDecimalConstructionDemo.zero().signum());
        assertEquals("0", BigDecimalConstructionDemo.zero().toPlainString());
    }

    @Test
    void oneConstant() {
        assertEquals(1, BigDecimalConstructionDemo.one().intValue());
    }

    @Test
    void tenConstant() {
        assertEquals(10, BigDecimalConstructionDemo.ten().intValue());
    }

    @Test
    void getScaleReturnsDecimalPlaces() {
        assertEquals(2, BigDecimalConstructionDemo.getScale(new BigDecimal("1.23")));
        assertEquals(0, BigDecimalConstructionDemo.getScale(new BigDecimal("100")));
        assertEquals(5, BigDecimalConstructionDemo.getScale(new BigDecimal("1.00000")));
    }

    @Test
    void getPrecisionReturnsTotalDigits() {
        assertEquals(3, BigDecimalConstructionDemo.getPrecision(new BigDecimal("1.23")));
        assertEquals(1, BigDecimalConstructionDemo.getPrecision(new BigDecimal("5")));
        assertEquals(4, BigDecimalConstructionDemo.getPrecision(new BigDecimal("1234")));
    }

    @Test
    void stripTrailingZerosRemovesZeros() {
        BigDecimal stripped = BigDecimalConstructionDemo.stripZeros(new BigDecimal("1.00"));
        assertEquals(new BigDecimal("1"), stripped);
        assertEquals(0, stripped.scale());
    }

    @Test
    void stripTrailingZerosPreservesNonZero() {
        BigDecimal stripped = BigDecimalConstructionDemo.stripZeros(new BigDecimal("1.50"));
        assertEquals(new BigDecimal("1.5"), stripped);
    }
}
