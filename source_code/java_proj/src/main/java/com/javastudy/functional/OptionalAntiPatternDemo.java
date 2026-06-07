package com.javastudy.functional;

import java.util.Optional;

/**
 * 知识点：Optional 反模式与正确用法对比
 *
 * 反模式：
 * 1. 用 Optional 做字段/参数/集合类型 — Optional 不可序列化，不应用作字段
 * 2. isPresent + get — 比 null 检查更啰嗦，失去 Optional 的意义
 * 3. Optional.ofNullable(null).get() — 直接抛 NoSuchElementException
 * 4. 在集合上用 Optional — 集合本身就可以为空，不需要 Optional<List<T>>
 * 5. orElse 里抛异常 — 应用 orElseThrow
 *
 * 正确用法：
 * 1. 用作返回值，表示"可能没有值"
 * 2. 用 map/flatMap/filter/orElse 链式处理
 * 3. 用 orElse/orElseGet 提供默认值
 */
public class OptionalAntiPatternDemo {

    public record Order(String id, String customerName, String note) {}

    // ── 反模式 1：isPresent + get（应该用 map / orElse） ──────────────

    /** 反模式：isPresent + get */
    public static String badGetCity(Optional<String> city) {
        if (city.isPresent()) {
            return city.get();
        }
        return "unknown";
    }

    /** 正确：orElse 一行搞定 */
    public static String goodGetCity(Optional<String> city) {
        return city.orElse("unknown");
    }

    // ── 反模式 2：Optional 做方法参数 ─────────────────────────────────

    /** 反模式：Optional 做参数（调用方被迫包装） */
    public static String badFindUser(Optional<String> username) {
        return username.orElse("anonymous");
    }

    /** 正确：参数用 nullable，返回值用 Optional */
    public static Optional<String> goodFindUser(String username) {
        return Optional.ofNullable(username);
    }

    // ── 反模式 3：对 null 调用 Optional.of ───────────────────────────

    /** 反模式：对 null 用 Optional.of 会 NPE */
    public static boolean badCheckNull(String value) {
        try {
            Optional.of(value); // value 为 null 时抛 NPE
            return true;
        } catch (NullPointerException e) {
            return false;
        }
    }

    /** 正确：用 ofNullable */
    public static boolean goodCheckNull(String value) {
        return Optional.ofNullable(value).isPresent();
    }

    // ── 反模式 4：在 orElse 里执行副作用 / 返回 null ──────────────────

    /** 反模式：orElse 返回 null — 创建了 Optional 又绕回 null */
    public static String badOrElseNull(Optional<String> opt) {
        return opt.orElse(null);
    }

    /** 正确：保持 Optional 链直到最终需要值 */
    public static String goodOrElseEmpty(Optional<String> opt) {
        return opt.orElse("");
    }

    // ── 反模式 5：Optional 代替条件判断（过度使用） ───────────────────

    /** 反模式：用 Optional 包装本来就不该为 null 的值 */
    public static String badOptionalOveruse(String name) {
        return Optional.ofNullable(name)
                .map(String::trim)
                .orElse("unknown");
    }

    /** 更简洁的做法：直接判空 */
    public static String goodSimpleNullCheck(String name) {
        if (name == null || name.isBlank()) {
            return "unknown";
        }
        return name.trim();
    }

    // ── 正确用法总结：链式操作 ────────────────────────────────────────

    /** 正确的链式 Optional 使用 */
    public static String extractNote(Optional<Order> order) {
        return order
                .map(Order::note)
                .filter(n -> !n.isBlank())
                .orElse("no note");
    }
}
