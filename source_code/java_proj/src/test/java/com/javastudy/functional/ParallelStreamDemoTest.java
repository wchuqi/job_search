package com.javastudy.functional;

import org.junit.jupiter.api.Test;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

class ParallelStreamDemoTest {

    @Test
    void parallelSumComputesCorrectly() {
        assertEquals(10, ParallelStreamDemo.parallelSum(List.of(1, 2, 3, 4)));
    }

    @Test
    void parallelSumHandlesEmptyList() {
        assertEquals(0, ParallelStreamDemo.parallelSum(List.of()));
    }

    @Test
    void parallelFilterKeepsPositiveNumbers() {
        var result = ParallelStreamDemo.parallelFilter(List.of(-2, -1, 0, 1, 2, 3));
        assertTrue(result.contains(1));
        assertTrue(result.contains(2));
        assertTrue(result.contains(3));
        assertFalse(result.contains(-1));
    }

    @Test
    void parallelCountCharactersSumsLengths() {
        long count = ParallelStreamDemo.parallelCountCharacters(List.of("ab", "cd", "ef"));
        assertEquals(6, count);
    }

    @Test
    void parallelTransformConvertsToUpperCase() {
        var result = ParallelStreamDemo.parallelTransform(List.of("a", "b", "c"));
        assertTrue(result.contains("A"));
        assertTrue(result.contains("B"));
        assertTrue(result.contains("C"));
    }

    @Test
    void parallelSideEffectDemoReturnsCorrectSum() {
        var result = ParallelStreamDemo.parallelSideEffectDemo(List.of(1, 2, 3, 4, 5));
        assertEquals(5, result[0]); // LongAdder 线程安全，结果正确
    }

    @Test
    void parallelBigDataSumComputesCorrectly() {
        // 1 + 2 + ... + 10000 = 50005000
        assertEquals(50_005_000, ParallelStreamDemo.parallelBigDataSum());
    }

    @Test
    void sequentialSmallDataComputesCorrectly() {
        assertEquals(10, ParallelStreamDemo.sequentialSmallData(List.of(1, 2, 3, 4)));
    }
}
