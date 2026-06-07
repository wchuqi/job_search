package com.javastudy.functional;

import org.junit.jupiter.api.Test;
import java.util.function.Function;
import static org.junit.jupiter.api.Assertions.*;

class FunctionalInterfaceDemoTest {

    @Test
    void toUpperCaseTransformsString() {
        assertEquals("HELLO", FunctionalInterfaceDemo.toUpperCase("hello"));
        assertEquals("WORLD", FunctionalInterfaceDemo.toUpperCase("World"));
    }

    @Test
    void chainTransformAppliesBothTransformers() {
        // first: trim, second: toUpperCase
        var result = FunctionalInterfaceDemo.chainTransform(
                "  hello  ",
                String::trim,
                String::toUpperCase
        );
        assertEquals("HELLO", result);
    }

    @Test
    void addCalculatesSum() {
        assertEquals(5, FunctionalInterfaceDemo.add(2, 3));
        assertEquals(0, FunctionalInterfaceDemo.add(-1, 1));
        assertEquals(-5, FunctionalInterfaceDemo.add(-2, -3));
    }

    @Test
    void multiplyCalculatesProduct() {
        assertEquals(6, FunctionalInterfaceDemo.multiply(2, 3));
        assertEquals(0, FunctionalInterfaceDemo.multiply(5, 0));
        assertEquals(-6, FunctionalInterfaceDemo.multiply(-2, 3));
    }

    @Test
    void lengthFunctionReturnsStringLength() {
        Function<String, Integer> fn = FunctionalInterfaceDemo.lengthFunction();
        assertEquals(5, fn.apply("hello"));
        assertEquals(0, fn.apply(""));
    }

    @Test
    void executeAndCountRunsActionAndReturnsOne() {
        int count = FunctionalInterfaceDemo.executeAndCount(() -> {});
        assertEquals(1, count);
    }

    @Test
    void intCalculatorStaticFactoryMethods() {
        var addCalc = FunctionalInterfaceDemo.IntCalculator.add();
        var mulCalc = FunctionalInterfaceDemo.IntCalculator.multiply();
        assertEquals(10, addCalc.calculate(4, 6));
        assertEquals(24, mulCalc.calculate(4, 6));
    }

    @Test
    void andThenComposesTransformers() {
        FunctionalInterfaceDemo.StringTransformer trim = String::trim;
        FunctionalInterfaceDemo.StringTransformer upper = String::toUpperCase;
        FunctionalInterfaceDemo.StringTransformer combined = trim.andThen(upper);
        assertEquals("HELLO", combined.transform("  hello  "));
    }
}
