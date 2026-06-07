package com.javastudy.functional;

import org.junit.jupiter.api.Test;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

class LambdaDemoTest {

    @Test
    void noArgsExecutesLambda() {
        assertEquals("executed", LambdaDemo.noArgs());
    }

    @Test
    void singleArgTransformsToUpperCase() {
        assertEquals("HELLO", LambdaDemo.singleArg("hello"));
    }

    @Test
    void multiArgsAddsNumbers() {
        assertEquals(5, LambdaDemo.multiArgs(2, 3));
    }

    @Test
    void multiStatementJoinsWithComma() {
        assertEquals("a, b, c", LambdaDemo.multiStatement(List.of("a", "b", "c")));
    }

    @Test
    void multiStatementHandlesSingleItem() {
        assertEquals("only", LambdaDemo.multiStatement(List.of("only")));
    }

    @Test
    void sortWithInferredTypeSortsByLength() {
        var result = LambdaDemo.sortWithInferredType(List.of("banana", "a", "cherry", "bb"));
        assertEquals(List.of("a", "bb", "banana", "cherry"), result);
    }

    @Test
    void inferredFromTargetSquaresInput() {
        assertEquals(9, LambdaDemo.inferredFromTarget(3));
        assertEquals(0, LambdaDemo.inferredFromTarget(0));
    }

    @Test
    void captureEffectivelyFinalUsesPrefix() {
        assertEquals("greeting: hello", LambdaDemo.captureEffectivelyFinal("greeting"));
    }

    @Test
    void captureMutableStateIncrementsCounter() {
        int[] counter = LambdaDemo.captureMutableState();
        assertEquals(2, counter[0]);
    }

    @Test
    void sortByLengthSortsByStringLength() {
        var result = LambdaDemo.sortByLength(List.of("banana", "a", "cherry", "bb"));
        assertEquals(List.of("a", "bb", "banana", "cherry"), result);
    }

    @Test
    void runWithLambdaReturnsDone() {
        assertEquals("done", LambdaDemo.runWithLambda());
    }
}
