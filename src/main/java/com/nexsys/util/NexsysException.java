package com.nexsys.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;

import java.util.*;

/**
 * Base exception class for Nexsys system errors.
 *
 * Provides:
 * - Categorized error types for consistent handling
 * - Severity levels for prioritization
 * - Contextual information for debugging
 * - Suggestion system for error resolution
 * - MDC-aware logging for better observability
 */
public class NexsysException extends Exception {

    private static final Logger LOGGER = LoggerFactory.getLogger(NexsysException.class);

    // Core fields
    private final String cachedMessage;
    private final ErrorCategory category;
    private final ErrorSeverity severity;
    private final String errorCode;
    private final Map<String, Object> errorContext;
    private final List<String> suggestions;

    /**
     * Error categories for systematic handling
     */
    public enum ErrorCategory {
        CONFIGURATION("configuration", "Configuration and setup errors"),
        INTEGRATION("integration", "Integration and module errors"),
        DEVICE("device", "Device communication and control errors"),
        AUTOMATION("automation", "Automation and rule execution errors"),
        SECURITY("security", "Authentication and authorization errors"),
        SYSTEM("system", "Core system and infrastructure errors"),
        VALIDATION("validation", "Data validation and schema errors"),
        NETWORK("network", "Network communication errors"),
        STORAGE("storage", "Data persistence and storage errors"),
        API("api", "API and service interface errors");

        private final String code;
        private final String description;

        ErrorCategory(String code, String description) {
            this.code = code;
            this.description = description;
        }

        public String getCode() { return code; }
        public String getDescription() { return description; }
    }

    /**
     * Error severity levels for prioritization and handling
     */
    public enum ErrorSeverity {
        CRITICAL("critical", "System-critical errors requiring immediate attention"),
        ERROR("error", "Standard errors that prevent normal operation"),
        WARNING("warning", "Warnings that may indicate potential issues"),
        INFO("info", "Informational errors for debugging and monitoring");

        private final String level;
        private final String description;

        ErrorSeverity(String level, String description) {
            this.level = level;
            this.description = description;
        }

        public String getLevel() { return level; }
        public String getDescription() { return description; }
    }

    /**
     * Main constructor with all fields
     */
    protected NexsysException(String cachedMessage,
                              ErrorCategory category,
                              ErrorSeverity severity,
                              String errorCode,
                              Map<String, Object> errorContext,
                              List<String> suggestions,
                              Throwable cause) {
        super(cachedMessage, cause);
        this.cachedMessage = cachedMessage != null ? cachedMessage : "Unknown error occurred";
        this.category = category != null ? category : ErrorCategory.SYSTEM;
        this.severity = severity != null ? severity : ErrorSeverity.ERROR;
        this.errorCode = errorCode != null ? errorCode : "UNKNOWN_ERROR";
        this.errorContext = errorContext != null ?
                Collections.unmodifiableMap(new HashMap<>(errorContext)) : Collections.emptyMap();
        this.suggestions = suggestions != null ?
                Collections.unmodifiableList(new ArrayList<>(suggestions)) : Collections.emptyList();
    }

    /**
     * Builder pattern for creating complex exceptions
     */
    public static class Builder {
        private String message;
        private ErrorCategory category = ErrorCategory.SYSTEM;
        private ErrorSeverity severity = ErrorSeverity.ERROR;
        private String errorCode = "UNKNOWN_ERROR";
        private Map<String, Object> errorContext = new HashMap<>();
        private List<String> suggestions = new ArrayList<>();
        private Throwable cause;

        public Builder message(String message) {
            this.message = message;
            return this;
        }

        public Builder category(ErrorCategory category) {
            this.category = category;
            return this;
        }

        public Builder severity(ErrorSeverity severity) {
            this.severity = severity;
            return this;
        }

        public Builder errorCode(String errorCode) {
            this.errorCode = errorCode;
            return this;
        }

        public Builder context(String key, Object value) {
            this.errorContext.put(key, value);
            return this;
        }

        public Builder context(Map<String, Object> context) {
            this.errorContext.putAll(context);
            return this;
        }

