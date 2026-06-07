package com.javastudy.functional;

import java.util.List;
import java.util.function.*;

/**
 * 知识点：方法引用（Method Reference）
 *
 * 方法引用是 lambda 的简写形式，当 lambda 体只是调用一个已有方法时使用。
 *
 * 四种形式：
 * 1. 静态方法引用        ClassName::staticMethod      等价于 (args) -> ClassName.staticMethod(args)
 * 2. 实例方法引用(对象)   object::instanceMethod       等价于 (args) -> object.instanceMethod(args)
 * 3. 实例方法引用(类)     ClassName::instanceMethod    等价于 (obj, args) -> obj.instanceMethod(args)
 * 4. 构造方法引用         ClassName::new               等价于 (args) -> new ClassName(args)
 */
public class MethodReferenceDemo {

    // 辅助类
    public static class Person {
        private final String name;
        private final int age;

        public Person(String name, int age) {
            this.name = name;
            this.age = age;
        }

        public String getName() { return name; }
        public int getAge() { return age; }

        public static Person create(String name, int age) {
            return new Person(name, age);
        }

        public boolean isAdult() {
            return age >= 18;
        }

        @Override
        public String toString() {
            return name + "(" + age + ")";
        }
    }

    // ── 1. 静态方法引用  ClassName::staticMethod ──────────────────────

    /** Integer.parseInt — 静态方法引用 */
    public static List<Integer> parseNumbers(List<String> strings) {
        Function<String, Integer> parser = Integer::parseInt;
        return strings.stream().map(parser).toList();
    }

    /** Person.create — 静态方法引用 */
    public static Person createPerson(String name, int age) {
        BiFunction<String, Integer, Person> factory = Person::create;
        return factory.apply(name, age);
    }

    // ── 2. 实例方法引用（已有对象） object::instanceMethod ────────────

    /** System.out::println — 已有对象的实例方法 */
    public static String captureOutput(List<String> items) {
        var sb = new StringBuilder();
        Consumer<String> appender = sb::append; // 等价于 s -> sb.append(s)
        items.forEach(appender);
        return sb.toString();
    }

    /** String::length 不适用此形式，这里用已有对象演示 */
    public static int getStringLength(String s) {
        // obj::method 形式 — obj 是已知实例
        java.util.function.Supplier<Integer> lenSupplier = s::length;
        return lenSupplier.get();
    }

    // ── 3. 实例方法引用（通过类名） ClassName::instanceMethod ─────────

    /** String::toUpperCase — 第一个参数作为调用者 */
    public static List<String> toUpperCase(List<String> items) {
        Function<String, String> transformer = String::toUpperCase;
        // 等价于 (String s) -> s.toUpperCase()
        return items.stream().map(transformer).toList();
    }

    /** String::compareTo — 第一个参数是调用者，第二个是参数 */
    public static int compareStrings(String a, String b) {
        BiFunction<String, String, Integer> comparator = String::compareTo;
        return comparator.apply(a, b);
    }

    /** String::isEmpty */
    public static List<String> filterNonEmpty(List<String> items) {
        Predicate<String> isEmpty = String::isEmpty;
        return items.stream().filter(isEmpty.negate()).toList();
    }

    // ── 4. 构造方法引用 ClassName::new ────────────────────────────────

    /** 无参构造 */
    public static StringBuilder createStringBuilder() {
        Supplier<StringBuilder> factory = StringBuilder::new;
        return factory.get();
    }

    /** 单参构造 */
    public static Person createFromName(String name) {
        // 这里演示单参构造；由于 Person 构造器是双参，用 Lambda 包装
        Function<String, Person> factory = n -> new Person(n, 0);
        return factory.apply(name);
    }

    /** 双参构造 */
    public static Person createFromNameAndAge(String name, int age) {
        BiFunction<String, Integer, Person> factory = Person::new;
        return factory.apply(name, age);
    }

    /** 用构造方法引用批量创建对象 */
    public static List<Person> createPeople(List<String> names) {
        Function<String, Person> factory = n -> new Person(n, 0);
        return names.stream().map(factory).toList();
    }
}
