package com.javastudy.io;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

class PathDemoTest {

    @TempDir
    Path tempDir;

    @Test
    void createPath_shouldConstructPath() {
        Path path = PathDemo.createPath("a", "b", "c");
        assertEquals(Path.of("a", "b", "c"), path);
    }

    @Test
    void exists_existingFile_shouldReturnTrue() throws IOException {
        Path file = tempDir.resolve("existing.txt");
        Files.writeString(file, "content", StandardCharsets.UTF_8);
        assertTrue(PathDemo.exists(file));
    }

    @Test
    void exists_nonExistingFile_shouldReturnFalse() {
        assertFalse(PathDemo.exists(tempDir.resolve("no_such_file.txt")));
    }

    @Test
    void isRegularFile_file_shouldReturnTrue() throws IOException {
        Path file = tempDir.resolve("regular.txt");
        Files.writeString(file, "data", StandardCharsets.UTF_8);
        assertTrue(PathDemo.isRegularFile(file));
    }

    @Test
    void isRegularFile_directory_shouldReturnFalse() {
        assertFalse(PathDemo.isRegularFile(tempDir));
    }

    @Test
    void isDirectory_directory_shouldReturnTrue() {
        assertTrue(PathDemo.isDirectory(tempDir));
    }

    @Test
    void isDirectory_file_shouldReturnFalse() throws IOException {
        Path file = tempDir.resolve("file.txt");
        Files.writeString(file, "data", StandardCharsets.UTF_8);
        assertFalse(PathDemo.isDirectory(file));
    }

    @Test
    void getFileName_shouldReturnFileName() {
        Path path = Path.of("/some/dir/file.txt");
        assertEquals("file.txt", PathDemo.getFileName(path));
    }

    @Test
    void getParent_shouldReturnParentPath() {
        Path path = Path.of("/some/dir/file.txt");
        assertEquals(Path.of("/some/dir"), PathDemo.getParent(path));
    }

    @Test
    void resolvePath_shouldAppendChild() {
        Path base = Path.of("/base");
        Path resolved = PathDemo.resolvePath(base, "child");
        assertEquals(Path.of("/base", "child"), resolved);
    }

    @Test
    void getFileSize_shouldReturnCorrectSize() throws IOException {
        Path file = tempDir.resolve("size.txt");
        Files.writeString(file, "hello", StandardCharsets.UTF_8);
        assertEquals(5, PathDemo.getFileSize(file));
    }
}
