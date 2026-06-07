package com.javastudy.collections;

import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class ImmutableCollectionsDemoTest {

    private final ImmutableCollectionsDemo demo = new ImmutableCollectionsDemo();

    @Test
    void immutableListContainsElements() {
        List<String> list = demo.immutableList();
        assertEquals(3, list.size());
        assertEquals(List.of("A", "B", "C"), list);
    }

    @Test
    void immutableSetContainsElements() {
        Set<String> set = demo.immutableSet();
        assertEquals(3, set.size());
        assertTrue(set.containsAll(Set.of("X", "Y", "Z")));
    }

    @Test
    void immutableMapContainsEntries() {
        Map<String, Integer> map = demo.immutableMap();
        assertEquals(3, map.size());
        assertEquals(1, map.get("A"));
    }

    @Test
    void immutableMapLargeContainsAllEntries() {
        Map<String, Integer> map = demo.immutableMapLarge();
        assertEquals(11, map.size());
        assertEquals(1, map.get("A"));
        assertEquals(11, map.get("K"));
    }

    @Test
    void copyOfCreatesIndependentCopy() {
        List<String> result = demo.copyOfDemo();
        assertEquals(List.of("X", "Y", "Z"), result);
        assertEquals(3, result.size()); // "W" was not added to the copy
    }

    @Test
    void setCopyOfRemovesDuplicates() {
        assertEquals(Set.of(1, 2, 3), demo.setCopyOfDemo());
    }

    @Test
    void mapCopyOfCreatesImmutableCopy() {
        Map<String, Integer> map = demo.mapCopyOfDemo();
        assertEquals(2, map.size());
        assertThrows(UnsupportedOperationException.class, () -> map.put("C", 3));
    }

    @Test
    void immutableListThrowsOnAdd() {
        assertFalse(demo.tryAddToList(List.of("A", "B")));
    }

    @Test
    void immutableListThrowsOnNull() {
        assertFalse(demo.tryAddNull());
    }
}
