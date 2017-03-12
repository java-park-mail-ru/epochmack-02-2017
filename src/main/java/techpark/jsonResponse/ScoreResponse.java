package techpark.jsonResponse;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

/**
 * Created by Fedorova on 05.03.2017.
 */
public class ScoreResponse extends Response {
    public final int score;

    public ScoreResponse(int score){
        super("ok");
        this.score = score;
    }
}
