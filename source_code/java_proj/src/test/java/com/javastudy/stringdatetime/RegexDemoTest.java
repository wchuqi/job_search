package com.javastudy.stringdatetime;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class RegexDemoTest {

    @Test
    void matchesEntireStringForDigits() {
        assertTrue(RegexDemo.matchesEntireString("12345", "\\d+"));
        assertFalse(RegexDemo.matchesEntireString("123ab", "\\d+"));
    }

    @Test
    void findAllMatchesFindsAllNumbers() {
        List<String> matches = RegexDemo.findAllMatches("abc 123 def 456", "\\d+");
        assertEquals(List.of("123", "456"), matches);
    }

    @Test
    void findAllMatchesReturnsEmptyWhenNone() {
        List<String> matches = RegexDemo.findAllMatches("no numbers here", "\\d+");
        assertTrue(matches.isEmpty());
    }

    @Test
    void extractGroupsCapturesParts() {
        List<String> groups = RegexDemo.extractGroups("2024-01-15", "(\\d{4})-(\\d{2})-(\\d{2})");
        assertEquals(4, groups.size());
        assertEquals("2024-01-15", groups.get(0)); // full match
        assertEquals("2024", groups.get(1));        // group 1
        assertEquals("01", groups.get(2));          // group 2
        assertEquals("15", groups.get(3));          // group 3
    }

    @Test
    void extractGroupsReturnsEmptyForNoMatch() {
        List<String> groups = RegexDemo.extractGroups("no match", "(\\d{4})-(\\d{2})-(\\d{2})");
        assertTrue(groups.isEmpty());
    }

    @Test
    void validEmailAccepted() {
        assertTrue(RegexDemo.isValidEmail("user@example.com"));
        assertTrue(RegexDemo.isValidEmail("user.name+tag@domain.co"));
    }

    @Test
    void invalidEmailRejected() {
        assertFalse(RegexDemo.isValidEmail("not-an-email"));
        assertFalse(RegexDemo.isValidEmail("@no-user.com"));
        assertFalse(RegexDemo.isValidEmail("user@"));
    }

    @Test
    void parsePhoneNumberExtractsParts() {
        List<String> parts = RegexDemo.parsePhoneNumber("555-123-4567");
        assertEquals(3, parts.size());
        assertEquals("555", parts.get(0));
        assertEquals("123", parts.get(1));
        assertEquals("4567", parts.get(2));
    }

    @Test
    void parsePhoneNumberWithSpaces() {
        List<String> parts = RegexDemo.parsePhoneNumber("555 123 4567");
        assertEquals(3, parts.size());
    }

    @Test
    void parsePhoneNumberReturnsEmptyForInvalid() {
        List<String> parts = RegexDemo.parsePhoneNumber("not a phone");
        assertTrue(parts.isEmpty());
    }

    @Test
    void replaceMatchesSubstitutesCorrectly() {
        String result = RegexDemo.replaceMatches("hello 123 world 456", "\\d+", "NUM");
        assertEquals("hello NUM world NUM", result);
    }

    @Test
    void matchLiteralDotRequiresEscaping() {
        assertTrue(RegexDemo.matchLiteralDot("file.txt"));
        assertTrue(RegexDemo.matchLiteralDot("a.b.c"));
        assertFalse(RegexDemo.matchLiteralDot("no-dot-here"));
    }

    @Test
    void matchLiteralStringUsesQuoteEscaping() {
        assertTrue(RegexDemo.matchLiteralString("price is $10.00", "$10.00"));
        assertTrue(RegexDemo.matchLiteralString("a+b=c", "a+b"));
        assertFalse(RegexDemo.matchLiteralString("abc", "a+b"));
    }

    @Test
    void splitByRegexSplitsCorrectly() {
        String[] parts = RegexDemo.splitByRegex("one,two,,three", ",");
        assertEquals(4, parts.length);
        assertEquals("one", parts[0]);
        assertEquals("two", parts[1]);
        assertEquals("", parts[2]);
        assertEquals("three", parts[3]);
    }

    @Test
    void caseInsensitiveMatchIgnoresCase() {
        assertTrue(RegexDemo.caseInsensitiveMatch("Hello World", "hello world"));
        assertTrue(RegexDemo.caseInsensitiveMatch("HELLO", "hello"));
        assertFalse(RegexDemo.caseInsensitiveMatch("goodbye", "hello"));
    }
}
