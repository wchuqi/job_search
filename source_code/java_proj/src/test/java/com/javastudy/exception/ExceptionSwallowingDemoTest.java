package com.javastudy.exception;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class ExceptionSwallowingDemoTest {

    @Test
    void testSwallowException() {
        String result = ExceptionSwallowingDemo.swallowException();
        assertEquals("no error (swallowed)", result);
    }

    @Test
    void testProperHandling() {
        String result = ExceptionSwallowingDemo.properHandling();
        assertEquals("caught: Something went wrong", result);
    }

    @Test
    void testWasErrorSwallowed() {
        assertTrue(ExceptionSwallowingDemo.wasErrorSwallowed());
    }
}