        public Builder suggestion(String suggestion) {
            this.suggestions.add(suggestion);
            return this;
        }

        public Builder suggestions(List<String> suggestions) {
            this.suggestions.addAll(suggestions);
            return this;
        }

        public Builder cause(Throwable cause) {
            this.cause = cause;
            return this;
        }

        public NexsysException build() {
            return new NexsysException(message, category, severity, errorCode,
                    errorContext, suggestions, cause);
        }
    }

    /**
     * Create a new builder instance
     */
    public static Builder builder() {
        return new Builder();
    }

    // Getters
    public ErrorCategory getCategory() { return category; }
    public ErrorSeverity getSeverity() { return severity; }
    public String getErrorCode() { return errorCode; }
    public Map<String, Object> getErrorContext() { return errorContext; }
    public List<String> getSuggestions() { return suggestions; }

    /**
     * Check if this exception has a specific context key
     */
    public boolean hasContext(String key) {
        return errorContext.containsKey(key);
    }

    /**
     * Get context value by key
     */
    @SuppressWarnings("unchecked")
    public <T> T getContext(String key, Class<T> type) {
        Object value = errorContext.get(key);
        if (value != null && type.isInstance(value)) {
            return (T) value;
        }
        return null;
    }

    /**
     * Get context value with default
     */
    public <T> T getContext(String key, Class<T> type, T defaultValue) {
        T value = getContext(key, type);
        return value != null ? value : defaultValue;
    }

    /**
     * Log this exception with MDC context
     */
    public void logException() {
        logException(LOGGER);
    }

    /**
     * Log this exception to a specific logger with MDC context
     */
    public void logException(Logger targetLogger) {
        try {
            // Set MDC context
            MDC.put("errorCode", errorCode);
            MDC.put("errorCategory", category.getCode());
            MDC.put("errorSeverity", severity.getLevel());

            // Add error context to MDC
            errorContext.forEach((key, value) ->
                    MDC.put("error." + key, String.valueOf(value)));

            // Build log message
            String message = buildLogMessage();

            // Log based on severity
            switch (severity) {
                case CRITICAL:
                    targetLogger.error("CRITICAL: {}", message, this);
                    break;
                case ERROR:
                    targetLogger.error(message, this);
                    break;
                case WARNING:
                    targetLogger.warn(message, this);
                    break;
                case INFO:
                    targetLogger.info(message);
                    break;
            }
        } finally {
            // Always clear MDC to prevent context leakage
            MDC.clear();
        }
    }

    /**
     * Build log message with context and suggestions
     */
    private String buildLogMessage() {
        StringBuilder sb = new StringBuilder();
        sb.append(getMessage());

        if (!errorContext.isEmpty()) {
            sb.append(" | Context: ");
            errorContext.forEach((k, v) ->
                    sb.append(k).append("=").append(v).append(" "));
        }

        if (!suggestions.isEmpty()) {
            sb.append(" | Suggestions: ");
            sb.append(String.join("; ", suggestions));
        }

        return sb.toString();
    }

    /**
     * Create a detailed error report
     */
    public String getDetailedErrorReport() {
        StringBuilder report = new StringBuilder();

        report.append("Nexsys Exception Report\n");
        report.append("======================\n");
        report.append("Category: ").append(category.getDescription()).append("\n");
        report.append("Severity: ").append(severity.getDescription()).append("\n");
        report.append("Error Code: ").append(errorCode).append("\n");
        report.append("Message: ").append(getMessage()).append("\n");

        if (!errorContext.isEmpty()) {
            report.append("Error Context:\n");
            errorContext.forEach((key, value) ->
                    report.append("  ").append(key).append(": ").append(value).append("\n"));
        }

        if (!suggestions.isEmpty()) {
            report.append("Suggestions:\n");
            suggestions.forEach(suggestion ->
                    report.append("  - ").append(suggestion).append("\n"));
        }

        if (getCause() != null) {
            report.append("Caused by: ").append(getCause().getClass().getSimpleName())
                    .append(": ").append(getCause().getMessage()).append("\n");
        }

        return report.toString();
    }

    // Factory methods for common exception types

