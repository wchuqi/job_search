package com.javastudy.functional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.List;
import java.util.Map;
import static org.junit.jupiter.api.Assertions.*;

class OrderProcessingExerciseTest {

    private List<OrderProcessingExercise.Order> orders;

    @BeforeEach
    void setUp() {
        orders = List.of(
                new OrderProcessingExercise.Order("O1", "user1", true, List.of(
                        new OrderProcessingExercise.OrderItem("Laptop", 1, 5000.0),
                        new OrderProcessingExercise.OrderItem("Mouse", 2, 50.0)
                )),
                new OrderProcessingExercise.Order("O2", "user1", true, List.of(
                        new OrderProcessingExercise.OrderItem("Keyboard", 1, 200.0)
                )),
                new OrderProcessingExercise.Order("O3", "user2", false, List.of(
                        new OrderProcessingExercise.OrderItem("Monitor", 1, 1500.0)
                )),
                new OrderProcessingExercise.Order("O4", "user2", true, List.of(
                        new OrderProcessingExercise.OrderItem("Laptop", 1, 5500.0),
                        new OrderProcessingExercise.OrderItem("USB Cable", 3, 10.0)
                )),
                new OrderProcessingExercise.Order("O5", "user3", true, List.of(
                        new OrderProcessingExercise.OrderItem("Mouse", 5, 50.0)
                ))
        );
    }

    // ── 1. 过滤已支付 ────────────────────────────────────────────────

    @Test
    void filterPaidOrdersReturnsOnlyPaid() {
        var paid = OrderProcessingExercise.filterPaidOrders(orders);
        assertEquals(4, paid.size());
        assertTrue(paid.stream().allMatch(OrderProcessingExercise.Order::paid));
    }

    @Test
    void filterPaidOrdersExcludesUnpaid() {
        var paid = OrderProcessingExercise.filterPaidOrders(orders);
        assertFalse(paid.stream().anyMatch(o -> o.orderId().equals("O3")));
    }

    // ── 2. 按用户分组 ────────────────────────────────────────────────

    @Test
    void groupByUserReturnsThreeGroups() {
        var groups = OrderProcessingExercise.groupByUser(orders);
        assertEquals(3, groups.size());
        assertEquals(2, groups.get("user1").size());
        assertEquals(2, groups.get("user2").size());
        assertEquals(1, groups.get("user3").size());
    }

    @Test
    void groupPaidByUserExcludesUnpaidOrders() {
        var groups = OrderProcessingExercise.groupPaidByUser(orders);
        // user2 的 O3 未支付，只有 O4
        assertEquals(1, groups.get("user2").size());
        assertEquals("O4", groups.get("user2").get(0).orderId());
    }

    // ── 3. 计算每个用户的订单总额 ────────────────────────────────────

    @Test
    void totalAmountByUserComputesCorrectAmounts() {
        var totals = OrderProcessingExercise.totalAmountByUser(orders);
        // user1: (5000 + 100) + 200 = 5300
        assertEquals(5300.0, totals.get("user1"), 0.01);
        // user2: 5500 + 30 = 5530
        assertEquals(5530.0, totals.get("user2"), 0.01);
        // user3: 250
        assertEquals(250.0, totals.get("user3"), 0.01);
    }

    // ── 4. 找到消费最高的用户 ────────────────────────────────────────

    @Test
    void findTopSpenderReturnsUser2() {
        var top = OrderProcessingExercise.findTopSpender(orders);
        assertTrue(top.isPresent());
        assertEquals("user2", top.orElseThrow());
    }

    @Test
    void findTopSpenderWithAmountReturnsCorrectEntry() {
        var entry = OrderProcessingExercise.findTopSpenderWithAmount(orders);
        assertTrue(entry.isPresent());
        assertEquals("user2", entry.orElseThrow().getKey());
        assertEquals(5530.0, entry.orElseThrow().getValue(), 0.01);
    }

    @Test
    void findTopSpenderEmptyListReturnsEmpty() {
        var top = OrderProcessingExercise.findTopSpender(List.of());
        assertTrue(top.isEmpty());
    }

    // ── 5. 获取所有已支付订单的商品名称（去重） ──────────────────────

    @Test
    void uniqueProductNamesReturnsDistinctSorted() {
        var names = OrderProcessingExercise.uniqueProductNames(orders);
        assertEquals(List.of("Keyboard", "Laptop", "Mouse", "USB Cable"), names);
    }

    @Test
    void uniqueProductNamesExcludesUnpaidOrderItems() {
        var names = OrderProcessingExercise.uniqueProductNames(orders);
        // "Monitor" 只在未支付订单 O3 中
        assertFalse(names.contains("Monitor"));
    }

    // ── 综合查询 ─────────────────────────────────────────────────────

    @Test
    void orderCountByUserCountsPaidOrders() {
        var counts = OrderProcessingExercise.orderCountByUser(orders);
        assertEquals(2L, counts.get("user1"));
        assertEquals(1L, counts.get("user2")); // O3 未支付，不算
        assertEquals(1L, counts.get("user3"));
    }

    @Test
    void totalItemsSoldCountsAllPaidItems() {
        // user1: 1+2+1=4, user2: 1+3=4, user3: 5 => total=13
        assertEquals(13, OrderProcessingExercise.totalItemsSold(orders));
    }

    @Test
    void revenueByProductComputesPerProductRevenue() {
        var revenue = OrderProcessingExercise.revenueByProduct(orders);
        // Laptop: 5000 + 5500 = 10500
        assertEquals(10500.0, revenue.get("Laptop"), 0.01);
        // Mouse: 100 + 250 = 350
        assertEquals(350.0, revenue.get("Mouse"), 0.01);
        // Keyboard: 200
        assertEquals(200.0, revenue.get("Keyboard"), 0.01);
        // USB Cable: 30
        assertEquals(30.0, revenue.get("USB Cable"), 0.01);
    }
}
