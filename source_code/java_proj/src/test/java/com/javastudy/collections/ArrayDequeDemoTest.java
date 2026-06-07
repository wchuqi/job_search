package com.javastudy.collections;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ArrayDequeDemoTest {

    private final ArrayDequeDemo demo = new ArrayDequeDemo();

    @Test
    void stackDemoFollowsLIFO() {
        List<String> result = demo.stackDemo();
        assertEquals("C", result.get(0)); // peek
        assertEquals("C", result.get(1)); // pop
        assertEquals("B", result.get(2)); // pop
    }

    @Test
    void queueDemoFollowsFIFO() {
        List<String> result = demo.queueDemo();
        assertEquals("A", result.get(0)); // peek
        assertEquals("A", result.get(1)); // poll
        assertEquals("B", result.get(2)); // poll
    }

    @Test
    void firstLastReturnsCorrectElements() {
        List<String> result = demo.firstLastDemo();
        assertEquals("A", result.get(0)); // getFirst
        assertEquals("D", result.get(1)); // getLast
    }

    @Test
    void emptyDequeIsEmpty() {
        assertTrue(demo.emptyCheck());
    }

    @Test
    void sizeAfterBulkReturnsCorrectCount() {
        assertEquals(5, demo.sizeAfterBulk());
    }
}