    public static NexsysException configurationError(String message, String domain) {
        return builder()
                .message(message)
                .category(ErrorCategory.CONFIGURATION)
                .errorCode("CONFIGURATION_ERROR")
                .context("domain", domain)
                .suggestion("Check configuration file syntax")
                .suggestion("Verify configuration parameters")
                .build();
    }

    public static NexsysException integrationError(String integration, String reason, Throwable cause) {
        return builder()
                .message("Failed to set up integration '" + integration + "': " + reason)
                .category(ErrorCategory.INTEGRATION)
                .errorCode("INTEGRATION_SETUP_FAILED")
                .context("integration", integration)
                .context("reason", reason)
                .cause(cause)
                .suggestion("Check integration documentation")
                .suggestion("Verify integration dependencies")
                .build();
    }

    public static NexsysException deviceNotFound(String deviceId) {
        return builder()
                .message("Device '" + deviceId + "' not found or unavailable")
                .category(ErrorCategory.DEVICE)
                .errorCode("DEVICE_NOT_FOUND")
                .context("device_id", deviceId)
                .suggestion("Check device connection")
                .suggestion("Verify device configuration")
                .suggestion("Check device power status")
                .build();
    }

    public static NexsysException validationError(String field, String reason) {
        return builder()
                .message("Validation failed for '" + field + "': " + reason)
                .category(ErrorCategory.VALIDATION)
                .errorCode("VALIDATION_FAILED")
                .context("field", field)
                .context("reason", reason)
                .suggestion("Check field format and constraints")
                .suggestion("Refer to documentation for valid values")
                .build();
    }

    public static NexsysException authenticationError(String user, String reason) {
        return builder()
                .message("Authentication failed for user '" + user + "': " + reason)
                .category(ErrorCategory.SECURITY)
                .severity(ErrorSeverity.WARNING)
                .errorCode("AUTHENTICATION_FAILED")
                .context("user", user)
                .context("reason", reason)
                .suggestion("Check username and password")
                .suggestion("Verify account status")
                .suggestion("Check authentication configuration")
                .build();
    }

    public static NexsysException networkError(String endpoint, Throwable cause) {
        return builder()
                .message("Network connection failed: " + endpoint)
                .category(ErrorCategory.NETWORK)
                .errorCode("NETWORK_CONNECTION_FAILED")
                .context("endpoint", endpoint)
                .cause(cause)
                .suggestion("Check network connectivity")
                .suggestion("Verify endpoint availability")
                .suggestion("Check firewall settings")
                .build();
    }

    public static NexsysException automationError(String automationId, String error) {
        return builder()
                .message("Automation '" + automationId + "' failed: " + error)
                .category(ErrorCategory.AUTOMATION)
                .errorCode("AUTOMATION_EXECUTION_FAILED")
                .context("automation_id", automationId)
                .context("error", error)
                .suggestion("Check automation configuration")
                .suggestion("Verify trigger conditions")
                .suggestion("Check action validity")
                .build();
    }

    public static NexsysException storageError(String operation, Throwable cause) {
        return builder()
                .message("Storage operation failed: " + operation)
                .category(ErrorCategory.STORAGE)
                .errorCode("STORAGE_OPERATION_FAILED")
                .context("operation", operation)
                .cause(cause)
                .suggestion("Check disk space")
                .suggestion("Verify file permissions")
                .suggestion("Check database connectivity")
                .build();
    }

    public static NexsysException apiError(String endpoint, String message) {
        return builder()
                .message("API error in '" + endpoint + "': " + message)
                .category(ErrorCategory.API)
                .errorCode("API_ERROR")
                .context("endpoint", endpoint)
                .context("error_message", message)
                .suggestion("Check API documentation")
                .suggestion("Verify request parameters")
                .suggestion("Check API rate limits")
                .build();
    }

    public static NexsysException systemError(String message, Throwable cause) {
        return builder()
                .message(message)
                .category(ErrorCategory.SYSTEM)
                .severity(ErrorSeverity.CRITICAL)
                .errorCode("SYSTEM_ERROR")
                .cause(cause)
                .suggestion("Check system logs")
                .suggestion("Verify system resources")
                .build();
    }
}