package com.javastudy.collections;

import org.junit.jupiter.api.Test;

import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ConcurrentHashMap;

import static org.junit.jupiter.api.Assertions.*;

class ConcurrentHashMapDemoTest {

    private final ConcurrentHashMapDemo demo = new ConcurrentHashMapDemo();

    @Test
    void basicOperationsReturnsCorrectMap() {
        Map<String, Integer> map = demo.basicOperations();
        assertEquals(3, map.size());
        assertEquals(1, map.get("A"));
    }

    @Test
    void mergeCombinesValues() {
        Map<String, Integer> map = demo.mergeDemo();
        assertEquals(11, map.get("A"));  // 1 + 10
        assertEquals(5, map.get("B"));   // new entry
    }

    @Test
    void computeAccumulatesValues() {
        Map<String, Integer> map = demo.computeDemo();
        assertEquals(2, map.get("count"));
    }

    @Test
    void computeIfAbsentDoesNotOverwrite() {
        Map<String, String> map = demo.computeIfAbsentDemo();
        assertEquals("existing", map.get("A"));
        assertEquals("new_B", map.get("B"));
    }

    @Test
    void computeIfPresentOnlyUpdatesExisting() {
        Map<String, Integer> map = demo.computeIfPresentDemo();
        assertEquals(20, map.get("A"));  // 10 * 2
        assertNull(map.get("B"));        // not created
    }

    @Test
    void atomicCounterSumsAllKeys() {
        int total = demo.atomicCounter("A", "B", "A", "C", "B", "A");
        assertEquals(6, total);
    }

    @Test
    void replaceAtomicallyUpdates() {
        Map<String, Integer> map = demo.replaceDemo();
        assertEquals(99, map.get("A"));
        assertNull(map.get("B"));
    }

    @Test
    void concurrentMergeIsThreadSafe() throws InterruptedException {
        ConcurrentHashMap<String, Integer> map = new ConcurrentHashMap<>();
        int threadCount = 10;
        int incrementsPerThread = 1000;
        CountDownLatch latch = new CountDownLatch(threadCount);

        for (int i = 0; i < threadCount; i++) {
            new Thread(() -> {
                for (int j = 0; j < incrementsPerThread; j++) {
                    map.merge("count", 1, Integer::sum);
                }
                latch.countDown();
            }).start();
        }

        latch.await();
        assertEquals(threadCount * incrementsPerThread, map.get("count"));
    }
}
