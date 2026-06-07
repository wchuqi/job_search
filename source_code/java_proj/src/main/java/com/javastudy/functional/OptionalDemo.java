package com.javastudy.functional;

import java.util.Optional;

/**
 * 知识点：Optional 用法
 *
 * Optional 是容器对象，可能包含或不包含非 null 值。
 * 用于优雅地替代 null 检查，避免 NullPointerException。
 *
 * 创建：
 *   Optional.of(value)      — value 不能为 null，否则 NPE
 *   Optional.empty()        — 空的 Optional
 *   Optional.ofNullable(v)  — v 可以为 null
 *
 * 取值与转换：
 *   get()          — 有值则返回，否则抛 NoSuchElementException
 *   orElse(other)  — 有值则返回，否则返回 other
 *   orElseGet(s)   — 有值则返回，否则调用 Supplier
 *   orElseThrow()  — 有值则返回，否则抛 NoSuchElementException
 *   orElseThrow(s) — 有值则返回，否则抛 Supplier 提供的异常
 *   map(fn)        — 有值则应用 fn，返回 Optional<R>
 *   flatMap(fn)    — 类似 map，但 fn 返回 Optional<R>（避免嵌套）
 *   ifPresent(fn)  — 有值则执行 fn
 *   filter(pred)   — 有值且满足条件则保留，否则 empty
 */
public class OptionalDemo {

    // ── 创建 ─────────────────────────────────────────────────────────

    /** Optional.of — 值不能为 null */
    public static Optional<String> createWithOf(String value) {
        return Optional.of(value);
    }

    /** Optional.empty */
    public static Optional<String> createEmpty() {
        return Optional.empty();
    }

    /** Optional.ofNullable — 值可以为 null */
    public static Optional<String> createWithOfNullable(String value) {
        return Optional.ofNullable(value);
    }

    // ── 取值 ─────────────────────────────────────────────────────────

    /** orElse — 提供默认值 */
    public static String getOrDefault(Optional<String> opt) {
        return opt.orElse("default");
    }

    /** orElseGet — 用 Supplier 延迟计算默认值 */
    public static String getOrComputeDefault(Optional<String> opt) {
        return opt.orElseGet(() -> "computed-" + System.nanoTime());
    }

    /** orElseThrow — 有值返回，否则抛异常 */
    public static String getOrThrow(Optional<String> opt) {
        return opt.orElseThrow(() -> new IllegalArgumentException("值不存在"));
    }

    // ── map / flatMap ────────────────────────────────────────────────

    /** map — 对值做变换 */
    public static Optional<Integer> mapToLength(Optional<String> opt) {
        return opt.map(String::length);
    }

    /** 链式 map */
    public static Optional<String> mapToUpperTrimmed(Optional<String> opt) {
        return opt.map(String::trim).map(String::toUpperCase);
    }

    /** flatMap — 避免 Optional 嵌套 */
    public static Optional<String> flatMapExample(Optional<String> opt) {
        // 假设 getValue 返回 Optional<String>
        return opt.flatMap(v -> Optional.of(v.toUpperCase()));
    }

    // ── filter ───────────────────────────────────────────────────────

    /** filter — 条件过滤 */
    public static Optional<String> filterNonEmpty(Optional<String> opt) {
        return opt.filter(s -> !s.isBlank());
    }

    // ── ifPresent / ifPresentOrElse (JDK 9+) ─────────────────────────

    /** ifPresent — 有值时执行副作用 */
    public static String ifPresentDemo(Optional<String> opt) {
        var sb = new StringBuilder();
        opt.ifPresent(v -> sb.append("found:").append(v));
        return sb.toString();
    }

    /** ifPresentOrElse (JDK 9+) */
    public static String ifPresentOrElseDemo(Optional<String> opt) {
        var sb = new StringBuilder();
        opt.ifPresentOrElse(
                v -> sb.append("value:").append(v),
                () -> sb.append("empty")
        );
        return sb.toString();
    }

    // ── 实际应用：安全地获取嵌套属性 ──────────────────────────────────

    public record Address(String city) {}
    public record User(String name, Address address) {}

    /** 安全地获取用户所在城市 */
    public static String getCityOrDefault(User user) {
        return Optional.ofNullable(user)
                .map(User::address)
                .map(Address::city)
                .orElse("unknown");
    }
}
