package com.javastudy.basics;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class ParameterPassingDemoTest {

    @Test
    void testIncrementPrimitiveNoEffect() {
        int value = 10;
        ParameterPassingDemo.incrementPrimitive(value);
        assertEquals(10, value); // 原值不变
    }

    @Test
    void testAddItemModifiesObject() {
        StringBuilder sb = new StringBuilder("hello");
        ParameterPassingDemo.addItem(sb, " world");
        assertEquals("hello world", sb.toString()); // 对象状态被修改
    }

    @Test
    void testReassignReferenceNoEffect() {
        StringBuilder sb = new StringBuilder("original");
        ParameterPassingDemo.reassignReference(sb);
        assertEquals("original", sb.toString()); // 引用重新赋值不影响外部
    }

    @Test
    void testUpdateNameModifiesObject() {
        ParameterPassingDemo.Person person = new ParameterPassingDemo.Person("Alice");
        ParameterPassingDemo.updateName(person, "Bob");
        assertEquals("Bob", person.getName());
    }

    @Test
    void testReassignPersonNoEffect() {
        ParameterPassingDemo.Person person = new ParameterPassingDemo.Person("Alice");
        ParameterPassingDemo.reassignPerson(person);
        assertEquals("Alice", person.getName()); // 重新赋值不影响外部
    }
}
