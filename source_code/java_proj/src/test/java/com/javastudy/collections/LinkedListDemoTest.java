package com.javastudy.collections;

import org.junit.jupiter.api.Test;

import java.util.Deque;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class LinkedListDemoTest {

    private final LinkedListDemo demo = new LinkedListDemo();

    @Test
    void asListReturnsListInterface() {
        List<String> result = demo.asList();
        assertEquals(3, result.size());
        assertEquals("A", result.get(0));
    }

    @Test
    void asDequeReturnsDequeInterface() {
        Deque<String> result = demo.asDeque();
        assertEquals("A", result.peekFirst());
        assertEquals("C", result.peekLast());
    }

    @Test
    void stackOperationsLIFO() {
        assertEquals("C", demo.stackOperations());
    }

    @Test
    void queueOperationsFIFO() {
        assertEquals("A", demo.queueOperations());
    }

    @Test
    void peekOperationsReturnsHeadAndTail() {
        List<String> result = demo.peekOperations();
        assertEquals("A", result.get(0));
        assertEquals("C", result.get(1));
    }

    @Test
    void sizeReturnsCorrectCount() {
        assertEquals(3, demo.size());
    }
}
