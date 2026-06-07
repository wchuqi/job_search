package com.javastudy.collections;

import org.junit.jupiter.api.Test;

import java.util.Map;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class EnumMapDemoTest {

    private final EnumMapDemo demo = new EnumMapDemo();

    @Test
    void basicEnumMapContainsExpectedEntries() {
        Map<EnumMapDemo.Day, String> map = demo.basicEnumMap();
        assertEquals(3, map.size());
        assertEquals("Start of week", map.get(EnumMapDemo.Day.MONDAY));
        assertEquals("End of week", map.get(EnumMapDemo.Day.FRIDAY));
        assertEquals("Rest day", map.get(EnumMapDemo.Day.SUNDAY));
    }

    @Test
    void fromMapCreatesEnumMap() {
        Map<EnumMapDemo.Day, String> map = demo.fromMap();
        assertEquals(2, map.size());
        assertEquals("Mon", map.get(EnumMapDemo.Day.MONDAY));
    }

    @Test
    void orderedKeysFollowEnumOrdinals() {
        Set<EnumMapDemo.Day> keys = demo.orderedKeys();
        var list = keys.stream().toList();
        // Should be in ordinal order: MONDAY(1), WEDNESDAY(3), SUNDAY(6)
        assertEquals(EnumMapDemo.Day.MONDAY, list.get(0));
        assertEquals(EnumMapDemo.Day.WEDNESDAY, list.get(1));
        assertEquals(EnumMapDemo.Day.SUNDAY, list.get(2));
    }

    @Test
    void priorityMapContainsAllPriorities() {
        Map<EnumMapDemo.Priority, Integer> map = demo.priorityMap();
        assertEquals(4, map.size());
        assertEquals(1, map.get(EnumMapDemo.Priority.CRITICAL));
        assertEquals(5, map.get(EnumMapDemo.Priority.LOW));
    }
}
