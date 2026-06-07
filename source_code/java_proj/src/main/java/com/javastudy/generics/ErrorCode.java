package com.javastudy.generics;

/**
 * 知识点：带字段和方法的枚举
 * 枚举可以有字段、构造器、方法
 */
public enum ErrorCode {
    SUCCESS(0, "Success"),
    BAD_REQUEST(400, "Bad Request"),
    UNAUTHORIZED(401, "Unauthorized"),
    FORBIDDEN(403, "Forbidden"),
    NOT_FOUND(404, "Not Found"),
    INTERNAL_ERROR(500, "Internal Server Error");

    private final int code;
    private final String message;

    ErrorCode(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public int getCode() { return code; }
    public String getMessage() { return message; }

    /**
     * 根据code查找枚举
     */
    public static ErrorCode fromCode(int code) {
        for (ErrorCode ec : values()) {
            if (ec.code == code) return ec;
        }
        throw new IllegalArgumentException("Unknown code: " + code);
    }
}
