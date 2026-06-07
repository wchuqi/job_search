package com.javastudy.reflection;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ReflectionProxyDemoTest {

    private final ReflectionProxyDemo demo = new ReflectionProxyDemo();

    @Test
    void reflectionReadsClassAnnotationConstructorsAndMethods() throws Exception {
        assertEquals("UppercaseCommand", demo.inspectClass(UppercaseCommand.class).getFirst());
        assertEquals("upper", demo.annotationName(UppercaseCommand.class));
        Command command = (Command) demo.construct(UppercaseCommand.class);
        assertEquals("JAVA", demo.invokeExecute(command, "java"));
    }

    @Test
    void methodHandleDynamicProxyAndServiceLoaderWork() throws Throwable {
        assertEquals("cba", demo.methodHandleExecute(new ReverseCommand(), "abc"));

        List<String> calls = new ArrayList<>();
        Command proxy = demo.timingProxy(new UppercaseCommand(), calls);
        assertEquals("AOP", proxy.execute("aop"));
        assertEquals(List.of("execute"), calls);

        Map<String, Command> commands = demo.loadCommands();
        assertEquals("SPI", commands.get("upper").execute("spi"));
        assertEquals("ips", commands.get("reverse").execute("spi"));
    }

    @Test
    void moduleAndReflectionRiskConceptsAreNamed() {
        assertTrue(demo.jpmsKeywords().contains("opens"));
        assertTrue(demo.reflectionAlternatives().contains("MethodHandle"));
    }
}
