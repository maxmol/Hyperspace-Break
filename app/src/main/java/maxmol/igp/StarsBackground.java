package maxmol.igp;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.CountDownTimer;
import android.util.AttributeSet;
import android.view.View;

import java.util.ArrayList;

import maxmol.igp.Drawing.BackgroundStar;
import maxmol.igp.Drawing.Entity;
import maxmol.igp.Classes.Vec2D;

/**
 * A simple View we use to create moving stars in main menu.
 */
public class StarsBackground extends View {
    public ArrayList<Entity> entities = new ArrayList<>();

    public StarsBackground StarsBackground;
    private Paint p;

    /**
     * Create timer for spawning stars
     */
    private void initTimer() {
        CountDownTimer timer = new CountDownTimer(Integer.MAX_VALUE, 50) {
            public void onTick(long millisUntilFinished) {
                StarsBackground.invalidate();
            }

            public void onFinish() {}
        };

        timer.start();
    }

    private void initPaint() {
        p = new Paint();
        p.setColor(Color.rgb(21, 5, 28));
    }

    public StarsBackground(Context context) {
        super(context);

        StarsBackground = this;
        initPaint();

        initTimer();
    }

    public StarsBackground(Context context, AttributeSet attrs) {
        super(context, attrs);

        StarsBackground = this;
        initPaint();

        initTimer();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (entities.size() == 0) {
            float h = 0f;

            while (h < getHeight()) {
                h += BackgroundStar.SPAWN_CHANCE * Math.random() * 1500;
                BackgroundStar star = new BackgroundStar(this);
                star.setPos(new Vec2D(getWidth() * Math.random(), h));
                entities.add(star);
            }
        }

        canvas.drawPaint(p);

        if (Math.random() < BackgroundStar.SPAWN_CHANCE) {
            entities.add(new BackgroundStar(this));
        }

        for (Entity ent: (ArrayList<Entity>) entities.clone()) {
            ent.tick();
        }

        for (Entity ent : entities) {
            ent.draw(canvas);
        }
    }
}