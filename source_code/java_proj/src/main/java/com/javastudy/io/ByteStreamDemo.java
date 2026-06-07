package com.javastudy.io;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * 知识点：字节流 (Byte Stream)
 * Files.newInputStream 读取原始字节, readAllBytes 一次性读取全部
 */
public class ByteStreamDemo {

    /**
     * 使用 Files.newInputStream 获取 InputStream, 逐字节读取
     * 返回读取的字节总和 (用于验证正确性)
     */
    public static int readByteByByte(Path path) throws IOException {
        int total = 0;
        try (InputStream in = Files.newInputStream(path)) {
            int b;
            while ((b = in.read()) != -1) {
                total += b;
            }
        }
        return total;
    }

    /**
     * 使用 readAllBytes 一次性读取文件全部字节
     */
    public static byte[] readAll(Path path) throws IOException {
        return Files.readAllBytes(path);
    }

    /**
     * 读取指定偏移量和长度的字节
     */
    public static byte[] readPartial(Path path, int offset, int len) throws IOException {
        byte[] all = Files.readAllBytes(path);
        if (offset >= all.length) {
            return new byte[0];
        }
        int end = Math.min(offset + len, all.length);
        byte[] result = new byte[end - offset];
        System.arraycopy(all, offset, result, 0, result.length);
        return result;
    }
}
