package maxmol.igp.Classes;

import android.content.DialogInterface;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.v7.app.AlertDialog;
import java.util.Random;
import maxmol.igp.Drawing.Bullet;
import maxmol.igp.Drawing.DrawThread;
import maxmol.igp.Drawing.Enemy;
import maxmol.igp.Drawing.Entity;
import maxmol.igp.Drawing.ExplosionEffect;
import maxmol.igp.Drawing.GameDraw;
import maxmol.igp.Drawing.LaserBeam;
import maxmol.igp.Drawing.PathEnemy;
import maxmol.igp.Drawing.SparksEffect;
import maxmol.igp.FlightActivity;
import maxmol.igp.R;

import static maxmol.igp.Drawing.GameDraw.cp;


/**
 * Stages logic. Making a stage is rather simple with this.
 */
public class Stages {
    private static int money;

    public static final int COUNT = 5;

    /**
     * overrided extended bullet class for an enemy
     */
    private static class Stage2BossBullet extends Bullet {
        @Override
        public void draw(Canvas canvas) {
            Paint p = new Paint();
            p.setColor(Color.argb(128, 64, 111, 230));
            canvas.drawCircle((float) getPos().x, (float) getPos().y, cp(25f), p);
            canvas.drawCircle((float) getPos().x, (float) getPos().y, cp(20f), p);
        }

        public Stage2BossBullet(Vec2D vec2D, Vec2D vel, double accel, int damage, double curving, Entity owner) {
            super(vec2D, vel, accel, damage, curving, owner);
        }

        @Override
        public void tick() {
            super.tick();

            if (getPos().x > GameDraw.context.scrW * 0.95 || getPos().x < GameDraw.context.scrW * 0.05 || getPos().y < GameDraw.context.scrH * 0.05 || getPos().y > GameDraw.context.scrH * 0.95) {
                BulletGenerator bulletGenerator = new BulletGenerator(8, 360.0, 10.0, 0.0, null, 0, null, null, null, null, null, null, null, null, null);
                bulletGenerator.setOwner(this);
                bulletGenerator.update();
                remove();
            }
        }
    }

    /**
     * overrided extended bullet generator class for an enemy
     */
    private static class Stage2BossBulletGenerator extends BulletGenerator {
        @Override
        public Bullet constructBullet(Vec2D bulletPos, Vec2D vel) {
            return new Stages.Stage2BossBullet(bulletPos, vel, bulletAcceleration, bulletDamage, bulletCurve, owner);
        }

        public Stage2BossBulletGenerator() {
            super();
        }
        public Stage2BossBulletGenerator(Integer bulletsPerShoot, Double angleSpread, Double bulletSpeed, Double bulletAcceleration, Double bulletCurve, Integer bulletRate, Integer bulletDamage, Integer arraysCount, Double arraysSpread, Double spinSpeed, Double currentSpin, Double spinAcceleration, Double spinMaxSpeed, Integer roundBullets, Double roundReload) {
            super(bulletsPerShoot, angleSpread, bulletSpeed, bulletAcceleration, bulletCurve, bulletRate, bulletDamage, arraysCount, arraysSpread, spinSpeed, currentSpin, spinAcceleration, spinMaxSpeed, roundBullets, roundReload);
        }
    }

    /**
     * empty bullet generator to be changed after creating
     */
    private static class FreeMode3BulletGenerator extends Stage2BossBulletGenerator {
        public FreeMode3BulletGenerator() {
            super(null, null, null, null, null, 50, 5, null, null, null, null, null, null, null, null);
            this.inaccuracy = 0.2;
        }
    }

    /**
     * Enemy spawning thread
     */
    public static class StageScript extends Thread {
        private int stage;
        public boolean running = true;

        /**
         * @param stage: which stage are we in
         */
        public StageScript(int stage) {
            this.stage = stage;
        }

