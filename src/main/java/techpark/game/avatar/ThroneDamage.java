package techpark.game.avatar;

/**
 * Created by Варя on 22.04.2017.
 */
@SuppressWarnings("unused")
public class ThroneDamage {
    private double hp;
    private int enemy;

    public ThroneDamage(double hp, int enemy){
        this.hp = hp;
        this.enemy = enemy;
    }

    public void setEnemy(int enemy) {
        this.enemy = enemy;
    }

    public int getEnemy() {
        return enemy;
    }

    public double getHp() {
        return hp;
    }

    public void setHp(double hp) {
        this.hp = hp;
    }
}
