package maxmol.igp.Drawing;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.AsyncTask;

import maxmol.igp.R;
import maxmol.igp.Classes.Vec2D;

import static maxmol.igp.Drawing.GameDraw.cp;

/**
 * Boom!
 */
public class ExplosionEffect extends Entity {
    private int updateRate;
    private static final int maxFrames = 48;
    private static final float magicValue = 1f;
    private int frameSize;
    private int updateCounter;
    private int frame = 0;
    private static final Bitmap mainBitmap = BitmapFactory.decodeResource(GameDraw.context.getResources(), R.drawable.explosion);
    private Bitmap bitmap;
    private static Integer explosionSound;

    public ExplosionEffect(Vec2D pos, double size, int updateRate) {
        setPos(pos);

        this.updateRate = updateRate;
        this.updateCounter = updateRate;

        frameSize = (int) (cp(256) * size);

        AsyncTask asyncTask = new AsyncTask() {
            @Override
            protected Object doInBackground(Object[] objects) {
                bitmap = Bitmap.createScaledBitmap(mainBitmap, frameSize * maxFrames, frameSize, false);
                return null;
            }
        };
        asyncTask.execute();

        SoundPool soundPool = GameDraw.context.soundPool;
        if (explosionSound == null) {
            explosionSound = soundPool.load(GameDraw.context.getContext(), R.raw.explode4, 1);
            soundPool.setOnLoadCompleteListener(new SoundPool.OnLoadCompleteListener() {
                public void onLoadComplete(SoundPool soundPool, int sampleId, int status) {
                    soundPool.play(explosionSound, 1f, 1f, 0, 0, 1.2f);
                }
            });
        }
        else {
            soundPool.play(explosionSound, 1f, 1f, 0, 0, 1.2f);
        }
    }

    @Override
    public void tick() {
        if (bitmap == null) return;

        if (--updateCounter < 0) {
            updateCounter = updateRate;
            frame++;

            if (frame > maxFrames) remove();
        }
    }

    @Override
    public void draw(Canvas canvas) {
        if (bitmap == null) return;

        int x = (int) getPos().x, y = (int) getPos().y;
        canvas.drawBitmap(bitmap,
                new Rect(
                        (int) (frame * frameSize * magicValue),
                        0,
                        (int) ((frame + 1) * frameSize * magicValue),
                        frameSize
                ), new RectF(
                        x - frameSize/2f * magicValue,
                        y - frameSize/2f,
                        x + frameSize/2f * magicValue,
                        y + frameSize/2f), new Paint()
        );
    }

    @Override
    public boolean isPhysicsObject() {
        return false;
    }

    @Override
    public int getZPos() {
        return 19;
    }
}
