package com.javastudy.stringdatetime;

import java.math.BigDecimal;

/**
 * Demonstrates BigDecimal comparison pitfalls and correct approaches.
 * <p>
 * Key points:
 * <ul>
 *   <li>equals() considers scale: new BigDecimal("1.0").equals(new BigDecimal("1.00")) is FALSE</li>
 *   <li>compareTo() ignores scale: new BigDecimal("1.0").compareTo(new BigDecimal("1.00")) returns 0</li>
 *   <li>Always use compareTo() for value comparison</li>
 *   <li>signum() returns -1, 0, or 1 for negative, zero, positive</li>
 * </ul>
 */
public class BigDecimalComparisonDemo {

    /**
     * equals() compares value AND scale.
     * "1.0" and "1.00" have different scales, so equals() returns false.
     */
    public static boolean equalsWithScale(BigDecimal a, BigDecimal b) {
        return a.equals(b);
    }

    /**
     * compareTo() compares value only, ignoring scale.
     * "1.0" and "1.00" are equal by compareTo().
     * Returns: negative if a < b, 0 if a == b, positive if a > b.
     */
    public static int compareToIgnoreScale(BigDecimal a, BigDecimal b) {
        return a.compareTo(b);
    }

    /**
     * Check equality using compareTo (recommended).
     */
    public static boolean isEqual(BigDecimal a, BigDecimal b) {
        return a.compareTo(b) == 0;
    }

    /**
     * Check if a < b.
     */
    public static boolean isLessThan(BigDecimal a, BigDecimal b) {
        return a.compareTo(b) < 0;
    }

    /**
     * Check if a > b.
     */
    public static boolean isGreaterThan(BigDecimal a, BigDecimal b) {
        return a.compareTo(b) > 0;
    }

    /**
     * Check if a <= b.
     */
    public static boolean isLessOrEqual(BigDecimal a, BigDecimal b) {
        return a.compareTo(b) <= 0;
    }

    /**
     * Check if a >= b.
     */
    public static boolean isGreaterOrEqual(BigDecimal a, BigDecimal b) {
        return a.compareTo(b) >= 0;
    }

    /**
     * signum(): -1 for negative, 0 for zero, 1 for positive.
     */
    public static int signum(BigDecimal value) {
        return value.signum();
    }

    /**
     * Check if value is zero.
     */
    public static boolean isZero(BigDecimal value) {
        return value.signum() == 0;
    }

    /**
     * Check if value is positive (> 0).
     */
    public static boolean isPositive(BigDecimal value) {
        return value.signum() > 0;
    }

    /**
     * Check if value is negative (< 0).
     */
    public static boolean isNegative(BigDecimal value) {
        return value.signum() < 0;
    }

    /**
     * max() returns the greater of two BigDecimals.
     */
    public static BigDecimal max(BigDecimal a, BigDecimal b) {
        return a.max(b);
    }

    /**
     * min() returns the lesser of two BigDecimals.
     */
    public static BigDecimal min(BigDecimal a, BigDecimal b) {
        return a.min(b);
    }
}
