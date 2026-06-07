package com.javastudy.generics;

import org.junit.jupiter.api.Test;
import java.lang.reflect.Method;
import static org.junit.jupiter.api.Assertions.*;

class AnnotationDemoTest {

    @Test
    void testAnnotationPresent() throws Exception {
        Method method = AnnotationDemo.class.getMethod("createOrder", String.class);
        assertTrue(method.isAnnotationPresent(Audited.class));
    }

    @Test
    void testAnnotationValue() throws Exception {
        Method method = AnnotationDemo.class.getMethod("createOrder", String.class);
        Audited audited = method.getAnnotation(Audited.class);
        assertEquals("创建订单", audited.value());
    }

    @Test
    void testAnnotationNotPresent() throws Exception {
        Method method = AnnotationDemo.class.getMethod("unannotatedMethod");
        assertFalse(method.isAnnotationPresent(Audited.class));
    }
}
