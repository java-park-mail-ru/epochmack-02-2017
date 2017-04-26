package techpark.game.base;

import techpark.game.avatar.EnemyDamage;
import techpark.game.avatar.Square;
import techpark.game.avatar.ThroneDamage;

import java.util.List;

/**
 * Created by Варя on 22.04.2017.
 */
@SuppressWarnings("unused")
public class ServerSnapPart2 {
    private int wave;
    private List<Square> route;
    private List<EnemyDamage> enemyDamages;
    private List<ThroneDamage> throneDamages;
    private int points;

    public int getWave() {
        return wave;
    }

    public List<Square> getRoute() {
        return route;
    }

    public List<EnemyDamage> getEnemyDamages() {
        return enemyDamages;
    }

    public List<ThroneDamage> getThroneDamages() {
        return throneDamages;
    }

    public void setRoute(List<Square> rout) {
        this.route = rout;
    }

    public void addRoute(List<Square> rout){
        this.route.addAll(rout);
    }

    public void setWave(int wave) {
        this.wave = wave;
    }

    public void setEnemyDamages(List<EnemyDamage> enemyDamages) {
        this.enemyDamages = enemyDamages;
    }

    public void setThroneDamages(List<ThroneDamage> throneDamages) {
        this.throneDamages = throneDamages;
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }
}
