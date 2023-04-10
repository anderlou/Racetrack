package ch.zhaw.pm2.racetrack.core;

import ch.zhaw.pm2.racetrack.exception.InvalidFileFormatException;
import ch.zhaw.pm2.racetrack.given.Direction;
import ch.zhaw.pm2.racetrack.given.PositionVector;
import ch.zhaw.pm2.racetrack.given.SpaceType;
import ch.zhaw.pm2.racetrack.model.Car;
import ch.zhaw.pm2.racetrack.model.Track;
import ch.zhaw.pm2.racetrack.strategy.DoNotMoveStrategy;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Test class for the {@link Game} class.<br>
 *
 * <br>This test class has the following equivalence classes:<br>
 *
 * <br>01. GameCanBeCreated: Tests if the game can be build under specific conditions.
 * <br>02. GameCanNotBeCreated: Tests if the game can not be build under specific conditions.
 * <br>03. CarCountWorks: Tests if the amount of cars is wright.
 * <br>04. CarCountDoNotWork: Tests if not possible car counts can not be build.
 * <br>06. CarPositionIsWright: Tests if the car position is wright under specific conditions.
 * <br>07. CarPositionIsWrong: Tests if the car position is wrong under specific conditions.
 * <br>08. StrategyCanBeCreated: Tests if the strategy can be build under specific conditions.
 * <br>09. StrategyCanNotBeCreated: Tests if the game can not be build under specific conditions.
 * <br>10. CarIsCrashed: Tests if the car is crashed after some actions.
 * <br>11. CarIsNotCrashed: Tests if the car is not crashed after some actions.
 * <br>12. CarWins: Tests if the car wins after some actions.
 * <br>13. CarDoesNotWin: Tests if the car does not win after some actions.
 * <br>14. CarIdIsValid: Tests if the car id can be created under specific conditions.
 * <br>15. CarIdIsInvalid: Tests if the car id can not be created under specific conditions.
 * <br>16. CarVelocityGetter: Tests if the car id can be created under specific conditions.
 * <br>17. StrategyWorks: Tests if the car id can not be created under specific conditions.
 * <br>18. SwitchingWorks: Tests if the current car can be switched under specific conditions.
 * <br>19. calculatePathParameterInvalid: Tests if the calculatePath throws an exception.
 * <br>20. ScoreTie: Tests if a ScoreTie is achieved.
 *
 * @author StackOverflow
 * @version 1.0
 */
public class GameTest {

    private final File tracksFolder = new File("src/test/resources/tracks");

    private Track trackOvalClock;
    private Game gameOvalClock;
    private Car carOvalClockIndex0;
    private Car carOvalClockIndex1;

    private Track trackQuarterMile;
    private Game gameQuarterMile;
    private Car carQuarterMileIndex0;

    private Game gameOvalAnticlockRight;

    private Track trackWith5Cars;
    private Game gameWith5Cars;

    private Game gameChallenge;

    /**
     * Sets up a new game.
     *
     * @throws InvalidFileFormatException if file is not valid.
     * @throws IOException                if track isn't valid.
     */
    @BeforeEach
    public void setUpGames() throws InvalidFileFormatException, IOException {
        trackOvalClock = new Track(new File("tracks/oval-clock-up.txt"));
        gameOvalClock = new Game(trackOvalClock);
        carOvalClockIndex0 = trackOvalClock.getCar(0);
        carOvalClockIndex1 = trackOvalClock.getCar(1);

        trackQuarterMile = new Track(new File("tracks/quarter-mile.txt"));
        gameQuarterMile = new Game(trackQuarterMile);
        carQuarterMileIndex0 = trackQuarterMile.getCar(0);

        gameOvalAnticlockRight = new Game(new Track(new File("tracks/oval-clock-up.txt")));

        trackWith5Cars = new Track(new File(tracksFolder + "/quarter-mile-with-5-cars.txt"));
        gameWith5Cars = new Game(trackWith5Cars);

        gameChallenge = new Game(new Track(new File("tracks/challenge.txt")));
    }

