package com.javastudy.concurrency;

import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ConcurrencyDemoTest {

    private final ConcurrencyDemo demo = new ConcurrencyDemo();

    @Test
    void runnableCallableAndFuturesReturnResults() throws Exception {
        assertEquals("ran:called", demo.runRunnableAndCallable());
    }

    @Test
    void synchronizedAndAtomicCountersAreThreadSafe() throws Exception {
        assertEquals(400, demo.synchronizedCounter(4, 100));
        assertEquals(400, demo.atomicCounter(4, 100));
    }

    @Test
    void lockConditionAndBlockingQueueCoordinateThreads() throws Exception {
        assertEquals(List.of("message"), demo.lockConditionExchange("message"));
        assertEquals(List.of("a", "b"), demo.blockingQueueRoundTrip(List.of("a", "b")));
    }

    @Test
    void concurrentHashMapAndCompletableFutureWork() {
        assertEquals(2, demo.concurrentMapCounts(List.of("java", "java", "jvm")).get("java"));
        assertEquals(13, demo.completableFuturePipeline(4));
    }

    @Test
    void jmmThreadLocalAndVirtualThreadsAreDemonstrated() throws Exception {
        assertTrue(demo.threadLocalIsolation().containsAll(List.of("first", "second")));
        assertEquals(42, demo.startJoinHappensBefore());
        assertEquals(7, demo.volatileWriteThenRead());
        assertEquals("virtual", demo.virtualThreadName());
    }

    @Test
    void threadPoolAndStructuredConcurrencyConceptsAreNamed() {
        ThreadPoolExecutor executor = new ThreadPoolExecutor(
                1, 2, 30, TimeUnit.SECONDS, new ArrayBlockingQueue<>(2));
        try {
            assertTrue(demo.threadPoolConfiguration(executor).contains("core=1"));
            assertTrue(demo.structuredConcurrencyConcepts().contains("fork"));
        } finally {
            executor.shutdownNow();
        }
    }
}
