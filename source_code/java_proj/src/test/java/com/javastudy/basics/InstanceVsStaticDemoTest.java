package com.javastudy.basics;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;

class InstanceVsStaticDemoTest {

    @BeforeEach
    void setUp() {
        InstanceVsStaticDemo.resetCounter();
    }

    @Test
    void testStaticCounterIncrements() {
        assertEquals(0, InstanceVsStaticDemo.getCounter());
        new InstanceVsStaticDemo(1);
        assertEquals(1, InstanceVsStaticDemo.getCounter());
        new InstanceVsStaticDemo(2);
        assertEquals(2, InstanceVsStaticDemo.getCounter());
    }

    @Test
    void testInstanceFieldsAreIndependent() {
        InstanceVsStaticDemo obj1 = new InstanceVsStaticDemo(10);
        InstanceVsStaticDemo obj2 = new InstanceVsStaticDemo(20);
        assertEquals(10, obj1.getInstanceValue());
        assertEquals(20, obj2.getInstanceValue());
    }

    @Test
    void testModifyInstanceField() {
        InstanceVsStaticDemo obj = new InstanceVsStaticDemo(10);
        obj.setInstanceValue(99);
        assertEquals(99, obj.getInstanceValue());
    }

    @Test
    void testStaticMethod() {
        new InstanceVsStaticDemo(1);
        String result = InstanceVsStaticDemo.staticMethod();
        assertTrue(result.contains("counter=1"));
    }

    @Test
    void testInstanceMethod() {
        InstanceVsStaticDemo obj = new InstanceVsStaticDemo(42);
        String result = obj.instanceMethod();
        assertTrue(result.contains("instance=42"));
        assertTrue(result.contains("counter=1"));
    }
}
