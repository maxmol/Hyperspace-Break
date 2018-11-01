package maxmol.igp.Drawing;

import android.graphics.Canvas;
import android.view.MotionEvent;

import maxmol.igp.classes.Vec2D;

/**
 * A joystick that is created where the user tapped and is there until the screen is released or touched outside the dpad circle.
 */
public class DynamicDPad extends DPad {
    @Override
    public boolean checkTouch(MotionEvent event, int pointerId) {
        Vec2D t = new Vec2D(event.getX(pointerId), event.getY(pointerId));
        int action = (event.getAction() & MotionEvent.ACTION_MASK);

        if (!isPressed() && (action == MotionEvent.ACTION_DOWN || action == MotionEvent.ACTION_POINTER_DOWN)) {
            setPos(t);
        }

        if (t.distance(getPos()) <= size * 3) {
            return true;
        }

        return false;
    }

    @Override
    public void draw(Canvas canvas) {
        if (this.isPressed()) {
            super.draw(canvas);
        }
    }
}
