package techpark.game.avatar;

import org.jetbrains.annotations.NotNull;
import org.xguzm.pathfinding.grid.NavigationGrid;
import org.xguzm.pathfinding.grid.finders.AStarGridFinder;
import org.xguzm.pathfinding.grid.finders.GridFinderOptions;
import techpark.resources.Generator;

import java.util.*;


/**
 * Created by Варя on 22.04.2017.
 */
@SuppressWarnings("MagicNumber")
public class Field {
    private char[][] map;
    private HashMap<Square, Character> avaliableGems;
    private final int width;
    private final int height;

    private void initMap(){
        this.map = new char[width][height];
        for (int i =  0; i < width; i++)
            for(int j = 0; j < height; j++)
                map[i][j] = 'o';
    }

    public Field(){
        this.avaliableGems = new HashMap<>();
        final Generator generator = new Generator();
        this.width = (int)generator.settings("width");
        this.height = (int)generator.settings("height");
        initMap();
    }

    public void setGem(Square coord){
        final Date dt = new Date();
        final Random random = new Random(dt.getTime());
        if(map[coord.getX()][coord.getY()] != '#')
            map[coord.getX()][coord.getY()] = (char) (random.nextInt(6) +'a');
    }

    public void setGem(Square coord, char gem){
        map[coord.getX()][coord.getY()] = gem;
    }

    public HashMap<Square, Character> getAvaliableGems() {
        return avaliableGems;
    }

    public void setStone(Square stone){
        map[stone.getX()][stone.getY()] = '#';
    }

    public void setStones(Set<Square> stones){
        for(Square stone: stones)
            setStone(stone);
    }

    public char[][] getMap(){return map;}

    public char getSquare(Square square){
        return map[square.getX()][square.getY()];
    }


    public List<Square> calculateRoute(){
        final NavigationGrid<Square> navGrid  = setWallkables();
        return getSquares(navGrid);
    }

    public void addAvailableGem(Square sq, Character gem){
        this.avaliableGems.put(sq, gem);
    }

    @NotNull
    @SuppressWarnings({"deprecation", "unchecked"})
    private List<Square> getSquares(NavigationGrid<Square> navGrid) {
        final AStarGridFinder<Square> finder = new AStarGridFinder(Square.class);
        final List<Square> route = new ArrayList<>();
        final Generator generator = new Generator();
        final List<Square> points = generator.controlPoints();
        for (int i = 0; i < points.size()-1; i++){
            final List<Square> interval = finder.findPath(points.get(i).getX(),
                    points.get(i).getY(),
                    points.get(i+1).getX(), points.get(i+1).getY(), navGrid);
            if(interval == null) {
                return new ArrayList<>();
            }
            route.addAll(interval);
        }
        setDiagonals(navGrid, route);
        return  route;
    }

    private void setDiagonals(NavigationGrid<Square> navGrid, List<Square> route) {
        for (int i = 0; i < route.size()-1; i++)
            if(navGrid.getMovementCost(route.get(i), route.get(i + 1), new GridFinderOptions()) == 1.4f) {
                route.get(i).setDiagonal();
            }
    }

    @SuppressWarnings({"deprecation", "unchecked"})
    private NavigationGrid<Square> setWallkables() {
        final Square[][] cells = new Square[width][height];
        for(int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                cells[i][j] = new Square(i, j);
                if (this.map[i][j] != 'o') {
                    cells[i][j].setWalkable(false);
                }
            }
        }
        return (NavigationGrid<Square>) new NavigationGrid(cells);
    }


    @Override
    public String toString(){
        final StringBuilder stringBuilder = new StringBuilder();
        for(int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                stringBuilder.append(map[i][j]);
            }
            stringBuilder.append('\n');
        }
        return stringBuilder.toString();
    }
}
