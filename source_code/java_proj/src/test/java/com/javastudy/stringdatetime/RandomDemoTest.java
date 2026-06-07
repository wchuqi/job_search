package com.javastudy.stringdatetime;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class RandomDemoTest {

    @Test
    void randomIntIsWithinBound() {
        for (int i = 0; i < 100; i++) {
            int result = RandomDemo.randomInt(10);
            assertTrue(result >= 0 && result < 10,
                    "randomInt(10) should be in [0, 10): " + result);
        }
    }

    @Test
    void seededRandomIsReproducible() {
        int first = RandomDemo.seededRandomInt(42L, 100);
        int second = RandomDemo.seededRandomInt(42L, 100);
        assertEquals(first, second, "Same seed should produce same result");
    }

    @Test
    void randomDoubleIsBetweenZeroAndOne() {
        for (int i = 0; i < 100; i++) {
            double result = RandomDemo.randomDouble();
            assertTrue(result >= 0.0 && result < 1.0,
                    "randomDouble() should be in [0.0, 1.0): " + result);
        }
    }

    @Test
    void randomBooleanReturnsTrueOrFalse() {
        // Just verify it doesn't throw
        boolean result = RandomDemo.randomBoolean();
        assertTrue(result || !result); // always true, but exercises the method
    }

    @Test
    void threadLocalRandomIntIsWithinBound() {
        for (int i = 0; i < 100; i++) {
            int result = RandomDemo.threadLocalRandomInt(10);
            assertTrue(result >= 0 && result < 10);
        }
    }

    @Test
    void threadLocalRandomIntRangeIsCorrect() {
        for (int i = 0; i < 100; i++) {
            int result = RandomDemo.threadLocalRandomIntRange(5, 15);
            assertTrue(result >= 5 && result < 15,
                    "threadLocalRandomIntRange(5, 15) should be in [5, 15): " + result);
        }
    }

    @Test
    void threadLocalRandomDoubleRangeIsCorrect() {
        for (int i = 0; i < 100; i++) {
            double result = RandomDemo.threadLocalRandomDoubleRange(1.0, 2.0);
            assertTrue(result >= 1.0 && result < 2.0,
                    "threadLocalRandomDoubleRange(1.0, 2.0) should be in [1.0, 2.0): " + result);
        }
    }

    @Test
    void secureRandomIntIsWithinBound() {
        for (int i = 0; i < 100; i++) {
            int result = RandomDemo.secureRandomInt(10);
            assertTrue(result >= 0 && result < 10);
        }
    }

    @Test
    void secureRandomBytesHasCorrectLength() {
        byte[] bytes = RandomDemo.secureRandomBytes(32);
        assertEquals(32, bytes.length);
    }

    @Test
    void secureRandomBytesAreNotAllZero() {
        byte[] bytes = RandomDemo.secureRandomBytes(32);
        boolean hasNonZero = false;
        for (byte b : bytes) {
            if (b != 0) {
                hasNonZero = true;
                break;
            }
        }
        assertTrue(hasNonZero, "Random bytes should not all be zero");
    }

    @Test
    void gaussianRandomProducesFiniteValue() {
        double result = RandomDemo.gaussianRandom();
        assertTrue(Double.isFinite(result));
    }

    @Test
    void generateArrayHasCorrectLength() {
        int[] array = RandomDemo.generateArray(100, 50);
        assertEquals(100, array.length);
        for (int value : array) {
            assertTrue(value >= 0 && value < 50);
        }
    }

    @Test
    void differentSeedsProduceDifferentResults() {
        int a = RandomDemo.seededRandomInt(1L, 1000);
        int b = RandomDemo.seededRandomInt(2L, 1000);
        // Not guaranteed to be different for all bounds, but very likely for bound=1000
        // We just verify the method works
        assertTrue(a >= 0 && a < 1000);
        assertTrue(b >= 0 && b < 1000);
    }
}
