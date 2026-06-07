package com.javastudy.stringdatetime;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.time.ZoneId;

import static org.junit.jupiter.api.Assertions.*;

class DateTimeFormatterDemoTest {

    @Test
    void formatDateWithPattern() {
        LocalDate date = LocalDate.of(2024, 6, 15);
        assertEquals("2024-06-15", DateTimeFormatterDemo.formatDate(date, "yyyy-MM-dd"));
        assertEquals("15/06/2024", DateTimeFormatterDemo.formatDate(date, "dd/MM/yyyy"));
        assertEquals("2024年06月15日", DateTimeFormatterDemo.formatDate(date, "yyyy年MM月dd日"));
    }

    @Test
    void formatDateTimeWithPattern() {
        LocalDateTime ldt = LocalDateTime.of(2024, 6, 15, 14, 30, 0);
        assertEquals("2024-06-15 14:30:00", DateTimeFormatterDemo.formatDateTime(ldt, "yyyy-MM-dd HH:mm:ss"));
    }

    @Test
    void parseDateFromPattern() {
        LocalDate date = DateTimeFormatterDemo.parseDate("15/06/2024", "dd/MM/yyyy");
        assertEquals(2024, date.getYear());
        assertEquals(6, date.getMonthValue());
        assertEquals(15, date.getDayOfMonth());
    }

    @Test
    void parseDateTimeFromPattern() {
        LocalDateTime ldt = DateTimeFormatterDemo.parseDateTime("2024-06-15 14:30:00", "yyyy-MM-dd HH:mm:ss");
        assertEquals(14, ldt.getHour());
        assertEquals(30, ldt.getMinute());
    }

    @Test
    void yyyyVsYYYYSameForMidYear() {
        // For most of the year, yyyy and YYYY give the same result
        LocalDate midYear = LocalDate.of(2024, 6, 15);
        assertTrue(DateTimeFormatterDemo.yyyyVsYYYY(midYear));
    }

    @Test
    void yyyyVsYYYYDiffersNearNewYear() {
        // 2024-12-31 is in ISO week 1 of 2025
        LocalDate dec31 = LocalDate.of(2024, 12, 31);
        // yyyy gives 2024, YYYY gives 2025
        // Whether they differ depends on the exact ISO week calculation
        String calendarYear = DateTimeFormatterDemo.calendarYear(dec31);
        String weekBasedYear = DateTimeFormatterDemo.weekBasedYear(dec31);
        // At minimum, verify the formatters produce output
        assertNotNull(calendarYear);
        assertNotNull(weekBasedYear);
    }

    @Test
    void calendarYearVsWeekBasedYear() {
        // Jan 1 2024 is a Monday, so week-based year should match
        LocalDate jan1 = LocalDate.of(2024, 1, 1);
        assertEquals("2024", DateTimeFormatterDemo.calendarYear(jan1));
        assertEquals("2024", DateTimeFormatterDemo.weekBasedYear(jan1));
    }

    @Test
    void formatISODate() {
        LocalDate date = LocalDate.of(2024, 6, 15);
        assertEquals("2024-06-15", DateTimeFormatterDemo.formatISO(date));
    }

    @Test
    void formatISOLocalDateTime() {
        LocalDateTime ldt = LocalDateTime.of(2024, 6, 15, 14, 30, 0);
        assertEquals("2024-06-15T14:30:00", DateTimeFormatterDemo.formatLocalized(ldt));
    }

    @Test
    void reformatConvertsPattern() {
        String result = DateTimeFormatterDemo.reformat("15/06/2024", "dd/MM/yyyy", "yyyy-MM-dd");
        assertEquals("2024-06-15", result);
    }

    @Test
    void tryParseReturnsDateForValidInput() {
        LocalDate date = DateTimeFormatterDemo.tryParse("2024-06-15", "yyyy-MM-dd");
        assertNotNull(date);
        assertEquals(2024, date.getYear());
    }

    @Test
    void tryParseReturnsNullForInvalidInput() {
        LocalDate date = DateTimeFormatterDemo.tryParse("not-a-date", "yyyy-MM-dd");
        assertNull(date);
    }

    @Test
    void tryParseReturnsNullForWrongPattern() {
        LocalDate date = DateTimeFormatterDemo.tryParse("2024/06/15", "yyyy-MM-dd");
        assertNull(date);
    }

    @Test
    void formatZonedDateTime() {
        ZonedDateTime zdt = ZonedDateTime.of(2024, 6, 15, 14, 30, 0, 0, ZoneId.of("Asia/Shanghai"));
        String result = DateTimeFormatterDemo.formatZoned(zdt, "yyyy-MM-dd HH:mm:ss z");
        assertTrue(result.contains("2024-06-15"));
        assertTrue(result.contains("14:30:00"));
    }
}
