package techpark.websocket;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;
import techpark.exceptions.AccountServiceDBException;
import techpark.exceptions.HandleException;
import techpark.game.GameMechanics;
import techpark.service.AccountService;
import techpark.user.UserProfile;

import javax.naming.AuthenticationException;
import java.io.IOException;

/**
 * Created by Варя on 17.04.2017.
 */
public class GameSocketHandler extends TextWebSocketHandler{
    private static final Logger LOGGER = LoggerFactory.getLogger(GameSocketHandler.class);

    @NotNull
    private AccountService accountService;
    @NotNull
    private final EventHandlerContainer eventHandlerContainer;
    @NotNull
    private final RemotePointService remotePointService;
    @NotNull
    private final GameMechanics gameMechanics;

    private final ObjectMapper objectMapper = new ObjectMapper();


    public GameSocketHandler(@NotNull EventHandlerContainer eventHandlerContainer,@NotNull AccountService authService,
                             @NotNull RemotePointService remotePointService, @NotNull GameMechanics gameMechanics) {
        this.eventHandlerContainer = eventHandlerContainer;
        this.accountService = authService;
        this.remotePointService = remotePointService;
        this.gameMechanics = gameMechanics;
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession webSocketSession) throws AuthenticationException,
            AccountServiceDBException {
        final String login = (String) webSocketSession.getAttributes().get("Login");
        final UserProfile user;
        if (login == null || (user = accountService.getUserByLogin(login)) == null) {
            throw new AuthenticationException("Only authenticated users allowed to play a game");
        }
        remotePointService.registerUser(user, webSocketSession);
    }

    @SuppressWarnings("OverlyBroadCatchBlock")
    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws AuthenticationException
            , AccountServiceDBException {
        final String login = (String) session.getAttributes().get("Login");
        final UserProfile user;
        if (login == null || (user = accountService.getUserByLogin(login)) == null) {
            throw new AuthenticationException("Only authenticated users allowed to play a game");
        }
        final EventMessage event;
        try {
            event = objectMapper.readValue(message.getPayload(), EventMessage.class);
        } catch (IOException ex) {
            LOGGER.error("wrong json format at ping response", ex);
            return;
        }
        try {
            eventHandlerContainer.handle(event, user);
        } catch (HandleException e) {
            LOGGER.error("Can't handle message of type " + event.getType() + " with content: " + event.getContent(), e);
        }
    }


    @Override
    public void handleTransportError(WebSocketSession webSocketSession, Throwable throwable) throws Exception {
        LOGGER.warn("Websocket transport problem", throwable);
    }

    @Override
    public void afterConnectionClosed(WebSocketSession webSocketSession, CloseStatus closeStatus)
            throws AccountServiceDBException {
        final String login = (String) webSocketSession.getAttributes().get("Login");
        final UserProfile user;
        if (login == null || (user = accountService.getUserByLogin(login)) == null || !remotePointService.isConnected(user)) {
            LOGGER.error("User disconnected but his session was not found (closeStatus=" + closeStatus + ')');
            return;
        }
        gameMechanics.endGameFor(user);
        remotePointService.closeConnection(user, closeStatus);
        remotePointService.removeUser(user);
    }

    @Override
    public boolean supportsPartialMessages() {
        return false;
    }
}
