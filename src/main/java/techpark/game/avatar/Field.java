package techpark.game.avatar;

import techpark.game.Config;
import techpark.game.pathfinding.ExampleFactory;
import techpark.game.pathfinding.Map;

import java.util.*;

import static java.lang.Character.isAlphabetic;

/**
 * Created by Варя on 22.04.2017.
 */
public class Field {
    private char[][] map;
    private HashMap<Square, Character> avaliableGems;

    private void initMap(){
        this.avaliableGems = new HashMap<>();
        this.map = new char[Config.WIDTH][Config.HEIGHT];
        for (int i =  0; i < Config.WIDTH; i++)
            for(int j = 0; j < Config.HEIGHT; j++)
                map[i][j] = 'o';
    }

    public Field(){
        initMap();
    }

    public void setGem(Square coord){
        final Random random = new Random();
        map[coord.getxPosition()][coord.getyPosition()] = (char) (random.nextInt(6) +'a');
    }

    public void setGem(Square coord, char gem){
        map[coord.getxPosition()][coord.getyPosition()] = gem;
    }

    public void calcAvaliableGems() {
        this.avaliableGems.clear();
        for (int i =  0; i < Config.WIDTH; i++)
            for(int j = 0; j < Config.HEIGHT; j++) {
                final Square sq = new Square(i, j);
                final Character sym = this.map[i][j];
                if (isAlphabetic(sym))
                    avaliableGems.put(sq, sym);
            }
    }

    public HashMap<Square, Character> getAvaliableGems() {
        return avaliableGems;
    }

    public void setStones(List<Square> stones){
        for(Square stone: stones)
            map[stone.getxPosition()][stone.getyPosition()] = '#';
    }

    public char[][] getMap(){return map;}

    public char getSquare(Square square){
        return map[square.getxPosition()][square.getyPosition()];
    }

    public HashMap<Square, Character> calculateCombinations(){
        final HashMap<Square, Character> combinations = new HashMap<>();
        for (java.util.Map.Entry<Character,List<Character>> entry: Config.COMBGEM.entrySet()){
            if(avaliableGems.containsValue(entry.getValue().get(0)) &&
                    avaliableGems.containsValue(entry.getValue().get(1)) &&
                    avaliableGems.containsValue(entry.getValue().get(2)))
                for (java.util.Map.Entry<Square, Character> gem: avaliableGems.entrySet())
                    if(gem.getValue().equals(entry.getValue().get(0)) &&
                            gem.getValue().equals(entry.getValue().get(1)) &&
                            gem.getValue().equals(entry.getValue().get(2)))
                        combinations.put(gem.getKey(), entry.getKey());
        }
        return combinations;
    }

    public List<Square> calculateRoute(){
        final Map<Square> myMap = new Map<>(Config.WIDTH, Config.HEIGHT, new ExampleFactory());
        for(int i = 0; i < Config.WIDTH; i++)
            for (int j = 0; j < Config.HEIGHT; j++)
                if (this.map[i][j] != 'o')
                    myMap.setWalkable(i, j, false);
        final List<Square> route = new ArrayList<>();
        for (int i = 0; i < Config.CONTROLPOINTS.size()-1; i++){
            final List<Square> interval = new ArrayList<>();
            interval.addAll(myMap.findPath(Config.CONTROLPOINTS.get(i).getxPosition(),
                    Config.CONTROLPOINTS.get(i).getyPosition(),
                    Config.CONTROLPOINTS.get(i+1).getxPosition(), Config.CONTROLPOINTS.get(i+1).getyPosition()));
            if(interval.isEmpty())
                return new ArrayList<>();
            route.addAll(interval);
        }
        return  route;
    }

    @SuppressWarnings("ClassNamingConvention")
    public class AOE {
        private java.util.Map<Square, List<Tower>> towersOnRoute;
        private LinkedList<Enemy> enemies;

        public AOE(int wave , List<Square> route){
            this.towersOnRoute = new TreeMap<>();
            this.enemies = new LinkedList<>();
            reorgonizeTowers(route,Field.this.avaliableGems);
            for(int i = 0; i < Config.NUMBERENEMY; i++)
                this.enemies.add(new Enemy(Config.ENEMYHP*Config.WAVECOFF*wave,
                        i, Config.ENEMYSPEED*Config.WAVECOFF*wave));
        }

        private void reorgonizeTowers(List<Square> route, java.util.Map<Square, Character> gems){
            for (Square roureSquare: route){
                searchTower(roureSquare, gems);
            }
        }

        private void searchTower(Square route, java.util.Map<Square, Character> gems){
            final List<Tower> towersAvailable = new ArrayList<>();
            for (int i = route.getxPosition() - Config.MAXRADIUS.intValue();
                 i <= route.getxPosition() + Config.MAXRADIUS.intValue(); i++)
                for (int j = route.getyPosition() - Config.MAXRADIUS.intValue();
                     j <= route.getyPosition() + Config.MAXRADIUS.intValue(); j++) {
                    final Square square =new Square(i, j);
                    if (gems.get(square) != null){
                        towersAvailable.add(new Tower(gems.get(square),square));
                    }
                }
            if(!towersAvailable.isEmpty())
                this.towersOnRoute.put(route, towersAvailable);
        }

        public List<EnemyDamage> calulateEnemyDamage(){
            final java.util.Map<Tower, Double> timeLeft = new HashMap<>();
            final List<EnemyDamage> enemyDamage = new ArrayList<>();
            for (java.util.Map.Entry<Square, List<Tower>> entry: towersOnRoute.entrySet()){
                for (Tower tower: entry.getValue()){
                    final Double time = timeLeft.get(tower);
                    double i  = time == null ? 0.0 : time;
                    for (Enemy enemy: enemies){
                        i  = time == null ? 0.0 : time;
                        for ( ; i <= enemy.getSpeed(); i+= tower.getFrequency()) {
                            enemy.incrementHp(tower.getDamage());
                            enemyDamage.add(new EnemyDamage(tower.getSquare().getxPosition() + i,
                                    tower.getSquare().getyPosition() + i, enemy));
                            if (enemy.getHp() <= 0.0)
                                enemies.remove(enemy);
                        }
                    }
                    timeLeft.put(tower, i - enemies.get(0).getSpeed());
                }
            }
            return enemyDamage;
        }

        @SuppressWarnings("MagicNumber")
        public List<ThroneDamage> calulateThroneDamage(){
            final List<ThroneDamage> throneDamages = new ArrayList<>();
            Double throneHP = 100.0;
            for (Enemy enemy: enemies){
                throneHP -= enemy.getHp()*Config.THRONECOFF;
                throneDamages.add(new ThroneDamage(throneHP, enemy.getNumber()));
                if (throneHP <= 0.0)
                    break;
            }
            return throneDamages;
        }

        public LinkedList<Enemy> getEnemies() {
            return enemies;
        }
    }
}
