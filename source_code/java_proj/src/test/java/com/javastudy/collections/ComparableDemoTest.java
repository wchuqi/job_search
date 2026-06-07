package com.javastudy.collections;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ComparableDemoTest {

    private final ComparableDemo demo = new ComparableDemo();

    @Test
    void sortEmployeesBySalaryDesc() {
        List<String> result = demo.sortEmployees();
        // Bob (90k) first, then Alice and Charlie (both 80k, alphabetical)
        assertEquals("Bob", result.get(0));
        assertEquals("Alice", result.get(1));
        assertEquals("Charlie", result.get(2));
    }

    @Test
    void treeSetUsesNaturalOrdering() {
        List<String> result = demo.treeSetOrdering();
        assertEquals("Bob", result.get(0));
        assertEquals("Alice", result.get(1));
        assertEquals("Charlie", result.get(2));
    }

    @Test
    void compareIntegersReturnsNegativeForLess() {
        assertTrue(demo.compareIntegers(1, 2) < 0);
    }

    @Test
    void compareIntegersReturnsZeroForEqual() {
        assertEquals(0, demo.compareIntegers(5, 5));
    }

    @Test
    void compareIntegersReturnsPositiveForGreater() {
        assertTrue(demo.compareIntegers(3, 1) > 0);
    }

    @Test
    void compareStringsLexicographic() {
        assertTrue(demo.compareStrings("Apple", "Banana") < 0);
        assertEquals(0, demo.compareStrings("Same", "Same"));
        assertTrue(demo.compareStrings("Zebra", "Apple") > 0);
    }

    @Test
    void employeeCompareToIsConsistentWithEquals() {
        ComparableDemo.Employee e1 = new ComparableDemo.Employee("Alice", 80000);
        ComparableDemo.Employee e2 = new ComparableDemo.Employee("Alice", 80000);
        assertEquals(0, e1.compareTo(e2));
        assertEquals(e1, e2);
    }
}
