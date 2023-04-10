package ch.zhaw.pm2.racetrack.strategy;

import ch.zhaw.pm2.racetrack.given.Direction;

import java.util.Deque;
import java.util.LinkedList;

/**
 * Superclass of {@link PathFinderStrategy} and {@link PathFollowerMoveStrategy} to
 * reduce duplicate code.
 *
 * @author Team03 - Stackoverflow
 * @version 1.0
 */
abstract class PathStrategy implements MoveStrategy {

    protected final Deque<Direction> moves = new LinkedList<>();

    protected int calculateSumToZero(int number) {
        int sum = 0;
        if (number != 0) {
            if (number > 0) {
                sum = number * (number + 1) / 2;
            } else {
                sum = (-1) * (number * (number - 1) / 2);
            }
        }
        return sum;
    }
}
