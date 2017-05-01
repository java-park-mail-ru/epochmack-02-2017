package techpark.game.internal;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;
import techpark.game.GameSession;
import techpark.game.avatar.AOE;
import techpark.game.avatar.GameUser;
import techpark.game.avatar.ThroneDamage;
import techpark.game.base.ServerMazeSnap;
import techpark.game.base.ServerWaveSnap;
import techpark.resources.Generator;
import techpark.websocket.EventMessage;
import techpark.websocket.RemotePointService;

import java.io.IOException;
import java.util.List;


/**
 * Created by Варя on 22.04.2017.
 */
@SuppressWarnings("OverlyBroadCatchBlock")
@Service
public class ServerSnapshotService {
    private final RemotePointService remotePointService;
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final Generator generator;

    public ServerSnapshotService(RemotePointService remotePointService) {
        this.remotePointService = remotePointService;
        this.generator = new Generator();
    }

    @SuppressWarnings("OverlyBroadCatchBlock")
    public void sendSnapshotsFor(GameSession session){
        processFirstPart(session);
        boolean ready = true;
        for (GameUser gamer: session.getUsers())
            ready &= gamer.isReady();
        if (ready) {
            session.incrementWave();
            processSecondtPart(session);
        }
    }

    private void processFirstPart(GameSession session){
        final ServerMazeSnap serverSnap = new ServerMazeSnap();
        serverSnap.setMap(session.field.getMap());
        try {
            final EventMessage message = new EventMessage(ServerMazeSnap.class, objectMapper.writeValueAsString(serverSnap));
            for (GameUser player : session.getUsers()) {
                if(player.getAvaliableGems().size() == (int)generator.settings("gemsPerRound"))
                    serverSnap.setCombinatios(player.calculateCombinations(session.field.getAvaliableGems()));
                remotePointService.sendMessageToUser(player.getUser(), message);
            }
        } catch (IOException ex) {
            throw new RuntimeException("Failed sending snapshot", ex);
        }
    }

    private void processSecondtPart(GameSession session){
        final ServerWaveSnap serverSnap = new ServerWaveSnap();
        serverSnap.setRoute(session.field.calculateRoute());
        final AOE aoe = new AOE(session.getWave(), serverSnap.getRoute(), session.field.getAvaliableGems());
        serverSnap.setEnemyDamages(aoe.calulateEnemyDamage());
        final List<ThroneDamage> throneDamages = aoe.calulateThroneDamage();
        serverSnap.setThroneDamages(throneDamages);
        session.setPoints((int)generator.settings("numberOfEnemies") - aoe.getEnemies().size());
        serverSnap.setPoints(session.getPoints());
        serverSnap.setWave(session.getWave());
        if(throneDamages.get(throneDamages.size() - 1).getHp() <= 0.0)
            session.setGameOver();
        try {
            final EventMessage message = new EventMessage(ServerWaveSnap.class, objectMapper.writeValueAsString(serverSnap));
            for (GameUser player : session.getUsers())
                remotePointService.sendMessageToUser(player.getUser(), message);
        }
        catch (IOException ex) {
            throw new RuntimeException("Failed sending snapshot", ex);
        }
    }

}
