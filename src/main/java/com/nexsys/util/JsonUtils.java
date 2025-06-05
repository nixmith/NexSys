// src/main/java/com/nexsys/util/JsonUtils.java
package com.nexsys.util;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Objects;

import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * JSON utilities using Jackson.
 */
public final class JsonUtils {
    // Will be initialized once Jackson is available
    private static final ObjectMapper mapper = new ObjectMapper();

    private JsonUtils() {
        // Utility class
    }

    /**
     * Parse JSON from string.
     *
     * @param json the JSON string
     * @return parsed JsonNode
     * @throws IOException if parsing fails
     */
    public static Object parse(String json) throws IOException {
        Objects.requireNonNull(json, "JSON string cannot be null");
        return mapper.readTree(json);
    }

    /**
     * Parse JSON from bytes.
     *
     * @param json the JSON bytes
     * @return parsed JsonNode
     * @throws IOException if parsing fails
     */
    public static Object parse(byte[] json) throws IOException {
        Objects.requireNonNull(json, "JSON bytes cannot be null");
        return mapper.readTree(json);
    }

    /**
     * Write object to JSON file.
     *
     * @param path the target path
     * @param object the object to write
     * @throws IOException if writing fails
     */
    public static void write(Path path, Object object) throws IOException {
        Objects.requireNonNull(path, "Path cannot be null");
        mapper.writeValue(path.toFile(), object);
    }

    /**
     * Read JSON file into object.
     *
     * @param path the source path
     * @param type the target type
     * @param <T> the type parameter
     * @return the deserialized object
     * @throws IOException if reading fails
     */
    public static <T> T read(Path path, Class<T> type) throws IOException {
        Objects.requireNonNull(path, "Path cannot be null");
        Objects.requireNonNull(type, "Type cannot be null");
        return mapper.readValue(path.toFile(), type);
    }

    /**
     * Convert object to JSON string.
     *
     * @param object the object
     * @return JSON string
     * @throws IOException if serialization fails
     */
    public static String toJson(Object object) throws IOException {
        return mapper.writeValueAsString(object);
    }

    /**
     * Convert object to pretty-printed JSON string.
     *
     * @param object the object
     * @return pretty JSON string
     * @throws IOException if serialization fails
     */
    public static String toPrettyJson(Object object) throws IOException {
        return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(object);
    }
}