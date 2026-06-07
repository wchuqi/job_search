package com.javastudy.oop;

/**
 * 知识点：继承 (Inheritance)
 * 基类 Animal，子类 Dog 重写 speak()
 */
public class Animal {
    protected final String name;

    public Animal(String name) {
        this.name = name;
    }

    public String speak() {
        return name + " makes a sound";
    }

    public String getName() {
        return name;
    }
}
