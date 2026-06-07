package com.javastudy.io;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * 知识点：Path API (NIO.2)
 * Path.of() 构造路径, Files.exists 判断存在性
 */
public class PathDemo {

    /**
     * 使用 Path.of() 构造路径 (JDK 11+)
     */
    public static Path createPath(String first, String... more) {
        return Path.of(first, more);
    }

    /**
     * 判断文件或目录是否存在
     */
    public static boolean exists(Path path) {
        return Files.exists(path);
    }

    /**
     * 判断是否为普通文件
     */
    public static boolean isRegularFile(Path path) {
        return Files.isRegularFile(path);
    }

    /**
     * 判断是否为目录
     */
    public static boolean isDirectory(Path path) {
        return Files.isDirectory(path);
    }

    /**
     * 获取文件名 (不含路径)
     */
    public static String getFileName(Path path) {
        return path.getFileName().toString();
    }

    /**
     * 获取父路径
     */
    public static Path getParent(Path path) {
        return path.getParent();
    }

    /**
     * 路径拼接: resolve
     */
    public static Path resolvePath(Path base, String child) {
        return base.resolve(child);
    }

    /**
     * 获取文件大小
     */
    public static long getFileSize(Path path) throws IOException {
        return Files.size(path);
    }
}
