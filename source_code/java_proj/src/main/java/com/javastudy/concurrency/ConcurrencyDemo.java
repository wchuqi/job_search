package com.javastudy.concurrency;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Callable;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class ConcurrencyDemo {

    private volatile boolean ready;
    private int sharedValue;

    public String runRunnableAndCallable() throws Exception {
        StringBuilder runnableResult = new StringBuilder();
        Runnable runnable = () -> runnableResult.append("ran");
        Callable<String> callable = () -> "called";
        try (ExecutorService executor = Executors.newSingleThreadExecutor()) {
            Future<?> runnableFuture = executor.submit(runnable);
            Future<String> callableFuture = executor.submit(callable);
            runnableFuture.get(1, TimeUnit.SECONDS);
            return runnableResult + ":" + callableFuture.get(1, TimeUnit.SECONDS);
        }
    }

    public int synchronizedCounter(int workers, int incrementsPerWorker) throws InterruptedException {
        Counter counter = new Counter();
        List<Thread> threads = new ArrayList<>();
        for (int i = 0; i < workers; i++) {
            Thread thread = new Thread(() -> {
                for (int j = 0; j < incrementsPerWorker; j++) {
                    counter.increment();
                }
            });
            threads.add(thread);
            thread.start();
        }
        for (Thread thread : threads) {
            thread.join();
        }
        return counter.value();
    }

    public int atomicCounter(int workers, int incrementsPerWorker) throws InterruptedException {
        AtomicInteger counter = new AtomicInteger();
        List<Thread> threads = new ArrayList<>();
        for (int i = 0; i < workers; i++) {
            Thread thread = new Thread(() -> {
                for (int j = 0; j < incrementsPerWorker; j++) {
                    counter.incrementAndGet();
                }
            });
            threads.add(thread);
            thread.start();
        }
        for (Thread thread : threads) {
            thread.join();
        }
        return counter.get();
    }

    public List<String> lockConditionExchange(String value) throws InterruptedException {
        Mailbox mailbox = new Mailbox();
        List<String> received = new CopyOnWriteArrayList<>();
        Thread consumer = new Thread(() -> received.add(mailbox.take()));
        consumer.start();
        mailbox.put(value);
        consumer.join(Duration.ofSeconds(1));
        return received;
    }

    public Map<String, Integer> concurrentMapCounts(List<String> words) {
        ConcurrentHashMap<String, Integer> counts = new ConcurrentHashMap<>();
        words.parallelStream().forEach(word -> counts.merge(word, 1, Integer::sum));
        return counts;
    }

    public List<String> blockingQueueRoundTrip(List<String> input) throws InterruptedException {
        BlockingQueue<String> queue = new ArrayBlockingQueue<>(input.size());
        for (String value : input) {
            queue.put(value);
        }
        List<String> output = new ArrayList<>();
        while (!queue.isEmpty()) {
            output.add(queue.take());
        }
        return output;
    }

    public int completableFuturePipeline(int value) {
        return CompletableFuture.supplyAsync(() -> value + 1)
                .thenApply(n -> n * 2)
                .thenApply(n -> n + 3)
                .join();
    }

    public List<String> threadLocalIsolation() throws Exception {
        ThreadLocal<String> local = new ThreadLocal<>();
        try (ExecutorService executor = Executors.newFixedThreadPool(2)) {
            Future<String> first = executor.submit(() -> {
                local.set("first");
                return local.get();
            });
            Future<String> second = executor.submit(() -> {
                local.set("second");
                return local.get();
            });
            return List.of(first.get(1, TimeUnit.SECONDS), second.get(1, TimeUnit.SECONDS));
        } finally {
            local.remove();
        }
    }

    public int startJoinHappensBefore() throws InterruptedException {
        int[] holder = new int[1];
        Thread thread = new Thread(() -> holder[0] = 42);
        thread.start();
        thread.join();
        return holder[0];
    }

    public int volatileWriteThenRead() throws InterruptedException {
        Thread writer = new Thread(() -> {
            sharedValue = 7;
            ready = true;
        });
        writer.start();
        writer.join();
        return ready ? sharedValue : -1;
    }

    public String virtualThreadName() throws Exception {
        Future<String> future = Executors.newVirtualThreadPerTaskExecutor()
                .submit(() -> Thread.currentThread().isVirtual() ? "virtual" : "platform");
        return future.get(1, TimeUnit.SECONDS);
    }

    public List<String> threadPoolConfiguration(ThreadPoolExecutor executor) {
        return List.of(
                "core=" + executor.getCorePoolSize(),
                "max=" + executor.getMaximumPoolSize(),
                "queue=" + executor.getQueue().getClass().getSimpleName(),
                "rejection=" + executor.getRejectedExecutionHandler().getClass().getSimpleName()
        );
    }

    public List<String> structuredConcurrencyConcepts() {
        return List.of("scope", "fork", "join", "cancel siblings on failure", "propagate result");
    }

    static class Counter {
        private int value;

        synchronized void increment() {
            value++;
        }

        synchronized int value() {
            return value;
        }
    }

    static class Mailbox {
        private final ReentrantLock lock = new ReentrantLock();
        private final Condition hasValue = lock.newCondition();
        private String value;

        void put(String newValue) {
            lock.lock();
            try {
                value = newValue;
                hasValue.signalAll();
            } finally {
                lock.unlock();
            }
        }

        String take() {
            lock.lock();
            try {
                while (value == null) {
                    hasValue.awaitUninterruptibly();
                }
                return value;
            } finally {
                lock.unlock();
            }
        }
    }
}
