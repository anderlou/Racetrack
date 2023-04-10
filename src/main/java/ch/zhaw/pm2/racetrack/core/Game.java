package ch.zhaw.pm2.racetrack.core;

import ch.zhaw.pm2.racetrack.given.Direction;
import ch.zhaw.pm2.racetrack.given.GameSpecification;
import ch.zhaw.pm2.racetrack.given.PositionVector;
import ch.zhaw.pm2.racetrack.given.SpaceType;
import ch.zhaw.pm2.racetrack.model.Car;
import ch.zhaw.pm2.racetrack.model.Track;
import ch.zhaw.pm2.racetrack.strategy.DoNotMoveStrategy;
import ch.zhaw.pm2.racetrack.strategy.MoveStrategy;
import ch.zhaw.pm2.racetrack.utils.BresenhamAlgorithm;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * Game controller class, performing all actions to modify the game state.
 * It contains the logic to switch and move the cars, detect if they are crashed
 * and if we have a winner.
 *
 * @author StackOverflow
 * @version 1.0
 */
public class Game implements GameSpecification {
    /**
     * Value representing, that the game is still running, and we have no winner
     */
    public static final int NO_WINNER = -1;
    private Car winningCar = null;

    private final List<String> cheating;

    private final Track track;
    private int currentCarIndex;
    private final Map<Integer, MoveStrategy> strategyMap = new HashMap<>();

    /**
     * Constructs a new Game object.
     *
     * @param track Is the track used in the game.
     * @throws NullPointerException if track is null.
     */
    public Game(Track track) {
        Objects.requireNonNull(track, "The track object in the game constructor may not be null");
        this.track = track;
        cheating = new ArrayList<>();
        currentCarIndex = 0;
    }

    /**
     * Return the number of cars.
     *
     * @return Number of cars
     */
    @Override
    public int getCarCount() {
        return track.getCarCount();
    }

    /**
     * Return the index of the current active car.
     * Car indexes are zero-based, so the first car is 0, and the last car is getCarCount() - 1.
     *
     * @return The zero-based number of the current car
     */
    @Override
    public int getCurrentCarIndex() {
        return currentCarIndex;
    }

    /**
     * Get the id of the specified car.
     *
     * @param carIndex The zero-based carIndex number
     * @return A char containing the id of the car
     */
    @Override
    public char getCarId(int carIndex) {
        throwExceptionIfCarIndexInvalid(carIndex);
        return track.getCar(carIndex).getId();
    }

    /**
     * Get the position of the specified car.
     *
     * @param carIndex The zero-based carIndex number
     * @return A PositionVector containing the car's current position
     */
    @Override
    public PositionVector getCarPosition(int carIndex) {
        throwExceptionIfCarIndexInvalid(carIndex);
        return track.getCar(carIndex).getCurrentPosition();
    }

    /**
     * Get the velocity of the specified car.
     *
     * @param carIndex The zero-based carIndex number
     * @return A PositionVector containing the car's current velocity
     */
    @Override
    public PositionVector getCarVelocity(int carIndex) {
        return track.getCar(carIndex).getVelocity();
    }

    /**
     * Set the {@link MoveStrategy} for the specified car.
     *
     * @param carIndex        The zero-based carIndex number.
     * @param carMoveStrategy The {@link MoveStrategy} to be associated with the specified car.
     * @throws NullPointerException if carMoveStrategy is null.
     */
    @Override
    public void setCarMoveStrategy(int carIndex, MoveStrategy carMoveStrategy) {
        Objects.requireNonNull(carMoveStrategy, "The carMoveStrategy may not be null");
        throwExceptionIfCarIndexInvalid(carIndex);
        strategyMap.put(carIndex, carMoveStrategy);
    }

    /**
     * Get the {@link MoveStrategy} of the specified car.
     *
     * @param carIndex The zero-based carIndex number
     * @return The {@link MoveStrategy} associated with the specified car
     */
    @Override
    public MoveStrategy getCarMoveStrategy(int carIndex) {
        throwExceptionIfCarIndexInvalid(carIndex);
        return strategyMap.get(carIndex);
    }

