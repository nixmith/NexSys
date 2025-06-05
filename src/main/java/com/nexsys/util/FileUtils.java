// src/main/java/com/nexsys/util/FileUtils.java
package com.nexsys.util;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.nio.file.attribute.PosixFilePermission;
import java.nio.file.attribute.PosixFilePermissions;
import java.util.Objects;
import java.util.Set;

/**
 * Utilities for file operations.
 */
public final class FileUtils {
    private static final Set<PosixFilePermission> PRIVATE_PERMS =
            PosixFilePermissions.fromString("rw-------");
    private static final Set<PosixFilePermission> PUBLIC_PERMS =
            PosixFilePermissions.fromString("rw-r--r--");

    private FileUtils() {
        // Utility class
    }

    /**
     * Write UTF-8 content to a file atomically.
     *
     * @param path the target file path
     * @param content the content to write
     * @param privateFile whether to set private permissions (600)
     * @throws IOException if an I/O error occurs
     */
    public static void writeUtf8Atomic(Path path, String content, boolean privateFile)
            throws IOException {
        Objects.requireNonNull(path, "Path cannot be null");
        Objects.requireNonNull(content, "Content cannot be null");

        Path parent = path.getParent();
        if (parent != null) {
            Files.createDirectories(parent);
        }

        Path temp = Files.createTempFile(parent, path.getFileName().toString(), ".tmp");

        try {
            // Write to temp file
            Files.write(temp, content.getBytes(StandardCharsets.UTF_8),
                    StandardOpenOption.WRITE, StandardOpenOption.TRUNCATE_EXISTING);

            // Set permissions if on POSIX system
            if (isPosixFileSystem(temp)) {
                Files.setPosixFilePermissions(temp, privateFile ? PRIVATE_PERMS : PUBLIC_PERMS);
            }

            // Atomic move
            Files.move(temp, path, StandardCopyOption.ATOMIC_MOVE, StandardCopyOption.REPLACE_EXISTING);
        } catch (Exception e) {
            // Clean up temp file on failure
            Files.deleteIfExists(temp);
            throw e;
        }
    }

    /**
     * Write UTF-8 content to a file.
     *
     * @param path the target file path
     * @param content the content to write
     * @param privateFile whether to set private permissions (600)
     * @throws IOException if an I/O error occurs
     */
    public static void writeUtf8(Path path, String content, boolean privateFile)
            throws IOException {
        Objects.requireNonNull(path, "Path cannot be null");
        Objects.requireNonNull(content, "Content cannot be null");

        Path parent = path.getParent();
        if (parent != null) {
            Files.createDirectories(parent);
        }

        Files.write(path, content.getBytes(StandardCharsets.UTF_8),
                StandardOpenOption.CREATE, StandardOpenOption.WRITE, StandardOpenOption.TRUNCATE_EXISTING);

        // Set permissions if on POSIX system
        if (privateFile && isPosixFileSystem(path)) {
            Files.setPosixFilePermissions(path, PRIVATE_PERMS);
        }
    }

    /**
     * Read UTF-8 content from a file.
     *
     * @param path the file path
     * @return the file content
     * @throws IOException if an I/O error occurs
     */
    public static String readUtf8(Path path) throws IOException {
        Objects.requireNonNull(path, "Path cannot be null");
        return Files.readString(path, StandardCharsets.UTF_8);
    }

    /**
     * Check if the file system supports POSIX permissions.
     *
     * @param path the path to check
     * @return true if POSIX permissions are supported
     */
    private static boolean isPosixFileSystem(Path path) {
        try {
            return path.getFileSystem().supportedFileAttributeViews().contains("posix");
        } catch (Exception e) {
            return false;
        }
    }
}