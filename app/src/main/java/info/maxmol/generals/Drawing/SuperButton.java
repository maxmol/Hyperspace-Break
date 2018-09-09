package info.maxmol.generals.Drawing;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.view.MotionEvent;

import info.maxmol.generals.classes.Vec2D;

import static info.maxmol.generals.Drawing.GameDraw.cp;

// This is the first and only object that is a child for my SuperVGUI class.
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
    public void Draw(Canvas canvas) {
        Paint p = new Paint();

        int drawColor = color;
        if (this.isPressed())
            drawColor = Color.argb(Color.alpha(color), Color.red(color) / 2, Color.green(color) / 2, Color.blue(color) / 2);

        float x = (float) getPos().x, y = (float) getPos().y;

        p.setColor(drawColor);
        canvas.drawRect(x - width, y - height, x + width, y + height, p);

        p.setColor(Color.WHITE);
        p.setTextSize(textSize);
        p.setAntiAlias(true);

        Rect textBounds = new Rect();
        p.getTextBounds(text, 0, text.length(), textBounds);

        canvas.drawText(text, (float) getPos().x - textBounds.width()/2, (float) getPos().y + textBounds.height()/2, p);
    }

    @Override
    public boolean CheckTouch(MotionEvent event, int pointerId) {
        return event.getX(pointerId) >= getPos().x - width && event.getX(pointerId) <= getPos().x + width && event.getY(pointerId) >= getPos().y - height && event.getY(pointerId) <= getPos().y + height;
    }

    @Override
    public void OnDown(MotionEvent event) {
        super.OnDown(event);

        if (pressEvent != null) pressEvent.onPress(this, event);
    }
}