    /**
     * This is a positive and of the equivalence class 3. The test checks if the car count of each game object is correct.
     */
    @Test
    public void testCarCount() {
        assertEquals(2, gameOvalClock.getCarCount());
        assertEquals(2, gameChallenge.getCarCount());
        assertEquals(2, gameOvalAnticlockRight.getCarCount());
        assertEquals(2, gameQuarterMile.getCarCount());
        assertEquals(5, gameWith5Cars.getCarCount());
    }

    /**
     * This is a negative and of the equivalence class 4. The test checks if the constructor of the Game class throws an {@link IllegalArgumentException}.
     * The test checks if an {@link IllegalArgumentException} is thrown when a file with 0 or 10 cars is passed to the Game constructor.
     */
    @Test
    public void testCarCountIsInvalid() {
        assertThrows(InvalidFileFormatException.class, () -> new Game(new Track(new File(tracksFolder + "/quarter-mile-with-0-cars.txt"))));
        assertThrows(InvalidFileFormatException.class, () -> new Game(new Track(new File(tracksFolder + "/quarter-mile-with-10-cars.txt"))));
    }

    /**
     * This is a negative test and of the equivalence class 2. It checks if Game constructor throws {@link IllegalArgumentException} when passed the
     * invalid parameter value null.
     */
    @Test
    public void testConstructorWithNull() {
        assertThrows(NullPointerException.class, () -> new Game(null));
    }

    /**
     * This is a negative test and of the equivalence class 19. The test checks if the {@link Game#calculatePath(PositionVector, PositionVector)} throws a {@link NullPointerException} when parameter are null.
     */
    @Test
    public void testCalculatePathParameterNull() {
        assertThrows(NullPointerException.class, () -> gameQuarterMile.calculatePath(new PositionVector(1, 3), null));
        assertThrows(NullPointerException.class, () -> gameQuarterMile.calculatePath(null, new PositionVector(1, 3)));
    }

    /**
     * This is a positive test and of the equivalence class 20. The test checks if {@link Game#onlyCarsWithDoNotMoveStrategyRemaining()} returns true
     * when the two remaining cars have the {@link DoNotMoveStrategy}.
     */
    @Test
    public void testOnlyCarsRemaining() {
        for (int i = 0; i < gameWith5Cars.getCarCount() - 2; i++) {
            trackWith5Cars.getCar(i).crash(new PositionVector(2, 3));
        }
        gameWith5Cars.switchToNextActiveCar();
        gameWith5Cars.setCarMoveStrategy(gameWith5Cars.getCurrentCarIndex(), new DoNotMoveStrategy());
        gameWith5Cars.switchToNextActiveCar();
        gameWith5Cars.setCarMoveStrategy(gameWith5Cars.getCurrentCarIndex(), new DoNotMoveStrategy());
        assertTrue(gameWith5Cars.onlyCarsWithDoNotMoveStrategyRemaining());
    }

    /**
     * This is a positive test and of the equivalence class 14. The test checks if the car id of the current car is correct.
     */
    @Test
    public void testCarId() {
        int currentCarIndex = gameOvalClock.getCurrentCarIndex();
        assertEquals('a', gameOvalClock.getCarId(currentCarIndex));
        gameOvalClock.switchToNextActiveCar();
        currentCarIndex = gameOvalClock.getCurrentCarIndex();
        assertEquals('b', gameOvalClock.getCarId(currentCarIndex));
    }

    /**
     * This is a negative test and of the equivalence class 15. The test checks if a {@link IllegalArgumentException} is thrown, when the car index is invalid.
     */
    @Test
    public void testCarIdNegative() {
        assertThrows(IllegalStateException.class, () -> gameOvalClock.getCarId(-1));
    }

    /**
     * This is a negative test and of the equivalence class 15. The test checks if a {@link IllegalArgumentException} is thrown, when the car index is invalid.
     */
    @Test
    public void testCarIdOverTheLimit() {
        assertThrows(IllegalArgumentException.class, () -> gameOvalClock.getCarId(gameOvalClock.getCarCount()));
    }

