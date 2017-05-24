package techpark.game.base;

import techpark.game.avatar.Square;

import java.util.HashMap;
import java.util.List;

/**
 * Created by Варя on 22.04.2017.
 */
@SuppressWarnings("unused")
public class ServerMazeSnap {
    private char[][] map;
    private String user;
    private HashMap<Square, List<Character>> combinations;

    public void setMap(char[][] map) {
        this.map = map;
    }

    public void setCombinatios(HashMap<Square, List<Character>> combinations) {
        this.combinations = combinations;
    }

    public HashMap<Square, List<Character>> getCombinatios() {
        return combinations;
    }

    public char[][] getMap() {
        return map;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }
}
