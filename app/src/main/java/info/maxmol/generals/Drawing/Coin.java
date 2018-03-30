package info.maxmol.generals.Drawing;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;

import info.maxmol.generals.classes.Vec2D;

public class Coin extends Entity {
    public Vec2D velocity;
    public int count = 1;
    public static final int radius = (int) cp(20);

    public Coin() {

    }

    public Coin(int count, Vec2D vec2D) {
        setCount(count);
        setPos(vec2D);
        velocity = Vec2D.random().mul(cp(10.0));
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    @Override
    public void Tick() {
        if (velocity.x > 1 || velocity.y > 1) {
            velocity.multiply(0.9);
        }
        else velocity = Vec2D.zero;

        Move(velocity.plus(new Vec2D(0, cp(2))));
    }

    @Override
    public void Draw(Canvas canvas) {
        Paint p = new Paint();
        p.setColor(Color.GREEN);

        double posx = getPos().x, posy = getPos().y;
        canvas.drawRect(
                (float) (posx - radius),
                (float) (posy - radius),
                (float) (posx + radius),
                (float) (posy + radius),
                p);
    }

    @Override
    public Path generatePath() {
        double posx = getPos().x, posy = getPos().y;

        Path path = new Path();

        path.setFillType(Path.FillType.EVEN_ODD);

        path.moveTo((float) (posx - radius), (float) (posy - radius));
        path.lineTo((float) (posx + radius), (float) (posy - radius));
        path.lineTo((float) (posx + radius), (float) (posy + radius));
        path.lineTo((float) (posx - radius), (float) (posy + radius));
        path.close();

        return path;
    }

    @Override
    public boolean isPhysicsObject() {
        return false;
    }

    @Override
    public int getZPos() {
        return 0;
    }
}
