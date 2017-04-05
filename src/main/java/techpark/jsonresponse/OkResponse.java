package techpark.jsonResponse;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

/**
 * Created by Fedorova on 05.03.2017.
 */
public class OkResponse extends Response {
    public OkResponse() {
        super("ok");
    }
}
