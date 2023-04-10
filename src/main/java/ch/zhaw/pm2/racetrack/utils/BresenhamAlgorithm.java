package ch.zhaw.pm2.racetrack.utils;

import ch.zhaw.pm2.racetrack.given.PositionVector;

import java.util.ArrayList;
import java.util.List;

/**
 * The Bresenham algorithm is an algorithm used to draw a line between two given points in a
 * 2D plane. Given two position, the algorithm works by finding the points that the line between
 * the given points should pass through.
 *
 * @author Team03 - Stackoverflow
 * @version 1.0
 */
public final class BresenhamAlgorithm {

    private final PositionVector startPosition;
    private final PositionVector endPosition;

    /**
     * Constructs a new instance of the Bresenham algorithm using the start point and the end point.
     *
     * @param startPosition The starting point as a {@link PositionVector}.
     * @param endPosition   The ending point as a {@link PositionVector}.
     */
    public BresenhamAlgorithm(PositionVector startPosition, PositionVector endPosition) {
        this.startPosition = startPosition;
        this.endPosition = endPosition;
    }

    /**
     * Calculates the path between the given points and returns a list of points that are
     * crossed by the line.
     *
     * @return A list of {@link PositionVector} that are crossed by the line between two
     * given points.
     */
    public List<PositionVector> calculatePath() {
        List<PositionVector> path = new ArrayList<>();
        // Use Bresenham's algorithm to determine positions.
        // Relative Distance (x & y-axis) between end- and starting position
        int differenceX = endPosition.getX() - startPosition.getX();
        int differenceY = endPosition.getY() - startPosition.getY();

        // Absolute distance (x & y-axis) between end- and starting position
        int distanceX = Math.abs(differenceX);
        int distanceY = Math.abs(differenceY);

        // Direction of vector on x & y axis (-1: to left/down, 0: none, +1 : to right/up)
        int directionX = Integer.signum(differenceX);
        int directionY = Integer.signum(differenceY);

        // Determine which axis is the fast direction and set parallel/diagonal step values
        int parallelStepX, parallelStepY;
        int diagonalStepX, diagonalStepY;
        int distanceSlowAxis, distanceFastAxis;

        if (distanceX > distanceY) {
            // x-axis is the 'fast' direction
            parallelStepX = directionX;
            parallelStepY = 0; // parallel step only moves in x direction
            diagonalStepX = directionX;
            diagonalStepY = directionY; // diagonal step moves in both directions
            distanceSlowAxis = distanceY;
            distanceFastAxis = distanceX;
        } else {
            // y-axis is the 'fast' direction
            parallelStepX = 0;  // parallel step only moves in y direction
            parallelStepY = directionY;// parallel step only moves in y direction
            diagonalStepX = directionX;
            diagonalStepY = directionY;// diagonal step moves in both directions
            distanceSlowAxis = distanceX;
            distanceFastAxis = distanceY;
        }

        // initialize path loop
        int x = startPosition.getX();
        int y = startPosition.getY();
        int error = distanceFastAxis / 2; // set to half distance to get a good starting value

        path.add(new PositionVector(x, y));
        // path loop:
        // by default step parallel to the fast axis.
        // if error value gets negative take a diagonal step
        // this happens approximately every (distanceFastAxis / distanceSlowAxis) steps

        for (int step = 0; step < distanceFastAxis; step++) {
            error -= distanceSlowAxis; // update error value
            if (error < 0) {
                error += distanceFastAxis;// correct error value to be positive again
                // step into slow direction; diagonal step
                x += diagonalStepX;
                y += diagonalStepY;
            } else {
                // step into slow direction; diagonal step
                x += parallelStepX;
                y += parallelStepY;
            }
            path.add(new PositionVector(x, y));
        }
        return path;
    }
}
