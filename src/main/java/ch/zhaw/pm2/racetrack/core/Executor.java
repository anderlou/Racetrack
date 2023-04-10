package ch.zhaw.pm2.racetrack.core;

import ch.zhaw.pm2.racetrack.exception.InvalidFileFormatException;
import ch.zhaw.pm2.racetrack.given.Config;
import ch.zhaw.pm2.racetrack.model.Track;
import ch.zhaw.pm2.racetrack.strategy.DoNotMoveStrategy;
import ch.zhaw.pm2.racetrack.strategy.MoveListStrategy;
import ch.zhaw.pm2.racetrack.strategy.MoveStrategy;
import ch.zhaw.pm2.racetrack.strategy.MoveStrategy.StrategyType;
import ch.zhaw.pm2.racetrack.strategy.PathFinderStrategy;
import ch.zhaw.pm2.racetrack.strategy.PathFollowerMoveStrategy;
import ch.zhaw.pm2.racetrack.strategy.UserMoveStrategy;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Class Executor which executes the game and defines its flow.
 *
 * @author Team03 - Stackoverflow
 * @version 1.0
 */
public class Executor {
    private final UserInterface userInterface = new Communication();
    private final Config config = new Config();
    private boolean gameIsRunning;
    private Game game;
    private Track track;
    private final Map<Integer, Integer> numberOfMovesForEachCar = new HashMap<>();

    /**
     * This method starts the game.
     */
    public void start() {
        try {
            gameIsRunning = true;
            userInterface.outputWelcomeMessage();

            File trackFile = getTrackFile();

            if (trackFile == null) {
                userInterface.outputException("There was no track file found");
                endGame();
            } else {
                try {
                    this.track = new Track(trackFile);

                } catch (IOException | InvalidFileFormatException e) {
                    userInterface.outputException("Following error occurred, while trying to start the game: " + e.getCause().getMessage());
                    endGame();
                }
            }

            game = new Game(track);
            runGame();
        }catch (NullPointerException e){
            userInterface.outputException("Following error occurred: " + e.getMessage());
        }
        endGame();
    }

    private void runGame() {
        applyMoveStrategies();
        userInterface.outputPrintRaceTrack(track.toString());

        while (gameIsRunning) {
            int currentCarIndex = game.getCurrentCarIndex();
            MoveStrategy carMoveStrategy = game.getCarMoveStrategy(currentCarIndex);

            if (carMoveStrategy instanceof UserMoveStrategy) {
                userInterface.outputCurrentCarPlaying(game.getCarId(currentCarIndex));
            }
            game.doCarTurn(carMoveStrategy.nextMove());

            if (!(carMoveStrategy instanceof DoNotMoveStrategy)) {
                userInterface.outputPrintRaceTrack(track.toString());
            }

            int newNumberOfTurns = numberOfMovesForEachCar.get(currentCarIndex) + 1;
            numberOfMovesForEachCar.put(currentCarIndex, newNumberOfTurns);

            if (track.getCar(currentCarIndex).isCrashed()) {
                userInterface.outputCarCrashed(game.getCarId(currentCarIndex));
            }

            if (game.getWinner() != -1) {
                gameIsRunning = false;
                userInterface.outputWinner(game.getCarId(game.getWinner()));
                userInterface.outputAmountOfTurnsNeededToWin(numberOfMovesForEachCar.get(game.getWinner()));
            } else if (game.onlyCarsWithDoNotMoveStrategyRemaining()) {
                gameIsRunning = false;
                userInterface.outputTie();
            } else {
                game.switchToNextActiveCar();
            }
        }
    }

    private void endGame() {
        boolean startNewGame = userInterface.inputStartNewGame();
        if (startNewGame) {
            this.start();
        } else {
            userInterface.outputExitGame();
        }
    }

    private File getTrackFile() {
        String[] trackArray = config.getTrackDirectory().list();

        File fileToBeReturned = null;

        if (trackArray != null && trackArray.length != 0) {
            List<String> trackList = new ArrayList<>(List.of(trackArray));
            trackList.removeIf(s -> !s.endsWith(".txt"));

            if (trackList.size() != 0) {
                String trackOptions = getOptionString(trackList);
                int trackNumber = userInterface.inputChooseRacetrack(trackOptions, trackList.size());
                fileToBeReturned = new File(config.getTrackDirectory(), trackList.get(trackNumber));
            }
        }
        return fileToBeReturned;
    }

