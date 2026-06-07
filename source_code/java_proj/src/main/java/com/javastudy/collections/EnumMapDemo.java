package com.javastudy.collections;

import java.util.EnumMap;
import java.util.Map;
import java.util.Set;

/**
 * EnumMap: highly efficient map for enum keys.
 *
 * Internally backed by an array indexed by ordinal().
 * Faster and more memory-efficient than HashMap for enum keys.
 */
public class EnumMapDemo {

    public enum Day {
        MONDAY, TUESDAY, WEDNESDAY, THURSDAY, FRIDAY, SATURDAY, SUNDAY
    }

    public enum Priority {
        LOW, MEDIUM, HIGH, CRITICAL
    }

    /** Basic EnumMap usage. */
    public Map<Day, String> basicEnumMap() {
        EnumMap<Day, String> map = new EnumMap<>(Day.class);
        map.put(Day.MONDAY, "Start of week");
        map.put(Day.FRIDAY, "End of week");
        map.put(Day.SUNDAY, "Rest day");
        return map;
    }

    /** Create from an existing Map. */
    public Map<Day, String> fromMap() {
        Map<Day, String> source = Map.of(
            Day.MONDAY, "Mon",
            Day.TUESDAY, "Tue"
        );
        return new EnumMap<>(source);
    }

    /** EnumMap preserves natural enum ordering (by ordinal). */
    public Set<Day> orderedKeys() {
        EnumMap<Day, Integer> map = new EnumMap<>(Day.class);
        map.put(Day.SUNDAY, 7);
        map.put(Day.MONDAY, 1);
        map.put(Day.WEDNESDAY, 3);
        return map.keySet(); // ordered by ordinal: MONDAY, WEDNESDAY, SUNDAY
    }

    /** Priority-based task map. */
    public Map<Priority, Integer> priorityMap() {
        EnumMap<Priority, Integer> tasks = new EnumMap<>(Priority.class);
        tasks.put(Priority.LOW, 5);
        tasks.put(Priority.MEDIUM, 3);
        tasks.put(Priority.HIGH, 2);
        tasks.put(Priority.CRITICAL, 1);
        return tasks;
    }
}
