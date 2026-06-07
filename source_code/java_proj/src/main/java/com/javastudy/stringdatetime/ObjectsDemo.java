package com.javastudy.stringdatetime;

import java.util.Objects;

/**
 * Demonstrates the java.util.Objects utility class (Java 7+).
 * <p>
 * Key points:
 * <ul>
 *   <li>Objects.requireNonNull() - null check with exception (great for constructors)</li>
 *   <li>Objects.equals() - null-safe equality comparison</li>
 *   <li>Objects.hashCode() - null-safe hash code computation</li>
 *   <li>Objects.hash() - convenient hash from multiple fields</li>
 *   <li>Objects.isNull() / Objects.nonNull() - predicates for streams</li>
 *   <li>Objects.toString() - null-safe toString</li>
 * </ul>
 */
public class ObjectsDemo {

    /**
     * requireNonNull throws NullPointerException if obj is null.
     * Returns obj if non-null. Great for validating constructor/method parameters.
     */
    public static <T> T requireNonNull(T obj) {
        return Objects.requireNonNull(obj);
    }

    /**
     * requireNonNull with a custom message.
     */
    public static <T> T requireNonNullWithMessage(T obj, String message) {
        return Objects.requireNonNull(obj, message);
    }

    /**
     * Null-safe equals: handles null on either side without NPE.
     * Two nulls are considered equal; one null and one non-null are not.
     */
    public static boolean equals(Object a, Object b) {
        return Objects.equals(a, b);
    }

    /**
     * Null-safe hashCode: returns 0 for null, obj.hashCode() otherwise.
     */
    public static int hashCode(Object obj) {
        return Objects.hashCode(obj);
    }

    /**
     * hash() computes a hash code from multiple fields.
     * Convenient for implementing hashCode() in custom classes.
     * Order matters: hash(a, b) != hash(b, a) in general.
     */
    public static int hash(Object... values) {
        return Objects.hash(values);
    }

    /**
     * isNull() returns true if obj is null.
     * Useful as a Predicate in streams: .filter(Objects::isNull)
     */
    public static boolean isNull(Object obj) {
        return Objects.isNull(obj);
    }

    /**
     * nonNull() returns true if obj is not null.
     * Useful as a Predicate in streams: .filter(Objects::nonNull)
     */
    public static boolean nonNull(Object obj) {
        return Objects.nonNull(obj);
    }

    /**
     * toString() with default value for null.
     */
    public static String toString(Object obj, String nullDefault) {
        return Objects.toString(obj, nullDefault);
    }

    /**
     * compare() for null-safe comparison with a Comparator.
     */
    public static <T extends Comparable<T>> int compare(T a, T b) {
        return Objects.compare(a, b, Comparable::compareTo);
    }
}
