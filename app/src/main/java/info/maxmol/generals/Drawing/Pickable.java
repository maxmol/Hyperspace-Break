package info.maxmol.generals.Drawing;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;

import info.maxmol.generals.classes.Vec2D;

import static info.maxmol.generals.Drawing.GameDraw.cp;

public class Pickable extends Entity {
    public Vec2D velocity;
    public static int radius;
    protected String pickableText = "P";
    protected int pickableColor = Color.CYAN;

    public Pickable() {
        radius = (int) cp(25);
    }

    public Pickable(Vec2D vec2D) {
        setPos(vec2D);
        velocity = new Vec2D((Math.random() - 0.5) * 10, cp(-15.0));
        radius = (int) cp(25);
    }

    public void collect() {
        this.Remove();
    }

    @Override
    public void Tick() {
        if (Math.abs(velocity.x) > 1 || Math.abs(velocity.y) > 1) {
            velocity.multiply(0.95);
        }
        else velocity = Vec2D.zero;

        if (GameDraw.context.ship.getPos().Distance(this.getPos()) < cp(200)) {
            Vec2D t = GameDraw.context.ship.getPos().minus(this.getPos());
            velocity = t.GetNormalized().mul(cp(200.0)).minus(t).mul(0.1);
        }

        Move(velocity.plus(new Vec2D(0, cp(4))));

        if (getPos().y > GameDraw.context.ScrH + cp(50)) {
            Remove();
        }
    }

    @Override
    public void Draw(Canvas canvas) {
        float posx = (float) getPos().x, posy = (float) getPos().y;

        Paint p = new Paint();
        p.setAntiAlias(true);
        p.setStyle(Paint.Style.FILL);
        p.setColor(Color.rgb(255, 255, 0));
        canvas.drawCircle(posx, posy, radius, p);

        p.setStyle(Paint.Style.STROKE);
        p.setColor(pickableColor);
        p.setStrokeWidth(radius * 0.15f);
        canvas.drawCircle(posx, posy, radius, p);

        p.setTextSize(radius * 2f);
        p.setColor(Color.BLACK);
        p.setTextAlign(Paint.Align.CENTER);
        canvas.drawText(pickableText, posx, posy + radius * 0.6f, p);
    }

    @Override
    public Path generatePath() {
        double posx = getPos().x, posy = getPos().y;

        Path path = new Path();

        path.setFillType(Path.FillType.EVEN_ODD);

        path.moveTo((float) (posx - radius), (float) (posy - radius));
        path.lineTo((float) (posx + radius), (float) (posy - radius));
        path.lineTo((float) (posx + radius), (float) (posy + radius));
        path.lineTo((float) (posx - radius), (float) (posy + radius));
        path.close();

        return path;
    }

    @Override
    public boolean isPhysicsObject() {
        return false;
    }

    @Override
    public int getZPos() {
        return 0;
    }
}
