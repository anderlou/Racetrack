package ch.zhaw.pm2.racetrack.given;

/**
 * Holds a position (vector to x,y-position of the car on the track grid)
 * or a velocity vector (x,y-components of the velocity vector of a car).<br/>
 * PositionVectors are immutable, which means they cannot be modified.<br/>
 * Vector operations like {@link #add(PositionVector)} and {@link #subtract(PositionVector)}
 * return a new PositionVector containing the result.
 *
 * @author mach
 * @version FS2023
 */
public final class PositionVector {

    /**
     * horizontal value (position / velocity)
     */
    private final int x;

    /**
     * vertical value (position / velocity)
     */
    private final int y;

    /**
     * Base constructor, initializing the position using coordinates or a velocity vector
     *
     * @param x horizontal value (position or velocity)
     * @param y vertical value (position or velocity)
     */
    public PositionVector(final int x, final int y) {
        this.y = y;
        this.x = x;
    }

    /**
     * Copy constructor, copying the values from another PositionVector.
     *
     * @param other position vector to copy from
     */
    public PositionVector(final PositionVector other) {
        this.x = other.getX();
        this.y = other.getY();
    }

    /**
     * @return the horizontal value (position or velocity)
     */
    public int getX() {
        return this.x;
    }

    /**
     * @return vertical value (position or velocity)
     */
    public int getY() {
        return this.y;
    }

    @Override
    public boolean equals(final Object other) {
        if (!(other instanceof final PositionVector otherVector)) return false;
        return this.y == otherVector.getY() && this.x == otherVector.getX();
    }

    @Override
    public int hashCode() {
        return this.x ^ this.y;
    }

    @Override
    public String toString() {
        return "(X:" + this.x + ", Y:" + this.y + ")";
    }

    /**
     * Calculates the vector addition of the current vector with the given vector, e.g.
     * <ul>
     *   <li>if a velocity vector is added to a position, the next position is returned</li>
     *   <li>if a direction vector is added to a velocity, the new velocity is returned</li>
     * </ul>
     * The vectors values are not modified, but a new Vector containing the result is returned.
     *
     * @param vector a position or velocity vector to add
     * @return A new PositionVector holding the result of the addition.
     */
    public PositionVector add(final PositionVector vector) {
        return new PositionVector(this.getX() + vector.getX(), this.getY() + vector.getY());
    }

    /**
     * Calculates the vector difference of the current vector to the given vector,
     * i.e. subtracts the given from the current vectors coordinates. (e.g. car position and/or velocity vector) <br>
     * The vectors values are not modified, but a new Vector containing the result is returned.
     *
     * @param vector A position or velocity vector to subtract
     * @return A new PositionVector holding the result of the subtraction.
     */
    public PositionVector subtract(final PositionVector vector) {
        return new PositionVector(this.getX() - vector.getX(), this.getY() - vector.getY());
    }

}
