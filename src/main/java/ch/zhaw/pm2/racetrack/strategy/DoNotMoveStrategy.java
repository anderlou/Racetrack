package ch.zhaw.pm2.racetrack.strategy;

import ch.zhaw.pm2.racetrack.given.Direction;

/**
 * Class DoNotMoveStrategy which implements class MoveStrategy.
 * Defines Strategy where car doesn't move.
 *
 * @author Team03 - Stackoverflow
 * @version 1.0
 */
public class DoNotMoveStrategy implements MoveStrategy {
    /**
     * {@inheritDoc}
     *
     * @return always {@link Direction#NONE}
     */
    @Override
    public Direction nextMove() {
        return Direction.NONE;
    }
}
