package com.javastudy.io;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * 知识点：try-with-resources 在 IO 中的应用
 * 自动关闭 Reader/Writer, 无论是否发生异常
 */
public class TryWithResourcesIODemo {

    /**
     * 使用 try-with-resources 复制文本文件 (逐行)
     */
    public static void copyFile(Path source, Path target) throws IOException {
        try (BufferedReader reader = Files.newBufferedReader(source, StandardCharsets.UTF_8);
             BufferedWriter writer = Files.newBufferedWriter(target, StandardCharsets.UTF_8)) {
            String line;
            while ((line = reader.readLine()) != null) {
                writer.write(line);
                writer.newLine();
            }
        }
    }

    /**
     * 使用 Files.copy 简化版复制
     */
    public static void copyFileWithFilesCopy(Path source, Path target) throws IOException {
        Files.copy(source, target);
    }

    /**
     * 演示多个资源在同一个 try 语句中声明
     * 返回第一个和最后一个字符
     */
    public static String readFirstAndLast(Path path) throws IOException {
        try (BufferedReader reader = Files.newBufferedReader(path, StandardCharsets.UTF_8)) {
            String firstLine = reader.readLine();
            if (firstLine == null) {
                return "";
            }
            String lastLine = firstLine;
            String line;
            while ((line = reader.readLine()) != null) {
                lastLine = line;
            }
            return "" + firstLine.charAt(0) + lastLine.charAt(lastLine.length() - 1);
        }
    }
}
