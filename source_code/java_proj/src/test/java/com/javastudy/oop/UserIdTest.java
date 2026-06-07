package com.javastudy.oop;

import org.junit.jupiter.api.Test;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import static org.junit.jupiter.api.Assertions.*;

class UserIdTest {

    @Test
    void testEquals() {
        UserId a = new UserId(1L);
        UserId b = new UserId(1L);
        assertEquals(a, b);
    }

    @Test
    void testNotEquals() {
        UserId a = new UserId(1L);
        UserId b = new UserId(2L);
        assertNotEquals(a, b);
    }

    @Test
    void testHashCodeContract() {
        UserId a = new UserId(1L);
        UserId b = new UserId(1L);
        assertEquals(a.hashCode(), b.hashCode());
    }

    @Test
    void testHashMapLookup() {
        Map<UserId, String> map = new HashMap<>();
        UserId key = new UserId(1L);
        map.put(key, "Alice");

        // 用另一个equals的key应该能找到
        assertEquals("Alice", map.get(new UserId(1L)));
    }

    @Test
    void testHashSetDedup() {
        Set<UserId> set = new HashSet<>();
        set.add(new UserId(1L));
        set.add(new UserId(1L));
        assertEquals(1, set.size());
    }
}