        /**
         * stage script and ending here
         */
        @Override
        public void run() {
            switch (stage) {
                case 0: {
                    // - TUTORIAL - //
                    FlightActivity.playMusic(R.raw.stage_music_1);

                    GameDraw.context.ava.say("In 2521 aliens\ndestroyed our\nplanet.");
                    while (GameDraw.context.ava.isTalking()) {
                        if (wait(50)) return;
                    }

                    GameDraw.context.ava.say("I was the \nonly survivor.");
                    while (GameDraw.context.ava.isTalking()) {
                        if (wait(50)) return;
                    }

                    GameDraw.context.ava.say("For so long\nI was preparing\nfor revenge..");
                    while (GameDraw.context.ava.isTalking()) {
                        if (wait(50)) return;
                    }

                    GameDraw.context.ava.say("On the control panel\nyou can start laser\nattack or pause.");
                    while (GameDraw.context.ava.isTalking()) {
                        if (wait(100)) return;
                    }

                    GameDraw.context.ava.say("Touch the screen and\nuse the joystick\nto move.");
                    while (GameDraw.context.ava.isTalking()) {
                        if (wait(100)) return;
                    }

                    GameDraw.context.ava.say("Kill aliens\nand don't let them\ndamage the ship.");
                    while (GameDraw.context.ava.isTalking()) {
                        if (wait(100)) return;
                    }

                    PathEnemy pe;
                    pe = new PathEnemy(50, 20, getBulletGenerator(1), R.drawable.enemy, new Vec2D[]{new Vec2D(50, 25)}, true);
                    pe.coinsCount = 10;
                    GameDraw.context.AddEntity(pe);

                    while (pe.getHealth() > 0) {
                        if (wait(100)) return;
                    }

                    GameDraw.context.ava.say("Nice job!\nDon't forget to \ncollect coins.");

                    while (GameDraw.context.ava.isTalking()) {
                        if (wait(100)) return;
                    }

                    break;
                }
                case 1: {
                    // - STAGE I - //
                    FlightActivity.playMusic(R.raw.stage_music_1);

                    GameDraw.context.ava.say("THEY destroy.\nTHEY kill.\nNow they will pay.");

                    if (wait(100)) return;
                    GameDraw.context.AddEntity(new Enemy(50, 20, getBulletGenerator(0)));

                    if (wait(250)) return;
                    GameDraw.context.AddEntity(new Enemy(20, 20, getBulletGenerator(0)));
                    GameDraw.context.AddEntity(new Enemy(80, 20, getBulletGenerator(0)));

                    if (wait(350)) return;
                    GameDraw.context.AddEntity(new Enemy(30, 20, getBulletGenerator(1), R.drawable.enemy));
                    GameDraw.context.AddEntity(new Enemy(50, 20, getBulletGenerator(1), R.drawable.enemy));
                    GameDraw.context.AddEntity(new Enemy(70, 20, getBulletGenerator(1), R.drawable.enemy));

                    if (wait(500)) return;

                    GameDraw.context.AddEntity(new PathEnemy(10, 999, getBulletGenerator(2), R.drawable.enemy, new Vec2D[] {new Vec2D(10, 25)}, true));
                    GameDraw.context.AddEntity(new PathEnemy(90, 999, getBulletGenerator(2), R.drawable.enemy, new Vec2D[] {new Vec2D(90, 25)}, true));

                    if (wait(100)) return;

                    GameDraw.context.AddEntity(new PathEnemy(20, 999, getBulletGenerator(2), R.drawable.enemy, new Vec2D[] {new Vec2D(20, 15)}, true));
                    GameDraw.context.AddEntity(new PathEnemy(80, 999, getBulletGenerator(2), R.drawable.enemy, new Vec2D[] {new Vec2D(80, 15)}, true));

                    if (wait(200)) return;

                    GameDraw.context.ava.say("I'd better keep away \nfrom that laser.");

                    PathEnemy laserEnemy = new PathEnemy(50, 150, null, R.drawable.enemy3, new Vec2D[] {new Vec2D(50, 10)}, true);
                    laserEnemy.coinsCount = 4;
                    laserEnemy.explosion = 2;
                    GameDraw.context.AddEntity(laserEnemy);

                    if (wait(100)) return;

                    while (laserEnemy.getHealth() > 0) {
                        if (wait(100)) return;

                        laserEnemy.addPathPos(new Vec2D(75, 10), true);

                        LaserBeam laserBeam = new LaserBeam(100, 1, 20);
                        laserBeam.setOwner(laserEnemy);
                        GameDraw.context.AddEntity(laserBeam);

                        if (wait(100)) return;

                        laserEnemy.addPathPos(new Vec2D(25, 10), true);
                    }

                    GameDraw.context.ava.say("Boom!");
                    for (Entity e: GameDraw.context.getEntities()) {
                        if (e instanceof Enemy) {
                            ((Enemy) e).kill();
                        }
                    }

                    if (wait(400)) return;

                    break;
                }
                case 2: {
                    // - STAGE II - //
                    FlightActivity.playMusic(R.raw.stage_music_2);

                    GameDraw.context.ava.say("I will show them!");

                    PathEnemy pe;

                    if (wait(150)) return;
                    GameDraw.context.AddEntity(new Enemy(10, 35, getBulletGenerator(3)));
                    GameDraw.context.AddEntity(new Enemy(90, 35, getBulletGenerator(3)));

                    if (wait(500)) return;
                    GameDraw.context.AddEntity(new Enemy(25, 20, getBulletGenerator(5), R.drawable.enemy));
                    GameDraw.context.AddEntity(new Enemy(50, 20, getBulletGenerator(5), R.drawable.enemy));
                    GameDraw.context.AddEntity(new Enemy(75, 20, getBulletGenerator(5), R.drawable.enemy));

                    if (wait(600)) return;

                    GameDraw.context.ava.say("I don't like this\nat all!");

                    pe = new PathEnemy(50, 80, getBulletGenerator(6), R.drawable.enemy, new Vec2D[]{new Vec2D(50, 25)}, true);
                    pe.coinsCount = 10;
                    pe.explosion = 2f;
                    GameDraw.context.AddEntity(pe);

                    Enemy laserEnemy = new Enemy(10, 60, null, R.drawable.enemy3);
                    GameDraw.context.AddEntity(laserEnemy);
                    laserEnemy.explosion = 1.5f;
                    Enemy laserEnemy2 = new Enemy(90, 60, null, R.drawable.enemy3);
                    GameDraw.context.AddEntity(laserEnemy2);
                    laserEnemy2.explosion = 1.5f;

                    if (wait(75)) return;

                    LaserBeam laserBeam = new LaserBeam(1000, 2, 20);
                    laserBeam.setOwner(laserEnemy);
                    GameDraw.context.AddEntity(laserBeam);

                    LaserBeam laserBeam2 = new LaserBeam(1000, 2, 20);
                    laserBeam2.setOwner(laserEnemy2);
                    GameDraw.context.AddEntity(laserBeam2);

                    if (wait(150)) return;

                    while (laserEnemy.getAngle() > 52) {
                        if (wait(2)) return;

                        laserBeam.angle -= 1;
                        laserBeam2.angle += 1;

                        laserEnemy.setAngle(laserEnemy.getAngle() - 1);
                        laserEnemy2.setAngle(laserEnemy2.getAngle() + 1);
                    }

                    while (pe.getHealth() > 0 ) {
                        if (wait(10)) return;
                    }

                    if (wait(100)) return;

                    GameDraw.context.ava.say("This is something\nnew...");

                    for (int i = 0; i < 2; i++) {
                        Stage2BossBulletGenerator bulletGenerator = new Stage2BossBulletGenerator();
                        bulletGenerator.bulletSpeed = 10;
                        bulletGenerator.bulletAcceleration = -0.01;
                        bulletGenerator.bulletDamage = 5;
                        bulletGenerator.currentSpin = i == 0 ? -90 : 90;
                        bulletGenerator.bulletRate = 24;
                        bulletGenerator.bulletCountDown = i == 0 ? 12 : 0;

                        Enemy startboss = new Enemy(i == 0 ? 10 : 90, 10000, bulletGenerator, R.drawable.enemy4);
                        startboss.setAngle(i == 0 ? 0 : -180);
                        startboss.setSpeed(5);
                        GameDraw.context.AddEntity(startboss);
                    }

                    if (wait(600)) return;

                    break;
                }
                case 3: {
                    // - STAGE III - //
                    FlightActivity.playMusic(R.raw.stage_music_2);

                    PathEnemy pe;

                    if (wait(150)) return;

                    GameDraw.context.ava.say("Uh...\nit's them again!");

                    PathEnemy[] twins = new PathEnemy[2];

                    for (int i = 0; i < 2; i++) {
                        Stage2BossBulletGenerator bulletGenerator = new Stage2BossBulletGenerator();
                        bulletGenerator.bulletSpeed = 10;
                        bulletGenerator.bulletDamage = 5;
                        bulletGenerator.currentSpin = i == 0 ? -90 : 90;
                        bulletGenerator.bulletRate = 30;
                        bulletGenerator.inaccuracy = 0.3;
                        bulletGenerator.bulletCountDown = i == 0 ? 12 : 0;

                        PathEnemy boss = new PathEnemy(0, 80, bulletGenerator, R.drawable.enemy4, new Vec2D[]{new Vec2D(i == 0 ? 10 : 90, 40)}, true);
                        boss.setPos(new Vec2D(GameDraw.context.scrW * (i == 0 ? 0.1 : 0.9), GameDraw.context.scrH + cp(5)));
                        boss.setAngle(i == 0 ? 0 : -180);
                        boss.setSpeed(5);
                        boss.coinsCount = 8;
                        GameDraw.context.AddEntity(boss);

                        twins[i] = boss;
                    }

                    while (true) {
                        if (wait(10)) return;

                        if (twins[0].getHealth() <= 0 && twins[1].getHealth() <= 0) break;
                    }

                    if (wait(150)) return;

                    Stage2BossBulletGenerator bulletGenerator = new Stage2BossBulletGenerator();
                    bulletGenerator.bulletSpeed = 10;
                    bulletGenerator.bulletDamage = 5;
                    bulletGenerator.bulletRate = 30;
                    bulletGenerator.bulletsPerShoot = 2;
                    bulletGenerator.angleSpread = 45;

                    PathEnemy e1 = new PathEnemy(50, 100, bulletGenerator, R.drawable.enemy4, new Vec2D[]{new Vec2D(50, 20)}, true);
                    e1.coinsCount = 6;
                    GameDraw.context.AddEntity(e1);

                    PathEnemy e2 = new PathEnemy(15, 50, null, R.drawable.enemy3, new Vec2D[]{new Vec2D(15, 80)}, true);
                    e2.setAngle(25);
                    e2.coinsCount = 3;
                    GameDraw.context.AddEntity(e2);

                    PathEnemy e3 = new PathEnemy(85, 50, null, R.drawable.enemy3, new Vec2D[]{new Vec2D(85, 80)}, true);
                    e3.setAngle(155);
                    e3.coinsCount = 3;
                    GameDraw.context.AddEntity(e3);

                    GameDraw.context.ava.say("WOAH!");

                    while (e1.isAlive() || e2.isAlive() || e3.isAlive()) {
                        if (e2.isAlive()) e2.addPathPos(new Vec2D(15, 25), true);
                        if (e3.isAlive()) e3.addPathPos(new Vec2D(85, 25), true);
                        if (wait(100)) return;

                        LaserBeam lb1 = new LaserBeam(150, 1, 20);
                        lb1.setOwner(e2);
                        lb1.angle = 25;
                        GameDraw.context.AddEntity(lb1);

                        LaserBeam lb2 = new LaserBeam(150, 1, 20);
                        lb2.setOwner(e3);
                        lb2.angle = 155;
                        GameDraw.context.AddEntity(lb2);

                        if (wait(100)) return;
                        if (e2.isAlive()) e2.addPathPos(new Vec2D(15, 95), true);
                        if (e3.isAlive()) e3.addPathPos(new Vec2D(85, 95), true);
                    }

                    GameDraw.context.ava.say("I'm almost there.\nApproaching the CORE.");
                    if (wait(200)) return;
                    break;
                }
                case 4: {
                    // STAGE IV

                    FlightActivity.playMusic(R.raw.stage_music_1);
                    GameDraw.context.ava.say("They are waiting\nfor me...");

                    Enemy e1 = new Enemy(30, 20, getBulletGenerator(7), R.drawable.enemy2);
                    e1.coinsCount = 4;
                    GameDraw.context.AddEntity(e1);
                    Enemy e2 = new Enemy(70, 20, getBulletGenerator(7), R.drawable.enemy2);
                    e2.coinsCount = 4;
                    GameDraw.context.AddEntity(e2);

                    if (wait(100)) return;
                    Enemy e3 = new Enemy(50, 30, getBulletGenerator(6), R.drawable.enemy2);
                    e3.coinsCount = 4;
                    GameDraw.context.AddEntity(e3);

                    while (true) {
                        if (wait(10)) return;

                        if (!e1.isValid() && !e2.isValid() && !e3.isValid()) break;
                    }

                    if (wait(100)) return;

                    for (int i = 1; i <= 5; i++) {
                        GameDraw.context.AddEntity(new Enemy(i * 10, 20, getBulletGenerator(0), R.drawable.enemy));
                        if (i != 5)
                            GameDraw.context.AddEntity(new Enemy(100 - i * 10, 10, getBulletGenerator(0), R.drawable.enemy));

                        if (wait(70)) return;
                    }

                    if (wait(500)) return;

                    Stage2BossBulletGenerator bulletGenerator = new Stage2BossBulletGenerator();
                    bulletGenerator.bulletSpeed = 10;
                    bulletGenerator.bulletDamage = 5;
                    bulletGenerator.bulletRate = 100;
                    bulletGenerator.bulletsPerShoot = 2;
                    bulletGenerator.inaccuracy = 0.025;
                    bulletGenerator.angleSpread = 60;
                    bulletGenerator.currentSpin = 0;

                    PathEnemy bigBalls = new PathEnemy(50, 120, bulletGenerator, R.drawable.enemy5, new Vec2D[]{new Vec2D(50, 15)}, true);
                    bigBalls.coinsCount = 8;
                    GameDraw.context.AddEntity(bigBalls);

                    LaserBeam laserBeam = new LaserBeam(10000, 10, 20);
                    laserBeam.setOwner(bigBalls);
                    GameDraw.context.AddEntity(laserBeam);

                    boolean left = true;
                    while (bigBalls.isAlive()) {
                        if (left) {
                            laserBeam.angle++;
                            bigBalls.setAngle(bigBalls.getAngle() + 1);
                        } else {
                            laserBeam.angle--;
                            bigBalls.setAngle(bigBalls.getAngle() - 1);
                        }

                        if (Math.abs(laserBeam.angle - 90) >= 20) {
                            left = !left;
                        }

                        if (wait(4)) return;
                    }

                    if (wait(200)) return;

                    break;
                }
                case 5: {
                    // STAGE V - FINAL
                    FlightActivity.playMusic(R.raw.boss_theme);
                    GameDraw.context.ava.say("This is the end\nof my journey\nof my revenge...");

                    BulletGenerator bossGenerator = new BulletGenerator(4, 50.0, 10.0, -0.02, null, 5, 2, 2, 180.0, -21.0, null, 1.5, 30.0, null, null);

                    PathEnemy boss = new PathEnemy(50, 2000, bossGenerator, R.drawable.enemyboss, new Vec2D[]{new Vec2D(50, 30)}, true);
                    boss.coinsCount = 25;
                    GameDraw.context.AddEntity(boss);

                    while (boss.getHealth() > boss.getMaxHealth() * 0.66) {
                        if (wait(4)) return;
                    }

                    GameDraw.context.ava.say("Gotcha.");

                    GameDraw.context.AddEntity(new ExplosionEffect(boss.getPos(), 2.0, 0));

                    bossGenerator.bulletRate = 4;

                    boss.setBulletGenerator(null);

                    if (wait(200)) return;

                    boss.setBulletGenerator(bossGenerator);

                    LaserBeam laserBeam = null;
                    LaserBeam laserBeam2 = null;

                    int ang1 = 90;
                    int ang2 = 90;
                    boolean reverse = false;

                    int laserInterval = 50;
                    while (boss.getHealth() > boss.getMaxHealth() * 0.33) {
                        if (laserInterval >= 20) {
                            laserBeam = new LaserBeam(60, 1, 20);
                            laserBeam.setOwner(boss);
                            GameDraw.context.AddEntity(laserBeam);

                            laserBeam2 = new LaserBeam(60, 1, 20);
                            laserBeam2.setOwner(boss);
                            GameDraw.context.AddEntity(laserBeam2);

                            laserInterval = 0;
                        }
                        else {
                            laserInterval++;
                        }

                        if (Math.abs(ang1 - 90) > 40) reverse = !reverse;

                        if (laserBeam != null) {
                            ang1 += reverse ? -1 : 1;
                            ang2 -= reverse ? -1 : 1;

                            laserBeam.angle = ang1;
                            laserBeam2.angle = ang2;
                        }

                        if (wait(4)) return;
                    }

                    GameDraw.context.AddEntity(new ExplosionEffect(boss.getPos(), 2.0, 0));
                    GameDraw.context.ava.say("You can not \ndefeat me!");

                    int spawnInterval = 90;

                    while (boss.isAlive()) {
                        if (laserInterval >= 20) {
                            laserBeam = new LaserBeam(60, 2, 20);
                            laserBeam.setOwner(boss);
                            GameDraw.context.AddEntity(laserBeam);

                            laserBeam2 = new LaserBeam(60, 2, 20);
                            laserBeam2.setOwner(boss);
                            GameDraw.context.AddEntity(laserBeam2);

                            laserInterval = 0;
                        }
                        else {
                            laserInterval++;
                        }

                        if (Math.abs(ang1 - 90) > 40) reverse = !reverse;

                        if (laserBeam != null) {
                            ang1 += reverse ? -1 : 1;
                            ang2 -= reverse ? -1 : 1;

                            laserBeam.angle = ang1;
                            laserBeam2.angle = ang2;
                        }

                        if (spawnInterval >= 100) {
                            Enemy enemy = new Enemy(20, 20, new Stage2BossBulletGenerator(null, null, null, null, null, 75, null, null, null, null, null, null, null, null, null), R.drawable.enemy4);
                            enemy.setSpeed(4.00);
                            GameDraw.context.AddEntity(enemy);

                            Enemy enemy2 = new Enemy(80, 20, new Stage2BossBulletGenerator(null, null, null, null, null, 75, null, null, null, null, null, null, null, null, null), R.drawable.enemy4);
                            enemy2.setSpeed(4.00);
                            GameDraw.context.AddEntity(enemy2);
                            spawnInterval = 0;
                        }
                        spawnInterval++;

                        if (wait(4)) return;
                    }

                    GameDraw.context.ava.say("I did it!\nI destroyed them!\nIt's over!");

                    if (wait(350)) return;

                    break;
                }
                case COUNT + 1: {
                    // -= FREEMODE =- //

                    // save our campaign stats
                    String[] stats = Game.getTable();

                    // Reset our stats
                    Game.reset();

                    GameDraw.context.ship.initBulletGenerators();

                    FlightActivity.playMusic(R.raw.stage_music_1);

                    Random random = new Random();

                    int[] enemyTypes = new int[] {
                            R.drawable.enemy,
                            R.drawable.enemy2,
                            R.drawable.enemy4,
                    };

                    GameDraw.context.ava.say("I have to destroy\nthem!");

                    int counter = 0;
                    int upgrade = 1;

                    while (true) {
                        if (Stages.getMoney() > 100 * upgrade) {
                            if (upgrade <= 5) {
                                Game.attackLevel++;
                                Game.maxHealthLevel++;
                                Game.bombLevel++;
                                Game.critLevel++;
                                GameDraw.context.ava.say("Levelled up!\nHealth restored\n+1 Laser Attack");
                                upgrade = (int) Math.round(upgrade * 1.5);
                            }
                            else {
                                GameDraw.context.ava.say("Health restored\n+1 Laser Attack");
                                upgrade += 2;
                            }

                            Game.laserAttackCount++;
                            Game.setHealth(Game.getMaxHealth());
                            GameDraw.context.ship.initBulletGenerators();
                            GameDraw.context.AddEntity(new SparksEffect(new Vec2D(GameDraw.context.ship.getPos().x, GameDraw.context.ship.getPos().y), 25, 1, 0, 8, 0.8, Color.rgb(149, 75, 255)));
                        }

                        int spawnType = random.nextInt(3);
                        int primaryType = random.nextInt(3);
                        int secondaryType = random.nextInt(3);

                        int primaryTypeImg = enemyTypes[primaryType];
                        int secondaryTypeImg = enemyTypes[secondaryType];

                        int primaryBulletGenerator = 1;
                        if (primaryType == 0) {
                            primaryBulletGenerator = random.nextInt(3);
                        }
                        else if (primaryType == 1) {
                            primaryBulletGenerator = 3 + random.nextInt(3);
                        }
                        else if (primaryType == 2) {
                            primaryBulletGenerator = -1;
                        }

                        int secondaryBulletGenerator = 1;
                        if (secondaryType == 0) {
                            secondaryBulletGenerator = random.nextInt(3);
                        }
                        else if (secondaryType == 1) {
                            secondaryBulletGenerator = 3 + random.nextInt(5);
                        }
                        else if (secondaryType == 2) {
                            secondaryBulletGenerator = -1;
                        }

                        int health = 20 + counter * 5;
                        switch (spawnType) {
                            case 0:
                                GameDraw.context.AddEntity(new Enemy(10 + random.nextInt(80), health, primaryBulletGenerator == -1 ? new FreeMode3BulletGenerator() : getBulletGenerator(primaryBulletGenerator), primaryTypeImg));
                                break;
                            case 1:
                                GameDraw.context.AddEntity(new Enemy(15, health, primaryBulletGenerator == -1 ? new FreeMode3BulletGenerator() : getBulletGenerator(primaryBulletGenerator), primaryTypeImg));
                                GameDraw.context.AddEntity(new Enemy(85, health, primaryBulletGenerator == -1 ? new FreeMode3BulletGenerator() : getBulletGenerator(primaryBulletGenerator), primaryTypeImg));
                                break;
                            case 2:
                                GameDraw.context.AddEntity(new Enemy(50, 20 + counter * 5, secondaryBulletGenerator == -1 ? new FreeMode3BulletGenerator() : getBulletGenerator(secondaryBulletGenerator), secondaryTypeImg));

                                GameDraw.context.AddEntity(new Enemy(15, health, primaryBulletGenerator == -1 ? new FreeMode3BulletGenerator() : getBulletGenerator(primaryBulletGenerator), primaryTypeImg));
                                GameDraw.context.AddEntity(new Enemy(85, health, primaryBulletGenerator == -1 ? new FreeMode3BulletGenerator() : getBulletGenerator(primaryBulletGenerator), primaryTypeImg));
                                break;
                        }

                        if (wait(500 - MUtil.clamp(counter * 10, 0, 300))) {
                            stats[8] = Game.highScore.toString(); // we dont want to lose the new highscore
                            Game.setTable(stats);
                            return;
                        }
                        counter++;
                    }
                }
            }


            Game.addMoney(Stages.getMoney());
            
            if (Game.getStep() == Game.getStage()) {
                Game.nextStep();
                Game.setStage(Game.getStep());
            }

            FlightActivity.context.runOnUiThread(new Runnable() {

                @Override
                public void run() {
                    GameDraw.context.drawThread.kill();

                    new AlertDialog.Builder(FlightActivity.context)
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .setTitle("Congrats!")
                            .setMessage("You have completed this stage.")
                            .setPositiveButton("Continue", new DialogInterface.OnClickListener()
                            {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    DrawThread.getout();
                                }

                            })
                            .setOnCancelListener(new DialogInterface.OnCancelListener() {
                                @Override
                                public void onCancel(DialogInterface dialogInterface) {
                                    DrawThread.getout();
                                }
                            })
                            .show();
                }
            });
        }

