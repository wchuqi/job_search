package com.javastudy.collections;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class LinkedHashSetDemoTest {

    private final LinkedHashSetDemo demo = new LinkedHashSetDemo();

    @Test
    void insertionOrderIsPreserved() {
        List<String> result = demo.insertionOrder();
        assertEquals(List.of("C", "A", "B"), result);
    }

    @Test
    void duplicatesIgnoredKeepsFirstInsertionOrder() {
        List<String> result = demo.duplicatesIgnored();
        assertEquals(List.of("A", "B", "C"), result);
    }

    @Test
    void removeAndReAddMovesToEnd() {
        List<String> result = demo.removeAndReAdd();
        assertEquals(List.of("B", "C", "A"), result);
    }

    @Test
    void fromCollectionPreservesOrder() {
        List<String> source = List.of("C", "A", "B", "A");
        List<String> result = demo.fromCollection(source);
        assertEquals(List.of("C", "A", "B"), result);
    }
}
