package com.javastudy.oop;

/**
 * 知识点：抽象类
 * 抽象方法 area() 由子类实现
 */
public abstract class Shape {
    public abstract double area();

    /**
     * 模板方法：基于area()计算
     */
    public String describe() {
        return "Area = %.2f".formatted(area());
    }
}
