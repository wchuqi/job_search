package com.javastudy.exception;

import java.io.IOException;
import java.sql.SQLException;

/**
 * 知识点：异常层次结构
 * checked: IOException, SQLException (必须声明或捕获)
 * unchecked: IllegalArgumentException, NullPointerException (RuntimeException子类)
 * Error: OutOfMemoryError, StackOverflowError (不应捕获)
 */
public class ExceptionHierarchyDemo {

    /**
     * 抛出checked异常：必须声明throws
     */
    public static void throwCheckedException() throws IOException {
        throw new IOException("Checked exception example");
    }

    /**
     * 抛出unchecked异常：无需声明throws
     */
    public static void throwUncheckedException() {
        throw new IllegalArgumentException("Unchecked exception example");
    }

    /**
     * 抛出Error：不应捕获
     */
    public static void throwError() {
        throw new OutOfMemoryError("Error example");
    }

    /**
     * 验证异常类型
     */
    public static boolean isCheckedException(Throwable t) {
        return t instanceof Exception && !(t instanceof RuntimeException);
    }

    public static boolean isUncheckedException(Throwable t) {
        return t instanceof RuntimeException;
    }

    public static boolean isError(Throwable t) {
        return t instanceof Error;
    }
}
