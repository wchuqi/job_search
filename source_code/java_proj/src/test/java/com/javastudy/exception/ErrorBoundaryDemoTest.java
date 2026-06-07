package com.javastudy.exception;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class ErrorBoundaryDemoTest {

    @Test
    void testSuccessBoundary() {
        String result = ErrorBoundaryDemo.runWithErrorBoundary(() -> {});
        assertEquals("success", result);
    }

    @Test
    void testBusinessExceptionBoundary() {
        String result = ErrorBoundaryDemo.runWithErrorBoundary(() -> {
            throw new BusinessException("ERR_001", "Business error");
        });
        assertEquals("business error: [ERR_001] Business error", result);
    }

    @Test
    void testUnexpectedExceptionBoundary() {
        String result = ErrorBoundaryDemo.runWithErrorBoundary(() -> {
            throw new RuntimeException("Unexpected");
        });
        assertEquals("unexpected error: Unexpected", result);
    }

    @Test
    void testDivideWithAssert() {
        assertEquals(5, ErrorBoundaryDemo.divideWithAssert(10, 2));
    }
}
