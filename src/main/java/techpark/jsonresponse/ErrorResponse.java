package techpark.jsonresponse;


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
