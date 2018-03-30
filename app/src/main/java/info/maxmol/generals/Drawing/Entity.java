package info.maxmol.generals.Drawing;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.Rect;
import android.view.MotionEvent;

import info.maxmol.generals.classes.Vec2D;

public abstract class Entity {
    private Vec2D pos = new Vec2D(0, cp(50));
    private Vec2D[] pointsMesh;

    protected static float cp(float pixels) { // Converted Pixels
        return GameDraw.cp(pixels);
    }

    protected static float cp(int pixels) {
        return GameDraw.cp(pixels);
    }

    protected static double cp(double pixels) {
        return GameDraw.cp(pixels);
    }

    public void setPointsMesh(Vec2D[] pts) {
        pointsMesh = pts;
    }

    public Path generatePath() {
        double posx = getPos().x, posy = getPos().y;

        Path path = new Path();

        if (pointsMesh == null || pointsMesh.length == 0) {
            return path;
        }

        path.setFillType(Path.FillType.EVEN_ODD);

        path.moveTo((float) (posx + pointsMesh[0].x), (float) (posy + pointsMesh[0].y));

        for (int i = 1; i < pointsMesh.length; i++) {
            path.lineTo((float) (posx + pointsMesh[i].x), (float) (posy + pointsMesh[i].y));
        }

        path.close();

        return path;
    }

    protected void drawByPoints(Canvas canvas, Paint p) {
        if (pointsMesh.length < 3) {
            return;
        }

        canvas.drawPath(generatePath(), p);
    }

    public Vec2D getPos() {
        return pos;
    }

    public void setPos(Vec2D pos) {
        this.pos = pos;
    }

    abstract public void Tick();

    public void OnTouch(MotionEvent event) {}

    public void Move(Vec2D p) {
        setPos(getPos().plus(p));
    }

    abstract public void Draw(Canvas canvas);

    public void Remove() {
        GameDraw.context.entities.remove(this);
    }

    public void takeDamage(int damage) {
        Remove();
    }

    abstract public boolean isPhysicsObject();

    abstract public int getZPos();
}
