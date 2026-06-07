package com.example.algo;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/** Dependency-free test runner for environments where JUnit is unavailable. */
public final class TestRunner {
    private TestRunner() {
    }

    public static void main(String[] args) {
        // 每次运行前清空覆盖记录，避免上一次运行影响本次结果。
        Coverage.reset();
        List<String> failures = new ArrayList<>();
        // 当前项目只有一个测试套件；后续可以继续往这里追加测试类。
        run("AlgoLibraryTest", AlgoLibraryTest::runAll, failures);

        // 测试断言通过后，再检查是否所有文档知识点都被命中过。
        Set<CoverageTopic> missed = Coverage.missedTopics();
        if (!missed.isEmpty()) {
            failures.add("Knowledge coverage missed topics: " + missed);
        }

        if (!failures.isEmpty()) {
            // 只要有一个断言失败或一个知识点漏测，就让进程以失败状态退出。
            failures.forEach(System.err::println);
            throw new AssertionError(failures.size() + " test failure(s)");
        }

        System.out.println("All tests passed. Knowledge coverage: 100% (" + CoverageTopic.values().length + " topics).");
    }

    private static void run(String name, Runnable test, List<String> failures) {
        try {
            test.run();
            System.out.println("[PASS] " + name);
        } catch (Throwable error) {
            failures.add("[FAIL] " + name + ": " + error.getMessage());
        }
    }
}
