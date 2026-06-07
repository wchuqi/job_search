package com.javastudy.oop;

/**
 * 知识点：抽象类的另一个实现
 */
public class Rectangle extends Shape {
    private final double width;
    private final double height;

    public Rectangle(double width, double height) {
        if (width < 0 || height < 0) throw new IllegalArgumentException("Dimensions cannot be negative");
        this.width = width;
        this.height = height;
    }

    @Override
    public double area() {
        return width * height;
    }

    public double getWidth() { return width; }
    public double getHeight() { return height; }
}
