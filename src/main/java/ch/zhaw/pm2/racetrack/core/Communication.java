package ch.zhaw.pm2.racetrack.core;

import ch.zhaw.pm2.racetrack.given.Direction;
import ch.zhaw.pm2.racetrack.strategy.MoveStrategy;
import org.beryx.textio.TextIO;
import org.beryx.textio.TextIoFactory;
import org.beryx.textio.TextTerminal;

import java.awt.Color;

/**
 * Class Communication which outputs in game messages and reads  user input.
 *
 * @author Team03 - Stackoverflow
 * @version 1.0
 */
public class Communication implements UserInterface {
    private final TextIO textIO = TextIoFactory.getTextIO();
    private final TextTerminal<?> textTerminal = textIO.getTextTerminal();

    /**
     * Constructs a new Object of the class Communication.
     */
    public Communication() {
        textTerminal.getProperties().setPromptColor(new Color(255, 255, 255));
    }

    /**
     * Is the method which lets you print the selected racetrack file in console.
     *
     * @param racetrackOutput The chosen racetrack file.
     */
    @Override
    public void outputPrintRaceTrack(String racetrackOutput) {
        try {
            Thread.sleep(350);
        } catch (InterruptedException e) {
            outputException("Following error occurred while trying to display the track: " + e.getMessage());
        }
        textTerminal.resetToBookmark("display");
        textTerminal.print(racetrackOutput);
    }

    /**
     * Is the method that prints in console who won the game.
     *
     * @param car The winning car.
     */
    @Override
    public void outputWinner(char car) {
        textTerminal.println("Car " + car + " won the race!");
    }

    /**
     * Is the method that prints out the welcome message in console.
     */
    @Override
    public void outputWelcomeMessage() {
        textTerminal.resetToBookmark("display");
        textTerminal.setBookmark("display");
        textTerminal.println("Welcome to racetrack!");
    }

    /**
     * Is the method that prints a message that the player should choose a different strategy.
     */
    @Override
    public void outputChooseDifferentStrategy() {
        textTerminal.println("Please choose a different strategy.");
    }

    /**
     * Is the method that prints in console who crashed their car.
     *
     * @param car The car that crashed.
     */
    @Override
    public void outputCarCrashed(char car) {
        textTerminal.println("Car " + car + " crashed and is disqualified!");
    }

    /**
     * Is the method that prints whose turn it is.
     *
     * @param car Is the car currently playing.
     */
    @Override
    public void outputCurrentCarPlaying(char car) {
        textTerminal.println("Playing as car " + car + ": ");
    }

    /**
     * Is the method that prints how many turns it took the winner to win.
     *
     * @param amountOfTurns Is the amount of turns it took.
     */
    @Override
    public void outputAmountOfTurnsNeededToWin(int amountOfTurns) {
        textTerminal.println("It took you " + amountOfTurns + " turns to win the game.");
    }

    /**
     * Is the method that prints that the game has been closed.
     */
    @Override
    public void outputExitGame() {
        textTerminal.dispose();
        textIO.dispose();
    }

    /**
     * Is the method that prints that not all cars can have the 'do not move' strategy.
     */
    @Override
    public void outputWarningDoNotMoveStrategy() {
        textTerminal.println("Not every car can use the 'Do not move' strategy, please choose a different one.");
    }

    /**
     * Is the method that prints out the exceptions.
     *
     * @param exceptionText is the text of the printed exception.
     */
    @Override
    public void outputException(String exceptionText) {
        textTerminal.getProperties().setPromptColor(new Color(255, 0, 0));
        textTerminal.println(exceptionText);
        textTerminal.getProperties().setPromptColor(new Color(255, 255, 255));
    }

    /**
     * This method prints out that the game ended in a tie.
     */
    @Override
    public void outputTie() {
        textTerminal.println("No car could win the game, the game ended in a tie!");
    }

    /**
     * Is the method that lets the user choose which racetrack they want to play on.
     *
     * @param trackNames String of the different names of the track files to print out.
     * @param length     Is the length of the array of the track names.
     * @return Returns the number of the chosen racetrack.
     */
    @Override
    public int inputChooseRacetrack(String trackNames, int length) {
        return textIO.newIntInputReader().withMinVal(0).withMaxVal(length - 1).read("Choose a racetrack:" + "\n" + trackNames);
    }

    /**
     * Is the method that lets the user choose which MoveListStrategy to choose for one car.
     *
     * @param fileNames String of filenames to print out.
     * @param length    Is the length of the array of the filenames.
     * @return Returns number chosen.
     */
    @Override
    public int inputChooseMoveListStrategyFile(String fileNames, int length) {
        return textIO.newIntInputReader().withMinVal(0).withMaxVal(length - 1).read("Choose a file for the MoveListStrategy:" + "\n" + fileNames);
    }

    /**
     * Is the method that lets the user choose which file should be used for the path follower.
     *
     * @param fileNames String of filenames to print out.
     * @param length    Is the length of the array of the filenames.
     * @return Returns number chosen.
     */
    @Override
    public int inputChooseFollowerStrategyFile(String fileNames, int length) {
        return textIO.newIntInputReader().withMinVal(0).withMaxVal(length - 1).read("Choose a file for the PathFollowerStrategy:" + "\n" + fileNames);

    }

    /**
     * Is the method that lets the user choose which move strategy they want for a certain car.
     *
     * @param carNumber Is the number of the car for which the user needs to choose a strategy for.
     * @return Returns the move strategy chosen for the car.
     */
    @Override
    public MoveStrategy.StrategyType inputChooseStrategyForCar(char carNumber) {
        return textIO.newEnumInputReader(MoveStrategy.StrategyType.class).read("Choose a move strategy for car " + carNumber + ":");
    }

    /**
     * Is the method that lets the user choose direction for choosing a velocity direction.
     *
     * @return Returns the direction chosen by the user.
     */
    @Override
    public Direction inputVelocityDirection() {
        return textIO.newEnumInputReader(Direction.class).read("Choose a velocity direction: ");
    }

    /**
     * Is the method that lets the user choose if they want to continue the game or not.
     *
     * @return Returns true if game should continue and no if it shouldn't.
     */
    @Override
    public boolean inputStartNewGame() {
        return textIO.newBooleanInputReader().withTrueInput("yes").withFalseInput("no").read("Do you want to start a new game? If not, the application will close");
    }
}
