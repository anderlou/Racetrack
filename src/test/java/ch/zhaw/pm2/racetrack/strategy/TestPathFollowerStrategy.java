package ch.zhaw.pm2.racetrack.strategy;

import ch.zhaw.pm2.racetrack.core.Game;
import ch.zhaw.pm2.racetrack.exception.InvalidFileFormatException;
import ch.zhaw.pm2.racetrack.given.Direction;
import ch.zhaw.pm2.racetrack.given.PositionVector;
import ch.zhaw.pm2.racetrack.model.Track;
import ch.zhaw.pm2.racetrack.utils.Reader;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Test class for the {@link PathFollowerMoveStrategy} class.<br>
 *
 * <br>This test class has the following equivalence classes:<br>
 *
 * <br>01. PathFollowerCanBeCreated: Tests if the pathFollower can be build under specific conditions.
 * <br>02. PathFollowerCanNotBeCreated: Tests if the pathFollower can not be build under specific conditions.
 * <br>03. PathWorks: Tests if the pathFollower works under specific conditions.
 * <br>04. PathDoesNotWork: Tests if the pathFollower does not work under specific conditions.
 * <br>05. NextMoveWithoutMoves: Tests if the Direction is NONE, when no moves are left.
 *
 * @author StackOverflow
 * @version 1.0
 */

public class TestPathFollowerStrategy {

    private final File followerFolder = new File("src/test/resources/follower");

    private Game game;

    @BeforeEach
    public void setUP() throws InvalidFileFormatException, IOException {
        game = new Game(new Track(new File("tracks/challenge.txt")));
    }

    /**
     * This is a positive and of the equivalence class 5. The test checks if the Direction with null in movement is None.
     */
    @Test
    public void testNextMoveIsNull() throws InvalidFileFormatException, IOException {
        File file = new File(followerFolder + "/only-one-line.txt");
        PositionVector currentPosition = game.getCarPosition(0);
        PathFollowerMoveStrategy pathFollower = new PathFollowerMoveStrategy(file, currentPosition);
        Deque<Direction> moves = pathFollower.getMoves();

        while (!moves.isEmpty()) {
            pathFollower.nextMove();
        }
        assertEquals(Direction.NONE, pathFollower.nextMove());

    }

    /**
     * This is a positive and of the equivalence class 3. The test checks if the points are reached.
     */
    @Test
    public void testPathFollowerChallengeFile() throws InvalidFileFormatException, IOException {
        File file = new File(followerFolder + "/challenge.txt");
        PositionVector currentPosition = game.getCarPosition(0);
        PositionVector currentVelocity = new PositionVector(0, 0);
        PathFollowerMoveStrategy pathFollower = new PathFollowerMoveStrategy(file, currentPosition);

        String[] pointsToTangle = Reader.readFile(file);
        List<PositionVector> allPositionsToTangle = new ArrayList<>();
        Deque<Direction> moves = pathFollower.getMoves();

        List<PositionVector> allPointsGiven = fileExportToPositionVector(pointsToTangle);

        while (!moves.isEmpty()) {
            currentVelocity = currentVelocity.add(pathFollower.nextMove().vector);
            currentPosition = currentPosition.add(currentVelocity);
            allPositionsToTangle.add(currentPosition);
        }

        for (PositionVector point : allPointsGiven) {
            assertTrue(allPositionsToTangle.contains(point));
        }

    }

    /**
     * This is a negative test and of the equivalence class 2. The constructor should throw an {@link NullPointerException} if the file is null.
     */
    @Test
    public void testConstructorWithNull() {
        PositionVector startPosition = new PositionVector(26, 20);
        assertThrows(NullPointerException.class, () -> new PathFollowerMoveStrategy(null, startPosition));
    }

    /**
     * This is a negative test and of the equivalence class 2. The constructor should throw an {@link NullPointerException} if the position is null.
     */
    @Test
    public void testStartPositionNull() {
        File file = new File(followerFolder + "/challenge.txt");
        assertThrows(NullPointerException.class, () -> new PathFollowerMoveStrategy(file, null));
    }

    /**
     * This is a positive test and of the equivalence class 1. The constructor should not throw an exception if only one line in the file.
     */
    @Test
    public void testOnlyOneLine() {
        PositionVector startPosition = new PositionVector(1, 5);
        File fileOnlyOneLine = new File(followerFolder + "/only-one-line.txt");
        assertDoesNotThrow(() -> new PathFollowerMoveStrategy(fileOnlyOneLine, startPosition));
    }

