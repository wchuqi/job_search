package com.javastudy.functional;

import org.junit.jupiter.api.Test;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

class MethodReferenceDemoTest {

    // ── 静态方法引用 ─────────────────────────────────────────────────

    @Test
    void parseNumbersConvertsStringsToIntegers() {
        var result = MethodReferenceDemo.parseNumbers(List.of("1", "2", "3"));
        assertEquals(List.of(1, 2, 3), result);
    }

    @Test
    void createPersonUsesStaticMethodReference() {
        var person = MethodReferenceDemo.createPerson("Alice", 30);
        assertEquals("Alice", person.getName());
        assertEquals(30, person.getAge());
    }

    // ── 实例方法引用（已有对象） ─────────────────────────────────────

    @Test
    void captureOutputAppendsAllItems() {
        var result = MethodReferenceDemo.captureOutput(List.of("a", "b", "c"));
        assertEquals("abc", result);
    }

    @Test
    void getStringLengthReturnsLength() {
        assertEquals(5, MethodReferenceDemo.getStringLength("hello"));
        assertEquals(0, MethodReferenceDemo.getStringLength(""));
    }

    // ── 实例方法引用（通过类名） ─────────────────────────────────────

    @Test
    void toUpperCaseConvertsAllItems() {
        var result = MethodReferenceDemo.toUpperCase(List.of("a", "b", "c"));
        assertEquals(List.of("A", "B", "C"), result);
    }

    @Test
    void compareStringsComparesLexicographically() {
        assertTrue(MethodReferenceDemo.compareStrings("a", "b") < 0);
        assertEquals(0, MethodReferenceDemo.compareStrings("a", "a"));
        assertTrue(MethodReferenceDemo.compareStrings("b", "a") > 0);
    }

    @Test
    void filterNonEmptyRemovesEmptyStrings() {
        var result = MethodReferenceDemo.filterNonEmpty(List.of("a", "", "b", ""));
        assertEquals(List.of("a", "b"), result);
    }

    // ── 构造方法引用 ─────────────────────────────────────────────────

    @Test
    void createStringBuilderCreatesEmptyBuilder() {
        var sb = MethodReferenceDemo.createStringBuilder();
        assertNotNull(sb);
        assertEquals(0, sb.length());
    }

    @Test
    void createFromNameCreatesPersonWithZeroAge() {
        var person = MethodReferenceDemo.createFromName("Bob");
        assertEquals("Bob", person.getName());
        assertEquals(0, person.getAge());
    }

    @Test
    void createFromNameAndAgeUsesConstructorRef() {
        var person = MethodReferenceDemo.createFromNameAndAge("Alice", 25);
        assertEquals("Alice", person.getName());
        assertEquals(25, person.getAge());
    }

    @Test
    void createPeopleBatchCreatesPersons() {
        var people = MethodReferenceDemo.createPeople(List.of("A", "B", "C"));
        assertEquals(3, people.size());
        assertEquals("A", people.get(0).getName());
        assertEquals("B", people.get(1).getName());
        assertEquals("C", people.get(2).getName());
    }
}
