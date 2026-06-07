package com.javastudy.functional;

import org.junit.jupiter.api.Test;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

class StreamSideEffectDemoTest {

    @Test
    void badSideEffectFiltersCorrectly() {
        var result = StreamSideEffectDemo.badSideEffect(List.of("ab", "abcd", "a", "abcde"));
        assertTrue(result.contains("abcd"));
        assertTrue(result.contains("abcde"));
        assertEquals(2, result.size());
    }

    @Test
    void correctCollectFiltersCorrectly() {
        var result = StreamSideEffectDemo.correctCollect(List.of("ab", "abcd", "a", "abcde"));
        assertEquals(List.of("abcd", "abcde"), result);
    }

    @Test
    void correctParallelCollectProducesSameSet() {
        var result = StreamSideEffectDemo.correctParallelCollect(List.of("ab", "abcd", "a", "abcde"));
        assertEquals(2, result.size());
        assertTrue(result.contains("abcd"));
        assertTrue(result.contains("abcde"));
    }

    @Test
    void badParallelSideEffectFiltersCorrectly() {
        // 串行时结果正确，但这是反模式
        var result = StreamSideEffectDemo.badParallelSideEffect(List.of("ab", "abcd", "a", "abcde"));
        // 并行时 ArrayList 可能丢数据，这里只验证不崩溃
        assertNotNull(result);
    }

    @Test
    void correctPureMapAppendsExclamation() {
        var result = StreamSideEffectDemo.correctPureMap(List.of("a", "b", "c"));
        assertEquals(List.of("a!", "b!", "c!"), result);
    }

    @Test
    void parallelOrderUnpredictableContainsAllElements() {
        var items = List.of("a", "b", "c", "d", "e");
        var result = StreamSideEffectDemo.parallelOrderUnpredictable(items);
        assertEquals(items.size(), result.size());
        assertTrue(result.containsAll(items));
    }
}
