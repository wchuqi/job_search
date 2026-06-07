package com.javastudy.collections;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * ConcurrentHashMap: thread-safe map with merge/compute.
 *
 * Fine-grained locking (bucket-level) instead of a single lock.
 * Supports atomic operations: merge, compute, computeIfAbsent, computeIfPresent.
 */
public class ConcurrentHashMapDemo {

    /** Basic thread-safe put and get. */
    public Map<String, Integer> basicOperations() {
        ConcurrentHashMap<String, Integer> map = new ConcurrentHashMap<>();
        map.put("A", 1);
        map.put("B", 2);
        map.put("C", 3);
        return map;
    }

    /** merge: atomically combine existing value with a new one. */
    public Map<String, Integer> mergeDemo() {
        ConcurrentHashMap<String, Integer> map = new ConcurrentHashMap<>();
        map.put("A", 1);
        // If "A" exists, apply (old, new) -> old + new
        map.merge("A", 10, Integer::sum);  // A=11
        map.merge("B", 5, Integer::sum);   // B=5 (new entry)
        return map;
    }

    /** compute: atomically compute a new value for a key. */
    public Map<String, Integer> computeDemo() {
        ConcurrentHashMap<String, Integer> map = new ConcurrentHashMap<>();
        map.put("count", 0);
        map.compute("count", (key, val) -> val + 1);
        map.compute("count", (key, val) -> val + 1);
        return map;
    }

    /** computeIfAbsent: compute only if key is absent. */
    public Map<String, String> computeIfAbsentDemo() {
        ConcurrentHashMap<String, String> map = new ConcurrentHashMap<>();
        map.put("A", "existing");
        map.computeIfAbsent("A", key -> "new_" + key); // no change
        map.computeIfAbsent("B", key -> "new_" + key); // creates B
        return map;
    }

    /** computeIfPresent: compute only if key is present. */
    public Map<String, Integer> computeIfPresentDemo() {
        ConcurrentHashMap<String, Integer> map = new ConcurrentHashMap<>();
        map.put("A", 10);
        map.computeIfPresent("A", (key, val) -> val * 2); // A=20
        map.computeIfPresent("B", (key, val) -> val * 2); // no effect
        return map;
    }

    /** Atomic counter pattern using merge. */
    public int atomicCounter(String... keys) {
        ConcurrentHashMap<String, Integer> map = new ConcurrentHashMap<>();
        for (String key : keys) {
            map.merge(key, 1, Integer::sum);
        }
        return map.values().stream().mapToInt(Integer::intValue).sum();
    }

    /** replace: atomically replace a value. */
    public Map<String, Integer> replaceDemo() {
        ConcurrentHashMap<String, Integer> map = new ConcurrentHashMap<>();
        map.put("A", 1);
        map.replace("A", 2);        // A=2
        map.replace("A", 2, 99);    // A=99 (CAS: old=2, new=99)
        map.replace("B", 1);        // no effect (B absent)
        return map;
    }
}
