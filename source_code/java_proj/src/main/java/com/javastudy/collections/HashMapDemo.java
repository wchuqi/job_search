package com.javastudy.collections;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * HashMap: put/get/getOrDefault, initial capacity.
 *
 * Backed by an array of Node (linked list / tree at high collision).
 * Default load factor 0.75; resizes when size > capacity * loadFactor.
 */
public class HashMapDemo {

    /** Basic put and get. */
    public Map<String, Integer> basicPutGet() {
        Map<String, Integer> map = new HashMap<>();
        map.put("Alice", 90);
        map.put("Bob", 85);
        map.put("Charlie", 92);
        return map;
    }

    /** getOrDefault returns a fallback for missing keys. */
    public int getOrDefaultDemo(String key) {
        Map<String, Integer> map = Map.of("Alice", 90, "Bob", 85);
        return map.getOrDefault(key, -1);
    }

    /** Pre-sized HashMap to avoid rehashing. */
    public Map<String, Integer> preSizedMap(int expectedSize) {
        // capacity = expectedSize / loadFactor, rounded up
        Map<String, Integer> map = new HashMap<>(expectedSize);
        for (int i = 0; i < expectedSize; i++) {
            map.put("key" + i, i);
        }
        return map;
    }

    /** putIfAbsent only inserts if key is not already present. */
    public Map<String, Integer> putIfAbsentDemo() {
        Map<String, Integer> map = new HashMap<>();
        map.put("A", 1);
        map.putIfAbsent("A", 999); // no effect
        map.putIfAbsent("B", 2);
        return map;
    }

    /** Remove by key. */
    public Map<String, Integer> removeDemo() {
        Map<String, Integer> map = new HashMap<>(Map.of("A", 1, "B", 2, "C", 3));
        map.remove("B");
        return map;
    }

    /** containsKey and containsValue. */
    public boolean[] containsDemo(String key, int value) {
        Map<String, Integer> map = Map.of("X", 10, "Y", 20);
        return new boolean[]{map.containsKey(key), map.containsValue(value)};
    }

    /** keySet, values, entrySet. */
    public Map<String, Object> viewsDemo() {
        Map<String, Integer> map = Map.of("A", 1, "B", 2, "C", 3);
        Set<String> keys = map.keySet();
        var values = map.values();
        var entries = map.entrySet();
        return Map.of(
            "keyCount", keys.size(),
            "valueSum", values.stream().mapToInt(Integer::intValue).sum(),
            "entryCount", entries.size()
        );
    }
}
