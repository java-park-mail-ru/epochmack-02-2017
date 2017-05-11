package techpark.game.avatar;

import techpark.resources.Generator;
import techpark.user.UserProfile;

import java.util.*;


/**
 * Created by Варя on 28.04.2017.
 */
public class GameUser {
    private final UserProfile user;
    private HashMap<Square, Character> avaliableGems;
    private boolean ready;

    public GameUser(UserProfile userProfile){
        this.user = userProfile;
        this.avaliableGems = new HashMap<>();
        ready = false;
    }

    public void setReady(boolean ready) {
        this.ready = ready;
    }

    public boolean isReady() {
        return ready;
    }

    public UserProfile getUser() {
        return user;
    }

    public void clearAvaliableGems(){
        avaliableGems.clear();
    }

    public HashMap<Square, Character> getAvaliableGems() {
        return avaliableGems;
    }

    public void setAvaliableGem(Square square, Character gem){
        this.avaliableGems.put(square, gem);
    }

    public void delAvailableGems(Square stone){
        avaliableGems.remove(stone);
    }


    @SuppressWarnings("unchecked")
    public HashMap<Square, List<Character>> calculateCombinations(HashMap<Square, Character> fieldGems){
        final Map<Square, Character> allGems =  new HashMap<>();
        allGems.putAll(fieldGems);
        allGems.putAll(avaliableGems);
        final Generator generator = new Generator();
        final HashMap<Character,List<Character>> comb = generator.combinations();
        final HashMap<Square, List<Character>> combinations = new HashMap<>();
        for (Map.Entry<Character,List<Character>> entry: comb.entrySet()){
            if (allGems.values().containsAll(entry.getValue())){
                addCombination(combinations, entry);
            }
        }
        return combinations;
    }

    private void addCombination(HashMap<Square, List<Character>> combinations, Map.Entry<Character, List<Character>> entry) {
        for (Map.Entry<Square, Character> gem: avaliableGems.entrySet())
            if(entry.getValue().contains(gem.getValue()))
                combinations.computeIfAbsent(gem.getKey(), v -> new ArrayList<>()).add(entry.getKey());
    }
}
