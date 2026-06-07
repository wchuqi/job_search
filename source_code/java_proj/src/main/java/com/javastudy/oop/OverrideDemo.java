package com.javastudy.oop;

import java.util.Objects;

/**
 * 知识点：方法重写 (Overriding)
 * 重写 toString(), equals(), hashCode()
 */
public class OverrideDemo {
    private final String name;
    private final int value;

    public OverrideDemo(String name, int value) {
        this.name = name;
        this.value = value;
    }

    /**
     * 重写 toString()
     */
    @Override
    public String toString() {
        return "OverrideDemo{name='%s', value=%d}".formatted(name, value);
    }

    /**
     * 重写 equals()
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof OverrideDemo that)) return false;
        return value == that.value && Objects.equals(name, that.name);
    }

    /**
     * 重写 hashCode() - 必须与equals一致
     */
    @Override
    public int hashCode() {
        return Objects.hash(name, value);
    }

    public String getName() { return name; }
    public int getValue() { return value; }
}
