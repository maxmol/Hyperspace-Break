package maxmol.igp.Drawing;

import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.media.AudioManager;
import android.media.SoundPool;

import maxmol.igp.R;
import maxmol.igp.classes.BulletGenerator;
import maxmol.igp.classes.Game;
import maxmol.igp.classes.MUtil;
import maxmol.igp.classes.Stages;
import maxmol.igp.classes.Vec2D;

import static maxmol.igp.Drawing.GameDraw.cp;

/**
 * The enemy ship entity!
 */
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
    private int imageSize = 128;

    private void preSpawn() {
        speed = cp(2.5);
        soundPool = new SoundPool(5, AudioManager.STREAM_MUSIC, 0);
        shotSound = soundPool.load(GameDraw.context.getContext(), R.raw.shotdown, 1);
    }

    public void initBitmap(Integer res) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inScaled = false;

        bitmap = BitmapFactory.decodeResource(GameDraw.context.getResources(), res == null ? R.drawable.enemy2 : res, options);

        if (res != null) {
            switch (res) {
                case R.drawable.enemy: {
                    imageSize = (int) cp(114);
                    //bitmap = Bitmap.createScaledBitmap(bitmap, (int) cp(228), (int) cp(228), false);
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
                    imageSize = (int) cp(48);
                    //bitmap = Bitmap.createScaledBitmap(bitmap, (int) cp(96), (int) cp(96), false);
                    setPointsMesh(new Vec2D[]{
                            new Vec2D(cp(50), cp(-50)),
                            new Vec2D(cp(-50), cp(-50)),
                            new Vec2D(cp(-50), cp(50)),
                            new Vec2D(cp(50), cp(50)),
                    });
                    collisionRadius = (int) cp(60);
                    break;
                }
                case R.drawable.enemy3: {
                    imageSize = (int) cp(128);
                    //bitmap = Bitmap.createScaledBitmap(bitmap, (int) cp(256), (int) cp(256), false);
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
                    imageSize = (int) cp(100);
                    //bitmap = Bitmap.createScaledBitmap(bitmap, (int) cp(200), (int) cp(200), false);
                    setPointsMesh(new Vec2D[]{
                            new Vec2D(cp(-85), cp(-70)),
                            new Vec2D(cp(85), cp(-70)),
                            new Vec2D(cp(85), cp(25)),
                            new Vec2D(cp(-85), cp(25)),
                    });
                    collisionRadius = (int) cp(90);
                    break;
                }
                case R.drawable.enemy5: {
                    imageSize = (int) cp(100);
                    //bitmap = Bitmap.createScaledBitmap(bitmap, (int) cp(200), (int) cp(200), false);
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
                    imageSize = (int) cp(100);
                    //bitmap = Bitmap.createScaledBitmap(bitmap, (int) cp(200), (int) cp(200), false);
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
                    imageSize = (int) cp(200);
                    //bitmap = Bitmap.createScaledBitmap(bitmap, (int) cp(400), (int) cp(400), false);
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
            imageSize = (int) cp(48);
            //bitmap = Bitmap.createScaledBitmap(bitmap, (int) cp(96), (int) cp(96), false);
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

        setPos(new Vec2D(Math.random() * GameDraw.context.scrW, cp(-20)));
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

        setPos(new Vec2D(GameDraw.context.scrW * widthPercent/100, cp(-40)));
        setBulletGenerator(bg);
        setMaxHealth(health);
        setHealth(health);

        initBitmap(null);
    }

    public Enemy(float widthPercent, int health, BulletGenerator bg, Integer res) {
        preSpawn();

        setPos(new Vec2D(GameDraw.context.scrW * widthPercent/100, cp(-40)));
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
        move(new Vec2D(0, speed));
    }

    @Override
    public void tick() {
        enemyMove();

        if (getPos().y > GameDraw.context.scrH + cp(50)) {
            remove();
        }

        if (bulletGenerator != null) bulletGenerator.update();
    }

    @Override
    public void draw(Canvas canvas) {
        int posX = (int) getPos().x, posY = (int) (getPos().y);

        canvas.save();
        canvas.rotate(angle - 90, posX, posY);

        Paint p = new Paint();
        p.setAntiAlias(false);
        p.setFilterBitmap(false);
        p.setDither(true);

        canvas.drawBitmap(bitmap, null, new Rect(posX - imageSize, posY - imageSize, posX + imageSize, posY + imageSize), p);

        //canvas.drawBitmap(bitmap, posX - bitmap.getWidth()/2, posY - bitmap.getHeight()/2, p);

        if (healthBarCountDown > 0) {
            float width = cp(80), height = cp(6);

            p.setColor(Color.argb((int) (128 * MUtil.clamp(healthBarCountDown / 32f, 0f, 1f)), 255, 255, 255));
            canvas.drawRect(posX - width, posY - height, posX + width, posY + height, p);

            p.setColor(Color.argb((int) (255 * MUtil.clamp(healthBarCountDown / 32f, 0f, 1f)), 0, 255, 0));
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
        this.health = MUtil.clamp(health, 0, getMaxHealth());
    }

    public int getHealth() {
        return health;
    }

    public void addHealth(int health) {
        setHealth(getHealth() + MUtil.clamp(health, 0));
    }

    public void kill() {
        GameDraw.context.AddEntity(new SparksEffect(new Vec2D(getPos().x, getPos().y), (int) (Math.random() * 5) + 12, 1, 0, 8 * explosion, 0.8, Color.rgb(196, 0, 0)));
        GameDraw.context.AddEntity(new ExplosionEffect(new Vec2D(getPos().x, getPos().y), 1.5 * explosion, 0));

        for (Entity e: GameDraw.context.getEntities()) {
            if (e instanceof Bullet) {
                Bullet b = (Bullet) e;
                if (b.owner == this) {
                    b.hit(null);
                }
            }
        }

        //if (Game.getStep() == Game.getStage()) {
            for (int i = 0; i < coinsCount; i++)
                GameDraw.context.AddEntity(new Coin(Game.getStage() * 2, getPos()));
        //}
        remove();
    }

    public void takeDamage(int damage) {
        setHealth(getHealth() - MUtil.clamp(damage, 0));

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
        this.maxHealth = MUtil.clamp(health, 0);
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
