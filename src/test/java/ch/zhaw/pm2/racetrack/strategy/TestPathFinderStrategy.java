package ch.zhaw.pm2.racetrack.strategy;

import ch.zhaw.pm2.racetrack.exception.InvalidFileFormatException;
import ch.zhaw.pm2.racetrack.model.Track;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertThrows;

public class TestPathFinderStrategy {

    @Test
    public void testConstructorWithNull() {
        assertThrows(NullPointerException.class, () -> new PathFinderStrategy(null, 0));
    }

    @Test
    public void testInvalidCarIndex() throws InvalidFileFormatException, IOException {
        Track track = new Track(new File("tracks/challenge.txt"));
        assertThrows(IllegalArgumentException.class, () -> new PathFinderStrategy(track, 2));
    }
}
