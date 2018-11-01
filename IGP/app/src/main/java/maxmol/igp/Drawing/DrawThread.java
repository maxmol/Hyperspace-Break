package maxmol.igp.Drawing;

import android.content.DialogInterface;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.AlertDialog;
import android.view.SurfaceHolder;

import maxmol.igp.FlightActivity;
import maxmol.igp.GameActivity;
import maxmol.igp.R;
import maxmol.igp.classes.Game;
import maxmol.igp.classes.MUtil;
import maxmol.igp.classes.SaveLoad;
import maxmol.igp.classes.Stages;

// This is the drawing and thinking machine. It calls and draws all entities in the game.
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
        GameDraw.context.stageSleeping += MUtil.Clamp(intervals, 0);
    }

    private void StageTick() {
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

            if (!GameDraw.context.paused) StageTick();
            sysTime = System.currentTimeMillis();

            // --- Core Stuff ---

            if (!GameDraw.context.paused) {
                if (Math.random() < BackgroundStar.SPAWN_CHANCE) {
                    GameDraw.context.AddEntity(new BackgroundStar());
                }

                // - Tick
                for (Entity ent : GameDraw.context.getEntities()) {
                    ent.Tick();
                }
            }

            Paint p = new Paint();
            p.setAntiAlias(true);
            p.setColor(Color.rgb(0, 0, 0));
            canvas.drawPaint(p);

            if (GameDraw.context.shouldSort) {
                GameDraw.context.SortEntities();
            }

            for (Entity ent : GameDraw.context.getEntities()) {
                ent.Draw(canvas);
            }

            p.setColor(Color.WHITE);
            p.setTypeface(unlearn);
            p.setTextSize(cp(60));
            canvas.drawText(Game.formatMoney(Stages.getMoney()), cp(25), cp(80), p);
            if (Stages.isArcade())
                canvas.drawText("HIGH: " + Game.formatMoney(Game.HighScore), cp(25), cp(160), p);

            p.setColor(Color.rgb(16, 18, 20));
            canvas.drawRect(0, GameDraw.context.ScrH, GameDraw.context.ScrW, GameDraw.context.ScrH + cp(200), p);

            for (SuperVGUI v : GameDraw.context.getVGUIObjects()) {
                v.Draw(canvas);
            }

            p.setColor(Color.GREEN);
            try {
                lerpHealth = MUtil.Lerp(0.15f, lerpHealth, Game.getHealth());
                canvas.drawRect(
                        cp(200),
                        GameDraw.context.ScrH + cp(154),
                        cp(200) + MUtil.Clamp(cp(300) * (lerpHealth/Game.getMaxHealth()), 0),
                        GameDraw.context.ScrH + cp(170), p);
            }
            catch (Exception e) {

            }

            p.setColor(Color.BLUE);
            try {
                lerpArmor = MUtil.Lerp(0.15f, lerpArmor, GameDraw.context.ship.armor);
                canvas.drawRect(
                        cp(200),
                        GameDraw.context.ScrH + cp(124),
                        cp(200) + MUtil.Clamp(cp(300) * (lerpArmor / GameDraw.context.ship.maxArmor), 0),
                        GameDraw.context.ScrH + cp(140), p);
            }
            catch (Exception e) {

            }

            // --- Pause ---

            if (GameDraw.context.paused) {
                p.setColor(Color.WHITE);
                p.setTextSize(cp(64));
                p.setTextAlign(Paint.Align.CENTER);
                p.setTypeface(unlearn);
                canvas.drawText("Paused", GameDraw.context.ScrW / 2, cp(120), p);
            }

            long deltaTime = System.currentTimeMillis() - sysTime + 1;
            //canvas.drawText(1000/deltaTime + " FPS", GameDraw.context.ScrW/10, cp(120), p); // FPS counter

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
                        ent.Remove();
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