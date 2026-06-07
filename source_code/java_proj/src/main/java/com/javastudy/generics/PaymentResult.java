package com.javastudy.generics;

/**
 * 知识点：密封接口 (Sealed Interface, JDK 17+)
 * 限制哪些类可以实现该接口
 */
public sealed interface PaymentResult permits PaymentResult.PaymentSuccess, PaymentResult.PaymentFailure {

    record PaymentSuccess(String transactionId, double amount) implements PaymentResult {}

    record PaymentFailure(String errorCode, String reason) implements PaymentResult {}

    /**
     * 使用密封类型的模式匹配
     */
    static String describe(PaymentResult result) {
        return switch (result) {
            case PaymentSuccess success -> "Success: %s, amount=%.2f"
                .formatted(success.transactionId(), success.amount());
            case PaymentFailure failure -> "Failed: [%s] %s"
                .formatted(failure.errorCode(), failure.reason());
        };
    }
}
