package com.javastudy.oop;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class OverloadDemoTest {

    @Test
    void testOverloadedMethods() {
        OverloadDemo demo = new OverloadDemo();
        assertEquals("String: hello", demo.print("hello"));
        assertEquals("int: 42", demo.print(42));
        assertEquals("double: 3.14", demo.print(3.14));
        assertEquals("Age: 25", demo.print("Age", 25));
    }
}
