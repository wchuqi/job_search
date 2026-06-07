package com.javastudy.oop;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class OrderServiceTest {

    @Test
    void testPlaceOrderSuccess() {
        OrderService.PaymentClient mockClient = amount -> true;
        OrderService service = new OrderService(mockClient);
        assertEquals("Order ORD-001 placed successfully", service.placeOrder("ORD-001", 100));
    }

    @Test
    void testPlaceOrderFailure() {
        OrderService.PaymentClient mockClient = amount -> false;
        OrderService service = new OrderService(mockClient);
        assertEquals("Order ORD-002 failed: payment declined", service.placeOrder("ORD-002", 100));
    }
}
