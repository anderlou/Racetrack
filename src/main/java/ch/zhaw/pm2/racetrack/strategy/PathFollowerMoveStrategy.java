package ch.zhaw.pm2.racetrack.strategy;

import ch.zhaw.pm2.racetrack.exception.InvalidFileFormatException;
import ch.zhaw.pm2.racetrack.given.Direction;
import ch.zhaw.pm2.racetrack.given.PositionVector;
import ch.zhaw.pm2.racetrack.utils.Reader;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;
import java.util.Objects;

/**
 * Class PathFollowerMoveStrategy which implements class PathStrategy.
 * Determines the next move based on a file containing points on a path.
 *
 * @author Team03 - Stackoverflow
 * @version 1.0
 */
public class PathFollowerMoveStrategy extends PathStrategy {

    private PositionVector velocityVector = new PositionVector(0, 0);
    private PositionVector currentCarPosition;
    private final List<PositionVector> allCoordinates;

    /**
     * Constructs a new PathFollowerMovesStrategyObject.
     *
     * @param file          File read for strategy.
     * @param startPosition Starting position of car.
     * @throws InvalidFileFormatException if file isn't readable.
     * @throws IOException                if starting position is null.
     * @throws NullPointerException       if file is null or startPosition is null.
     */
    public PathFollowerMoveStrategy(File file, PositionVector startPosition) throws InvalidFileFormatException, IOException {
        Objects.requireNonNull(file, "file may not be null!");
        Objects.requireNonNull(startPosition, "startPosition may not be null!");

        this.currentCarPosition = startPosition;

        String[] lines = Reader.readFile(file);

        if (lines.length == 0) {
            throw new InvalidFileFormatException("The file is empty");
        }

        for (String line : lines) {
            if (!line.matches("\\(X:\\d+,\\s*Y:\\d+\\)")) {
                throw new InvalidFileFormatException("The text in the file is invalid.");
            }
        }
        this.allCoordinates = convertToPositionVector(lines);

        calculateMoves();
    }

    /**
     * This method is for testing purposes package private. It returns all the remaining moves.
     */
    Deque<Direction> getMoves() {
        return moves;
    }

    /**
     * {@inheritDoc}
     *
     * @return next direction to follow the given path, {@link Direction#NONE} if there are no more coordinates available
     */
    @Override
    public Direction nextMove() {
        Direction nextMove = moves.pollFirst();
        if (nextMove == null) {
            nextMove = Direction.NONE;
        }
        return nextMove;
    }


    private List<PositionVector> convertToPositionVector(String[] lines) {
        List<PositionVector> allCoordinates = new ArrayList<>();
        for (String line : lines) {
            String[] coordinates = line.split(",");
            int x = Integer.parseInt(coordinates[0].replaceAll("\\D", ""));
            int y = Integer.parseInt(coordinates[1].replaceAll("\\D", ""));
            allCoordinates.add(new PositionVector(x, y));
        }
        return allCoordinates;
    }

    private void calculateMoves() {
        for (PositionVector nextCoordinate : allCoordinates) {
            while (!currentCarPosition.equals(nextCoordinate)) {
                PositionVector way = nextCoordinate.subtract(currentCarPosition);
                int norm = getNorm(getMaxDistance(velocityVector).subtract(way));
                Direction move = Direction.NONE;
                for (Direction direction : Direction.values()) {
                    int newNorm = getNorm(getMaxDistance(velocityVector.add(direction.vector)).subtract(way));
                    if (newNorm < norm) {
                        norm = newNorm;
                        move = direction;
                    }
                }
                moves.add(move);
                velocityVector = velocityVector.add(move.vector);
                currentCarPosition = currentCarPosition.add(velocityVector);
            }
        }
    }

    private int getNorm(PositionVector positionVector) {
        return Math.abs(positionVector.getX()) + Math.abs(positionVector.getY());
    }

    private PositionVector getMaxDistance(PositionVector positionVector) {
        return new PositionVector(calculateSumToZero(positionVector.getX()), calculateSumToZero(positionVector.getY()));
    }
}
