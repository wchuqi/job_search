package com.javastudy.collections;

import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class HashSetDemoTest {

    private final HashSetDemo demo = new HashSetDemo();

    @Test
    void uniqueStringsRemovesDuplicates() {
        Set<String> result = demo.uniqueStrings();
        assertEquals(2, result.size());
        assertTrue(result.contains("A"));
        assertTrue(result.contains("B"));
    }

    @Test
    void containsElementReturnsTrueForPresent() {
        assertTrue(demo.containsElement("X"));
        assertTrue(demo.containsElement("Y"));
    }

    @Test
    void containsElementReturnsFalseForAbsent() {
        assertFalse(demo.containsElement("W"));
    }

    @Test
    void customPointEquality() {
        HashSetDemo.Point p1 = new HashSetDemo.Point(1, 2);
        HashSetDemo.Point p2 = new HashSetDemo.Point(1, 2);
        assertEquals(p1, p2);
        assertEquals(p1.hashCode(), p2.hashCode());
    }

    @Test
    void addPointsRejectsDuplicates() {
        assertEquals(2, demo.addPoints());
    }

    @Test
    void unionCombinesAllElements() {
        Set<String> a = Set.of("A", "B");
        Set<String> b = Set.of("B", "C");
        Set<String> result = demo.union(a, b);
        assertEquals(3, result.size());
        assertTrue(result.containsAll(Set.of("A", "B", "C")));
    }

    @Test
    void intersectionReturnsCommonElements() {
        Set<String> a = Set.of("A", "B", "C");
        Set<String> b = Set.of("B", "C", "D");
        Set<String> result = demo.intersection(a, b);
        assertEquals(Set.of("B", "C"), result);
    }

    @Test
    void differenceReturnsElementsOnlyInFirst() {
        Set<String> a = Set.of("A", "B", "C");
        Set<String> b = Set.of("B", "C", "D");
        Set<String> result = demo.difference(a, b);
        assertEquals(Set.of("A"), result);
    }
}
