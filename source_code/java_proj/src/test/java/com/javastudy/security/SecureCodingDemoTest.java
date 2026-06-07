package com.javastudy.security;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class SecureCodingDemoTest {

    private final SecureCodingDemo demo = new SecureCodingDemo();

    @Test
    void inputSqlXssAndSsrfDefensesAreDemonstrated() {
        assertTrue(demo.validUsername("alice_01"));
        assertFalse(demo.validUsername("1bad"));
        assertTrue(demo.preparedSqlTemplate().contains("?"));
        assertEquals("&lt;script&gt;", demo.escapeHtml("<script>"));
        assertTrue(demo.allowedOutboundUrl("https://example.com/api"));
        assertFalse(demo.allowedOutboundUrl("http://localhost/admin"));
    }

    @Test
    void secretsLogsDependenciesAndDeserializationAreHandled() {
        assertEquals("****", demo.maskSecret("abc"));
        assertEquals("******7890", demo.maskSecret("1234567890"));
        assertEquals("a_b_c", demo.sanitizeForLog("a\nb\rc"));
        assertTrue(demo.dependencyVulnerabilityChecklist().contains("scan CVE"));
        assertTrue(demo.deserializationDefenses().contains("use ObjectInputFilter"));
    }

    @Test
    void immutabilityNullStrategyAndLayeringAreCovered() {
        List<String> copy = demo.immutableCopy(new ArrayList<>(List.of("a")));
        assertThrows(UnsupportedOperationException.class, () -> copy.add("b"));
        assertTrue(demo.optionalEmail("").isEmpty());
        assertEquals("dev@example.com", demo.optionalEmail("dev@example.com").orElseThrow());
        assertTrue(demo.layeredArchitecture().contains("service"));
    }
}
