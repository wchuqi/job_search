package com.javastudy.io;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * 知识点：Socket 网络编程基础
 * Socket 连接, PrintWriter 发送数据, BufferedReader 接收数据
 */
public class SocketDemo {

    /**
     * 创建一个简单的 echo 服务器 (在指定端口监听, 回显收到的消息)
     * 返回 ServerSocket 供测试关闭
     */
    public static ServerSocket startEchoServer(int port) throws IOException {
        ServerSocket serverSocket = new ServerSocket(port);
        // 在实际使用中, 服务器会持续运行; 这里仅用于演示
        return serverSocket;
    }

    /**
     * 客户端: 连接到服务器, 发送一条消息, 读取响应
     */
    public static String sendMessage(String host, int port, String message) throws IOException {
        try (Socket socket = new Socket(host, port);
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
             BufferedReader in = new BufferedReader(
                     new InputStreamReader(socket.getInputStream()))) {
            out.println(message);
            return in.readLine();
        }
    }

    /**
     * 处理单个客户端连接 (服务器端): 读取一行并回显
     */
    public static void handleClient(Socket clientSocket) throws IOException {
        try (BufferedReader in = new BufferedReader(
                new InputStreamReader(clientSocket.getInputStream()));
             PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true)) {
            String line = in.readLine();
            if (line != null) {
                out.println(line); // echo 回显
            }
        }
    }

    /**
     * 服务器端接受一个连接并处理 (阻塞)
     */
    public static String acceptAndEcho(ServerSocket serverSocket) throws IOException {
        try (Socket clientSocket = serverSocket.accept();
             BufferedReader in = new BufferedReader(
                     new InputStreamReader(clientSocket.getInputStream()));
             PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true)) {
            String line = in.readLine();
            if (line != null) {
                out.println(line);
                return line;
            }
            return null;
        }
    }
}
