package com.javastudy.stringdatetime;

import org.junit.jupiter.api.Test;

import java.util.Locale;

import static org.junit.jupiter.api.Assertions.*;

class StringFormattingDemoTest {

    @Test
    void basicFormatIncludesAllParts() {
        String result = StringFormattingDemo.basicFormat("Alice", 30, 95.678);
        assertEquals("Name: Alice, Age: 30, Score: 95.68", result);
    }

    @Test
    void fluentFormatMatchesBasicFormat() {
        String basic = StringFormattingDemo.basicFormat("Bob", 25, 88.1);
        String fluent = StringFormattingDemo.fluentFormat("Bob", 25, 88.1);
        assertEquals(basic, fluent);
    }

    @Test
    void formatDecimalWithPrecision() {
        assertEquals("3.14", StringFormattingDemo.formatDecimal(3.14159, 2));
        assertEquals("3.142", StringFormattingDemo.formatDecimal(3.14159, 3));
        assertEquals("3", StringFormattingDemo.formatDecimal(3.14159, 0));
    }

    @Test
    void formatWithPaddingAlignsColumns() {
        String result = StringFormattingDemo.formatWithPadding("Alice", 42);
        assertEquals("|Alice     |        42|", result);
    }

    @Test
    void formatWithArgIndexReorders() {
        String result = StringFormattingDemo.formatWithArgIndex("Alice", 5);
        assertEquals("Alice has 5 items; 5 items belong to Alice", result);
    }

    @Test
    void formatIntegersInDifferentBases() {
        String result = StringFormattingDemo.formatIntegers(255);
        assertEquals("decimal=255, hex=ff, octal=377", result);
    }

    @Test
    void formatPercentage() {
        assertEquals("Completion: 85.0%", StringFormattingDemo.formatPercentage(0.85));
        assertEquals("Completion: 100.0%", StringFormattingDemo.formatPercentage(1.0));
    }

    @Test
    void formatWithLocaleUsesCommas() {
        // US locale uses commas as thousands separator
        String result = StringFormattingDemo.formatWithLocale(Locale.US, 1234567.891);
        assertTrue(result.contains("1,234,567.89"), "US locale should use commas: " + result);
    }

    @Test
    void formatZeroPadded() {
        assertEquals("0042", StringFormattingDemo.formatZeroPadded(42, 4));
        assertEquals("00007", StringFormattingDemo.formatZeroPadded(7, 5));
    }

    @Test
    void formatWithNewlineContainsPlatformSeparator() {
        String result = StringFormattingDemo.formatWithNewline("line1", "line2");
        assertTrue(result.contains("line1"));
        assertTrue(result.contains("line2"));
        assertTrue(result.contains(System.lineSeparator()));
    }
}
