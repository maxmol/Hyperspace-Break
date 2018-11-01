package maxmol.igp.Drawing;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.MotionEvent;

import maxmol.igp.classes.Vec2D;
import static maxmol.igp.Drawing.GameDraw.cp;

/**
 * A static joystick on the screen. Was used before, but now extends to DynamicDPad.
 */
public class DPad extends SuperVGUI {
    protected Vec2D dir = new Vec2D(0, 0);
    public Vec2D output = new Vec2D(0, 0);
    protected float rotation = 0;
    protected float size = cp(200);
    public boolean save = false;

    private Vec2D lastTouch;

    @Override
    public void draw(Canvas canvas) {
        Paint p = new Paint();
        p.setColor(Color.argb(64, 64, 64, 64));
        canvas.drawCircle((float) getPos().x, (float) getPos().y, size, p);

        if (lastTouch == null) lastTouch = getPos();
        canvas.drawCircle((float) lastTouch.x, (float) lastTouch.y, size/4, p);

        p.setStyle(Paint.Style.STROKE);
        canvas.drawCircle((float) getPos().x, (float) getPos().y, size, p);
    }

    @Override
    public boolean checkTouch(MotionEvent event, int pointerId) {
        Vec2D t = new Vec2D(event.getX(pointerId), event.getY(pointerId));
        if (t.distance(getPos()) <= size * 4) {
            return true;
        }

        return false;
    }

    @Override
    public void onTouch(MotionEvent event) {
        lastTouch = new Vec2D(event.getX(pointerId), event.getY(pointerId));
        dir = lastTouch.minus(getPos()).getNormalized();
        rotation = (float) lastTouch.getRotationTo(getPos());

        if (lastTouch.distance(getPos()) > size*0.75) {
            lastTouch = getPos().plus(dir.mul((double) size*0.75));
        }

        output = lastTouch.minus(getPos()).mul(1/(double)size);
    }

    @Override
    public void onRelease(MotionEvent event) {
        super.onRelease(event);

        if (!save) {
            lastTouch = getPos();

            dir.x = 0;
            dir.y = 0;

            output.x = 0;
            output.y = 0;

            rotation = 0;
        }
    }

    public Vec2D getDir() {
        return dir;
    }

    public Vec2D getOutput() {
        return output;
    }

    public float getRotation() {
        return rotation;
    }
}
