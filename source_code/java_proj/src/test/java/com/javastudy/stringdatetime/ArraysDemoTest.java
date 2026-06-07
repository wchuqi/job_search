package com.javastudy.stringdatetime;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ArraysDemoTest {

    @Test
    void sortIntArray() {
        int[] result = ArraysDemo.sort(new int[]{5, 3, 1, 4, 2});
        assertArrayEquals(new int[]{1, 2, 3, 4, 5}, result);
    }

    @Test
    void sortDoesNotModifyOriginal() {
        int[] original = {5, 3, 1};
        ArraysDemo.sort(original);
        assertArrayEquals(new int[]{5, 3, 1}, original, "Original should be unchanged");
    }

    @Test
    void sortRangeSortsOnlyRange() {
        int[] result = ArraysDemo.sortRange(new int[]{5, 3, 1, 4, 2}, 1, 4);
        // Only indices 1-3 are sorted: [5, 1, 3, 4, 2] -> [5, 1, 3, 4, 2]
        // Actually: indices 1,2,3 = [3,1,4] sorted = [1,3,4], so result = [5,1,3,4,2]
        assertArrayEquals(new int[]{5, 1, 3, 4, 2}, result);
    }

    @Test
    void sortStrings() {
        String[] result = ArraysDemo.sortStrings(new String[]{"banana", "apple", "cherry"});
        assertArrayEquals(new String[]{"apple", "banana", "cherry"}, result);
    }

    @Test
    void asListCreatesListFromArray() {
        List<String> list = ArraysDemo.asList("a", "b", "c");
        assertEquals(3, list.size());
        assertEquals("a", list.get(0));
        assertEquals("b", list.get(1));
        assertEquals("c", list.get(2));
    }

    @Test
    void asListThrowsOnAdd() {
        List<String> list = ArraysDemo.asList("a", "b");
        assertThrows(UnsupportedOperationException.class, () -> list.add("c"),
                "asList() returns a fixed-size list that does not support add()");
    }

    @Test
    void asListThrowsOnRemove() {
        List<String> list = ArraysDemo.asList("a", "b");
        assertThrows(UnsupportedOperationException.class, () -> list.remove(0),
                "asList() returns a fixed-size list that does not support remove()");
    }

    @Test
    void asListAllowsSet() {
        String[] array = {"a", "b", "c"};
        List<String> list = ArraysDemo.asListWithSet(array, 1, "X");
        assertEquals("X", list.get(1));
        // The underlying array is also modified
        assertEquals("X", array[1]);
    }

    @Test
    void copyOfWithLargerLength() {
        int[] result = ArraysDemo.copyOf(new int[]{1, 2, 3}, 5);
        assertArrayEquals(new int[]{1, 2, 3, 0, 0}, result);
    }

    @Test
    void copyOfWithSmallerLength() {
        int[] result = ArraysDemo.copyOf(new int[]{1, 2, 3, 4, 5}, 3);
        assertArrayEquals(new int[]{1, 2, 3}, result);
    }

    @Test
    void fillArrayWithValue() {
        int[] result = ArraysDemo.fill(new int[5], 42);
        assertArrayEquals(new int[]{42, 42, 42, 42, 42}, result);
    }

    @Test
    void equalsForIdenticalArrays() {
        assertTrue(ArraysDemo.equals(new int[]{1, 2, 3}, new int[]{1, 2, 3}));
    }

    @Test
    void equalsForDifferentArrays() {
        assertFalse(ArraysDemo.equals(new int[]{1, 2, 3}, new int[]{1, 2, 4}));
    }

    @Test
    void toStringReadableFormat() {
        assertEquals("[1, 2, 3]", ArraysDemo.toString(new int[]{1, 2, 3}));
    }

    @Test
    void sumUsingStream() {
        assertEquals(15, ArraysDemo.sum(new int[]{1, 2, 3, 4, 5}));
    }

    @Test
    void binarySearchFindsElement() {
        int[] sorted = {1, 3, 5, 7, 9};
        assertEquals(2, ArraysDemo.binarySearch(sorted, 5));
    }

    @Test
    void binarySearchReturnsNegativeForMissing() {
        int[] sorted = {1, 3, 5, 7, 9};
        int result = ArraysDemo.binarySearch(sorted, 4);
        assertTrue(result < 0, "Negative value means element not found");
    }

    @Test
    void mismatchReturnsMinusOneForEqual() {
        assertEquals(-1, ArraysDemo.mismatch(new int[]{1, 2, 3}, new int[]{1, 2, 3}));
    }

    @Test
    void mismatchReturnsIndexForDifferent() {
        assertEquals(1, ArraysDemo.mismatch(new int[]{1, 2, 3}, new int[]{1, 9, 3}));
    }
}
