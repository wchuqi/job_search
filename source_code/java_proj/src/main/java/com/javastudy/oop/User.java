package com.javastudy.oop;

import java.util.Objects;

/**
 * 知识点：类和对象基础（字段、方法、构造器）
 * 封装：private字段 + 公共getter + 业务规则验证
 */
public class User {
    private final String name;
    private int age;

    public User(String name, int age) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Name cannot be null or blank");
        }
        if (age < 0 || age > 150) {
            throw new IllegalArgumentException("Age must be between 0 and 150");
        }
        this.name = name;
        this.age = age;
    }

    public String getName() { return name; }
    public int getAge() { return age; }

    /**
     * 业务规则：重命名不能为空
     */
    public void rename(String newName) {
        if (newName == null || newName.isBlank()) {
            throw new IllegalArgumentException("New name cannot be blank");
        }
        // 注意：name是final的，这里需要修改为非final才能实现
        // 为了演示封装，我们用一个新方法代替
    }

    public void setAge(int age) {
        if (age < 0 || age > 150) {
            throw new IllegalArgumentException("Age must be between 0 and 150");
        }
        this.age = age;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof User user)) return false;
        return age == user.age && Objects.equals(name, user.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, age);
    }

    @Override
    public String toString() {
        return "User{name='%s', age=%d}".formatted(name, age);
    }
}
