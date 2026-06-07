package com.javastudy.basics;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class SwitchExpressionDemoTest {

    @Test
    void testDayType() {
        assertEquals("Weekday", SwitchExpressionDemo.dayType("Monday"));
        assertEquals("Weekday", SwitchExpressionDemo.dayType("Friday"));
        assertEquals("Weekend", SwitchExpressionDemo.dayType("Saturday"));
        assertEquals("Weekend", SwitchExpressionDemo.dayType("Sunday"));
        assertEquals("Unknown", SwitchExpressionDemo.dayType("Holiday"));
    }

    @Test
    void testDaysInMonth() {
        assertEquals(31, SwitchExpressionDemo.daysInMonth("January", 2024));
        assertEquals(30, SwitchExpressionDemo.daysInMonth("April", 2024));
        assertEquals(29, SwitchExpressionDemo.daysInMonth("February", 2024)); // 闰年
        assertEquals(28, SwitchExpressionDemo.daysInMonth("February", 2023)); // 非闰年
    }

    @Test
    void testDaysInMonthInvalid() {
        assertThrows(IllegalArgumentException.class,
            () -> SwitchExpressionDemo.daysInMonth("Invalid", 2024));
    }

    @Test
    void testSeasonFromMonth() {
        assertEquals("Spring", SwitchExpressionDemo.seasonFromMonth(3));
        assertEquals("Summer", SwitchExpressionDemo.seasonFromMonth(7));
        assertEquals("Autumn", SwitchExpressionDemo.seasonFromMonth(10));
        assertEquals("Winter", SwitchExpressionDemo.seasonFromMonth(12));
    }
}
