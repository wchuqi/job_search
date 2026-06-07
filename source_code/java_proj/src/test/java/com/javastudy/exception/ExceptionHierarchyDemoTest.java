package com.javastudy.exception;

import org.junit.jupiter.api.Test;
import java.io.IOException;
import static org.junit.jupiter.api.Assertions.*;

class ExceptionHierarchyDemoTest {

    @Test
    void testCheckedException() {
        assertThrows(IOException.class,
            ExceptionHierarchyDemo::throwCheckedException);
    }

    @Test
    void testUncheckedException() {
        assertThrows(IllegalArgumentException.class,
            ExceptionHierarchyDemo::throwUncheckedException);
    }

    @Test
    void testError() {
        assertThrows(OutOfMemoryError.class,
            ExceptionHierarchyDemo::throwError);
    }

    @Test
    void testIsCheckedException() {
        assertTrue(ExceptionHierarchyDemo.isCheckedException(new IOException("test")));
        assertFalse(ExceptionHierarchyDemo.isCheckedException(new IllegalArgumentException("test")));
    }

    @Test
    void testIsUncheckedException() {
        assertTrue(ExceptionHierarchyDemo.isUncheckedException(new IllegalArgumentException("test")));
        assertFalse(ExceptionHierarchyDemo.isUncheckedException(new IOException("test")));
    }

    @Test
    void testIsError() {
        assertTrue(ExceptionHierarchyDemo.isError(new OutOfMemoryError("test")));
        assertFalse(ExceptionHierarchyDemo.isError(new RuntimeException("test")));
    }
}
