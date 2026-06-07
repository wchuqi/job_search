package com.javastudy.collections;

import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class TreeMapDemoTest {

    private final TreeMapDemo demo = new TreeMapDemo();

    @Test
    void naturalOrderSortsKeys() {
        assertEquals(List.of("Apple", "Banana", "Cherry"), demo.naturalOrder());
    }

    @Test
    void reverseOrderSortsKeysDesc() {
        assertEquals(List.of("Cherry", "Banana", "Apple"), demo.reverseOrder());
    }

    @Test
    void firstAndLastReturnBoundKeys() {
        List<String> result = demo.firstAndLast();
        assertEquals("A", result.get(0));
        assertEquals("C", result.get(1));
    }

    @Test
    void rangeViewsReturnCorrectSizes() {
        Map<String, Integer> result = demo.rangeViews();
        assertEquals(3, result.get("head"));  // A, B, C
        assertEquals(3, result.get("tail"));  // C, D, E
        assertEquals(3, result.get("sub"));   // B, C, D
    }

    @Test
    void sortByLengthUsesCustomComparator() {
        List<String> result = demo.sortByLength();
        assertEquals("Fig", result.get(0));    // length 3
        assertEquals("Kiwi", result.get(1));   // length 4
        assertEquals("Apple", result.get(2));  // length 5
        assertEquals("Banana", result.get(3)); // length 6
    }

    @Test
    void navigableOperationsReturnCorrectValues() {
        Map<String, Integer> result = demo.navigableOperations();
        assertEquals(10, result.get("lower"));
        assertEquals(30, result.get("higher"));
        assertEquals(20, result.get("floor"));
        assertEquals(30, result.get("ceiling"));
    }
}
