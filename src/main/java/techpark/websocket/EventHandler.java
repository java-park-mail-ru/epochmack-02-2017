package techpark.websocket;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.jetbrains.annotations.NotNull;
import techpark.exceptions.HandleException;
import techpark.user.UserProfile;

import java.io.IOException;

/**
 * Created by Варя on 17.04.2017.
 */
public abstract class EventHandler<T> {
    @NotNull
    private final Class<T> clazz;

    public EventHandler(@NotNull Class<T> clazz) {
        this.clazz = clazz;
    }

    @SuppressWarnings("OverlyBroadCatchBlock")
    public void handleEvent(@NotNull EventMessage message, @NotNull UserProfile user) throws HandleException {
        try {
            final Object data = new ObjectMapper().readValue(message.getContent(), clazz);

            handle(clazz.cast(data), user);
        } catch (IOException | ClassCastException ex) {
            throw new HandleException("Can't read incoming message of type " + message.getType() + " with content: " + message.getContent(), ex);
        }
    }

    public abstract void handle(@NotNull T message, @NotNull UserProfile user) throws HandleException;
}
