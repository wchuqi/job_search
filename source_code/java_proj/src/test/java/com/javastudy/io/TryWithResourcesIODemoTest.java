package com.javastudy.io;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

class TryWithResourcesIODemoTest {

    @TempDir
    Path tempDir;

    @Test
    void copyFile_shouldCopyContentLineByLine() throws IOException {
        Path source = tempDir.resolve("source.txt");
        Path target = tempDir.resolve("target.txt");
        Files.writeString(source, "line1\nline2\nline3", StandardCharsets.UTF_8);

        TryWithResourcesIODemo.copyFile(source, target);

        String content = Files.readString(target, StandardCharsets.UTF_8);
        assertEquals(String.join(System.lineSeparator(), "line1", "line2", "line3")
                + System.lineSeparator(), content);
    }

    @Test
    void copyFile_emptyFile_shouldCreateEmptyTarget() throws IOException {
        Path source = tempDir.resolve("empty_source.txt");
        Path target = tempDir.resolve("empty_target.txt");
        Files.writeString(source, "", StandardCharsets.UTF_8);

        TryWithResourcesIODemo.copyFile(source, target);

        assertEquals("", Files.readString(target, StandardCharsets.UTF_8));
    }

    @Test
    void copyFile_utf8Content_shouldPreserveEncoding() throws IOException {
        Path source = tempDir.resolve("utf8_source.txt");
        Path target = tempDir.resolve("utf8_target.txt");
        Files.writeString(source, "你好\n世界", StandardCharsets.UTF_8);

        TryWithResourcesIODemo.copyFile(source, target);

        String content = Files.readString(target, StandardCharsets.UTF_8);
        assertTrue(content.contains("你好"));
        assertTrue(content.contains("世界"));
    }

    @Test
    void copyFileWithFilesCopy_shouldCopyBytes() throws IOException {
        Path source = tempDir.resolve("src.bin");
        Path target = tempDir.resolve("tgt.bin");
        Files.writeString(source, "binary content", StandardCharsets.UTF_8);

        TryWithResourcesIODemo.copyFileWithFilesCopy(source, target);

        assertEquals(Files.readString(source, StandardCharsets.UTF_8),
                Files.readString(target, StandardCharsets.UTF_8));
    }

    @Test
    void readFirstAndLast_shouldReturnFirstAndLastChars() throws IOException {
        Path file = tempDir.resolve("firstlast.txt");
        Files.writeString(file, "abc\ndef\nghi", StandardCharsets.UTF_8);

        String result = TryWithResourcesIODemo.readFirstAndLast(file);
        assertEquals("ai", result); // 'a' from "abc", 'i' from "ghi"
    }

    @Test
    void readFirstAndLast_singleLine_shouldReturnFirstAndLastOfSameLine() throws IOException {
        Path file = tempDir.resolve("single.txt");
        Files.writeString(file, "hello", StandardCharsets.UTF_8);

        String result = TryWithResourcesIODemo.readFirstAndLast(file);
        assertEquals("ho", result);
    }

    @Test
    void readFirstAndLast_emptyFile_shouldReturnEmpty() throws IOException {
        Path file = tempDir.resolve("empty.txt");
        Files.writeString(file, "", StandardCharsets.UTF_8);

        String result = TryWithResourcesIODemo.readFirstAndLast(file);
        assertEquals("", result);
    }
}
