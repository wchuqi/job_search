package com.javastudy.oop;

/**
 * 知识点：抽象类的具体实现
 */
public class Circle extends Shape {
    private final double radius;

    public Circle(double radius) {
        if (radius < 0) throw new IllegalArgumentException("Radius cannot be negative");
        this.radius = radius;
    }

    @Override
    public double area() {
        return Math.PI * radius * radius;
    }

    public double getRadius() { return radius; }
}
