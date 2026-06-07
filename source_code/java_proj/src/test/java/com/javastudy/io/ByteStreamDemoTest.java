package com.javastudy.io;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

class ByteStreamDemoTest {

    @TempDir
    Path tempDir;

    private Path testFile;

    @BeforeEach
    void setUp() throws IOException {
        testFile = tempDir.resolve("bytes.bin");
        Files.write(testFile, new byte[]{1, 2, 3, 4, 5});
    }

    @Test
    void readByteByByte_shouldReturnSumOfBytes() throws IOException {
        int total = ByteStreamDemo.readByteByByte(testFile);
        assertEquals(1 + 2 + 3 + 4 + 5, total);
    }

    @Test
    void readByteByByte_emptyFile_shouldReturnZero() throws IOException {
        Path empty = tempDir.resolve("empty.bin");
        Files.write(empty, new byte[0]);
        assertEquals(0, ByteStreamDemo.readByteByByte(empty));
    }

    @Test
    void readAll_shouldReturnAllBytes() throws IOException {
        byte[] bytes = ByteStreamDemo.readAll(testFile);
        assertArrayEquals(new byte[]{1, 2, 3, 4, 5}, bytes);
    }

    @Test
    void readAll_emptyFile_shouldReturnEmptyArray() throws IOException {
        Path empty = tempDir.resolve("empty.bin");
        Files.write(empty, new byte[0]);
        byte[] bytes = ByteStreamDemo.readAll(empty);
        assertEquals(0, bytes.length);
    }

    @Test
    void readPartial_shouldReturnRequestedSlice() throws IOException {
        byte[] partial = ByteStreamDemo.readPartial(testFile, 1, 3);
        assertArrayEquals(new byte[]{2, 3, 4}, partial);
    }

    @Test
    void readPartial_offsetBeyondEnd_shouldReturnEmpty() throws IOException {
        byte[] partial = ByteStreamDemo.readPartial(testFile, 100, 5);
        assertEquals(0, partial.length);
    }

    @Test
    void readPartial_lenExceedsAvailable_shouldReturnRest() throws IOException {
        byte[] partial = ByteStreamDemo.readPartial(testFile, 3, 100);
        assertArrayEquals(new byte[]{4, 5}, partial);
    }
}
