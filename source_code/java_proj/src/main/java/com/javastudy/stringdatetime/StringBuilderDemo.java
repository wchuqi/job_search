package com.javastudy.stringdatetime;

import java.util.List;

/**
 * Demonstrates StringBuilder for efficient string concatenation.
 * <p>
 * Key points:
 * <ul>
 *   <li>String concatenation in loops creates many intermediate objects</li>
 *   <li>StringBuilder is mutable and efficient for repeated appends</li>
 *   <li>append() returns 'this' for method chaining</li>
 *   <li>toString() produces the final String</li>
 *   <li>StringJoiner or String.join() are alternatives for delimiter-based joining</li>
 * </ul>
 */
public class StringBuilderDemo {

    /**
     * BAD: Naive string concatenation in a loop.
     * Creates a new String object on every iteration (O(n^2) time).
     * Use this only for trivial cases.
     */
    public static String concatenateWithPlus(List<String> items) {
        String result = "";
        for (String item : items) {
            result = result + item + ", ";
        }
        return result;
    }

    /**
     * GOOD: Using StringBuilder for loop concatenation.
     * Mutable buffer, O(n) time.
     */
    public static String concatenateWithStringBuilder(List<String> items) {
        StringBuilder sb = new StringBuilder();
        for (String item : items) {
            sb.append(item).append(", ");
        }
        return sb.toString();
    }

    /**
     * Demonstrates StringBuilder capacity management.
     * You can pre-allocate capacity to avoid resizing.
     */
    public static String withPreallocatedCapacity(List<String> items, int estimatedSize) {
        StringBuilder sb = new StringBuilder(estimatedSize);
        for (String item : items) {
            sb.append(item);
        }
        return sb.toString();
    }

    /**
     * Demonstrates method chaining with append().
     * append() returns the same StringBuilder instance.
     */
    public static String chainedAppend(String name, int age, double salary) {
        return new StringBuilder()
                .append("Name: ").append(name)
                .append(", Age: ").append(age)
                .append(", Salary: ").append(salary)
                .toString();
    }

    /**
     * Demonstrates insert() to add content at a specific position.
     */
    public static String insertAtPosition(String original, int index, String toInsert) {
        return new StringBuilder(original).insert(index, toInsert).toString();
    }

    /**
     * Demonstrates delete() to remove characters in a range.
     */
    public static String deleteRange(String original, int start, int end) {
        return new StringBuilder(original).delete(start, end).toString();
    }

    /**
     * Demonstrates reverse().
     */
    public static String reverse(String value) {
        return new StringBuilder(value).reverse().toString();
    }

    /**
     * Demonstrates replace() on StringBuilder.
     */
    public static StringBuilder replaceRange(String original, int start, int end, String replacement) {
        return new StringBuilder(original).replace(start, end, replacement);
    }

    /**
     * Shows String.join() as a simpler alternative for delimiter-based joining.
     */
    public static String joinWithDelimiter(String delimiter, List<String> items) {
        return String.join(delimiter, items);
    }

    /**
     * Shows StringJoiner for building delimited strings with prefix/suffix.
     */
    public static String joinWithPrefixSuffix(String delimiter, String prefix, String suffix, List<String> items) {
        java.util.StringJoiner joiner = new java.util.StringJoiner(delimiter, prefix, suffix);
        for (String item : items) {
            joiner.add(item);
        }
        return joiner.toString();
    }
}
