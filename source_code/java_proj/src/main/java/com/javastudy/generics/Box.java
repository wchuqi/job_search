package com.javastudy.generics;

/**
 * 知识点：泛型类 (Generic Class)
 * Box<T> 持有任意类型的值
 */
public class Box<T> {
    private T value;

    public Box(T value) {
        this.value = value;
    }

    public T getValue() { return value; }

    public void setValue(T value) { this.value = value; }

    @Override
    public String toString() {
        return "Box{value=%s}".formatted(value);
    }
}
