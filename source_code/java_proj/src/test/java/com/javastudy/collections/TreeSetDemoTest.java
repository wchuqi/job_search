package com.javastudy.collections;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class TreeSetDemoTest {

    private final TreeSetDemo demo = new TreeSetDemo();

    @Test
    void naturalOrderSortsStrings() {
        assertEquals(List.of("Apple", "Banana", "Cherry"), demo.naturalOrder());
    }

    @Test
    void integerOrderSortsNumerically() {
        assertEquals(List.of(10, 20, 30), demo.integerOrder());
    }

    @Test
    void customComparatorSortsByLengthThenAlpha() {
        List<String> result = demo.customComparator();
        // Length 3: Fig, Kiwi; Length 5: Apple; Length 6: Banana
        assertEquals("Fig", result.get(0));
        assertEquals("Kiwi", result.get(1));
        assertEquals("Apple", result.get(2));
        assertEquals("Banana", result.get(3));
    }

    @Test
    void descendingOrderReversesNatural() {
        assertEquals(List.of("C", "B", "A"), demo.descendingOrder());
    }

    @Test
    void rangeOperationsReturnCorrectBounds() {
        List<String> result = demo.rangeOperations();
        assertEquals("A", result.get(0)); // first
        assertEquals("E", result.get(1)); // last
    }

    @Test
    void customComparableSortsByGradeThenName() {
        List<String> result = demo.customComparable();
        assertEquals("Bob", result.get(0));       // grade 85
        assertEquals("Alice", result.get(1));     // grade 90
        assertEquals("Charlie", result.get(2));   // grade 90
    }
}
