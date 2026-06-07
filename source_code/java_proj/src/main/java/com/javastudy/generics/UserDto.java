package com.javastudy.generics;

import java.util.Objects;

/**
 * 知识点：Record (JDK 16+)
 * 不可变数据载体，自动生成构造器、accessor、equals、hashCode、toString
 * 紧凑构造器中进行验证
 */
public record UserDto(long id, String name) {
    /**
     * 紧凑构造器：参数验证
     */
    public UserDto {
        Objects.requireNonNull(name, "Name cannot be null");
        if (name.isBlank()) {
            throw new IllegalArgumentException("Name cannot be blank");
        }
    }

    /**
     * 自定义方法
     */
    public String displayName() {
        return "User[%d]: %s".formatted(id, name);
    }
}
