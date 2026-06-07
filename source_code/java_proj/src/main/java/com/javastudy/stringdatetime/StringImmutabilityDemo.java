package com.javastudy.stringdatetime;

/**
 * Demonstrates String immutability in Java.
 * <p>
 * Key points:
 * <ul>
 *   <li>Strings are immutable - every modification returns a new object</li>
 *   <li>toUpperCase(), toLowerCase(), trim() etc. return new String instances</li>
 *   <li>== compares references; equals() compares content</li>
 *   <li>String literals are interned, so == may appear to work for literals</li>
 * </ul>
 */
public class StringImmutabilityDemo {

    /**
     * Demonstrates that toUpperCase returns a new String object.
     * The original string is never modified.
     */
    public static String toUpperCasePreservesOriginal(String original) {
        String upper = original.toUpperCase();
        // original is unchanged; upper is a new object
        return upper;
    }

    /**
     * Returns the original string (unchanged after calling toUpperCase internally).
     */
    public static String getOriginalAfterUpper(String original) {
        String upper = original.toUpperCase(); // upper is a new object
        // original is still the same reference, unchanged
        return original;
    }

    /**
     * Compares two strings using == (reference equality).
     */
    public static boolean referenceEquals(String a, String b) {
        return a == b;
    }

    /**
     * Compares two strings using equals() (content equality).
     */
    public static boolean contentEquals(String a, String b) {
        return a.equals(b);
    }

    /**
     * Demonstrates that new String("...") always creates a new object,
     * so == fails even for identical content.
     */
    public static boolean newStringReferenceEquals(String value) {
        String literal = value;
        String fromNew = new String(value);
        return literal == fromNew;
    }

    /**
     * Demonstrates String concatenation creates a new object.
     */
    public static boolean concatenationCreatesNewObject(String a, String b) {
        String before = a;
        String after = a + b;
        return before == after; // false (unless b is empty and compiler optimizes)
    }

    /**
     * Demonstrates that intern() returns a canonical reference from the string pool.
     */
    public static boolean internReturnsSameReference(String value) {
        String newStr = new String(value);
        String interned = newStr.intern();
        return interned == value; // true if value was a compile-time literal
    }

    /**
     * Shows that trim() also returns a new string.
     */
    public static boolean trimReturnsNewObject(String original) {
        String trimmed = original.trim();
        return original == trimmed; // false unless string had no leading/trailing spaces
    }
}
