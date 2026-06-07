package com.javastudy.functional;

import org.junit.jupiter.api.Test;
import java.util.List;
import java.util.function.*;
import static org.junit.jupiter.api.Assertions.*;

class BuiltInInterfacesDemoTest {

    // ── Predicate ────────────────────────────────────────────────────

    @Test
    void isEvenReturnsTrueForEvenNumbers() {
        Predicate<Integer> isEven = BuiltInInterfacesDemo.isEven();
        assertTrue(isEven.test(2));
        assertTrue(isEven.test(0));
        assertFalse(isEven.test(3));
    }

    @Test
    void lengthGreaterThanFiltersByLength() {
        Predicate<String> longEnough = BuiltInInterfacesDemo.lengthGreaterThan(3);
        assertFalse(longEnough.test("ab"));
        assertTrue(longEnough.test("abcd"));
    }

    @Test
    void filterEvenPositiveCombinesPredicates() {
        var result = BuiltInInterfacesDemo.filterEvenPositive(List.of(-2, -1, 0, 1, 2, 3, 4));
        assertEquals(List.of(2, 4), result);
    }

    // ── Function ─────────────────────────────────────────────────────

    @Test
    void toLengthReturnsStringLength() {
        Function<String, Integer> fn = BuiltInInterfacesDemo.toLength();
        assertEquals(5, fn.apply("hello"));
        assertEquals(0, fn.apply(""));
    }

    @Test
    void doubleThenAddOneAppliesBothFunctions() {
        Function<Integer, Integer> fn = BuiltInInterfacesDemo.doubleThenAddOne();
        assertEquals(3, fn.apply(1)); // 1*2 + 1
        assertEquals(5, fn.apply(2)); // 2*2 + 1
        assertEquals(1, fn.apply(0)); // 0*2 + 1
    }

    // ── Consumer ─────────────────────────────────────────────────────

    @Test
    void appendToAppendsToStringBuilder() {
        var sb = new StringBuilder();
        Consumer<String> consumer = BuiltInInterfacesDemo.appendTo(sb);
        consumer.accept("hello");
        consumer.accept(" world");
        assertEquals("hello world", sb.toString());
    }

    @Test
    void logAndAppendExecutesBothConsumers() {
        var sb = new StringBuilder();
        var log = new StringBuilder();
        Consumer<String> consumer = BuiltInInterfacesDemo.logAndAppend(sb, log);
        consumer.accept("test");
        assertTrue(log.toString().contains("logged:test"));
        assertEquals("test", sb.toString());
    }

    // ── Supplier ─────────────────────────────────────────────────────

    @Test
    void defaultValueReturnsDefault() {
        assertEquals("default", BuiltInInterfacesDemo.defaultValue().get());
    }

    @Test
    void getOrDefaultReturnsValueWhenNotNull() {
        assertEquals("actual", BuiltInInterfacesDemo.getOrDefault("actual", () -> "default"));
    }

    @Test
    void getOrDefaultReturnsDefaultWhenNull() {
        assertEquals("default", BuiltInInterfacesDemo.getOrDefault(null, () -> "default"));
    }

    // ── UnaryOperator ────────────────────────────────────────────────

    @Test
    void toUpperConvertsToUpperCase() {
        UnaryOperator<String> op = BuiltInInterfacesDemo.toUpper();
        assertEquals("HELLO", op.apply("hello"));
    }

    @Test
    void applyUnaryAppliesOperator() {
        assertEquals("HELLO", BuiltInInterfacesDemo.applyUnary("hello", String::toUpperCase));
    }

    // ── BinaryOperator ───────────────────────────────────────────────

    @Test
    void maxReturnsLargerValue() {
        BinaryOperator<Integer> max = BuiltInInterfacesDemo.max();
        assertEquals(5, max.apply(3, 5));
        assertEquals(5, max.apply(5, 3));
    }

    @Test
    void concatConcatenatesStrings() {
        BinaryOperator<String> concat = BuiltInInterfacesDemo.concat();
        assertEquals("ab", concat.apply("a", "b"));
    }
}
