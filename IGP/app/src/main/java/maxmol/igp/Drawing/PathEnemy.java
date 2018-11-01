package maxmol.igp.Drawing;

import java.util.LinkedList;

import maxmol.igp.classes.BulletGenerator;
import maxmol.igp.classes.Vec2D;

/**
 * An enemy that uses a specific movement and rotation path.
 */
public class PathEnemy extends Enemy {
    private LinkedList<Vec2D> movePath = new LinkedList<>();

    public PathEnemy(Vec2D pos, int health, BulletGenerator bg, int resId, Vec2D[] positions, boolean percentage) {
        super(pos, health, bg, resId);

        for (Vec2D p: positions) {
            if (percentage) {
                p.x = p.x * GameDraw.context.scrW / 100;
                p.y = p.y * GameDraw.context.scrH / 100;
            }

            movePath.add(p);
        }
    }

    public PathEnemy(float pc, int health, BulletGenerator bg, int resId, Vec2D[] positions, boolean percentage) {
        super(pc, health, bg, resId);

        for (Vec2D p: positions) {
            if (percentage) {
                p.x = p.x * GameDraw.context.scrW / 100;
                p.y = p.y * GameDraw.context.scrH / 100;
            }

            movePath.add(p);
        }
    }

    @Override
    protected void enemyMove() {
        if (movePath.isEmpty()) return;

        if (getPos().minus(movePath.peek()).length() < this.speed * 2) {
            setPos(movePath.poll());
            return;
        }

        move(movePath.peek().minus(getPos()).getNormalized().mul(this.speed));
    }

    public void addPathPos(Vec2D p, boolean percentage) {
        if (percentage) {
            p.x = p.x * GameDraw.context.scrW / 100;
            p.y = p.y * GameDraw.context.scrH / 100;
        }

        movePath.add(p);
    }
}
