package techpark.game.request;

import java.util.List;

/**
 * Created by Варя on 18.04.2017.
 */
public class InitGame {
    @SuppressWarnings("unused")
    public static final class Request {
        private List<String> players;

        public void setPlayers(List<String> players) {
            this.players = players;
        }

        public List<String> getPlayers() {
            return players;
        }
    }
}
