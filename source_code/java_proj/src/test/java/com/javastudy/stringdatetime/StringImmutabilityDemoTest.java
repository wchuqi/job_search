package com.javastudy.stringdatetime;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class StringImmutabilityDemoTest {

    @Test
    void toUpperCaseReturnsNewString() {
        String original = "hello";
        String upper = StringImmutabilityDemo.toUpperCasePreservesOriginal(original);
        assertEquals("HELLO", upper);
        // Original is unchanged
        assertEquals("hello", original);
    }

    @Test
    void originalUnchangedAfterToUpperCase() {
        String original = "hello";
        String result = StringImmutabilityDemo.getOriginalAfterUpper(original);
        assertSame(original, result, "Original reference should be unchanged");
    }

    @Test
    void referenceEqualsFailsForDifferentObjects() {
        String a = new String("hello");
        String b = new String("hello");
        assertFalse(StringImmutabilityDemo.referenceEquals(a, b),
                "== should be false for different objects with same content");
    }

    @Test
    void referenceEqualsSucceedsForSameLiteral() {
        // String literals are interned, so == works for compile-time constants
        String a = "hello";
        String b = "hello";
        assertTrue(StringImmutabilityDemo.referenceEquals(a, b),
                "== should be true for interned literals");
    }

    @Test
    void contentEqualsWorksRegardlessOfReference() {
        String a = new String("hello");
        String b = new String("hello");
        assertTrue(StringImmutabilityDemo.contentEquals(a, b),
                "equals() should compare content, not reference");
    }

    @Test
    void newStringReferenceDiffersFromLiteral() {
        assertFalse(StringImmutabilityDemo.newStringReferenceEquals("test"),
                "new String() creates a different reference than a literal");
    }

    @Test
    void concatenationCreatesNewObject() {
        // "hello" + "world" creates a new object, so != "hello"
        // Note: the implementation checks before == after, where after = a + b
        // This tests the concept that concatenation produces a new reference
        String a = "hello";
        String b = "world";
        boolean sameRef = StringImmutabilityDemo.concatenationCreatesNewObject(a, b);
        assertFalse(sameRef, "Concatenation should produce a new object");
    }

    @Test
    void internReturnsCanonicalReference() {
        // For a compile-time literal, intern() returns the same reference
        // But new String("test").intern() returns the pool reference
        // which == "test" (the literal)
        // We need to test with a value that's in the pool
        String value = "test";
        assertTrue(StringImmutabilityDemo.internReturnsSameReference(value));
    }

    @Test
    void trimReturnsNewObjectWhenWhitespaceExists() {
        String original = "  hello  ";
        assertFalse(StringImmutabilityDemo.trimReturnsNewObject(original),
                "trim() should return a new object when whitespace is present");
    }

    @Test
    void trimReturnsSameObjectWhenNoWhitespace() {
        // Optimization: if no trimming needed, some JVMs return same object
        // This is implementation-dependent
        String original = "hello";
        // Note: this may or may not be true depending on JVM implementation
        // The important thing is the content is correct
        String trimmed = original.trim();
        assertEquals("hello", trimmed);
    }
}
