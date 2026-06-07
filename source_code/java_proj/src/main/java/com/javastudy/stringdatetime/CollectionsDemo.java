package com.javastudy.stringdatetime;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Demonstrates java.util.Collections utility methods for unmodifiable collections.
 * <p>
 * Key points:
 * <ul>
 *   <li>Collections.unmodifiableList() - wraps a list, blocking add/remove/set</li>
 *   <li>Collections.emptyList() - immutable empty list (singleton)</li>
 *   <li>Collections.singletonList() - immutable list with exactly one element</li>
 *   <li>Collections.unmodifiableMap() / unmodifiableSet() - same for Map/Set</li>
 *   <li>List.of() (Java 9+) is preferred for creating small immutable lists</li>
 *   <li>Unmodifiable != Unmodifiable: modifying the backing list still affects the view!</li>
 * </ul>
 */
public class CollectionsDemo {

    /**
     * Create an unmodifiable list from an existing list.
     * The returned list throws UnsupportedOperationException on mutation attempts.
     * WARNING: changes to the original list are still visible through the unmodifiable view.
     */
    public static List<String> unmodifiableList(List<String> original) {
        return Collections.unmodifiableList(original);
    }

    /**
     * Collections.emptyList() returns a shared immutable empty list.
     */
    public static List<String> emptyList() {
        return Collections.emptyList();
    }

    /**
     * Collections.singletonList() returns an immutable list with one element.
     */
    public static List<String> singletonList(String element) {
        return Collections.singletonList(element);
    }

    /**
     * Collections.emptyMap() returns a shared immutable empty map.
     */
    public static Map<String, Integer> emptyMap() {
        return Collections.emptyMap();
    }

    /**
     * Collections.emptySet() returns a shared immutable empty set.
     */
    public static Set<String> emptySet() {
        return Collections.emptySet();
    }

    /**
     * Collections.singletonMap() returns an immutable map with one entry.
     */
    public static Map<String, Integer> singletonMap(String key, Integer value) {
        return Collections.singletonMap(key, value);
    }

    /**
     * Collections.singleton() returns an immutable set with one element.
     */
    public static Set<String> singleton(String element) {
        return Collections.singleton(element);
    }

    /**
     * List.of() (Java 9+) creates an immutable list directly.
     * No backing list - truly immutable from creation.
     */
    public static List<String> listOf(String... items) {
        return List.of(items);
    }

    /**
     * Collections.unmodifiableSet() wraps a set.
     */
    public static Set<String> unmodifiableSet(Set<String> original) {
        return Collections.unmodifiableSet(original);
    }

    /**
     * Collections.unmodifiableMap() wraps a map.
     */
    public static Map<String, Integer> unmodifiableMap(Map<String, Integer> original) {
        return Collections.unmodifiableMap(original);
    }

    /**
     * Collections.sort() sorts a list in-place.
     */
    public static List<String> sort(List<String> list) {
        List<String> copy = new java.util.ArrayList<>(list);
        Collections.sort(copy);
        return copy;
    }

    /**
     * Collections.reverse() reverses a list in-place.
     */
    public static List<String> reverse(List<String> list) {
        List<String> copy = new java.util.ArrayList<>(list);
        Collections.reverse(copy);
        return copy;
    }

    /**
     * Collections.shuffle() randomizes list order.
     */
    public static List<String> shuffle(List<String> list) {
        List<String> copy = new java.util.ArrayList<>(list);
        Collections.shuffle(copy);
        return copy;
    }
}
