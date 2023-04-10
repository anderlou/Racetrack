package ch.zhaw.pm2.racetrack.given;

import ch.zhaw.pm2.racetrack.model.Car;

/**
 * Interface representing the mandatory functions of a car on the racetrack.<br/>
 * IMPORTANT: This interface shall not be altered!<br/>
 * It specifies elements we use to test Racetrack for grading.<br/>
 * You may change or extend the default implementation provided in
 * {@link Car}<br/>
 * Full Javadoc can be found in the implementation file.
 */
public interface CarSpecification {

    char getId();

    PositionVector getCurrentPosition();

    PositionVector getVelocity();

    PositionVector getNextPosition();

    void accelerate(Direction acceleration);

    void move();

    void crash(PositionVector crashPosition);

    boolean isCrashed();

}
