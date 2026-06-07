package com.javastudy.basics;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class PrimitiveTypesDemoTest {

    @Test
    void testByteRange() {
        assertEquals(-128, PrimitiveTypesDemo.BYTE_MIN);
        assertEquals(127, PrimitiveTypesDemo.BYTE_MAX);
    }

    @Test
    void testIntRange() {
        assertEquals(Integer.MIN_VALUE, PrimitiveTypesDemo.INT_MIN);
        assertEquals(Integer.MAX_VALUE, PrimitiveTypesDemo.INT_MAX);
    }

    @Test
    void testCharRange() {
        assertEquals(0, PrimitiveTypesDemo.CHAR_MIN);
        assertEquals(65535, PrimitiveTypesDemo.CHAR_MAX);
    }

    @Test
    void testByteSizes() {
        assertEquals(1, PrimitiveTypesDemo.getByteSize("byte"));
        assertEquals(2, PrimitiveTypesDemo.getByteSize("short"));
        assertEquals(4, PrimitiveTypesDemo.getByteSize("int"));
        assertEquals(8, PrimitiveTypesDemo.getByteSize("long"));
        assertEquals(4, PrimitiveTypesDemo.getByteSize("float"));
        assertEquals(8, PrimitiveTypesDemo.getByteSize("double"));
        assertEquals(2, PrimitiveTypesDemo.getByteSize("char"));
    }

    @Test
    void testGetByteSizeUnknownType() {
        assertThrows(IllegalArgumentException.class,
            () -> PrimitiveTypesDemo.getByteSize("unknown"));
    }

    @Test
    void testAutoBox() {
        Integer boxed = PrimitiveTypesDemo.autoBox(42);
        assertEquals(42, boxed);
        assertTrue(boxed instanceof Integer);
    }

    @Test
    void testAutoUnbox() {
        int unboxed = PrimitiveTypesDemo.autoUnbox(Integer.valueOf(42));
        assertEquals(42, unboxed);
    }

    @Test
    void testCachedIntegerRange() {
        // -128~127 范围内 == 为 true
        assertTrue(PrimitiveTypesDemo.isCachedIntegerRange(100));
        assertTrue(PrimitiveTypesDemo.isCachedIntegerRange(-128));
        assertTrue(PrimitiveTypesDemo.isCachedIntegerRange(127));
        // 超出范围 == 为 false
        assertFalse(PrimitiveTypesDemo.isCachedIntegerRange(128));
        assertFalse(PrimitiveTypesDemo.isCachedIntegerRange(1000));
    }
}
