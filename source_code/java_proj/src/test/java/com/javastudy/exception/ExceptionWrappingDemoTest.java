package com.javastudy.exception;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class ExceptionWrappingDemoTest {

    @Test
    void testExceptionWrapping() {
        var ex = assertThrows(ExceptionWrappingDemo.UserLoadException.class,
            () -> ExceptionWrappingDemo.loadUser(42));
        assertEquals("Failed to load user: 42", ex.getMessage());
    }

    @Test
    void testCauseIsPreserved() {
        var ex = assertThrows(ExceptionWrappingDemo.UserLoadException.class,
            () -> ExceptionWrappingDemo.loadUser(42));
        assertNotNull(ex.getCause());
        assertEquals(ExceptionWrappingDemo.SimulatedSQLException.class, ex.getCause().getClass());
    }

    @Test
    void testCauseType() {
        var ex = assertThrows(ExceptionWrappingDemo.UserLoadException.class,
            () -> ExceptionWrappingDemo.loadUser(42));
        assertEquals(ExceptionWrappingDemo.SimulatedSQLException.class,
            ExceptionWrappingDemo.getCauseType(ex));
    }
}
