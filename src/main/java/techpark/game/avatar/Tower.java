package techpark.game.avatar;

import techpark.game.Config;

/**
 * Created by Варя on 23.04.2017.
 */
@SuppressWarnings("unused")
public class Tower {
    private Double damage;
    private Double frequency;
    private Double radius;
    private Square square;

    public Tower(Character type, Square square){
        this.damage = Config.TOWERS.get(type).get("DAMAGE");
        this.frequency = Config.TOWERS.get(type).get("FREQUENCY");
        this.radius = Config.TOWERS.get(type).get("RADIUS");
        this.square = square;
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
