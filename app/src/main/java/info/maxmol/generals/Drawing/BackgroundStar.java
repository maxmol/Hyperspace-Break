package info.maxmol.generals.Drawing;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import info.maxmol.generals.classes.MUtil;
import info.maxmol.generals.classes.Vec2D;

public class BackgroundStar extends Entity {
    public float distance;
    private static final float SPEED_MULTIPLIER = cp(4f);
    public static final float SPAWN_CHANCE = 0.05f;

    private float circleRadius;

    public BackgroundStar() {
        setPos(new Vec2D(Math.random() * GameDraw.context.ScrW, -5));
        distance = (float) MUtil.Clamp(Math.random(), 0.6, 1);
        circleRadius = cp(10) * (1 - distance);
    }

    public BackgroundStar(Vec2D vec2D) {
        setPos(vec2D);
        distance = (float) MUtil.Clamp(Math.random(), 0.6, 0.9);

        circleRadius = cp(10) * (1 - distance);
    }

    @Override
    public void Tick() {
        Move(new Vec2D(0, (1 - distance) * SPEED_MULTIPLIER));

        if (getPos().y > GameDraw.context.ScrH + cp(5)) {
           Remove();
        }
    }

    @Override
    public void Draw(Canvas canvas) {
        Paint p = new Paint();
        p.setColor(Color.WHITE);
        canvas.drawCircle((float) getPos().x, (float) getPos().y, circleRadius, p);
    }

    @Override
    public int getZPos() {
        return -99;
    }

    @Override
    public boolean isPhysicsObject() {
        return false;
    }
}
