package com.javastudy.generics;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class ErrorCodeTest {

    @Test
    void testCodeAndMessage() {
        assertEquals(0, ErrorCode.SUCCESS.getCode());
        assertEquals("Success", ErrorCode.SUCCESS.getMessage());
        assertEquals(404, ErrorCode.NOT_FOUND.getCode());
        assertEquals("Not Found", ErrorCode.NOT_FOUND.getMessage());
    }

    @Test
    void testFromCode() {
        assertEquals(ErrorCode.SUCCESS, ErrorCode.fromCode(0));
        assertEquals(ErrorCode.NOT_FOUND, ErrorCode.fromCode(404));
    }

    @Test
    void testFromCodeInvalid() {
        assertThrows(IllegalArgumentException.class, () -> ErrorCode.fromCode(999));
    }
}
