package com.javastudy.oop;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class ShapeTest {

    @Test
    void testCircleArea() {
        Circle circle = new Circle(5);
        assertEquals(Math.PI * 25, circle.area(), 0.01);
    }

    @Test
    void testRectangleArea() {
        Rectangle rect = new Rectangle(3, 4);
        assertEquals(12, rect.area());
    }

    @Test
    void testAbstractClassPolymorphism() {
        Shape shape = new Circle(1);
        assertEquals(Math.PI, shape.area(), 0.01);

        shape = new Rectangle(2, 3);
        assertEquals(6, shape.area());
    }

    @Test
    void testDescribe() {
        Shape shape = new Rectangle(3, 4);
        assertEquals("Area = 12.00", shape.describe());
    }
}