    /**
     * This is a positive test and of the equivalence class 1. The constructor should throw an {@link InvalidFileFormatException} if the invalid parameter value null is passed.
     */
    @Test
    public void testFileWithoutParentheses() {
        PositionVector startPosition = new PositionVector(1, 5);
        File fileWithoutParentheses = new File(followerFolder + "/without-parentheses.txt");
        assertThrows(InvalidFileFormatException.class, () -> new PathFollowerMoveStrategy(fileWithoutParentheses, startPosition));
    }

    /**
     * This is a negative test and of the equivalence class 2. The constructor should throw an {@link IllegalArgumentException} if the file contains special characters.
     */
    @Test
    public void testFileWithSpecialCharacters() {
        PositionVector startPosition = new PositionVector(1, 5);
        File fileWithSpecialCharacters = new File(followerFolder + "/special-character.txt");
        assertThrows(InvalidFileFormatException.class, () -> new PathFollowerMoveStrategy(fileWithSpecialCharacters, startPosition));
    }

    /**
     * This is a negative test and of the equivalence class 2. The constructor should throw an {@link InvalidFileFormatException} if the file is empty.
     */
    @Test
    public void testFileEmpty() {
        PositionVector startPosition = new PositionVector(1, 5);
        File fileEmpty = new File(followerFolder + "/empty.txt");
        assertThrows(InvalidFileFormatException.class, () -> new PathFollowerMoveStrategy(fileEmpty, startPosition));
    }

    /**
     * This is a positive test and of the equivalence class 3. The test checks if the pathFollower works with positions more than once in the file. {@link InvalidFileFormatException if the file isn't valid} {@link IOException if no more moves are detected.}
     */
    @Test
    public void testSamePoints() throws InvalidFileFormatException, IOException {
        File file = new File(followerFolder + "/same-points.txt");
        PositionVector currentPosition = game.getCarPosition(0);
        PositionVector currentVelocity = new PositionVector(0, 0);
        PathFollowerMoveStrategy pathFollower = new PathFollowerMoveStrategy(file, currentPosition);

        String[] pointsToTangle = Reader.readFile(file);
        List<PositionVector> allPositionsToTangle = new ArrayList<>();
        Deque<Direction> moves = pathFollower.getMoves();

        List<PositionVector> allPointsGiven = fileExportToPositionVector(pointsToTangle);

        while (!moves.isEmpty()) {
            currentVelocity = currentVelocity.add(pathFollower.nextMove().vector);
            currentPosition = currentPosition.add(currentVelocity);
            allPositionsToTangle.add(currentPosition);
        }

        for (PositionVector point : allPointsGiven) {
            assertTrue(allPositionsToTangle.contains(point));
        }
    }


    private List<PositionVector> fileExportToPositionVector(String[] pointsToTangle) {
        List<PositionVector> allPointsGiven = new ArrayList<>();
        for (String line : pointsToTangle) {
            String[] coordinates = line.split(",");
            int x = Integer.parseInt(coordinates[0].replaceAll("\\D", ""));
            int y = Integer.parseInt(coordinates[1].replaceAll("\\D", ""));
            allPointsGiven.add(new PositionVector(x, y));
        }
        return allPointsGiven;
    }

    /**
     * This is a positive test and of the equivalence class 3. The test checks if the pathFollower works with big leaps between the points.
     */
    @Test
    public void testPathDifficult() throws InvalidFileFormatException, IOException {
        File file = new File(followerFolder + "/zick-zack.txt");
        PositionVector currentPosition = game.getCarPosition(0);
        PositionVector currentVelocity = new PositionVector(0, 0);
        PathFollowerMoveStrategy pathFollower = new PathFollowerMoveStrategy(file, currentPosition);

        String[] pointsToTangle = Reader.readFile(file);
        List<PositionVector> allPositionsToTangle = new ArrayList<>();
        Deque<Direction> moves = pathFollower.getMoves();

        List<PositionVector> allPointsGiven = fileExportToPositionVector(pointsToTangle);

        while (!moves.isEmpty()) {
            currentVelocity = currentVelocity.add(pathFollower.nextMove().vector);
            currentPosition = currentPosition.add(currentVelocity);
            allPositionsToTangle.add(currentPosition);
        }

        for (PositionVector point : allPointsGiven) {
            assertTrue(allPositionsToTangle.contains(point));
        }
    }
}