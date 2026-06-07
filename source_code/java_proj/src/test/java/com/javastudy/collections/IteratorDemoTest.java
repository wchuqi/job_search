package com.javastudy.collections;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class IteratorDemoTest {

    private final IteratorDemo demo = new IteratorDemo();

    @Test
    void basicIterationVisitsAllElements() {
        List<String> result = demo.basicIteration();
        assertEquals(List.of("A", "B", "C", "D"), result);
    }

    @Test
    void iteratorRemoveRemovesCorrectElements() {
        List<String> result = demo.iteratorRemove();
        assertEquals(List.of("A", "C", "E"), result);
    }

    @Test
    void failFastThrowsConcurrentModificationException() {
        assertTrue(demo.failFastDemo());
    }

    @Test
    void removeIfRemovesMatchingElements() {
        List<String> result = demo.removeIfDemo();
        assertEquals(List.of("A", "C", "E"), result);
    }

    @Test
    void removeIfNumbersRemovesEvenNumbers() {
        List<Integer> result = demo.removeIfNumbers();
        assertEquals(List.of(1, 3, 5, 7, 9), result);
    }

    @Test
    void listIteratorCanSetElements() {
        List<String> result = demo.listIteratorDemo();
        assertEquals(List.of("A", "BB", "C"), result);
    }

    @Test
    void forEachRemainingProcessesRemainingElements() {
        List<String> result = demo.forEachRemainingDemo();
        assertEquals(List.of("B", "C", "D", "E"), result);
    }
}
