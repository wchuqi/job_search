package com.javastudy.exception;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class TryWithResourcesDemoTest {

    @BeforeEach
    void setUp() {
        TryWithResourcesDemo.MockResource.clearLog();
    }

    @Test
    void testResourceAutoClosed() {
        String result = TryWithResourcesDemo.useResource("test");
        assertEquals("using:test", result);
        // 验证资源被自动关闭
        assertTrue(TryWithResourcesDemo.MockResource.getCloseLog().contains("closed:test"));
    }

    @Test
    void testMultipleResourcesClosedInReverseOrder() {
        String result = TryWithResourcesDemo.useMultipleResources();
        assertEquals("using:first,using:second", result);
        // 验证关闭顺序：second先关闭，first后关闭
        var log = TryWithResourcesDemo.MockResource.getCloseLog();
        int secondCloseIdx = log.indexOf("closed:second");
        int firstCloseIdx = log.indexOf("closed:first");
        assertTrue(secondCloseIdx < firstCloseIdx);
    }

    @Test
    void testResourceClosedEvenOnException() {
        TryWithResourcesDemo.MockResource resource = new TryWithResourcesDemo.MockResource("test");
        assertFalse(resource.isClosed());
        try (var r = resource) {
            throw new RuntimeException("error");
        } catch (RuntimeException ignored) {}
        assertTrue(resource.isClosed()); // 即使抛异常也会关闭
    }
}
