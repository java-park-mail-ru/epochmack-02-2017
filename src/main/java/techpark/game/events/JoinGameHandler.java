package techpark.game.events;

import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import techpark.exceptions.HandleException;
import techpark.game.GameMechanics;
import techpark.user.UserProfile;
import techpark.websocket.EventHandler;
import techpark.websocket.EventHandlerContainer;

import javax.annotation.PostConstruct;


/**
 * Created by Варя on 18.04.2017.
 */
@Component
public class JoinGameHandler extends EventHandler<JoinGame>{
    private EventHandlerContainer eventHandlerContainer;
    private GameMechanics gameMechanics;

    @Autowired
    public JoinGameHandler(EventHandlerContainer container, GameMechanics gameMechanics) {
        super(JoinGame.class);
        this.eventHandlerContainer = container;
        this.gameMechanics = gameMechanics;
    }

    @PostConstruct
    private void init() {
        eventHandlerContainer.registerHandler(JoinGame.class, this);
    }

    @Override
    public void handle(@NotNull JoinGame message, @NotNull UserProfile userProfile) throws HandleException {
        gameMechanics.addPlayer(userProfile);
    }
}