    private void applyMoveStrategies() {
        boolean notAllCarsHaveDoNotMoveStrategy = false;
        for (int currentCarIndex = 0; currentCarIndex < game.getCarCount(); currentCarIndex++) {

            boolean strategyAppliedSuccessfully = false;
            MoveStrategy moveStrategy = new DoNotMoveStrategy();
            while (!strategyAppliedSuccessfully) {
                StrategyType strategyType = userInterface.inputChooseStrategyForCar(game.getCarId(currentCarIndex));

                switch (strategyType) {
                    case USER -> {
                        moveStrategy = new UserMoveStrategy(userInterface);
                        strategyAppliedSuccessfully = true;
                        notAllCarsHaveDoNotMoveStrategy = true;
                    }

                    case MOVE_LIST -> {
                        String[] moveListFilesArray = config.getMoveDirectory().list();

                        if (moveListFilesArray == null || moveListFilesArray.length == 0) {
                            userInterface.outputException("There was no file found to read your moves from!");
                            userInterface.outputChooseDifferentStrategy();
                        } else {
                            List<String> moveListFiles = new ArrayList<>(List.of(moveListFilesArray));
                            String fileOptions = getOptionString(moveListFiles);

                            int fileIndex = userInterface.inputChooseMoveListStrategyFile(fileOptions, moveListFiles.size());
                            File file = new File(config.getMoveDirectory(), moveListFiles.get(fileIndex));

                            try {
                                moveStrategy = new MoveListStrategy(file);
                                strategyAppliedSuccessfully = true;
                                notAllCarsHaveDoNotMoveStrategy = true;
                            } catch (InvalidFileFormatException | IOException e) {
                                userInterface.outputException("The Strategy could not be properly applied.");
                                userInterface.outputChooseDifferentStrategy();
                            }
                        }
                    }
                    case DO_NOT_MOVE -> {
                        if (currentCarIndex == game.getCarCount() - 1 && !notAllCarsHaveDoNotMoveStrategy) {
                            userInterface.outputWarningDoNotMoveStrategy();
                            userInterface.outputChooseDifferentStrategy();
                        } else {
                            moveStrategy = new DoNotMoveStrategy();
                            strategyAppliedSuccessfully = true;
                        }
                    }

                    case PATH_FOLLOWER -> {
                        String[] pathFollowerFilesArray = config.getFollowerDirectory().list();

                        if (pathFollowerFilesArray == null || pathFollowerFilesArray.length == 0) {
                            userInterface.outputException("There was no file found to read your path from!");
                            userInterface.outputChooseDifferentStrategy();
                        } else {
                            List<String> moveListFiles = new ArrayList<>(List.of(pathFollowerFilesArray));
                            String fileOptions = getOptionString(moveListFiles);

                            int fileIndex = userInterface.inputChooseFollowerStrategyFile(fileOptions, moveListFiles.size());
                            File file = new File(config.getFollowerDirectory(), moveListFiles.get(fileIndex));

                            try {
                                moveStrategy = new PathFollowerMoveStrategy(file, game.getCarPosition(currentCarIndex));
                                strategyAppliedSuccessfully = true;
                                notAllCarsHaveDoNotMoveStrategy = true;
                            } catch (InvalidFileFormatException | IOException e) {
                                userInterface.outputException("The Strategy could not be properly applied. Following error occurred: " + e.getMessage());
                                userInterface.outputChooseDifferentStrategy();
                            }
                        }
                    }

                    case PATH_FINDER -> {
                        moveStrategy = new PathFinderStrategy(track, currentCarIndex);
                        strategyAppliedSuccessfully = true;
                        notAllCarsHaveDoNotMoveStrategy = true;
                    }

                    default -> {
                        userInterface.outputException("The Strategy could not be properly applied.");
                        userInterface.outputChooseDifferentStrategy();
                    }
                }
            }
            game.setCarMoveStrategy(currentCarIndex, moveStrategy);
            numberOfMovesForEachCar.put(currentCarIndex, 0);
        }
    }

    private String getOptionString(List<String> optionList) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < optionList.size(); i++) {
            sb.append(i).append(": ").append(optionList.get(i)).append("\n");
        }
        return sb.toString();
    }
}
