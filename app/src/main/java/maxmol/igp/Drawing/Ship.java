package maxmol.igp.Drawing;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.media.AudioManager;
import android.media.SoundPool;

import maxmol.igp.FlightActivity;
import maxmol.igp.R;
import maxmol.igp.Classes.BulletGenerator;
import maxmol.igp.Classes.Game;
import maxmol.igp.Classes.MUtil;
import maxmol.igp.Classes.Stages;
import maxmol.igp.Classes.Vec2D;

import static maxmol.igp.Drawing.GameDraw.cp;

/**
 * This is the player.
 */
public class Ship extends Entity {
    private static class CritBullet extends Bullet {
        @Override
        public void draw(Canvas canvas) {
            Paint p = new Paint();
            p.setColor(Color.argb(128, 255, 220, 255));
            canvas.drawCircle((float) getPos().x, (float) getPos().y, cp(25f), p);
            canvas.drawCircle((float) getPos().x, (float) getPos().y, cp(20f), p);
        }

        public CritBullet(Vec2D vec2D, Vec2D vel, double accel, int damage, double curving, Entity owner) {
            super(vec2D, vel, accel, damage, curving, owner);
        }
    }

    private static class CritBulletGenerator extends BulletGenerator {
        @Override
        public Bullet constructBullet(Vec2D bulletPos, Vec2D vel) {
            return new CritBullet(bulletPos, vel, bulletAcceleration, bulletDamage, bulletCurve, owner);
        }

        public CritBulletGenerator(Entity e) {
            super(e);
        }
    }

    private BulletGenerator[] bulletGenerators = new BulletGenerator[5];
    private LaserBeam laserBeam;
    private int colorChange = 0;
    private boolean colorIncremental = true;
    public Rect shipRect = new Rect();
    private SoundPool soundPool;
    private int shotSound;
    public DPad movePad;
    private double speed = cp(25);
    private float angle = 0;
    private long soundCoolDown = 0;
    private Bitmap[] bitmaps = new Bitmap[6];
    private int curFrame = 0;
    private long cooldown = 0;
    public int armor = 0;
    public int maxArmor = 10;
    private long armorCooldown = 0;

    @Override
    protected boolean limitMove() {
        return true;
    }

    public Ship() {
        movePad = new DynamicDPad();
        movePad.setPos(new Vec2D(GameDraw.context.scrW * 0.15, GameDraw.context.scrH * 0.8));
        GameDraw.context.addVGUI(movePad);


        setPos(new Vec2D(GameDraw.context.scrW / 2f, GameDraw.context.scrH * 0.8));

        initBulletGenerators();

        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inScaled = false;
        options.outWidth = 64;
        options.outHeight = 64;

        for (int i = 0; i < bitmaps.length; i++) {
            bitmaps[i] = BitmapFactory.decodeResource(FlightActivity.context.getResources(), FlightActivity.context.getResources().getIdentifier("ship" + i, "drawable", FlightActivity.context.getPackageName()), options);
        }

        soundPool = new SoundPool(5, AudioManager.STREAM_MUSIC, 0);
        shotSound = soundPool.load(GameDraw.context.getContext(), R.raw.shotdown, 1);
    }

