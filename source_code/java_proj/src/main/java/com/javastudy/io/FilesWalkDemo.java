package com.javastudy.io;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Stream;

/**
 * 知识点：Files.walk 遍历目录树
 * 结合 isRegularFile 过滤, 支持 maxDepth 控制深度
 */
public class FilesWalkDemo {

    /**
     * 使用 Files.walk 遍历目录, 收集所有普通文件路径
     */
    public static List<Path> listRegularFiles(Path dir) throws IOException {
        try (Stream<Path> stream = Files.walk(dir)) {
            return stream
                    .filter(Files::isRegularFile)
                    .toList();
        }
    }

    /**
     * 限制遍历深度
     */
    public static List<Path> listRegularFilesWithDepth(Path dir, int maxDepth) throws IOException {
        try (Stream<Path> stream = Files.walk(dir, maxDepth)) {
            return stream
                    .filter(Files::isRegularFile)
                    .toList();
        }
    }

    /**
     * 按文件扩展名过滤
     */
    public static List<Path> listByExtension(Path dir, String extension) throws IOException {
        try (Stream<Path> stream = Files.walk(dir)) {
            return stream
                    .filter(Files::isRegularFile)
                    .filter(p -> p.toString().endsWith(extension))
                    .toList();
        }
    }

    /**
     * 统计目录下普通文件数量
     */
    public static long countRegularFiles(Path dir) throws IOException {
        try (Stream<Path> stream = Files.walk(dir)) {
            return stream.filter(Files::isRegularFile).count();
        }
    }

    /**
     * 列出直接子目录 (不递归)
     */
    public static List<Path> listSubDirectories(Path dir) throws IOException {
        try (Stream<Path> stream = Files.list(dir)) {
            return stream
                    .filter(Files::isDirectory)
                    .toList();
        }
    }
}
