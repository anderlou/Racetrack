package ch.zhaw.pm2.racetrack.given;

import ch.zhaw.pm2.racetrack.core.Game;
import ch.zhaw.pm2.racetrack.strategy.MoveStrategy;

import java.util.List;

/**
 * Interface representing the mandatory functions of the Game controller class.<br/>
 * IMPORTANT: This interface shall not be altered!<br/>
 * It specifies elements we use to test Racetrack for grading.<br/>
 * You may change or extend the default implementation provided in
 * {@link Game}<br/>
 * Full Javadoc can be found in the implementation file.
 */
public interface GameSpecification {

    int getCarCount();

    int getCurrentCarIndex();

    char getCarId(int carIndex);

    PositionVector getCarPosition(int carIndex);

    PositionVector getCarVelocity(int carIndex);

    MoveStrategy getCarMoveStrategy(int carIndex);

    void setCarMoveStrategy(int carIndex, MoveStrategy carMoveStrategy);

    int getWinner();

    void doCarTurn(Direction acceleration);

    void switchToNextActiveCar();

    List<PositionVector> calculatePath(PositionVector startPosition, PositionVector endPosition);

}
