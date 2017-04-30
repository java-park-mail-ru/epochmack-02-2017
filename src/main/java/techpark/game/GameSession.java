package techpark.game;

import techpark.game.avatar.Field;
import techpark.game.avatar.GameUser;
import techpark.user.UserProfile;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Варя on 18.04.2017.
 */
@SuppressWarnings("PublicField")
public class GameSession {
    private List<GameUser> users;
    private int wave;
    private int points;
    private boolean gameOver;
    public final Field field;

    public GameSession(List<UserProfile> players) {
        users = new ArrayList<>();
        for(UserProfile user: players){
            users.add(new GameUser(user));
        }
        this.wave = 0;
        this.points = 0;
        this.field = new Field();
        this.gameOver = false;
    }

    public List<GameUser> getUsers() {
        return users;
    }

    public void incrementWave(){this.wave++;}

    public void setPoints(int points) {
        this.points += points;
    }

    public int getPoints() {
        return points;
    }

    public int getWave() {
        return wave;
    }

    public boolean isGameOver() {
        return gameOver;
    }

    public void setGameOver() {
        this.gameOver = true;
    }

    public GameUser getSelf(UserProfile user){
        for (GameUser gamer: users){
            if(gamer.getUser().equals(user))
                return gamer;
        }
        throw new IllegalArgumentException("Request self for game but user not participate it");
    }

}
