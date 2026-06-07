package com.javastudy.functional;

import org.junit.jupiter.api.Test;
import java.util.List;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;

class ReduceDemoTest {

    // ── 带 identity ──────────────────────────────────────────────────

    @Test
    void sumComputesTotal() {
        assertEquals(10, ReduceDemo.sum(List.of(1, 2, 3, 4)));
    }

    @Test
    void sumEmptyListReturnsIdentity() {
        assertEquals(0, ReduceDemo.sum(List.of()));
    }

    @Test
    void productComputesProduct() {
        assertEquals(24, ReduceDemo.product(List.of(1, 2, 3, 4)));
    }

    @Test
    void productEmptyListReturnsIdentity() {
        assertEquals(1, ReduceDemo.product(List.of()));
    }

    @Test
    void joinWithCommaJoinsItems() {
        assertEquals("a, b, c", ReduceDemo.joinWithComma(List.of("a", "b", "c")));
    }

    @Test
    void joinWithCommaEmptyReturnsEmpty() {
        assertEquals("", ReduceDemo.joinWithComma(List.of()));
    }

    @Test
    void joinWithCommaSingleItem() {
        assertEquals("only", ReduceDemo.joinWithComma(List.of("only")));
    }

    @Test
    void findMaxFindsMaximum() {
        assertEquals(5, ReduceDemo.findMax(List.of(1, 5, 3, 2)));
    }

    @Test
    void findMaxEmptyReturnsMinValue() {
        assertEquals(Integer.MIN_VALUE, ReduceDemo.findMax(List.of()));
    }

    // ── 无 identity ──────────────────────────────────────────────────

    @Test
    void sumOptionalReturnsSum() {
        Optional<Integer> result = ReduceDemo.sumOptional(List.of(1, 2, 3));
        assertEquals(6, result.orElseThrow());
    }

    @Test
    void sumOptionalEmptyReturnsEmpty() {
        Optional<Integer> result = ReduceDemo.sumOptional(List.of());
        assertTrue(result.isEmpty());
    }

    @Test
    void joinOptionalReturnsJoined() {
        Optional<String> result = ReduceDemo.joinOptional(List.of("a", "b", "c"));
        assertEquals("a, b, c", result.orElseThrow());
    }

    @Test
    void joinOptionalEmptyReturnsEmpty() {
        Optional<String> result = ReduceDemo.joinOptional(List.of());
        assertTrue(result.isEmpty());
    }

    // ── identity 重要性 ──────────────────────────────────────────────

    @Test
    void sumEmptyWithIdentityReturnsZero() {
        assertEquals(0, ReduceDemo.sumEmptyWithIdentity());
    }

    @Test
    void sumEmptyWithoutIdentityReturnsEmptyOptional() {
        Optional<Integer> result = ReduceDemo.sumEmptyWithoutIdentity();
        assertTrue(result.isEmpty());
    }

    // ── 三参数 reduce ────────────────────────────────────────────────

    @Test
    void sumParallelComputesCorrectSum() {
        assertEquals(15, ReduceDemo.sumParallel(List.of(1, 2, 3, 4, 5)));
    }

    @Test
    void sumParallelEmptyReturnsZero() {
        assertEquals(0, ReduceDemo.sumParallel(List.of()));
    }

    @Test
    void toUpperCaseViaReduceTransformsAll() {
        var result = ReduceDemo.toUpperCaseViaReduce(List.of("a", "b", "c"));
        assertEquals(List.of("A", "B", "C"), result);
    }
}
