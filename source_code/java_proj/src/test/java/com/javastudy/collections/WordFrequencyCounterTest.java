package com.javastudy.collections;

import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class WordFrequencyCounterTest {

    private final WordFrequencyCounter counter = new WordFrequencyCounter();

    @Test
    void countFrequenciesCountsWords() {
        Map<String, Integer> result = counter.countFrequencies("the cat sat on the mat");
        assertEquals(2, result.get("the"));
        assertEquals(1, result.get("cat"));
        assertEquals(1, result.get("sat"));
        assertEquals(1, result.get("on"));
        assertEquals(1, result.get("mat"));
    }

    @Test
    void countFrequenciesIsCaseInsensitive() {
        Map<String, Integer> result = counter.countFrequencies("The THE the");
        assertEquals(3, result.get("the"));
    }

    @Test
    void sortByFrequencyOrdersByCountDesc() {
        var result = counter.sortByFrequency("a b a c a b");
        assertEquals("a", result.get(0).getKey());
        assertEquals(3, result.get(0).getValue());
        assertEquals("b", result.get(1).getKey());
        assertEquals(2, result.get(1).getValue());
    }

    @Test
    void sortAlphabeticallyOrdersByWord() {
        var result = counter.sortAlphabetically("banana apple cherry");
        assertEquals("apple", result.get(0).getKey());
        assertEquals("banana", result.get(1).getKey());
        assertEquals("cherry", result.get(2).getKey());
    }

    @Test
    void topNReturnsTopEntries() {
        var result = counter.topN("a b a c a b d d d d", 2);
        assertEquals(2, result.size());
        assertEquals("d", result.get(0).getKey());
        assertEquals(4, result.get(0).getValue());
        assertEquals("a", result.get(1).getKey());
        assertEquals(3, result.get(1).getValue());
    }

    @Test
    void countWithComputeIfAbsentProducesSameResult() {
        String text = "hello world hello";
        Map<String, Integer> mergeResult = counter.countFrequencies(text);
        Map<String, Integer> computeResult = counter.countWithComputeIfAbsent(text);
        assertEquals(mergeResult, computeResult);
    }

    @Test
    void orderedResultIsSortedByFrequency() {
        Map<String, Integer> result = counter.orderedResult("c a b a");
        var keys = new java.util.ArrayList<>(result.keySet());
        assertEquals("a", keys.get(0));  // freq 2
        assertEquals("b", keys.get(1));  // freq 1
        assertEquals("c", keys.get(2));  // freq 1
    }

    @Test
    void uniqueWordsReturnsWordsWithFreqOne() {
        List<String> result = counter.uniqueWords("a b a c b d");
        assertEquals(List.of("c", "d"), result);
    }

    @Test
    void totalWordsCountsAllWords() {
        assertEquals(6, counter.totalWords("the cat sat on the mat"));
    }

    @Test
    void emptyTextProducesEmptyResult() {
        Map<String, Integer> result = counter.countFrequencies("");
        assertTrue(result.isEmpty());
    }
}
