package com.javastudy.oop;

import java.util.Objects;

/**
 * 知识点：equals 和 hashCode 契约
 * 正确实现 equals/hashCode 以支持 HashMap/HashSet
 */
public class UserId {
    private final long id;

    public UserId(long id) {
        this.id = id;
    }

    public long getId() { return id; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof UserId userId)) return false;
        return id == userId.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "UserId{id=%d}".formatted(id);
    }
}
