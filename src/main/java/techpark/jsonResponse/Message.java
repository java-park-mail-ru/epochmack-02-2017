package techpark.jsonResponse;

import org.springframework.http.ResponseEntity;

/**
 * Created by Fedorova on 05.03.2017.
 */
public interface Message {
    ResponseEntity<?> getMessage();
}
