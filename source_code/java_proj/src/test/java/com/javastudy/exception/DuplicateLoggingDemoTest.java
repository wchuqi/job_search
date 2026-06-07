package com.javastudy.exception;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class DuplicateLoggingDemoTest {

    @BeforeEach
    void setUp() {
        DuplicateLoggingDemo.clearLog();
    }

    @Test
    void testDuplicateLogging() {
        assertThrows(RuntimeException.class,
            DuplicateLoggingDemo::controllerLayer);

        var logs = DuplicateLoggingDemo.getLogEntries();
        // 同一条错误被记录了3次（反模式演示）
        assertEquals(3, logs.size());
        assertEquals("DB layer: DB connection failed", logs.get(0));
        assertEquals("Service layer: DB connection failed", logs.get(1));
        assertEquals("Controller layer: DB connection failed", logs.get(2));
    }
}
