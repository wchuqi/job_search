package com.javastudy.stringdatetime;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;

/**
 * Demonstrates BigDecimal arithmetic operations.
 * <p>
 * Key points:
 * <ul>
 *   <li>add(), subtract(), multiply() - no scale/rounding issues</li>
 *   <li>divide() - MUST specify scale and rounding mode to avoid ArithmeticException</li>
 *   <li>setScale() - change the scale (decimal places) with rounding</li>
 *   <li>RoundingMode - HALF_UP (school rounding), HALF_EVEN (banker's rounding)</li>
 *   <li>All operations return new BigDecimal instances (immutable)</li>
 * </ul>
 */
public class BigDecimalArithmeticDemo {

    /**
     * Add two BigDecimals.
     */
    public static BigDecimal add(BigDecimal a, BigDecimal b) {
        return a.add(b);
    }

    /**
     * Subtract b from a.
     */
    public static BigDecimal subtract(BigDecimal a, BigDecimal b) {
        return a.subtract(b);
    }

    /**
     * Multiply two BigDecimals.
     * Scale of result = scale(a) + scale(b).
     */
    public static BigDecimal multiply(BigDecimal a, BigDecimal b) {
        return a.multiply(b);
    }

    /**
     * Divide with explicit scale and rounding mode.
     * Without scale/rounding, divide() throws ArithmeticException for non-terminating decimals.
     */
    public static BigDecimal divide(BigDecimal a, BigDecimal b, int scale, RoundingMode roundingMode) {
        return a.divide(b, scale, roundingMode);
    }

    /**
     * Divide that throws ArithmeticException if result has infinite decimal expansion.
     * Use this only when you know the division is exact.
     */
    public static BigDecimal divideExact(BigDecimal a, BigDecimal b) {
        return a.divide(b);
    }

    /**
     * Set scale with a specific rounding mode.
     */
    public static BigDecimal setScale(BigDecimal value, int scale, RoundingMode roundingMode) {
        return value.setScale(scale, roundingMode);
    }

    /**
     * Demonstrates HALF_UP rounding (school rounding: 0.5 rounds up).
     */
    public static BigDecimal roundHalfUp(BigDecimal value, int scale) {
        return value.setScale(scale, RoundingMode.HALF_UP);
    }

    /**
     * Demonstrates HALF_EVEN rounding (banker's rounding: round to nearest even).
     * This is the default for IEEE 754 floating point.
     */
    public static BigDecimal roundHalfEven(BigDecimal value, int scale) {
        return value.setScale(scale, RoundingMode.HALF_EVEN);
    }

    /**
     * Demonstrates floor (round toward negative infinity).
     */
    public static BigDecimal floor(BigDecimal value, int scale) {
        return value.setScale(scale, RoundingMode.FLOOR);
    }

    /**
     * Demonstrates ceiling (round toward positive infinity).
     */
    public static BigDecimal ceil(BigDecimal value, int scale) {
        return value.setScale(scale, RoundingMode.CEILING);
    }

    /**
     * abs() - absolute value.
     */
    public static BigDecimal abs(BigDecimal value) {
        return value.abs();
    }

    /**
     * negate() - change sign.
     */
    public static BigDecimal negate(BigDecimal value) {
        return value.negate();
    }

    /**
     * pow() - raise to a power.
     */
    public static BigDecimal pow(BigDecimal base, int exponent) {
        return base.pow(exponent);
    }

    /**
     * Chained calculation: (a + b) * c / d with rounding.
     */
    public static BigDecimal chainedCalculation(BigDecimal a, BigDecimal b,
                                                 BigDecimal c, BigDecimal d,
                                                 int scale, RoundingMode roundingMode) {
        return a.add(b).multiply(c).divide(d, scale, roundingMode);
    }
}
