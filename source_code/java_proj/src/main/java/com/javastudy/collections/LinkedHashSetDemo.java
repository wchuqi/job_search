package com.javastudy.collections;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

/**
 * LinkedHashSet preserves insertion order.
 *
 * Backed by a hash table + doubly-linked list.
 * Slightly more memory overhead than HashSet, but iteration order is predictable.
 */
public class LinkedHashSetDemo {

    /** Elements are iterated in insertion order. */
    public List<String> insertionOrder() {
        Set<String> set = new LinkedHashSet<>();
        set.add("C");
        set.add("A");
        set.add("B");
        return new ArrayList<>(set);
    }

    /** Duplicates are still rejected, but order of first insertion is kept. */
    public List<String> duplicatesIgnored() {
        Set<String> set = new LinkedHashSet<>();
        set.add("A");
        set.add("B");
        set.add("A"); // ignored
        set.add("C");
        return new ArrayList<>(set);
    }

    /** Remove and re-add: element goes to the end. */
    public List<String> removeAndReAdd() {
        Set<String> set = new LinkedHashSet<>(List.of("A", "B", "C"));
        set.remove("A");
        set.add("A"); // now at the end
        return new ArrayList<>(set);
    }

    /** Construction from an existing collection preserves order. */
    public List<String> fromCollection(List<String> source) {
        Set<String> set = new LinkedHashSet<>(source);
        return new ArrayList<>(set);
    }
}
