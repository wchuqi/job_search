package com.javastudy.collections;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

/**
 * Comparator: external comparison strategy.
 *
 * Comparator.comparing() and thenComparing() for fluent, readable sort definitions.
 * Reversed, nullsFirst, nullsLast for edge cases.
 */
public class ComparatorDemo {

    public record Product(String name, String category, double price, int rating) {}

    /** Sort by a single field. */
    public List<String> sortByPrice() {
        List<Product> products = sampleProducts();
        products.sort(Comparator.comparingDouble(Product::price));
        return products.stream().map(Product::name).toList();
    }

    /** Sort by multiple fields: category, then price descending. */
    public List<String> sortByCategoryThenPriceDesc() {
        List<Product> products = sampleProducts();
        products.sort(
            Comparator.comparing(Product::category)
                      .thenComparing(Comparator.comparingDouble(Product::price).reversed())
        );
        return products.stream().map(Product::name).toList();
    }

    /** Reverse comparator. */
    public List<String> sortByRatingDesc() {
        List<Product> products = sampleProducts();
        products.sort(Comparator.comparingInt(Product::rating).reversed());
        return products.stream().map(Product::name).toList();
    }

    /** Comparator with nullsFirst. */
    public List<String> nullsFirstDemo() {
        List<String> items = new ArrayList<>(Arrays.asList("B", null, "A", "C", null));
        items.sort(Comparator.nullsFirst(Comparator.naturalOrder()));
        return items;
    }

    /** Comparator with nullsLast. */
    public List<String> nullsLastDemo() {
        List<String> items = new ArrayList<>(Arrays.asList("B", null, "A", "C", null));
        items.sort(Comparator.nullsLast(Comparator.naturalOrder()));
        return items;
    }

    /** Chaining multiple comparators. */
    public List<String> multiFieldSort() {
        List<Product> products = sampleProducts();
        Comparator<Product> cmp = Comparator.comparing(Product::category)
                                             .thenComparingInt(Product::rating)
                                             .thenComparing(Product::name);
        products.sort(cmp);
        return products.stream().map(Product::name).toList();
    }

    /** ComparingInt, comparingDouble, comparingLong convenience methods. */
    public List<String> comparingIntDemo() {
        List<Product> products = sampleProducts();
        products.sort(Comparator.comparingInt(Product::rating));
        return products.stream().map(Product::name).toList();
    }

    private List<Product> sampleProducts() {
        return new ArrayList<>(List.of(
            new Product("Laptop", "Electronics", 999.99, 4),
            new Product("Book", "Education", 29.99, 5),
            new Product("Phone", "Electronics", 699.99, 3),
            new Product("Pen", "Education", 2.99, 4),
            new Product("Tablet", "Electronics", 499.99, 5)
        ));
    }
}
