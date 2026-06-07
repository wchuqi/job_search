package com.javastudy.exception;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class BusinessExceptionTest {

    @Test
    void testBusinessExceptionCodeAndMessage() {
        BusinessException ex = new BusinessException("USER_NOT_FOUND", "User not found");
        assertEquals("USER_NOT_FOUND", ex.getCode());
        assertEquals("User not found", ex.getMessage());
    }

    @Test
    void testBusinessExceptionWithCause() {
        RuntimeException cause = new RuntimeException("root cause");
        BusinessException ex = new BusinessException("DB_ERROR", "Database error", cause);
        assertEquals("DB_ERROR", ex.getCode());
        assertEquals("Database error", ex.getMessage());
        assertEquals(cause, ex.getCause());
    }
}
