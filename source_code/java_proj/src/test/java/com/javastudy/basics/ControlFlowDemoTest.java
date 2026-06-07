package com.javastudy.basics;

import org.junit.jupiter.api.Test;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

class ControlFlowDemoTest {

    @Test
    void testClassifyScore() {
        assertEquals("A", ControlFlowDemo.classifyScore(95));
        assertEquals("B", ControlFlowDemo.classifyScore(85));
        assertEquals("C", ControlFlowDemo.classifyScore(75));
        assertEquals("D", ControlFlowDemo.classifyScore(65));
        assertEquals("F", ControlFlowDemo.classifyScore(50));
    }

    @Test
    void testSumWithForLoop() {
        assertEquals(55, ControlFlowDemo.sumWithForLoop(10));
        assertEquals(1, ControlFlowDemo.sumWithForLoop(1));
        assertEquals(0, ControlFlowDemo.sumWithForLoop(0));
    }

    @Test
    void testSumWithForEach() {
        assertEquals(15, ControlFlowDemo.sumWithForEach(List.of(1, 2, 3, 4, 5)));
        assertEquals(0, ControlFlowDemo.sumWithForEach(List.of()));
    }

    @Test
    void testSumWithWhile() {
        assertEquals(55, ControlFlowDemo.sumWithWhile(10));
    }

    @Test
    void testSumWithDoWhile() {
        assertEquals(55, ControlFlowDemo.sumWithDoWhile(10));
    }

    @Test
    void testSumUntilNegative() {
        assertEquals(6, ControlFlowDemo.sumUntilNegative(new int[]{2, 4, -1, 6}));
        assertEquals(20, ControlFlowDemo.sumUntilNegative(new int[]{2, 4, 6, 8}));
    }
}
