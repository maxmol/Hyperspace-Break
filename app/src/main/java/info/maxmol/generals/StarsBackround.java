package info.maxmol.generals;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.CountDownTimer;
import android.util.AttributeSet;
import android.view.View;

import java.util.ArrayList;

import info.maxmol.generals.Drawing.BackgroundStar;
import info.maxmol.generals.Drawing.Entity;
import info.maxmol.generals.Drawing.GameDraw;
import info.maxmol.generals.classes.Vec2D;

// Moving stars view for main menu.
public class StarsBackround extends View {
    public ArrayList<Entity> entities = new ArrayList<>();

    public StarsBackround starsBackround;

    private void initTimer() {
        CountDownTimer timer = new CountDownTimer(Integer.MAX_VALUE, 50) {
            public void onTick(long millisUntilFinished) {
                starsBackround.invalidate();
            }

            public void onFinish() {}
        };

        timer.start();
    }

    public StarsBackround(Context context) {
        super(context);

        starsBackround = this;

        initTimer();
    }

    public StarsBackround(Context context, AttributeSet attrs) {
        super(context, attrs);

        starsBackround = this;

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

        Paint p = new Paint();
        p.setColor(Color.rgb(0, 0, 64));
        canvas.drawPaint(p);

        if (Math.random() < BackgroundStar.SPAWN_CHANCE) {
            entities.add(new BackgroundStar(this));
        }

        for (Entity ent: (ArrayList<Entity>) entities.clone()) {
            ent.Tick();
        }

        for (Entity ent : entities) {
            ent.Draw(canvas);
        }
    }
}
