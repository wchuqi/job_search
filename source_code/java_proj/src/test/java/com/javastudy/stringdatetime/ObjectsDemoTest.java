package com.javastudy.stringdatetime;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ObjectsDemoTest {

    @Test
    void requireNonNullReturnsObjectForNonNull() {
        String value = "hello";
        assertSame(value, ObjectsDemo.requireNonNull(value));
    }

    @Test
    void requireNonNullThrowsForNull() {
        assertThrows(NullPointerException.class, () -> ObjectsDemo.requireNonNull(null));
    }

    @Test
    void requireNonNullWithMessageThrowsWithMessage() {
        NullPointerException ex = assertThrows(NullPointerException.class,
                () -> ObjectsDemo.requireNonNullWithMessage(null, "name must not be null"));
        assertEquals("name must not be null", ex.getMessage());
    }

    @Test
    void equalsHandlesBothNonNull() {
        assertTrue(ObjectsDemo.equals("hello", "hello"));
        assertFalse(ObjectsDemo.equals("hello", "world"));
    }

    @Test
    void equalsHandlesBothNull() {
        assertTrue(ObjectsDemo.equals(null, null));
    }

    @Test
    void equalsHandlesOneNull() {
        assertFalse(ObjectsDemo.equals(null, "hello"));
        assertFalse(ObjectsDemo.equals("hello", null));
    }

    @Test
    void hashCodeReturnsZeroForNull() {
        assertEquals(0, ObjectsDemo.hashCode(null));
    }

    @Test
    void hashCodeReturnsObjectHashCode() {
        String value = "hello";
        assertEquals(value.hashCode(), ObjectsDemo.hashCode(value));
    }

    @Test
    void hashComputesFromMultipleFields() {
        int hash = ObjectsDemo.hash("hello", 42, 3.14);
        // Just verify it's deterministic
        assertEquals(hash, ObjectsDemo.hash("hello", 42, 3.14));
    }

    @Test
    void hashDependsOnOrder() {
        int hash1 = ObjectsDemo.hash("a", "b");
        int hash2 = ObjectsDemo.hash("b", "a");
        // Order matters (usually different)
        assertNotEquals(hash1, hash2);
    }

    @Test
    void isNullForNull() {
        assertTrue(ObjectsDemo.isNull(null));
    }

    @Test
    void isNullForNonNull() {
        assertFalse(ObjectsDemo.isNull("hello"));
    }

    @Test
    void nonNullForNull() {
        assertFalse(ObjectsDemo.nonNull(null));
    }

    @Test
    void nonNullForNonNull() {
        assertTrue(ObjectsDemo.nonNull("hello"));
    }

    @Test
    void toStringReturnsDefaultForNull() {
        assertEquals("N/A", ObjectsDemo.toString(null, "N/A"));
    }

    @Test
    void toStringReturnsToStringForNonNull() {
        assertEquals("hello", ObjectsDemo.toString("hello", "N/A"));
    }

    @Test
    void compareComparesComparable() {
        assertTrue(ObjectsDemo.compare("apple", "banana") < 0);
        assertEquals(0, ObjectsDemo.compare("apple", "apple"));
        assertTrue(ObjectsDemo.compare("banana", "apple") > 0);
    }
}
