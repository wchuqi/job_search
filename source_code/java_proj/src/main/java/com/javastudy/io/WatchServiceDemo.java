package com.javastudy.io;

import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.StandardWatchEventKinds;
import java.nio.file.WatchEvent;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * 知识点：WatchService 文件系统监控
 * FileSystems.newWatchService, 监听 ENTRY_MODIFY 等事件
 */
public class WatchServiceDemo {

    /**
     * 创建 WatchService 并注册目录监听
     */
    public static WatchService createWatchService(Path dir) throws IOException {
        WatchService watchService = FileSystems.getDefault().newWatchService();
        dir.register(watchService,
                StandardWatchEventKinds.ENTRY_CREATE,
                StandardWatchEventKinds.ENTRY_MODIFY,
                StandardWatchEventKinds.ENTRY_DELETE);
        return watchService;
    }

    /**
     * 等待指定超时时间内的事件, 返回事件类型列表
     */
    public static List<String> pollEvents(WatchService watchService, long timeoutMs)
            throws InterruptedException {
        List<String> eventTypes = new ArrayList<>();
        WatchKey key = watchService.poll(timeoutMs, TimeUnit.MILLISECONDS);
        if (key != null) {
            for (WatchEvent<?> event : key.pollEvents()) {
                eventTypes.add(event.kind().name());
            }
            key.reset();
        }
        return eventTypes;
    }

    /**
     * 获取事件关联的文件名
     */
    public static List<String> pollEventFileNames(WatchService watchService, long timeoutMs)
            throws InterruptedException {
        List<String> fileNames = new ArrayList<>();
        WatchKey key = watchService.poll(timeoutMs, TimeUnit.MILLISECONDS);
        if (key != null) {
            for (WatchEvent<?> event : key.pollEvents()) {
                @SuppressWarnings("unchecked")
                WatchEvent<Path> pathEvent = (WatchEvent<Path>) event;
                fileNames.add(pathEvent.context().toString());
            }
            key.reset();
        }
        return fileNames;
    }

    /**
     * 监听特定类型的事件 (如只监听 MODIFY)
     */
    public static List<String> pollModifyEvents(WatchService watchService, long timeoutMs)
            throws InterruptedException {
        List<String> fileNames = new ArrayList<>();
        WatchKey key = watchService.poll(timeoutMs, TimeUnit.MILLISECONDS);
        if (key != null) {
            for (WatchEvent<?> event : key.pollEvents()) {
                if (event.kind() == StandardWatchEventKinds.ENTRY_MODIFY) {
                    @SuppressWarnings("unchecked")
                    WatchEvent<Path> pathEvent = (WatchEvent<Path>) event;
                    fileNames.add(pathEvent.context().toString());
                }
            }
            key.reset();
        }
        return fileNames;
    }
}
