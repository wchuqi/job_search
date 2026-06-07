package com.javastudy.oop;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class ProductTest {

    @Test
    void testCreateProduct() {
        Product p = new Product("Laptop", 999.99);
        assertEquals("Laptop", p.getName());
        assertEquals(999.99, p.getPrice(), 0.01);
    }

    @Test
    void testNullNameThrows() {
        assertThrows(NullPointerException.class, () -> new Product(null, 10));
    }

    @Test
    void testBlankNameThrows() {
        assertThrows(IllegalArgumentException.class, () -> new Product("  ", 10));
    }

    @Test
    void testNegativePriceThrows() {
        assertThrows(IllegalArgumentException.class, () -> new Product("Item", -1));
    }
}
