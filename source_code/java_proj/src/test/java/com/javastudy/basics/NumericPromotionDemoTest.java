package com.javastudy.basics;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class NumericPromotionDemoTest {

    @Test
    void testAddBytesReturnsInt() {
        // byte + byte -> int
        int result = NumericPromotionDemo.addBytes((byte) 100, (byte) 50);
        assertEquals(150, result);
    }

    @Test
    void testAddBytesWithCast() {
        byte result = NumericPromotionDemo.addBytesWithCast((byte) 10, (byte) 20);
        assertEquals(30, result);
    }

    @Test
    void testAddBytesOverflowWithCast() {
        // 100 + 50 = 150, 超出byte范围(-128~127), 强转后溢出
        byte result = NumericPromotionDemo.addBytesWithCast((byte) 100, (byte) 50);
        assertEquals((byte) 150, result); // -106 (溢出)
    }

    @Test
    void testMixedArithmeticLong() {
        long result = NumericPromotionDemo.mixedArithmetic(100, 200L);
        assertEquals(300L, result);
    }

    @Test
    void testMixedArithmeticDouble() {
        double result = NumericPromotionDemo.mixedArithmeticDouble(10, 3.14);
        assertEquals(13.14, result, 0.001);
    }

    @Test
    void testCharArithmetic() {
        int result = NumericPromotionDemo.charArithmetic('A');
        assertEquals(66, result); // 'A' = 65, 65 + 1 = 66
    }
}
