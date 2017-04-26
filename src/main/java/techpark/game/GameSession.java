package techpark.game;

import techpark.game.avatar.Field;
import techpark.game.avatar.Square;
import techpark.user.UserProfile;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Варя on 18.04.2017.
 */
@SuppressWarnings("PublicField")
public class GameSession {
    private List<UserProfile> players;
    private int wave;
    private List<Square> gemsToStones;
    private int points;
    private boolean gameOver;
    public final Field field;

    public GameSession(List<UserProfile> players) {
        this.players = players;
        this.wave = 0;
        this.points = 0;
        this.gemsToStones = new ArrayList<>();
        this.field = new Field();
        this.gameOver = false;
    }

    public List<UserProfile> getPlayers() {
        return players;
    }

    public void setGem(Square square){
        gemsToStones.add(square);
    }

    public void removeGem(Square square){
        gemsToStones.remove(square);
    }

    public void clearGems(){
        gemsToStones.clear();
    }

    public void incrementWave(){this.wave++;}

    public void setPoints(int points) {
        this.points += points;
    }

    public int getPoints() {
        return points;
    }

    public List<Square> getGems() {
        return gemsToStones;
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
}
