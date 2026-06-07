package com.javastudy.exception;

import com.javastudy.Generated;

import java.util.ArrayList;
import java.util.List;

/**
 * 知识点：反模式 - 重复日志记录
 * 每层都catch+log+re-throw导致重复日志
 */
@Generated
public class DuplicateLoggingDemo {

    private static final List<String> logEntries = new ArrayList<>();

    public static List<String> getLogEntries() { return logEntries; }
    public static void clearLog() { logEntries.clear(); }

    /**
     * 底层方法
     */
    public static void databaseOperation() {
        try {
            throw new RuntimeException("DB connection failed");
        } catch (Exception e) {
            logEntries.add("DB layer: " + e.getMessage());
            throw e; // 重新抛出
        }
    }

    /**
     * 中间层 - 又记录一次
     */
    public static void serviceLayer() {
        try {
            databaseOperation();
        } catch (Exception e) {
            logEntries.add("Service layer: " + e.getMessage());
            throw e; // 再次重新抛出
        }
    }

    /**
     * 控制层 - 又记录一次
     */
    public static void controllerLayer() {
        try {
            serviceLayer();
        } catch (Exception e) {
            logEntries.add("Controller layer: " + e.getMessage());
            throw e;
        }
    }
}
