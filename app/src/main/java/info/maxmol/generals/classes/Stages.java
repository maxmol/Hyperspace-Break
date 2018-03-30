package info.maxmol.generals.classes;

import java.util.Random;

public class Stages {
    public static int completion;
    private static int money;

    public static void start() {
        completion = stageTime();
        money = 0;
    }

    public static void collectMoney(int m) {
        money += m;
    }

    public static int getMoney() {
        return money;
    }

    public static int enemySpawnRate() {
        return 1000;
    }

    public static BulletGenerator getBulletGenerator() {
        Random r = new Random();
        int i = r.nextInt(7);

        switch (i) {
            case 1: return new BulletGenerator(4, 32.0, null, 0.01, null, 16, null, null, null, null, null, null, null, null, null);
            case 2: return new BulletGenerator(1, null, null, 0.01, null, 2, null, null, null, 10.0, null, null, null, 40, 120.0);
            case 3: return new BulletGenerator(2, 180.0, null, 0.01, null, 10, null, null, null, 15.0, null, null, null, null, null);
            case 4: return new BulletGenerator(2, 30.0, null, 0.0, null, 20, null, 2, 180.0, 15.0, null, null, null, null, null);
            case 5: return new BulletGenerator(10, 360.0, null, 0.0, null, 50, null, null, null, null, null, null, null, null, null);
            case 6: return new BulletGenerator(2, 180.0, null, 0.01, null, 10, null, null, null, 0.0, null, 2.0, 20.0, null, null);
        }

        return new BulletGenerator();
    }

    public static int stageTime() {
        return 2000;
    }
}