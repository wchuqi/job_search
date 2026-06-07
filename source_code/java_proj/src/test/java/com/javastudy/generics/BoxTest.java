package com.javastudy.generics;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class BoxTest {

    @Test
    void testBoxString() {
        Box<String> box = new Box<>("hello");
        assertEquals("hello", box.getValue());
    }

    @Test
    void testBoxInteger() {
        Box<Integer> box = new Box<>(42);
        assertEquals(42, box.getValue());
    }

    @Test
    void testSetBoxValue() {
        Box<String> box = new Box<>("initial");
        box.setValue("updated");
        assertEquals("updated", box.getValue());
    }
}
