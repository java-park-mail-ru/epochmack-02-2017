package techpark.jsonresponse;

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
