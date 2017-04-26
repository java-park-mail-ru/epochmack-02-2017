package techpark.websocket;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.eclipse.jetty.websocket.api.WebSocketException;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import techpark.user.UserProfile;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by Варя on 17.04.2017.
 */
@Service
public class RemotePointService {
    private Map<UserProfile, WebSocketSession> sessions = new ConcurrentHashMap<>();
    private ObjectMapper objectMapper = new ObjectMapper();

    public void registerUser(@NotNull UserProfile user, @NotNull WebSocketSession webSocketSession) {
        sessions.put(user, webSocketSession);
    }

    public boolean isConnected(@NotNull UserProfile user) {
        return sessions.containsKey(user) && sessions.get(user).isOpen();
    }

    public void removeUser(@NotNull UserProfile user)
    {
        sessions.remove(user);
    }

    public void closeConnection(@NotNull UserProfile user, @NotNull CloseStatus closeStatus) {
        final WebSocketSession webSocketSession = sessions.get(user);
        if (webSocketSession != null && webSocketSession.isOpen()) {
            try {
                webSocketSession.close(closeStatus);
            } catch (IOException ignore) {
            }
        }
    }

    public void sendMessageToUser(@NotNull UserProfile user, @NotNull EventMessage message) throws IOException {
        final WebSocketSession webSocketSession = sessions.get(user);
        if (webSocketSession == null) {
            throw new IOException("no game websocket for user " + user);
        }
        if (!webSocketSession.isOpen()) {
            throw new IOException("session is closed or not exsists");
        }
        try {
            webSocketSession.sendMessage(new TextMessage(objectMapper.writeValueAsString(message)));
        } catch (JsonProcessingException | WebSocketException e) {
            throw new IOException("Unnable to send message", e);
        }
    }
}