    /**
     * This is a negative test and of the equivalence class 16. The test checks if a {@link IllegalArgumentException} is thrown, when the car index is invalid.
     */
    @Test
    public void testCarVelocity() {
        assertThrows(IllegalArgumentException.class, () -> gameOvalClock.getCarVelocity(-1));
        assertThrows(IllegalArgumentException.class, () -> gameOvalClock.getCarVelocity(10));
    }

    /**
     * This is a positive test and of the equivalence class 6. The test checks if the position vector of each car in the game object is not null and has non-negative x and y coordinates.
     */
    @Test
    public void testBasicCarPosition() {
        for (int i = 0; i < gameOvalClock.getCarCount(); i++) {
            PositionVector position = gameOvalClock.getCarPosition(i);
            assertNotNull(position);
            assertFalse(position.getX() < 0);
            assertFalse(position.getY() < 0);
        }

        for (int i = 0; i < gameQuarterMile.getCarCount(); i++) {
            PositionVector position = gameQuarterMile.getCarPosition(i);
            assertNotNull(position);
            assertFalse(position.getX() < 0);
            assertFalse(position.getY() < 0);
        }
    }

    /**
     * This is a positive test and of the equivalence class 6. The test checks if the car's position vector is updated correctly when it drives over the finish line.
     */
    @Test
    public void testCarPositionWhenOverLine1() {
        gameQuarterMile.doCarTurn(Direction.LEFT);
        gameQuarterMile.doCarTurn(Direction.LEFT);
        gameQuarterMile.doCarTurn(Direction.LEFT);
        gameQuarterMile.doCarTurn(Direction.LEFT);
        gameQuarterMile.doCarTurn(Direction.LEFT);
        gameQuarterMile.doCarTurn(Direction.LEFT);
        gameQuarterMile.doCarTurn(Direction.LEFT);
        gameQuarterMile.doCarTurn(Direction.LEFT);
        gameQuarterMile.doCarTurn(Direction.LEFT);
        gameQuarterMile.doCarTurn(Direction.LEFT);

        assertEquals(gameQuarterMile.getCurrentCarIndex(), gameQuarterMile.getWinner());
        assertEquals(SpaceType.FINISH_LEFT, trackQuarterMile.getSpaceTypeAtPosition(gameQuarterMile.getCarPosition(0)));
    }

    /**
     * This is a positive test and of the equivalence class 13. The test checks if the game is still going when the line is crossed in wrong direction.
     */
    @Test
    public void testTwoTimesOverLine() {
        gameOvalClock.doCarTurn(Direction.DOWN);
        gameOvalClock.doCarTurn(Direction.DOWN);
        assertEquals(-1, gameOvalClock.getWinner());
        gameOvalClock.doCarTurn(Direction.UP);
        gameOvalClock.doCarTurn(Direction.UP);
        assertEquals(-1, gameOvalClock.getWinner());
    }

    /**
     * This is a positive test and of the equivalence class 6. The test checks if the car's position vector is updated correctly when it drives over the finish line.
     */
    @Test
    public void testCarPositionWhenOverLine2() {
        gameOvalClock.doCarTurn(Direction.UP_RIGHT);
        gameOvalClock.doCarTurn(Direction.DOWN_RIGHT);
        gameOvalClock.doCarTurn(Direction.RIGHT);
        gameOvalClock.doCarTurn(Direction.UP_RIGHT);
        gameOvalClock.doCarTurn(Direction.RIGHT);
        gameOvalClock.doCarTurn(Direction.DOWN_LEFT);
        gameOvalClock.doCarTurn(Direction.DOWN_LEFT);
        gameOvalClock.doCarTurn(Direction.LEFT);
        gameOvalClock.doCarTurn(Direction.DOWN);
        gameOvalClock.doCarTurn(Direction.UP_LEFT);
        gameOvalClock.doCarTurn(Direction.LEFT);
        gameOvalClock.doCarTurn(Direction.LEFT);
        gameOvalClock.doCarTurn(Direction.LEFT);
        gameOvalClock.doCarTurn(Direction.LEFT);
        gameOvalClock.doCarTurn(Direction.UP_LEFT);
        gameOvalClock.doCarTurn(Direction.UP_LEFT);
        gameOvalClock.doCarTurn(Direction.UP_LEFT);
        gameOvalClock.doCarTurn(Direction.UP);

        assertEquals(gameOvalClock.getCurrentCarIndex(), gameOvalClock.getWinner());
        assertEquals(SpaceType.FINISH_UP, trackOvalClock.getSpaceTypeAtPosition(gameOvalClock.getCarPosition(0)));
    }

