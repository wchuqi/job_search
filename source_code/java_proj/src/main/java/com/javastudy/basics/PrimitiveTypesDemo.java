package com.javastudy.basics;

/**
 * 知识点：Java 8种基本数据类型及其范围
 * byte(8bit), short(16), int(32), long(64), float(32), double(64), char(16), boolean
 */
public class PrimitiveTypesDemo {

    // byte: -128 ~ 127
    public static final byte BYTE_MIN = Byte.MIN_VALUE;   // -128
    public static final byte BYTE_MAX = Byte.MAX_VALUE;   // 127

    // short: -32768 ~ 32767
    public static final short SHORT_MIN = Short.MIN_VALUE;
    public static final short SHORT_MAX = Short.MAX_VALUE;

    // int: -2^31 ~ 2^31-1
    public static final int INT_MIN = Integer.MIN_VALUE;
    public static final int INT_MAX = Integer.MAX_VALUE;

    // long: -2^63 ~ 2^63-1
    public static final long LONG_MIN = Long.MIN_VALUE;
    public static final long LONG_MAX = Long.MAX_VALUE;

    // float: IEEE 754 单精度
    public static final float FLOAT_MIN_POSITIVE = Float.MIN_VALUE;

    // double: IEEE 754 双精度
    public static final double DOUBLE_MIN_POSITIVE = Double.MIN_VALUE;

    // char: 0 ~ 65535 (无符号)
    public static final char CHAR_MIN = Character.MIN_VALUE;  // 0
    public static final char CHAR_MAX = Character.MAX_VALUE;  // 65535

    // boolean: true / false (JVM规范未定义大小)
    public static final boolean DEFAULT_BOOLEAN = false;

    /**
     * 获取各类型的字节大小
     */
    public static int getByteSize(String typeName) {
        return switch (typeName) {
            case "byte" -> Byte.BYTES;       // 1
            case "short" -> Short.BYTES;     // 2
            case "int" -> Integer.BYTES;     // 4
            case "long" -> Long.BYTES;       // 8
            case "float" -> Float.BYTES;     // 4
            case "double" -> Double.BYTES;   // 8
            case "char" -> Character.BYTES;  // 2
            case "boolean" -> 1;             // JVM实现相关
            default -> throw new IllegalArgumentException("Unknown type: " + typeName);
        };
    }

    /**
     * 自动装箱与拆箱演示
     */
    public static Integer autoBox(int value) {
        return value; // 自动装箱 int -> Integer
    }

    public static int autoUnbox(Integer value) {
        return value; // 自动拆箱 Integer -> int
    }

    /**
     * 缓存池演示: Integer缓存 -128~127
     */
    public static boolean isCachedIntegerRange(int value) {
        Integer a = value;
        Integer b = value;
        return a == b; // -128~127 范围内 == 为 true
    }
}
