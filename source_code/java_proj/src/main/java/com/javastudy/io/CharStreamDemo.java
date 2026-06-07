package com.javastudy.io;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

/**
 * 知识点：字符流 (Character Stream)
 * Files.newBufferedReader 以 UTF-8 编码逐行读取文本
 */
public class CharStreamDemo {

    /**
     * 使用 Files.newBufferedReader + UTF-8 逐行读取
     */
    public static List<String> readLines(Path path) throws IOException {
        List<String> lines = new ArrayList<>();
        try (BufferedReader reader = Files.newBufferedReader(path, StandardCharsets.UTF_8)) {
            String line;
            while ((line = reader.readLine()) != null) {
                lines.add(line);
            }
        }
        return lines;
    }

    /**
     * 读取第一行 (演示 BufferedReader.readLine)
     */
    public static String readFirstLine(Path path) throws IOException {
        try (BufferedReader reader = Files.newBufferedReader(path, StandardCharsets.UTF_8)) {
            return reader.readLine();
        }
    }

    /**
     * 使用平台默认编码读取 (不推荐, 演示编码差异)
     */
    public static List<String> readLinesDefaultCharset(Path path) throws IOException {
        List<String> lines = new ArrayList<>();
        try (BufferedReader reader = Files.newBufferedReader(path)) {
            String line;
            while ((line = reader.readLine()) != null) {
                lines.add(line);
            }
        }
        return lines;
    }
}
