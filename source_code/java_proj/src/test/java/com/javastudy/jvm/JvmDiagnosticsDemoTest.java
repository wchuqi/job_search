package com.javastudy.jvm;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class JvmDiagnosticsDemoTest {

    private final JvmDiagnosticsDemo demo = new JvmDiagnosticsDemo();

    @Test
    void memoryClassLoadingGcAndJitConceptsAreListed() {
        assertTrue(demo.runtimeMemoryAreas().contains("heap"));
        assertEquals("load", demo.classLoadingPhases().getFirst());
        assertTrue(demo.objectCreationSteps().contains("constructor"));
        assertTrue(demo.gcRoots().contains("static fields"));
        assertTrue(demo.collectors().contains("ZGC"));
        assertTrue(demo.jitOptimizations().contains("escape analysis"));
    }

    @Test
    void diagnosticAndTroubleshootingRunbooksAreListed() {
        assertTrue(demo.stackFrameParts().contains("operand stack"));
        assertFalse(demo.classLoaders().isEmpty());
        assertTrue(demo.diagnosticTools().contains("jcmd"));
        assertEquals("inspect thread dump", demo.cpuSpikeRunbook().getLast());
        assertTrue(demo.memoryLeakSignals().contains("heap dump"));
        assertTrue(demo.deadlockMonitoringAvailable());
    }

    @Test
    void javaMemoryModelKeywordsAreExplained() {
        assertEquals("visibility and ordering", demo.javaMemoryModelKeyword("volatile"));
        assertEquals("mutual exclusion and happens-before", demo.javaMemoryModelKeyword("synchronized"));
        assertEquals("safe publication after construction", demo.javaMemoryModelKeyword("final"));
        assertEquals("unknown", demo.javaMemoryModelKeyword("other"));
    }
}
