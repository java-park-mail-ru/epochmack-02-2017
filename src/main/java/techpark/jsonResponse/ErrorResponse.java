package techpark.jsonResponse;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;


/**
 * Created by Fedorova on 05.03.2017.
 */
public class ErrorResponse extends Response {
    public final String error;

    public ErrorResponse(String text) {
        super("error");
        this.error = text;
    }
}
