package com.javastudy.io;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

class FilesCopyMoveDeleteDemoTest {

    @TempDir
    Path tempDir;

    private Path sourceFile;

    @BeforeEach
    void setUp() throws IOException {
        sourceFile = tempDir.resolve("source.txt");
        Files.writeString(sourceFile, "hello");
    }

    @Test
    void copy_shouldCopyContent() throws IOException {
        Path target = tempDir.resolve("copy.txt");
        Path result = FilesCopyMoveDeleteDemo.copy(sourceFile, target);

        assertTrue(Files.exists(result));
        assertEquals("hello", Files.readString(result));
    }

    @Test
    void copy_targetExists_shouldThrow() throws IOException {
        Path target = tempDir.resolve("existing.txt");
        Files.writeString(target, "existing");

        assertThrows(IOException.class, () ->
                FilesCopyMoveDeleteDemo.copy(sourceFile, target));
    }

    @Test
    void copyReplaceExisting_shouldOverwriteTarget() throws IOException {
        Path target = tempDir.resolve("existing.txt");
        Files.writeString(target, "old content");

        FilesCopyMoveDeleteDemo.copyReplaceExisting(sourceFile, target);

        assertEquals("hello", Files.readString(target));
    }

    @Test
    void move_shouldMoveFile() throws IOException {
        Path target = tempDir.resolve("moved.txt");

        FilesCopyMoveDeleteDemo.move(sourceFile, target);

        assertFalse(Files.exists(sourceFile));
        assertTrue(Files.exists(target));
        assertEquals("hello", Files.readString(target));
    }

    @Test
    void moveReplaceExisting_shouldOverwriteTarget() throws IOException {
        Path target = tempDir.resolve("target.txt");
        Files.writeString(target, "old");

        FilesCopyMoveDeleteDemo.moveReplaceExisting(sourceFile, target);

        assertFalse(Files.exists(sourceFile));
        assertEquals("hello", Files.readString(target));
    }

    @Test
    void delete_shouldDeleteFile() throws IOException {
        assertTrue(Files.exists(sourceFile));
        FilesCopyMoveDeleteDemo.delete(sourceFile);
        assertFalse(Files.exists(sourceFile));
    }

    @Test
    void delete_nonExistingFile_shouldThrow() {
        Path noSuch = tempDir.resolve("no_such.txt");
        assertThrows(IOException.class, () ->
                FilesCopyMoveDeleteDemo.delete(noSuch));
    }

    @Test
    void deleteIfExists_existingFile_shouldReturnTrue() throws IOException {
        assertTrue(FilesCopyMoveDeleteDemo.deleteIfExists(sourceFile));
        assertFalse(Files.exists(sourceFile));
    }

    @Test
    void deleteIfExists_nonExistingFile_shouldReturnFalse() {
        Path noSuch = tempDir.resolve("no_such.txt");
        assertDoesNotThrow(() -> {
            boolean result = FilesCopyMoveDeleteDemo.deleteIfExists(noSuch);
            assertFalse(result);
        });
    }
}
