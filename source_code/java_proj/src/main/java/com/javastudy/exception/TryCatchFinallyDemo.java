package com.javastudy.exception;

/**
 * 知识点：try-catch-finally
 * 捕获异常并执行清理
 */
public class TryCatchFinallyDemo {

    /**
     * 基本 try-catch-finally
     */
    public static String tryCatchFinally(int input) {
        StringBuilder result = new StringBuilder();
        try {
            if (input < 0) {
                throw new IllegalArgumentException("Negative input: " + input);
            }
            result.append("processed:").append(input);
        } catch (IllegalArgumentException e) {
            result.append("caught:").append(e.getMessage());
        } finally {
            result.append(":finally");
        }
        return result.toString();
    }

    /**
     * 多个catch块：从具体到通用
     */
    public static String multipleCatchBlocks(String type) {
        try {
            return switch (type) {
                case "io" -> {
                    throw new java.io.IOException("IO error");
                }
                case "sql" -> {
                    throw new java.sql.SQLException("SQL error");
                }
                case "runtime" -> {
                    throw new RuntimeException("Runtime error");
                }
                default -> "no error";
            };
        } catch (java.io.IOException e) {
            return "IO: " + e.getMessage();
        } catch (java.sql.SQLException e) {
            return "SQL: " + e.getMessage();
        } catch (Exception e) {
            return "General: " + e.getMessage();
        }
    }
}
