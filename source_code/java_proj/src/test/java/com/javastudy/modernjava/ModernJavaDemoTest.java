package com.javastudy.modernjava;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ModernJavaDemoTest {

    private final ModernJavaDemo demo = new ModernJavaDemo();

    @Test
    void patternMatchingRecordsSealedAndSequencedCollectionsWork() {
        assertEquals("large circle", demo.switchPattern(new ModernJavaDemo.Circle(11)));
        assertEquals("circle 2.0", demo.switchPattern(new ModernJavaDemo.Circle(2)));
        assertEquals("rectangle 12.0", demo.switchPattern(new ModernJavaDemo.Rectangle(3, 4)));
        assertEquals("3,4", demo.recordPattern(new ModernJavaDemo.Point(3, 4)));
        assertEquals("not point", demo.recordPattern("x"));
        assertEquals(List.of("tail", "last", "middle", "first", "zero"), demo.sequencedCollectionDemo());
    }

    @Test
    void jdk21VirtualThreadsPreviewAndUpgradeConceptsAreCovered() throws Exception {
        assertTrue(demo.virtualThreadSupported());
        assertTrue(demo.jdk21Features().contains("generational ZGC"));
        assertTrue(demo.previewFeatureFlags().contains("--enable-preview"));
        assertTrue(demo.java8To21UpgradeChecklist().contains("fix illegal reflection"));
    }
}
