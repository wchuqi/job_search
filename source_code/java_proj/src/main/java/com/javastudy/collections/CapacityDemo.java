package com.javastudy.collections;

import com.javastudy.Generated;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Pre-sizing ArrayList and HashMap to avoid costly resizing.
 *
 * ArrayList: grows by ~50%, each resize copies the entire array.
 * HashMap: doubles capacity when size > capacity * loadFactor.
 * Pre-sizing eliminates unnecessary rehashing and array copies.
 */
public class CapacityDemo {

    /** Pre-sized ArrayList: avoids repeated grow-and-copy. */
    public List<Integer> presizedArrayList(int n) {
        List<Integer> list = new ArrayList<>(n);
        for (int i = 0; i < n; i++) {
            list.add(i);
        }
        return list;
    }

    /** Default ArrayList: starts with capacity 10, grows repeatedly. */
    public List<Integer> defaultArrayList(int n) {
        List<Integer> list = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            list.add(i);
        }
        return list;
    }

    /** Pre-sized HashMap: avoid rehashing. */
    public Map<String, Integer> presizedHashMap(int expectedEntries) {
        // Initial capacity = expectedEntries / loadFactor (0.75)
        int capacity = (int) (expectedEntries / 0.75f) + 1;
        Map<String, Integer> map = new HashMap<>(capacity);
        for (int i = 0; i < expectedEntries; i++) {
            map.put("key" + i, i);
        }
        return map;
    }

    /** HashMap with default capacity. */
    public Map<String, Integer> defaultHashMap(int expectedEntries) {
        Map<String, Integer> map = new HashMap<>();
        for (int i = 0; i < expectedEntries; i++) {
            map.put("key" + i, i);
        }
        return map;
    }

    /** Compute optimal HashMap capacity for a given expected size. */
    public static int optimalHashMapCapacity(int expectedSize) {
        // HashMap rounds up to next power of 2
        // We need: capacity * 0.75 >= expectedSize
        // So: capacity >= expectedSize / 0.75
        int capacity = (int) (expectedSize / 0.75f) + 1;
        // Round up to next power of 2
        capacity |= capacity >> 1;
        capacity |= capacity >> 2;
        capacity |= capacity >> 4;
        capacity |= capacity >> 8;
        capacity |= capacity >> 16;
        return capacity + 1;
    }

    /** Measure internal ArrayList capacity (using reflection). */
    @Generated
    public int measureArrayListCapacity(int initialCapacity) {
        ArrayList<Integer> list = new ArrayList<>(initialCapacity);
        try {
            var field = ArrayList.class.getDeclaredField("elementData");
            field.setAccessible(true);
            Object[] data = (Object[]) field.get(list);
            return data.length;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
