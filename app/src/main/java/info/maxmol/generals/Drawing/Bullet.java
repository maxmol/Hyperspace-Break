package info.maxmol.generals.Drawing;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Region;

import java.util.ArrayList;

import info.maxmol.generals.classes.MUtil;
import info.maxmol.generals.classes.Vec2D;

public class Bullet extends Entity{
    public Vec2D velocity;
    public Vec2D startVelocity;
    public double acceleration;
    public double curving = 0;
    private int damage;
    private Entity owner;
    private static double bulletLen = cp(16);
    private static double bulletWide = cp(8);

    public Bullet() {

    }

    public Bullet(Vec2D vec2D) {
        setPos(vec2D);
    }

    public Bullet(Vec2D vec2D, Vec2D vel, double accel) {
        setPos(vec2D);
        velocity = vel;
        startVelocity = vel;
        acceleration = accel;
    }

    public Bullet(Vec2D vec2D, Vec2D vel, double accel, int damage) {
        setPos(vec2D);
        velocity = vel;
        startVelocity = vel;
        acceleration = accel;
        setDamage(damage);
    }

    public Bullet(Vec2D vec2D, Vec2D vel, double accel, int damage, double curving, Entity owner) {
        setPos(vec2D);
        velocity = vel;
        startVelocity = vel;
        acceleration = accel;
        this.curving = curving;
        setDamage(damage);
        this.owner = owner;
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

        Region clip = new Region(0, 0, GameDraw.context.ScrW, GameDraw.context.ScrH);

        for (Entity e: (ArrayList<Entity>) GameDraw.context.entities.clone()) {
            if (!e.isPhysicsObject()) continue;
            if (e instanceof Bullet) continue;

            if (this.getOwner() instanceof Enemy && e instanceof Enemy) {
                continue;
            }

            if (this.getOwner() == e) {
                continue;
            }

            if (e.getPos().Distance(this.getPos()) > cp(100)) { // FPS Fix
                continue;
            }

            Region eReg = new Region();
            eReg.setPath(e.generatePath(), clip);

            if (eReg.contains((int) (getPos().x), (int) (getPos().y)))
            {
                e.takeDamage(getDamage());
                Remove();
            }
        }
    }

    @Override
    public void Draw(Canvas canvas) {
        Paint p = new Paint();
        p.setColor(Color.RED);

        Vec2D normVel = velocity.GetNormalized();
        Vec2D drawLen = normVel.mul(bulletLen);
        Vec2D drawWide = normVel.GetRotated(90).mul(bulletWide);

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

        canvas.drawPath(path, p);
    }

    @Override
    public int getZPos() {
        return -0;
    }

    @Override
    public boolean isPhysicsObject() {
        return true;
    }
}