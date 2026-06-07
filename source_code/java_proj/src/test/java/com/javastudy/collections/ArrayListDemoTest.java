package com.javastudy.collections;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ArrayListDemoTest {

    private final ArrayListDemo demo = new ArrayListDemo();

    @Test
    void basicOperationsReturnsThreeElements() {
        List<String> result = demo.basicOperations();
        assertEquals(3, result.size());
        assertEquals("A", result.get(0));
        assertEquals("B", result.get(1));
        assertEquals("C", result.get(2));
    }

    @Test
    void insertAtPlacesElementAtIndex() {
        List<String> result = demo.insertAt(1, "X");
        assertEquals(List.of("A", "X", "B", "C"), result);
    }

    @Test
    void insertAtBeginning() {
        List<String> result = demo.insertAt(0, "FIRST");
        assertEquals("FIRST", result.get(0));
        assertEquals(4, result.size());
    }

    @Test
    void removeElementsRemovesByObjectAndIndex() {
        List<String> result = demo.removeElements();
        // Original: ["A", "B", "C", "B"]
        // remove("B") -> ["A", "C", "B"]
        // remove(0) -> ["C", "B"]
        assertEquals(List.of("C", "B"), result);
    }

    @Test
    void preallocatedCapacityStartsEmpty() {
        assertEquals(0, demo.preallocatedCapacity(100));
    }

    @Test
    void internalCapacityMatchesPreallocation() {
        List<String> list = new java.util.ArrayList<>(50);
        int capacity = demo.getInternalCapacity(list);
        assertTrue(capacity >= 50, "Internal capacity should be at least 50");
    }

    @Test
    void setElementReplacesAtIndex() {
        List<String> result = demo.setElement(1, "Z");
        assertEquals(List.of("A", "Z", "C"), result);
    }

    @Test
    void subListViewReturnsCorrectRange() {
        List<String> result = demo.subListView();
        assertEquals(List.of("B", "C", "D"), result);
    }
}
