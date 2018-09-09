package info.maxmol.generals.classes;

import info.maxmol.generals.Drawing.Enemy;
import info.maxmol.generals.Drawing.GameDraw;

// This class was being used earlier in development. Not used anymore.
// For FreeMode
public class EnemySpawn {
    private int time;
    private float widthPercent;
    private int bulletType;

    public EnemySpawn(int time, float widthPercent, int bulletType) {
        this.time = time;
        this.widthPercent = widthPercent;
        this.bulletType = bulletType;
    }

    public int getTime() {
        return time;
    }

    public Enemy spawn() {
        Enemy e = new Enemy(widthPercent, 20, Stages.getBulletGenerator(bulletType));
        GameDraw.context.AddEntity(e);

        return e;
    }
}
