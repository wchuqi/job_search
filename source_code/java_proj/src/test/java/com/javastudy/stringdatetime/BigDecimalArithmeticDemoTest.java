package com.javastudy.stringdatetime;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.math.RoundingMode;

import static org.junit.jupiter.api.Assertions.*;

class BigDecimalArithmeticDemoTest {

    @Test
    void addTwoDecimals() {
        BigDecimal result = BigDecimalArithmeticDemo.add(
                new BigDecimal("1.1"), new BigDecimal("2.2"));
        assertEquals(new BigDecimal("3.3"), result);
    }

    @Test
    void subtractDecimals() {
        BigDecimal result = BigDecimalArithmeticDemo.subtract(
                new BigDecimal("5.5"), new BigDecimal("2.2"));
        assertEquals(new BigDecimal("3.3"), result);
    }

    @Test
    void multiplyDecimals() {
        BigDecimal result = BigDecimalArithmeticDemo.multiply(
                new BigDecimal("2.5"), new BigDecimal("4"));
        assertEquals(new BigDecimal("10.0"), result);
    }

    @Test
    void multiplyScaleIsAdditive() {
        BigDecimal result = BigDecimalArithmeticDemo.multiply(
                new BigDecimal("1.23"), new BigDecimal("4.56"));
        // scale(2) + scale(2) = scale(4)
        assertEquals(4, result.scale());
        assertEquals(new BigDecimal("5.6088"), result);
    }

    @Test
    void divideWithScaleAndRounding() {
        BigDecimal result = BigDecimalArithmeticDemo.divide(
                new BigDecimal("10"), new BigDecimal("3"), 2, RoundingMode.HALF_UP);
        assertEquals(new BigDecimal("3.33"), result);
    }

    @Test
    void divideExactThrowsForNonTerminating() {
        assertThrows(ArithmeticException.class, () -> {
            BigDecimalArithmeticDemo.divideExact(new BigDecimal("10"), new BigDecimal("3"));
        });
    }

    @Test
    void divideExactWorksForExactDivision() {
        BigDecimal result = BigDecimalArithmeticDemo.divideExact(
                new BigDecimal("10"), new BigDecimal("2"));
        assertEquals(new BigDecimal("5"), result);
    }

    @Test
    void setScaleRoundsCorrectly() {
        BigDecimal result = BigDecimalArithmeticDemo.setScale(
                new BigDecimal("3.456"), 2, RoundingMode.HALF_UP);
        assertEquals(new BigDecimal("3.46"), result);
    }

    @Test
    void roundHalfUp() {
        assertEquals(new BigDecimal("3.46"),
                BigDecimalArithmeticDemo.roundHalfUp(new BigDecimal("3.455"), 2));
        assertEquals(new BigDecimal("3.45"),
                BigDecimalArithmeticDemo.roundHalfUp(new BigDecimal("3.454"), 2));
    }

    @Test
    void roundHalfEvenBankersRounding() {
        // 2.5 rounds to 2 (even) with HALF_EVEN
        assertEquals(new BigDecimal("2"),
                BigDecimalArithmeticDemo.roundHalfEven(new BigDecimal("2.5"), 0));
        // 3.5 rounds to 4 (even) with HALF_EVEN
        assertEquals(new BigDecimal("4"),
                BigDecimalArithmeticDemo.roundHalfEven(new BigDecimal("3.5"), 0));
    }

    @Test
    void floorRoundsDown() {
        assertEquals(new BigDecimal("3.45"),
                BigDecimalArithmeticDemo.floor(new BigDecimal("3.459"), 2));
        assertEquals(new BigDecimal("-3.46"),
                BigDecimalArithmeticDemo.floor(new BigDecimal("-3.451"), 2));
    }

    @Test
    void ceilRoundsUp() {
        assertEquals(new BigDecimal("3.46"),
                BigDecimalArithmeticDemo.ceil(new BigDecimal("3.451"), 2));
    }

    @Test
    void absReturnsAbsoluteValue() {
        assertEquals(new BigDecimal("5"),
                BigDecimalArithmeticDemo.abs(new BigDecimal("-5")));
        assertEquals(new BigDecimal("5"),
                BigDecimalArithmeticDemo.abs(new BigDecimal("5")));
    }

    @Test
    void negateChangesSign() {
        assertEquals(new BigDecimal("-5"),
                BigDecimalArithmeticDemo.negate(new BigDecimal("5")));
        assertEquals(new BigDecimal("5"),
                BigDecimalArithmeticDemo.negate(new BigDecimal("-5")));
    }

    @Test
    void powRaisesToPower() {
        BigDecimal result = BigDecimalArithmeticDemo.pow(new BigDecimal("2"), 10);
        assertEquals(new BigDecimal("1024"), result);
    }

    @Test
    void chainedCalculationCorrect() {
        // (10 + 5) * 3 / 4 = 15 * 3 / 4 = 45 / 4 = 11.25
        BigDecimal result = BigDecimalArithmeticDemo.chainedCalculation(
                new BigDecimal("10"), new BigDecimal("5"),
                new BigDecimal("3"), new BigDecimal("4"),
                2, RoundingMode.HALF_UP);
        assertEquals(new BigDecimal("11.25"), result);
    }
}
