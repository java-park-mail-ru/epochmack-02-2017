package techpark.jsonResponse;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

/**
 * Created by Fedorova on 05.03.2017.
 */
public class LoginResponse implements Message{
    final String login;

    public LoginResponse(String login){
        this.login = login;
    }

    @Override
    public ResponseEntity<?> getMessage(){
        return new ResponseEntity<>("{\"login\": \"" + this.login + "\"}", HttpStatus.OK);
    }
}
