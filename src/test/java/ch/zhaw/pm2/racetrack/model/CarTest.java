package ch.zhaw.pm2.racetrack.model;

import ch.zhaw.pm2.racetrack.given.Direction;
import ch.zhaw.pm2.racetrack.given.PositionVector;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Test class for the {@link Car} class.<br>
 *
 * <br>This test class has the following equivalence classes:<br>
 *
 * <br>01. CarIsCreated: Tests if the car can be build under specific conditions.
 * <br>02. CarIsNotCreated: Tests if the car can not be build under specific conditions.
 * <br>03. PositionCalculatesRight: Tests if the position calculates right.
 * <br>04. VelocityCalculatesRight: Tests if the velocity calculates right.
 * <br>05. AccelerationCalculatesRight: Tests if the acceleration calculates right.
 * <br>06. MoveWorksRight: Tests if the move method calculates right.
 * <br>07. CarCrashed: Tests if the car crashed under specific conditions.
 * <br>08. CarDoesNotCrash: Tests if the car does not crash under specific conditions.
 * <br>09. CarWins: Tests if the car wins after some actions.
 * <br>10. CarDoesNotWin: Tests if the car does not win after some actions.
 *
 * @author StackOverflow
 * @version 1.0
 */


public class CarTest {

    private Car car;
    private final PositionVector startingPosition = new PositionVector(4, 5);
    private final PositionVector nullVector = new PositionVector(0, 0);

    /**
     * Sets up a new car object.
     */
    @BeforeEach
    public void setUp() {
        car = new Car('a', startingPosition);
    }

    /**
     * Positive test checking that the {@link Car} constructor works
     * as intended with valid parameter valid. This is a test of the equivalence class 1.
     */
    @Test
    public void testCarConstructor() {
        Car carA = new Car('a', new PositionVector(1, 2));
        Car carB = new Car('b', new PositionVector(3, 5));
        Car carSpecial = new Car('!', new PositionVector(10, 8));
        Car car3 = new Car('3', new PositionVector(6, 55));

        assertEquals('a', carA.getId());
        assertEquals('b', carB.getId());
        assertEquals('!', carSpecial.getId());
        assertEquals('3', car3.getId());
    }

    /**
     * Negative test checking that the {@link Car} constructor should
     * throw an exception with the invalid parameter value null. This is a test of the equivalence class 2.
     */
    @Test
    public void testCarConstructorWithNull() {
        assertThrows(NullPointerException.class, () -> new Car('A', null));
    }

    /**
     * Positive test checking that the {@link Car#getCurrentPosition()} method
     * works as intended by returning the current position of the car. This is a test of the equivalence class 3.
     */
    @Test
    public void testCurrentPosition() {
        assertEquals(startingPosition, car.getCurrentPosition());
        PositionVector currentPosition = car.getCurrentPosition();
        car.accelerate(Direction.UP);
        car.move();
        assertEquals(new PositionVector(currentPosition.getX(), currentPosition.getY() - 1), car.getCurrentPosition());
    }

    /**
     * Positive test checking that the {@link Car#accelerate(Direction)} method
     * works as intended by adding each acceleration on top of each other.
     * This is a test of the equivalence class 4.
     */
    @Test
    public void testVelocity() {
        assertEquals(nullVector, car.getVelocity());
        car.accelerate(Direction.LEFT);
        assertEquals(new PositionVector(-1, 0), car.getVelocity());
        car.accelerate(Direction.NONE);
        assertEquals(new PositionVector(-1, 0), car.getVelocity());
        car.accelerate(Direction.UP_RIGHT);
        assertEquals(new PositionVector(0, -1), car.getVelocity());
    }

