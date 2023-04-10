package ch.zhaw.pm2.racetrack.given;

/**
 * Enum representing a direction on the track grid.
 * Also representing the possible acceleration values.
 */
public enum Direction {
    DOWN_LEFT(new PositionVector(-1, 1)),
    DOWN(new PositionVector(0, 1)),
    DOWN_RIGHT(new PositionVector(1, 1)),
    LEFT(new PositionVector(-1, 0)),
    NONE(new PositionVector(0, 0)),
    RIGHT(new PositionVector(1, 0)),
    UP_LEFT(new PositionVector(-1, -1)),
    UP(new PositionVector(0, -1)),
    UP_RIGHT(new PositionVector(1, -1));

    public final PositionVector vector;

    Direction(final PositionVector vector) {
        this.vector = vector;
    }
}
