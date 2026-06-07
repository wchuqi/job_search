package com.javastudy.io;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class CharStreamDemoTest {

    @TempDir
    Path tempDir;

    private Path textFile;

    @BeforeEach
    void setUp() throws IOException {
        textFile = tempDir.resolve("text.txt");
        Files.writeString(textFile, "hello\nworld\nfoo", StandardCharsets.UTF_8);
    }

    @Test
    void readLines_shouldReturnAllLines() throws IOException {
        List<String> lines = CharStreamDemo.readLines(textFile);
        assertEquals(List.of("hello", "world", "foo"), lines);
    }

    @Test
    void readLines_utf8Content_shouldHandleCorrectly() throws IOException {
        Path utf8File = tempDir.resolve("utf8.txt");
        Files.writeString(utf8File, "你好\n世界", StandardCharsets.UTF_8);
        List<String> lines = CharStreamDemo.readLines(utf8File);
        assertEquals(2, lines.size());
        assertEquals("你好", lines.get(0));
        assertEquals("世界", lines.get(1));
    }

    @Test
    void readFirstLine_shouldReturnFirstLine() throws IOException {
        assertEquals("hello", CharStreamDemo.readFirstLine(textFile));
    }

    @Test
    void readFirstLine_emptyFile_shouldReturnNull() throws IOException {
        Path empty = tempDir.resolve("empty.txt");
        Files.writeString(empty, "", StandardCharsets.UTF_8);
        assertNull(CharStreamDemo.readFirstLine(empty));
    }

    @Test
    void readLines_singleLine_shouldReturnOneElement() throws IOException {
        Path single = tempDir.resolve("single.txt");
        Files.writeString(single, "only one line", StandardCharsets.UTF_8);
        List<String> lines = CharStreamDemo.readLines(single);
        assertEquals(1, lines.size());
        assertEquals("only one line", lines.get(0));
    }

    @Test
    void readLinesDefaultCharset_shouldReturnAllLines() throws IOException {
        List<String> lines = CharStreamDemo.readLinesDefaultCharset(textFile);
        assertEquals(3, lines.size());
        assertEquals("hello", lines.get(0));
    }
}
