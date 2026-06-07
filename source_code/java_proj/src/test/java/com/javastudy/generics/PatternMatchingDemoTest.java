package com.javastudy.generics;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class PatternMatchingDemoTest {

    @Test
    void testDescribeString() {
        assertEquals("String of length 5: hello",
            PatternMatchingDemo.describeObject("hello"));
    }

    @Test
    void testDescribeInteger() {
        assertEquals("Integer: 42",
            PatternMatchingDemo.describeObject(42));
    }

    @Test
    void testDescribeDouble() {
        assertEquals("Double: 3.14",
            PatternMatchingDemo.describeObject(3.14));
    }

    @Test
    void testDescribeUnknown() {
        assertTrue(PatternMatchingDemo.describeObject(new Object())
            .startsWith("Unknown: java.lang.Object@"));
    }

    @Test
    void testSwitchNull() {
        assertEquals("null value", PatternMatchingDemo.switchPatternMatch(null));
    }

    @Test
    void testSwitchString() {
        assertEquals("String: hello", PatternMatchingDemo.switchPatternMatch("hello"));
    }

    @Test
    void testSwitchLongString() {
        String longStr = "a".repeat(20);
        String result = PatternMatchingDemo.switchPatternMatch(longStr);
        assertTrue(result.startsWith("Long string: "));
    }

    @Test
    void testSwitchInteger() {
        assertEquals("Integer: 42", PatternMatchingDemo.switchPatternMatch(42));
    }

    @Test
    void testDeconstructPoint() {
        assertEquals("Point at (3, 4)",
            PatternMatchingDemo.deconstructPoint(new PatternMatchingDemo.Point(3, 4)));
    }

    @Test
    void testDeconstructNotPoint() {
        assertEquals("Not a point",
            PatternMatchingDemo.deconstructPoint("not a point"));
    }
}
