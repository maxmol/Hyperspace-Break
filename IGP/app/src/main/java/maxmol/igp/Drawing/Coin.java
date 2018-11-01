package maxmol.igp.Drawing;

import android.graphics.Color;
import android.media.AudioManager;
import android.media.SoundPool;
import maxmol.igp.R;
import maxmol.igp.classes.Stages;
import maxmol.igp.classes.Vec2D;

/**
 * Money spawned after killing the enemy
 */
public class Coin extends Pickable {
    public int count = 1;
    private SoundPool soundPool;
    private int coinSound;

    /**
     * customize the Pickable defaults
     */
    public void initVars() {
        pickableText = "¤";
        pickableColor = Color.rgb(227, 48, 200);;
    }

    public Coin(int count, Vec2D vec2D) {
        super(vec2D);
        setCount(count);

        soundPool = new SoundPool(5, AudioManager.STREAM_MUSIC, 0);
        coinSound = soundPool.load(GameDraw.context.getContext(), R.raw.blip1, 1);
        initVars();
    }

    public Coin() {
        super();
        initVars();
    }

    /**
     * we picked up the coin
     */
    public void collect() {
        if (this.getCount() != -1) {
            Stages.collectMoney(this.getCount());
            soundPool.play(coinSound, 0.5f, 0.5f, 0, 0, 0.8f);
            super.collect();
        }
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
}