        /**
         * @param intervals: how much time to wait in milliseconds
         * @return
         */
        private boolean wait(int intervals) {
            GameDraw.context.drawThread.stageSleep(intervals);

            while (true) {
                if (!running) return true;

                if (GameDraw.context.stageSleeping > 0) {
                    try {
                        this.sleep(DrawThread.interval);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    continue;
                }

                break;
            }

            return false;
        }
    }

    /**
     * start the stage
     */
    public static void start() {
        money = 0;
    }

    /**
     * we picked up some coins!
     * @param m: amount
     */
    public static void collectMoney(int m) {
        money += m;

        if (isArcade() && money > Game.highScore) {
            Game.highScore = money;
        }
    }

    /**
     * @return money amount in this run
     */
    public static int getMoney() {
        return money;
    }

    /**
     * @return if we are playing arcade (freemode) or not
     */
    public static boolean isArcade() {
        return Game.getStage() == Stages.COUNT + 1;
    }

    /**
     * @param i
     * @return Default bullet generators for stages
     */
    public static BulletGenerator getBulletGenerator(int i) {
        BulletGenerator bg = new BulletGenerator();
        switch (i) {
            case 0:
                bg = new BulletGenerator(1, null, null, null, null, null, null, null, null, null, null, null, null, null, null);
                bg.shootAtPlayer = true;
                bg.inaccuracy = 0.02;
                break;
            case 1:
                bg = new BulletGenerator(2, 15.0, null, null, null, null, null, null, null, null, null, null, null, null, null);
                break;
            case 2:
                bg = new BulletGenerator(3, 10.0, null, null, null, null, null, null, null, null, null, null, null, null, null);
                break;
            case 3:
                bg = new BulletGenerator(1, null, null, 0.01, null, 2, null, null, null, 10.0, -10.0, null, null, 40, 120.0);
                break;
            case 4:
                bg = new BulletGenerator(2, 180.0, null, 0.01, null, 10, null, null, null, 15.0, null, null, null, null, null);
                break;
            case 5:
                bg = new BulletGenerator(2, 30.0, null, 0.0, null, 20, null, 2, 180.0, 15.0, null, null, null, null, null);
                break;
            case 6:
                bg = new BulletGenerator(16, 360.0, null, 0.0, null, 10, null, null, null, 4.0, null, null, null, 8, 25.0);
                break;
            case 7:
                bg = new BulletGenerator(2, 180.0, null, 0.01, null, 10, null, null, null, 0.0, null, 2.0, 20.0, null, null);
                break;
        }

        bg.bulletRate -= MUtil.clamp(Game.difficulty, 1);

        return  bg;
    }
}