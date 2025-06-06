// src/test/java/com/nexsys/util/HttpClientUtilsTest.java
package com.nexsys.util;

import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import java.net.http.*;
import java.time.Duration;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

class HttpClientUtilsTest {

    @Test
    void testGet() throws Exception {
        CompletableFuture<HttpResponse<String>> future =
                HttpClientUtils.get("http://example.com", Duration.ofSeconds(5));

        assertThat(future).isNotNull();
        // Note: Real test would mock HttpClient
    }

    @Test
    void testPostJson() throws Exception {
        Map<String, Object> body = Map.of("key", "value");

        CompletableFuture<HttpResponse<String>> future =
                HttpClientUtils.postJson("http://example.com/api", body);

        assertThat(future).isNotNull();
        // Note: Real test would mock HttpClient
    }
}