package com.javastudy.oop;

/**
 * 知识点：接口 + 默认方法
 * 抽象方法 pay() + 默认方法 supportsRefund()
 */
public interface Payable {
    double amount();

    void pay();

    /**
     * 默认方法：子类可以不重写
     */
    default boolean supportsRefund() {
        return true;
    }
}
