package com.javastudy.stringdatetime;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class OrderExportExerciseTest {

    private List<OrderExportExercise.OrderItem> sampleItems() {
        return List.of(
                new OrderExportExercise.OrderItem("Laptop", 1, new BigDecimal("999.99")),
                new OrderExportExercise.OrderItem("Mouse", 2, new BigDecimal("29.99")),
                new OrderExportExercise.OrderItem("USB Cable", 5, new BigDecimal("9.99"))
        );
    }

    @Test
    void createOrderGeneratesUUID() {
        OrderExportExercise.Order order = OrderExportExercise.createOrder(
                "Alice", "alice@example.com", sampleItems(), ZoneId.of("Asia/Shanghai"));
        assertNotNull(order.orderId());
        // UUID format: 8-4-4-4-12
        assertEquals(36, order.orderId().length());
    }

    @Test
    void createOrderSetsCustomerInfo() {
        OrderExportExercise.Order order = OrderExportExercise.createOrder(
                "Bob", "bob@example.com", sampleItems(), ZoneId.of("UTC"));
        assertEquals("Bob", order.customerName());
        assertEquals("bob@example.com", order.customerEmail());
    }

    @Test
    void createOrderSetsTimezone() {
        ZoneId shanghai = ZoneId.of("Asia/Shanghai");
        OrderExportExercise.Order order = OrderExportExercise.createOrder(
                "Alice", "alice@example.com", sampleItems(), shanghai);
        assertEquals(shanghai, order.orderTime().getZone());
    }

    @Test
    void totalAmountCalculatesCorrectly() {
        // Laptop: 999.99, Mouse: 2 * 29.99 = 59.98, USB: 5 * 9.99 = 49.95
        // Total: 999.99 + 59.98 + 49.95 = 1109.92
        OrderExportExercise.Order order = OrderExportExercise.createOrder(
                "Alice", "alice@example.com", sampleItems(), ZoneId.of("UTC"));
        BigDecimal total = order.totalAmount();
        assertEquals(new BigDecimal("1109.92"), total);
    }

    @Test
    void totalWithTaxCalculatesCorrectly() {
        OrderExportExercise.Order order = OrderExportExercise.createOrder(
                "Alice", "alice@example.com", sampleItems(), ZoneId.of("UTC"));
        BigDecimal taxRate = new BigDecimal("0.08"); // 8% tax
        BigDecimal totalWithTax = order.totalWithTax(taxRate);
        // 1109.92 * 0.08 = 88.79 (tax), 1109.92 + 88.79 = 1198.71
        BigDecimal expectedTax = new BigDecimal("1109.92").multiply(new BigDecimal("0.08"))
                .setScale(2, RoundingMode.HALF_UP);
        BigDecimal expected = new BigDecimal("1109.92").add(expectedTax);
        assertEquals(expected, totalWithTax);
    }

    @Test
    void subtotalCalculatesCorrectly() {
        OrderExportExercise.OrderItem item = new OrderExportExercise.OrderItem(
                "Widget", 3, new BigDecimal("15.50"));
        assertEquals(new BigDecimal("46.50"), item.subtotal());
    }

    @Test
    void exportAsJsonContainsOrderInfo() {
        OrderExportExercise.Order order = OrderExportExercise.createOrder(
                "Alice", "alice@example.com", sampleItems(), ZoneId.of("Asia/Shanghai"));
        String json = OrderExportExercise.exportAsJson(order);

        assertTrue(json.contains("\"orderId\""));
        assertTrue(json.contains(order.orderId()));
        assertTrue(json.contains("\"name\": \"Alice\""));
        assertTrue(json.contains("\"email\": \"alice@example.com\""));
        assertTrue(json.contains("\"Laptop\""));
        assertTrue(json.contains("\"Mouse\""));
        assertTrue(json.contains("\"USB Cable\""));
        assertTrue(json.contains("\"totalAmount\""));
    }

    @Test
    void exportAsJsonIsValidStructure() {
        OrderExportExercise.Order order = OrderExportExercise.createOrder(
                "Test", "test@test.com", sampleItems(), ZoneId.of("UTC"));
        String json = OrderExportExercise.exportAsJson(order);

        assertTrue(json.trim().startsWith("{"), "JSON should start with {");
        assertTrue(json.trim().endsWith("}"), "JSON should end with }");
        assertTrue(json.contains("["), "JSON should contain array for items");
        assertTrue(json.contains("]"), "JSON should contain closing array bracket");
    }

    @Test
    void exportReceiptContainsOrderInfo() {
        OrderExportExercise.Order order = OrderExportExercise.createOrder(
                "Alice", "alice@example.com", sampleItems(), ZoneId.of("UTC"));
        String receipt = OrderExportExercise.exportReceipt(order);

        assertTrue(receipt.contains("ORDER RECEIPT"));
        assertTrue(receipt.contains(order.orderId()));
        assertTrue(receipt.contains("Alice"));
        assertTrue(receipt.contains("alice@example.com"));
        assertTrue(receipt.contains("Laptop"));
        assertTrue(receipt.contains("TOTAL"));
    }

    @Test
    void calculateStatsReturnsCorrectValues() {
        OrderExportExercise.Order order = OrderExportExercise.createOrder(
                "Alice", "alice@example.com", sampleItems(), ZoneId.of("UTC"));
        OrderExportExercise.OrderStats stats = OrderExportExercise.calculateStats(order);

        assertEquals(new BigDecimal("1109.92"), stats.totalAmount());
        assertEquals(3, stats.distinctItems());
        assertEquals(8, stats.totalQuantity()); // 1 + 2 + 5
        // averageUnitPrice = 1109.92 / 8 = 138.74
        assertEquals(new BigDecimal("138.74"), stats.averageUnitPrice());
    }

    @Test
    void formatOrderTimeInDifferentZone() {
        ZoneId shanghai = ZoneId.of("Asia/Shanghai");
        OrderExportExercise.Order order = OrderExportExercise.createOrder(
                "Alice", "alice@example.com", sampleItems(), shanghai);

        String tokyoTime = OrderExportExercise.formatOrderTimeInZone(order, ZoneId.of("Asia/Tokyo"));
        assertTrue(tokyoTime.contains("Asia/Tokyo") || tokyoTime.contains("JST")
                        || tokyoTime.contains("+09"),
                "Tokyo time should reference Tokyo timezone: " + tokyoTime);
    }

    @Test
    void emptyItemsOrderHasZeroTotal() {
        OrderExportExercise.Order order = OrderExportExercise.createOrder(
                "Empty", "empty@test.com", List.of(), ZoneId.of("UTC"));
        assertEquals(0, order.totalAmount().signum());
    }

    @Test
    void orderItemWithZeroQuantity() {
        OrderExportExercise.OrderItem item = new OrderExportExercise.OrderItem(
                "Free", 0, new BigDecimal("10.00"));
        assertEquals(0, item.subtotal().signum());
    }
}
