package techpark.resources;

import techpark.game.avatar.Enemy;
import techpark.game.avatar.Square;
import techpark.game.avatar.Tower;

import java.util.HashMap;
import java.util.List;

/**
 * Created by Варя on 01.05.2017.
 */
public class Generator {
    private ResourceFactory resourceFactory = new ResourceFactory();

    public Resource tower(Character alias){
        switch (alias){
            case 'a':
            case 'b':
            case 'c':
            case 'd':
            case 'e':
            case 'f':
                return resourceFactory.get("data/towers/level1/" + alias + ".json", Tower.class);
            case 'k':
            case 'l':
            case 'm':
                return resourceFactory.get("data/towers/level2/" + alias + ".json", Tower.class);
            case 'z':
                return resourceFactory.get("data/towers/level3/" + alias + ".json", Tower.class);
            default:
                return resourceFactory.get("data/towers/level1/a.json", Tower.class);
        }
    }

    public HashMap combinations(){
          return resourceFactory.jsonToMap("data/combinations/combinations.json");
    }

    public Resource enemy(){
        return resourceFactory.get("data/enemy/enemy.json", Enemy.class);
    }

    public List<Square> controlPoints(){
        return resourceFactory.jsonToSquareList("data/controlPoints/points.json");
    }

    public Object settings(String name){
        final RawResource resource = resourceFactory.getRaw("data/commonSettings.json");
        return resource.anyGet(name);
    }
}
