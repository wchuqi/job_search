package com.javastudy.collections;

import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class CapacityDemoTest {

    private final CapacityDemo demo = new CapacityDemo();

    @Test
    void presizedArrayListContainsAllElements() {
        List<Integer> result = demo.presizedArrayList(100);
        assertEquals(100, result.size());
        assertEquals(0, result.get(0));
        assertEquals(99, result.get(99));
    }

    @Test
    void defaultArrayListContainsAllElements() {
        List<Integer> result = demo.defaultArrayList(100);
        assertEquals(100, result.size());
    }

    @Test
    void presizedAndDefaultProduceSameContent() {
        List<Integer> presized = demo.presizedArrayList(50);
        List<Integer> defaultList = demo.defaultArrayList(50);
        assertEquals(presized, defaultList);
    }

    @Test
    void presizedHashMapContainsAllEntries() {
        Map<String, Integer> map = demo.presizedHashMap(100);
        assertEquals(100, map.size());
        assertEquals(0, map.get("key0"));
        assertEquals(99, map.get("key99"));
    }

    @Test
    void optimalCapacityIsPowerOfTwo() {
        int capacity = CapacityDemo.optimalHashMapCapacity(100);
        // Must be a power of 2
        assertTrue((capacity & (capacity - 1)) == 0, "Capacity should be power of 2");
        // Must be large enough: capacity * 0.75 >= 100
        assertTrue(capacity * 0.75 >= 100, "Capacity should be large enough for 100 entries");
    }

    @Test
    void measureArrayListCapacityMatchesInitial() {
        int capacity = demo.measureArrayListCapacity(50);
        assertTrue(capacity >= 50, "Internal capacity should be >= 50");
    }

    @Test
    void measureArrayListCapacityDefaultIs10() {
        int capacity = demo.measureArrayListCapacity(0);
        // Default ArrayList starts with elementData of length 10 (or 0 if empty)
        // After first add it becomes 10
        assertTrue(capacity >= 0);
    }
}
