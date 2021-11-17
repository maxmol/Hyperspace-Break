package maxmol.igp.Drawing;

import android.content.DialogInterface;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Typeface;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.AlertDialog;
import android.view.SurfaceHolder;

import maxmol.igp.FlightActivity;
import maxmol.igp.GameActivity;
import maxmol.igp.R;
import maxmol.igp.Classes.Game;
import maxmol.igp.Classes.MUtil;
import maxmol.igp.Classes.Stages;

/**
 * This is the drawing and thinking machine. It calls and draws all entities in the game.
 */
public class DrawThread extends Thread {
    private SurfaceHolder surfaceHolder;
    private boolean running = true;
    public static final int interval = 25;
    public long sysTime;
    private int dieCounter = 50; // Wait a bit before creating a dialog window

    private float lerpHealth = 0;
    private float lerpArmor = 0;
    public static Typeface unlearn;
    public static Typeface sabofilled;

    private static float cp(float pixels) { // Converted Pixels
        return GameDraw.cp(pixels);
    }

    private static float cp(int pixels) {
        return GameDraw.cp(pixels);
    }

    private static double cp(double pixels) {
        return GameDraw.cp(pixels);
    }

    public DrawThread(SurfaceHolder surfaceHolder) {
        this.surfaceHolder = surfaceHolder;
        unlearn = ResourcesCompat.getFont(FlightActivity.context, R.font.unlearn2);
        sabofilled = ResourcesCompat.getFont(FlightActivity.context, R.font.sabofilled);
    }

    public void stageSleep(int intervals) {
        GameDraw.context.stageSleeping += MUtil.clamp(intervals, 0);
    }

    private void stageTick() {
        if (GameDraw.context.stageSleeping > 0) {
            GameDraw.context.stageSleeping--;
        }
    }

    @Override
    public void run() {
        Stages.start();

        while (running) {
            Canvas canvas = surfaceHolder.lockCanvas();

            if (canvas == null) {
                continue;
            }

            // --- TickStuff ---

            if (!GameDraw.context.paused) stageTick();
            sysTime = System.currentTimeMillis();

            // --- Core Stuff ---

            if (!GameDraw.context.paused) {
                if (Math.random() < BackgroundStar.SPAWN_CHANCE) {
                    GameDraw.context.AddEntity(new BackgroundStar());
                }

                // - tick
                for (Entity ent : GameDraw.context.getEntities()) {
                    ent.tick();
                }
            }

            Paint p = new Paint();
            p.setAntiAlias(true);
            p.setColor(Color.rgb(21, 5, 28));
            canvas.drawPaint(p);

            if (GameDraw.context.shouldSort) {
                GameDraw.context.sortEntities();
            }

            for (Entity ent : GameDraw.context.getEntities()) {
                ent.draw(canvas);
            }

            p.setColor(Color.WHITE);
            p.setTypeface(unlearn);
            p.setTextSize(cp(60));
            canvas.drawText(Game.formatMoney(Stages.getMoney()), cp(25), cp(80), p);
            if (Stages.isArcade())
                canvas.drawText("HIGH: " + Game.formatMoney(Game.highScore), cp(25), cp(160), p);

            p.setColor(Color.rgb(16, 18, 20));
            canvas.drawRect(0, GameDraw.context.scrH, GameDraw.context.scrW, GameDraw.context.scrH + cp(200), p);

            for (SuperVGUI v : GameDraw.context.getVGUIObjects()) {
                v.draw(canvas);
            }

            p.setColor(Color.rgb(0, 194, 14));
            try {
                lerpHealth = MUtil.lerp(0.15f, lerpHealth, Game.getHealth());
                if (lerpHealth > 0.5f) {

                    float left = cp(200);
                    float top = GameDraw.context.scrH + cp(144);
                    float right = cp(200) + MUtil.clamp(cp(240) * (lerpHealth / Game.getMaxHealth()), 0);
                    float bottom = GameDraw.context.scrH + cp(160);

                    canvas.drawRect(
                            left,
                            top,
                            right,
                            bottom,
                            p);

                    Path path = new Path();
                    path.moveTo(right + cp(10), top);
                    path.lineTo(right, bottom);
                    path.lineTo(right, top);
                    canvas.drawPath(path, p);
                }
            }
            catch (Exception e) {

            }

            p.setColor(Color.rgb(0, 81, 216));
            try {
                lerpArmor = MUtil.lerp(0.15f, lerpArmor, GameDraw.context.ship.armor);
                if (lerpArmor > 0.5f) {
                    float left = cp(200);
                    float top = GameDraw.context.scrH + cp(114);
                    float right = cp(200) + MUtil.clamp(cp(280) * (lerpArmor / GameDraw.context.ship.maxArmor), 0);
                    float bottom = GameDraw.context.scrH + cp(130);
                    canvas.drawRect(
                            left,
                            top,
                            right,
                            bottom,
                            p);

                    Path path = new Path();
                    path.moveTo(right + cp(10), top);
                    path.lineTo(right, bottom);
                    path.lineTo(right, top);
                    canvas.drawPath(path, p);
                }
            }
            catch (Exception e) {

            }

            // --- Pause ---

            if (GameDraw.context.paused) {
                p.setColor(Color.WHITE);
                p.setTextSize(cp(64));
                p.setTextAlign(Paint.Align.CENTER);
                p.setTypeface(unlearn);
                canvas.drawText("Paused", GameDraw.context.scrW / 2f, cp(120), p);
            }

            long deltaTime = System.currentTimeMillis() - sysTime + 1;
            //canvas.drawText(1000/deltaTime + " FPS", GameDraw.context.scrW/10, cp(120), p); // FPS counter

            surfaceHolder.unlockCanvasAndPost(canvas);

            if (deltaTime < interval) {
                try {
                    sleep(interval - deltaTime);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            if (GameDraw.context.ship == null || !GameDraw.context.ship.isValid()) {
                if (dieCounter > 0) {
                    dieCounter--;
                }
                else {
                    for (Entity ent : GameDraw.context.getEntities()) {
                        ent.remove();
                    }

                    FlightActivity.context.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            new AlertDialog.Builder(FlightActivity.context)
                                    .setIcon(android.R.drawable.ic_dialog_alert)
                                    .setTitle(Stages.isArcade() ? "Collected: " + Game.formatMoney(Stages.getMoney()) : "You lose!")
                                    .setMessage("Your ship is destroyed.")
                                    .setPositiveButton("Close", new DialogInterface.OnClickListener()
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

                    kill();
                }
            }
        }
    }

    public static void getout() {
        if (!Stages.isArcade()) {
            GameActivity.context.recreate();
        }
        FlightActivity.context.finish();
    }

    public void kill() {
        running = false;
    }
}