    /**
     * This is a positive test and of the equivalence class 6. The test checks if the car's position vector is updated correctly when it crashes into a wall.
     */
    @Test
    public void testCarPositionWhenCrashedInWall1() {
        gameQuarterMile.doCarTurn(Direction.LEFT);
        gameQuarterMile.doCarTurn(Direction.UP);
        gameQuarterMile.doCarTurn(Direction.UP);
        gameQuarterMile.doCarTurn(Direction.UP);

        assertTrue(carQuarterMileIndex0.isCrashed());
        assertEquals(SpaceType.WALL, trackQuarterMile.getSpaceTypeAtPosition(gameQuarterMile.getCarPosition(0)));
    }

    /**
     * This is a positive test and of the equivalence class 6. The test checks if the car's position vector is updated correctly when it crashes into a wall.
     */
    @Test
    public void testCarPositionWhenCrashedInWall2() {
        gameOvalClock.doCarTurn(Direction.UP_RIGHT);
        gameOvalClock.doCarTurn(Direction.RIGHT);
        gameOvalClock.doCarTurn(Direction.DOWN_RIGHT);
        gameOvalClock.doCarTurn(Direction.RIGHT);
        gameOvalClock.doCarTurn(Direction.RIGHT);
        gameOvalClock.doCarTurn(Direction.UP);
        gameOvalClock.doCarTurn(Direction.UP);
        gameOvalClock.doCarTurn(Direction.UP);
        gameOvalClock.doCarTurn(Direction.UP);

        assertTrue(carOvalClockIndex0.isCrashed());
        assertEquals(SpaceType.WALL, trackOvalClock.getSpaceTypeAtPosition(gameOvalClock.getCarPosition(0)));
    }

    /**
     * This is a positive test and of the equivalence class 6. The test checks if the car's position vector is updated correctly when it crashes into another car.
     */
    @Test
    public void testCarPositionWhenCollide() {
        gameWith5Cars.switchToNextActiveCar();
        gameWith5Cars.doCarTurn(Direction.UP_LEFT);

        Car currentCar = trackWith5Cars.getCar(gameWith5Cars.getCurrentCarIndex());
        assertTrue(currentCar.isCrashed());
        assertEquals(currentCar.getCurrentPosition(), trackWith5Cars.getCar(0).getCurrentPosition());
        assertNotEquals(SpaceType.WALL, trackWith5Cars.getSpaceTypeAtPosition(gameWith5Cars.getCarPosition(0)));
    }

    /**
     * This is a positive test and of the equivalence class 10. The test that checks if only the driving car crashes.
     */
    @Test
    public void testOnlyOneCarCrashesInCollisionAtTheBeginning() {
        gameWith5Cars.switchToNextActiveCar();
        gameWith5Cars.doCarTurn(Direction.UP_LEFT);

        assertTrue(trackWith5Cars.getCar(gameWith5Cars.getCurrentCarIndex()).isCrashed());
    }

    /**
     * This is a positive test and of the equivalence class 10. The test checks if only the driving car crashes by a collision.
     */
    @Test
    public void testOnlyOneCarCrashesInCollisionMidGame() {

        gameOvalClock.doCarTurn(Direction.UP_RIGHT);
        gameOvalClock.switchToNextActiveCar();
        gameOvalClock.doCarTurn(Direction.UP_RIGHT);
        gameOvalClock.switchToNextActiveCar();
        gameOvalClock.doCarTurn(Direction.RIGHT);
        gameOvalClock.switchToNextActiveCar();
        gameOvalClock.doCarTurn(Direction.LEFT);
        gameOvalClock.switchToNextActiveCar();
        gameOvalClock.doCarTurn(Direction.DOWN_RIGHT);

        assertTrue(carOvalClockIndex0.isCrashed());
        assertEquals(carOvalClockIndex0.getCurrentPosition(), carOvalClockIndex1.getCurrentPosition());
    }

