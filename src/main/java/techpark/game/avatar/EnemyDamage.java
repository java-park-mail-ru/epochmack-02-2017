package techpark.game.avatar;

/**
 * Created by Варя on 22.04.2017.
 */
@SuppressWarnings("unused")
public class EnemyDamage {
    private double coordinateX;
    private double coordinateY;
    private Enemy enemy;

    public EnemyDamage(double coordinateX,  double coordinateY, Enemy enemy){
        this.coordinateX = coordinateX;
        this.coordinateY = coordinateY;
        this.enemy = enemy;
    }

    public Enemy getEnemy() {
        return enemy;
    }

    public double getCoordinateX() {
        return coordinateX;
    }

    public double getCoordinateY() {
        return coordinateY;
    }

    public void setCoordinateX(double coordinateX) {
        this.coordinateX = coordinateX;
    }

    public void setCoordinateY(double coordinateY) {
        this.coordinateY = coordinateY;
    }

    public void setEnemy(Enemy enemy) {
        this.enemy = enemy;
    }
}
