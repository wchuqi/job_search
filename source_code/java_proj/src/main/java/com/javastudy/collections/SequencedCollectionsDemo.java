package com.javastudy.collections;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.SequencedCollection;
import java.util.SequencedMap;
import java.util.SequencedSet;

/**
 * SequencedCollections API (JDK 21).
 *
 * JDK 21 introduced SequencedCollection, SequencedSet, and SequencedMap
 * interfaces that provide uniform first/last/reversed operations.
 */
public class SequencedCollectionsDemo {

    /** getFirst and getLast on a SequencedCollection (ArrayList). */
    public List<String> firstAndLast() {
        SequencedCollection<String> list = new ArrayList<>(List.of("A", "B", "C", "D"));
        String first = list.getFirst();
        String last = list.getLast();
        return List.of(first, last);
    }

    /** addFirst and addLast on a SequencedCollection. */
    public List<String> addFirstLast() {
        SequencedCollection<String> list = new ArrayList<>(List.of("B", "C"));
        list.addFirst("A");
        list.addLast("D");
        return new ArrayList<>(list);
    }

    /** reversed() returns a reversed view. */
    public List<String> reversedView() {
        SequencedCollection<String> list = new ArrayList<>(List.of("A", "B", "C"));
        SequencedCollection<String> reversed = list.reversed();
        return new ArrayList<>(reversed);
    }

    /** SequencedSet: LinkedHashSet supports first/last/reversed. */
    public List<String> sequencedSetDemo() {
        SequencedSet<String> set = new LinkedHashSet<>(List.of("C", "A", "B"));
        String first = set.getFirst();
        String last = set.getLast();
        SequencedSet<String> reversed = set.reversed();
        return new ArrayList<>(reversed);
    }

    /** SequencedMap: LinkedHashMap supports firstEntry/lastEntry/reversed. */
    public Map<String, Integer> sequencedMapDemo() {
        SequencedMap<String, Integer> map = new LinkedHashMap<>();
        map.put("C", 3);
        map.put("A", 1);
        map.put("B", 2);
        Map.Entry<String, Integer> first = map.firstEntry();
        Map.Entry<String, Integer> last = map.lastEntry();
        SequencedMap<String, Integer> reversed = map.reversed();
        // Return reversed keys in order
        return new LinkedHashMap<>(reversed);
    }

    /** getFirst and removeFirst demonstrate SequencedCollection operations. */
    public List<String> pollFirstLast() {
        SequencedCollection<String> deque = new ArrayDeque<>(List.of("A", "B", "C"));
        String first = deque.getFirst();
        deque.removeFirst();
        String last = deque.getLast();
        deque.removeLast();
        return List.of(first, last, deque.toString());
    }

    /** putFirst and putLast on SequencedMap. */
    public List<String> putFirstLastMap() {
        SequencedMap<String, Integer> map = new LinkedHashMap<>();
        map.put("B", 2);
        map.putFirst("A", 1);
        map.putLast("C", 3);
        return new ArrayList<>(map.keySet());
    }
}
