package com.javastudy.collections;

import java.util.HashSet;
import java.util.Set;

/**
 * HashSet uniqueness and equals/hashCode contract.
 *
 * HashSet uses hashCode() + equals() to determine uniqueness.
 * Objects that are equals() must have the same hashCode().
 */
public class HashSetDemo {

    /** Add duplicate strings; only unique elements are kept. */
    public Set<String> uniqueStrings() {
        Set<String> set = new HashSet<>();
        set.add("A");
        set.add("B");
        set.add("A"); // duplicate, ignored
        return set;
    }

    /** Demonstrate contains uses equals/hashCode. */
    public boolean containsElement(String element) {
        Set<String> set = new HashSet<>(Set.of("X", "Y", "Z"));
        return set.contains(element);
    }

    /** Custom class with correct equals/hashCode. */
    public static class Point {
        private final int x;
        private final int y;

        public Point(int x, int y) {
            this.x = x;
            this.y = y;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof Point p)) return false;
            return x == p.x && y == p.y;
        }

        @Override
        public int hashCode() {
            return 31 * x + y;
        }

        public int getX() { return x; }
        public int getY() { return y; }
    }

    /** Add custom Point objects; equal points are treated as duplicates. */
    public int addPoints() {
        Set<Point> set = new HashSet<>();
        set.add(new Point(1, 2));
        set.add(new Point(3, 4));
        set.add(new Point(1, 2)); // duplicate based on equals/hashCode
        return set.size();
    }

    /** Set operations: union, intersection, difference. */
    public Set<String> union(Set<String> a, Set<String> b) {
        Set<String> result = new HashSet<>(a);
        result.addAll(b);
        return result;
    }

    public Set<String> intersection(Set<String> a, Set<String> b) {
        Set<String> result = new HashSet<>(a);
        result.retainAll(b);
        return result;
    }

    public Set<String> difference(Set<String> a, Set<String> b) {
        Set<String> result = new HashSet<>(a);
        result.removeAll(b);
        return result;
    }
}
