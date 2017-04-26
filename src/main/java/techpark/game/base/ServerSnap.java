package techpark.game.base;

import techpark.game.avatar.Square;

import java.util.HashMap;

/**
 * Created by Варя on 22.04.2017.
 */
@SuppressWarnings("unused")
public class ServerSnap {
    private char[][] map;
    private HashMap<Square, Character> combinations;

    public void setMap(char[][] map) {
        this.map = map;
    }

    public void setCombinatios(HashMap<Square, Character> combinations) {
        this.combinations = combinations;
    }

    public HashMap<Square, Character> getCombinatios() {
        return combinations;
    }

    public char[][] getMap() {
        return map;
    }
}
