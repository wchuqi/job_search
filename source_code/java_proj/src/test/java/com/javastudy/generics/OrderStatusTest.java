package com.javastudy.generics;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class OrderStatusTest {

    @Test
    void testValues() {
        assertEquals(5, OrderStatus.values().length);
    }

    @Test
    void testValueOf() {
        assertEquals(OrderStatus.CREATED, OrderStatus.valueOf("CREATED"));
    }

    @Test
    void testOrdinal() {
        assertEquals(0, OrderStatus.CREATED.ordinal());
        assertEquals(1, OrderStatus.PAID.ordinal());
    }

    @Test
    void testIsTerminal() {
        assertFalse(OrderStatus.CREATED.isTerminal());
        assertFalse(OrderStatus.PAID.isTerminal());
        assertTrue(OrderStatus.DELIVERED.isTerminal());
        assertTrue(OrderStatus.CANCELLED.isTerminal());
    }

    @Test
    void testGetDescription() {
        assertEquals("已创建", OrderStatus.CREATED.getDescription());
        assertEquals("已取消", OrderStatus.CANCELLED.getDescription());
    }
}
