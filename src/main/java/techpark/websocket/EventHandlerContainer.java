package techpark.websocket;

import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import techpark.exceptions.HandleException;
import techpark.user.UserProfile;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Варя on 17.04.2017.
 */
@Service
public class EventHandlerContainer {

    @NotNull
    private static final Logger LOGGER = LoggerFactory.getLogger(EventHandlerContainer.class);
    final Map<Class<?>, EventHandler<?>> handlerMap = new HashMap<>();

    public void handle(@NotNull EventMessage message, @NotNull UserProfile user) throws HandleException {

        final Class clazz;
        try {
            System.out.println(message.getType());
            clazz = Class.forName(message.getType());
            System.out.println(clazz.getName());
        } catch (ClassNotFoundException e) {
            throw new HandleException("Can't handle message of " + message.getType() + " type", e);
        }
        final EventHandler<?> eventHandler = handlerMap.get(clazz);
        if (eventHandler == null) {
            throw new HandleException("no handler for message of " + message.getType() + " type");
        }
        eventHandler.handleEvent(message, user);
        LOGGER.debug("message handled: type =[" + message.getType() + "], content=[" + message.getContent() + ']');
    }

    public <T> void registerHandler(@NotNull Class<T> clazz, EventHandler<T> handler) {
        handlerMap.put(clazz, handler);
    }


}
