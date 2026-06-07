package com.javastudy.oop;

import java.util.Objects;

/**
 * 知识点：构造器验证和初始化顺序
 * 构造器中进行参数校验，拒绝null/blank name和负数price
 */
public class Product {
    private final String name;
    private final double price;

    public Product(String name, double price) {
        Objects.requireNonNull(name, "Product name cannot be null");
        if (name.isBlank()) {
            throw new IllegalArgumentException("Product name cannot be blank");
        }
        if (price < 0) {
            throw new IllegalArgumentException("Price cannot be negative");
        }
        this.name = name;
        this.price = price;
    }

    public String getName() { return name; }
    public double getPrice() { return price; }

    @Override
    public String toString() {
        return "Product{name='%s', price=%.2f}".formatted(name, price);
    }
}
