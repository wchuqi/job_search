package com.javastudy.generics;

/**
 * 知识点：instanceof 模式匹配 (JDK 16+)
 * switch 模式匹配 (JDK 21+)
 */
public class PatternMatchingDemo {

    /**
     * instanceof 模式匹配：类型检查 + 变量绑定一步完成
     */
    public static String describeObject(Object obj) {
        if (obj instanceof String text) {
            return "String of length %d: %s".formatted(text.length(), text);
        } else if (obj instanceof Integer num) {
            return "Integer: %d".formatted(num);
        } else if (obj instanceof Double d) {
            return "Double: %.2f".formatted(d);
        } else {
            return "Unknown: " + obj;
        }
    }

    /**
     * switch 模式匹配 (JDK 21+)
     */
    public static String switchPatternMatch(Object obj) {
        return switch (obj) {
            case null -> "null value";
            case String s when s.length() > 10 -> "Long string: " + s.substring(0, 10) + "...";
            case String s -> "String: " + s;
            case Integer i -> "Integer: " + i;
            case Long l -> "Long: " + l;
            case Double d -> "Double: " + d;
            default -> "Other: " + obj.getClass().getSimpleName();
        };
    }

    /**
     * Record 模式解构 (JDK 21+)
     */
    public static String deconstructPoint(Object obj) {
        if (obj instanceof Point(int x, int y)) {
            return "Point at (%d, %d)".formatted(x, y);
        }
        return "Not a point";
    }

    public record Point(int x, int y) {}
}
