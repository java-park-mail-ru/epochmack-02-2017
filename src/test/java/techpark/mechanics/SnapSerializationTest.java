package techpark.mechanics;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import techpark.game.avatar.Enemy;
import techpark.game.avatar.EnemyDamage;
import techpark.game.avatar.Square;
import techpark.game.base.ClientSnap;
import techpark.game.base.ServerMazeSnap;
import techpark.game.base.ServerWaveSnap;
import techpark.game.request.InitGame;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;

/**
 * Created by Варя on 27.04.2017.
 */
@SuppressWarnings({"MagicNumber", "OverlyBroadThrowsClause"})
public class SnapSerializationTest {

    @Test
    public void clientSnapTest() throws IOException {
        final String clientSnapStr =
                "{ " +
                        "\"square\":{" +
                        "\"x\": 2.0," +
                        "\"y\": 3.0" +
                        "}, " +
                        "\"comb\":\"\"," +
                        "\"startWave\":\"false\"" +
                        '}';
        final ObjectMapper objectMapper = new ObjectMapper();
        final ClientSnap clientSnap = objectMapper.readValue(clientSnapStr, ClientSnap.class);
        objectMapper.writeValueAsString(clientSnap);
    }

    @Test
    public void serverMazeSnapTest() throws IOException {
        final ServerMazeSnap serverMazeSnap = new ServerMazeSnap();
        final char[][] map = {{'o','o','o','o','o','o','o','o','o','o'},
                {'o','o','o','o','#','o','o','o','o','o'},
                {'o','o','o','o','#','o','o','o','o','o'},
                {'o','o','o','o','#','o','o','o','o','o'},
                {'o','o','o','o','#','o','o','o','o','o'},
                {'o','o','o','o','#','o','o','o','o','o'},
                {'o','o','o','o','k','o','o','o','o','o'},
                {'o','o','o','o','o','o','o','o','o','o'},
                {'o','o','o','o','o','o','o','o','o','o'},
                {'o','o','o','o','o','o','o','o','o','o'}};
        serverMazeSnap.setMap(map);
        serverMazeSnap.setCombinatios(new HashMap<>());
        final ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.writeValueAsString(serverMazeSnap);
    }

    @Test
    public void serverWaveSnapTest() throws IOException {
        final ServerWaveSnap serverWaveSnap = new ServerWaveSnap();
        serverWaveSnap.setPoints(10);
        serverWaveSnap.setWave(2);
        serverWaveSnap.setRoute(Arrays.asList(new Square(3,1), new Square(5, 6), new Square(9, 9)));
        serverWaveSnap.setEnemyDamages(Arrays.asList(
                new EnemyDamage(4.5, 2.5, new Enemy(90.0, 1, 0.5)),
                new EnemyDamage(2.7, 1.5, new Enemy(80.0, 1, 0.5))));
        final ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.writeValueAsString(serverWaveSnap);
    }

    @Test
    public void serverInitTest() throws IOException {
        final InitGame.Request initGame = new InitGame.Request();
        initGame.setPlayers(Arrays.asList("login1", "login2"));
        final ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.writeValueAsString(initGame);
    }
}
