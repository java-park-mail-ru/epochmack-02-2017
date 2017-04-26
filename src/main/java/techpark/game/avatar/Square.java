package techpark.game.avatar;

import techpark.game.pathfinding.AbstractNode;

/**
 * Created by Варя on 22.04.2017.
 */
public class Square extends AbstractNode {

    public Square(int x, int y){
        super(x, y);
    }

    @Override
    public void sethCosts(AbstractNode endNode) {
        this.sethCosts((absolute(this.getxPosition() - endNode.getxPosition())
                + absolute(this.getyPosition() - endNode.getyPosition()))
                * BASICMOVEMENTCOST);
    }

    private int absolute(int a) {
        return a > 0 ? a : -a;
    }
}
