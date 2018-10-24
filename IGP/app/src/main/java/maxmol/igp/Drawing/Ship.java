package maxmol.igp.Drawing;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.media.AudioManager;
import android.media.SoundPool;
import android.view.MotionEvent;

import maxmol.igp.R;
import maxmol.igp.classes.BulletGenerator;
import maxmol.igp.classes.Game;
import maxmol.igp.classes.Stages;
import maxmol.igp.classes.Vec2D;

import static maxmol.igp.Drawing.GameDraw.cp;

// This is the player.
public class Ship extends Entity {
    private static class CritBullet extends Bullet {
        @Override
        public void Draw(Canvas canvas) {
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

    private boolean moveStarted = false;
    private BulletGenerator[] bulletGenerators = new BulletGenerator[5];
    private LaserBeam laserBeam;
    private int colorChange = 0;
    private boolean colorIncremental = true;
    public Rect shipRect = new Rect();
    private SoundPool soundPool;
    private int shotSound;
    private int coinSound;
    private Vec2D direction = new Vec2D(0, -1);
    public DPad movePad;//, rotatePad;
    private double speed = cp(20);
    private float angle = 0;
    private long soundCoolDown = 0;

    @Override
    protected boolean limitMove() {
        return true;
    }

    public Ship() {
        movePad = new DynamicDPad();
        movePad.setPos(new Vec2D(GameDraw.context.ScrW * 0.15, GameDraw.context.ScrH * 0.8));
        GameDraw.context.AddVGUI(movePad);

        /*rotatePad = new DPad();
        rotatePad.save = true;
        rotatePad.setPos(new Vec2D(GameDraw.context.ScrW * 0.75, GameDraw.context.ScrH * 0.8));
        GameDraw.context.AddVGUI(rotatePad);*/

        setPos(new Vec2D(GameDraw.context.ScrW/2, GameDraw.context.ScrH * 0.8));
        setPointsMesh(new Vec2D[] {
                new Vec2D(cp(30), cp(51)),
                new Vec2D(cp(-30), cp(51)),
                new Vec2D(cp(-30), cp(-55)),
                new Vec2D(cp(30), cp(-55)),
        });

        bulletGenerators[0] = new BulletGenerator(this);
        bulletGenerators[0].currentSpin = 180;
        bulletGenerators[0].bulletSpeed = 20;
        bulletGenerators[0].bulletAcceleration = 0;
        bulletGenerators[0].bulletRate = 12;
        bulletGenerators[0].inaccuracy = 0.04;
        bulletGenerators[0].bulletDamage = 1;
        bulletGenerators[0].bulletOffset.x = cp(24);
        bulletGenerators[0].bulletOffset.y = cp(-80);

        boolean additionalBullets = false;
        switch (Game.AttackLevel) {
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
                bulletGenerators[0].bulletRate -= 3;
                bulletGenerators[0].inaccuracy -= 0.01;
            }
            case 1: {
                bulletGenerators[0].bulletAcceleration += 0.01;
                bulletGenerators[0].bulletRate -= 1;
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
            bulletGenerators[3].bulletOffset.x = cp(-24);
            bulletGenerators[3].bulletOffset.y = cp(-80);
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
            bulletGenerators[4].bulletOffset.x = cp(24);
            bulletGenerators[4].bulletOffset.y = cp(-80);
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
        bulletGenerators[1].bulletOffset.x = cp(-24);
        bulletGenerators[1].bulletOffset.y = cp(-80);

        bulletGenerators[2] = new CritBulletGenerator(this);
        bulletGenerators[2].currentSpin = 180;
        bulletGenerators[2].bulletSpeed = 15;
        bulletGenerators[2].bulletRate = 200;
        bulletGenerators[2].bulletDamage = 5;
        bulletGenerators[2].bulletOffset.y = cp(-80);

        switch (Game.CritLevel) {
            case 5: {
                bulletGenerators[2].bulletDamage += 5;
                bulletGenerators[2].bulletSpeed += 5;
            }
            case 4: {
                bulletGenerators[2].bulletSpeed += 5;
                bulletGenerators[2].bulletRate -= 50;
            }
            case 3: {
                bulletGenerators[2].bulletDamage += 3;
                bulletGenerators[2].bulletRate -= 25;
            }
            case 2: {
                bulletGenerators[2].bulletDamage += 2;
                bulletGenerators[2].bulletRate -= 25;
            }
            case 1: {
                bulletGenerators[2].bulletSpeed += 5;
                bulletGenerators[2].bulletRate -= 50;
            }
        }

        bitmap = BitmapFactory.decodeResource(GameDraw.context.getResources(), R.drawable.ship);
        bitmap = Bitmap.createScaledBitmap(bitmap, (int) cp(180), (int) cp(180), false);

        soundPool = new SoundPool(5, AudioManager.STREAM_MUSIC, 0);
        shotSound = soundPool.load(GameDraw.context.getContext(), R.raw.shotdown, 1);
        coinSound = soundPool.load(GameDraw.context.getContext(), R.raw.blip1, 1);
    }

    public boolean activateLaser() {
        if (laserBeam == null || laserBeam.isDead()) {
            if (Game.LaserAttackCount > 0) {
                laserBeam = new LaserBeam(250 + Game.BombLevel * 50, 1, 20);
                laserBeam.setOwner(this);
                laserBeam.angle = -90;
                GameDraw.context.AddEntity(laserBeam);

                Game.LaserAttackCount--;
                return true;
            }
        }

        return false;
    }

    @Override
    public void Tick() {
        double posX = getPos().x, posY = getPos().y;
        shipRect.set((int) (cp(-38) + posX), (int) (cp(-70) + posY), (int) (cp(38) + posX), (int) (cp(65) + posY));
        for (BulletGenerator bg: bulletGenerators) {
            if (bg != null) bg.update();
        }

        //Region clip = new Region(0, 0, GameDraw.context.ScrW, GameDraw.context.ScrH);

        for (Entity e: GameDraw.context.getEntities()) {
            if (!(e instanceof Pickable)) continue;

            /*if (e.getPos().Distance(this.getPos()) > cp(100)) {
                continue;
            }

            Region eReg = new Region();
            eReg.setPath(e.generatePath(), clip);

            Region thisReg = new Region();
            thisReg.setPath(generatePath(), clip);

            new Vec2D(cp(38), cp(65)),
                new Vec2D(cp(-38), cp(65)),
                new Vec2D(cp(-38), cp(-70)),
                new Vec2D(cp(38), cp(-70)),*/


            if (e.getPos().Distance(this.getPos()) < cp(50))
            {
                ((Pickable) e).collect();
            }
        }

        Move(movePad.getOutput().mul(speed));

        //setAngle(rotatePad.getRotation());

        for (BulletGenerator b: bulletGenerators) {
            if (b != null) {
                b.currentSpin = 180 + getAngle();
            }
        }
    }

    @Override
    public void Draw(Canvas canvas) {
        canvas.save();
        canvas.rotate(angle, (float) getPos().x, (float) getPos().y);

        Paint p = new Paint();
        p.setAntiAlias(true);
        canvas.drawBitmap(bitmap, (float) (getPos().x - bitmap.getWidth()/2), (float) (getPos().y - bitmap.getHeight()/2), p);

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
        //drawByPoints(canvas, p);
    }

    @Override
    public int getZPos() {
        return 10;
    }

    @Override
    public void takeDamage(int hp) {
        if (this.isValid()) {
            Game.takeDamage(hp);

            if (soundCoolDown < System.currentTimeMillis()) {
                soundCoolDown = System.currentTimeMillis() + 100;
                soundPool.play(shotSound, 0.5f, 0.5f, 0, 0, 0.8f);
            }

            if (Game.getHealth() <= 0) {
                GameDraw.context.AddEntity(new SparksEffect(new Vec2D(getPos().x, getPos().y), (int) (Math.random() * 5) + 12, 1, 0, 10, 0.8, Color.rgb(196, 0, 0)));
                GameDraw.context.AddEntity(new ExplosionEffect(new Vec2D(getPos().x, getPos().y), 2, 0));

                if (Game.getStage() != Stages.COUNT + 1) {
                    if (Game.getStep() == Game.getStage()) {
                        for (int i = 0; i < (Stages.getMoney() / 10); i++)
                            GameDraw.context.AddEntity(new Coin(-1, getPos()));
                    }
                    Stages.collectMoney(-Stages.getMoney());
                }
                else {
                    Game.addMoney(Stages.getMoney());
                }
                Remove();
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
    public void Remove() {
        if (laserBeam != null) laserBeam.Remove();
        super.Remove();
    }
}