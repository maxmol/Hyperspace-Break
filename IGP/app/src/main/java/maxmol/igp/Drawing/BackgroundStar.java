package maxmol.igp.Drawing;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import maxmol.igp.StarsBackground;
import maxmol.igp.classes.MUtil;
import maxmol.igp.classes.Vec2D;

import static maxmol.igp.Drawing.GameDraw.cp;

// My first entity is a star in the background. Abstract Entity class makes it really easy to create different game objects.
public class BackgroundStar extends Entity {
    public float distance;
    private static final float SPEED_MULTIPLIER = cp(4f);
    public static final float SPAWN_CHANCE = 0.05f;
    private StarsBackground fromView;

    private float circleRadius;
    private Paint starPaint;

    private void initPaint() {
        starPaint = new Paint();
        starPaint.setAntiAlias(true);
        starPaint.setColor(Color.WHITE);
        //starPaint.setMaskFilter(new BlurMaskFilter(cp(1), BlurMaskFilter.Blur.NORMAL));
    }

    public BackgroundStar() {
        setPos(new Vec2D(Math.random() * GameDraw.context.ScrH, -5));
        distance = (float) MUtil.Clamp(Math.random(), 0.6, 1);
        circleRadius = cp(10) * (1 - distance);

        initPaint();
    }

    public BackgroundStar(StarsBackground fromView) {
        setPos(new Vec2D(Math.random() * (fromView != null ? fromView.getHeight() : GameDraw.context.ScrH), -5));
        distance = (float) MUtil.Clamp(Math.random(), 0.6, 1);
        circleRadius = cp(10) * (1 - distance);

        this.fromView = fromView;

        initPaint();
    }

    public BackgroundStar(Vec2D vec2D) {
        setPos(vec2D);
        distance = (float) MUtil.Clamp(Math.random(), 0.6, 0.9);

        circleRadius = cp(10) * (1 - distance);

        initPaint();
    }

    @Override
    public void Tick() {
        Move(new Vec2D(0, (1 - distance) * SPEED_MULTIPLIER));

        if (getPos().y > (fromView != null ? fromView.getHeight() : GameDraw.context.ScrH) + cp(5)) {
           Remove();
        }
    }

    @Override
    public void Draw(Canvas canvas) {
        canvas.drawCircle((float) getPos().x, (float) getPos().y, circleRadius, starPaint);
    }

    @Override
    public int getZPos() {
        return -99;
    }

    @Override
    public boolean isPhysicsObject() {
        return false;
    }

    @Override
    public void Remove() {
        if (fromView == null)
            super.Remove();
        else
            fromView.entities.remove(this);
    }
}