    /**
     * This is a negative test and of the equivalence class 11. The test checks if only the driving car crashes.
     */
    @Test
    public void testAfterCollisionTheNotMovingLivesAtTheBeginning() {
        gameWith5Cars.switchToNextActiveCar();
        gameWith5Cars.doCarTurn(Direction.UP_LEFT);

        assertFalse(trackWith5Cars.getCar(0).isCrashed());
    }

    /**
     * This is a negative test and of the equivalence class 11. The test checks if only the driving car crashes.
     */
    @Test
    public void testAfterCollisionTheNotMovingLivesAtMidGame() {
        gameOvalClock.doCarTurn(Direction.UP_RIGHT);
        gameOvalClock.switchToNextActiveCar();
        gameOvalClock.doCarTurn(Direction.UP_RIGHT);
        gameOvalClock.switchToNextActiveCar();
        gameOvalClock.doCarTurn(Direction.RIGHT);
        gameOvalClock.switchToNextActiveCar();
        gameOvalClock.doCarTurn(Direction.LEFT);
        gameOvalClock.switchToNextActiveCar();
        gameOvalClock.doCarTurn(Direction.DOWN_RIGHT);

        assertFalse(carOvalClockIndex1.isCrashed());
        assertEquals(carOvalClockIndex0.getCurrentPosition(), carOvalClockIndex1.getCurrentPosition());
    }


    /**
     * This is a positive test and of the equivalence class 6. The test checks if the car's position vector is updated correctly after it is moved and accelerated.
     */
    @Test
    public void testCarPositionDuringSomeMove() {
        PositionVector previousPosition = carOvalClockIndex0.getCurrentPosition();
        gameOvalClock.doCarTurn(Direction.UP_RIGHT);
        PositionVector position = carOvalClockIndex0.getCurrentPosition();
        assertEquals(previousPosition.getX() + 1, position.getX());
        assertEquals(previousPosition.getY() - 1, position.getY());

        previousPosition = carOvalClockIndex0.getCurrentPosition();
        gameOvalClock.doCarTurn(Direction.UP_RIGHT);
        position = carOvalClockIndex0.getCurrentPosition();
        assertEquals(previousPosition.getX() + 2, position.getX());
        assertEquals(previousPosition.getY() - 2, position.getY());

        previousPosition = carOvalClockIndex0.getCurrentPosition();
        gameOvalClock.doCarTurn(Direction.LEFT);
        position = carOvalClockIndex0.getCurrentPosition();
        assertEquals(previousPosition.getX(), position.getX());
        assertEquals(previousPosition.getY(), position.getY());
    }


    /**
     * This is a positive test and of the equivalence class 6. The test checks if the car's position vector is updated correctly after it is moved and accelerated.
     */
    @Test
    public void testCarPositionDuringSomeMove2() {

        PositionVector previousPosition = carQuarterMileIndex0.getCurrentPosition();
        gameQuarterMile.doCarTurn(Direction.LEFT);
        PositionVector position = carQuarterMileIndex0.getCurrentPosition();
        assertEquals(previousPosition.getX() - 1, position.getX());
        assertEquals(previousPosition.getY(), position.getY());

        previousPosition = carQuarterMileIndex0.getCurrentPosition();
        gameQuarterMile.doCarTurn(Direction.LEFT);
        position = carQuarterMileIndex0.getCurrentPosition();
        assertEquals(previousPosition.getX() - 2, position.getX());
        assertEquals(previousPosition.getY(), position.getY());

        previousPosition = carQuarterMileIndex0.getCurrentPosition();
        gameQuarterMile.doCarTurn(Direction.UP_LEFT);
        position = carQuarterMileIndex0.getCurrentPosition();
        assertEquals(previousPosition.getX() - 3, position.getX());
        assertEquals(previousPosition.getY() - 1, position.getY());

        previousPosition = carQuarterMileIndex0.getCurrentPosition();
        gameQuarterMile.doCarTurn(Direction.NONE);
        position = carQuarterMileIndex0.getCurrentPosition();
        assertEquals(previousPosition.getX() - 2, position.getX());
        assertEquals(previousPosition.getY() - 1, position.getY());
    }

