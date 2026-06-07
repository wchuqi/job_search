package com.javastudy.functional;

import org.junit.jupiter.api.Test;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;

class OptionalAntiPatternDemoTest {

    // ── 反模式 1 vs 正确 ─────────────────────────────────────────────

    @Test
    void badGetCityReturnsValue() {
        assertEquals("Beijing", OptionalAntiPatternDemo.badGetCity(Optional.of("Beijing")));
    }

    @Test
    void badGetCityReturnsDefault() {
        assertEquals("unknown", OptionalAntiPatternDemo.badGetCity(Optional.empty()));
    }

    @Test
    void goodGetCityReturnsValue() {
        assertEquals("Beijing", OptionalAntiPatternDemo.goodGetCity(Optional.of("Beijing")));
    }

    @Test
    void goodGetCityReturnsDefault() {
        assertEquals("unknown", OptionalAntiPatternDemo.goodGetCity(Optional.empty()));
    }

    // ── 反模式 2 vs 正确 ─────────────────────────────────────────────

    @Test
    void badFindUserReturnsValue() {
        assertEquals("Alice", OptionalAntiPatternDemo.badFindUser(Optional.of("Alice")));
    }

    @Test
    void badFindUserReturnsAnonymous() {
        assertEquals("anonymous", OptionalAntiPatternDemo.badFindUser(Optional.empty()));
    }

    @Test
    void goodFindUserReturnsPresent() {
        Optional<String> result = OptionalAntiPatternDemo.goodFindUser("Alice");
        assertEquals("Alice", result.orElseThrow());
    }

    @Test
    void goodFindUserReturnsEmpty() {
        Optional<String> result = OptionalAntiPatternDemo.goodFindUser(null);
        assertTrue(result.isEmpty());
    }

    // ── 反模式 3 vs 正确 ─────────────────────────────────────────────

    @Test
    void badCheckNullReturnsTrueForNonNull() {
        assertTrue(OptionalAntiPatternDemo.badCheckNull("hello"));
    }

    @Test
    void badCheckNullReturnsFalseForNull() {
        assertFalse(OptionalAntiPatternDemo.badCheckNull(null));
    }

    @Test
    void goodCheckNullReturnsTrueForNonNull() {
        assertTrue(OptionalAntiPatternDemo.goodCheckNull("hello"));
    }

    @Test
    void goodCheckNullReturnsFalseForNull() {
        assertFalse(OptionalAntiPatternDemo.goodCheckNull(null));
    }

    // ── 反模式 4 vs 正确 ─────────────────────────────────────────────

    @Test
    void badOrElseNullReturnsValue() {
        assertEquals("hello", OptionalAntiPatternDemo.badOrElseNull(Optional.of("hello")));
    }

    @Test
    void badOrElseNullReturnsNull() {
        assertNull(OptionalAntiPatternDemo.badOrElseNull(Optional.empty()));
    }

    @Test
    void goodOrElseEmptyReturnsValue() {
        assertEquals("hello", OptionalAntiPatternDemo.goodOrElseEmpty(Optional.of("hello")));
    }

    @Test
    void goodOrElseEmptyReturnsEmptyString() {
        assertEquals("", OptionalAntiPatternDemo.goodOrElseEmpty(Optional.empty()));
    }

    // ── 反模式 5 vs 正确 ─────────────────────────────────────────────

    @Test
    void badOptionalOveruseReturnsTrimmed() {
        assertEquals("hello", OptionalAntiPatternDemo.badOptionalOveruse("  hello  "));
    }

    @Test
    void badOptionalOveruseReturnsUnknown() {
        assertEquals("unknown", OptionalAntiPatternDemo.badOptionalOveruse(null));
    }

    @Test
    void goodSimpleNullCheckReturnsTrimmed() {
        assertEquals("hello", OptionalAntiPatternDemo.goodSimpleNullCheck("  hello  "));
    }

    @Test
    void goodSimpleNullCheckReturnsUnknown() {
        assertEquals("unknown", OptionalAntiPatternDemo.goodSimpleNullCheck(null));
    }

    @Test
    void goodSimpleNullCheckReturnsUnknownForBlank() {
        assertEquals("unknown", OptionalAntiPatternDemo.goodSimpleNullCheck("  "));
    }

    // ── 正确链式使用 ─────────────────────────────────────────────────

    @Test
    void extractNoteReturnsNote() {
        var order = new OptionalAntiPatternDemo.Order("1", "Alice", "urgent");
        assertEquals("urgent", OptionalAntiPatternDemo.extractNote(Optional.of(order)));
    }

    @Test
    void extractNoteReturnsNoNoteForBlankNote() {
        var order = new OptionalAntiPatternDemo.Order("1", "Alice", "  ");
        assertEquals("no note", OptionalAntiPatternDemo.extractNote(Optional.of(order)));
    }

    @Test
    void extractNoteReturnsNoNoteForEmpty() {
        assertEquals("no note", OptionalAntiPatternDemo.extractNote(Optional.empty()));
    }
}
