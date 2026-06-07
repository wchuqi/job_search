package com.javastudy.stringdatetime;

import java.time.*;
import java.time.zone.ZoneRules;

/**
 * Demonstrates Instant and ZonedDateTime for timezone-aware date-time handling.
 * <p>
 * Key points:
 * <ul>
 *   <li>Instant.now() captures the current moment on the UTC timeline</li>
 *   <li>ZonedDateTime wraps a LocalDateTime with a ZoneId</li>
 *   <li>atZone() converts an Instant to a ZonedDateTime</li>
 *   <li>toInstant() converts a ZonedDateTime back to an Instant</li>
 *   <li>ZoneId.of("Asia/Shanghai") - IANA timezone identifiers</li>
 *   <li>DST transitions are handled automatically</li>
 * </ul>
 */
public class InstantZonedDateTimeDemo {

    /**
     * Get the current Instant (UTC timeline point).
     */
    public static Instant nowInstant() {
        return Instant.now();
    }

    /**
     * Convert an Instant to a ZonedDateTime in a specific timezone.
     */
    public static ZonedDateTime instantToZoned(Instant instant, ZoneId zone) {
        return instant.atZone(zone);
    }

    /**
     * Convert a ZonedDateTime to an Instant.
     */
    public static Instant zonedToInstant(ZonedDateTime zdt) {
        return zdt.toInstant();
    }

    /**
     * Get the current time in a specific timezone.
     */
    public static ZonedDateTime nowInZone(ZoneId zone) {
        return ZonedDateTime.now(zone);
    }

    /**
     * Convert between timezones.
     * A ZonedDateTime can be "withZoneSameInstant" to keep the same moment,
     * or "withZoneSameLocal" to keep the same local time.
     */
    public static ZonedDateTime convertTimezone(ZonedDateTime zdt, ZoneId targetZone) {
        return zdt.withZoneSameInstant(targetZone);
    }

    /**
     * Get the offset from UTC for a timezone at a given instant.
     */
    public static ZoneOffset getOffset(ZoneId zone, Instant instant) {
        return zone.getRules().getOffset(instant);
    }

    /**
     * Check if a timezone uses Daylight Saving Time at a given instant.
     */
    public static boolean isDaylightSavings(ZoneId zone, Instant instant) {
        ZoneRules rules = zone.getRules();
        return rules.isDaylightSavings(instant);
    }

    /**
     * Create a ZonedDateTime from components.
     */
    public static ZonedDateTime createZoned(int year, int month, int day,
                                              int hour, int minute, int second,
                                              ZoneId zone) {
        return ZonedDateTime.of(year, month, day, hour, minute, second, 0, zone);
    }

    /**
     * Instant arithmetic: plus seconds.
     */
    public static Instant addSeconds(Instant instant, long seconds) {
        return instant.plusSeconds(seconds);
    }

    /**
     * Instant arithmetic: duration between two instants.
     */
    public static Duration durationBetween(Instant start, Instant end) {
        return Duration.between(start, end);
    }

    /**
     * ZonedDateTime arithmetic: plus days (handles DST correctly).
     */
    public static ZonedDateTime plusDays(ZonedDateTime zdt, long days) {
        return zdt.plusDays(days);
    }

    /**
     * ZonedDateTime arithmetic: plus hours.
     */
    public static ZonedDateTime plusHours(ZonedDateTime zdt, long hours) {
        return zdt.plusHours(hours);
    }

    /**
     * Get available zone IDs count (demonstrates ZoneId.getAvailableZoneIds).
     */
    public static int availableZoneCount() {
        return ZoneId.getAvailableZoneIds().size();
    }
}
