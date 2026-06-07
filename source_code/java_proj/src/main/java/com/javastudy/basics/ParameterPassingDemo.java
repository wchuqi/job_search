package com.javastudy.basics;

/**
 * 知识点：参数传递 (Java始终是值传递)
 * 基本类型传值副本，引用类型传引用副本
 * 方法内可修改对象状态，但不能重新赋值调用者的引用
 */
public class ParameterPassingDemo {

    /**
     * 基本类型传值：方法内修改不影响外部
     */
    public static void incrementPrimitive(int value) {
        value++; // 只修改副本
    }

    /**
     * 引用类型传引用副本：方法内可修改对象状态
     */
    public static void addItem(StringBuilder sb, String item) {
        sb.append(item); // 修改了对象的状态
    }

    /**
     * 引用类型：重新赋值引用不影响外部
     */
    public static void reassignReference(StringBuilder sb) {
        sb = new StringBuilder("new"); // 只修改了局部引用副本
    }

    /**
     * 自定义对象：修改字段值影响外部
     */
    public static void updateName(Person person, String newName) {
        person.setName(newName); // 修改了对象状态
    }

    /**
     * 自定义对象：重新赋值不影响外部
     */
    public static void reassignPerson(Person person) {
        person = new Person("reassigned"); // 只修改了局部引用副本
    }

    public static class Person {
        private String name;

        public Person(String name) {
            this.name = name;
        }

        public String getName() { return name; }
        public void setName(String name) { this.name = name; }
    }
}
