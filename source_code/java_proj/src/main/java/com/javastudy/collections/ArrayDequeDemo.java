package com.javastudy.collections;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

/**
 * ArrayDeque: resizable array-based deque for stack and queue operations.
 *
 * Faster than LinkedList for both stack and queue use cases.
 * Cannot store null elements.
 */
public class ArrayDequeDemo {

    /** Use as a stack (push/pop/peek). */
    public List<String> stackDemo() {
        Deque<String> stack = new ArrayDeque<>();
        stack.push("A");
        stack.push("B");
        stack.push("C");
        List<String> result = new ArrayList<>();
        result.add(stack.peek()); // "C"
        result.add(stack.pop());  // "C"
        result.add(stack.pop());  // "B"
        return result;
    }

    /** Use as a queue (offer/poll/peek). */
    public List<String> queueDemo() {
        Deque<String> queue = new ArrayDeque<>();
        queue.offer("A");
        queue.offer("B");
        queue.offer("C");
        List<String> result = new ArrayList<>();
        result.add(queue.peek()); // "A"
        result.add(queue.poll()); // "A"
        result.add(queue.poll()); // "B"
        return result;
    }

    /** First and last operations (bidirectional). */
    public List<String> firstLastDemo() {
        Deque<String> deque = new ArrayDeque<>();
        deque.addFirst("B");
        deque.addLast("C");
        deque.addFirst("A");
        deque.addLast("D");
        List<String> result = new ArrayList<>();
        result.add(deque.getFirst()); // "A"
        result.add(deque.getLast());  // "D"
        return result;
    }

    /** isEmpty and size. */
    public boolean emptyCheck() {
        Deque<String> deque = new ArrayDeque<>();
        return deque.isEmpty();
    }

    /** Bulk operations. */
    public int sizeAfterBulk() {
        Deque<Integer> deque = new ArrayDeque<>();
        deque.addAll(List.of(1, 2, 3, 4, 5));
        return deque.size();
    }
}
