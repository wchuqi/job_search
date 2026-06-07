package com.javastudy.exception;

/**
 * 知识点：异常包装/转换 (Exception Wrapping)
 * 捕获底层异常，包装为领域异常，保留原始cause
 */
public class ExceptionWrappingDemo {

    /**
     * 自定义领域异常
     */
    public static class UserLoadException extends RuntimeException {
        public UserLoadException(String message, Throwable cause) {
            super(message, cause);
        }
    }

    /**
     * 模拟底层SQL异常
     */
    public static class SimulatedSQLException extends Exception {
        public SimulatedSQLException(String message) {
            super(message);
        }
    }

    /**
     * 模拟加载用户时发生SQL异常，包装为领域异常
     */
    public static void loadUser(long userId) {
        try {
            // 模拟数据库操作
            throw new SimulatedSQLException("Connection timeout");
        } catch (SimulatedSQLException e) {
            throw new UserLoadException("Failed to load user: " + userId, e);
        }
    }

    /**
     * 获取包装异常的原始cause类型
     */
    public static Class<?> getCauseType(RuntimeException e) {
        Throwable cause = e.getCause();
        return cause != null ? cause.getClass() : null;
    }
}
