package com.javastudy.basics;

import org.junit.jupiter.api.Test;
import java.util.List;
import java.util.Map;
import static org.junit.jupiter.api.Assertions.*;

class VarDemoTest {

    @Test
    void testVarWithInt() {
        assertEquals(42, VarDemo.varWithInt());
    }

    @Test
    void testVarWithString() {
        assertEquals("hello", VarDemo.varWithString());
    }

    @Test
    void testVarWithList() {
        List<String> list = VarDemo.varWithList();
        assertEquals(List.of("a", "b", "c"), list);
    }

    @Test
    void testVarWithMap() {
        Map<String, Integer> map = VarDemo.varWithMap();
        assertEquals(1, map.get("key"));
    }

    @Test
    void testVarInForLoop() {
        assertEquals("xyz", VarDemo.varInForLoop());
    }
}
