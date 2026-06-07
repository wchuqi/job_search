package com.javastudy.io;

import java.io.IOException;
import java.nio.file.CopyOption;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

/**
 * 知识点：文件的复制、移动、删除
 * Files.copy, Files.move (REPLACE_EXISTING), Files.delete, Files.deleteIfExists
 */
public class FilesCopyMoveDeleteDemo {

    /**
     * 复制文件 (不覆盖已存在的目标)
     */
    public static Path copy(Path source, Path target) throws IOException {
        return Files.copy(source, target);
    }

    /**
     * 复制文件 (覆盖已存在的目标)
     */
    public static Path copyReplaceExisting(Path source, Path target) throws IOException {
        return Files.copy(source, target, StandardCopyOption.REPLACE_EXISTING);
    }

    /**
     * 移动文件
     */
    public static Path move(Path source, Path target) throws IOException {
        return Files.move(source, target);
    }

    /**
     * 移动文件 (覆盖已存在的目标)
     */
    public static Path moveReplaceExisting(Path source, Path target) throws IOException {
        return Files.move(source, target, StandardCopyOption.REPLACE_EXISTING);
    }

    /**
     * 删除文件, 若不存在则抛 NoSuchFileException
     */
    public static void delete(Path path) throws IOException {
        Files.delete(path);
    }

    /**
     * 删除文件, 若不存在则返回 false 而非抛异常
     */
    public static boolean deleteIfExists(Path path) throws IOException {
        return Files.deleteIfExists(path);
    }
}