    /**
     * This is a positive test and of the equivalence class 12. The test checks if the getWinner method returns the correct index of the winning car when all other cars have crashed.
     */
    @Test
    public void testWinnerWhenAllOtherCarsCrashed1() {
        gameOvalClock.doCarTurn(Direction.UP_LEFT);
        assertEquals(-1, gameOvalClock.getWinner());
        gameOvalClock.doCarTurn(Direction.UP);
        assertEquals(1, gameOvalClock.getWinner());
    }

    /**
     * This is a positive test and of the equivalence class 12. The test checks if the getWinner method returns the correct index of the winning car when all other cars have crashed.
     */
    @Test
    public void testWinnerWhenAllOtherCarsCrashed2() {
        gameWith5Cars.switchToNextActiveCar();
        gameWith5Cars.doCarTurn(Direction.RIGHT);
        gameWith5Cars.doCarTurn(Direction.RIGHT);
        gameWith5Cars.switchToNextActiveCar();
        gameWith5Cars.doCarTurn(Direction.RIGHT);
        gameWith5Cars.doCarTurn(Direction.RIGHT);
        gameWith5Cars.switchToNextActiveCar();
        gameWith5Cars.doCarTurn(Direction.RIGHT);
        gameWith5Cars.doCarTurn(Direction.RIGHT);
        gameWith5Cars.switchToNextActiveCar();
        gameWith5Cars.doCarTurn(Direction.RIGHT);
        gameWith5Cars.doCarTurn(Direction.RIGHT);

        assertEquals(0, gameWith5Cars.getWinner());
    }

    /**
     * This is a positive test  and of the equivalence class 12. The test checks if the winner is correctly determined when a car crosses the finish line.
     */
    @Test
    public void testWinnerOverLine() {
        assertEquals(-1, gameQuarterMile.getWinner());
        gameQuarterMile.doCarTurn(Direction.LEFT);
        gameQuarterMile.doCarTurn(Direction.LEFT);
        gameQuarterMile.doCarTurn(Direction.LEFT);
        gameQuarterMile.doCarTurn(Direction.LEFT);
        gameQuarterMile.doCarTurn(Direction.LEFT);
        gameQuarterMile.doCarTurn(Direction.LEFT);
        gameQuarterMile.doCarTurn(Direction.LEFT);
        gameQuarterMile.doCarTurn(Direction.LEFT);
        gameQuarterMile.doCarTurn(Direction.LEFT);
        gameQuarterMile.doCarTurn(Direction.LEFT);
        assertEquals(gameQuarterMile.getCurrentCarIndex(), gameQuarterMile.getWinner());
    }

    /**
     * This is a negative test and of the equivalence class 13. The test checks if there is no winner when a car crosses the finish line with the wrong direction.
     */
    @Test
    public void testOverLineFromWrongDirection() {
        gameOvalClock.doCarTurn(Direction.DOWN);
        gameOvalClock.doCarTurn(Direction.DOWN);
        gameOvalClock.doCarTurn(Direction.UP);
        gameOvalClock.doCarTurn(Direction.UP);
        gameOvalClock.doCarTurn(Direction.UP);
        assertEquals(-1, gameOvalClock.getWinner());
    }

    /**
     * This is a negative test and of the equivalence class 13. The test checks if there are no winners, when no moves are made and two cars are left active.
     */
    @Test
    public void testWinnerWhenThereAreActiveCars() {
        assertEquals(-1, gameWith5Cars.getWinner());

        int carCount = gameWith5Cars.getCarCount();
        for (int i = 0; i < carCount - 2; i++) {
            int currentCarIndex = gameWith5Cars.getCurrentCarIndex();
            trackWith5Cars.getCar(currentCarIndex).crash(new PositionVector(3, 2));
        }
        assertNotEquals(carCount - 1, gameWith5Cars.getWinner());
        assertEquals(-1, gameWith5Cars.getWinner());
    }

