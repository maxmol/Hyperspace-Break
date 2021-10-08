package maxmol.igp.Drawing;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import java.util.ArrayList;

import maxmol.igp.classes.MUtil;
import maxmol.igp.classes.Vec2D;

import static maxmol.igp.Drawing.GameDraw.cp;

/**
 * Cool effect created when a bullet hits something.
 */
public class SparksEffect extends Entity {

    private static class Spark {
        public Vec2D pos;
        public Vec2D vel;

        public Spark(Vec2D pos, Vec2D vel) {
            this.pos = pos;
            this.vel = vel;
        }
    }

    private ArrayList<Spark> sparks = new ArrayList<>();
    private int count = 1;
    private double force = 1;
    private int spawn = 1;
    private int spawnRate = 0;
    private int curSpawnRate = 0;
    private double fade;
    private int color = Color.YELLOW;

    private void spawn() {
        for (int i = 0; i < count; i++) {
            Vec2D vel = Vec2D.random().mul((Math.random() + 0.5) * force);
            sparks.add(new Spark(getPos().plus(vel.mul(8.0)), vel));
        }

        spawn--;
    }

    public SparksEffect(Vec2D pos, int count, int spawn, int spawnRate, double force, double fade, int color) {
        this.count = MUtil.clamp(count, 1, 50);
        this.force = cp(force);
        this.spawn = spawn;
        this.spawnRate = spawnRate;
        this.color = color;
        this.fade = fade < 1 ? (1 - fade / 5) : fade;
        curSpawnRate = spawnRate;
        setPos(pos);
    }

    @Override
    public void tick() {
        if (spawn <= 0 && sparks.size() <= 0) {
            remove();
        }

        for (Spark spark: (ArrayList<Spark>) sparks.clone()) {
            spark.pos = spark.pos.plus(spark.vel);
            spark.vel = spark.vel.mul(0.8 / fade);

            if (spark.vel.length() < 1) {
                sparks.remove(spark);
            }
        }

        if (--curSpawnRate <= 0 && spawn > 0) {
            spawn();
            curSpawnRate = spawnRate;
        }
    }

    @Override
    public void draw(Canvas canvas) {
        Paint p = new Paint();
        p.setColor(color);
        p.setAntiAlias(true);

        for (Spark spark: sparks) {
            float length = (float) (spark.vel.length() * 8f);
            float x = (float) spark.pos.x, y = (float) spark.pos.y;

            canvas.save();
            canvas.rotate((float) Vec2D.zero.getRotationTo(spark.vel), x, y);
            canvas.drawRect(x - cp(2f), y - length, x + cp(2f), y, p);
            canvas.restore();
        }
    }

    @Override
    public boolean isPhysicsObject() {
        return false;
    }

    @Override
    public int getZPos() {
        return 20;
    }
}
