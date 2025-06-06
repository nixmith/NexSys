// src/main/java/com/nexsys/util/HttpClientUtils.java
package com.nexsys.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.net.URI;
import java.net.http.*;
import java.time.Duration;
import java.util.concurrent.CompletableFuture;

/**
 * HTTP client utilities.
 */
public final class HttpClientUtils {
    private static final HttpClient CLIENT = HttpClient.newBuilder()
            .connectTimeout(Duration.ofSeconds(10))
            .followRedirects(HttpClient.Redirect.NORMAL)
            .build();

    private static final ObjectMapper MAPPER = new ObjectMapper();

    private HttpClientUtils() {}

    /**
     * Perform async GET request.
     */
    public static CompletableFuture<HttpResponse<String>> get(String url, Duration timeout) {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .timeout(timeout)
                .GET()
                .build();

        return NexsysExecutor.getInstance().submitBlocking(() ->
                CLIENT.send(request, HttpResponse.BodyHandlers.ofString())
        );
    }

    /**
     * Perform async POST request with JSON body.
     */
    public static CompletableFuture<HttpResponse<String>> postJson(String url, Object body) {
        try {
            String json = MAPPER.writeValueAsString(body);

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(json))
                    .build();

            return NexsysExecutor.getInstance().submitBlocking(() ->
                    CLIENT.send(request, HttpResponse.BodyHandlers.ofString())
            );
        } catch (Exception e) {
            return CompletableFuture.failedFuture(e);
        }
    }
}