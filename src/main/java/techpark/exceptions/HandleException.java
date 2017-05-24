package techpark.exceptions;

/**
 * Created by Варя on 17.04.2017.
 */
public class HandleException extends Exception{
    public HandleException(String message, Throwable cause) {
        super(message, cause);
    }

    public HandleException(String message) {
        super(message);
    }
}
