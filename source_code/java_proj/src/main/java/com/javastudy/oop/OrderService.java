package com.javastudy.oop;

/**
 * 知识点：组合优于继承 (Composition over Inheritance)
 * 通过构造器注入协作者，而不是继承
 */
public class OrderService {
    private final PaymentClient paymentClient;

    /**
     * 构造器注入：组合 PaymentClient
     */
    public OrderService(PaymentClient paymentClient) {
        this.paymentClient = paymentClient;
    }

    public String placeOrder(String orderId, double amount) {
        boolean success = paymentClient.processPayment(amount);
        if (success) {
            return "Order %s placed successfully".formatted(orderId);
        } else {
            return "Order %s failed: payment declined".formatted(orderId);
        }
    }

    /**
     * 支付客户端接口
     */
    public interface PaymentClient {
        boolean processPayment(double amount);
    }
}
