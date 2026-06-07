package com.javastudy.stringdatetime;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.Month;
import java.time.temporal.TemporalAdjusters;

/**
 * Demonstrates LocalDate operations and date arithmetic.
 * <p>
 * Key points:
 * <ul>
 *   <li>LocalDate is immutable - all "modification" methods return new instances</li>
 *   <li>now() - current date from system clock</li>
 *   <li>plusDays(), plusWeeks(), plusMonths(), plusYears() - add to date</li>
 *   <li>minusDays(), minusWeeks() etc. - subtract from date</li>
 *   <li>with() - adjust to a specific value (e.g., first day of month)</li>
 *   <li>TemporalAdjusters provides common adjustments</li>
 * </ul>
 */
public class LocalDateDemo {

    /**
     * Get today's date.
     */
    public static LocalDate today() {
        return LocalDate.now();
    }

    /**
     * Create a specific date.
     */
    public static LocalDate of(int year, int month, int dayOfMonth) {
        return LocalDate.of(year, month, dayOfMonth);
    }

    /**
     * Parse an ISO date string (yyyy-MM-dd).
     */
    public static LocalDate parse(String text) {
        return LocalDate.parse(text);
    }

    /**
     * Add weeks to a date.
     */
    public static LocalDate plusWeeks(LocalDate date, long weeks) {
        return date.plusWeeks(weeks);
    }

    /**
     * Add days to a date.
     */
    public static LocalDate plusDays(LocalDate date, long days) {
        return date.plusDays(days);
    }

    /**
     * Subtract days from a date.
     */
    public static LocalDate minusDays(LocalDate date, long days) {
        return date.minusDays(days);
    }

    /**
     * Add months to a date.
     */
    public static LocalDate plusMonths(LocalDate date, long months) {
        return date.plusMonths(months);
    }

    /**
     * Add years to a date.
     */
    public static LocalDate plusYears(LocalDate date, long years) {
        return date.plusYears(years);
    }

    /**
     * Get the day of the week.
     */
    public static DayOfWeek dayOfWeek(LocalDate date) {
        return date.getDayOfWeek();
    }

    /**
     * Get the day of the year (1-366).
     */
    public static int dayOfYear(LocalDate date) {
        return date.getDayOfYear();
    }

    /**
     * Check if a year is a leap year.
     */
    public static boolean isLeapYear(LocalDate date) {
        return date.isLeapYear();
    }

    /**
     * Get the first day of the month.
     */
    public static LocalDate firstDayOfMonth(LocalDate date) {
        return date.with(TemporalAdjusters.firstDayOfMonth());
    }

    /**
     * Get the last day of the month.
     */
    public static LocalDate lastDayOfMonth(LocalDate date) {
        return date.with(TemporalAdjusters.lastDayOfMonth());
    }

    /**
     * Get the next occurrence of a specific day of the week.
     */
    public static LocalDate nextDayOfWeek(LocalDate date, DayOfWeek dayOfWeek) {
        return date.with(TemporalAdjusters.next(dayOfWeek));
    }

    /**
     * Get the first day of the next month.
     */
    public static LocalDate firstDayOfNextMonth(LocalDate date) {
        return date.with(TemporalAdjusters.firstDayOfNextMonth());
    }

    /**
     * Check if a date is before another date.
     */
    public static boolean isBefore(LocalDate a, LocalDate b) {
        return a.isBefore(b);
    }

    /**
     * Check if a date is after another date.
     */
    public static boolean isAfter(LocalDate a, LocalDate b) {
        return a.isAfter(b);
    }

    /**
     * Get the length of the month (28, 29, 30, or 31).
     */
    public static int lengthOfMonth(LocalDate date) {
        return date.lengthOfMonth();
    }

    /**
     * Get the month enum value.
     */
    public static Month getMonth(LocalDate date) {
        return date.getMonth();
    }
}
