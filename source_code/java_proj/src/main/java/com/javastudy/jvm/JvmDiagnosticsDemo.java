package com.javastudy.jvm;

import java.lang.management.ManagementFactory;
import java.lang.management.ThreadMXBean;
import java.util.List;

public class JvmDiagnosticsDemo {

    public List<String> runtimeMemoryAreas() {
        return List.of("heap", "java stack", "native method stack", "metaspace", "program counter");
    }

    public List<String> stackFrameParts() {
        return List.of("local variables", "operand stack", "dynamic link", "return address");
    }

    public List<String> classLoadingPhases() {
        return List.of("load", "verify", "prepare", "resolve", "initialize");
    }

    public List<String> classLoaders() {
        ClassLoader application = getClass().getClassLoader();
        ClassLoader platform = String.class.getClassLoader();
        return List.of(application.getName(), platform == null ? "bootstrap" : platform.getName());
    }

    public List<String> objectCreationSteps() {
        return List.of("class loading check", "allocate memory", "zero initialization",
                "object header", "constructor");
    }

    public List<String> gcRoots() {
        return List.of("stack references", "static fields", "constant references", "JNI references");
    }

    public List<String> collectors() {
        return List.of("G1", "ZGC", "Shenandoah");
    }

    public List<String> jitOptimizations() {
        return List.of("hotspot detection", "method inlining", "escape analysis",
                "scalar replacement", "tiered compilation");
    }

    public List<String> diagnosticTools() {
        return List.of("jps", "jcmd", "jstack", "jmap", "jstat", "jfr", "jdeps");
    }

    public List<String> cpuSpikeRunbook() {
        return List.of("find process", "find hot thread", "convert nid to hex", "inspect thread dump");
    }

    public List<String> memoryLeakSignals() {
        return List.of("frequent full GC", "heap keeps growing", "OutOfMemoryError", "heap dump");
    }

    public boolean deadlockMonitoringAvailable() {
        ThreadMXBean bean = ManagementFactory.getThreadMXBean();
        return bean.isObjectMonitorUsageSupported() || bean.isSynchronizerUsageSupported();
    }

    public String javaMemoryModelKeyword(String keyword) {
        return switch (keyword) {
            case "volatile" -> "visibility and ordering";
            case "synchronized" -> "mutual exclusion and happens-before";
            case "final" -> "safe publication after construction";
            default -> "unknown";
        };
    }
}
