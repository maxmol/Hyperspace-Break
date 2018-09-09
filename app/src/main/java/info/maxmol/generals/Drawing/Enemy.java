package info.maxmol.generals.Drawing;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.media.AudioManager;
import android.media.SoundPool;

import info.maxmol.generals.R;
import info.maxmol.generals.classes.BulletGenerator;
import info.maxmol.generals.classes.Game;
import info.maxmol.generals.classes.MUtil;
import info.maxmol.generals.classes.Stages;
import info.maxmol.generals.classes.Vec2D;

import static info.maxmol.generals.Drawing.GameDraw.cp;

// Maybe that was the hardest entity to code in the whole game.
public class Enemy extends Entity {
    private int health = 20;
    private int maxHealth = 20;
    private int collisionRadius;
    private float angle = 90;
    protected double speed;
    private BulletGenerator bulletGenerator;
    private int healthBarCountDown;
    public byte coinsCount = 2;
    public float explosion = 1f;
    private SoundPool soundPool;
    private int shotSound;
    private long soundCoolDown = 0;

    private void preSpawn() {
        speed = cp(2.5);
        soundPool = new SoundPool(5, AudioManager.STREAM_MUSIC, 0);
        shotSound = soundPool.load(GameDraw.context.getContext(), R.raw.shotdown, 1);
    }

    public void initBitmap(Integer res) {
        bitmap = BitmapFactory.decodeResource(GameDraw.context.getResources(), res == null ? R.drawable.enemy2 : res);

        if (res != null) {
            switch (res) {
                case R.drawable.enemy: {
                    bitmap = Bitmap.createScaledBitmap(bitmap, (int) cp(228), (int) cp(228), false);
                    setPointsMesh(new Vec2D[]{
                            new Vec2D(cp(73), cp(-46)),
                            new Vec2D(cp(-73), cp(-46)),
                            new Vec2D(cp(-73), cp(5)),
                            new Vec2D(cp(-20), cp(5)),
                            new Vec2D(cp(-20), cp(70)),
                            new Vec2D(cp(20), cp(70)),
                            new Vec2D(cp(20), cp(5)),
                            new Vec2D(cp(73), cp(5)),
                    });
                    collisionRadius = (int) cp(80); // 50
                    break;
                }
                case R.drawable.enemy2: {
                    bitmap = Bitmap.createScaledBitmap(bitmap, (int) cp(96), (int) cp(96), false);
                    setPointsMesh(new Vec2D[]{
                            new Vec2D(cp(50), cp(-50)),
                            new Vec2D(cp(-50), cp(-50)),
                            new Vec2D(cp(-50), cp(50)),
                            new Vec2D(cp(50), cp(50)),
                    });
                    collisionRadius = (int) cp(36);
                    break;
                }
                case R.drawable.enemy3: {
                    bitmap = Bitmap.createScaledBitmap(bitmap, (int) cp(256), (int) cp(256), false);
                    setPointsMesh(new Vec2D[]{
                            new Vec2D(cp(86), cp(-58)),
                            new Vec2D(cp(-86), cp(-58)),
                            new Vec2D(cp(-86), cp(-10)),
                            new Vec2D(cp(-30), cp(-10)),
                            new Vec2D(cp(-16), cp(86)),
                            new Vec2D(cp(16), cp(86)),
                            new Vec2D(cp(30), cp(-10)),
                            new Vec2D(cp(30), cp(-10)),
                            new Vec2D(cp(86), cp(-10)),
                    });
                    collisionRadius = (int) cp(120); // 50
                    break;
                }
                case R.drawable.enemy4: {
                    bitmap = Bitmap.createScaledBitmap(bitmap, (int) cp(200), (int) cp(200), false);
                    setPointsMesh(new Vec2D[]{
                            new Vec2D(cp(-85), cp(-70)),
                            new Vec2D(cp(85), cp(-70)),
                            new Vec2D(cp(85), cp(25)),
                            new Vec2D(cp(-85), cp(25)),
                    });
                    collisionRadius = (int) cp(58);
                    break;
                }
                case R.drawable.enemy5: {
                    bitmap = Bitmap.createScaledBitmap(bitmap, (int) cp(200), (int) cp(200), false);
                    setPointsMesh(new Vec2D[]{
                            new Vec2D(cp(-85), cp(-65)),
                            new Vec2D(cp(85), cp(-65)),
                            new Vec2D(cp(85), cp(65)),
                            new Vec2D(cp(-85), cp(65)),
                    });
                    collisionRadius = (int) cp(100);
                    break;
                }
                case R.drawable.enemy6: {
                    bitmap = Bitmap.createScaledBitmap(bitmap, (int) cp(200), (int) cp(200), false);
                    setPointsMesh(new Vec2D[]{
                            new Vec2D(cp(-65), cp(-65)),
                            new Vec2D(cp(65), cp(-65)),
                            new Vec2D(cp(65), cp(65)),
                            new Vec2D(cp(-65), cp(65)),
                    });
                    collisionRadius = (int) cp(100);
                    break;
                }
                case R.drawable.enemyboss: {
                    bitmap = Bitmap.createScaledBitmap(bitmap, (int) cp(400), (int) cp(400), false);
                    setPointsMesh(new Vec2D[]{
                            new Vec2D(cp(-200), cp(-190)),
                            new Vec2D(cp(200), cp(-190)),
                            new Vec2D(cp(100), cp(-50)),
                            new Vec2D(cp(100), cp(110)),
                            new Vec2D(cp(0), cp(200)),
                            new Vec2D(cp(-100), cp(110)),
                            new Vec2D(cp(-100), cp(-50)),
                    });
                    collisionRadius = (int) cp(400);
                    break;
                }
            }
        }
        else {
            bitmap = Bitmap.createScaledBitmap(bitmap, (int) cp(96), (int) cp(96), false);
            setPointsMesh(new Vec2D[]{
                    new Vec2D(cp(50), cp(-50)),
                    new Vec2D(cp(-50), cp(-50)),
                    new Vec2D(cp(-50), cp(50)),
                    new Vec2D(cp(-50), cp(50))
            });
            collisionRadius = (int) cp(72);
        }
    }

