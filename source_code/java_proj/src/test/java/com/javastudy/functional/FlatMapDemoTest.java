package com.javastudy.functional;

import org.junit.jupiter.api.Test;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

class FlatMapDemoTest {

    @Test
    void mapToLengthReturnsLengths() {
        var result = FlatMapDemo.mapToLength(List.of("hello world", "foo"));
        assertEquals(List.of(11, 3), result);
    }

    @Test
    void mapToWordArrayReturnsArrays() {
        var result = FlatMapDemo.mapToWordArray(List.of("a b", "c d"));
        assertEquals(2, result.size());
        assertArrayEquals(new String[]{"a", "b"}, result.get(0));
        assertArrayEquals(new String[]{"c", "d"}, result.get(1));
    }

    @Test
    void flatMapToWordsSplitsAndFlattens() {
        var result = FlatMapDemo.flatMapToWords(List.of("hello world", "foo bar baz"));
        assertEquals(List.of("hello", "world", "foo", "bar", "baz"), result);
    }

    @Test
    void flatMapToWordsHandlesSingleWordSentences() {
        var result = FlatMapDemo.flatMapToWords(List.of("hello", "world"));
        assertEquals(List.of("hello", "world"), result);
    }

    @Test
    void countTotalWordsCountsAllWords() {
        long count = FlatMapDemo.countTotalWords(List.of("hello world", "foo bar baz"));
        assertEquals(5, count);
    }

    @Test
    void uniqueWordsReturnsSortedDistinctWords() {
        var result = FlatMapDemo.uniqueWords(List.of("Hello World", "hello JAVA", "java"));
        assertEquals(List.of("hello", "java", "world"), result);
    }

    @Test
    void flattenListsFlattensNestedLists() {
        var nested = List.of(List.of(1, 2), List.of(3, 4), List.of(5));
        var result = FlatMapDemo.flattenLists(nested);
        assertEquals(List.of(1, 2, 3, 4, 5), result);
    }

    @Test
    void flattenListsHandlesEmptyLists() {
        var nested = List.of(List.of(1), List.<Integer>of(), List.of(2));
        var result = FlatMapDemo.flattenLists(nested);
        assertEquals(List.of(1, 2), result);
    }

    @Test
    void longWordsFiltersByLength() {
        var result = FlatMapDemo.longWords(List.of("hello world", "a bb cccc"));
        assertEquals(List.of("hello", "world", "cccc"), result);
    }
}
