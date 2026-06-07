package com.javastudy.oop;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class PayableTest {

    @Test
    void testInvoiceImplementsPayable() {
        Invoice invoice = new Invoice(100);
        Payable payable = invoice;
        assertEquals(100, payable.amount());
        assertFalse(invoice.isPaid());
        payable.pay();
        assertTrue(invoice.isPaid());
    }

    @Test
    void testDefaultSupportsRefund() {
        Invoice invoice = new Invoice(100);
        assertTrue(invoice.supportsRefund()); // 默认方法返回true
    }
}
