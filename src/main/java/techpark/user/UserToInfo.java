package techpark.user;

/**
 * Created by yaches on 12.03.17.
 */
public class UserToInfo {
    public final String login;
    public final int score;

    public UserToInfo(UserProfile user) {
        this.login = user.getLogin();
        this.score = user.getScore();
    }
}
