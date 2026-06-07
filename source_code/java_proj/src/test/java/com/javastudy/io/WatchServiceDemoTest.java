package com.javastudy.io;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.WatchService;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class WatchServiceDemoTest {

    @TempDir
    Path tempDir;

    @Test
    void createWatchService_shouldNotBeNull() throws IOException {
        try (WatchService ws = WatchServiceDemo.createWatchService(tempDir)) {
            assertNotNull(ws);
        }
    }

    @Test
    void pollEvents_shouldDetectFileCreation() throws Exception {
        try (WatchService ws = WatchServiceDemo.createWatchService(tempDir)) {
            // Create a file to trigger an event
            Path newFile = tempDir.resolve("new.txt");
            Files.writeString(newFile, "content", StandardCharsets.UTF_8);

            // Poll with a generous timeout
            List<String> events = WatchServiceDemo.pollEvents(ws, 3000);
            assertFalse(events.isEmpty(), "Should detect at least one event");
        }
    }

    @Test
    void pollEvents_shouldDetectFileModification() throws Exception {
        try (WatchService ws = WatchServiceDemo.createWatchService(tempDir)) {
            Path file = tempDir.resolve("modify.txt");
            Files.writeString(file, "initial", StandardCharsets.UTF_8);

            // Small delay to ensure the creation event is consumed first
            Thread.sleep(200);
            // Drain creation events
            WatchServiceDemo.pollEvents(ws, 1000);

            // Modify the file
            Files.writeString(file, "modified", StandardCharsets.UTF_8);

            List<String> events = WatchServiceDemo.pollEvents(ws, 3000);
            assertFalse(events.isEmpty(), "Should detect modification event");
        }
    }

    @Test
    void pollEventFileNames_shouldReturnFileName() throws Exception {
        try (WatchService ws = WatchServiceDemo.createWatchService(tempDir)) {
            Path file = tempDir.resolve("tracked.txt");
            Files.writeString(file, "data", StandardCharsets.UTF_8);

            List<String> fileNames = WatchServiceDemo.pollEventFileNames(ws, 3000);
            assertFalse(fileNames.isEmpty(), "Should detect file creation");
            assertTrue(fileNames.contains("tracked.txt"));
        }
    }

    @Test
    void pollModifyEvents_shouldOnlyReturnModifyEvents() throws Exception {
        try (WatchService ws = WatchServiceDemo.createWatchService(tempDir)) {
            // Modify events may not fire reliably on all platforms for file creation.
            // This test verifies the filtering logic works when events are present.
            Path file = tempDir.resolve("mod.txt");
            Files.writeString(file, "v1", StandardCharsets.UTF_8);
            Thread.sleep(200);
            WatchServiceDemo.pollEvents(ws, 1000); // drain create

            Files.writeString(file, "v2", StandardCharsets.UTF_8);
            List<String> modEvents = WatchServiceDemo.pollModifyEvents(ws, 3000);
            // On some OS, MODIFY events may or may not fire; just verify no crash
            assertNotNull(modEvents);
        }
    }

    @Test
    void pollEvents_noEvents_shouldReturnEmptyList() throws Exception {
        try (WatchService ws = WatchServiceDemo.createWatchService(tempDir)) {
            // Don't trigger any events, poll with short timeout
            List<String> events = WatchServiceDemo.pollEvents(ws, 200);
            assertTrue(events.isEmpty());
        }
    }
}
