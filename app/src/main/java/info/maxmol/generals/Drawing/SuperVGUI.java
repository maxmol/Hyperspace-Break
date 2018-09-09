package info.maxmol.generals.Drawing;

import android.graphics.Canvas;
import android.view.MotionEvent;

import info.maxmol.generals.classes.Vec2D;

// This is the base class for vgui objects in surfaceview. Buttons, Labels, Forms etc. I'll use this in my future projects
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

    public abstract void Draw(Canvas canvas);

    public boolean CheckTouch(MotionEvent event, int pointerId) {
        return false;
    }

    public void OnTouch(MotionEvent event) {

    }

    public void OnDown(MotionEvent event) {
        this.pressed = true;
        this.OnTouch(event);
    }

    public void OnRelease(MotionEvent event) {
        this.pressed = false;
    }

    public int pointerId = -1;
}
