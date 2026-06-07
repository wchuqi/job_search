package com.javastudy.stringdatetime;

import java.util.Locale;

/**
 * Demonstrates string formatting in Java.
 * <p>
 * Key points:
 * <ul>
 *   <li>String.format() - static method, printf-style formatting</li>
 *   <li>String.formatted() - instance method (Java 15+), more fluent</li>
 *   <li>%s - string, %d - integer, %f - floating point, %.2f - 2 decimal places</li>
 *   <li>%n - platform line separator, %t - date/time</li>
 *   <li>Argument index: %1$s, %2$d for reordering</li>
 * </ul>
 */
public class StringFormattingDemo {

    /**
     * Basic String.format() with %s (string), %d (integer), %f (float).
     */
    public static String basicFormat(String name, int age, double score) {
        return String.format("Name: %s, Age: %d, Score: %.2f", name, age, score);
    }

    /**
     * String.formatted() instance method (Java 15+) - same as String.format() but fluent.
     */
    public static String fluentFormat(String name, int age, double score) {
        return "Name: %s, Age: %d, Score: %.2f".formatted(name, age, score);
    }

    /**
     * Formatting with precision: %.2f for 2 decimal places.
     */
    public static String formatDecimal(double value, int decimals) {
        return String.format("%." + decimals + "f", value);
    }

    /**
     * Padding and alignment: %-10s (left-align, 10 chars), %10s (right-align).
     */
    public static String formatWithPadding(String name, int value) {
        return String.format("|%-10s|%10d|", name, value);
    }

    /**
     * Argument index reordering: %1$s, %2$d.
     */
    public static String formatWithArgIndex(String name, int count) {
        return String.format("%1$s has %2$d items; %2$d items belong to %1$s", name, count);
    }

    /**
     * Integer formatting: %d, %x (hex), %o (octal), %b (boolean).
     */
    public static String formatIntegers(int value) {
        return String.format("decimal=%d, hex=%x, octal=%o", value, value, value);
    }

    /**
     * Line separator with %n (platform-independent).
     */
    public static String formatWithNewline(String line1, String line2) {
        return String.format("%s%n%s", line1, line2);
    }

    /**
     * Formatting a percentage.
     */
    public static String formatPercentage(double ratio) {
        return String.format("Completion: %.1f%%", ratio * 100);
    }

    /**
     * Locale-aware formatting.
     */
    public static String formatWithLocale(Locale locale, double amount) {
        return String.format(locale, "Amount: %,.2f", amount);
    }

    /**
     * Zero-padding integers.
     */
    public static String formatZeroPadded(int value, int width) {
        return String.format("%0" + width + "d", value);
    }
}
