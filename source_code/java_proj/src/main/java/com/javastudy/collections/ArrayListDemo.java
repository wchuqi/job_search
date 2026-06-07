package com.javastudy.collections;

import java.util.ArrayList;
import java.util.List;

/**
 * ArrayList basics: add, get, size, remove, pre-allocation.
 *
 * ArrayList backed by an Object[] that grows by ~50% when full.
 * Pre-sizing avoids repeated reallocation.
 */
public class ArrayListDemo {

    /** Add elements and retrieve by index. */
    public List<String> basicOperations() {
        List<String> list = new ArrayList<>();
        list.add("A");
        list.add("B");
        list.add("C");
        return list;
    }

    /** Insert at a specific index. */
    public List<String> insertAt(int index, String element) {
        List<String> list = new ArrayList<>(List.of("A", "B", "C"));
        list.add(index, element);
        return list;
    }

    /** Remove by index and by object. */
    public List<String> removeElements() {
        List<String> list = new ArrayList<>(List.of("A", "B", "C", "B"));
        list.remove("B");   // removes first occurrence of "B"
        list.remove(0);     // removes element at index 0
        return list;
    }

    /** Pre-allocate with initial capacity to avoid resizing. */
    public int preallocatedCapacity(int initialCapacity) {
        List<String> list = new ArrayList<>(initialCapacity);
        return list.size(); // still 0, but internal array is pre-sized
    }

    /** Get the internal capacity via reflection for demonstration. */
    public int getInternalCapacity(List<?> list) {
        try {
            var field = ArrayList.class.getDeclaredField("elementData");
            field.setAccessible(true);
            Object[] data = (Object[]) field.get(list);
            return data.length;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /** Set replaces an element at a given index. */
    public List<String> setElement(int index, String replacement) {
        List<String> list = new ArrayList<>(List.of("A", "B", "C"));
        list.set(index, replacement);
        return list;
    }

    /** subList returns a view backed by the original list. */
    public List<String> subListView() {
        List<String> list = new ArrayList<>(List.of("A", "B", "C", "D", "E"));
        return list.subList(1, 4); // ["B", "C", "D"]
    }
}
