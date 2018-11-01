package maxmol.igp.Drawing;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import maxmol.igp.classes.MUtil;
import maxmol.igp.classes.Vec2D;

public class Particle extends Entity {
    public enum Type {
        Square,
        Circle,
    }

    public Type type = Type.Square;
    public int color = Color.RED;
    public float startsize = 2;
    public float endsize = 4;
    public float size = -1;
    public int alpha = -1;
    public int startalpha = 255;
    public int endalpha = 0;
    public float duration = 2f;
    public float life = -1;
    public Vec2D pos = new Vec2D();
    public Vec2D vel = new Vec2D();
    public double fric = 1.0;
    Paint p = new Paint();

    public Particle() {

    }

    @Override
    public void Tick() {
        pos.add(vel);
        vel.multiply(fric);

        if (alpha == -1) {
            alpha = startalpha;
        }
        if (size == -1) {
            size = startsize;
        }
        if (life == -1) {
            life = duration;
        }

        alpha = (int) MUtil.Clamp(alpha + (endalpha - startalpha) * 0.02f / duration, 0, 255);
        size = MUtil.Clamp(size + (endsize - startsize) * 0.02f / duration, 0);

        life -= 0.02f;
        if (life < 0) {
            Remove();
        }
    }

    @Override
    public void Draw(Canvas canvas) {
        p.setColor(Color.argb(alpha, Color.red(color), Color.green(color), Color.blue(color)));

        switch (type) {
            case Square:
                canvas.drawRect((float)pos.x - size, (float)pos.y - size, (float)pos.x + size, (float)pos.y + size, p);
                break;
            case Circle:
                canvas.drawCircle((float) pos.x, (float) pos.y, size, p);
                break;
        }
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
