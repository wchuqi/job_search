package com.javastudy.exception;

/**
 * 知识点：反模式 - 吞异常 (Swallowing Exceptions)
 * 空catch块隐藏错误，导致问题难以排查
 */
public class ExceptionSwallowingDemo {

    /**
     * 反模式：空catch块吞掉异常
     */
    public static String swallowException() {
        try {
            throw new RuntimeException("Something went wrong");
        } catch (Exception e) {
            // 空catch块 - 异常被吞掉，错误被隐藏
            return "no error (swallowed)";
        }
    }

    /**
     * 正确做法：记录并传播异常
     */
    public static String properHandling() {
        try {
            throw new RuntimeException("Something went wrong");
        } catch (Exception e) {
            // 正确做法：记录日志并重新抛出
            return "caught: " + e.getMessage();
        }
    }

    /**
     * 演示吞异常导致的后果：调用者无法知道发生了错误
     */
    public static boolean wasErrorSwallowed() {
        String result = swallowException();
        // 调用者无法区分"没有错误"和"错误被吞掉"
        return result.contains("no error");
    }
}
