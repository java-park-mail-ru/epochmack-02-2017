package techpark.game.avatar;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import techpark.resources.Generator;
import techpark.resources.Resource;

/**
 * Created by Варя on 22.04.2017.
 */
@SuppressWarnings("unused")
public class Enemy extends Resource implements Cloneable{
    private int number;
    private double hp;
    private double speed;
    private boolean dead  ;

    @JsonCreator
    public Enemy(@JsonProperty("type") String type,
                 @JsonProperty("hp") double hp,
                 @JsonProperty("speed") double speed){
        super(type);
        this.hp = hp;
        this.speed = speed;
        this.dead = false;
    }

    public Enemy(double hp, int number, double speed){
        this.hp = hp;
        this.number = number;
        this.speed = speed;
        this.dead = false;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public void setHp(double hp) {
        this.hp = hp;
    }

    public void waveCoff(int wave){
        final Generator generator = new Generator();
        final double coff = (double)generator.settings("waveCoff") * wave;
        this.hp *= coff;
    }

    public void incrementHp(double h) {
        this.hp -= h;
    }

    public double getHp() {
        return hp;
    }

    public int getNumber() {
        return number;
    }

    @JsonIgnore
    public double getSpeed() {
        return speed;
    }

    public void setDead() {
        this.dead = true;
    }

    public boolean isDead() {
        return dead;
    }

    @Override
    public boolean equals(Object o){
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final Enemy enemy = (Enemy) o;
        return (enemy.number == this.number);
    }

    @Override
    public int hashCode() {
        int result = number;
        long temp = Double.doubleToLongBits(hp);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(speed);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        return result;
    }

    @Override
    public Enemy clone()  {
        try {
            return (Enemy) super.clone();
        } catch (CloneNotSupportedException e) {
            return this;
        }
    }

}
