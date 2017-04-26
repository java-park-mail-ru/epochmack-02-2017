package techpark.game.internal;

import org.springframework.stereotype.Service;
import techpark.game.Config;
import techpark.game.GameSession;
import techpark.game.avatar.Square;
import techpark.game.base.ClientSnap;

import java.util.Map;


/**
 * Created by Варя on 22.04.2017.
 */
@Service
public class ClientSnapshotsService {

    public void processSnapshotsFor(GameSession session, ClientSnap snap){
        if(!snap.isStartWave())
            processFirstPart(session, snap);
        else processSecondtPart(session, snap);
    }

    private void processFirstPart(GameSession session, ClientSnap snap){
        session.field.setGem(snap.getSquare());
        if(session.field.calculateRoute().isEmpty()){
            session.field.setGem(snap.getSquare(), 'o');
            return;
        }
        session.setGem(snap.getSquare());
        if(session.getGems().size() == 6)
            session.field.calcAvaliableGems();
    }

    private void processSecondtPart(GameSession session, ClientSnap snap){
        if(snap.getComb() != null){
            for(Character gem: Config.COMBGEM.get(snap.getComb()))
                if(!gem.equals(session.field.getSquare(snap.getSquare())))
                    for (Map.Entry<Square, Character> agem: session.field.getAvaliableGems().entrySet())
                        if(agem.getValue().equals(gem))
                            session.setGem(agem.getKey());
            session.field.setGem(snap.getSquare(),snap.getComb());
        }
        else session.removeGem(snap.getSquare());
        session.field.setStones(session.getGems());
        session.clearGems();
        session.incrementWave();
    }
}
