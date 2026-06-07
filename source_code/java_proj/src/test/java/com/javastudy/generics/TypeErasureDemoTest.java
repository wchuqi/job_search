package com.javastudy.generics;

import org.junit.jupiter.api.Test;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

class TypeErasureDemoTest {

    @Test
    void testListClassEquals() {
        assertTrue(TypeErasureDemo.listClassEquals());
    }

    @Test
    void testCreateDefault() throws Exception {
        StringBuilder sb = TypeErasureDemo.createDefault(StringBuilder.class);
        assertNotNull(sb);
        assertEquals("", sb.toString());
    }

    @Test
    void testBoxInt() {
        Box<Integer> box = TypeErasureDemo.boxInt(42);
        assertEquals(42, box.getValue());
    }

    @Test
    void testInstanceofCheck() {
        assertTrue(TypeErasureDemo.instanceofCheck(List.of("a")));
        assertFalse(TypeErasureDemo.instanceofCheck("not a list"));
    }
}
