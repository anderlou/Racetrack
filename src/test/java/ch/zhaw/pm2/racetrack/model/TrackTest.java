package ch.zhaw.pm2.racetrack.model;

import ch.zhaw.pm2.racetrack.exception.InvalidFileFormatException;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.nio.file.NoSuchFileException;

import static org.junit.jupiter.api.Assertions.assertThrows;

public class TrackTest {

    private final File tracksFolder = new File("src/test/resources/tracks");

    @Test
    public void testNonExistingFile() {
        assertThrows(NoSuchFileException.class, () -> new Track(new File("tracks/not-found.txt")));
    }

    @Test
    public void testEmptyFile() {
        assertThrows(InvalidFileFormatException.class, () -> new Track(new File(tracksFolder + "/empty.txt")));
    }
}
