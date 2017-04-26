package techpark.game.base;

import techpark.game.avatar.Square;

/**
 * Created by Варя on 22.04.2017.
 */
@SuppressWarnings("unused")
public class ClientSnap {
    private Square square;
    private Character comb;
    private boolean startWave;

    public void setStartWave(boolean startWave) {
        this.startWave = startWave;
    }

    public void setComb(Character comb) {
        this.comb = comb;
    }

    public Character getComb() {
        return comb;
    }

    public void setSquare(Square square) {
        this.square = square;
    }

    public Square getSquare() {
        return square;
    }

    public boolean isStartWave() {
        return startWave;
    }
}
