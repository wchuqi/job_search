package com.javastudy.stringdatetime;

/**
 * Demonstrates useful String methods introduced or commonly used in modern Java.
 * <p>
 * Key points:
 * <ul>
 *   <li>isBlank() - true if string is empty or contains only whitespace (Java 11+)</li>
 *   <li>strip() - trims Unicode-aware whitespace (Java 11+)</li>
 *   <li>contains() - checks if a substring is present</li>
 *   <li>startsWith() / endsWith() - prefix/suffix checks</li>
 *   <li>substring() - extract a portion</li>
 *   <li>replace() / replaceAll() - substitution</li>
 * </ul>
 */
public class StringMethodsDemo {

    /**
     * isBlank() returns true for empty string or whitespace-only strings.
     * Unlike isEmpty(), isBlank() handles whitespace.
     */
    public static boolean isBlank(String value) {
        return value.isBlank();
    }

    /**
     * strip() removes leading and trailing whitespace, including Unicode whitespace.
     * Preferred over trim() in modern Java because trim() only strips ASCII <= U+0020.
     */
    public static String strip(String value) {
        return value.strip();
    }

    /**
     * stripLeading() only removes leading whitespace.
     */
    public static String stripLeading(String value) {
        return value.stripLeading();
    }

    /**
     * stripTrailing() only removes trailing whitespace.
     */
    public static String stripTrailing(String value) {
        return value.stripTrailing();
    }

    /**
     * contains() checks whether the string contains the given subsequence.
     */
    public static boolean contains(String haystack, String needle) {
        return haystack.contains(needle);
    }

    /**
     * startsWith() checks if the string begins with the given prefix.
     */
    public static boolean startsWith(String value, String prefix) {
        return value.startsWith(prefix);
    }

    /**
     * endsWith() checks if the string ends with the given suffix.
     */
    public static boolean endsWith(String value, String suffix) {
        return value.endsWith(suffix);
    }

    /**
     * substring(beginIndex, endIndex) extracts a portion of the string.
     * beginIndex is inclusive, endIndex is exclusive.
     */
    public static String substring(String value, int beginIndex, int endIndex) {
        return value.substring(beginIndex, endIndex);
    }

    /**
     * substring(beginIndex) extracts from beginIndex to the end.
     */
    public static String substringFrom(String value, int beginIndex) {
        return value.substring(beginIndex);
    }

    /**
     * replace() replaces all occurrences of a literal target with a replacement.
     * Does NOT use regex.
     */
    public static String replace(String value, String target, String replacement) {
        return value.replace(target, replacement);
    }

    /**
     * replaceAll() uses a regex pattern for matching.
     */
    public static String replaceAll(String value, String regex, String replacement) {
        return value.replaceAll(regex, replacement);
    }

    /**
     * indexOf() returns the index of the first occurrence, or -1 if not found.
     */
    public static int indexOf(String value, String search) {
        return value.indexOf(search);
    }

    /**
     * repeat() repeats the string n times (Java 11+).
     */
    public static String repeat(String value, int count) {
        return value.repeat(count);
    }

    /**
     * lines() splits the string by line terminators and returns a Stream (Java 11+).
     */
    public static long lineCount(String value) {
        return value.lines().count();
    }
}
