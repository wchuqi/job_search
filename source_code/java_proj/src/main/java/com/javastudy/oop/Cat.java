package com.javastudy.oop;

/**
 * 知识点：多态 - 另一个Animal子类
 */
public class Cat extends Animal {
    public Cat(String name) {
        super(name);
    }

    @Override
    public String speak() {
        return name + " meows";
    }
}
