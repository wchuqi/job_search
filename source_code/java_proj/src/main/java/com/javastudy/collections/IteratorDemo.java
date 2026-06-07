package com.javastudy.collections;

import com.javastudy.Generated;

import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.List;

/**
 * Iterator: fail-fast behavior, ConcurrentModificationException, removeIf.
 *
 * Fail-fast iterators throw ConcurrentModificationException if the collection
 * is structurally modified during iteration (except through iterator.remove).
 * removeIf is a safe alternative.
 */
public class IteratorDemo {

    /** Basic iteration with Iterator. */
    public List<String> basicIteration() {
        List<String> list = new ArrayList<>(List.of("A", "B", "C", "D"));
        List<String> result = new ArrayList<>();
        Iterator<String> it = list.iterator();
        while (it.hasNext()) {
            result.add(it.next());
        }
        return result;
    }

    /** Iterator.remove: safe removal during iteration. */
    public List<String> iteratorRemove() {
        List<String> list = new ArrayList<>(List.of("A", "B", "C", "D", "E"));
        Iterator<String> it = list.iterator();
        while (it.hasNext()) {
            String current = it.next();
            if (current.equals("B") || current.equals("D")) {
                it.remove();
            }
        }
        return list;
    }

    /** Demonstrates fail-fast: modifying list during iteration throws. */
    @Generated
    public boolean failFastDemo() {
        List<String> list = new ArrayList<>(List.of("A", "B", "C"));
        try {
            Iterator<String> it = list.iterator();
            it.next();
            list.add("D"); // structural modification outside the iterator
            it.next();
            return false; // should not reach here
        } catch (ConcurrentModificationException e) {
            return true;
        }
    }

    /** removeIf: safe bulk removal without ConcurrentModificationException. */
    public List<String> removeIfDemo() {
        List<String> list = new ArrayList<>(List.of("A", "BB", "C", "DD", "E"));
        list.removeIf(s -> s.length() > 1);
        return list;
    }

    /** removeIf with integers. */
    public List<Integer> removeIfNumbers() {
        List<Integer> list = new ArrayList<>(List.of(1, 2, 3, 4, 5, 6, 7, 8, 9, 10));
        list.removeIf(n -> n % 2 == 0); // remove even numbers
        return list;
    }

    /** ListIterator for bidirectional traversal and modification. */
    public List<String> listIteratorDemo() {
        List<String> list = new ArrayList<>(List.of("A", "B", "C"));
        var it = list.listIterator();
        while (it.hasNext()) {
            String val = it.next();
            if (val.equals("B")) {
                it.set("BB"); // replace "B" with "BB"
            }
        }
        return list;
    }

    /** forEachRemaining on Iterator. */
    public List<String> forEachRemainingDemo() {
        List<String> list = new ArrayList<>(List.of("A", "B", "C", "D", "E"));
        List<String> result = new ArrayList<>();
        Iterator<String> it = list.iterator();
        it.next(); // skip "A"
        it.forEachRemaining(result::add);
        return result;
    }
}
