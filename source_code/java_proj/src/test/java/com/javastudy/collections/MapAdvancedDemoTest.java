package com.javastudy.collections;

import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class MapAdvancedDemoTest {

    private final MapAdvancedDemo demo = new MapAdvancedDemo();

    @Test
    void getOrDefaultReturnsExistingValue() {
        assertEquals(1, demo.getOrDefaultDemo("A"));
    }

    @Test
    void getOrDefaultReturnsDefaultForMissing() {
        assertEquals(0, demo.getOrDefaultDemo("Z"));
    }

    @Test
    void groupByFirstCharGroupsCorrectly() {
        Map<String, List<String>> result = demo.groupByFirstChar("apple", "avocado", "banana", "blueberry", "cherry");
        assertEquals(2, result.get("A").size());
        assertEquals(2, result.get("B").size());
        assertEquals(1, result.get("C").size());
    }

    @Test
    void mergeCountingCountsOccurrences() {
        Map<String, Integer> result = demo.mergeCounting("A", "B", "A", "C", "B", "A");
        assertEquals(3, result.get("A"));
        assertEquals(2, result.get("B"));
        assertEquals(1, result.get("C"));
    }

    @Test
    void computeUpdatesExistingAndCreatesNew() {
        Map<String, Integer> result = demo.computeDemo();
        assertEquals(15, result.get("A")); // 10 + 5
        assertEquals(42, result.get("C")); // new
    }

    @Test
    void computeIfPresentOnlyUpdatesExisting() {
        Map<String, Integer> result = demo.computeIfPresentDemo();
        assertEquals(20, result.get("A")); // 10 * 2
        assertNull(result.get("X"));
    }

    @Test
    void replaceAllTransformsAllValues() {
        Map<String, Integer> result = demo.replaceAllDemo();
        assertEquals(10, result.get("A"));
        assertEquals(20, result.get("B"));
        assertEquals(30, result.get("C"));
    }
}