    public Enemy() {
        preSpawn();

        setPos(new Vec2D(Math.random() * GameDraw.context.ScrW, cp(-20)));
        setBulletGenerator(Stages.getBulletGenerator(1));

        initBitmap(null);
    }

    public Enemy(Vec2D pos, int health, BulletGenerator bg) {
        preSpawn();

        setPos(pos);
        setBulletGenerator(bg);
        setMaxHealth(health);
        setHealth(health);

        initBitmap(null);
    }

    public Enemy(Vec2D pos, int health, BulletGenerator bg, Integer res) {
        preSpawn();

        setPos(pos);
        setBulletGenerator(bg);
        setMaxHealth(health);
        setHealth(health);

        initBitmap(res);
    }

    public Enemy(float widthPercent, int health, BulletGenerator bg) {
        preSpawn();

        setPos(new Vec2D(GameDraw.context.ScrW * widthPercent/100, cp(-40)));
        setBulletGenerator(bg);
        setMaxHealth(health);
        setHealth(health);

        initBitmap(null);
    }

    public Enemy(float widthPercent, int health, BulletGenerator bg, Integer res) {
        preSpawn();

        setPos(new Vec2D(GameDraw.context.ScrW * widthPercent/100, cp(-40)));
        setBulletGenerator(bg);
        setMaxHealth(health);
        setHealth(health);

        initBitmap(res);
    }

    public void setBulletGenerator(BulletGenerator bg) {
        if (bg == null) return;

        bulletGenerator = bg;
        bulletGenerator.setOwner(this);
    }

    protected void enemyMove() {
        Move(new Vec2D(0, speed));
    }

    @Override
    public void Tick() {
        enemyMove();

        if (getPos().y > GameDraw.context.ScrH + cp(50)) {
            Remove();
        }

        if (bulletGenerator != null) bulletGenerator.update();
    }

    @Override
    public void Draw(Canvas canvas) {
        float posX = (float) getPos().x, posY = (float) (getPos().y);

        canvas.save();
        canvas.rotate(angle - 90, posX, posY);

        Paint p = new Paint();
        p.setAntiAlias(true);
        canvas.drawBitmap(bitmap, posX - bitmap.getWidth()/2, posY - bitmap.getHeight()/2, p);

        if (healthBarCountDown > 0) {
            float width = cp(80), height = cp(6);

            p.setColor(Color.argb((int) (128 * MUtil.Clamp(healthBarCountDown / 32f, 0f, 1f)), 255, 255, 255));
            canvas.drawRect(posX - width, posY - height, posX + width, posY + height, p);

            p.setColor(Color.argb((int) (255 * MUtil.Clamp(healthBarCountDown / 32f, 0f, 1f)), 0, 255, 0));
            canvas.drawRect(posX - width, posY - height, posX - width + ((float) getHealth() / getMaxHealth() * width * 2), posY + height, p);
            healthBarCountDown--;
        }

        canvas.restore();

        //Paint p2 = new Paint();
        //p2.setColor(Color.argb(196, 255, 0, 0));
        //drawByPoints(canvas, p2);
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
        GameDraw.context.AddEntity(new SparksEffect(new Vec2D(getPos().x, getPos().y), (int) (Math.random() * 5) + 12, 1, 0, 8 * explosion, 0.8, Color.rgb(196, 0, 0)));
        GameDraw.context.AddEntity(new ExplosionEffect(new Vec2D(getPos().x, getPos().y), 1.5 * explosion, 0));

        if (Game.getStep() == Game.getStage()) {
            for (int i = 0; i < coinsCount; i++)
                GameDraw.context.AddEntity(new Coin(10, getPos()));
        }
        Remove();
    }

    public void takeDamage(int damage) {
        setHealth(getHealth() - MUtil.Clamp(damage, 0));

        if (getHealth() <= 0) {
            kill();
        }

        healthBarCountDown = 128;

        if (soundCoolDown < System.currentTimeMillis()) {
            soundPool.play(shotSound, 0.25f, 0.25f, 0, 0, 1.5f);
            soundCoolDown = System.currentTimeMillis() + 100;
        }
    }

    @Override
    public int getCollisionRadius() {
        return collisionRadius;
    }

    public int getMaxHealth() {
        return maxHealth;
    }

    public void setMaxHealth(int health) {
        this.maxHealth = MUtil.Clamp(health, 0);
    }

    @Override
    public float getAngle() {
        return angle;
    }

    public void setAngle(float angle) {
        this.angle = angle;
    }

    public double getSpeed() {
        return speed;
    }

    public void setSpeed(double speed) {
        this.speed = speed;
    }

    public boolean isAlive() {
        return getHealth() > 0;
    }
}
