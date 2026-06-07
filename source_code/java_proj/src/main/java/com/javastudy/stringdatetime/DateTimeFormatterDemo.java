package com.javastudy.stringdatetime;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

/**
 * Demonstrates DateTimeFormatter for parsing and formatting date-times.
 * <p>
 * Key points:
 * <ul>
 *   <li>DateTimeFormatter.ofPattern() - create a formatter from a pattern string</li>
 *   <li>parse() - convert a string to a date/time object</li>
 *   <li>format() - convert a date/time object to a string</li>
 *   <li>yyyy vs YYYY pitfall: yyyy = calendar year, YYYY = week-based year</li>
 *   <li>MM = month, dd = day, HH = 24h hour, mm = minute, ss = second</li>
 *   <li>Predefined formatters: ISO_LOCAL_DATE, ISO_DATE_TIME, etc.</li>
 * </ul>
 */
public class DateTimeFormatterDemo {

    /**
     * Format a LocalDate using a pattern.
     */
    public static String formatDate(LocalDate date, String pattern) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);
        return date.format(formatter);
    }

    /**
     * Format a LocalDateTime using a pattern.
     */
    public static String formatDateTime(LocalDateTime dateTime, String pattern) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);
        return dateTime.format(formatter);
    }

    /**
     * Parse a date string into a LocalDate.
     */
    public static LocalDate parseDate(String text, String pattern) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);
        return LocalDate.parse(text, formatter);
    }

    /**
     * Parse a date-time string into a LocalDateTime.
     */
    public static LocalDateTime parseDateTime(String text, String pattern) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);
        return LocalDateTime.parse(text, formatter);
    }

    /**
     * Demonstrates the yyyy vs YYYY pitfall.
     * <p>
     * yyyy = calendar year (what you usually want)
     * YYYY = week-based year (ISO 8601) - can differ around New Year!
     * <p>
     * Example: 2024-12-29 is in week 1 of 2025 in ISO 8601,
     * so YYYY would give 2025 but yyyy gives 2024.
     *
     * @return true if the two formatters produce the same result for the given date
     */
    public static boolean yyyyVsYYYY(LocalDate date) {
        String calendarYear = date.format(DateTimeFormatter.ofPattern("yyyy"));
        String weekBasedYear = date.format(DateTimeFormatter.ofPattern("YYYY"));
        return calendarYear.equals(weekBasedYear);
    }

    /**
     * Get the calendar year formatted with yyyy.
     */
    public static String calendarYear(LocalDate date) {
        return date.format(DateTimeFormatter.ofPattern("yyyy"));
    }

    /**
     * Get the week-based year formatted with YYYY.
     */
    public static String weekBasedYear(LocalDate date) {
        return date.format(DateTimeFormatter.ofPattern("YYYY"));
    }

    /**
     * Use predefined ISO formatters.
     */
    public static String formatISO(LocalDate date) {
        return date.format(DateTimeFormatter.ISO_LOCAL_DATE);
    }

    /**
     * Format with localized date style.
     */
    public static String formatLocalized(LocalDateTime dateTime) {
        return dateTime.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
    }

    /**
     * Parse and reformat: convert from one pattern to another.
     */
    public static String reformat(String input, String inputPattern, String outputPattern) {
        DateTimeFormatter inputFormatter = DateTimeFormatter.ofPattern(inputPattern);
        DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern(outputPattern);
        LocalDate date = LocalDate.parse(input, inputFormatter);
        return date.format(outputFormatter);
    }

    /**
     * Try-parse pattern: handle parse errors gracefully.
     */
    public static LocalDate tryParse(String text, String pattern) {
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);
            return LocalDate.parse(text, formatter);
        } catch (DateTimeParseException e) {
            return null;
        }
    }

    /**
     * Format a ZonedDateTime.
     */
    public static String formatZoned(ZonedDateTime zdt, String pattern) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);
        return zdt.format(formatter);
    }
}
