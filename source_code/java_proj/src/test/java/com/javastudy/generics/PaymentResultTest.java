package com.javastudy.generics;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class PaymentResultTest {

    @Test
    void testPaymentSuccess() {
        PaymentResult result = new PaymentResult.PaymentSuccess("TX-001", 99.99);
        assertInstanceOf(PaymentResult.PaymentSuccess.class, result);
        PaymentResult.PaymentSuccess success = (PaymentResult.PaymentSuccess) result;
        assertEquals("TX-001", success.transactionId());
        assertEquals(99.99, success.amount(), 0.01);
    }

    @Test
    void testPaymentFailure() {
        PaymentResult result = new PaymentResult.PaymentFailure("INSUFFICIENT_FUNDS", "Not enough money");
        assertInstanceOf(PaymentResult.PaymentFailure.class, result);
        PaymentResult.PaymentFailure failure = (PaymentResult.PaymentFailure) result;
        assertEquals("INSUFFICIENT_FUNDS", failure.errorCode());
    }

    @Test
    void testDescribeSuccess() {
        PaymentResult result = new PaymentResult.PaymentSuccess("TX-001", 99.99);
        String desc = PaymentResult.describe(result);
        assertTrue(desc.contains("TX-001"));
        assertTrue(desc.contains("99.99"));
    }

    @Test
    void testDescribeFailure() {
        PaymentResult result = new PaymentResult.PaymentFailure("ERR", "failed");
        String desc = PaymentResult.describe(result);
        assertTrue(desc.contains("ERR"));
        assertTrue(desc.contains("failed"));
    }
}
