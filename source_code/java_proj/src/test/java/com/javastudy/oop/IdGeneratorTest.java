package com.javastudy.oop;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class IdGeneratorTest {

    @BeforeEach
    void setUp() {
        IdGenerator.reset();
    }

    @Test
    void testNextIncrements() {
        assertEquals(1, IdGenerator.next());
        assertEquals(2, IdGenerator.next());
        assertEquals(3, IdGenerator.next());
    }

    @Test
    void testCurrentAfterNext() {
        IdGenerator.next();
        IdGenerator.next();
        assertEquals(2, IdGenerator.current());
    }

    @Test
    void testReset() {
        IdGenerator.next();
        IdGenerator.next();
        IdGenerator.reset();
        assertEquals(0, IdGenerator.current());
    }
}
