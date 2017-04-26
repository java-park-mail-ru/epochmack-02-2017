package techpark.game.avatar;

/**
 * Created by Варя on 22.04.2017.
 */
@SuppressWarnings("unused")
public class Enemy {
    private int number;
    private double hp;
    private double speed;

    public Enemy(double hp, int number, double speed){
        this.hp = hp;
        this.number = number;
        this.speed = speed;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public void setHp(double hp) {
        this.hp = hp;
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

    public double getSpeed() {
        return speed;
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
}
