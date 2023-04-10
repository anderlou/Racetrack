package ch.zhaw.pm2.racetrack.utils;

import ch.zhaw.pm2.racetrack.given.PositionVector;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * Test class for the Bresenham class.
 *
 * @author StackOverflow
 * @version 1.0
 */
public class BresenhamTest {

    @Test
    public void basicTest() {
        PositionVector start = new PositionVector(1, 1);
        PositionVector end = new PositionVector(11, 5);

        List<PositionVector> expectedPath = List.of(
                new PositionVector(1, 1),
                new PositionVector(2, 1),
                new PositionVector(3, 2),
                new PositionVector(4, 2),
                new PositionVector(5, 3),
                new PositionVector(6, 3),
                new PositionVector(7, 3),
                new PositionVector(8, 4),
                new PositionVector(9, 4),
                new PositionVector(10, 5),
                new PositionVector(11, 5)
        );

        BresenhamAlgorithm algorithm = new BresenhamAlgorithm(start, end);
        List<PositionVector> path = algorithm.calculatePath();

        assertNotNull(path);
        assertFalse(path.isEmpty());
        assertEquals(expectedPath, path);
    }
}
