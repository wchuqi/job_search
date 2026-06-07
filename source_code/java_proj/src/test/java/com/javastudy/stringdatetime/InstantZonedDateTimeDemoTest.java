package com.javastudy.stringdatetime;

import org.junit.jupiter.api.Test;

import java.time.*;

import static org.junit.jupiter.api.Assertions.*;

class InstantZonedDateTimeDemoTest {

    @Test
    void nowInstantIsNotNull() {
        assertNotNull(InstantZonedDateTimeDemo.nowInstant());
    }

    @Test
    void instantToZonedConvertsCorrectly() {
        Instant instant = Instant.parse("2024-06-15T12:00:00Z");
        ZoneId shanghai = ZoneId.of("Asia/Shanghai");
        ZonedDateTime zdt = InstantZonedDateTimeDemo.instantToZoned(instant, shanghai);
        assertEquals(20, zdt.getHour()); // UTC+8
        assertEquals(shanghai, zdt.getZone());
    }

    @Test
    void zonedToInstantConvertsBack() {
        ZonedDateTime zdt = ZonedDateTime.of(2024, 6, 15, 20, 0, 0, 0, ZoneId.of("Asia/Shanghai"));
        Instant instant = InstantZonedDateTimeDemo.zonedToInstant(zdt);
        assertEquals(12, instant.atZone(ZoneId.of("UTC")).getHour());
    }

    @Test
    void roundTripConversionPreservesInstant() {
        Instant original = Instant.parse("2024-06-15T12:00:00Z");
        ZoneId tokyo = ZoneId.of("Asia/Tokyo");
        ZonedDateTime zdt = InstantZonedDateTimeDemo.instantToZoned(original, tokyo);
        Instant roundTrip = InstantZonedDateTimeDemo.zonedToInstant(zdt);
        assertEquals(original, roundTrip);
    }

    @Test
    void nowInZoneReturnsCorrectZone() {
        ZoneId london = ZoneId.of("Europe/London");
        ZonedDateTime zdt = InstantZonedDateTimeDemo.nowInZone(london);
        assertEquals(london, zdt.getZone());
    }

    @Test
    void convertTimezonePreservesInstant() {
        ZonedDateTime shanghaiTime = ZonedDateTime.of(
                2024, 6, 15, 20, 0, 0, 0, ZoneId.of("Asia/Shanghai"));
        ZonedDateTime tokyoTime = InstantZonedDateTimeDemo.convertTimezone(
                shanghaiTime, ZoneId.of("Asia/Tokyo"));
        // Same instant, different local time (1 hour difference)
        assertEquals(21, tokyoTime.getHour());
        assertEquals(shanghaiTime.toInstant(), tokyoTime.toInstant());
    }

    @Test
    void getOffsetReturnsCorrectOffset() {
        Instant summer = Instant.parse("2024-06-15T12:00:00Z");
        ZoneOffset offset = InstantZonedDateTimeDemo.getOffset(ZoneId.of("Asia/Shanghai"), summer);
        assertEquals(ZoneOffset.ofHours(8), offset);
    }

    @Test
    void createZonedWithComponents() {
        ZonedDateTime zdt = InstantZonedDateTimeDemo.createZoned(
                2024, 6, 15, 14, 30, 0, ZoneId.of("Asia/Shanghai"));
        assertEquals(2024, zdt.getYear());
        assertEquals(6, zdt.getMonthValue());
        assertEquals(15, zdt.getDayOfMonth());
        assertEquals(14, zdt.getHour());
        assertEquals(30, zdt.getMinute());
    }

    @Test
    void addSecondsToInstant() {
        Instant instant = Instant.parse("2024-06-15T12:00:00Z");
        Instant result = InstantZonedDateTimeDemo.addSeconds(instant, 3600);
        assertEquals(Instant.parse("2024-06-15T13:00:00Z"), result);
    }

    @Test
    void durationBetweenInstants() {
        Instant start = Instant.parse("2024-06-15T12:00:00Z");
        Instant end = Instant.parse("2024-06-15T14:30:00Z");
        Duration duration = InstantZonedDateTimeDemo.durationBetween(start, end);
        assertEquals(2, duration.toHours());
        assertEquals(30, duration.toMinutesPart());
    }

    @Test
    void plusDaysHandlesDSTCorrectly() {
        // In zones with DST, adding days should keep the same local time
        ZonedDateTime before = ZonedDateTime.of(
                2024, 3, 9, 14, 0, 0, 0, ZoneId.of("America/New_York"));
        ZonedDateTime after = InstantZonedDateTimeDemo.plusDays(before, 1);
        assertEquals(14, after.getHour(), "Local time should be preserved across DST");
    }

    @Test
    void plusHoursToZonedDateTime() {
        ZonedDateTime zdt = ZonedDateTime.of(
                2024, 6, 15, 10, 0, 0, 0, ZoneId.of("UTC"));
        ZonedDateTime result = InstantZonedDateTimeDemo.plusHours(zdt, 5);
        assertEquals(15, result.getHour());
    }

    @Test
    void availableZoneCountIsLarge() {
        int count = InstantZonedDateTimeDemo.availableZoneCount();
        assertTrue(count > 500, "Should have hundreds of timezone IDs");
    }
}
