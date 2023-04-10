package ch.zhaw.pm2.racetrack.model;

import ch.zhaw.pm2.racetrack.given.CarSpecification;
import ch.zhaw.pm2.racetrack.given.Direction;
import ch.zhaw.pm2.racetrack.given.PositionVector;

import java.util.Objects;

/**
 * Class representing a car on the racetrack.<br/>
 * Uses {@link PositionVector} to store current position on the track grid and current velocity vector.<br/>
 * Each car has an identifier character which represents the car on the racetrack board.<br/>
 * Also keeps the state, if the car is crashed (not active anymore). The state can not be changed back to not crashed.<br/>
 * The velocity is changed by providing an acceleration vector.<br/>
 * The car is able to calculate the endpoint of its next position and on request moves to it.<br/>
 *
 * @author Team03 - Stackoverflow
 * @version 1.0
 */
public class Car implements CarSpecification {

    /**
     * Car identifier used to represent the car on the track
     */
    private final char id;
    private PositionVector currentPosition;
    private PositionVector velocity = new PositionVector(0, 0);
    private boolean isCrashed = false;

    /**
     * Constructor for class Car
     *
     * @param id            unique Car identification
     * @param startPosition initial position of the Car
     * @throws NullPointerException if startPosition is null.
     */
    public Car(char id, PositionVector startPosition) {
        Objects.requireNonNull(startPosition, "startPosition in Car Constructor may not be null.");
        this.id = id;
        this.currentPosition = startPosition;
    }

    /**
     * Returns Identifier of the car, which represents the car on the track
     *
     * @return identifier character
     */
    @Override
    public char getId() {
        return this.id;
    }

    /**
     * Returns a copy of the current position of the car on the track as a {@link PositionVector}
     *
     * @return copy of the car's current position
     */
    @Override
    public PositionVector getCurrentPosition() {
        return new PositionVector(currentPosition);
    }

    /**
     * Returns a copy of the velocity vector of the car as a {@link PositionVector}<br/>
     * It should not be possible to change the cars velocity vector using this return value.
     *
     * @return copy of car's velocity vector
     */
    @Override
    public PositionVector getVelocity() {
        return new PositionVector(velocity);
    }

    /**
     * Return the position that will apply after the next move at the current velocity.
     * Does not complete the move, so the current position remains unchanged.
     *
     * @return Expected position after the next move
     */
    @Override
    public PositionVector getNextPosition() {
        return currentPosition.add(velocity);
    }

    /**
     * Add the specified amounts to this car's velocity.<br/>
     * The only acceleration values allowed are -1, 0 or 1 in both axis<br/>
     * There are 9 possible acceleration vectors, which are defined in {@link Direction}.<br/>
     * Changes only velocity, not position.<br/>
     *
     * @param acceleration A Direction vector containing the amounts to add to the velocity in x and y dimension
     * @throws NullPointerException if acceleration direction is null.
     */
    @Override
    public void accelerate(Direction acceleration) {
        Objects.requireNonNull(acceleration, "The acceleration direction may not be null");
        velocity = velocity.add(acceleration.vector);
    }

    /**
     * Update this Car's position based on its current velocity.
     */
    @Override
    public void move() {
        currentPosition = currentPosition.add(velocity);
    }

    /**
     * Mark this Car as being crashed at the given position.
     *
     * @param crashPosition position the car crashed.
     * @throws NullPointerException if crashPosition is null.
     */
    @Override
    public void crash(PositionVector crashPosition) {
        Objects.requireNonNull(crashPosition, "The crashPosition may not be null");
        isCrashed = true;
        currentPosition = crashPosition;
    }

    /**
     * Returns whether this Car has been marked as crashed.
     *
     * @return Returns true if crash() has been called on this Car, false otherwise.
     */
    @Override
    public boolean isCrashed() {
        return isCrashed;
    }

    /**
     * Sets the current position to the winning position. It can then be accessed
     * with the {@link Car#getCurrentPosition()} method.
     *
     * @param winningPosition A {@link PositionVector} being the position where the car won.
     * @throws NullPointerException if winningPosition is null.
     */
    public void setWinningPosition(PositionVector winningPosition) {
        Objects.requireNonNull(winningPosition, "The winningPosition may not be null");
        currentPosition = winningPosition;
    }
}
