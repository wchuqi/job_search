package com.javastudy.collections;

import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class HashMapDemoTest {

    private final HashMapDemo demo = new HashMapDemo();

    @Test
    void basicPutGetReturnsCorrectValues() {
        Map<String, Integer> map = demo.basicPutGet();
        assertEquals(3, map.size());
        assertEquals(90, map.get("Alice"));
        assertEquals(85, map.get("Bob"));
        assertEquals(92, map.get("Charlie"));
    }

    @Test
    void getOrDefaultReturnsValueForExistingKey() {
        assertEquals(90, demo.getOrDefaultDemo("Alice"));
    }

    @Test
    void getOrDefaultReturnsDefaultForMissingKey() {
        assertEquals(-1, demo.getOrDefaultDemo("Unknown"));
    }

    @Test
    void preSizedMapContainsAllEntries() {
        Map<String, Integer> map = demo.preSizedMap(100);
        assertEquals(100, map.size());
        assertEquals(0, map.get("key0"));
        assertEquals(99, map.get("key99"));
    }

    @Test
    void putIfAbsentDoesNotOverwrite() {
        Map<String, Integer> map = demo.putIfAbsentDemo();
        assertEquals(1, map.get("A")); // not overwritten with 999
        assertEquals(2, map.get("B"));
    }

    @Test
    void removeRemovesByKey() {
        Map<String, Integer> map = demo.removeDemo();
        assertNull(map.get("B"));
        assertEquals(2, map.size());
    }

    @Test
    void containsKeyAndValue() {
        boolean[] result = demo.containsDemo("X", 10);
        assertTrue(result[0]);  // containsKey("X")
        assertTrue(result[1]);  // containsValue(10)
    }

    @Test
    void containsKeyReturnsFalseForMissing() {
        boolean[] result = demo.containsDemo("Z", 99);
        assertFalse(result[0]);
        assertFalse(result[1]);
    }

    @Test
    void viewsDemoReturnsCorrectCounts() {
        Map<String, Object> result = demo.viewsDemo();
        assertEquals(3, result.get("keyCount"));
        assertEquals(6, result.get("valueSum"));
        assertEquals(3, result.get("entryCount"));
    }
}
