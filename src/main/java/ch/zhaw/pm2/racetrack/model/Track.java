package ch.zhaw.pm2.racetrack.model;

import ch.zhaw.pm2.racetrack.exception.InvalidFileFormatException;
import ch.zhaw.pm2.racetrack.given.PositionVector;
import ch.zhaw.pm2.racetrack.given.SpaceType;
import ch.zhaw.pm2.racetrack.given.TrackSpecification;
import ch.zhaw.pm2.racetrack.utils.Reader;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

/**
 * This class represents the racetrack board.
 *
 * <p>The racetrack board consists of a rectangular grid of 'width' columns and 'height' rows.
 * The zero point of he grid is at the top left. The x-axis points to the right and the y-axis points downwards.</p>
 * <p>Positions on the track grid are specified using {@link PositionVector} objects. These are vectors containing an
 * x/y coordinate pair, pointing from the zero-point (top-left) to the addressed space in the grid.</p>
 *
 * <p>Each position in the grid represents a space which can hold an enum object of type {@link SpaceType}.<br>
 * Possible Space types are:
 * <ul>
 *  <li>WALL : road boundary or off track space</li>
 *  <li>TRACK: road or open track space</li>
 *  <li>FINISH_LEFT, FINISH_RIGHT, FINISH_UP, FINISH_DOWN :  finish line spaces which have to be crossed
 *      in the indicated direction to winn the race.</li>
 * </ul>
 * <p>Beside the board the track contains the list of cars, with their current state (position, velocity, crashed,...)</p>
 *
 * <p>At initialization the track grid data is read from the given track file. The track data must be a
 * rectangular block of text. Empty lines at the start are ignored. Processing stops at the first empty line
 * following a non-empty line, or at the end of the file.</p>
 * <p>Characters in the line represent SpaceTypes. The mapping of the Characters is as follows:</p>
 * <ul>
 *   <li>WALL : '#'</li>
 *   <li>TRACK: ' '</li>
 *   <li>FINISH_LEFT : '&lt;'</li>
 *   <li>FINISH_RIGHT: '&gt;'</li>
 *   <li>FINISH_UP   : '^;'</li>
 *   <li>FINISH_DOWN: 'v'</li>
 *   <li>Any other character indicates the starting position of a car.<br>
 *       The character acts as the id for the car and must be unique.<br>
 *       There are 1 to {@link TrackSpecification#MAX_CARS} allowed. </li>
 * </ul>
 *
 * <p>All lines must have the same length, used to initialize the grid width.<br/>
 * Beginning empty lines are skipped. <br/>
 * The track ends with the first empty line or the file end.<br>
 * An {@link InvalidFileFormatException} is thrown, if
 * <ul>
 *   <li>the file contains no track lines (grid height is 0)</li>
 *   <li>not all track lines have the same length</li>
 *   <li>the file contains no cars</li>
 *   <li>the file contains more than {@link TrackSpecification#MAX_CARS} cars</li>
 * </ul>
 *
 * <p>The Tracks {@link #toString()} method returns a String representing the current state of the race
 * (including car positions and status)</p>
 *
 * @author Team03 - Stackoverflow
 * @version 1.0
 */
public class Track implements TrackSpecification {

    public static final char CRASH_INDICATOR = 'X';

    private final SpaceType[][] grid;
    private final List<Car> cars;

