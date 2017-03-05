package techpark.jsonResponse;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;


/**
 * Created by Fedorova on 05.03.2017.
 */
public class ErrorResponse implements Message{
    private final HttpStatus code;
    private final String text;

    public ErrorResponse(HttpStatus code, String text){
        this.code = code;
        this.text = text;
    }


    @Override
    public ResponseEntity<?> getMessage(){
        return new ResponseEntity<>("{\"error\": \"" + this.text + "\"}", this.code);
    }
}
