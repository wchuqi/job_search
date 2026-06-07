package com.javastudy.collections;

import com.javastudy.Generated;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Immutable collections: List.of, Set.of, Map.of, List.copyOf.
 *
 * These return unmodifiable collections that throw UnsupportedOperationException
 * on any mutation attempt. No null elements allowed.
 */
public class ImmutableCollectionsDemo {

    /** List.of creates an immutable list. */
    public List<String> immutableList() {
        return List.of("A", "B", "C");
    }

    /** Set.of creates an immutable set. */
    public Set<String> immutableSet() {
        return Set.of("X", "Y", "Z");
    }

    /** Map.of creates an immutable map (max 10 key-value pairs). */
    public Map<String, Integer> immutableMap() {
        return Map.of("A", 1, "B", 2, "C", 3);
    }

    /** Map.ofEntries for more than 10 entries. */
    public Map<String, Integer> immutableMapLarge() {
        return Map.ofEntries(
            Map.entry("A", 1),
            Map.entry("B", 2),
            Map.entry("C", 3),
            Map.entry("D", 4),
            Map.entry("E", 5),
            Map.entry("F", 6),
            Map.entry("G", 7),
            Map.entry("H", 8),
            Map.entry("I", 9),
            Map.entry("J", 10),
            Map.entry("K", 11)
        );
    }

    /** List.copyOf creates an immutable copy from an existing collection. */
    public List<String> copyOfDemo() {
        List<String> mutable = new ArrayList<>(List.of("X", "Y", "Z"));
        List<String> immutable = List.copyOf(mutable);
        // mutating original does not affect the copy
        mutable.add("W");
        return immutable;
    }

    /** Set.copyOf. */
    public Set<Integer> setCopyOfDemo() {
        return Set.copyOf(List.of(1, 2, 3, 2, 1)); // {1, 2, 3}
    }

    /** Map.copyOf. */
    public Map<String, Integer> mapCopyOfDemo() {
        Map<String, Integer> mutable = new HashMap<>();
        mutable.put("A", 1);
        mutable.put("B", 2);
        return Map.copyOf(mutable);
    }

    /** Attempting to modify throws UnsupportedOperationException. */
    public boolean tryAddToList(List<String> list) {
        try {
            list.add("Z");
            return true;
        } catch (UnsupportedOperationException e) {
            return false;
        }
    }

    /** Attempting to add null throws NullPointerException. */
    @Generated
    public boolean tryAddNull() {
        try {
            List.of("A", null);
            return true;
        } catch (NullPointerException e) {
            return false;
        }
    }
}
