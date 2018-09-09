package info.maxmol.generals.Drawing;

import android.graphics.Canvas;
import android.view.MotionEvent;

import info.maxmol.generals.classes.Vec2D;

public class DynamicDPad extends DPad {
    @Override
    public boolean CheckTouch(MotionEvent event, int pointerId) {
        Vec2D t = new Vec2D(event.getX(pointerId), event.getY(pointerId));
        int action = (event.getAction() & MotionEvent.ACTION_MASK);

        if (!isPressed() && (action == MotionEvent.ACTION_DOWN || action == MotionEvent.ACTION_POINTER_DOWN) && (t.x < GameDraw.context.ScrW * 0.85 || t.y < GameDraw.context.ScrH * 0.75)) {
            setPos(t);
        }

        if (t.Distance(getPos()) <= size * 3) {
            return true;
        }

        return false;
    }

    @Override
    public void Draw(Canvas canvas) {
        if (this.isPressed()) {
            super.Draw(canvas);
        }
    }
}
