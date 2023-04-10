package ch.zhaw.pm2.racetrack.strategy;

import ch.zhaw.pm2.racetrack.given.Direction;
import ch.zhaw.pm2.racetrack.given.PositionVector;
import ch.zhaw.pm2.racetrack.given.SpaceType;
import ch.zhaw.pm2.racetrack.model.Car;
import ch.zhaw.pm2.racetrack.model.Track;
import ch.zhaw.pm2.racetrack.utils.BresenhamAlgorithm;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Class PathFinderStrategy which implements class PathStrategy.
 * Calculates the next move based on a {@link Track} object
 *
 * @author Team03 - Stackoverflow
 * @version 1.0
 */
public class PathFinderStrategy extends PathStrategy {
    private final Track track;
    private final GridElement[][] pathPlanningGrid;
    private final List<PositionVector> finishLineElements = new ArrayList<>();

    /**
     * Constructs a new instance of the PathFinderStrategy class for a given track and car.
     *
     * @param track    the track on which the car is running.
     * @param carIndex the index of the car to plan the path for.
     * @throws NullPointerException if track is null.
     */
    public PathFinderStrategy(Track track, int carIndex) {
        Objects.requireNonNull(track, "track may not be null!");

        this.track = track;

        if (carIndex < 0 || carIndex >= track.getCarCount()) {
            throw new IllegalArgumentException("Invalid car index!");
        }

        pathPlanningGrid = new GridElement[track.getHeight()][track.getWidth()];
        fillPathPlanningGrid();

        Car car = track.getCar(carIndex);

        PositionVector currentPosition = car.getCurrentPosition();
        pathPlanningGrid[currentPosition.getY()][currentPosition.getX()].setCount(0);

        calculateDistancesToAllFields();

        generateMoveDeque();
    }

    /**
     * {@inheritDoc}
     *
     * @return next direction from {@link PathFinderStrategy#moves} or NONE, if no more moves are available.
     */
    @Override
    public Direction nextMove() {
        Direction nextMove = moves.pollLast();
        if (nextMove == null) {
            nextMove = Direction.NONE;
        }
        return nextMove;
    }

    private void generateMoveDeque() {
        GridElement closestFinishLineElement = getClosestFinishLineElement();

        if (closestFinishLineElement.getCount() != Integer.MAX_VALUE) {
            GridElement currentElement = closestFinishLineElement;
            while (currentElement.getCount() != 0) {

                GridElement closestElement = pathPlanningGrid[currentElement.getPreviousPosition().getY()][currentElement.getPreviousPosition().getX()];
                PositionVector move = currentElement.getVelocity().subtract(closestElement.getVelocity());

                for (Direction direction : Direction.values()) {
                    if (move.equals(direction.vector)) {
                        moves.add(direction);
                    }
                }
                currentElement = closestElement;
            }
        }
    }

    private void fillPathPlanningGrid() {
        for (int y = 0; y < pathPlanningGrid.length; y++) {
            for (int x = 0; x < pathPlanningGrid[y].length; x++) {
                boolean isAccessible = track.getCharRepresentationAtPosition(y, x) != SpaceType.WALL.getSpaceChar();
                pathPlanningGrid[y][x] = new GridElement(isAccessible);
                SpaceType spaceType = track.getSpaceTypeAtPosition(new PositionVector(x, y));
                boolean isFinishLineElement = spaceType == SpaceType.FINISH_DOWN || spaceType == SpaceType.FINISH_UP || spaceType == SpaceType.FINISH_LEFT || spaceType == SpaceType.FINISH_RIGHT;
                if (isFinishLineElement) {
                    finishLineElements.add(new PositionVector(x, y));
                }
            }
        }
    }

    private GridElement getClosestFinishLineElement() {
        GridElement shortestPosition = new GridElement(true);
        for (PositionVector finishLineElement : finishLineElements) {
            int x = finishLineElement.getX();
            int y = finishLineElement.getY();

            GridElement currentFinishLine = pathPlanningGrid[y][x];
            if (currentFinishLine.getCount() < shortestPosition.getCount()) {
                shortestPosition = currentFinishLine;
            }
        }
        return shortestPosition;
    }

