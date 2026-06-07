package com.javastudy.io;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class FilesWalkDemoTest {

    @TempDir
    Path tempDir;

    private Path subDir;

    @BeforeEach
    void setUp() throws IOException {
        // Create structure:
        // tempDir/
        //   a.txt
        //   b.java
        //   sub/
        //     c.txt
        //     deep/
        //       d.txt
        subDir = tempDir.resolve("sub");
        Path deepDir = subDir.resolve("deep");
        Files.createDirectories(deepDir);

        Files.writeString(tempDir.resolve("a.txt"), "a");
        Files.writeString(tempDir.resolve("b.java"), "b");
        Files.writeString(subDir.resolve("c.txt"), "c");
        Files.writeString(deepDir.resolve("d.txt"), "d");
    }

    @Test
    void listRegularFiles_shouldFindAllFiles() throws IOException {
        List<Path> files = FilesWalkDemo.listRegularFiles(tempDir);
        assertEquals(4, files.size());
    }

    @Test
    void listRegularFilesWithDepth_depth1_shouldOnlyFindTopLevel() throws IOException {
        List<Path> files = FilesWalkDemo.listRegularFilesWithDepth(tempDir, 1);
        assertEquals(2, files.size()); // a.txt and b.java only
    }

    @Test
    void listRegularFilesWithDepth_depth2_shouldFindOneLevelDeep() throws IOException {
        List<Path> files = FilesWalkDemo.listRegularFilesWithDepth(tempDir, 2);
        assertEquals(3, files.size()); // a.txt, b.java, c.txt
    }

    @Test
    void listByExtension_txt_shouldFindOnlyTxtFiles() throws IOException {
        List<Path> txtFiles = FilesWalkDemo.listByExtension(tempDir, ".txt");
        assertEquals(3, txtFiles.size()); // a.txt, c.txt, d.txt
    }

    @Test
    void listByExtension_java_shouldFindOnlyJavaFiles() throws IOException {
        List<Path> javaFiles = FilesWalkDemo.listByExtension(tempDir, ".java");
        assertEquals(1, javaFiles.size());
        assertTrue(javaFiles.get(0).toString().endsWith("b.java"));
    }

    @Test
    void listByExtension_noMatch_shouldReturnEmpty() throws IOException {
        List<Path> xmlFiles = FilesWalkDemo.listByExtension(tempDir, ".xml");
        assertTrue(xmlFiles.isEmpty());
    }

    @Test
    void countRegularFiles_shouldReturnTotalCount() throws IOException {
        assertEquals(4, FilesWalkDemo.countRegularFiles(tempDir));
    }

    @Test
    void listSubDirectories_shouldReturnDirectSubdirs() throws IOException {
        List<Path> subDirs = FilesWalkDemo.listSubDirectories(tempDir);
        assertEquals(1, subDirs.size());
        assertEquals(subDir.getFileName(), subDirs.get(0).getFileName());
    }

    @Test
    void listSubDirectories_emptyDir_shouldReturnEmpty() throws IOException {
        Path empty = tempDir.resolve("empty");
        Files.createDirectory(empty);
        List<Path> subDirs = FilesWalkDemo.listSubDirectories(empty);
        assertTrue(subDirs.isEmpty());
    }
}
