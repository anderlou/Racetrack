package ch.zhaw.pm2.racetrack.utils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

/**
 * A utility class for reading files.
 *
 * @author Team03 - Stackoverflow
 * @version 1.0
 */
public final class Reader {

    private Reader() {
    }

    /**
     * Reads a given file and returns its lines.
     *
     * @param file File to read the content of.
     * @return A String array containing the individual lines.
     * @throws IOException If an I/O error occurs while reading the file.
     */
    public static String[] readFile(File file) throws IOException {
        return Files.readAllLines(file.toPath()).stream()
                .filter(s -> !s.isBlank())
                .toList()
                .toArray(new String[0]);
    }
}
