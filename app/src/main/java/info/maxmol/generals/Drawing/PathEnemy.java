package info.maxmol.generals.Drawing;

import java.util.LinkedList;

import info.maxmol.generals.classes.BulletGenerator;
import info.maxmol.generals.classes.Vec2D;

public class PathEnemy extends Enemy {
    private LinkedList<Vec2D> movePath = new LinkedList<>();

    public PathEnemy(Vec2D pos, int health, BulletGenerator bg, int resId, Vec2D[] positions, boolean percentage) {
        super(pos, health, bg, resId);

        for (Vec2D p: positions) {
            if (percentage) {
                p.x = p.x * GameDraw.context.ScrW / 100;
                p.y = p.y * GameDraw.context.ScrH / 100;
            }

            movePath.add(p);
        }
    }

    public PathEnemy(float pc, int health, BulletGenerator bg, int resId, Vec2D[] positions, boolean percentage) {
        super(pc, health, bg, resId);

        for (Vec2D p: positions) {
            if (percentage) {
                p.x = p.x * GameDraw.context.ScrW / 100;
                p.y = p.y * GameDraw.context.ScrH / 100;
            }

            movePath.add(p);
        }
    }

    @Override
    protected void enemyMove() {
        if (movePath.isEmpty()) return;

        if (getPos().minus(movePath.peek()).Length() < this.speed * 2) {
            setPos(movePath.poll());
            return;
        }

        Move(movePath.peek().minus(getPos()).GetNormalized().mul(this.speed));
    }

    public void addPathPos(Vec2D p, boolean percentage) {
        if (percentage) {
            p.x = p.x * GameDraw.context.ScrW / 100;
            p.y = p.y * GameDraw.context.ScrH / 100;
        }

        movePath.add(p);
    }
}
