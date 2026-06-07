package com.javastudy.collections;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.TreeSet;

/**
 * TreeSet: sorted set with natural ordering and custom Comparator.
 *
 * Backed by a Red-Black tree. O(log n) for add/remove/contains.
 * Elements must be Comparable or a Comparator must be provided.
 */
public class TreeSetDemo {

    /** Natural ordering (String implements Comparable). */
    public List<String> naturalOrder() {
        TreeSet<String> set = new TreeSet<>();
        set.add("Banana");
        set.add("Apple");
        set.add("Cherry");
        return new ArrayList<>(set); // [Apple, Banana, Cherry]
    }

    /** Integer natural ordering. */
    public List<Integer> integerOrder() {
        TreeSet<Integer> set = new TreeSet<>();
        set.add(30);
        set.add(10);
        set.add(20);
        return new ArrayList<>(set); // [10, 20, 30]
    }

    /** Custom Comparator: sort strings by length, then alphabetically. */
    public List<String> customComparator() {
        TreeSet<String> set = new TreeSet<>(
            Comparator.comparingInt(String::length)
                      .thenComparing(Comparator.naturalOrder())
        );
        set.add("Banana");
        set.add("Fig");
        set.add("Apple");
        set.add("Kiwi");
        return new ArrayList<>(set);
    }

    /** Descending order. */
    public List<String> descendingOrder() {
        TreeSet<String> set = new TreeSet<>();
        set.add("A");
        set.add("B");
        set.add("C");
        return new ArrayList<>(set.descendingSet());
    }

    /** first(), last(), subSet operations. */
    public List<String> rangeOperations() {
        TreeSet<String> set = new TreeSet<>(List.of("A", "B", "C", "D", "E"));
        String first = set.first();
        String last = set.last();
        // headSet: elements < "D"
        List<String> head = new ArrayList<>(set.headSet("D"));
        // tailSet: elements >= "C"
        List<String> tail = new ArrayList<>(set.tailSet("C"));
        return List.of(first, last, head.toString(), tail.toString());
    }

    /** Custom class with Comparable. */
    public static class Student implements Comparable<Student> {
        private final String name;
        private final int grade;

        public Student(String name, int grade) {
            this.name = name;
            this.grade = grade;
        }

        @Override
        public int compareTo(Student other) {
            int cmp = Integer.compare(this.grade, other.grade);
            return cmp != 0 ? cmp : this.name.compareTo(other.name);
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof Student s)) return false;
            return grade == s.grade && name.equals(s.name);
        }

        @Override
        public int hashCode() {
            return 31 * name.hashCode() + grade;
        }

        public String getName() { return name; }
        public int getGrade() { return grade; }
    }

    /** TreeSet of Comparable objects. */
    public List<String> customComparable() {
        TreeSet<Student> set = new TreeSet<>();
        set.add(new Student("Alice", 90));
        set.add(new Student("Bob", 85));
        set.add(new Student("Charlie", 90));
        return set.stream()
                  .map(Student::getName)
                  .toList();
    }
}
