package info.maxmol.generals.Drawing;

import android.content.Context;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import info.maxmol.generals.classes.Vec2D;

public class GameDraw extends SurfaceView implements SurfaceHolder.Callback {
    public static GameDraw context;

    public DrawThread drawThread;
    private Ship ship;

    public int ScrW, ScrH;

    public ArrayList<Entity> entities = new ArrayList<>();

    public void AddEntity(Entity ent) {
        entities.add(ent);

        Collections.sort(GameDraw.context.entities, new Comparator<Entity>() {
            @Override
            public int compare(Entity e1, Entity e2) {
                return e1.getZPos() - e2.getZPos();
            }
        });
    }

    public static float cp(float pixels) { // Converted Pixels
        return pixels/2.5f * context.getResources().getDisplayMetrics().density;
    }

    public static float cp(int pixels) {
        return pixels/2.5f * context.getResources().getDisplayMetrics().density;
    }

    public static double cp(double pixels) {
        return pixels/2.5 * context.getResources().getDisplayMetrics().density;
    }

    public GameDraw(Context context) {
        super(context);

        getHolder().addCallback(this);

        GameDraw.context = this;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        //for (Entity e: (ArrayList<Entity>) GameDraw.context.entities.clone()) {
        //    e.OnTouch(event);
        //}

        GameDraw.context.ship.OnTouch(event);
        return true;
    }

    @Override
    public void surfaceCreated(SurfaceHolder surfaceHolder) {

        drawThread = new DrawThread(surfaceHolder);
        drawThread.start();

        ScrW = this.getWidth();
        ScrH = this.getHeight();

        if (ship == null) {
            ship = new Ship();
            AddEntity(ship);

            float h = 0f;

            while (h < ScrH) {
                h += BackgroundStar.SPAWN_CHANCE * Math.random() * 1500;
                GameDraw.context.AddEntity(new BackgroundStar(new Vec2D(ScrW * Math.random(), h)));
            }
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder surfaceHolder, int format, int w, int h) {
        ScrW = w;
        ScrH = h;
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
        drawThread.kill();
        while (true) {
            try {
                drawThread.join();
            } catch (InterruptedException e) {

            }

            if (drawThread.isAlive() == false) break;
        }
    }
}
