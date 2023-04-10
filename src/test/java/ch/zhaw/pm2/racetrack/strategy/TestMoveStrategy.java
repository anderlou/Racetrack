package ch.zhaw.pm2.racetrack.strategy;

import ch.zhaw.pm2.racetrack.core.Game;
import ch.zhaw.pm2.racetrack.exception.InvalidFileFormatException;
import ch.zhaw.pm2.racetrack.given.Direction;
import ch.zhaw.pm2.racetrack.model.Track;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class TestMoveStrategy {

    private final File movesFolder = new File("src/test/resources/moves");

    private Game game;

    @BeforeEach
    public void setUp() throws InvalidFileFormatException, IOException {
        game = new Game(new Track(new File("tracks/quarter-mile.txt")));
    }

    /**
     * This is a negativ test and of the equivalence class 9. The test checks if the strategy can not be set when File null.
     */
    @Test
    public void testCarMoveStrategyWhenFileNull() {
        assertThrows(NullPointerException.class, () -> game.setCarMoveStrategy(0, new MoveListStrategy(null)));
    }

    /**
     * This is a negativ test and of the equivalence class 9. The test checks if the strategy can not be set when Strategy null.
     */
    @Test
    public void testCarMoveStrategyWhenStrategyNull() throws InvalidFileFormatException, IOException {
        Game game = new Game(new Track(new File("tracks/quarter-mile.txt")));
        assertThrows(NullPointerException.class, () -> game.setCarMoveStrategy(0, null));
    }

    /**
     * This is a negativ test and of the equivalence class 9. The test checks if the strategy can not be set when car count is wrong.
     */
    @Test
    public void testCarMoveStrategyWhenCarCountWrong() throws InvalidFileFormatException, IOException {
        Game game = new Game(new Track(new File("tracks/quarter-mile.txt")));
        assertThrows(IllegalStateException.class, () -> game.setCarMoveStrategy(10, new MoveListStrategy(new File(movesFolder + "/test-moves.txt"))));
    }

    /**
     * This is a negativ test and of the equivalence class 9. The test checks if the strategy can not be set when the file is empty.
     */
    @Test
    public void testCarMoveStrategyWhenFileIsEmpty() throws InvalidFileFormatException, IOException {
        Game game = new Game(new Track(new File("tracks/quarter-mile.txt")));
        assertThrows(InvalidFileFormatException.class, () -> game.setCarMoveStrategy(0, new MoveListStrategy(new File(movesFolder + "/test-empty.txt"))));
    }

    /**
     * This is a positive test and of the equivalence class 8. The test checks if the DoNotMoveStrategy can be set.
     */
    @Test
    public void testCarDontMoveStrategy() {
        assertDoesNotThrow(() -> game.setCarMoveStrategy(0, new DoNotMoveStrategy()));
    }

    /**
     * This is a positive test and of the equivalence class 8. The test checks if the MoveListStrategy can be set.
     */
    @Test
    public void testCarMoveListStrategy1() {
        assertDoesNotThrow(() -> game.setCarMoveStrategy(0, new MoveListStrategy(new File(movesFolder + "/test-moves.txt"))));
    }

    /**
     * This is a positive test and of the equivalence class 17. The test checks if the MoveListStrategy returns the correct moves.
     */
    @Test
    public void testCarMoveListStrategy2() throws InvalidFileFormatException, IOException {
        MoveStrategy moveStrategy = new MoveListStrategy(new File(movesFolder + "/test-moves.txt"));

        game.setCarMoveStrategy(0, moveStrategy);

        assertEquals(Direction.RIGHT, moveStrategy.nextMove());
        assertEquals(Direction.RIGHT, moveStrategy.nextMove());
        assertEquals(Direction.RIGHT, moveStrategy.nextMove());
        assertEquals(Direction.DOWN_LEFT, moveStrategy.nextMove());
        assertEquals(Direction.DOWN_RIGHT, moveStrategy.nextMove());
        assertEquals(Direction.LEFT, moveStrategy.nextMove());
        assertEquals(Direction.DOWN_RIGHT, moveStrategy.nextMove());
        assertEquals(Direction.UP, moveStrategy.nextMove());
    }
}
