package techpark.game.avatar;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import techpark.resources.Resource;

/**
 * Created by Варя on 23.04.2017.
 */
@SuppressWarnings("unused")
public class Tower extends Resource{
    private Double damage;
    private Double frequency;
    private Double radius;
    private Square square;

    @JsonCreator
    public Tower(
                 @JsonProperty("type")String type,
                 @JsonProperty("alias")String alias,
                 @JsonProperty("frequency") double frequency,
                 @JsonProperty("damage") double damage,
                 @JsonProperty("radius") double radius){
        super(type);
        this.damage = damage;
        this.frequency =frequency;
        this.radius = radius;
    }

    public Double getDamage() {
        return damage;
    }

    public Double getFrequency() {
        return frequency;
    }

    public Double getRadius() {
        return radius;
    }

    public Square getSquare() {
        return square;
    }

    public void setSquare(Square square) {
        this.square = square;
    }

    @Override
    public boolean equals(Object o){
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final Tower tower = (Tower) o;
        return (tower.square == this.square);
    }

    @Override
    public int hashCode() {
        int result = damage != null ? damage.hashCode() : 0;
        result = 31 * result + (frequency != null ? frequency.hashCode() : 0);
        result = 31 * result + (radius != null ? radius.hashCode() : 0);
        result = 31 * result + (square != null ? square.hashCode() : 0);
        return result;
    }
}
