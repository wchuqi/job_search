package com.javastudy.io;

import com.sun.net.httpserver.HttpServer;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.http.HttpResponse;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;

class HttpClientAsyncDemoTest {

    private static HttpServer server;
    private static int port;

    @BeforeAll
    static void startServer() throws IOException {
        server = HttpServer.create(new InetSocketAddress(0), 0);
        port = server.getAddress().getPort();

        server.createContext("/async-hello", exchange -> {
            String response = "Async Hello!";
            exchange.sendResponseHeaders(200, response.length());
            try (OutputStream os = exchange.getResponseBody()) {
                os.write(response.getBytes());
            }
        });

        server.start();
    }

    @AfterAll
    static void stopServer() {
        if (server != null) {
            server.stop(0);
        }
    }

    @Test
    void sendAsyncGet_shouldReturnResponse() throws Exception {
        CompletableFuture<HttpResponse<String>> future =
                HttpClientAsyncDemo.sendAsyncGet("http://localhost:" + port + "/async-hello");

        HttpResponse<String> response = future.get(5, TimeUnit.SECONDS);
        assertEquals(200, response.statusCode());
        assertEquals("Async Hello!", response.body());
    }

    @Test
    void getStatusCodeAsync_shouldReturn200() throws Exception {
        CompletableFuture<Integer> future =
                HttpClientAsyncDemo.getStatusCodeAsync("http://localhost:" + port + "/async-hello");

        int code = future.get(5, TimeUnit.SECONDS);
        assertEquals(200, code);
    }

    @Test
    void getBodyUpperCaseAsync_shouldReturnUpperCase() throws Exception {
        CompletableFuture<String> future =
                HttpClientAsyncDemo.getBodyUpperCaseAsync("http://localhost:" + port + "/async-hello");

        String body = future.get(5, TimeUnit.SECONDS);
        assertEquals("ASYNC HELLO!", body);
    }

    @Test
    void getBodyWithFallback_success_shouldReturnBody() throws Exception {
        CompletableFuture<String> future =
                HttpClientAsyncDemo.getBodyWithFallback("http://localhost:" + port + "/async-hello");

        String body = future.get(5, TimeUnit.SECONDS);
        assertEquals("Async Hello!", body);
    }

    @Test
    void getBodyWithFallback_failure_shouldReturnErrorMessage() throws Exception {
        CompletableFuture<String> future =
                HttpClientAsyncDemo.getBodyWithFallback("http://localhost:1/invalid");

        String body = future.get(5, TimeUnit.SECONDS);
        assertTrue(body.startsWith("Error:"));
    }

    @Test
    void getBodyLengthAsync_shouldReturnCorrectLength() throws Exception {
        CompletableFuture<Integer> future =
                HttpClientAsyncDemo.getBodyLengthAsync("http://localhost:" + port + "/async-hello");

        int length = future.get(5, TimeUnit.SECONDS);
        assertEquals("Async Hello!".length(), length);
    }
}
