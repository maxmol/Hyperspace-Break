package info.maxmol.generals.Drawing;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Region;

import java.util.ArrayList;

import info.maxmol.generals.classes.BulletGenerator;
import info.maxmol.generals.classes.MUtil;
import info.maxmol.generals.classes.Stages;
import info.maxmol.generals.classes.Vec2D;

public class Enemy extends Entity {
    private int health;
    private int maxHealth;
    private double speed;
    private BulletGenerator bulletGenerator;
    public static int counterToNextSpawn = 2000;


    public Enemy() {
        setPos(new Vec2D(Math.random() * GameDraw.context.ScrW, cp(-20)));
        setPointsMesh(new Vec2D[] {
                new Vec2D(cp(40), cp(-50)),
                new Vec2D(cp(-40), cp(-50)),
                new Vec2D(0, cp(50)),
        });

        setMaxHealth(20);
        setHealth(getMaxHealth());

        bulletGenerator = Stages.getBulletGenerator();
        bulletGenerator.setOwner(this);
        speed = cp(2.5);
    }

    @Override
    public void Tick() {
        Move(new Vec2D(0, speed));
        if (getPos().y > GameDraw.context.ScrH + cp(50)) {
            Remove();
        }

        bulletGenerator.update();
    }

    @Override
    public void Draw(Canvas canvas) {
        Paint p = new Paint();
        p.setColor(Color.RED);

        drawByPoints(canvas, p);
    }

    @Override
    public int getZPos() {
        return 5;
    }

    @Override
    public boolean isPhysicsObject() {
        return true;
    }

    public void setHealth(int health) {
        this.health = MUtil.Clamp(health, 0, getMaxHealth());
    }

    public int getHealth() {
        return health;
    }

    public void addHealth(int health) {
        setHealth(getHealth() + MUtil.Clamp(health, 0));
    }

    public void kill() {
        // todo: effect here
        GameDraw.context.AddEntity(new Coin(10, getPos()));
        Remove();
    }

    public void takeDamage(int damage) {
        setHealth(getHealth() - MUtil.Clamp(damage, 0));

        if (getHealth() <= 0) {
            kill();
        }
    }

    public int getMaxHealth() {
        return maxHealth;
    }

    public void setMaxHealth(int health) {
        this.maxHealth = MUtil.Clamp(health, 0);
    }
}
