package com.javastudy.io;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

class NioChannelBufferDemoTest {

    @TempDir
    Path tempDir;

    @Test
    void writeAndReadWithChannel_shouldRoundTrip() throws IOException {
        Path file = tempDir.resolve("channel.txt");
        String content = "Hello, NIO Channel!";
        NioChannelBufferDemo.writeWithChannel(file, content);

        String read = NioChannelBufferDemo.readWithChannel(file);
        assertEquals(content, read);
    }

    @Test
    void writeWithChannel_shouldOverwriteExisting() throws IOException {
        Path file = tempDir.resolve("overwrite.txt");
        NioChannelBufferDemo.writeWithChannel(file, "first");
        NioChannelBufferDemo.writeWithChannel(file, "second");

        assertEquals("second", NioChannelBufferDemo.readWithChannel(file));
    }

    @Test
    void readWithChannel_emptyFile_shouldReturnEmpty() throws IOException {
        Path file = tempDir.resolve("empty.txt");
        Files.writeString(file, "", StandardCharsets.UTF_8);

        String result = NioChannelBufferDemo.readWithChannel(file);
        assertEquals("", result);
    }

    @Test
    void readWithChannel_utf8Content_shouldHandleMultibyte() throws IOException {
        Path file = tempDir.resolve("utf8.txt");
        String content = "你好世界";
        NioChannelBufferDemo.writeWithChannel(file, content);

        String read = NioChannelBufferDemo.readWithChannel(file);
        assertEquals(content, read);
    }

    @Test
    void readWithChannel_longContent_shouldHandleMultipleBufferFills() throws IOException {
        Path file = tempDir.resolve("long.txt");
        // Create content longer than the 1024-byte buffer
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 200; i++) {
            sb.append("ABCDEFGHIJ"); // 10 chars * 200 = 2000 chars
        }
        String content = sb.toString();
        NioChannelBufferDemo.writeWithChannel(file, content);

        String read = NioChannelBufferDemo.readWithChannel(file);
        assertEquals(content, read);
    }

    @Test
    void demonstrateBufferOperations_shouldReturnSum() {
        int result = NioChannelBufferDemo.demonstrateBufferOperations();
        assertEquals(142, result); // 42 + 100
    }

    @Test
    void demonstrateCompact_shouldReturnRemainingByteCount() {
        int remaining = NioChannelBufferDemo.demonstrateCompact();
        assertEquals(2, remaining); // 4 bytes written, 2 read, 2 remaining after compact
    }
}
