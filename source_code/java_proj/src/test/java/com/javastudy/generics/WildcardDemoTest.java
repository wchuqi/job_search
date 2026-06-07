package com.javastudy.generics;

import org.junit.jupiter.api.Test;
import java.util.ArrayList;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

class WildcardDemoTest {

    @Test
    void testSumWithIntegers() {
        List<Integer> ints = List.of(1, 2, 3, 4, 5);
        assertEquals(15.0, WildcardDemo.sum(ints));
    }

    @Test
    void testSumWithDoubles() {
        List<Double> doubles = List.of(1.5, 2.5, 3.0);
        assertEquals(7.0, WildcardDemo.sum(doubles));
    }

    @Test
    void testAddIntegersToNumberList() {
        List<Number> numbers = new ArrayList<>();
        WildcardDemo.addIntegers(numbers);
        assertEquals(3, numbers.size());
        assertEquals(1, numbers.get(0));
    }

    @Test
    void testAddIntegersToObjectList() {
        List<Object> objects = new ArrayList<>();
        WildcardDemo.addIntegers(objects);
        assertEquals(3, objects.size());
    }

    @Test
    void testSize() {
        assertEquals(3, WildcardDemo.size(List.of(1, 2, 3)));
        assertEquals(0, WildcardDemo.size(List.of()));
    }
}
