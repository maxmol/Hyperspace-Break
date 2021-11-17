package maxmol.igp.Drawing;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Region;
import android.view.MotionEvent;

import maxmol.igp.Classes.Vec2D;

import static maxmol.igp.Drawing.GameDraw.cp;

import java.util.Random;

/**
 * This abstract class is the parent for all game objects you see in the game.
 */
public abstract class Entity {
    protected final static Random random = new Random();
    private Vec2D pos = new Vec2D(0, cp(50));
    private Vec2D[] pointsMesh;
    private Path path;
    private Region region;
    protected Bitmap bitmap;
    private boolean valid = true;
    protected boolean limitMove() {
        return false;
    };

    public void setPointsMesh(Vec2D[] pts) {
        pointsMesh = pts.clone();

        path = generatePath();

        region = new Region();
        region.setPath(path, GameDraw.clipRegion);
    }

    public Path generatePath() {
        Path path = new Path();

        if (pointsMesh == null || pointsMesh.length == 0) {
            return path;
        }

        path.setFillType(Path.FillType.EVEN_ODD);

        path.moveTo((float) (pointsMesh[0].x), (float) (pointsMesh[0].y));

        for (int i = 1; i < pointsMesh.length; i++) {
            path.lineTo((float) (pointsMesh[i].x), (float) (pointsMesh[i].y));
        }

        path.close();

        return path;
    }

    public Path getPath() {
        return path;
    }

    public Region getRegion() {
        return region;
    }

    protected void drawByPoints(Canvas canvas, Paint p) {
        if (path == null) {
            return;
        }

        canvas.save();
        canvas.translate((float) getPos().x, (float) getPos().y);
        canvas.drawPath(path, p);
        canvas.restore();
    }

    public Vec2D getPos() {
        return pos;
    }

    public void setPos(Vec2D pos) {
        this.pos = pos;
    }

    abstract public void tick();

    public void onTouch(MotionEvent event) {}

    public void move(Vec2D p) {
        Vec2D pos = getPos().plus(p);

        if (limitMove()) {
            float s = cp(70);
            pos = Vec2D.clamp(pos, s, GameDraw.context.scrW - s, s, GameDraw.context.scrH - s);
        }

        setPos(pos);
    }

    abstract public void draw(Canvas canvas);

    public void remove() {
        valid = false;
        GameDraw.context.entities.remove(this);
    }

    public boolean isValid() {
        return valid;
    }

    public void takeDamage(int damage) {
        remove();
    }

    public int getCollisionRadius() {
        return 0;
    }

    abstract public boolean isPhysicsObject();

    abstract public int getZPos();

    public float getAngle() {
        return 0f;
    }
}
