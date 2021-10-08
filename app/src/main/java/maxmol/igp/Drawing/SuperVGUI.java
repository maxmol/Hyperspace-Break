package maxmol.igp.Drawing;

import android.graphics.Canvas;
import android.view.MotionEvent;

import maxmol.igp.classes.Vec2D;

/**
 * This is the base class for UI objects in surfaceview. Buttons, Labels, Forms etc. I'll use this in my future projects
 */
public abstract class SuperVGUI {
    private Vec2D pos;
    protected boolean pressed;

    public boolean isPressed() {
        return pressed;
    }

    public Vec2D getPos() {
        return pos;
    }

    public void setPos(Vec2D pos) {
        this.pos = pos;
    }

    public abstract void draw(Canvas canvas);

    public boolean checkTouch(MotionEvent event, int pointerId) {
        return false;
    }

    public void onTouch(MotionEvent event) {

    }

    public void onDown(MotionEvent event) {
        this.pressed = true;
        this.onTouch(event);
    }

    public void onRelease(MotionEvent event) {
        this.pressed = false;
    }

    public int pointerId = -1;
}
