package maxmol.igp.Drawing;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.view.MotionEvent;

import maxmol.igp.Classes.Vec2D;

/**
 * A button that can be pressed.
 */
public class SuperButton extends SuperVGUI {
    public interface SuperPressEvent {
        void onPress(SuperButton self, MotionEvent event);
    }

    private float width, height;
    private int color;
    private SuperPressEvent pressEvent;
    private String text;

    private float textSize;

    public SuperButton(Vec2D pos, float width, float height, String text, float textSize, int color, SuperPressEvent pressEvent) {
        setPos(pos);
        this.width = width;
        this.height = height;
        this.text = text;
        this.textSize = textSize;
        this.color = color;
        this.pressEvent = pressEvent;
    }

    public float getWidth() {
        return width;
    }

    public void setWidth(float width) {
        this.width = width;
    }

    public float getHeight() {
        return height;
    }

    public void setHeight(float height) {
        this.height = height;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public float getTextSize() {
        return textSize;
    }

    public void setTextSize(float textSize) {
        this.textSize = textSize;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    @Override
    public void draw(Canvas canvas) {
        Paint p = new Paint();

        int drawColor = color;
        if (this.isPressed())
            drawColor = Color.argb(Color.alpha(color), Color.red(color) / 2, Color.green(color) / 2, Color.blue(color) / 2);

        float x = (float) getPos().x, y = (float) getPos().y;

        p.setColor(drawColor);
        canvas.drawRect(x, y, x + width, y + height, p);

        p.setColor(Color.WHITE);
        p.setTextSize(textSize);
        p.setAntiAlias(true);

        Rect textBounds = new Rect();
        p.getTextBounds(text, 0, text.length(), textBounds);

        canvas.drawText(text, (float) getPos().x + width/2 - textBounds.exactCenterX(), (float) getPos().y + height/2 - textBounds.exactCenterY(), p);
    }

    @Override
    public boolean checkTouch(MotionEvent event, int pointerId) {
        return event.getX(pointerId) >= getPos().x && event.getX(pointerId) <= getPos().x + width && event.getY(pointerId) >= getPos().y && event.getY(pointerId) <= getPos().y + height;
    }

    @Override
    public void onDown(MotionEvent event) {
        super.onDown(event);

        if (pressEvent != null) pressEvent.onPress(this, event);
    }
}
