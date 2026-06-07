package com.javastudy.testing;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class BuildTestingQualityDemo {

    private static final Logger log = LoggerFactory.getLogger(BuildTestingQualityDemo.class);

    public record MavenCoordinate(String groupId, String artifactId, String version) {}

    public MavenCoordinate projectCoordinate() {
        return new MavenCoordinate("com.javastudy", "java-knowledge-demo", "1.0.0-SNAPSHOT");
    }

    public List<String> dependencyScopes() {
        return List.of("compile", "provided", "runtime", "test", "system", "import");
    }

    public List<String> ciPipeline() {
        return List.of("compile", "unit test", "static analysis", "coverage", "package");
    }

    public List<String> qualityTools() {
        return List.of("Checkstyle", "SpotBugs", "PMD", "Error Prone", "JaCoCo", "SonarQube");
    }

    public List<String> debuggingFeatures() {
        return List.of("breakpoint", "conditional breakpoint", "evaluate expression",
                "call stack", "thread view", "remote debug");
    }

    public String slf4jPlaceholder(String userId, int orderCount) {
        log.info("user {} has {} orders", userId, orderCount);
        return "user %s has %d orders".formatted(userId, orderCount);
    }

    public int calculateTotal(PriceClient client, List<String> skuList) {
        return skuList.stream().mapToInt(client::priceOf).sum();
    }

    public String dependencyConflictRule(boolean directVersionWins) {
        return directVersionWins ? "nearest definition wins" : "dependencyManagement pins versions";
    }

    public interface PriceClient {
        int priceOf(String sku);
    }
}
