package ch.zhaw.pm2.racetrack.exception;

/**
 * Used for invalid formatted Move-List and Track files.
 *
 * @author Team03 - Stackoverflow
 * @version 1.0
 */
public class InvalidFileFormatException extends Exception {

    /**
     * Constructs a new InvalidFileFormatException.
     *
     * @param message Is the message printed if the exception occurs.
     */
    public InvalidFileFormatException(String message) {
        super(message);
    }
}
