package com.javastudy.collections;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * LinkedHashMap: insertion order (default) or access order.
 *
 * Insertion order: keys are iterated in the order they were first added.
 * Access order: keys are reordered on every get/put (useful for LRU caches).
 */
public class LinkedHashMapDemo {

    /** Default: insertion order is preserved. */
    public List<String> insertionOrder() {
        Map<String, Integer> map = new LinkedHashMap<>();
        map.put("C", 3);
        map.put("A", 1);
        map.put("B", 2);
        return new ArrayList<>(map.keySet());
    }

    /** Access order: every get() moves the entry to the end. */
    public List<String> accessOrder() {
        LinkedHashMap<String, Integer> map = new LinkedHashMap<>(16, 0.75f, true);
        map.put("A", 1);
        map.put("B", 2);
        map.put("C", 3);
        map.get("A"); // moves "A" to the end
        return new ArrayList<>(map.keySet());
    }

    /** LRU cache implementation using access-order LinkedHashMap. */
    public Map<String, String> lruCache(int maxSize) {
        LinkedHashMap<String, String> cache = new LinkedHashMap<>(16, 0.75f, true) {
            @Override
            protected boolean removeEldestEntry(Map.Entry<String, String> eldest) {
                return size() > maxSize;
            }
        };
        cache.put("a", "1");
        cache.put("b", "2");
        cache.put("c", "3");
        cache.put("d", "4"); // "a" is evicted when maxSize=3
        return cache;
    }

    /** Iteration order matches insertion order. */
    public List<Map.Entry<String, Integer>> orderedEntries() {
        Map<String, Integer> map = new LinkedHashMap<>();
        map.put("Z", 26);
        map.put("A", 1);
        map.put("M", 13);
        return new ArrayList<>(map.entrySet());
    }
}
