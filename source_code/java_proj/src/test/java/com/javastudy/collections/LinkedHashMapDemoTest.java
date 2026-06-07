package com.javastudy.collections;

import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class LinkedHashMapDemoTest {

    private final LinkedHashMapDemo demo = new LinkedHashMapDemo();

    @Test
    void insertionOrderIsPreserved() {
        List<String> result = demo.insertionOrder();
        assertEquals(List.of("C", "A", "B"), result);
    }

    @Test
    void accessOrderMovesGetToEnd() {
        List<String> result = demo.accessOrder();
        assertEquals(List.of("B", "C", "A"), result);
    }

    @Test
    void lruCacheEvictsOldest() {
        Map<String, String> cache = demo.lruCache(3);
        assertEquals(3, cache.size());
        assertNull(cache.get("a")); // evicted
        assertNotNull(cache.get("b"));
        assertNotNull(cache.get("c"));
        assertNotNull(cache.get("d"));
    }

    @Test
    void orderedEntriesMatchInsertionOrder() {
        var entries = demo.orderedEntries();
        assertEquals("Z", entries.get(0).getKey());
        assertEquals("A", entries.get(1).getKey());
        assertEquals("M", entries.get(2).getKey());
    }
}
