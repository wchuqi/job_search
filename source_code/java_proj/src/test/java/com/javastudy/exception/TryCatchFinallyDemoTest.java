package com.javastudy.exception;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class TryCatchFinallyDemoTest {

    @Test
    void testNormalExecution() {
        assertEquals("processed:10:finally",
            TryCatchFinallyDemo.tryCatchFinally(10));
    }

    @Test
    void testExceptionCaught() {
        assertEquals("caught:Negative input: -1:finally",
            TryCatchFinallyDemo.tryCatchFinally(-1));
    }

    @Test
    void testMultipleCatchIO() {
        assertEquals("IO: IO error",
            TryCatchFinallyDemo.multipleCatchBlocks("io"));
    }

    @Test
    void testMultipleCatchSQL() {
        assertEquals("SQL: SQL error",
            TryCatchFinallyDemo.multipleCatchBlocks("sql"));
    }

    @Test
    void testMultipleCatchRuntime() {
        assertEquals("General: Runtime error",
            TryCatchFinallyDemo.multipleCatchBlocks("runtime"));
    }

    @Test
    void testMultipleCatchNoError() {
        assertEquals("no error",
            TryCatchFinallyDemo.multipleCatchBlocks("none"));
    }
}
