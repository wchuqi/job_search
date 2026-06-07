package com.javastudy.collections;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Word frequency counter using HashMap + merge, sorted with Comparator.
 *
 * Demonstrates the practical pattern of:
 * 1. Splitting text into words
 * 2. Counting with merge(key, 1, Integer::sum)
 * 3. Sorting by frequency or alphabetically
 */
public class WordFrequencyCounter {

    /** Count word frequencies using merge. */
    public Map<String, Integer> countFrequencies(String text) {
        Map<String, Integer> freq = new HashMap<>();
        for (String word : text.toLowerCase().split("\\W+")) {
            if (!word.isEmpty()) {
                freq.merge(word, 1, Integer::sum);
            }
        }
        return freq;
    }

    /** Sort by frequency descending, then alphabetically. */
    public List<Map.Entry<String, Integer>> sortByFrequency(String text) {
        Map<String, Integer> freq = countFrequencies(text);
        List<Map.Entry<String, Integer>> entries = new ArrayList<>(freq.entrySet());
        entries.sort(
            Comparator.<Map.Entry<String, Integer>>comparingInt(Map.Entry::getValue).reversed()
                      .thenComparing(Map.Entry::getKey)
        );
        return entries;
    }

    /** Sort alphabetically. */
    public List<Map.Entry<String, Integer>> sortAlphabetically(String text) {
        Map<String, Integer> freq = countFrequencies(text);
        List<Map.Entry<String, Integer>> entries = new ArrayList<>(freq.entrySet());
        entries.sort(Comparator.comparing(Map.Entry::getKey));
        return entries;
    }

    /** Top N most frequent words. */
    public List<Map.Entry<String, Integer>> topN(String text, int n) {
        List<Map.Entry<String, Integer>> sorted = sortByFrequency(text);
        return sorted.subList(0, Math.min(n, sorted.size()));
    }

    /** Count using computeIfAbsent (alternative pattern). */
    public Map<String, Integer> countWithComputeIfAbsent(String text) {
        Map<String, Integer> freq = new HashMap<>();
        for (String word : text.toLowerCase().split("\\W+")) {
            if (!word.isEmpty()) {
                freq.compute(word, (k, v) -> v == null ? 1 : v + 1);
            }
        }
        return freq;
    }

    /** Build an ordered result map (insertion-order preserved). */
    public Map<String, Integer> orderedResult(String text) {
        List<Map.Entry<String, Integer>> sorted = sortByFrequency(text);
        Map<String, Integer> result = new LinkedHashMap<>();
        for (var entry : sorted) {
            result.put(entry.getKey(), entry.getValue());
        }
        return result;
    }

    /** Find unique words (frequency == 1). */
    public List<String> uniqueWords(String text) {
        Map<String, Integer> freq = countFrequencies(text);
        return freq.entrySet().stream()
                   .filter(e -> e.getValue() == 1)
                   .map(Map.Entry::getKey)
                   .sorted()
                   .toList();
    }

    /** Total word count. */
    public int totalWords(String text) {
        Map<String, Integer> freq = countFrequencies(text);
        return freq.values().stream().mapToInt(Integer::intValue).sum();
    }
}
