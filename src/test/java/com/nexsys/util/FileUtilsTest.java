// src/test/java/com/nexsys/util/FileUtilsTest.java
package com.nexsys.util;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import java.io.IOException;
import java.nio.file.*;
import static org.assertj.core.api.Assertions.*;

class FileUtilsTest {

    @Test
    void testWriteUtf8Atomic(@TempDir Path tempDir) throws IOException {
        Path file = tempDir.resolve("test.txt");
        String content = "Hello, World!";

        FileUtils.writeUtf8Atomic(file, content, false);

        assertThat(file).exists();
        assertThat(Files.readString(file)).isEqualTo(content);

        // Verify no temp files left
        try (var files = Files.list(tempDir)) {
            assertThat(files.filter(p -> p.toString().contains(".tmp"))).isEmpty();
        }
    }

    @Test
    void testAtomicWriteCreatesParentDirs(@TempDir Path tempDir) throws IOException {
        Path file = tempDir.resolve("subdir/deep/test.txt");
        String content = "Test content";

        FileUtils.writeUtf8Atomic(file, content, false);

        assertThat(file).exists();
        assertThat(Files.readString(file)).isEqualTo(content);
    }

    @Test
    void testWriteUtf8(@TempDir Path tempDir) throws IOException {
        Path file = tempDir.resolve("simple.txt");
        String content = "Simple write";

        FileUtils.writeUtf8(file, content, false);

        assertThat(file).exists();
        assertThat(FileUtils.readUtf8(file)).isEqualTo(content);
    }

    @Test
    void testOverwriteExisting(@TempDir Path tempDir) throws IOException {
        Path file = tempDir.resolve("overwrite.txt");

        FileUtils.writeUtf8(file, "original", false);
        FileUtils.writeUtf8Atomic(file, "replaced", false);

        assertThat(FileUtils.readUtf8(file)).isEqualTo("replaced");
    }
}