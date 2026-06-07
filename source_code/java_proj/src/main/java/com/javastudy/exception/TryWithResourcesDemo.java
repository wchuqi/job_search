package com.javastudy.exception;

import java.util.ArrayList;
import java.util.List;

/**
 * 知识点：try-with-resources (AutoCloseable)
 * 自动关闭资源
 */
public class TryWithResourcesDemo {

    /**
     * 自定义 AutoCloseable 资源
     */
    public static class MockResource implements AutoCloseable {
        private final String name;
        private boolean closed = false;
        private static final List<String> closeLog = new ArrayList<>();

        public MockResource(String name) {
            this.name = name;
            closeLog.add("opened:" + name);
        }

        public String use() {
            if (closed) throw new IllegalStateException("Resource already closed");
            return "using:" + name;
        }

        @Override
        public void close() {
            closed = true;
            closeLog.add("closed:" + name);
        }

        public boolean isClosed() { return closed; }

        public static List<String> getCloseLog() { return closeLog; }
        public static void clearLog() { closeLog.clear(); }
    }

    /**
     * 使用 try-with-resources 自动关闭
     */
    public static String useResource(String name) {
        try (MockResource resource = new MockResource(name)) {
            return resource.use();
        }
    }

    /**
     * 多个资源自动关闭（按声明逆序关闭）
     */
    public static String useMultipleResources() {
        try (MockResource r1 = new MockResource("first");
             MockResource r2 = new MockResource("second")) {
            return r1.use() + "," + r2.use();
        }
    }
}
