package com.javastudy.testing;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class BuildTestingQualityDemoTest {

    private final BuildTestingQualityDemo demo = new BuildTestingQualityDemo();

    @Test
    void mavenJdk21BuildAndQualityConceptsAreCovered() {
        assertEquals("java-knowledge-demo", demo.projectCoordinate().artifactId());
        assertTrue(demo.dependencyScopes().contains("test"));
        assertEquals("compile", demo.ciPipeline().getFirst());
        assertTrue(demo.qualityTools().contains("JaCoCo"));
        assertTrue(demo.debuggingFeatures().contains("remote debug"));
    }

    @Test
    void junitAndMockitoStyleIsDemonstrated() {
        BuildTestingQualityDemo.PriceClient client = mock(BuildTestingQualityDemo.PriceClient.class);
        when(client.priceOf("A")).thenReturn(10);
        when(client.priceOf("B")).thenReturn(20);
        assertEquals(30, demo.calculateTotal(client, List.of("A", "B")));
    }

    @Test
    void loggingAndDependencyConflictRulesAreNamed() {
        assertEquals("user u1 has 2 orders", demo.slf4jPlaceholder("u1", 2));
        assertEquals("nearest definition wins", demo.dependencyConflictRule(true));
        assertEquals("dependencyManagement pins versions", demo.dependencyConflictRule(false));
    }
}
