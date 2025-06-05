// src/test/java/com/nexsys/util/NetworkUtilsTest.java
package com.nexsys.util;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import static org.assertj.core.api.Assertions.*;

class NetworkUtilsTest {

    @ParameterizedTest
    @ValueSource(strings = {"127.0.0.1", "::1", "localhost"})
    void testIsLoopback(String address) {
        assertThat(NetworkUtils.isLoopback(address)).isTrue();
    }

    @ParameterizedTest
    @ValueSource(strings = {"192.168.1.1", "10.0.0.1", "172.16.0.1"})
    void testIsPrivate(String address) {
        assertThat(NetworkUtils.isPrivate(address)).isTrue();
    }

    @ParameterizedTest
    @ValueSource(strings = {"8.8.8.8", "1.1.1.1"})
    void testIsNotPrivate(String address) {
        assertThat(NetworkUtils.isPrivate(address)).isFalse();
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "192.168.1.1",
            "10.0.0.1",
            "2001:db8::1",
            "::1",
            "255.255.255.255"
    })
    void testIsIpAddress(String address) {
        assertThat(NetworkUtils.isIpAddress(address)).isTrue();
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "not-an-ip",
            "256.256.256.256",
            "",
            "google.com"
    })
    void testIsNotIpAddress(String address) {
        assertThat(NetworkUtils.isIpAddress(address)).isFalse();
    }

    @Test
    void testIsIpv4Address() {
        assertThat(NetworkUtils.isIpv4Address("192.168.1.1")).isTrue();
        assertThat(NetworkUtils.isIpv4Address("255.255.255.255")).isTrue();
        assertThat(NetworkUtils.isIpv4Address("::1")).isFalse();
        assertThat(NetworkUtils.isIpv4Address("not-an-ip")).isFalse();
    }

    @Test
    void testIsIpv6Address() {
        assertThat(NetworkUtils.isIpv6Address("::1")).isTrue();
        assertThat(NetworkUtils.isIpv6Address("2001:db8::1")).isTrue();
        assertThat(NetworkUtils.isIpv6Address("192.168.1.1")).isFalse();
    }

    @Test
    void testNormalizeUrl() {
        // Remove default ports
        assertThat(NetworkUtils.normalizeUrl("http://example.com:80/"))
                .isEqualTo("http://example.com");
        assertThat(NetworkUtils.normalizeUrl("https://example.com:443/"))
                .isEqualTo("https://example.com");

        // Keep non-default ports
        assertThat(NetworkUtils.normalizeUrl("http://example.com:8080/"))
                .isEqualTo("http://example.com:8080");

        // Remove trailing slash
        assertThat(NetworkUtils.normalizeUrl("http://example.com/path/"))
                .isEqualTo("http://example.com/path");

        // Keep root slash
        assertThat(NetworkUtils.normalizeUrl("http://example.com/"))
                .isEqualTo("http://example.com");
    }
}