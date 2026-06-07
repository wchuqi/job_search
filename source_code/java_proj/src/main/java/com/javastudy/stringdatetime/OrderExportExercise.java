package com.javastudy.stringdatetime;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.UUID;

/**
 * Exercise: Combine text blocks, DateTimeFormatter, BigDecimal, and UUID
 * to build an order export system.
 * <p>
 * This demonstrates how multiple java.time, String, and BigDecimal concepts
 * work together in a realistic business scenario:
 * <ul>
 *   <li>Generate unique order IDs with UUID</li>
 *   <li>Format dates with DateTimeFormatter</li>
 *   <li>Calculate totals with BigDecimal arithmetic</li>
 *   <li>Export orders as JSON using text blocks</li>
 * </ul>
 */
public class OrderExportExercise {

    public record OrderItem(String productName, int quantity, BigDecimal unitPrice) {
        public BigDecimal subtotal() {
            return unitPrice.multiply(BigDecimal.valueOf(quantity));
        }
    }

    public record Order(
            String orderId,
            String customerName,
            String customerEmail,
            List<OrderItem> items,
            ZonedDateTime orderTime
    ) {
        public BigDecimal totalAmount() {
            return items.stream()
                    .map(OrderItem::subtotal)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);
        }

        public BigDecimal totalWithTax(BigDecimal taxRate) {
            BigDecimal total = totalAmount();
            BigDecimal tax = total.multiply(taxRate).setScale(2, RoundingMode.HALF_UP);
            return total.add(tax);
        }
    }

    private static final DateTimeFormatter DATE_FMT =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss z");

    private static final DateTimeFormatter ISO_FMT =
            DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");

    /**
     * Create a new order with a generated UUID.
     */
    public static Order createOrder(String customerName, String customerEmail,
                                     List<OrderItem> items, ZoneId zone) {
        String orderId = UUID.randomUUID().toString();
        ZonedDateTime orderTime = ZonedDateTime.now(zone);
        return new Order(orderId, customerName, customerEmail, items, orderTime);
    }

    /**
     * Export an order as a JSON string using text blocks.
     */
    public static String exportAsJson(Order order) {
        StringBuilder itemsJson = new StringBuilder();
        for (int i = 0; i < order.items().size(); i++) {
            OrderItem item = order.items().get(i);
            if (i > 0) itemsJson.append(",\n        ");
            itemsJson.append("""
                    {
                          "product": "%s",
                          "quantity": %d,
                          "unitPrice": %s,
                          "subtotal": %s
                        }""".formatted(
                    item.productName(),
                    item.quantity(),
                    item.unitPrice().toPlainString(),
                    item.subtotal().toPlainString()));
        }

        return """
                {
                  "orderId": "%s",
                  "customer": {
                    "name": "%s",
                    "email": "%s"
                  },
                  "orderTime": "%s",
                  "items": [
                    %s
                  ],
                  "totalAmount": %s
                }
                """.formatted(
                order.orderId(),
                order.customerName(),
                order.customerEmail(),
                order.orderTime().format(DATE_FMT),
                itemsJson.toString(),
                order.totalAmount().toPlainString());
    }

    /**
     * Export a simplified receipt using text blocks and formatting.
     */
    public static String exportReceipt(Order order) {
        StringBuilder lineItems = new StringBuilder();
        for (OrderItem item : order.items()) {
            lineItems.append(String.format("  %-20s %3d x %8s = %10s%n",
                    item.productName(),
                    item.quantity(),
                    item.unitPrice().setScale(2, RoundingMode.HALF_UP).toPlainString(),
                    item.subtotal().setScale(2, RoundingMode.HALF_UP).toPlainString()));
        }

        return """
                ============================
                ORDER RECEIPT
                ============================
                Order ID: %s
                Customer: %s <%s>
                Date:     %s
                ----------------------------
                Items:
                %s----------------------------
                TOTAL:    %s
                ============================
                """.formatted(
                order.orderId(),
                order.customerName(),
                order.customerEmail(),
                order.orderTime().format(DATE_FMT),
                lineItems.toString(),
                order.totalAmount().setScale(2, RoundingMode.HALF_UP).toPlainString());
    }

    /**
     * Calculate order statistics.
     */
    public static OrderStats calculateStats(Order order) {
        BigDecimal total = order.totalAmount();
        int itemCount = order.items().size();
        int totalQuantity = order.items().stream()
                .mapToInt(OrderItem::quantity)
                .sum();
        BigDecimal averageItemPrice = itemCount > 0
                ? total.divide(BigDecimal.valueOf(totalQuantity), 2, RoundingMode.HALF_UP)
                : BigDecimal.ZERO;

        return new OrderStats(total, itemCount, totalQuantity, averageItemPrice);
    }

    /**
     * Convert order time between timezones.
     */
    public static String formatOrderTimeInZone(Order order, ZoneId targetZone) {
        ZonedDateTime converted = order.orderTime().withZoneSameInstant(targetZone);
        return converted.format(DATE_FMT);
    }

    public record OrderStats(
            BigDecimal totalAmount,
            int distinctItems,
            int totalQuantity,
            BigDecimal averageUnitPrice
    ) {}
}
