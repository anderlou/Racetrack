package ch.zhaw.pm2.racetrack.strategy;

import ch.zhaw.pm2.racetrack.core.UserInterface;
import ch.zhaw.pm2.racetrack.given.Direction;

/**
 * Let the user decide the next move.
 *
 * @author Team03 - Stackoverflow
 * @version 1.0
 */
public class UserMoveStrategy implements MoveStrategy {

    private final UserInterface userInterface;

    /**
     * Constructs a UserMoveStrategy Object.
     *
     * @param userInterface Is the used Userinterface.
     */
    public UserMoveStrategy(UserInterface userInterface) {
        this.userInterface = userInterface;
    }

    /**
     * {@inheritDoc}
     * Asks the user for the direction vector.
     *
     * @return next direction, null if the user terminates the game.
     */
    @Override
    public Direction nextMove() {
        return userInterface.inputVelocityDirection();
    }
}
