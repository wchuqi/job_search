package com.javastudy.stringdatetime;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class StringMethodsDemoTest {

    @Test
    void isBlankForEmptyString() {
        assertTrue(StringMethodsDemo.isBlank(""));
    }

    @Test
    void isBlankForWhitespace() {
        assertTrue(StringMethodsDemo.isBlank("   "));
        assertTrue(StringMethodsDemo.isBlank("\t\n"));
    }

    @Test
    void isNotBlankForContent() {
        assertFalse(StringMethodsDemo.isBlank("hello"));
        assertFalse(StringMethodsDemo.isBlank(" hello "));
    }

    @Test
    void stripRemovesLeadingTrailingWhitespace() {
        assertEquals("hello", StringMethodsDemo.strip("  hello  "));
        assertEquals("hello", StringMethodsDemo.strip("\thello\n"));
    }

    @Test
    void stripLeadingRemovesOnlyLeading() {
        assertEquals("hello  ", StringMethodsDemo.stripLeading("  hello  "));
    }

    @Test
    void stripTrailingRemovesOnlyTrailing() {
        assertEquals("  hello", StringMethodsDemo.stripTrailing("  hello  "));
    }

    @Test
    void containsFindsSubstring() {
        assertTrue(StringMethodsDemo.contains("hello world", "world"));
        assertTrue(StringMethodsDemo.contains("hello world", "lo wo"));
    }

    @Test
    void containsReturnsFalseForMissing() {
        assertFalse(StringMethodsDemo.contains("hello world", "xyz"));
    }

    @Test
    void startsWithChecksPrefix() {
        assertTrue(StringMethodsDemo.startsWith("hello world", "hello"));
        assertFalse(StringMethodsDemo.startsWith("hello world", "world"));
    }

    @Test
    void endsWithChecksSuffix() {
        assertTrue(StringMethodsDemo.endsWith("hello world", "world"));
        assertFalse(StringMethodsDemo.endsWith("hello world", "hello"));
    }

    @Test
    void substringExtractsRange() {
        assertEquals("llo", StringMethodsDemo.substring("hello", 2, 5));
        assertEquals("ell", StringMethodsDemo.substring("hello", 1, 4));
    }

    @Test
    void substringFromExtractsToEnd() {
        assertEquals("llo", StringMethodsDemo.substringFrom("hello", 2));
    }

    @Test
    void replaceSubstitutesAllOccurrences() {
        assertEquals("hellx wxrld", StringMethodsDemo.replace("hello world", "o", "x"));
        assertEquals("heLLo worLd", StringMethodsDemo.replace("hello world", "l", "L"));
    }

    @Test
    void replaceAllWithRegex() {
        assertEquals("h*ll* w*rld", StringMethodsDemo.replaceAll("hello world", "[aeiou]", "*"));
    }

    @Test
    void indexOfFindsPosition() {
        assertEquals(2, StringMethodsDemo.indexOf("hello", "llo"));
        assertEquals(-1, StringMethodsDemo.indexOf("hello", "xyz"));
    }

    @Test
    void repeatDuplicatesString() {
        assertEquals("abcabcabc", StringMethodsDemo.repeat("abc", 3));
        assertEquals("", StringMethodsDemo.repeat("abc", 0));
    }

    @Test
    void lineCountCountsLines() {
        assertEquals(1, StringMethodsDemo.lineCount("hello"));
        assertEquals(3, StringMethodsDemo.lineCount("line1\nline2\nline3"));
        assertEquals(2, StringMethodsDemo.lineCount("line1\nline2"));
    }
}
