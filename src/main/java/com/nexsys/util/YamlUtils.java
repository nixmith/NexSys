// src/main/java/com/nexsys/util/YamlUtils.java
package com.nexsys.util;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;
import java.util.Objects;

import org.snakeyaml.engine.v2.api.Dump;
import org.snakeyaml.engine.v2.api.DumpSettings;
import org.snakeyaml.engine.v2.api.Load;
import org.snakeyaml.engine.v2.api.LoadSettings;

/**
 * YAML utilities using SnakeYAML Engine.
 */
public final class YamlUtils {
    private YamlUtils() {
        // Utility class
    }

    /**
     * Load YAML from file.
     *
     * @param path the file path
     * @return the loaded object
     * @throws IOException if loading fails
     */
    public static Object load(Path path) throws IOException {
        Objects.requireNonNull(path, "Path cannot be null");
        String content = Files.readString(path);
        return parse(content);
    }

    /**
     * Load YAML from file and ensure it's a Map.
     *
     * @param path the file path
     * @return the loaded map
     * @throws IOException if loading fails
     * @throws IllegalArgumentException if top-level is not a map
     */
    @SuppressWarnings("unchecked")
    public static Map<String, Object> loadDict(Path path) throws IOException {
        Object result = load(path);
        if (result == null) {
            return Map.of();
        }
        if (!(result instanceof Map)) {
            throw new IllegalArgumentException("YAML file does not contain a map at top level");
        }
        return (Map<String, Object>) result;
    }

    /**
     * Parse YAML from string.
     *
     * @param yaml the YAML string
     * @return the parsed object
     */
    public static Object parse(String yaml) {
        Objects.requireNonNull(yaml, "YAML string cannot be null");
        LoadSettings settings = LoadSettings.builder().build();
        Load load = new Load(settings);
        return load.loadFromString(yaml);
    }

    /**
     * Write object to YAML file.
     *
     * @param path the target path
     * @param object the object to write
     * @throws IOException if writing fails
     */
    public static void write(Path path, Object object) throws IOException {
        Objects.requireNonNull(path, "Path cannot be null");
        String yaml = toYaml(object);
        Files.writeString(path, yaml);
    }

    /**
     * Convert object to YAML string.
     *
     * @param object the object
     * @return YAML string
     */
    public static String toYaml(Object object) {
        DumpSettings settings = DumpSettings.builder().build();
        Dump dump = new Dump(settings);
        return dump.dumpToString(object);
    }
}