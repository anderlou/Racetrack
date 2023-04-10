package ch.zhaw.pm2.racetrack;

import ch.zhaw.pm2.racetrack.core.Executor;

/**
 * Class App which starts a new racetrack game.
 *
 * @author Team03 - Stackoverflow
 * @version 1.0
 */
public class App {
    /**
     * Creates a new racetrack executor and runs it.
     *
     * @param args an array of command-line arguments for the application.
     */
    public static void main(String[] args) {
        Executor executor = new Executor();
        executor.start();
    }
}
