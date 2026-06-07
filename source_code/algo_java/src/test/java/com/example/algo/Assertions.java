package com.example.algo;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

final class Assertions {
    private Assertions() {
    }

    static void equals(Object expected, Object actual) {
        if (!Objects.equals(expected, actual)) {
            throw new AssertionError("expected=" + expected + ", actual=" + actual);
        }
    }

    static void arrayEquals(int[] expected, int[] actual) {
        if (!Arrays.equals(expected, actual)) {
            throw new AssertionError("expected=" + Arrays.toString(expected) + ", actual=" + Arrays.toString(actual));
        }
    }

    static void matrixEquals(List<List<Integer>> expected, List<List<Integer>> actual) {
        if (!Objects.equals(expected, actual)) {
            throw new AssertionError("expected=" + expected + ", actual=" + actual);
        }
    }

    static void isTrue(boolean value) {
        if (!value) {
            throw new AssertionError("expected true");
        }
    }

    static void isFalse(boolean value) {
        if (value) {
            throw new AssertionError("expected false");
        }
    }
}
