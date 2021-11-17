package maxmol.igp.Drawing;

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

import maxmol.igp.R;
import maxmol.igp.Classes.Game;
import maxmol.igp.Classes.Stages;
import maxmol.igp.Classes.Vec2D;

/**
 * Classes organization and preparation to start the game
 */
public class GameDraw extends SurfaceView implements SurfaceHolder.Callback {
    public static GameDraw context;

    public DrawThread drawThread;
    public Ship ship;
    public Avatar ava;
    public boolean shouldSort = false;
    public SoundPool soundPool;

    public int realW, realH;
    public int scrW, scrH;

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

    public void addVGUI(SuperVGUI o) {
        vgui.add(o);

        shouldSort = true;
    }

    public ArrayList<SuperVGUI> getVGUIObjects() {
        return (ArrayList<SuperVGUI>) vgui.clone();
    }

    synchronized public void sortEntities() {
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

        event.setLocation(event.getX() * ((float) scrW /realW), event.getY() * ((float) scrH /realH));

        try {
            switch (action) {
                case MotionEvent.ACTION_MOVE:
                    for (SuperVGUI v : getVGUIObjects()) {
                        if (v.pointerId != pointerId) continue;

                        if (!v.checkTouch(event, pointerId) && v.isPressed()) {
                            v.onRelease(event);
                        }

                        if (v.isPressed()) {
                            v.onTouch(event);
                        }
                    }

                    break;
                case MotionEvent.ACTION_DOWN:
                case MotionEvent.ACTION_POINTER_DOWN:
                    for (SuperVGUI v : getVGUIObjects()) {
                        if (v.checkTouch(event, pointerId)) {
                            v.pointerId = pointerId;
                            v.onDown(event);
                            break;
                        }
                    }

                    break;
                case MotionEvent.ACTION_UP:
                case MotionEvent.ACTION_POINTER_UP:
                case MotionEvent.ACTION_CANCEL:
                    for (SuperVGUI v : getVGUIObjects()) {
                        if (v.isPressed() && v.pointerId == pointerId) {
                            v.onRelease(event);
                        }
                    }
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        return true;
    }

    /*@Override
    public boolean onKeyDown(int keycode, KeyEvent event) {
        Toast.makeText(getContext(), "???", Toast.LENGTH_LONG);
        short x = 0, y = 0;
        switch (keycode) {
            case KeyEvent.KEYCODE_DPAD_DOWN:
                y = 1;
                break;
            case KeyEvent.KEYCODE_DPAD_UP:
                y = -1;
                break;
            case KeyEvent.KEYCODE_DPAD_LEFT:
                x = -1;
                break;
            case KeyEvent.KEYCODE_DPAD_RIGHT:
                x = 1;
                break;
        }
        ship.movePad.setPos(new Vec2D(scrW * 0.2, scrH * 0.8));
        ship.movePad.output = new Vec2D(x, y);

        return true;
    }*/

    @Override
    public void surfaceCreated(SurfaceHolder surfaceHolder) {
        drawThread = new DrawThread(surfaceHolder);
        drawThread.start();

        soundPool = new SoundPool(5, AudioManager.STREAM_MUSIC, 0);

        realW = this.getWidth();
        scrW = 720;//this.getWidth();
        double coef = ((float) scrW /this.getWidth());
        scrH = (int)(this.getHeight() * coef - cp(200));

        realH = (int) (this.getHeight() - cp(200) / coef);

        surfaceHolder.setFixedSize(scrW, scrH + (int)cp(200));

        clipRegion = new Region(-scrW, -scrH, scrW, scrH);

        if (ship == null) { // initializing game objects
            final int button10 = soundPool.load(GameDraw.context.getContext(), R.raw.button10, 1);
            final int blip1 = soundPool.load(GameDraw.context.getContext(), R.raw.blip1, 1);

            addVGUI(new SuperButton(new Vec2D(scrW - cp(280), scrH + cp(40)), cp(120), cp(120), "!", cp(80), Color.argb(128, 64, 64, 64), new SuperButton.SuperPressEvent() {
                @Override
                public void onPress(SuperButton self, MotionEvent event) {
                    if (!GameDraw.context.ship.activateLaser()) soundPool.play(button10, 1, 1, 0, 0, 1);
                }
            }));

            addVGUI(new SuperButton(new Vec2D(scrW - cp(140), scrH + cp(40)), cp(120), cp(120), "❚❚", cp(50), Color.argb(128, 64, 64, 64), new SuperButton.SuperPressEvent() {
                @Override
                public void onPress(SuperButton self, MotionEvent event) {
                    paused = !paused;
                    soundPool.play(blip1, 1, 1, 0, 0, 1);
                }
            }));

            ava = new Avatar(new Vec2D(cp(10), GameDraw.context.scrH + cp(10)), (int)cp(180));
            addVGUI(ava);

            float h = 0f;

            while (h < scrH) {
                h += BackgroundStar.SPAWN_CHANCE * Math.random() * 1500;
                GameDraw.context.AddEntity(new BackgroundStar(new Vec2D(scrW * Math.random(), h)));
            }

            ship = new Ship();
            AddEntity(ship);

            stageScript = new Stages.StageScript(Game.getStage());
            stageScript.start();

            Game.setHealth(Game.getMaxHealth());
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
