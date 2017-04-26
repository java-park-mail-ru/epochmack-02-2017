package techpark.game.internal;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;
import techpark.game.Config;
import techpark.game.GameSession;
import techpark.game.avatar.Field;
import techpark.game.avatar.ThroneDamage;
import techpark.game.base.ServerSnap;
import techpark.game.base.ServerSnapPart2;
import techpark.user.UserProfile;
import techpark.websocket.EventMessage;
import techpark.websocket.RemotePointService;

import java.io.IOException;
import java.util.List;


/**
 * Created by Варя on 22.04.2017.
 */
@Service
public class ServerSnapshotService {
    private final RemotePointService remotePointService;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public ServerSnapshotService(RemotePointService remotePointService) {
        this.remotePointService = remotePointService;
    }

    @SuppressWarnings("OverlyBroadCatchBlock")
    public void sendSnapshotsFor(GameSession session, int round){
        final Object snap;
        final String className;
        final List<UserProfile> players= session.getPlayers();
        if(round == 1) {
            snap = processFirstPart(session);
            className = ServerSnap.class.getName();
        }
        else{
            snap = processSecondtPart(session);
            className = ServerSnapPart2.class.getName();
        }
        try {
            final EventMessage message = new EventMessage(className, objectMapper.writeValueAsString(snap));
            for (UserProfile player : players) {
                remotePointService.sendMessageToUser(player, message);
            }
        } catch (IOException ex) {
            throw new RuntimeException("Failed sending snapshot", ex);
        }

    }

    private ServerSnap processFirstPart(GameSession session){
        final ServerSnap serverSnap = new ServerSnap();
        serverSnap.setMap(session.field.getMap());
        if(session.getGems().size() == 6)
            serverSnap.setCombinatios(session.field.calculateCombinations());
        return serverSnap;
    }

    private ServerSnapPart2 processSecondtPart(GameSession session){
        final ServerSnapPart2 serverSnap = new ServerSnapPart2();
        serverSnap.setRoute(session.field.calculateRoute());
        final Field.AOE aoe = session.field.new AOE(session.getWave(), serverSnap.getRoute());
        serverSnap.setEnemyDamages(aoe.calulateEnemyDamage());
        final List<ThroneDamage> throneDamages = aoe.calulateThroneDamage();
        serverSnap.setThroneDamages(throneDamages);
        session.setPoints(Config.NUMBERENEMY - aoe.getEnemies().size());
        serverSnap.setPoints(session.getPoints());
        if(throneDamages.get(throneDamages.size() - 1).getHp() <= 0.0)
            session.setGameOver();
        return serverSnap;
    }

}
