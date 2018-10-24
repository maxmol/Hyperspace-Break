package maxmol.igp.Drawing;

import android.graphics.BlurMaskFilter;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Region;

import java.util.ArrayList;

import maxmol.igp.classes.MUtil;
import maxmol.igp.classes.Stages;
import maxmol.igp.classes.Vec2D;

import static maxmol.igp.Drawing.GameDraw.cp;

// Just a bullet. It can be spawned by bulletgenerators.
public class Bullet extends Entity{
    public Vec2D velocity;
    public Vec2D startVelocity;
    public double acceleration;
    public double curving = 0;
    protected int damage;
    protected Entity owner;
    protected static double bulletLen = 16;
    protected static double bulletWide = 8;

    protected Paint bulletPaint;

    protected void initPaint() {
        bulletPaint = new Paint();
        bulletPaint.setAntiAlias(true);
        //bulletPaint.setMaskFilter(new BlurMaskFilter(cp(1), BlurMaskFilter.Blur.NORMAL));
    }

    public Bullet() {
        initPaint();
    }

    public Bullet(Vec2D vec2D) {
        setPos(vec2D);
        initPaint();
    }

    public Bullet(Vec2D vec2D, Vec2D vel, double accel) {
        setPos(vec2D);
        velocity = vel;
        startVelocity = vel.copy();
        acceleration = accel;

        initPaint();
    }

    public Bullet(Vec2D vec2D, Vec2D vel, double accel, int damage) {
        setPos(vec2D);
        velocity = vel;
        startVelocity = vel.copy();
        acceleration = accel;
        setDamage(damage);

        initPaint();
    }

    public Bullet(Vec2D vec2D, Vec2D vel, double accel, int damage, double curving, Entity owner) {
        setPos(vec2D);
        velocity = vel;
        startVelocity = vel.copy();
        acceleration = accel;
        this.curving = curving;
        setDamage(damage);
        this.owner = owner;

        initPaint();
    }

    public Entity getOwner() {
        if (owner == null) return this;

        return owner;
    }

    public int getDamage() {
        return this.damage;
    }

    public void setDamage(int damage) {
        this.damage = MUtil.Clamp(damage, 0);
    }

    @Override
    public void Tick() {
        Move(velocity);

        velocity.add(startVelocity.mul(acceleration));
        if (curving != 0) velocity.Rotate(curving);

        if (getPos().y > GameDraw.context.ScrH + cp(5) || getPos().y < cp(-5) || getPos().x > GameDraw.context.ScrW + cp(5) || getPos().x < cp(-5)) {
            Remove();
        }

        if (this.getOwner() instanceof Enemy || this.getOwner() instanceof Bullet) {
            if (GameDraw.context.ship.shipRect.contains((int) getPos().x, (int) getPos().y))
                hit(GameDraw.context.ship);
        }
        else {
            for (Entity e : GameDraw.context.getEntities()) {
                if (!e.isPhysicsObject()) continue;
                if (this.getOwner() == e) continue;
                if (e.getPos().Distance(this.getPos()) > e.getCollisionRadius()) continue;

                if (e.getRegion().contains((int) (getPos().x - e.getPos().x), (int) (getPos().y - e.getPos().y))) hit(e);

            }
        }
    }

    // @ Runs when a bullet hits an object
    // @params
    //      The object which the bullet just hit: Entity
    public void hit(Entity e) {
        if (e != null) e.takeDamage(getDamage());
        GameDraw.context.AddEntity(new SparksEffect(getPos(), 3 + (int)(Math.random() * 3), 1, 0, 6, 1, this.getOwner() instanceof Ship ? Color.rgb(64, 128, 255) : Color.RED));
        Remove();
    }

    @Override
    public void Draw(Canvas canvas) {
        if (this.getOwner() instanceof Ship) {
            bulletPaint.setColor(Color.rgb(64, 128, 255));
        }
        else {
            bulletPaint.setColor(Color.RED);
        }

        Vec2D normVel = velocity.GetNormalized();
        Vec2D drawLen = normVel.mul(cp(bulletLen));
        Vec2D drawWide = normVel.GetRotated(90).mul(cp(bulletWide));

        Vec2D top = getPos().plus(drawLen);
        Vec2D bottom = getPos().minus(drawLen);
        Vec2D right = getPos().plus(drawWide);
        Vec2D left = getPos().minus(drawWide);

        Path path = new Path();
        path.moveTo((float) top.x, (float) top.y);
        path.lineTo((float) right.x, (float) right.y);
        path.lineTo((float) bottom.x, (float) bottom.y);
        path.lineTo((float) left.x, (float) left.y);
        path.close();

        canvas.drawPath(path, bulletPaint);
    }

    @Override
    public int getZPos() {
        return -0;
    }

    @Override
    public boolean isPhysicsObject() {
        return false;
    }
}