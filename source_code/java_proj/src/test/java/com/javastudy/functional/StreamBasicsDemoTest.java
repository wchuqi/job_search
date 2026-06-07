package com.javastudy.functional;

import org.junit.jupiter.api.Test;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

class StreamBasicsDemoTest {

    // ── 创建 Stream ──────────────────────────────────────────────────

    @Test
    void fromCollectionPreservesElements() {
        var input = List.of("a", "b", "c");
        assertEquals(input, StreamBasicsDemo.fromCollection(input));
    }

    @Test
    void fromOfCreatesStreamFromValues() {
        assertEquals(List.of(1, 2, 3, 4, 5), StreamBasicsDemo.fromOf());
    }

    @Test
    void iterateStreamGeneratesSequence() {
        var result = StreamBasicsDemo.iterateStream(5);
        assertEquals(List.of(1, 2, 3, 4, 5), result);
    }

    @Test
    void generateRandomsProducesCorrectSize() {
        var result = StreamBasicsDemo.generateRandoms(10);
        assertEquals(10, result.size());
        result.forEach(r -> {
            assertTrue(r >= 0.0);
            assertTrue(r < 1.0);
        });
    }

    // ── filter ───────────────────────────────────────────────────────

    @Test
    void filterEvenKeepsEvenNumbers() {
        var result = StreamBasicsDemo.filterEven(List.of(1, 2, 3, 4, 5, 6));
        assertEquals(List.of(2, 4, 6), result);
    }

    @Test
    void filterNonNullRemovesNullAndBlank() {
        var result = StreamBasicsDemo.filterNonNull(java.util.Arrays.asList("a", null, "", "  ", "b"));
        assertEquals(List.of("a", "b"), result);
    }

    @Test
    void filterPositiveEvenAppliesBothFilters() {
        var result = StreamBasicsDemo.filterPositiveEven(List.of(-2, -1, 0, 1, 2, 3, 4));
        assertEquals(List.of(2, 4), result);
    }

    // ── map ──────────────────────────────────────────────────────────

    @Test
    void toUpperCaseConvertsAllStrings() {
        var result = StreamBasicsDemo.toUpperCase(List.of("a", "b", "c"));
        assertEquals(List.of("A", "B", "C"), result);
    }

    @Test
    void toLengthsReturnsStringSizes() {
        var result = StreamBasicsDemo.toLengths(List.of("a", "bb", "ccc"));
        assertEquals(List.of(1, 2, 3), result);
    }

    @Test
    void trimAndLowerAppliesBothTransforms() {
        var result = StreamBasicsDemo.trimAndLower(List.of("  HELLO ", "  WORLD "));
        assertEquals(List.of("hello", "world"), result);
    }

    // ── 惰性求值 ─────────────────────────────────────────────────────

    @Test
    void countFilterExecutionsCountsAllElements() {
        long count = StreamBasicsDemo.countFilterExecutions(List.of(1, -2, 3, -4, 5));
        assertEquals(5, count); // filter 遍历了所有 5 个元素
    }

    @Test
    void countWithLimitStopsEarly() {
        // 取前 2 个正数，filter 可能需要遍历 > 2 个元素
        long count = StreamBasicsDemo.countWithLimit(List.of(1, 2, 3, 4, 5), 2);
        // 只需遍历前 2 个元素就拿到 2 个正数
        assertEquals(2, count);
    }

    @Test
    void countWithLimitSkipsNegativeElements() {
        // 过滤后取前 2 个，需要遍历更多元素
        long count = StreamBasicsDemo.countWithLimit(List.of(-1, -2, 1, 2, 3), 2);
        assertEquals(4, count); // 遍历了 -1, -2, 1, 2（拿到 2 个正数后停止）
    }

    // ── Stream 一次性 ────────────────────────────────────────────────

    @Test
    void demonstrateSingleUseThrowsException() {
        var exception = StreamBasicsDemo.demonstrateSingleUse();
        assertNotNull(exception);
        assertInstanceOf(IllegalStateException.class, exception);
    }
}
