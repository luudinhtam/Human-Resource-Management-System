package util;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

/**
 * Generic file manager using Java NIO.
 * Reads and writes UTF-8 text files as lists of lines.
 */
public class FileManager {

    private final String fileName;

    public FileManager(String fileName) {
        this.fileName = fileName;
    }

    // ── Read ──────────────────────────────────────────────────────────

    /**
     * Reads all lines from file.
     * Returns empty list if file does not exist yet.
     *
     * @throws IOException if file exists but cannot be read
     */
    public List<String> readLines() throws IOException {
        File file = new File(fileName);

        // Check file exists
        if (!file.exists())
            return new ArrayList<String>(); // Return empty list
        // Because the program runs first, the file hasn't been created yet — this is
        // perfectly normal. Returning an empty list is handled automatically by
        // LoadFromFile().

        // Read the entire file, each line is a element in list
        return Files.readAllLines(file.toPath(), StandardCharsets.UTF_8);
    }

    // ── Write ─────────────────────────────────────────────────────────

    /**
     * Writes a list of lines to file, each separated by newline.
     * Creates parent directories automatically if they don't exist.
     *
     * @throws IOException if file cannot be written
     */
    public void writeLines(List<String> lines) throws IOException {
        // Check the file exists
        ensureDirectory();

        //
        String data = String.join("\n", lines);
        
        // UTF8 format
        Files.write(toPath(), data.getBytes(StandardCharsets.UTF_8));
        // NO APPEND
    }

    /**
     * Writes raw string content to file.
     * Creates parent directories automatically if they don't exist.
     *
     * @throws IOException if file cannot be written
     */
    public void writeContent(String content) throws IOException {
        //
        ensureDirectory();

        //
        Files.write(toPath(), content.getBytes(StandardCharsets.UTF_8));
    }

    // ── Helper ────────────────────────────────────────────────────────

    public String getFileName() {
        return fileName;
    }

    private Path toPath() {
        return new File(fileName).toPath();
    }

    private void ensureDirectory() throws IOException {
        File parent = new File(fileName).getParentFile();
        if (parent != null && !parent.exists()) {
            if (!parent.mkdirs()) {
                throw new IOException("Cannot create directory: " + parent.getPath());
            }
        }
    }
}
