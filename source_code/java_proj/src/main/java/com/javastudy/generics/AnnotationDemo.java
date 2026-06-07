package com.javastudy.generics;

/**
 * 知识点：注解使用示例
 * 在方法上使用自定义 @Audited 注解
 */
public class AnnotationDemo {

    @Audited("创建订单")
    public String createOrder(String orderId) {
        return "Order created: " + orderId;
    }

    @Audited("删除订单")
    public void deleteOrder(String orderId) {
        // 删除操作
    }

    public String unannotatedMethod() {
        return "no audit";
    }
}