    /**
     * Initialize a Track from the given track file.<br/>
     * See class description for structure and valid tracks.
     *
     * @param trackFile Reference to a file containing the track data
     * @throws IOException                if the track file can not be opened or reading fails
     * @throws InvalidFileFormatException if the track file contains invalid data (no track lines, inconsistent length, no cars)
     * @throws NullPointerException       if trackFile is null.
     */
    public Track(File trackFile) throws IOException, InvalidFileFormatException {
        Objects.requireNonNull(trackFile, "The trackFile may not be null");
        String[] lines = Reader.readFile(trackFile);
        checkLineConditions(lines);

        int height = lines.length;
        int width = lines[0].length();
        grid = new SpaceType[height][width];
        cars = new ArrayList<>();

        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                char symbol = lines[i].charAt(j);
                Optional<SpaceType> spaceTypeOptional = SpaceType.spaceTypeForChar(symbol);

                if (spaceTypeOptional.isPresent()) {
                    grid[i][j] = spaceTypeOptional.get();
                } else {
                    cars.add(new Car(symbol, new PositionVector(j, i)));
                    grid[i][j] = SpaceType.TRACK;
                }
            }
        }
        checkCarConditions();
    }

    private void checkLineConditions(String[] lines) throws InvalidFileFormatException {
        Objects.requireNonNull(lines, "lines may not be null");
        if (lines.length == 0) {
            throw new InvalidFileFormatException("File is empty!");
        }
        int length = lines[0].length();
        for (int i = 1; i < lines.length; i++) {
            if (lines[i].length() != length) {
                throw new InvalidFileFormatException("Not all track lines have the same length!");
            }
        }
    }

    private void checkCarConditions() throws InvalidFileFormatException {
        if (cars.isEmpty()) {
            throw new InvalidFileFormatException("File contains no cars!");
        }
        if (cars.size() > TrackSpecification.MAX_CARS) {
            throw new InvalidFileFormatException("File contains too many cars (max: " + TrackSpecification.MAX_CARS + ")!");
        }
        Set<Character> ids = new HashSet<>();
        for (Car car : cars) {
            if (ids.contains(car.getId())) {
                throw new InvalidFileFormatException("Car ids must be unique!");
            }
            ids.add(car.getId());
        }
    }

    /**
     * Return the height (number of rows) of the track grid.
     *
     * @return Height of the track grid
     */
    public int getHeight() {
        return grid.length;
    }

    /**
     * Return the width (number of columns) of the track grid.
     *
     * @return Width of the track grid
     */
    public int getWidth() {
        return grid[0].length;
    }

    /**
     * Return the number of cars.
     *
     * @return Number of cars
     */
    @Override
    public int getCarCount() {
        return cars.size();
    }

    /**
     * Get instance of specified car.
     *
     * @param carIndex The zero-based carIndex number
     * @return The car instance at the given index
     */
    @Override
    public Car getCar(int carIndex) {
        if (carIndex < 0 || carIndex >= cars.size()) {
            throw new IllegalArgumentException("Invalid car index!");
        }
        return cars.get(carIndex);
    }

    /**
     * Return the type of space at the given position.
     * If the location is outside the track bounds, it is considered a WALL.
     *
     * @param position The coordinates of the position to examine
     * @return The type of track position at the given location
     * @throws NullPointerException if parameter position is null.
     */
    @Override
    public SpaceType getSpaceTypeAtPosition(PositionVector position) {
        Objects.requireNonNull(position, "Parameter position may not be null!");
        int x = position.getX();
        int y = position.getY();

        // Given position is outside the grid and thus a wall
        if (x < 0 || x >= grid[0].length || y < 0 || y >= grid.length) {
            return SpaceType.WALL;
        }

        return grid[y][x];
    }

    /**
     * Gets the character representation for the given position of the racetrack, including cars.<br/>
     * This can be used for generating the {@link #toString()} representation of the racetrack.<br/>
     * If there is an active car (not crashed) at the given position, then the car id is returned.<br/>
     * If there is a crashed car at the position, {@link #CRASH_INDICATOR} is returned.<br/>
     * Otherwise, the space character for the given position is returned
     *
     * @param row row (y-value) of the racetrack position
     * @param col column (x-value) of the racetrack position
     * @return character representing the position (col,row) on the track
     * or {@link Car#getId()} resp. {@link #CRASH_INDICATOR}, if a car is at the given position
     */
    @Override
    public char getCharRepresentationAtPosition(int row, int col) {
        for (Car car : cars) {
            PositionVector positionVector = new PositionVector(col, row);
            if (positionVector.equals(car.getCurrentPosition())) {
                if (car.isCrashed()) {
                    return CRASH_INDICATOR;
                } else {
                    return car.getId();
                }
            }
        }
        return grid[row][col].getSpaceChar();
    }

    /**
     * Return a String representation of the track, including the car locations and status.
     *
     * @return A String representation of the track
     */
    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < getHeight(); i++) {
            for (int j = 0; j < getWidth(); j++) {
                stringBuilder.append(getCharRepresentationAtPosition(i, j));
            }
            stringBuilder.append("\n");
        }
        return stringBuilder.toString();
    }
}
