package com.javastudy.collections;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Advanced Map operations: getOrDefault, computeIfAbsent, merge.
 *
 * These methods reduce boilerplate and avoid redundant lookups.
 */
public class MapAdvancedDemo {

    /** getOrDefault: return a default if key is missing. */
    public int getOrDefaultDemo(String key) {
        Map<String, Integer> map = Map.of("A", 1, "B", 2);
        return map.getOrDefault(key, 0);
    }

    /** computeIfAbsent: lazily compute and insert if key absent. */
    public Map<String, List<String>> groupByFirstChar(String... words) {
        Map<String, List<String>> map = new HashMap<>();
        for (String word : words) {
            String key = word.substring(0, 1).toUpperCase();
            map.computeIfAbsent(key, k -> new java.util.ArrayList<>()).add(word);
        }
        return map;
    }

    /** merge: combine old and new values atomically. */
    public Map<String, Integer> mergeCounting(String... items) {
        Map<String, Integer> map = new HashMap<>();
        for (String item : items) {
            map.merge(item, 1, Integer::sum);
        }
        return map;
    }

    /** compute: transform a value in-place. */
    public Map<String, Integer> computeDemo() {
        Map<String, Integer> map = new HashMap<>(Map.of("A", 10, "B", 20));
        map.compute("A", (k, v) -> v == null ? 0 : v + 5);  // A=15
        map.compute("C", (k, v) -> v == null ? 42 : v);      // C=42
        return map;
    }

    /** computeIfPresent: only compute if key exists. */
    public Map<String, Integer> computeIfPresentDemo() {
        Map<String, Integer> map = new HashMap<>(Map.of("A", 10, "B", 20));
        map.computeIfPresent("A", (k, v) -> v * 2);  // A=20
        map.computeIfPresent("X", (k, v) -> 99);      // no effect
        return map;
    }

    /** replaceAll: transform all values. */
    public Map<String, Integer> replaceAllDemo() {
        Map<String, Integer> map = new HashMap<>(Map.of("A", 1, "B", 2, "C", 3));
        map.replaceAll((k, v) -> v * 10);
        return map;
    }
}