    private void calculateDistancesToAllFields() {
        int counter = 0;
        List<PositionVector> positionsWithCount = getPositionVectorsWithCount(counter);

        while (!positionsWithCount.isEmpty()) {
            for (PositionVector currentPosition : positionsWithCount) {
                GridElement currentElement = pathPlanningGrid[currentPosition.getY()][currentPosition.getX()];
                PositionVector currentVelocity = currentElement.getVelocity();
                int newCount = currentElement.getCount() + 1;

                checkAllDirection(currentPosition, currentVelocity, newCount);
            }
            counter++;
            positionsWithCount = getPositionVectorsWithCount(counter);
        }
    }

    private void checkAllDirection(PositionVector currentPosition, PositionVector currentVelocity, int count) {
        for (Direction direction : Direction.values()) {
            PositionVector newVelocity = currentVelocity.add(direction.vector);
            PositionVector newPosition = currentPosition.add(newVelocity);

            PositionVector maxPosition = newPosition.add(new PositionVector(calculateSumToZero(newVelocity.getX()), calculateSumToZero(newVelocity.getY())));

            if (isInGrid(maxPosition) && track.getSpaceTypeAtPosition(maxPosition) != SpaceType.WALL) {
                BresenhamAlgorithm bresenhamAlgorithm = new BresenhamAlgorithm(currentPosition, newPosition);
                List<PositionVector> passedFields = bresenhamAlgorithm.calculatePath();
                boolean carStaysOnTrack = checkPathStaysOnTrack(passedFields, newVelocity);

                GridElement nextElement = pathPlanningGrid[newPosition.getY()][newPosition.getX()];

                if (nextElement.getCount() > count && carStaysOnTrack) {
                    nextElement.setCount(count);
                    nextElement.setVelocity(newVelocity);
                    nextElement.setPreviousPosition(currentPosition);
                }
            }
        }
    }

    private boolean isInGrid(PositionVector position) {
        return position.getY() >= 0 && position.getY() < pathPlanningGrid.length &&
                position.getX() >= 0 && position.getX() < pathPlanningGrid[0].length;
    }

    private boolean checkPathStaysOnTrack(List<PositionVector> path, PositionVector newVelocity) {
        boolean carStaysOnTrack = true;
        for (PositionVector field : path) {
            carStaysOnTrack = carStaysOnTrack && passesOnlyValidFields(newVelocity, field);
        }
        return carStaysOnTrack;
    }

    private boolean passesOnlyValidFields(PositionVector newVelocity, PositionVector field) {
        SpaceType spaceType = track.getSpaceTypeAtPosition(field);
        return switch (spaceType) {
            case WALL -> false;
            case FINISH_DOWN -> newVelocity.getY() >= 0;
            case FINISH_UP -> newVelocity.getY() <= 0;
            case FINISH_LEFT -> newVelocity.getX() <= 0;
            case FINISH_RIGHT -> newVelocity.getX() >= 0;
            default -> true;
        };
    }

    private List<PositionVector> getPositionVectorsWithCount(int count) {
        List<PositionVector> positionsWithExactCount = new ArrayList<>();

        for (int y = 0; y < pathPlanningGrid.length; y++) {
            for (int x = 0; x < pathPlanningGrid[y].length; x++) {
                if (pathPlanningGrid[y][x].getCount() == count) {
                    positionsWithExactCount.add(new PositionVector(x, y));
                }
            }
        }
        return positionsWithExactCount;
    }

    private static class GridElement {
        private int count;
        private PositionVector previousPosition;
        private PositionVector velocity;

        private GridElement(boolean isAccessible) {
            PositionVector nullVector = new PositionVector(0, 0);
            if (isAccessible) {
                count = Integer.MAX_VALUE;
            } else {
                count = Integer.MIN_VALUE;
            }
            this.previousPosition = nullVector;
            this.velocity = nullVector;
        }

        private void setCount(int count) {
            this.count = count;
        }

        private int getCount() {
            return count;
        }

        private PositionVector getPreviousPosition() {
            return previousPosition;
        }

        private void setPreviousPosition(PositionVector previousPosition) {
            this.previousPosition = previousPosition;
        }

        private PositionVector getVelocity() {
            return velocity;
        }

        private void setVelocity(PositionVector velocity) {
            this.velocity = velocity;
        }
    }
}