    /**
     * This is a negative test and of the equivalence class 13. The test checks if there are no winners, when the game is going on.
     */
    @Test
    public void testWinnerWhenGameStillGoing() {
        assertEquals(-1, gameQuarterMile.getWinner());
        gameQuarterMile.doCarTurn(Direction.LEFT);
        gameQuarterMile.switchToNextActiveCar();
        gameQuarterMile.doCarTurn(Direction.LEFT);
        gameQuarterMile.switchToNextActiveCar();
        gameQuarterMile.doCarTurn(Direction.LEFT);
        gameQuarterMile.switchToNextActiveCar();
        gameQuarterMile.doCarTurn(Direction.LEFT);
        gameQuarterMile.switchToNextActiveCar();
        gameQuarterMile.doCarTurn(Direction.UP_LEFT);

        assertEquals(-1, gameQuarterMile.getWinner());
    }

    /**
     * This is a positive test and of the equivalence class 10. The test checks if a car crashes after driving into a wall.
     */
    @Test
    public void testCrashInWall1() {
        gameQuarterMile.doCarTurn(Direction.UP);
        gameQuarterMile.doCarTurn(Direction.UP);
        assertTrue(carQuarterMileIndex0.isCrashed());
        assertEquals(SpaceType.WALL, trackQuarterMile.getSpaceTypeAtPosition(carQuarterMileIndex0.getCurrentPosition()));
    }

    /**
     * This is a positive test and of the equivalence class 10. The test checks if a car crashes after driving into a wall.
     */
    @Test
    public void testCrashInWall2() {
        gameOvalClock.doCarTurn(Direction.UP_RIGHT);
        gameOvalClock.doCarTurn(Direction.RIGHT);
        gameOvalClock.doCarTurn(Direction.UP);
        assertTrue(carOvalClockIndex0.isCrashed());
        assertEquals(SpaceType.WALL, trackOvalClock.getSpaceTypeAtPosition(carOvalClockIndex0.getCurrentPosition()));
    }

    /**
     * This is a positive test and of the equivalence class 18. The test is for checking if the game correctly switches to next active car.
     */
    @Test
    public void testSwitchTurnWithNoCrashedCars() {
        int currentCarIndex = gameWith5Cars.getCurrentCarIndex();

        gameWith5Cars.switchToNextActiveCar();
        assertNotEquals(currentCarIndex, gameWith5Cars.getCurrentCarIndex());

        if (currentCarIndex < gameWith5Cars.getCarCount() - 1) {
            assertEquals(currentCarIndex + 1, gameWith5Cars.getCurrentCarIndex());
        } else {
            assertEquals(0, gameWith5Cars.getCurrentCarIndex());
        }
    }

    /**
     * Positive test for checking if the game correctly switches to next active car and
     * handles crashed cars correctly during car switching. With the equivalence class 18.
     */
    @Test
    public void testSwitchTurnWithCrashedCars1() {
        carOvalClockIndex1.crash(new PositionVector(2, 3));

        int currentCarIndex = gameOvalClock.getCurrentCarIndex();
        gameOvalClock.switchToNextActiveCar();
        assertEquals(currentCarIndex, gameOvalClock.getCurrentCarIndex());
    }

    /**
     * Positive test for checking if the game correctly switches to next active car and
     * handles crashed cars correctly during car switching. With the equivalence class 18.
     */
    @Test
    public void testSwitchTurnWithCrashedCars2() {
        trackWith5Cars.getCar(0).crash(new PositionVector(1, 1));
        trackWith5Cars.getCar(1).crash(new PositionVector(3, 1));
        trackWith5Cars.getCar(3).crash(new PositionVector(5, 1));

        assertEquals(0, gameWith5Cars.getCurrentCarIndex());
        gameWith5Cars.switchToNextActiveCar();
        assertEquals(2, gameWith5Cars.getCurrentCarIndex());
        gameWith5Cars.switchToNextActiveCar();
        assertEquals(4, gameWith5Cars.getCurrentCarIndex());
        gameWith5Cars.switchToNextActiveCar();
        assertEquals(2, gameWith5Cars.getCurrentCarIndex());
    }
}
