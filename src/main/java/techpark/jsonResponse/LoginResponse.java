package techpark.jsonResponse;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

/**
 * Created by Fedorova on 05.03.2017.
 */
public class LoginResponse extends Response {
    public final String login;

    public LoginResponse(String login) {
        super("ok");
        this.login = login;
    }
}
