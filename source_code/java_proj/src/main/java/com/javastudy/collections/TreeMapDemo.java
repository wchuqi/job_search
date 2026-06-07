package com.javastudy.collections;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * TreeMap: sorted keys, custom comparator, range views.
 *
 * Backed by a Red-Tree. O(log n) for get/put/remove.
 * Keys must be Comparable or a Comparator must be provided.
 */
public class TreeMapDemo {

    /** Natural key ordering. */
    public List<String> naturalOrder() {
        TreeMap<String, Integer> map = new TreeMap<>();
        map.put("Banana", 2);
        map.put("Apple", 1);
        map.put("Cherry", 3);
        return new ArrayList<>(map.keySet()); // [Apple, Banana, Cherry]
    }

    /** Custom comparator: reverse order. */
    public List<String> reverseOrder() {
        TreeMap<String, Integer> map = new TreeMap<>(Comparator.reverseOrder());
        map.put("Banana", 2);
        map.put("Apple", 1);
        map.put("Cherry", 3);
        return new ArrayList<>(map.keySet());
    }

    /** firstKey() and lastKey(). */
    public List<String> firstAndLast() {
        TreeMap<String, Integer> map = new TreeMap<>(Map.of("A", 1, "B", 2, "C", 3));
        return List.of(map.firstKey(), map.lastKey());
    }

    /** headMap, tailMap, subMap for range views. */
    public Map<String, Integer> rangeViews() {
        TreeMap<String, Integer> map = new TreeMap<>();
        map.put("A", 1);
        map.put("B", 2);
        map.put("C", 3);
        map.put("D", 4);
        map.put("E", 5);
        // headMap: keys < "D"
        Map<String, Integer> head = map.headMap("D");
        // tailMap: keys >= "C"
        Map<String, Integer> tail = map.tailMap("C");
        // subMap: "B" <= key < "E"
        Map<String, Integer> sub = map.subMap("B", "E");
        // Return counts to verify
        return Map.of("head", head.size(), "tail", tail.size(), "sub", sub.size());
    }

    /** Custom comparator by string length. */
    public List<String> sortByLength() {
        TreeMap<String, Integer> map = new TreeMap<>(Comparator.comparingInt(String::length));
        map.put("Banana", 2);
        map.put("Fig", 3);
        map.put("Apple", 1);
        map.put("Kiwi", 4);
        return new ArrayList<>(map.keySet());
    }

    /** NavigableMap features: lower, higher, floor, ceiling. */
    public Map<String, Integer> navigableOperations() {
        TreeMap<Integer, String> map = new TreeMap<>();
        map.put(10, "A");
        map.put(20, "B");
        map.put(30, "C");
        map.put(40, "D");
        return Map.of(
            "lower", map.lowerKey(20),       // 10 (greatest < 20)
            "higher", map.higherKey(20),      // 30 (least > 20)
            "floor", map.floorKey(25),        // 20 (greatest <= 25)
            "ceiling", map.ceilingKey(25)     // 30 (least >= 25)
        );
    }
}
