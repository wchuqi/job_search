package com.javastudy.basics;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class ScopeDemoTest {

    @Test
    void testCountTo() {
        assertEquals(55, ScopeDemo.countTo(10));
        assertEquals(0, ScopeDemo.countTo(0));
    }

    @Test
    void testBlockScopeExample() {
        assertEquals("inside if", ScopeDemo.blockScopeExample(true));
        assertEquals("outside", ScopeDemo.blockScopeExample(false));
    }

    @Test
    void testShadowExample() {
        ScopeDemo demo = new ScopeDemo();
        assertEquals(42, demo.shadowExample(42)); // 返回参数值
        assertEquals(100, demo.getFieldValue());  // 返回字段值
    }
}
