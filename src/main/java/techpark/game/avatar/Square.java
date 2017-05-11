package techpark.game.avatar;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.xguzm.pathfinding.grid.GridCell;


/**
 * Created by Варя on 22.04.2017.
 */
@JsonIgnoreProperties({"diagonal", "f", "g", "h", "walkable", "parent", "closedOnJob", "openedOnJob", "index"})
public class Square extends GridCell {

    private boolean diagonal;

    public void setDiagonal() {
        this.diagonal = true;
    }

    public boolean isDiagonal() {
        return diagonal;
    }

    public Square(@JsonProperty("x") int x, @JsonProperty("y") int y){
        super(x, y);
        diagonal = false;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final Square square = (Square) o;
        return this.getX() == square.getX() && this.getY() == square.getY();
    }

    @Override
    public int hashCode() {
        int result = this.getX();
        result = 31 * result + this.getY();
        return result;
    }
}
