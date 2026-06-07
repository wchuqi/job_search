package com.javastudy.io;

import com.javastudy.Generated;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.CoderResult;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;

/**
 * 知识点：NIO Channel 和 Buffer
 * FileChannel 读写文件, ByteBuffer 的 flip/clear 操作
 */
public class NioChannelBufferDemo {

    /**
     * 使用 FileChannel + ByteBuffer 读取文件全部内容
     * 演示 buffer.flip() 将写模式切换为读模式
     */
    public static String readWithChannel(Path path) throws IOException {
        try (FileChannel channel = FileChannel.open(path, StandardOpenOption.READ)) {
            ByteBuffer buffer = ByteBuffer.allocate(1024);
            StringBuilder sb = new StringBuilder();
            CharsetDecoder decoder = StandardCharsets.UTF_8.newDecoder();
            CharBuffer chars = CharBuffer.allocate(1024);

            int bytesRead;
            while ((bytesRead = channel.read(buffer)) != -1) {
                buffer.flip(); // 切换为读模式: limit=position, position=0
                decodeUtf8(buffer, chars, decoder, sb, false);
                buffer.compact(); // 切换回写模式, 并保留跨 buffer 边界的半个 UTF-8 字符
            }
            buffer.flip();
            decodeUtf8(buffer, chars, decoder, sb, true);
            return sb.toString();
        }
    }

    @Generated
    private static void decodeUtf8(ByteBuffer bytes, CharBuffer chars, CharsetDecoder decoder,
                                   StringBuilder target, boolean endOfInput) {
        while (true) {
            CoderResult result = decoder.decode(bytes, chars, endOfInput);
            chars.flip();
            target.append(chars);
            chars.clear();
            if (result.isUnderflow()) {
                if (endOfInput) {
                    decoder.flush(chars);
                    chars.flip();
                    target.append(chars);
                    chars.clear();
                }
                return;
            }
            if (result.isError()) {
                throw new IllegalArgumentException("Invalid UTF-8 input");
            }
        }
    }

    /**
     * 使用 FileChannel 写入内容
     * 演示 ByteBuffer.wrap 创建 buffer 并写入 channel
     */
    public static void writeWithChannel(Path path, String content) throws IOException {
        try (FileChannel channel = FileChannel.open(path,
                StandardOpenOption.CREATE, StandardOpenOption.WRITE, StandardOpenOption.TRUNCATE_EXISTING)) {
            ByteBuffer buffer = ByteBuffer.wrap(content.getBytes(StandardCharsets.UTF_8));
            channel.write(buffer);
        }
    }

    /**
     * 演示 ByteBuffer 的基本操作: allocate, put, flip, get, clear
     * 返回读取到的整数值
     */
    public static int demonstrateBufferOperations() {
        ByteBuffer buffer = ByteBuffer.allocate(8); // 分配 8 字节

        // 写入模式: put 数据
        buffer.putInt(42);
        buffer.putInt(100);

        // flip: 切换到读模式
        buffer.flip();

        // 读取
        int first = buffer.getInt();
        int second = buffer.getInt();

        // clear: 重置为写模式
        buffer.clear();

        return first + second;
    }

    /**
     * 演示 compact 操作: 保留未读数据, 腾出空间继续写
     * 返回未读字节数
     */
    public static int demonstrateCompact() {
        ByteBuffer buffer = ByteBuffer.allocate(16);
        buffer.put((byte) 1);
        buffer.put((byte) 2);
        buffer.put((byte) 3);
        buffer.put((byte) 4);

        buffer.flip();
        buffer.get(); // 读取一个字节, position=1
        buffer.get(); // 读取一个字节, position=2

        buffer.compact(); // 将 position..limit 的数据移到开头, position=2

        return buffer.position(); // 2 (剩余未读的字节数)
    }
}
