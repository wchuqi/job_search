package com.javastudy.io;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.concurrent.CompletableFuture;

/**
 * 知识点：HttpClient 异步请求 (JDK 11+)
 * sendAsync 返回 CompletableFuture, 支持 thenApply 链式处理
 */
public class HttpClientAsyncDemo {

    /**
     * sendAsync 发送异步请求, 返回 CompletableFuture
     */
    public static CompletableFuture<HttpResponse<String>> sendAsyncGet(String url) {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .GET()
                .build();
        return client.sendAsync(request, HttpResponse.BodyHandlers.ofString());
    }

    /**
     * 使用 thenApply 提取状态码
     */
    public static CompletableFuture<Integer> getStatusCodeAsync(String url) {
        return sendAsyncGet(url)
                .thenApply(HttpResponse::statusCode);
    }

    /**
     * 使用 thenApply 提取响应体并转换为大写
     */
    public static CompletableFuture<String> getBodyUpperCaseAsync(String url) {
        return sendAsyncGet(url)
                .thenApply(HttpResponse::body)
                .thenApply(String::toUpperCase);
    }

    /**
     * 使用 exceptionally 处理异常
     */
    public static CompletableFuture<String> getBodyWithFallback(String url) {
        return sendAsyncGet(url)
                .thenApply(HttpResponse::body)
                .exceptionally(ex -> "Error: " + ex.getMessage());
    }

    /**
     * 链式组合: 获取响应 -> 提取body -> 计算长度
     */
    public static CompletableFuture<Integer> getBodyLengthAsync(String url) {
        return sendAsyncGet(url)
                .thenApply(HttpResponse::body)
                .thenApply(String::length);
    }
}
