package maxmol.igp.Drawing;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import maxmol.igp.StarsBackground;
import maxmol.igp.classes.MUtil;
import maxmol.igp.classes.Vec2D;

import static maxmol.igp.Drawing.GameDraw.cp;

/**
 * My first entity is a star in the background. Abstract Entity class makes it really easy to create different game objects.
 */
public class BackgroundStar extends Entity {
    public float distance;
    private static final float SPEED_MULTIPLIER = cp(4f);
    public static final float SPAWN_CHANCE = 0.05f;
    private StarsBackground fromView;

    private float circleRadius;
    private Paint starPaint;

    /**
     * create paint object with our settings
     */
    private void initPaint() {
        starPaint = new Paint();
        starPaint.setAntiAlias(true);
        starPaint.setColor(Color.rgb(136, 7, 148));
        //starPaint.setMaskFilter(new BlurMaskFilter(cp(1), BlurMaskFilter.Blur.NORMAL));
    }

    public BackgroundStar() {
        setPos(new Vec2D(Math.random() * GameDraw.context.scrW, -5));
        distance = (float) MUtil.clamp(Math.random(), 0.6, 1);
        circleRadius = cp(20) * (1 - distance);

        initPaint();
    }

    public BackgroundStar(StarsBackground fromView) {
        setPos(new Vec2D(Math.random() * (fromView != null ? fromView.getHeight() : GameDraw.context.scrW), -5));
        distance = (float) MUtil.clamp(Math.random(), 0.6, 1);
        circleRadius = cp(20) * (1 - distance);

        this.fromView = fromView;

        initPaint();
    }

    public BackgroundStar(Vec2D vec2D) {
        setPos(vec2D);
        distance = (float) MUtil.clamp(Math.random(), 0.6, 0.9);

        circleRadius = cp(20) * (1 - distance);

        initPaint();
    }

    @Override
    public void tick() {
        move(new Vec2D(0, (1 - distance) * SPEED_MULTIPLIER));

        if (getPos().y > (fromView != null ? fromView.getHeight() : GameDraw.context.scrH) + cp(5)) {
           remove();
        }
    }

    @Override
    public void draw(Canvas canvas) {
        //canvas.drawCircle((float) getPos().x, (float) getPos().y, circleRadius, starPaint);
        canvas.drawRect((float) getPos().x - circleRadius/2, (float) getPos().y - circleRadius/2, (float) getPos().x + circleRadius/2, (float) getPos().y + circleRadius/2, starPaint);
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
    public void remove() {
        if (fromView == null)
            super.remove();
        else
            fromView.entities.remove(this);
    }
}
