package ch.zhaw.pm2.racetrack.strategy;

import ch.zhaw.pm2.racetrack.exception.InvalidFileFormatException;
import ch.zhaw.pm2.racetrack.given.Direction;
import ch.zhaw.pm2.racetrack.utils.Reader;

import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

/**
 * Determines the next move based on a file containing a list of directions.
 */
public class MoveListStrategy implements MoveStrategy {

    private final Queue<String> nextMoves;

    /**
     * Constructs a new MoveListStrategy Object.
     *
     * @param file Is the file used.
     * @throws InvalidFileFormatException if file isn't readable.
     * @throws IOException                if invalid moves are in the file.
     */
    public MoveListStrategy(File file) throws InvalidFileFormatException, IOException {

        String[] lines = Reader.readFile(file);

        if (lines.length == 0) {
            throw new InvalidFileFormatException("The file is empty");
        }

        checkForInvalidMoves(lines);

        this.nextMoves = new LinkedList<>(List.of(lines));
    }

    private static void checkForInvalidMoves(String[] lines) throws InvalidFileFormatException {
        for (String line : lines) {
            try {
                Direction.valueOf(line);
            } catch (IllegalArgumentException e) {
                throw new InvalidFileFormatException("There are invalid moves in this file.");
            }
        }
    }

    /**
     * {@inheritDoc}
     *
     * @return next direction from move file or NONE, if no more moves are available.
     */
    @Override
    public Direction nextMove() {
        String nextMove = nextMoves.poll();

        if (nextMove == null) {
            return Direction.NONE;
        }
        return Direction.valueOf(nextMove);
    }
}
