package techpark.game;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.CloseStatus;
import techpark.exceptions.AccountServiceDBException;
import techpark.game.base.ClientSnap;
import techpark.game.internal.ClientSnapshotsService;
import techpark.game.internal.ServerSnapshotService;
import techpark.game.request.InitGame;
import techpark.service.AccountService;
import techpark.user.UserProfile;
import techpark.websocket.EventMessage;
import techpark.websocket.RemotePointService;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by Варя on 18.04.2017.
 */
@Service
public class GameMechanics {
    private final Logger logger = LoggerFactory.getLogger(getClass());
    private RemotePointService remotePointService;
    private ClientSnapshotsService clientSnapshotsService;
    private ServerSnapshotService serverSnapshotService;
    private AccountService accountService;
    private Queue<UserProfile> waiters = new ConcurrentLinkedQueue<>();
    private Map<UserProfile, GameSession> sessions = new ConcurrentHashMap<>();
    private final ExecutorService executor = Executors.newSingleThreadExecutor();

    @Autowired
    public GameMechanics(RemotePointService remotePointService, ClientSnapshotsService clientSnapshotsService,
                           AccountService accountService, ServerSnapshotService serverSnapshotService) {
        this.remotePointService = remotePointService;
        this.clientSnapshotsService = clientSnapshotsService;
        this.accountService = accountService;
        this.serverSnapshotService = serverSnapshotService;
    }

    public void addPlayer(UserProfile user){
        if(!waiters.contains(user) && !sessions.containsKey(user)){
            waiters.add(user);
            searchGame();
        }
    }

    public void addClientSnapshot(UserProfile userProfile, ClientSnap snap) {
        executor.submit(() -> processAction(userProfile, snap));
    }

    public void searchGame(){
        waiters.removeIf(userProfile -> !remotePointService.isConnected(userProfile));
        while (waiters.size() >= Config.numberOfPlayers){
            final List<UserProfile> players = new ArrayList<>();
            for(int i = 0 ; i < Config.numberOfPlayers; i++)
                players.add(waiters.poll());
            final GameSession session = new GameSession(players);
            try {
                startGameforSession(session);
                for(UserProfile player: session.getPlayers())
                    sessions.put(player, session);
            } catch (IOException e) {
                logger.error("failed to send initial message", e);
                for(UserProfile player: session.getPlayers())
                    remotePointService.closeConnection(player, CloseStatus.SERVER_ERROR);
            }

        }
    }

    @SuppressWarnings("OverlyBroadThrowsClause")
    public void startGameforSession(GameSession session) throws IOException {
        final InitGame.Request initMessage = new InitGame.Request();
        final List<String> players = new ArrayList<>();
        for (UserProfile player : session.getPlayers())
            players.add(player.getLogin());
        initMessage.setPlayers(players);
        final ObjectMapper objectMapper = new ObjectMapper();
        final EventMessage message = new EventMessage(InitGame.Request.class.getName(),
                objectMapper.writeValueAsString(initMessage));
        for (UserProfile player : session.getPlayers())
            remotePointService.sendMessageToUser(player, message);
    }

    public void processAction(UserProfile userProfile, ClientSnap snap){
        final GameSession gameSession = sessions.get(userProfile);
        clientSnapshotsService.processSnapshotsFor(gameSession,  snap);
        serverSnapshotService.sendSnapshotsFor(gameSession, snap.isStartWave()? 1 : 2);
        if(gameSession.isGameOver())
            endGame(userProfile, gameSession);
    }

    public void endGame(UserProfile user, GameSession session){
        sessions.remove(user);
        remotePointService.closeConnection(user, CloseStatus.NORMAL);
        try {
            accountService.changeScore(user, session.getPoints());
        } catch (AccountServiceDBException e) {
            e.printStackTrace();
        }
    }

}
