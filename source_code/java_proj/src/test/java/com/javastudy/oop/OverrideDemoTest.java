package com.javastudy.oop;

import org.junit.jupiter.api.Test;
import java.util.HashSet;
import java.util.Set;
import static org.junit.jupiter.api.Assertions.*;

class OverrideDemoTest {

    @Test
    void testToString() {
        OverrideDemo obj = new OverrideDemo("test", 42);
        assertEquals("OverrideDemo{name='test', value=42}", obj.toString());
    }

    @Test
    void testEquals() {
        OverrideDemo a = new OverrideDemo("test", 42);
        OverrideDemo b = new OverrideDemo("test", 42);
        assertEquals(a, b);
    }

    @Test
    void testHashCodeContract() {
        OverrideDemo a = new OverrideDemo("test", 42);
        OverrideDemo b = new OverrideDemo("test", 42);
        assertEquals(a.hashCode(), b.hashCode());
    }

    @Test
    void testHashSetDedup() {
        Set<OverrideDemo> set = new HashSet<>();
        set.add(new OverrideDemo("test", 42));
        set.add(new OverrideDemo("test", 42));
        assertEquals(1, set.size()); // equals对象只存一份
    }
}
