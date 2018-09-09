package info.maxmol.generals.Drawing;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.MotionEvent;

import info.maxmol.generals.classes.Vec2D;
import static info.maxmol.generals.Drawing.GameDraw.cp;

public class DPad extends SuperVGUI {
    protected Vec2D dir = new Vec2D(0, 0);
    protected Vec2D output = new Vec2D(0, 0);
    protected float rotation = 0;
    protected float size = cp(180);
    public boolean save = false;

    private Vec2D lastTouch;

    @Override
    public void Draw(Canvas canvas) {
        Paint p = new Paint();
        p.setColor(Color.argb(64, 255, 255, 255));
        canvas.drawCircle((float) getPos().x, (float) getPos().y, size, p);

        if (lastTouch == null) lastTouch = getPos();
        canvas.drawCircle((float) lastTouch.x, (float) lastTouch.y, size/4, p);

        p.setStyle(Paint.Style.STROKE);
        p.setColor(Color.argb(128, 128, 128, 128));
        canvas.drawCircle((float) getPos().x, (float) getPos().y, size, p);
    }

    @Override
    public boolean CheckTouch(MotionEvent event, int pointerId) {
        Vec2D t = new Vec2D(event.getX(pointerId), event.getY(pointerId));
        if (t.Distance(getPos()) <= size * 3) {
            return true;
        }

        return false;
    }

    @Override
    public void OnTouch(MotionEvent event) {
        lastTouch = new Vec2D(event.getX(pointerId), event.getY(pointerId));
        dir = lastTouch.minus(getPos()).GetNormalized();
        rotation = (float) lastTouch.getRotationTo(getPos());

        if (lastTouch.Distance(getPos()) > size*0.75) {
            lastTouch = getPos().plus(dir.mul((double) size*0.75));
        }

        output = lastTouch.minus(getPos()).mul(1/(double)size);
    }

    @Override
    public void OnRelease(MotionEvent event) {
        super.OnRelease(event);

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
