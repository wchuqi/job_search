package com.javastudy.io;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class FilesReadWriteDemoTest {

    @TempDir
    Path tempDir;

    @Test
    void writeStringAndReadAllLines_shouldRoundTrip() throws IOException {
        Path file = tempDir.resolve("rw.txt");
        FilesReadWriteDemo.writeString(file, "line1\nline2\nline3");

        List<String> lines = FilesReadWriteDemo.readAllLines(file);
        assertEquals(List.of("line1", "line2", "line3"), lines);
    }

    @Test
    void writeString_shouldOverwriteExisting() throws IOException {
        Path file = tempDir.resolve("overwrite.txt");
        FilesReadWriteDemo.writeString(file, "first");
        FilesReadWriteDemo.writeString(file, "second");

        String content = FilesReadWriteDemo.readString(file);
        assertEquals("second", content);
    }

    @Test
    void appendString_shouldAppendToExisting() throws IOException {
        Path file = tempDir.resolve("append.txt");
        FilesReadWriteDemo.writeString(file, "hello");
        FilesReadWriteDemo.appendString(file, " world");

        String content = FilesReadWriteDemo.readString(file);
        assertEquals("hello world", content);
    }

    @Test
    void appendString_nonExistingFile_shouldCreateFile() throws IOException {
        Path file = tempDir.resolve("new_append.txt");
        FilesReadWriteDemo.appendString(file, "created");

        assertEquals("created", FilesReadWriteDemo.readString(file));
    }

    @Test
    void readString_shouldReturnFullContent() throws IOException {
        Path file = tempDir.resolve("readstr.txt");
        Files.writeString(file, "full content here");

        assertEquals("full content here", FilesReadWriteDemo.readString(file));
    }

    @Test
    void writeLines_shouldWriteMultipleLines() throws IOException {
        Path file = tempDir.resolve("writelines.txt");
        FilesReadWriteDemo.writeLines(file, List.of("alpha", "beta", "gamma"));

        List<String> lines = FilesReadWriteDemo.readAllLines(file);
        assertEquals(3, lines.size());
        assertEquals("alpha", lines.get(0));
        assertEquals("gamma", lines.get(2));
    }

    @Test
    void readAllLines_emptyFile_shouldReturnEmptyList() throws IOException {
        Path file = tempDir.resolve("empty.txt");
        Files.writeString(file, "");

        List<String> lines = FilesReadWriteDemo.readAllLines(file);
        assertEquals(List.of(), lines);
    }

    @Test
    void writeString_utf8_shouldPreserveEncoding() throws IOException {
        Path file = tempDir.resolve("utf8.txt");
        FilesReadWriteDemo.writeString(file, "你好世界");

        assertEquals("你好世界", FilesReadWriteDemo.readString(file));
    }
}
