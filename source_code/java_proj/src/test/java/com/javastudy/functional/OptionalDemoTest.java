package com.javastudy.functional;

import org.junit.jupiter.api.Test;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;

class OptionalDemoTest {

    // ── 创建 ─────────────────────────────────────────────────────────

    @Test
    void createWithOfReturnsPresent() {
        Optional<String> opt = OptionalDemo.createWithOf("hello");
        assertTrue(opt.isPresent());
        assertEquals("hello", opt.orElseThrow());
    }

    @Test
    void createWithOfThrowsNpeOnNull() {
        assertThrows(NullPointerException.class, () -> OptionalDemo.createWithOf(null));
    }

    @Test
    void createEmptyReturnsEmpty() {
        Optional<String> opt = OptionalDemo.createEmpty();
        assertTrue(opt.isEmpty());
    }

    @Test
    void createWithOfNullableWithNonNull() {
        Optional<String> opt = OptionalDemo.createWithOfNullable("hello");
        assertTrue(opt.isPresent());
    }

    @Test
    void createWithOfNullableWithNull() {
        Optional<String> opt = OptionalDemo.createWithOfNullable(null);
        assertTrue(opt.isEmpty());
    }

    // ── 取值 ─────────────────────────────────────────────────────────

    @Test
    void getOrDefaultReturnsValue() {
        assertEquals("hello", OptionalDemo.getOrDefault(Optional.of("hello")));
    }

    @Test
    void getOrDefaultReturnsDefault() {
        assertEquals("default", OptionalDemo.getOrDefault(Optional.empty()));
    }

    @Test
    void getOrComputeDefaultReturnsValue() {
        assertEquals("hello", OptionalDemo.getOrComputeDefault(Optional.of("hello")));
    }

    @Test
    void getOrComputeDefaultComputesDefault() {
        String result = OptionalDemo.getOrComputeDefault(Optional.empty());
        assertTrue(result.startsWith("computed-"));
    }

    @Test
    void getOrThrowReturnsValue() {
        assertEquals("hello", OptionalDemo.getOrThrow(Optional.of("hello")));
    }

    @Test
    void getOrThrowThrowsOnEmpty() {
        assertThrows(IllegalArgumentException.class, () -> OptionalDemo.getOrThrow(Optional.empty()));
    }

    // ── map / flatMap ────────────────────────────────────────────────

    @Test
    void mapToLengthReturnsLength() {
        assertEquals(5, OptionalDemo.mapToLength(Optional.of("hello")).orElseThrow());
    }

    @Test
    void mapToLengthOnEmptyReturnsEmpty() {
        assertTrue(OptionalDemo.mapToLength(Optional.empty()).isEmpty());
    }

    @Test
    void mapToUpperTrimmedChainsMaps() {
        assertEquals("HELLO", OptionalDemo.mapToUpperTrimmed(Optional.of("  hello  ")).orElseThrow());
    }

    @Test
    void mapToUpperTrimmedOnEmptyReturnsEmpty() {
        assertTrue(OptionalDemo.mapToUpperTrimmed(Optional.empty()).isEmpty());
    }

    @Test
    void flatMapExampleReturnsTransformed() {
        assertEquals("HELLO", OptionalDemo.flatMapExample(Optional.of("hello")).orElseThrow());
    }

    // ── filter ───────────────────────────────────────────────────────

    @Test
    void filterNonEmptyKeepsNonBlank() {
        assertEquals("hello", OptionalDemo.filterNonEmpty(Optional.of("hello")).orElseThrow());
    }

    @Test
    void filterNonEmptyRemovesBlank() {
        assertTrue(OptionalDemo.filterNonEmpty(Optional.of("  ")).isEmpty());
    }

    // ── ifPresent ────────────────────────────────────────────────────

    @Test
    void ifPresentDemoAppendsWhenPresent() {
        assertEquals("found:hello", OptionalDemo.ifPresentDemo(Optional.of("hello")));
    }

    @Test
    void ifPresentDemoEmptyWhenAbsent() {
        assertEquals("", OptionalDemo.ifPresentDemo(Optional.empty()));
    }

    @Test
    void ifPresentOrElseDemoWithValue() {
        assertEquals("value:hello", OptionalDemo.ifPresentOrElseDemo(Optional.of("hello")));
    }

    @Test
    void ifPresentOrElseDemoEmpty() {
        assertEquals("empty", OptionalDemo.ifPresentOrElseDemo(Optional.empty()));
    }

    // ── 嵌套属性 ─────────────────────────────────────────────────────

    @Test
    void getCityOrDefaultReturnsCity() {
        var user = new OptionalDemo.User("Alice", new OptionalDemo.Address("Beijing"));
        assertEquals("Beijing", OptionalDemo.getCityOrDefault(user));
    }

    @Test
    void getCityOrDefaultReturnsUnknownWhenNullUser() {
        assertEquals("unknown", OptionalDemo.getCityOrDefault(null));
    }

    @Test
    void getCityOrDefaultReturnsUnknownWhenNoAddress() {
        var user = new OptionalDemo.User("Alice", null);
        assertEquals("unknown", OptionalDemo.getCityOrDefault(user));
    }
}