    /**
     * Return the carIndex of the winner.<br/>
     * If the game is still in progress, returns {@link #NO_WINNER}.
     *
     * @return The winning car's index (zero-based, see {@link #getCurrentCarIndex()}),
     * or {@link #NO_WINNER} if the game is still in progress
     */
    @Override
    public int getWinner() {
        int winningIndex = NO_WINNER;
        for (int i = 0; i < track.getCarCount(); i++) {
            if (track.getCar(i).equals(winningCar)) {
                winningIndex = i;
            }
        }
        return winningIndex;
    }

    /**
     * Execute the next turn for the current active car.
     * <p>This method changes the current car's velocity and checks on the path to the next position,
     * if it crashes (car state to crashed) or passes the finish line in the right direction (set winner state).</p>
     * <p>The steps are as follows</p>
     * <ol>
     *   <li>Accelerate the current car</li>
     *   <li>Calculate the path from current (start) to next (end) position
     *       (see {@link Game#calculatePath(PositionVector, PositionVector)})</li>
     *   <li>Verify for each step what space type it hits:
     *      <ul>
     *          <li>TRACK: check for collision with other car (crashed &amp; don't continue), otherwise do nothing</li>
     *          <li>WALL: car did collide with the wall - crashed &amp; don't continue</li>
     *          <li>FINISH_*: car hits the finish line - wins only if it crosses the line in the correct direction</li>
     *      </ul>
     *   </li>
     *   <li>If the car crashed or wins, set its position to the crash/win coordinates</li>
     *   <li>If the car crashed, also detect if there is only one car remaining, remaining car is the winner</li>
     *   <li>Otherwise move the car to the end position</li>
     * </ol>
     * <p>The calling method must check the winner state and decide how to go on. If the winner is different
     * than {@link Game#NO_WINNER}, or the current car is already marked as crashed the method returns immediately.</p>
     *
     * @param acceleration A Direction containing the current cars acceleration vector (-1,0,1) in x and y direction
     *                     for this turn
     * @throws NullPointerException if acceleration is null.
     */
    @Override
    public void doCarTurn(Direction acceleration) {
        Objects.requireNonNull(acceleration, "The acceleration direction may not be null");

        Car currentCar = track.getCar(currentCarIndex);
        currentCar.accelerate(acceleration);

        if (isCarMoving(getCarVelocity(currentCarIndex))) {
            checkPath(currentCar);
        }

        if (!currentCar.isCrashed() && winningCar == null) {
            currentCar.move();
        } else if (currentCar.isCrashed()) {
            checkRemainingCars();
        }
    }

    private boolean isCarMoving(PositionVector velocity) {
        Objects.requireNonNull(velocity, "The velocity may not be null");
        return !velocity.equals(new PositionVector(0, 0));
    }

    private void checkPath(Car currentCar) {
        Objects.requireNonNull(currentCar, "The currentCar may not be null");
        PositionVector startPosition = currentCar.getCurrentPosition();
        PositionVector endPosition = currentCar.getNextPosition();

        List<PositionVector> path = calculatePath(startPosition, endPosition);

        PositionVector previous = path.get(0);

        Iterator<PositionVector> iterator = path.iterator();
        boolean pathInterrupted = false;

        while (!pathInterrupted && iterator.hasNext()) {
            PositionVector currentPosition = iterator.next();
            SpaceType spaceType = track.getSpaceTypeAtPosition(currentPosition);
            switch (spaceType) {
                case WALL -> {
                    currentCar.crash(currentPosition);
                    pathInterrupted = true;
                }
                case TRACK -> {
                    if (checkCrashedOtherCar(currentPosition)) {
                        currentCar.crash(currentPosition);
                        pathInterrupted = true;
                    }
                }

                case FINISH_DOWN, FINISH_LEFT, FINISH_RIGHT, FINISH_UP -> {
                    boolean finished = checkFinishedInCorrectDirection(spaceType, previous, currentPosition);
                    String id = String.valueOf(currentCar.getId());
                    if (finished) {
                        if (!cheating.contains(id)) {
                            winningCar = currentCar;
                            currentCar.setWinningPosition(currentPosition);
                            pathInterrupted = true;
                        } else {
                            cheating.remove(id);
                        }

                    } else {
                        if (!cheating.contains(id)) {
                            cheating.add(id);
                        }
                    }
                }

            }
            previous = currentPosition;
        }
    }