    /**
     * Positive test case for the {@link Car#getNextPosition()} method.
     * Tests whether the next position of the car is correctly calculated based on its current position and velocity.
     * This is a test of the equivalence class 3.
     */
    @Test
    public void getNextPositionTest() {
        assertEquals(car.getNextPosition(), car.getCurrentPosition());
        car.accelerate(Direction.DOWN);
        assertNotEquals(car.getNextPosition(), car.getCurrentPosition());
        assertEquals(car.getCurrentPosition().add(Direction.DOWN.vector), car.getNextPosition());
        car.accelerate(Direction.DOWN);
        assertNotEquals(car.getCurrentPosition().add(Direction.DOWN.vector), car.getNextPosition());
        assertEquals(car.getCurrentPosition().add(Direction.DOWN.vector).add(Direction.DOWN.vector), car.getNextPosition());
    }

    /**
     * Positive test checking that the {@link Car#accelerate(Direction)} method
     * works as intended with valid values. This is a test of the equivalence class 5.
     */
    @Test
    public void testAccelerate() {
        assertEquals(nullVector, car.getVelocity());
        assertEquals(startingPosition, car.getCurrentPosition());
        car.accelerate(Direction.LEFT);
        assertEquals(Direction.LEFT.vector, car.getVelocity());
        assertEquals(startingPosition, car.getCurrentPosition());
        car.accelerate(Direction.UP_LEFT);
        assertEquals(startingPosition, car.getCurrentPosition());
        assertEquals(Direction.LEFT.vector.add(Direction.UP_LEFT.vector), car.getVelocity());

    }

    /**
     * Positive test checking that the {@link Car#move()} method works.
     * This is a test of the equivalence class 6.
     */
    @Test
    public void moveTest() {
        assertEquals(startingPosition, car.getCurrentPosition());
        car.accelerate(Direction.LEFT);
        assertEquals(Direction.LEFT.vector, car.getVelocity());
        car.move();
        assertEquals(new PositionVector(3, 5), car.getCurrentPosition());

        car.accelerate(Direction.NONE);
        assertEquals(Direction.LEFT.vector, car.getVelocity());
        car.move();
        assertEquals(new PositionVector(2, 5), car.getCurrentPosition());

        car.accelerate(Direction.DOWN_LEFT);
        assertEquals(new PositionVector(-2, 1), car.getVelocity());
        car.move();
        assertEquals(new PositionVector(0, 6), car.getCurrentPosition());

        car.accelerate(Direction.UP);
        assertEquals(new PositionVector(-2, 0), car.getVelocity());
        car.move();
        assertEquals(new PositionVector(-2, 6), car.getCurrentPosition());
    }


    /**
     * Positive test checking that the {@link Car#crash(PositionVector)}
     * method works as intended. The method should change both the crashed
     * status and the current position of the car. This is a test of the equivalence class 7.
     */
    @Test
    public void testCrash() {
        PositionVector crashPosition = new PositionVector(1, 2);

        assertFalse(car.isCrashed());
        car.crash(crashPosition);
        assertTrue(car.isCrashed());
        assertEquals(crashPosition, car.getCurrentPosition());
    }

    /**
     * Negative test checking that the {@link Car#crash(PositionVector)}
     * method should throw an exception with the invalid parameter value null.
     * This is a test of the equivalence class 8.
     */
    @Test
    public void testCrashWithNull() {
        assertThrows(NullPointerException.class, () -> car.crash(null));
    }

    /**
     * Positive test checking that the {@link Car#setWinningPosition(PositionVector)}
     * method works as intended. After calling the method, the current position of the
     * car should be set at the winning position. This is a test of the equivalence class 9.
     */
    @Test
    public void testWinningPosition() {
        PositionVector winningPosition = new PositionVector(1, 1);
        car.setWinningPosition(winningPosition);
        assertEquals(winningPosition, car.getCurrentPosition());
    }

    /**
     * Negative test checking that the {@link Car#setWinningPosition(PositionVector)}
     * method should throw an exception with the invalid parameter value null.
     * This is a test of the equivalence class 10.
     */
    @Test
    public void testWinWithNull() {
        assertThrows(NullPointerException.class, () -> car.setWinningPosition(null));
    }
}
