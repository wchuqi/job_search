package com.javastudy.modernjava;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.SequencedCollection;
import java.util.concurrent.Executors;

public class ModernJavaDemo {

    public sealed interface Shape permits Circle, Rectangle {}
    public record Circle(double radius) implements Shape {}
    public record Rectangle(double width, double height) implements Shape {}
    public record Point(int x, int y) {}

    public String switchPattern(Shape shape) {
        return switch (shape) {
            case Circle c when c.radius() > 10 -> "large circle";
            case Circle c -> "circle %.1f".formatted(c.radius());
            case Rectangle r -> "rectangle %.1f".formatted(r.width() * r.height());
        };
    }

    public String recordPattern(Object value) {
        if (value instanceof Point(int x, int y)) {
            return x + "," + y;
        }
        return "not point";
    }

    public List<String> sequencedCollectionDemo() {
        SequencedCollection<String> values = new LinkedHashSet<>(List.of("first", "middle", "last"));
        values.addFirst("zero");
        values.addLast("tail");
        return new ArrayList<>(values.reversed());
    }

    public boolean virtualThreadSupported() throws Exception {
        try (var executor = Executors.newVirtualThreadPerTaskExecutor()) {
            return executor.submit(() -> Thread.currentThread().isVirtual()).get();
        }
    }

    public List<String> jdk21Features() {
        return List.of("virtual threads", "pattern matching for switch", "record patterns",
                "sequenced collections", "generational ZGC");
    }

    public List<String> previewFeatureFlags() {
        return List.of("--enable-preview", "--release 21");
    }

    public List<String> java8To21UpgradeChecklist() {
        return List.of("update source/target release", "check removed APIs", "fix illegal reflection",
                "review GC flags", "modernize tests and CI");
    }
}
