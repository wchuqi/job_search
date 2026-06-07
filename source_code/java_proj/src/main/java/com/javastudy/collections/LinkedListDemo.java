package com.javastudy.collections;

import java.util.Deque;
import java.util.LinkedList;
import java.util.List;

/**
 * LinkedList as both List and Deque.
 *
 * Doubly-linked list: O(1) add/remove at head/tail, O(n) random access.
 */
public class LinkedListDemo {

    /** Use LinkedList as a List (indexed access). */
    public List<String> asList() {
        List<String> list = new LinkedList<>();
        list.add("A");
        list.add("B");
        list.add("C");
        return list;
    }

    /** Use LinkedList as a Deque (double-ended queue). */
    public Deque<String> asDeque() {
        Deque<String> deque = new LinkedList<>();
        deque.addFirst("B");
        deque.addLast("C");
        deque.addFirst("A");
        return deque;
    }

    /** Push/pop as a stack (LIFO). */
    public String stackOperations() {
        Deque<String> stack = new LinkedList<>();
        stack.push("A");
        stack.push("B");
        stack.push("C");
        return stack.pop(); // "C"
    }

    /** Offer/poll as a queue (FIFO). */
    public String queueOperations() {
        Deque<String> queue = new LinkedList<>();
        queue.offer("A");
        queue.offer("B");
        queue.offer("C");
        return queue.poll(); // "A"
    }

    /** Peek at head and tail. */
    public List<String> peekOperations() {
        Deque<String> deque = new LinkedList<>(List.of("A", "B", "C"));
        String first = deque.peekFirst();
        String last = deque.peekLast();
        return List.of(first, last);
    }

    /** Demonstrate that indexed access is O(n) by returning size. */
    public int size() {
        LinkedList<String> list = new LinkedList<>(List.of("X", "Y", "Z"));
        return list.size();
    }
}
