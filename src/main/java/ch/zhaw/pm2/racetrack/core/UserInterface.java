package ch.zhaw.pm2.racetrack.core;

import ch.zhaw.pm2.racetrack.given.Direction;
import ch.zhaw.pm2.racetrack.strategy.MoveStrategy;

/**
 * Interface which defines Input and Output methods.
 *
 * @author Team03 - Stackoverflow
 * @version 1.0
 */
public interface UserInterface {
    /**
     * Is the method which lets you print the selected racetrack file.
     *
     * @param racetrackOutput The chosen racetrack file.
     */
    void outputPrintRaceTrack(String racetrackOutput);

    /**
     * Is the method that lets the player know who won the game.
     *
     * @param car The winning car.
     */
    void outputWinner(char car);

    /**
     * Is the method that prints out the welcome message.
     */
    void outputWelcomeMessage();

    /**
     * Is the method that prints a message that the player should choose a different strategy.
     */
    void outputChooseDifferentStrategy();

    /**
     * Is the method that prints who crashed their car.
     *
     * @param car The car that crashed.
     */
    void outputCarCrashed(char car);

    /**
     * Is the method that prints whose turn it is.
     *
     * @param car Is the car currently playing.
     */

    void outputCurrentCarPlaying(char car);

    /**
     * Is the method that prints how many turns it took the winner to win.
     *
     * @param amountOfTurns Is the amount of turns it took.
     */

    void outputAmountOfTurnsNeededToWin(int amountOfTurns);

    /**
     * Is the method that prints that the game has been closed.
     */
    void outputExitGame();

    /**
     * Is the method that prints that not all cars can have the 'do not move' strategy.
     */
    void outputWarningDoNotMoveStrategy();

    /**
     * Is the method that prints out the exceptions.
     *
     * @param exceptionText is the text of the printed exception.
     */
    void outputException(String exceptionText);

    /**
     * Is the method that lets the player know, that the game ended in a tie.
     */
    void outputTie();

    /**
     * Is the method that lets the user choose which racetrack they want to play on.
     *
     * @param trackNames String of the different names of the track files to print out.
     * @param length     Is the length of the array of the track names.
     * @return Returns the number of the chosen racetrack.
     */
    int inputChooseRacetrack(String trackNames, int length);

    /**
     * Is the method that lets the user choose which MoveListStrategy to choose for one car.
     *
     * @param fileNames String of filenames to print out.
     * @param length    Is the length of the array of the filenames.
     * @return Returns number chosen.
     */
    int inputChooseMoveListStrategyFile(String fileNames, int length);

    /**
     * Is the method that lets the user choose which file should be used for the path follower.
     *
     * @param fileNames String of filenames to print out.
     * @param length    Is the length of the array of the filenames.
     * @return Returns number chosen.
     */
    int inputChooseFollowerStrategyFile(String fileNames, int length);

    /**
     * Is the method that lets the user choose which move strategy they want for a certain car.
     *
     * @param carNumber Is the number of the car for which the user needs to choose a strategy for.
     * @return Returns the move strategy chosen for the car.
     */
    MoveStrategy.StrategyType inputChooseStrategyForCar(char carNumber);

    /**
     * Is the method that lets the user choose direction for choosing a velocity direction.
     *
     * @return Returns the direction chosen by the user.
     */
    Direction inputVelocityDirection();

    /**
     * Is the method that lets the user choose if they want to continue the game or not.
     *
     * @return Returns true if game should continue and no if it shouldn't.
     */
    boolean inputStartNewGame();
}
