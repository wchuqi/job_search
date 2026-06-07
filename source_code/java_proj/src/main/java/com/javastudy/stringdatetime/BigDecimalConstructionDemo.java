package com.javastudy.stringdatetime;

import java.math.BigDecimal;

/**
 * Demonstrates BigDecimal construction methods and their pitfalls.
 * <p>
 * Key points:
 * <ul>
 *   <li>new BigDecimal(String) - exact representation, preferred</li>
 *   <li>new BigDecimal(double) - INEXACT, captures floating-point representation</li>
 *   <li>BigDecimal.valueOf(double) - uses Double.toString() internally, safe</li>
 *   <li>BigDecimal.valueOf(long) - exact for integers</li>
 *   <li>Always use String constructor or valueOf() to avoid precision surprises</li>
 * </ul>
 */
public class BigDecimalConstructionDemo {

    /**
     * NEW BigDecimal(String) - exact representation.
     * "0.1" is stored as exactly 0.1.
     */
    public static BigDecimal fromString(String value) {
        return new BigDecimal(value);
    }

    /**
     * NEW BigDecimal(double) - INEXACT!
     * 0.1 in double is actually 0.1000000000000000055511151231257827021181583404541015625
     * This is almost never what you want.
     */
    public static BigDecimal fromDouble(double value) {
        return new BigDecimal(value);
    }

    /**
     * BigDecimal.valueOf(double) - safe conversion.
     * Internally uses Double.toString(double), which gives a clean representation.
     */
    public static BigDecimal valueOfDouble(double value) {
        return BigDecimal.valueOf(value);
    }

    /**
     * BigDecimal.valueOf(long) - exact for integer values.
     */
    public static BigDecimal valueOfLong(long value) {
        return BigDecimal.valueOf(value);
    }

    /**
     * Demonstrates the precision difference between fromDouble and fromString.
     * new BigDecimal(0.1) != new BigDecimal("0.1")
     */
    public static boolean doubleVsStringPrecision() {
        BigDecimal fromDouble = new BigDecimal(0.1);
        BigDecimal fromString = new BigDecimal("0.1");
        return fromDouble.equals(fromString);
    }

    /**
     * BigDecimal.valueOf(double) produces the same result as new BigDecimal(String).
     */
    public static boolean valueOfVsStringConstructor(double value) {
        BigDecimal viaValueOf = BigDecimal.valueOf(value);
        BigDecimal viaString = new BigDecimal(Double.toString(value));
        return viaValueOf.equals(viaString);
    }

    /**
     * BigDecimal.ZERO, ONE, TEN are convenient constants.
     */
    public static BigDecimal zero() {
        return BigDecimal.ZERO;
    }

    public static BigDecimal one() {
        return BigDecimal.ONE;
    }

    public static BigDecimal ten() {
        return BigDecimal.TEN;
    }

    /**
     * Demonstrates scale (number of digits after decimal point).
     */
    public static int getScale(BigDecimal value) {
        return value.scale();
    }

    /**
     * Demonstrates precision (total number of significant digits).
     */
    public static int getPrecision(BigDecimal value) {
        return value.precision();
    }

    /**
     * stripTrailingZeros() removes unnecessary trailing zeros.
     * Note: "0.10" becomes "0.1" (scale changes).
     */
    public static BigDecimal stripZeros(BigDecimal value) {
        return value.stripTrailingZeros();
    }
}
