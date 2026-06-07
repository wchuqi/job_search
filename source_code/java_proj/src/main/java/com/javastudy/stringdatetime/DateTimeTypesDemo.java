package com.javastudy.stringdatetime;

import java.time.*;
import java.time.temporal.ChronoUnit;

/**
 * Demonstrates the java.time API types (JSR-310, Java 8+).
 * <p>
 * Key types:
 * <ul>
 *   <li>Instant - a point on the timeline (UTC), nanosecond precision</li>
 *   <li>LocalDate - date without time or timezone (e.g., 2024-01-15)</li>
 *   <li>LocalTime - time without date or timezone (e.g., 14:30:00)</li>
 *   <li>LocalDateTime - date + time, no timezone</li>
 *   <li>ZonedDateTime - date + time + timezone (e.g., 2024-01-15T14:30+08:00[Asia/Shanghai])</li>
 *   <li>Duration - amount of time in seconds/nanos (for time-based)</li>
 *   <li>Period - amount of time in years/months/days (for date-based)</li>
 * </ul>
 */
public class DateTimeTypesDemo {

    /**
     * Instant represents a point on the UTC timeline.
     * Often used for timestamps, logging, and database storage.
     */
    public static Instant currentInstant() {
        return Instant.now();
    }

    /**
     * LocalDate is a date without time or timezone.
     * Useful for birthdays, holidays, business dates.
     */
    public static LocalDate today() {
        return LocalDate.now();
    }

    /**
     * Create a specific LocalDate.
     */
    public static LocalDate createDate(int year, int month, int dayOfMonth) {
        return LocalDate.of(year, month, dayOfMonth);
    }

    /**
     * LocalTime is a time without date or timezone.
     */
    public static LocalTime currentTime() {
        return LocalTime.now();
    }

    /**
     * Create a specific LocalTime.
     */
    public static LocalTime createTime(int hour, int minute, int second) {
        return LocalTime.of(hour, minute, second);
    }

    /**
     * LocalDateTime combines date and time, but no timezone.
     */
    public static LocalDateTime currentDateTime() {
        return LocalDateTime.now();
    }

    /**
     * Create a specific LocalDateTime.
     */
    public static LocalDateTime createDateTime(int year, int month, int day,
                                                 int hour, int minute, int second) {
        return LocalDateTime.of(year, month, day, hour, minute, second);
    }

    /**
     * ZonedDateTime includes timezone information.
     * This is the most complete date-time representation.
     */
    public static ZonedDateTime currentZonedDateTime(ZoneId zone) {
        return ZonedDateTime.now(zone);
    }

    /**
     * Convert LocalDateTime to ZonedDateTime by specifying a zone.
     */
    public static ZonedDateTime toZoned(LocalDateTime ldt, ZoneId zone) {
        return ldt.atZone(zone);
    }

    /**
     * Duration measures time in seconds and nanoseconds.
     * Best for Instants and LocalTimes.
     */
    public static Duration durationBetween(Instant start, Instant end) {
        return Duration.between(start, end);
    }

    /**
     * Create a Duration from hours/minutes/seconds.
     */
    public static Duration createDuration(long hours, long minutes, long seconds) {
        return Duration.ofHours(hours).plusMinutes(minutes).plusSeconds(seconds);
    }

    /**
     * Period measures time in years, months, days.
     * Best for LocalDates.
     */
    public static Period periodBetween(LocalDate start, LocalDate end) {
        return Period.between(start, end);
    }

    /**
     * Create a Period from years/months/days.
     */
    public static Period createPeriod(int years, int months, int days) {
        return Period.of(years, months, days);
    }

    /**
     * Convert between types: LocalDateTime to Instant via a zone.
     */
    public static Instant toInstant(LocalDateTime ldt, ZoneId zone) {
        return ldt.atZone(zone).toInstant();
    }

    /**
     * Convert Instant to LocalDateTime in a specific zone.
     */
    public static LocalDateTime toLocalDateTime(Instant instant, ZoneId zone) {
        return LocalDateTime.ofInstant(instant, zone);
    }

    /**
     * ChronoUnit provides precise unit-based arithmetic.
     */
    public static long daysBetween(LocalDate start, LocalDate end) {
        return ChronoUnit.DAYS.between(start, end);
    }
}
