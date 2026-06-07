package com.javastudy.oop;

/**
 * 知识点：继承 + 重写
 * Dog 继承 Animal，重写 speak()
 */
public class Dog extends Animal {
    public Dog(String name) {
        super(name);
    }

    @Override
    public String speak() {
        return name + " barks";
    }
}
