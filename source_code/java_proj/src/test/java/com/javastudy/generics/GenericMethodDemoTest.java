package com.javastudy.generics;

import org.junit.jupiter.api.Test;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

class GenericMethodDemoTest {

    @Test
    void testFirst() {
        assertEquals("a", GenericMethodDemo.first(List.of("a", "b", "c")));
        assertEquals(1, GenericMethodDemo.first(List.of(1, 2, 3)));
        assertNull(GenericMethodDemo.first(List.of()));
        assertNull(GenericMethodDemo.first(null));
    }

    @Test
    void testMax() {
        assertEquals(5, GenericMethodDemo.max(List.of(1, 5, 3)));
        assertEquals("z", GenericMethodDemo.max(List.of("a", "z", "m")));
        assertNull(GenericMethodDemo.max(List.<Comparable>of()));
    }

    @Test
    void testPairToString() {
        assertEquals("name=Alice", GenericMethodDemo.pairToString("name", "Alice"));
        assertEquals("age=30", GenericMethodDemo.pairToString("age", 30));
    }
}
