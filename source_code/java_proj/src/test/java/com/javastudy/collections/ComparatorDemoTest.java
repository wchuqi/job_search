package com.javastudy.collections;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ComparatorDemoTest {

    private final ComparatorDemo demo = new ComparatorDemo();

    @Test
    void sortByPriceAscending() {
        List<String> result = demo.sortByPrice();
        assertEquals("Pen", result.get(0));
        assertEquals("Book", result.get(1));
        assertEquals("Tablet", result.get(2));
        assertEquals("Phone", result.get(3));
        assertEquals("Laptop", result.get(4));
    }

    @Test
    void sortByCategoryThenPriceDesc() {
        List<String> result = demo.sortByCategoryThenPriceDesc();
        // Education: Book, Pen (price desc)
        // Electronics: Laptop, Phone, Tablet (price desc)
        assertEquals("Book", result.get(0));
        assertEquals("Pen", result.get(1));
        assertEquals("Laptop", result.get(2));
        assertEquals("Phone", result.get(3));
        assertEquals("Tablet", result.get(4));
    }

    @Test
    void sortByRatingDesc() {
        List<String> result = demo.sortByRatingDesc();
        assertEquals("Book", result.get(0));    // rating 5
        assertEquals("Tablet", result.get(1));  // rating 5
    }

    @Test
    void nullsFirstPutsNullsAtBeginning() {
        List<String> result = demo.nullsFirstDemo();
        assertNull(result.get(0));
        assertNull(result.get(1));
        assertEquals("A", result.get(2));
    }

    @Test
    void nullsLastPutsNullsAtEnd() {
        List<String> result = demo.nullsLastDemo();
        assertEquals("A", result.get(0));
        assertNull(result.get(3));
        assertNull(result.get(4));
    }

    @Test
    void comparingIntSortsByIntField() {
        List<String> result = demo.comparingIntDemo();
        assertEquals("Phone", result.get(0));  // rating 3
        assertEquals("Laptop", result.get(1)); // rating 4
        assertEquals("Pen", result.get(2));    // rating 4
    }
}
