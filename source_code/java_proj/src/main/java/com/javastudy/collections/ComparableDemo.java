package com.javastudy.collections;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.TreeSet;

/**
 * Comparable interface and compareTo method.
 *
 * Comparable defines the natural ordering of a class.
 * compareTo returns: negative (this < other), 0 (equal), positive (this > other).
 */
public class ComparableDemo {

    /** Employee with natural ordering by salary (descending), then name. */
    public static class Employee implements Comparable<Employee> {
        private final String name;
        private final double salary;

        public Employee(String name, double salary) {
            this.name = name;
            this.salary = salary;
        }

        @Override
        public int compareTo(Employee other) {
            // Higher salary first (descending)
            int cmp = Double.compare(other.salary, this.salary);
            return cmp != 0 ? cmp : this.name.compareTo(other.name);
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof Employee e)) return false;
            return Double.compare(e.salary, salary) == 0 && name.equals(e.name);
        }

        @Override
        public int hashCode() {
            return 31 * name.hashCode() + Double.hashCode(salary);
        }

        public String getName() { return name; }
        public double getSalary() { return salary; }
    }

    /** Sort employees using their natural ordering. */
    public List<String> sortEmployees() {
        List<Employee> list = new ArrayList<>();
        list.add(new Employee("Alice", 80000));
        list.add(new Employee("Bob", 90000));
        list.add(new Employee("Charlie", 80000));
        Collections.sort(list); // uses compareTo
        return list.stream().map(Employee::getName).toList();
    }

    /** TreeSet uses natural ordering. */
    public List<String> treeSetOrdering() {
        TreeSet<Employee> set = new TreeSet<>();
        set.add(new Employee("Alice", 80000));
        set.add(new Employee("Bob", 90000));
        set.add(new Employee("Charlie", 80000));
        return set.stream().map(Employee::getName).toList();
    }

    /** Simple integer comparison using Comparable. */
    public int compareIntegers(int a, int b) {
        return Integer.compare(a, b);
    }

    /** String comparison (lexicographic). */
    public int compareStrings(String a, String b) {
        return a.compareTo(b);
    }
}
