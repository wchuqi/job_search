package com.javastudy.stringdatetime;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class CollectionsDemoTest {

    @Test
    void unmodifiableListBlocksAdd() {
        List<String> original = new ArrayList<>(List.of("a", "b"));
        List<String> unmodifiable = CollectionsDemo.unmodifiableList(original);
        assertThrows(UnsupportedOperationException.class, () -> unmodifiable.add("c"));
    }

    @Test
    void unmodifiableListBlocksRemove() {
        List<String> original = new ArrayList<>(List.of("a", "b"));
        List<String> unmodifiable = CollectionsDemo.unmodifiableList(original);
        assertThrows(UnsupportedOperationException.class, () -> unmodifiable.remove(0));
    }

    @Test
    void unmodifiableListBlocksSet() {
        List<String> original = new ArrayList<>(List.of("a", "b"));
        List<String> unmodifiable = CollectionsDemo.unmodifiableList(original);
        assertThrows(UnsupportedOperationException.class, () -> unmodifiable.set(0, "x"));
    }

    @Test
    void unmodifiableListViewReflectsOriginalChanges() {
        List<String> original = new ArrayList<>(List.of("a", "b"));
        List<String> unmodifiable = CollectionsDemo.unmodifiableList(original);
        original.add("c");
        // The unmodifiable view reflects changes to the backing list
        assertEquals(3, unmodifiable.size());
        assertEquals("c", unmodifiable.get(2));
    }

    @Test
    void emptyListIsImmutable() {
        List<String> list = CollectionsDemo.emptyList();
        assertTrue(list.isEmpty());
        assertThrows(UnsupportedOperationException.class, () -> list.add("x"));
    }

    @Test
    void emptyListIsSingleton() {
        assertSame(CollectionsDemo.emptyList(), CollectionsDemo.emptyList(),
                "emptyList() should return the same instance");
    }

    @Test
    void singletonListContainsOneElement() {
        List<String> list = CollectionsDemo.singletonList("hello");
        assertEquals(1, list.size());
        assertEquals("hello", list.get(0));
    }

    @Test
    void singletonListIsImmutable() {
        List<String> list = CollectionsDemo.singletonList("hello");
        assertThrows(UnsupportedOperationException.class, () -> list.add("x"));
        assertThrows(UnsupportedOperationException.class, () -> list.remove(0));
    }

    @Test
    void emptyMapIsImmutable() {
        Map<String, Integer> map = CollectionsDemo.emptyMap();
        assertTrue(map.isEmpty());
        assertThrows(UnsupportedOperationException.class, () -> map.put("x", 1));
    }

    @Test
    void emptySetIsImmutable() {
        Set<String> set = CollectionsDemo.emptySet();
        assertTrue(set.isEmpty());
        assertThrows(UnsupportedOperationException.class, () -> set.add("x"));
    }

    @Test
    void singletonMapContainsOneEntry() {
        Map<String, Integer> map = CollectionsDemo.singletonMap("key", 42);
        assertEquals(1, map.size());
        assertEquals(42, map.get("key"));
    }

    @Test
    void singletonSetContainsOneElement() {
        Set<String> set = CollectionsDemo.singleton("hello");
        assertEquals(1, set.size());
        assertTrue(set.contains("hello"));
    }

    @Test
    void listOfCreatesImmutableList() {
        List<String> list = CollectionsDemo.listOf("a", "b", "c");
        assertEquals(3, list.size());
        assertThrows(UnsupportedOperationException.class, () -> list.add("d"));
    }

    @Test
    void unmodifiableSetBlocksAdd() {
        Set<String> original = new java.util.HashSet<>(Set.of("a", "b"));
        Set<String> unmodifiable = CollectionsDemo.unmodifiableSet(original);
        assertThrows(UnsupportedOperationException.class, () -> unmodifiable.add("c"));
    }

    @Test
    void unmodifiableMapBlocksPut() {
        Map<String, Integer> original = new java.util.HashMap<>(Map.of("a", 1));
        Map<String, Integer> unmodifiable = CollectionsDemo.unmodifiableMap(original);
        assertThrows(UnsupportedOperationException.class, () -> unmodifiable.put("b", 2));
    }

    @Test
    void sortSortsList() {
        List<String> sorted = CollectionsDemo.sort(List.of("banana", "apple", "cherry"));
        assertEquals(List.of("apple", "banana", "cherry"), sorted);
    }

    @Test
    void reverseReversesList() {
        List<String> reversed = CollectionsDemo.reverse(List.of("a", "b", "c"));
        assertEquals(List.of("c", "b", "a"), reversed);
    }

    @Test
    void shufflePreservesElements() {
        List<String> original = List.of("a", "b", "c", "d", "e");
        List<String> shuffled = CollectionsDemo.shuffle(original);
        assertEquals(original.size(), shuffled.size());
        assertTrue(shuffled.containsAll(original));
    }
}
