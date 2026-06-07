package com.javastudy.io;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.List;

/**
 * 知识点：Files 便捷读写方法
 * Files.readAllLines 按行读取, Files.writeString 写入字符串
 */
public class FilesReadWriteDemo {

    /**
     * Files.readAllLines: 一次性读取所有行
     */
    public static List<String> readAllLines(Path path) throws IOException {
        return Files.readAllLines(path, StandardCharsets.UTF_8);
    }

    /**
     * Files.writeString: 写入字符串到文件 (JDK 11+)
     * 默认 CREATE + TRUNCATE_EXISTING + WRITE
     */
    public static void writeString(Path path, String content) throws IOException {
        Files.writeString(path, content, StandardCharsets.UTF_8);
    }

    /**
     * Files.writeString 追加模式
     */
    public static void appendString(Path path, String content) throws IOException {
        Files.writeString(path, content, StandardCharsets.UTF_8,
                StandardOpenOption.CREATE, StandardOpenOption.APPEND);
    }

    /**
     * 读取全部内容为单个 String
     */
    public static String readString(Path path) throws IOException {
        return Files.readString(path, StandardCharsets.UTF_8);
    }

    /**
     * Files.write: 写入多行
     */
    public static void writeLines(Path path, List<String> lines) throws IOException {
        Files.write(path, lines, StandardCharsets.UTF_8);
    }
}
