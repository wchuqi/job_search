package com.javastudy.generics;

/**
 * 知识点：枚举基础 (Enum)
 * values(), valueOf(), ordinal()
 */
public enum OrderStatus {
    CREATED,
    PAID,
    SHIPPED,
    DELIVERED,
    CANCELLED;

    /**
     * 判断是否是终态
     */
    public boolean isTerminal() {
        return this == DELIVERED || this == CANCELLED;
    }

    /**
     * 获取中文描述
     */
    public String getDescription() {
        return switch (this) {
            case CREATED -> "已创建";
            case PAID -> "已支付";
            case SHIPPED -> "已发货";
            case DELIVERED -> "已送达";
            case CANCELLED -> "已取消";
        };
    }
}
