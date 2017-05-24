package techpark.game.events;

import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;
import techpark.exceptions.HandleException;
import techpark.game.GameMechanics;
import techpark.game.base.ClientSnap;
import techpark.user.UserProfile;
import techpark.websocket.EventHandler;
import techpark.websocket.EventHandlerContainer;

import javax.annotation.PostConstruct;

/**
 * Created by Варя on 22.04.2017.
 */
@Component
public class ClientSnapHandler extends EventHandler<ClientSnap> {
    @NotNull
    private GameMechanics gameMechanics;
    @NotNull
    private EventHandlerContainer eventHandlerContainer;

    public ClientSnapHandler(@NotNull GameMechanics gameMechanics, @NotNull EventHandlerContainer eventHandlerContainer) {
        super(ClientSnap.class);
        this.gameMechanics = gameMechanics;
        this.eventHandlerContainer = eventHandlerContainer;
    }

    @PostConstruct
    private void init() {
        eventHandlerContainer.registerHandler(ClientSnap.class, this);
    }

    @Override
    public void handle(@NotNull ClientSnap message, @NotNull UserProfile userProfile) throws HandleException {
        gameMechanics.addClientSnapshot(userProfile, message);
    }
}
