package info.maxmol.generals.Drawing;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Region;
import android.media.AudioManager;
import android.media.SoundPool;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import info.maxmol.generals.R;
import info.maxmol.generals.classes.Game;
import info.maxmol.generals.classes.Stages;
import info.maxmol.generals.classes.Vec2D;

// Classes organization and preparation to start the game
public class GameDraw extends SurfaceView implements SurfaceHolder.Callback {
    public static GameDraw context;

    public DrawThread drawThread;
    public Ship ship;
    public boolean shouldSort = false;

    public int realW, realH;
    public int ScrW, ScrH;

    public Stages.StageScript stageScript;
    public int stageSleeping = 0;
    public static Region clipRegion;

    public boolean paused = false;

    public ArrayList<Entity> entities = new ArrayList<>();

    public void AddEntity(Entity ent) {
        entities.add(ent);

        shouldSort = true;
    }

    public ArrayList<Entity> getEntities() {
        return (ArrayList<Entity>) entities.clone();
    }

    public ArrayList<SuperVGUI> vgui = new ArrayList<>();

    public void AddVGUI(SuperVGUI o) {
        vgui.add(o);

        shouldSort = true;
    }

    public ArrayList<SuperVGUI> getVGUIObjects() {
        return (ArrayList<SuperVGUI>) vgui.clone();
    }

    synchronized public void SortEntities() {
        try {
            Collections.sort(entities, new Comparator<Entity>() {
                @Override
                public int compare(Entity e1, Entity e2) {
                    return e1.getZPos() - e2.getZPos();
                }
            });
        }
        catch (Exception e) {
            Log.e("Crash Alert", "It could crash now");
        }
    }

    public static float cp(float pixels) { // Converted Pixels
        return pixels * 0.75f;//(Resources.getSystem().getDisplayMetrics().widthPixels / 600f) / 2;
    }

    public static float cp(int pixels) {
        return pixels * 0.75f;//(Resources.getSystem().getDisplayMetrics().widthPixels / 600f) / 2;
    }

    public static double cp(double pixels) {
        return pixels * 0.75;//(Resources.getSystem().getDisplayMetrics().widthPixels / 600f) / 2;
    }

    public GameDraw(Context context) {
        super(context);

        getHolder().addCallback(this);

        GameDraw.context = this;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int pointerIndex = ((event.getAction() & MotionEvent.ACTION_POINTER_ID_MASK)
                >> MotionEvent.ACTION_POINTER_ID_SHIFT);
        int action = (event.getAction() & MotionEvent.ACTION_MASK);
        int pointerId = event.getPointerId(pointerIndex);

        event.setLocation(event.getX() * ((float)ScrW/realW), event.getY() * ((float)ScrH/realH));

        switch (action) {
            case MotionEvent.ACTION_MOVE:
                for (SuperVGUI v : getVGUIObjects()) {
                    if (v.pointerId != pointerId) continue;

                    if (!v.CheckTouch(event, pointerId) && v.isPressed()) {
                        v.OnRelease(event);
                    }

                    if (v.isPressed()) {
                        v.OnTouch(event);
                    }
                }

                break;
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_POINTER_DOWN:
                for (SuperVGUI v : getVGUIObjects()) {
                    if (v.CheckTouch(event, pointerId)) {
                        v.pointerId = pointerId;
                        v.OnDown(event);
                        break;
                    }
                }

                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_POINTER_UP:
            case MotionEvent.ACTION_CANCEL:
                for (SuperVGUI v : getVGUIObjects()) {
                    if (v.isPressed() && v.pointerId == pointerId) {
                        v.OnRelease(event);
                    }
                }
        }
        return true;
    }

    @Override
    public void surfaceCreated(SurfaceHolder surfaceHolder) {
        drawThread = new DrawThread(surfaceHolder);
        drawThread.start();

        realW = this.getWidth();
        realH = this.getHeight();
        ScrW = 720;//this.getWidth();
        ScrH = (int)(this.getHeight() * ((float)ScrW/this.getWidth()));

        surfaceHolder.setFixedSize(ScrW, ScrH);

        clipRegion = new Region(-GameDraw.context.ScrW, -GameDraw.context.ScrH, GameDraw.context.ScrW, GameDraw.context.ScrH);

        if (ship == null) {
            ship = new Ship();
            AddEntity(ship);

            stageScript = new Stages.StageScript(Game.getStage());
            stageScript.start();

            Game.setHealth(Game.getMaxHealth());

            final SoundPool soundPool = new SoundPool(5, AudioManager.STREAM_MUSIC, 0);
            final int button10 = soundPool.load(GameDraw.context.getContext(), R.raw.button10, 1);
            final int blip1 = soundPool.load(GameDraw.context.getContext(), R.raw.blip1, 1);

            AddVGUI(new SuperButton(new Vec2D(ScrW * 0.9, ScrH * 0.9), cp(60), cp(60), "L", cp(84), Color.HSVToColor(200, new float[]{280f, 1f, 1f}), new SuperButton.SuperPressEvent() {
                @Override
                public void onPress(SuperButton self, MotionEvent event) {
                    if (!GameDraw.context.ship.activateLaser()) soundPool.play(button10, 1, 1, 0, 0, 1);
                }
            }));

            AddVGUI(new SuperButton(new Vec2D(ScrW * 0.9, ScrH * 0.8), cp(60), cp(60), "P", cp(84), Color.argb(128, 128, 128, 128), new SuperButton.SuperPressEvent() {
                @Override
                public void onPress(SuperButton self, MotionEvent event) {
                    paused = !paused;
                    soundPool.play(blip1, 1, 1, 0, 0, 1);
                }
            }));
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder surfaceHolder, int format, int w, int h) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
        drawThread.kill();

        int i = 0;
        while (true) {
            i++;
            try {
                drawThread.join();
            } catch (InterruptedException e) {

            }

            if (!drawThread.isAlive()) break;

            if (i > 1000) break;
        }
    }
}
