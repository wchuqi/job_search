package com.javastudy.io;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;

class SocketDemoTest {

    private ServerSocket serverSocket;
    private int port;

    @BeforeEach
    void setUp() throws IOException {
        serverSocket = new ServerSocket(0); // bind to random available port
        port = serverSocket.getLocalPort();
    }

    @AfterEach
    void tearDown() throws IOException {
        if (serverSocket != null && !serverSocket.isClosed()) {
            serverSocket.close();
        }
    }

    @Test
    void sendMessage_shouldReceiveEcho() throws Exception {
        // Start server handling in background
        CompletableFuture<String> serverFuture = CompletableFuture.supplyAsync(() -> {
            try {
                return SocketDemo.acceptAndEcho(serverSocket);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });

        // Client sends message
        String response = SocketDemo.sendMessage("localhost", port, "ping");

        // Verify
        assertEquals("ping", response);
        assertEquals("ping", serverFuture.get(5, TimeUnit.SECONDS));
    }

    @Test
    void sendMessage_differentMessages_shouldEchoCorrectly() throws Exception {
        CompletableFuture<String> serverFuture = CompletableFuture.supplyAsync(() -> {
            try {
                return SocketDemo.acceptAndEcho(serverSocket);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });

        String response = SocketDemo.sendMessage("localhost", port, "Hello, Socket!");
        assertEquals("Hello, Socket!", response);
    }

    @Test
    void handleClient_shouldEchoMessage() throws Exception {
        // Use a connected pair of sockets
        try (ServerSocket ss = new ServerSocket(0)) {
            int p = ss.getLocalPort();

            CompletableFuture<String> serverFuture = CompletableFuture.supplyAsync(() -> {
                try (Socket client = ss.accept()) {
                    java.io.BufferedReader in = new java.io.BufferedReader(
                            new java.io.InputStreamReader(client.getInputStream()));
                    return in.readLine();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            });

            try (Socket client = new Socket("localhost", p);
                 var out = new java.io.PrintWriter(client.getOutputStream(), true)) {
                out.println("test message");
            }

            assertEquals("test message", serverFuture.get(5, TimeUnit.SECONDS));
        }
    }

    @Test
    void startEchoServer_shouldReturnServerSocket() throws IOException {
        try (ServerSocket ss = SocketDemo.startEchoServer(0)) {
            assertNotNull(ss);
            assertFalse(ss.isClosed());
        }
    }
}