    public void initBulletGenerators() {
        bulletGenerators[0] = new BulletGenerator(this);
        bulletGenerators[0].currentSpin = 180;
        bulletGenerators[0].bulletSpeed = 30;
        bulletGenerators[0].bulletAcceleration = 0;
        bulletGenerators[0].bulletRate = 8;
        bulletGenerators[0].inaccuracy = 0.04;
        bulletGenerators[0].bulletDamage = 1;
        bulletGenerators[0].bulletOffset.x = cp(35);
        bulletGenerators[0].bulletOffset.y = cp(-30);

        boolean additionalBullets = false;
        switch (Game.attackLevel) {
            case 5: {
                additionalBullets = true;
            }
            case 4: {
                bulletGenerators[0].bulletSpeed += 5;
                bulletGenerators[0].bulletRate -= 2;
                bulletGenerators[0].inaccuracy -= 0.01;
            }
            case 3: {
                bulletGenerators[0].bulletAcceleration += 0.01;
                bulletGenerators[0].bulletSpeed += 5;
                bulletGenerators[0].inaccuracy -= 0.01;
            }
            case 2: {
                bulletGenerators[0].bulletRate -= 2;
                bulletGenerators[0].inaccuracy -= 0.01;
            }
            case 1: {
                bulletGenerators[0].bulletAcceleration += 0.01;
                bulletGenerators[0].inaccuracy -= 0.01;
            }
        }


        if (additionalBullets) {
            bulletGenerators[3] = new BulletGenerator(this);
            bulletGenerators[3].currentSpin = bulletGenerators[0].currentSpin;
            bulletGenerators[3].bulletSpeed = bulletGenerators[0].bulletSpeed;
            bulletGenerators[3].bulletAcceleration = bulletGenerators[0].bulletAcceleration;
            bulletGenerators[3].bulletRate = bulletGenerators[0].bulletRate/2;
            bulletGenerators[3].bulletDamage = bulletGenerators[0].bulletDamage;
            bulletGenerators[3].spinAcceleration = -bulletGenerators[0].spinAcceleration;
            bulletGenerators[3].spinMaxSpeed = bulletGenerators[0].spinMaxSpeed;
            bulletGenerators[3].bulletOffset.x = cp(-35);
            bulletGenerators[3].bulletOffset.y = cp(-30);
            bulletGenerators[3].spinAcceleration = 1;
            bulletGenerators[3].spinMaxSpeed = 3;

            bulletGenerators[4] = new BulletGenerator(this);
            bulletGenerators[4].currentSpin = bulletGenerators[0].currentSpin;
            bulletGenerators[4].bulletSpeed = bulletGenerators[0].bulletSpeed;
            bulletGenerators[4].bulletAcceleration = bulletGenerators[0].bulletAcceleration;
            bulletGenerators[4].bulletRate = bulletGenerators[0].bulletRate/2;
            bulletGenerators[4].bulletDamage = bulletGenerators[0].bulletDamage;
            bulletGenerators[4].spinAcceleration = -bulletGenerators[0].spinAcceleration;
            bulletGenerators[4].spinMaxSpeed = bulletGenerators[0].spinMaxSpeed;
            bulletGenerators[4].bulletOffset.x = cp(35);
            bulletGenerators[4].bulletOffset.y = cp(-30);
            bulletGenerators[4].spinAcceleration = -1;
            bulletGenerators[4].spinMaxSpeed = 3;
        }

        bulletGenerators[1] = new BulletGenerator(this);
        bulletGenerators[1].currentSpin = bulletGenerators[0].currentSpin;
        bulletGenerators[1].bulletSpeed = bulletGenerators[0].bulletSpeed;
        bulletGenerators[1].bulletAcceleration = bulletGenerators[0].bulletAcceleration;
        bulletGenerators[1].bulletRate = bulletGenerators[0].bulletRate;
        bulletGenerators[1].bulletDamage = bulletGenerators[0].bulletDamage;
        bulletGenerators[1].inaccuracy = bulletGenerators[0].inaccuracy;
        bulletGenerators[1].spinAcceleration = bulletGenerators[0].spinAcceleration;
        bulletGenerators[1].spinMaxSpeed = bulletGenerators[0].spinMaxSpeed;
        bulletGenerators[1].bulletOffset.x = cp(-35);
        bulletGenerators[1].bulletOffset.y = cp(-30);

        bulletGenerators[2] = new CritBulletGenerator(this);
        bulletGenerators[2].currentSpin = 180;
        bulletGenerators[2].bulletSpeed = 25;
        bulletGenerators[2].bulletRate = 160;
        bulletGenerators[2].bulletDamage = 5;
        bulletGenerators[2].bulletOffset.y = cp(-40);

        switch (Game.critLevel) {
            case 5: {
                bulletGenerators[2].bulletDamage += 1;
                bulletGenerators[2].bulletSpeed += 5;
            }
            case 4: {
                bulletGenerators[2].bulletSpeed += 5;
                bulletGenerators[2].bulletRate -= 50;
            }
            case 3: {
                bulletGenerators[2].bulletDamage += 1;
                bulletGenerators[2].bulletRate -= 25;
            }
            case 2: {
                bulletGenerators[2].bulletDamage += 1;
                bulletGenerators[2].bulletRate -= 25;
            }
            case 1: {
                bulletGenerators[2].bulletSpeed += 5;
                bulletGenerators[2].bulletRate -= 50;
            }
        }
    }

    public boolean activateLaser() {
        if (laserBeam == null || laserBeam.isDead()) {
            if (Game.laserAttackCount > 0) {
                laserBeam = new LaserBeam(250 + Game.bombLevel * 50, 1, 20);
                laserBeam.setOwner(this);
                laserBeam.angle = -90;
                GameDraw.context.AddEntity(laserBeam);

                Game.laserAttackCount--;
                return true;
            }
        }

        return false;
    }

    @Override
    public void tick() {
        double posX = getPos().x, posY = getPos().y;
        shipRect.set((int) (cp(-38) + posX), (int) (cp(-38) + posY), (int) (cp(38) + posX), (int) (cp(45) + posY));
        for (BulletGenerator bg: bulletGenerators) {
            if (bg != null) bg.update();
        }

        for (Entity e: GameDraw.context.getEntities()) {
            if (!(e instanceof Pickable)) continue;

            if (e.getPos().distance(this.getPos()) < cp(50))
            {
                ((Pickable) e).collect();
            }
        }

        move(movePad.getOutput().mul(speed));

        for (BulletGenerator b: bulletGenerators) {
            if (b != null) {
                b.currentSpin = 180 + getAngle();
            }
        }

        long curtime = System.currentTimeMillis();
        if (armor < maxArmor && armorCooldown < curtime) {
            armorCooldown = curtime + 500;
            armor++;
        }
    }

