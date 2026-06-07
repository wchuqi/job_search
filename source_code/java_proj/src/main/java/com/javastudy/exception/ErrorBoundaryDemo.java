package com.javastudy.exception;

/**
 * 知识点：错误边界 (Error Boundary)
 * 在最外层捕获所有未处理异常
 */
public class ErrorBoundaryDemo {

    /**
     * 模拟main方法的错误边界
     */
    public static String runWithErrorBoundary(Runnable task) {
        try {
            task.run();
            return "success";
        } catch (BusinessException e) {
            return "business error: [%s] %s".formatted(e.getCode(), e.getMessage());
        } catch (Exception e) {
            return "unexpected error: " + e.getMessage();
        }
    }

    /**
     * 使用assert验证前置条件
     */
    public static int divideWithAssert(int a, int b) {
        assert b != 0 : "Divisor must not be zero";
        return a / b;
    }
}
