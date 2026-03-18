package util;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

// Assume all files are encoded in UTF-8
// Reads and writes UTF-8 text files as lists of lines.

public class FileManager {

    private final String fileName;

    public FileManager(String fileName) {
        this.fileName = fileName;
    }

    // ── Read ──────────────────────────────────────────────────────────

    public List<String> readLines() throws IOException {
        File file = new File(fileName);

        // Check file exists
        if (!file.exists())
            return new ArrayList<String>(); // Return empty list
        // Because the program runs first, the file hasn't been created yet — this is
        // perfectly normal. Returning an empty list is handled automatically by
        // LoadFromFile().

        // Read (all lines) the entire file, each line is a element in list
        return Files.readAllLines(file.toPath(), StandardCharsets.UTF_8);
    }

    // ── Write ─────────────────────────────────────────────────────────

    public void writeLines(List<String> lines) throws IOException {
        //
        String data = String.join("\n", lines);

        // UTF8 format
        Files.write(toPath(), data.getBytes(StandardCharsets.UTF_8));
        // NO APPEND
    }

    // ── Helper ────────────────────────────────────────────────────────

    private Path toPath() {
        return new File(fileName).toPath();
    }
}
