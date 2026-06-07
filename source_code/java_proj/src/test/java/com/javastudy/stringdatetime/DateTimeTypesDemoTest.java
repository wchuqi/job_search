package com.javastudy.stringdatetime;

import org.junit.jupiter.api.Test;

import java.time.*;
import java.time.temporal.ChronoUnit;

import static org.junit.jupiter.api.Assertions.*;

class DateTimeTypesDemoTest {

    @Test
    void currentInstantIsNotNull() {
        Instant instant = DateTimeTypesDemo.currentInstant();
        assertNotNull(instant);
    }

    @Test
    void todayIsNotNull() {
        LocalDate today = DateTimeTypesDemo.today();
        assertNotNull(today);
    }

    @Test
    void createDateHasCorrectValues() {
        LocalDate date = DateTimeTypesDemo.createDate(2024, 6, 15);
        assertEquals(2024, date.getYear());
        assertEquals(Month.JUNE, date.getMonth());
        assertEquals(15, date.getDayOfMonth());
    }

    @Test
    void currentTimeIsNotNull() {
        LocalTime time = DateTimeTypesDemo.currentTime();
        assertNotNull(time);
    }

    @Test
    void createTimeHasCorrectValues() {
        LocalTime time = DateTimeTypesDemo.createTime(14, 30, 0);
        assertEquals(14, time.getHour());
        assertEquals(30, time.getMinute());
        assertEquals(0, time.getSecond());
    }

    @Test
    void currentDateTimeIsNotNull() {
        LocalDateTime ldt = DateTimeTypesDemo.currentDateTime();
        assertNotNull(ldt);
    }

    @Test
    void createDateTimeHasCorrectValues() {
        LocalDateTime ldt = DateTimeTypesDemo.createDateTime(2024, 6, 15, 14, 30, 0);
        assertEquals(2024, ldt.getYear());
        assertEquals(14, ldt.getHour());
        assertEquals(30, ldt.getMinute());
    }

    @Test
    void currentZonedDateTimeInShanghai() {
        ZoneId shanghai = ZoneId.of("Asia/Shanghai");
        ZonedDateTime zdt = DateTimeTypesDemo.currentZonedDateTime(shanghai);
        assertNotNull(zdt);
        assertEquals(shanghai, zdt.getZone());
    }

    @Test
    void toZonedAddsTimezone() {
        LocalDateTime ldt = LocalDateTime.of(2024, 6, 15, 14, 30, 0);
        ZoneId tokyo = ZoneId.of("Asia/Tokyo");
        ZonedDateTime zdt = DateTimeTypesDemo.toZoned(ldt, tokyo);
        assertNotNull(zdt);
        assertEquals(tokyo, zdt.getZone());
    }

    @Test
    void durationBetweenInstants() {
        Instant start = Instant.parse("2024-01-01T00:00:00Z");
        Instant end = Instant.parse("2024-01-01T01:00:00Z");
        Duration duration = DateTimeTypesDemo.durationBetween(start, end);
        assertEquals(3600, duration.getSeconds());
    }

    @Test
    void createDurationFromComponents() {
        Duration duration = DateTimeTypesDemo.createDuration(1, 30, 45);
        assertEquals(5445, duration.getSeconds()); // 1h30m45s = 5445 seconds
    }

    @Test
    void periodBetweenDates() {
        LocalDate start = LocalDate.of(2024, 1, 1);
        LocalDate end = LocalDate.of(2025, 3, 15);
        Period period = DateTimeTypesDemo.periodBetween(start, end);
        assertEquals(1, period.getYears());
        assertEquals(2, period.getMonths());
        assertEquals(14, period.getDays());
    }

    @Test
    void createPeriod() {
        Period period = DateTimeTypesDemo.createPeriod(1, 6, 15);
        assertEquals(1, period.getYears());
        assertEquals(6, period.getMonths());
        assertEquals(15, period.getDays());
    }

    @Test
    void toInstantFromLocalDateTime() {
        LocalDateTime ldt = LocalDateTime.of(2024, 6, 15, 12, 0, 0);
        ZoneId utc = ZoneId.of("UTC");
        Instant instant = DateTimeTypesDemo.toInstant(ldt, utc);
        assertNotNull(instant);
    }

    @Test
    void toLocalDateTimeFromInstant() {
        Instant instant = Instant.parse("2024-06-15T12:00:00Z");
        ZoneId utc = ZoneId.of("UTC");
        LocalDateTime ldt = DateTimeTypesDemo.toLocalDateTime(instant, utc);
        assertEquals(2024, ldt.getYear());
        assertEquals(6, ldt.getMonthValue());
        assertEquals(15, ldt.getDayOfMonth());
    }

    @Test
    void daysBetweenDates() {
        LocalDate start = LocalDate.of(2024, 1, 1);
        LocalDate end = LocalDate.of(2024, 1, 31);
        assertEquals(30, DateTimeTypesDemo.daysBetween(start, end));
    }
}
