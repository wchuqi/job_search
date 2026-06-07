package com.javastudy.collections;

import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class SequencedCollectionsDemoTest {

    private final SequencedCollectionsDemo demo = new SequencedCollectionsDemo();

    @Test
    void firstAndLastReturnsCorrectElements() {
        List<String> result = demo.firstAndLast();
        assertEquals("A", result.get(0));
        assertEquals("D", result.get(1));
    }

    @Test
    void addFirstLastInsertsAtCorrectPositions() {
        List<String> result = demo.addFirstLast();
        assertEquals(List.of("A", "B", "C", "D"), result);
    }

    @Test
    void reversedViewReturnsReversedOrder() {
        List<String> result = demo.reversedView();
        assertEquals(List.of("C", "B", "A"), result);
    }

    @Test
    void sequencedSetReversedOrder() {
        List<String> result = demo.sequencedSetDemo();
        assertEquals(List.of("B", "A", "C"), result);
    }

    @Test
    void sequencedMapReversed() {
        Map<String, Integer> result = demo.sequencedMapDemo();
        var keys = new java.util.ArrayList<>(result.keySet());
        assertEquals(List.of("B", "A", "C"), keys);
    }

    @Test
    void putFirstLastMapMaintainsOrder() {
        List<String> result = demo.putFirstLastMap();
        assertEquals(List.of("A", "B", "C"), result);
    }
}
