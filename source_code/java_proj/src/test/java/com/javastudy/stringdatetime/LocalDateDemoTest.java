package com.javastudy.stringdatetime;

import org.junit.jupiter.api.Test;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.Month;

import static org.junit.jupiter.api.Assertions.*;

class LocalDateDemoTest {

    @Test
    void ofCreatesCorrectDate() {
        LocalDate date = LocalDateDemo.of(2024, 6, 15);
        assertEquals(2024, date.getYear());
        assertEquals(Month.JUNE, date.getMonth());
        assertEquals(15, date.getDayOfMonth());
    }

    @Test
    void parseISFormat() {
        LocalDate date = LocalDateDemo.parse("2024-06-15");
        assertEquals(2024, date.getYear());
        assertEquals(6, date.getMonthValue());
        assertEquals(15, date.getDayOfMonth());
    }

    @Test
    void plusWeeksAddsCorrectDays() {
        LocalDate date = LocalDateDemo.of(2024, 1, 1);
        LocalDate result = LocalDateDemo.plusWeeks(date, 2);
        assertEquals(LocalDate.of(2024, 1, 15), result);
    }

    @Test
    void plusDaysAddsDays() {
        LocalDate date = LocalDateDemo.of(2024, 1, 30);
        LocalDate result = LocalDateDemo.plusDays(date, 3);
        assertEquals(LocalDate.of(2024, 2, 2), result);
    }

    @Test
    void minusDaysSubtractsDays() {
        LocalDate date = LocalDateDemo.of(2024, 3, 1);
        LocalDate result = LocalDateDemo.minusDays(date, 1);
        assertEquals(LocalDate.of(2024, 2, 29), result); // 2024 is a leap year
    }

    @Test
    void plusMonthsAddsMonths() {
        LocalDate date = LocalDateDemo.of(2024, 10, 31);
        LocalDate result = LocalDateDemo.plusMonths(date, 3);
        // Adding 3 months to Oct 31 gives Jan 31
        assertEquals(LocalDate.of(2025, 1, 31), result);
    }

    @Test
    void plusYearsAddsYears() {
        LocalDate date = LocalDateDemo.of(2024, 2, 29);
        LocalDate result = LocalDateDemo.plusYears(date, 1);
        // 2025 is not a leap year, so Feb 29 becomes Feb 28
        assertEquals(LocalDate.of(2025, 2, 28), result);
    }

    @Test
    void dayOfWeekIsCorrect() {
        // 2024-06-15 is a Saturday
        LocalDate date = LocalDateDemo.of(2024, 6, 15);
        assertEquals(DayOfWeek.SATURDAY, LocalDateDemo.dayOfWeek(date));
    }

    @Test
    void dayOfYearIsCorrect() {
        LocalDate date = LocalDateDemo.of(2024, 1, 1);
        assertEquals(1, LocalDateDemo.dayOfYear(date));

        LocalDate dec31 = LocalDateDemo.of(2024, 12, 31);
        assertEquals(366, LocalDateDemo.dayOfYear(dec31)); // 2024 is leap year
    }

    @Test
    void leapYearDetection() {
        assertTrue(LocalDateDemo.isLeapYear(LocalDate.of(2024, 1, 1)));
        assertFalse(LocalDateDemo.isLeapYear(LocalDate.of(2023, 1, 1)));
        assertTrue(LocalDateDemo.isLeapYear(LocalDate.of(2000, 1, 1)));
        assertFalse(LocalDateDemo.isLeapYear(LocalDate.of(1900, 1, 1)));
    }

    @Test
    void firstDayOfMonth() {
        LocalDate date = LocalDateDemo.of(2024, 6, 15);
        assertEquals(LocalDate.of(2024, 6, 1), LocalDateDemo.firstDayOfMonth(date));
    }

    @Test
    void lastDayOfMonth() {
        LocalDate date = LocalDateDemo.of(2024, 2, 15);
        assertEquals(LocalDate.of(2024, 2, 29), LocalDateDemo.lastDayOfMonth(date)); // leap year
    }

    @Test
    void nextDayOfWeek() {
        LocalDate date = LocalDateDemo.of(2024, 6, 15); // Saturday
        LocalDate nextMonday = LocalDateDemo.nextDayOfWeek(date, DayOfWeek.MONDAY);
        assertEquals(LocalDate.of(2024, 6, 17), nextMonday);
    }

    @Test
    void firstDayOfNextMonth() {
        LocalDate date = LocalDateDemo.of(2024, 6, 15);
        assertEquals(LocalDate.of(2024, 7, 1), LocalDateDemo.firstDayOfNextMonth(date));
    }

    @Test
    void isBeforeComparison() {
        LocalDate earlier = LocalDate.of(2024, 1, 1);
        LocalDate later = LocalDate.of(2024, 12, 31);
        assertTrue(LocalDateDemo.isBefore(earlier, later));
        assertFalse(LocalDateDemo.isBefore(later, earlier));
    }

    @Test
    void isAfterComparison() {
        LocalDate earlier = LocalDate.of(2024, 1, 1);
        LocalDate later = LocalDate.of(2024, 12, 31);
        assertTrue(LocalDateDemo.isAfter(later, earlier));
        assertFalse(LocalDateDemo.isAfter(earlier, later));
    }

    @Test
    void lengthOfMonth() {
        assertEquals(29, LocalDateDemo.lengthOfMonth(LocalDate.of(2024, 2, 1)));
        assertEquals(31, LocalDateDemo.lengthOfMonth(LocalDate.of(2024, 1, 1)));
        assertEquals(30, LocalDateDemo.lengthOfMonth(LocalDate.of(2024, 4, 1)));
    }

    @Test
    void getMonthReturnsEnum() {
        LocalDate date = LocalDate.of(2024, 6, 15);
        assertEquals(Month.JUNE, LocalDateDemo.getMonth(date));
    }
}
