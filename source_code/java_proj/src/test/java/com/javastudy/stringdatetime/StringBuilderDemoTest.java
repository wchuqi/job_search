package com.javastudy.stringdatetime;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class StringBuilderDemoTest {

    @Test
    void concatenateWithPlusProducesSameResult() {
        List<String> items = List.of("a", "b", "c");
        String result = StringBuilderDemo.concatenateWithPlus(items);
        assertTrue(result.contains("a, b, c"));
    }

    @Test
    void concatenateWithStringBuilderProducesSameResult() {
        List<String> items = List.of("a", "b", "c");
        String result = StringBuilderDemo.concatenateWithStringBuilder(items);
        assertTrue(result.contains("a, b, c"));
    }

    @Test
    void bothConcatenationMethodsProduceSameResult() {
        List<String> items = List.of("x", "y", "z");
        String plus = StringBuilderDemo.concatenateWithPlus(items);
        String sb = StringBuilderDemo.concatenateWithStringBuilder(items);
        assertEquals(plus, sb);
    }

    @Test
    void chainedAppendFormatsCorrectly() {
        String result = StringBuilderDemo.chainedAppend("Alice", 30, 50000.0);
        assertEquals("Name: Alice, Age: 30, Salary: 50000.0", result);
    }

    @Test
    void insertAtPosition() {
        assertEquals("hello world!", StringBuilderDemo.insertAtPosition("hello!", 5, " world"));
    }

    @Test
    void deleteRange() {
        assertEquals("ho", StringBuilderDemo.deleteRange("hello", 1, 4));
    }

    @Test
    void reverseString() {
        assertEquals("olleh", StringBuilderDemo.reverse("hello"));
        assertEquals("a", StringBuilderDemo.reverse("a"));
    }

    @Test
    void replaceRange() {
        assertEquals("heXXX", StringBuilderDemo.replaceRange("hello", 2, 5, "XXX").toString());
    }

    @Test
    void joinWithDelimiter() {
        List<String> items = List.of("a", "b", "c");
        assertEquals("a, b, c", StringBuilderDemo.joinWithDelimiter(", ", items));
    }

    @Test
    void joinWithEmptyDelimiter() {
        List<String> items = List.of("a", "b", "c");
        assertEquals("abc", StringBuilderDemo.joinWithDelimiter("", items));
    }

    @Test
    void joinWithPrefixSuffix() {
        List<String> items = List.of("a", "b", "c");
        assertEquals("[a, b, c]", StringBuilderDemo.joinWithPrefixSuffix(", ", "[", "]", items));
    }

    @Test
    void emptyListJoin() {
        List<String> items = List.of();
        assertEquals("[]", StringBuilderDemo.joinWithPrefixSuffix(", ", "[", "]", items));
    }

    @Test
    void withPreallocatedCapacity() {
        List<String> items = List.of("hello", " ", "world");
        assertEquals("hello world", StringBuilderDemo.withPreallocatedCapacity(items, 100));
    }
}
