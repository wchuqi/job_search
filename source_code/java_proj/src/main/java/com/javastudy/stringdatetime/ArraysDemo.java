package com.javastudy.stringdatetime;

import java.util.Arrays;
import java.util.List;

/**
 * Demonstrates java.util.Arrays utility methods.
 * <p>
 * Key points:
 * <ul>
 *   <li>Arrays.sort() - sort primitive and object arrays</li>
 *   <li>Arrays.asList() - wraps array as List (FIXED-SIZE: cannot add/remove)</li>
 *   <li>Arrays.copyOf() - copy array with new length</li>
 *   <li>Arrays.fill() - fill array with a value</li>
 *   <li>Arrays.equals() / deepEquals() - compare arrays</li>
 *   <li>Arrays.toString() / deepToString() - readable array output</li>
 *   <li>Arrays.stream() - convert array to Stream</li>
 * </ul>
 */
public class ArraysDemo {

    /**
     * Sort an int array in-place (ascending).
     */
    public static int[] sort(int[] array) {
        int[] copy = array.clone();
        Arrays.sort(copy);
        return copy;
    }

    /**
     * Sort a range of an array.
     */
    public static int[] sortRange(int[] array, int fromIndex, int toIndex) {
        int[] copy = array.clone();
        Arrays.sort(copy, fromIndex, toIndex);
        return copy;
    }

    /**
     * Sort a String array (natural ordering).
     */
    public static String[] sortStrings(String[] array) {
        String[] copy = array.clone();
        Arrays.sort(copy);
        return copy;
    }

    /**
     * Arrays.asList() wraps an array as a List.
     * WARNING: The returned list is a FIXED-SIZE view of the array.
     * You CANNOT add or remove elements (UnsupportedOperationException).
     * You CAN set elements (it modifies the underlying array).
     */
    public static List<String> asList(String... items) {
        return Arrays.asList(items);
    }

    /**
     * Demonstrate the fixed-size caveat: setting elements works.
     */
    public static List<String> asListWithSet(String[] array, int index, String newValue) {
        List<String> list = Arrays.asList(array);
        list.set(index, newValue);
        return list;
    }

    /**
     * Copy an array with a new length.
     * If new length > original, extra elements are zero/null/false.
     * If new length < original, truncates.
     */
    public static int[] copyOf(int[] array, int newLength) {
        return Arrays.copyOf(array, newLength);
    }

    /**
     * Fill an array with a specific value.
     */
    public static int[] fill(int[] array, int value) {
        int[] copy = array.clone();
        Arrays.fill(copy, value);
        return copy;
    }

    /**
     * Compare two arrays for equality (element-by-element).
     */
    public static boolean equals(int[] a, int[] b) {
        return Arrays.equals(a, b);
    }

    /**
     * deepEquals for nested arrays.
     */
    public static boolean deepEquals(Object[][] a, Object[][] b) {
        return Arrays.deepEquals(a, b);
    }

    /**
     * Human-readable array representation.
     */
    public static String toString(int[] array) {
        return Arrays.toString(array);
    }

    /**
     * deepToString for nested arrays.
     */
    public static String deepToString(Object[][] array) {
        return Arrays.deepToString(array);
    }

    /**
     * Convert array to stream for further processing.
     */
    public static int sum(int[] array) {
        return Arrays.stream(array).sum();
    }

    /**
     * Binary search (array must be sorted).
     * Returns index of the element, or negative insertion point - 1.
     */
    public static int binarySearch(int[] sortedArray, int key) {
        return Arrays.binarySearch(sortedArray, key);
    }

    /**
     * Check if two arrays are equal using mismatch (Java 10+).
     * Returns -1 if arrays are equal, otherwise the index of first mismatch.
     */
    public static int mismatch(int[] a, int[] b) {
        return Arrays.mismatch(a, b);
    }
}
