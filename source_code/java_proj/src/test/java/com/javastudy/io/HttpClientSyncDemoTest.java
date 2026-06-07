package com.javastudy.io;

import com.sun.net.httpserver.HttpServer;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.http.HttpResponse;
import java.time.Duration;

import static org.junit.jupiter.api.Assertions.*;

class HttpClientSyncDemoTest {

    private static HttpServer server;
    private static int port;

    @BeforeAll
    static void startServer() throws IOException {
        server = HttpServer.create(new InetSocketAddress(0), 0);
        port = server.getAddress().getPort();

        server.createContext("/hello", exchange -> {
            String response = "Hello, World!";
            exchange.getResponseHeaders().add("Content-Type", "text/plain");
            exchange.sendResponseHeaders(200, response.length());
            try (OutputStream os = exchange.getResponseBody()) {
                os.write(response.getBytes());
            }
        });

        server.createContext("/echo", exchange -> {
            if ("POST".equals(exchange.getRequestMethod())) {
                String body = new String(exchange.getRequestBody().readAllBytes());
                exchange.getResponseHeaders().add("Content-Type", "application/json");
                exchange.sendResponseHeaders(200, body.length());
                try (OutputStream os = exchange.getResponseBody()) {
                    os.write(body.getBytes());
                }
            } else {
                exchange.sendResponseHeaders(405, 0);
                exchange.getResponseBody().close();
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
    void getStatusCode_shouldReturn200() throws Exception {
        int code = HttpClientSyncDemo.getStatusCode("http://localhost:" + port + "/hello");
        assertEquals(200, code);
    }

    @Test
    void getResponseBody_shouldReturnExpectedContent() throws Exception {
        String body = HttpClientSyncDemo.getResponseBody("http://localhost:" + port + "/hello");
        assertEquals("Hello, World!", body);
    }

    @Test
    void postJson_shouldReturnEchoedBody() throws Exception {
        String json = "{\"key\":\"value\"}";
        HttpResponse<String> response = HttpClientSyncDemo.postJson(
                "http://localhost:" + port + "/echo", json);
        assertEquals(200, response.statusCode());
        assertEquals(json, response.body());
    }

    @Test
    void createClientWithTimeout_shouldNotBeNull() {
        var client = HttpClientSyncDemo.createClientWithTimeout(Duration.ofSeconds(5));
        assertNotNull(client);
    }
}
