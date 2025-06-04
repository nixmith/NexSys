package com.nexsys.util;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Map;

/**
 * Unit tests for NexsysException
 */
public class NexsysExceptionTest {

    @BeforeEach
    public void setUp() {
        // Clear any MDC context before each test
        org.slf4j.MDC.clear();
    }

    @Test
    public void testDeviceNotFoundException() {
        // Given
        String deviceId = "sensor.bedroom";

        // When
        NexsysException exception = NexsysException.deviceNotFound(deviceId);

        // Then
        assertEquals(NexsysException.ErrorCategory.DEVICE, exception.getCategory());
        assertEquals(NexsysException.ErrorSeverity.ERROR, exception.getSeverity());
        assertEquals("DEVICE_NOT_FOUND", exception.getErrorCode());
        assertEquals("Device 'sensor.bedroom' not found or unavailable", exception.getMessage());

        // Verify context
        assertTrue(exception.hasContext("device_id"));
        assertEquals(deviceId, exception.getContext("device_id", String.class));

        // Verify suggestions
        assertEquals(3, exception.getSuggestions().size());
        assertTrue(exception.getSuggestions().contains("Check device connection"));
    }

    @Test
    public void testValidationError() {
        // Given
        String field = "temperature";
        String reason = "Value must be between -50 and 150";

        // When
        NexsysException exception = NexsysException.validationError(field, reason);

        // Then
        assertEquals(NexsysException.ErrorCategory.VALIDATION, exception.getCategory());
        assertEquals("VALIDATION_FAILED", exception.getErrorCode());
        assertEquals("temperature", exception.getContext("field", String.class));
        assertEquals(reason, exception.getContext("reason", String.class));
    }

    @Test
    public void testAuthenticationError() {
        // Given
        String user = "john.doe";
        String reason = "Invalid password";

        // When
        NexsysException exception = NexsysException.authenticationError(user, reason);

        // Then
        assertEquals(NexsysException.ErrorCategory.SECURITY, exception.getCategory());
        assertEquals(NexsysException.ErrorSeverity.WARNING, exception.getSeverity());
        assertEquals("AUTHENTICATION_FAILED", exception.getErrorCode());
    }

    @Test
    public void testBuilderPattern() {
        // Given
        Exception cause = new RuntimeException("Database connection failed");

        // When
        NexsysException exception = NexsysException.builder()
                .message("Custom error message")
                .category(NexsysException.ErrorCategory.STORAGE)
                .severity(NexsysException.ErrorSeverity.CRITICAL)
                .errorCode("DB_CONNECTION_LOST")
                .context("database", "nexsys_db")
                .context("host", "localhost:5432")
                .suggestion("Check database server status")
                .suggestion("Verify network connectivity")
                .cause(cause)
                .build();

        // Then
        assertEquals("Custom error message", exception.getMessage());
        assertEquals(NexsysException.ErrorCategory.STORAGE, exception.getCategory());
        assertEquals(NexsysException.ErrorSeverity.CRITICAL, exception.getSeverity());
        assertEquals("DB_CONNECTION_LOST", exception.getErrorCode());
        assertEquals(cause, exception.getCause());

        // Verify context
        assertEquals("nexsys_db", exception.getContext("database", String.class));
        assertEquals("localhost:5432", exception.getContext("host", String.class));
        assertNull(exception.getContext("missing", String.class));
        assertEquals("default", exception.getContext("missing", String.class, "default"));

        // Verify suggestions
        assertEquals(2, exception.getSuggestions().size());
    }

    @Test
    public void testContextMap() {
        // Given
        Map<String, Object> context = Map.of(
                "integration", "mqtt",
                "broker", "192.168.1.100",
                "port", 1883
        );

        // When
        NexsysException exception = NexsysException.builder()
                .message("MQTT connection failed")
                .category(NexsysException.ErrorCategory.INTEGRATION)
                .errorCode("MQTT_CONNECTION_FAILED")
                .context(context)
                .build();

        // Then
        assertEquals("mqtt", exception.getContext("integration", String.class));
        assertEquals("192.168.1.100", exception.getContext("broker", String.class));
        assertEquals(1883, exception.getContext("port", Integer.class));
    }

    @Test
    public void testNetworkErrorWithCause() {
        // Given
        String endpoint = "http://api.weather.com/v1/current";
        Exception cause = new java.net.SocketTimeoutException("Read timed out");

        // When
        NexsysException exception = NexsysException.networkError(endpoint, cause);

        // Then
        assertEquals(NexsysException.ErrorCategory.NETWORK, exception.getCategory());
        assertEquals("NETWORK_CONNECTION_FAILED", exception.getErrorCode());
        assertEquals(endpoint, exception.getContext("endpoint", String.class));
        assertEquals(cause, exception.getCause());
        assertTrue(exception.getMessage().contains(endpoint));
    }

    @Test
    public void testDetailedErrorReport() {
        // Given
        NexsysException exception = NexsysException.builder()
                .message("Test error")
                .category(NexsysException.ErrorCategory.DEVICE)
                .severity(NexsysException.ErrorSeverity.ERROR)
                .errorCode("TEST_ERROR")
                .context("device_id", "switch.kitchen")
                .context("state", "unavailable")
                .suggestion("Check device power")
                .build();

        // When
        String report = exception.getDetailedErrorReport();

        // Then
        assertTrue(report.contains("Nexsys Exception Report"));
        assertTrue(report.contains("Category: Device communication"));
        assertTrue(report.contains("Error Code: TEST_ERROR"));
        assertTrue(report.contains("device_id: switch.kitchen"));
        assertTrue(report.contains("state: unavailable"));
        assertTrue(report.contains("Check device power"));
    }

    @Test
    public void testImmutableCollections() {
        // Given
        NexsysException exception = NexsysException.deviceNotFound("test.device");

        // When/Then - Verify collections are immutable
        assertThrows(UnsupportedOperationException.class, () -> {
            exception.getErrorContext().put("new_key", "value");
        });

        assertThrows(UnsupportedOperationException.class, () -> {
            exception.getSuggestions().add("New suggestion");
        });
    }

    @Test
    public void testSystemError() {
        // Given
        String message = "Critical system failure";
        Exception cause = new Exception("Heap space");

        // When
        NexsysException exception = NexsysException.systemError(message, cause);

        // Then
        assertEquals(NexsysException.ErrorCategory.SYSTEM, exception.getCategory());
        assertEquals(NexsysException.ErrorSeverity.CRITICAL, exception.getSeverity());
        assertEquals("SYSTEM_ERROR", exception.getErrorCode());
        assertEquals(message, exception.getMessage());
        assertEquals(cause, exception.getCause());
    }
}