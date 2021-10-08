package maxmol.igp.Drawing;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;

import maxmol.igp.classes.MUtil;
import maxmol.igp.classes.Vec2D;

import static maxmol.igp.Drawing.GameDraw.cp;

/**
 * Just a bullet. It can be spawned by bulletgenerators.
 */
public class Bullet extends Entity{
    public Vec2D velocity;
    public Vec2D startVelocity;
    public double acceleration;
    public double curving = 0;
    protected int damage;
    protected Entity owner;
    protected static double bulletLen = 12;
    protected static double bulletWide = 4;

    protected Paint bulletPaint;

    protected void initPaint() {
        bulletPaint = new Paint();
        bulletPaint.setAntiAlias(true);
        //bulletPaint.setMaskFilter(new BlurMaskFilter(cp(4), BlurMaskFilter.Blur.NORMAL));
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
        this.damage = MUtil.clamp(damage, 0);
    }

    @Override
    public void tick() {
        move(velocity);

        velocity.add(startVelocity.mul(acceleration));
        if (curving != 0) velocity.rotate(curving);

        if (getPos().y > GameDraw.context.scrH + cp(5) || getPos().y < cp(-5) || getPos().x > GameDraw.context.scrW + cp(5) || getPos().x < cp(-5)) {
            remove();
        }

        if (this.getOwner() instanceof Enemy || this.getOwner() instanceof Bullet) {
            if (GameDraw.context.ship.shipRect.contains((int) getPos().x, (int) getPos().y))
                hit(GameDraw.context.ship);
        }
        else {
            for (Entity e : GameDraw.context.getEntities()) {
                if (!e.isPhysicsObject()) continue;
                if (this.getOwner() == e) continue;
                if (e.getPos().distance(this.getPos()) > e.getCollisionRadius()) continue;

                if (e.getRegion().contains((int) (getPos().x - e.getPos().x), (int) (getPos().y - e.getPos().y))) {
                    hit(e);
                }
            }
        }
    }

    /**
     * Runs when a bullet hits an object
     * @param e: The object which the bullet just hit: Entity
     */
    public void hit(Entity e) {
        if (e != null) e.takeDamage(getDamage());
        GameDraw.context.AddEntity(new SparksEffect(getPos(), 3 + (int)(Math.random() * 3), 1, 0, 6, 1, this.getOwner() instanceof Ship ? Color.rgb(235, 144, 40) : Color.rgb(64, 111, 230)));
        remove();
    }

    @Override
    public void draw(Canvas canvas) {
        if (this.getOwner() instanceof Ship) {
            bulletPaint.setColor(Color.rgb(227, 48, 200));
        }
        else {
            bulletPaint.setColor(Color.rgb(64, 111, 230));
        }

        Vec2D normVel = velocity.getNormalized();
        Vec2D drawLen = normVel.mul(cp(bulletLen));
        Vec2D drawWide = normVel.getRotated(90).mul(cp(bulletWide));

        Vec2D topleft = getPos().plus(drawLen).minus(drawWide);
        Vec2D topright = getPos().plus(drawLen).plus(drawWide);
        Vec2D bottomright = getPos().minus(drawLen).plus(drawWide);
        Vec2D bottomleft = getPos().minus(drawLen).minus(drawWide);

        /*
        old style
        Vec2D top = getPos().plus(drawLen);
        Vec2D bottom = getPos().minus(drawLen);
        Vec2D right = getPos().plus(drawWide);
        Vec2D left = getPos().minus(drawWide);

        Path path = new Path();
        path.moveTo((float) top.x, (float) top.y);
        path.lineTo((float) right.x, (float) right.y);
        path.lineTo((float) bottom.x, (float) bottom.y);
        path.lineTo((float) left.x, (float) left.y);
        path.close();*/

        Path path = new Path();
        path.moveTo((float) topleft.x, (float) topleft.y);
        path.lineTo((float) topright.x, (float) topright.y);
        path.lineTo((float) bottomright.x, (float) bottomright.y);
        path.lineTo((float) bottomleft.x, (float) bottomleft.y);
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