package com.javastudy.functional;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 综合练习：订单处理
 *
 * 给定订单列表，完成以下操作：
 * 1. 过滤出已支付的订单
 * 2. 按用户分组
 * 3. 计算每个用户的订单总额
 * 4. 找到消费最高的用户
 * 5. 获取所有已支付订单的商品名称列表（去重）
 */
public class OrderProcessingExercise {

    // ── 数据模型 ─────────────────────────────────────────────────────

    public record OrderItem(String productName, int quantity, double unitPrice) {
        public double subtotal() {
            return quantity * unitPrice;
        }
    }

    public record Order(
            String orderId,
            String userId,
            boolean paid,
            List<OrderItem> items
    ) {
        public double totalAmount() {
            return items.stream()
                    .mapToDouble(OrderItem::subtotal)
                    .sum();
        }
    }

    // ── 1. 过滤已支付订单 ────────────────────────────────────────────

    /** 过滤出已支付的订单 */
    public static List<Order> filterPaidOrders(List<Order> orders) {
        return orders.stream()
                .filter(Order::paid)
                .toList();
    }

    // ── 2. 按用户分组 ────────────────────────────────────────────────

    /** 按用户 ID 分组 */
    public static Map<String, List<Order>> groupByUser(List<Order> orders) {
        return orders.stream()
                .collect(Collectors.groupingBy(Order::userId));
    }

    /** 按用户分组（只包含已支付订单） */
    public static Map<String, List<Order>> groupPaidByUser(List<Order> orders) {
        return orders.stream()
                .filter(Order::paid)
                .collect(Collectors.groupingBy(Order::userId));
    }

    // ── 3. 计算每个用户的订单总额 ────────────────────────────────────

    /** 每个用户的已支付订单总额 */
    public static Map<String, Double> totalAmountByUser(List<Order> orders) {
        return orders.stream()
                .filter(Order::paid)
                .collect(Collectors.groupingBy(
                        Order::userId,
                        Collectors.summingDouble(Order::totalAmount)
                ));
    }

    // ── 4. 找到消费最高的用户 ────────────────────────────────────────

    /** 找到消费最高的用户 ID */
    public static Optional<String> findTopSpender(List<Order> orders) {
        return totalAmountByUser(orders).entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey);
    }

    /** 找到消费最高的用户（返回 userId + amount） */
    public static Optional<Map.Entry<String, Double>> findTopSpenderWithAmount(List<Order> orders) {
        return totalAmountByUser(orders).entrySet().stream()
                .max(Map.Entry.comparingByValue());
    }

    // ── 5. 获取所有已支付订单的商品名称列表（去重） ──────────────────

    /** 所有已支付订单中的商品名称（去重、排序） */
    public static List<String> uniqueProductNames(List<Order> orders) {
        return orders.stream()
                .filter(Order::paid)
                .flatMap(order -> order.items().stream())
                .map(OrderItem::productName)
                .distinct()
                .sorted()
                .toList();
    }

    // ── 综合查询 ─────────────────────────────────────────────────────

    /** 每个用户的已支付订单数量 */
    public static Map<String, Long> orderCountByUser(List<Order> orders) {
        return orders.stream()
                .filter(Order::paid)
                .collect(Collectors.groupingBy(Order::userId, Collectors.counting()));
    }

    /** 已支付订单中的总商品数量 */
    public static int totalItemsSold(List<Order> orders) {
        return orders.stream()
                .filter(Order::paid)
                .flatMap(order -> order.items().stream())
                .mapToInt(OrderItem::quantity)
                .sum();
    }

    /** 每个商品的销售总额（按商品名分组） */
    public static Map<String, Double> revenueByProduct(List<Order> orders) {
        return orders.stream()
                .filter(Order::paid)
                .flatMap(order -> order.items().stream())
                .collect(Collectors.groupingBy(
                        OrderItem::productName,
                        Collectors.summingDouble(OrderItem::subtotal)
                ));
    }
}