    @Override
    public void draw(Canvas canvas) {
        canvas.save();
        canvas.rotate(angle, (float) getPos().x, (float) getPos().y);

        Paint p = new Paint();
        p.setAntiAlias(false);
        p.setFilterBitmap(false);
        p.setDither(true);

        int posx = (int) getPos().x, posy = (int) getPos().y;
        int size = (int)cp(86);
        canvas.drawBitmap(bitmaps[curFrame], null, new Rect(posx - size, posy - size, posx + size, posy + size), p);

        if (System.currentTimeMillis() > cooldown && !GameDraw.context.paused) {
            curFrame = (curFrame + 1) % bitmaps.length;
            cooldown = System.currentTimeMillis() + 100;

            Particle par = new Particle();
            par.pos = getPos().plus(new Vec2D(0, cp(75)));
            par.duration = .25f;
            par.startSize = cp(8);
            par.endSize = cp(4);
            par.color = Color.HSVToColor(new float[] {35 + (float)(Math.random() * 20), 97, 100});
            par.vel = new Vec2D(cp(2 - Math.random() * 4), cp(5 + Math.random() * 10));
            par.fric = 0.95;
            GameDraw.context.AddEntity(par);

            par = new Particle();
            par.pos = getPos().plus(new Vec2D(-50, cp(40)));
            par.duration = .25f;
            par.startSize = cp(5);
            par.endSize = cp(3);
            par.color = Color.HSVToColor(new float[] {35 + (float)(Math.random() * 20), 97, 100});
            par.vel = new Vec2D(cp(2 - Math.random() * 4), cp(5 + Math.random() * 10));
            par.fric = 0.95;
            GameDraw.context.AddEntity(par);

            par = new Particle();
            par.pos = getPos().plus(new Vec2D(50, cp(40)));
            par.duration = .25f;
            par.startSize = cp(5);
            par.endSize = cp(3);
            par.color = Color.HSVToColor(new float[] {35 + (float)(Math.random() * 20), 97, 100});
            par.vel = new Vec2D(cp(2 - Math.random() * 4), cp(5 + Math.random() * 10));
            par.fric = 0.95;
            GameDraw.context.AddEntity(par);
        }

        if (laserBeam != null && !laserBeam.isDead()) {
            if (colorIncremental)
                colorChange += 2;
            else
                colorChange -= 2;

            if (colorChange >= 128)
                colorIncremental = false;

            if (colorChange <= 64) {
                colorIncremental = true;
            }

            int clr = Color.HSVToColor(128, new float[]{280, colorChange / 128f, 1});
            laserBeam.color = clr;
        }

        canvas.restore();
    }

    @Override
    public int getZPos() {
        return 10;
    }

    @Override
    public void takeDamage(int hp) {
        if (this.isValid()) {
            long curtime = System.currentTimeMillis();
            armorCooldown = curtime + 5000;

            hp = (int) (hp * (Stages.isArcade() ? 1 : 1 + Game.difficulty /5f));

            armor = MUtil.clamp(armor - hp, -Game.getMaxHealth(), maxArmor);

            if (soundCoolDown < curtime) {
                soundCoolDown = curtime + 100;
                soundPool.play(shotSound, 0.5f, 0.5f, 0, 0, 0.8f);
            }

            if (armor < 0) {
                Game.takeDamage(-armor);
                armor = 0;

                if (Game.getHealth() <= 0) {
                    GameDraw.context.AddEntity(
                            new SparksEffect(
                                    new Vec2D(getPos().x, getPos().y),
                                    12 + random.nextInt(5),
                                    1, 0, 10, 0.8,
                                    Color.rgb(196, 0, 0)
                            )
                    );
                    GameDraw.context.AddEntity(new ExplosionEffect(new Vec2D(getPos().x, getPos().y), 2, 0));

                    if (!Stages.isArcade()) {
                        if (Game.getStep() == Game.getStage()) {
                            for (int i = 0; i < (Stages.getMoney() / 10); i++)
                                GameDraw.context.AddEntity(new Coin(-1, getPos()));
                        }
                        Stages.collectMoney(-Stages.getMoney());
                    }
                    remove();
                }
            }
        }
    }

    @Override
    public boolean isPhysicsObject() {
        return true;
    }

    @Override
    public float getAngle() {
        return angle;
    }

    public void setAngle(float angle) {
        this.angle = angle;
    }

    @Override
    public int getCollisionRadius() {
        return (int) cp(50);
    }

    @Override
    public void remove() {
        if (laserBeam != null) laserBeam.remove();
        super.remove();
    }
}