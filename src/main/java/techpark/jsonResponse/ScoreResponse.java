package techpark.jsonResponse;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

/**
 * Created by Fedorova on 05.03.2017.
 */
public class ScoreResponse implements Message {
    int score;

    public ScoreResponse(int score){
        this.score = score;
    }

    @Override
    public ResponseEntity<?> getMessage() {
        return new ResponseEntity<>("{\"score\": \"" + this.score + "\"}", HttpStatus.OK);
    }
}