    private void checkRemainingCars() {
        int remainingCars = 0;
        int currentCar = 0;

        while (currentCar < track.getCarCount() && remainingCars <= 1) {
            Car car = track.getCar(currentCar);
            if (!car.isCrashed()) {
                remainingCars++;
                winningCar = car;
                if (remainingCars > 1) {
                    winningCar = null;
                }
            }
            currentCar++;
        }
    }

    /**
     * This method checks if all active cars have the {@link MoveStrategy} {@link DoNotMoveStrategy}.
     *
     * @return true if that is the case.
     */
    public boolean onlyCarsWithDoNotMoveStrategyRemaining() {
        boolean onlyCarsWithDoNotMoveStrategyRemaining = true;
        for (int i = 0; i < track.getCarCount(); i++) {
            Car currentCar = track.getCar(i);
            if (!currentCar.isCrashed() && !(getCarMoveStrategy(i) instanceof DoNotMoveStrategy)) {
                onlyCarsWithDoNotMoveStrategyRemaining = false;
            }
        }
        return onlyCarsWithDoNotMoveStrategyRemaining;
    }

    private boolean checkFinishedInCorrectDirection(SpaceType spaceType, PositionVector previousPosition, PositionVector currentPosition) {
        return switch (spaceType) {
            case FINISH_DOWN -> previousPosition.getY() < currentPosition.getY();
            case FINISH_LEFT -> previousPosition.getX() > currentPosition.getX();
            case FINISH_RIGHT -> previousPosition.getX() < currentPosition.getX();
            case FINISH_UP -> previousPosition.getY() > currentPosition.getY();
            default -> false;
        };
    }

    private boolean checkCrashedOtherCar(PositionVector nextPosition) {
        for (int i = 0; i < track.getCarCount(); i++) {
            if (i == currentCarIndex || track.getCar(i).isCrashed()) {
                continue;
            }
            Car otherCar = track.getCar(i);
            if (nextPosition.equals(otherCar.getCurrentPosition())) {
                return true;
            }
        }
        return false;
    }

    /**
     * Switches to the next car who is still in the game. Skips crashed cars.
     */
    @Override
    public void switchToNextActiveCar() {
        int nextCarIndex = getNextCarIndex(currentCarIndex);
        while (track.getCar(nextCarIndex).isCrashed()) {
            nextCarIndex = getNextCarIndex(nextCarIndex);
        }
        currentCarIndex = nextCarIndex;
    }

    private int getNextCarIndex(int currentCarIndex) {
        int nextCarIndex = currentCarIndex;
        if (currentCarIndex == track.getCarCount() - 1) {
            nextCarIndex = 0;
        } else {
            nextCarIndex++;
        }
        return nextCarIndex;
    }

    /**
     * Returns all the grid positions in the path between two positions, for use in determining line of sight. <br>
     * Determine the 'pixels/positions' on a raster/grid using Bresenham's line algorithm.
     * (<a href="https://de.wikipedia.org/wiki/Bresenham-Algorithmus">Wikipedia</a>)<br>
     * Basic steps are <ul>
     * <li>Detect which axis of the distance vector is longer (faster movement)</li>
     * <li>for each pixel on the 'faster' axis calculate the position on the 'slower' axis.</li>
     * </ul>
     * Direction of the movement has to correctly considered.
     *
     * @param startPosition Starting position as a PositionVector
     * @param endPosition   Ending position as a PositionVector
     * @return Intervening grid positions as a List of PositionVector's, including the starting and ending positions.
     * @throws NullPointerException if parameter value is null.
     */
    @Override
    public List<PositionVector> calculatePath(PositionVector startPosition, PositionVector endPosition) {
        Objects.requireNonNull(startPosition, "startPosition may not be null.");
        Objects.requireNonNull(endPosition, "endPosition may not be null.");

        BresenhamAlgorithm bresenham = new BresenhamAlgorithm(startPosition, endPosition);
        return bresenham.calculatePath();
    }

    private void throwExceptionIfCarIndexInvalid(int carIndex) {
        if (carIndex < 0 || carIndex > track.getCarCount()) {
            throw new IllegalStateException("The car index is invalid");
        }
    }
}
