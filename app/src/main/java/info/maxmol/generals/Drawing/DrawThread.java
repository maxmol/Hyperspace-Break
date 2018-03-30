package info.maxmol.generals.Drawing;

import android.content.DialogInterface;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.SystemClock;
import android.support.v7.app.AlertDialog;
import android.view.SurfaceHolder;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import info.maxmol.generals.FightActivity;
import info.maxmol.generals.GameActivity;
import info.maxmol.generals.classes.Game;
import info.maxmol.generals.classes.Stages;

public class DrawThread extends Thread {
    private SurfaceHolder surfaceHolder;
    private boolean running = true;
    public static final int interval = 10;
    public long sysTime;
    public long sysTime2;

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
    }

    @Override
    public void run() {
        Stages.start();

        while (running) {
            // --- TickStuff --

            Stages.completion--;

            sysTime = System.currentTimeMillis();
            // - Add stars

            if (Math.random() < BackgroundStar.SPAWN_CHANCE) {
                GameDraw.context.AddEntity(new BackgroundStar());
            }

            // - Add enemies

            Enemy.counterToNextSpawn -= interval;
            if (Enemy.counterToNextSpawn <= 0) {
                GameDraw.context.AddEntity(new Enemy());
                Enemy.counterToNextSpawn = Stages.enemySpawnRate();
            }

            // --- Core Stuff ---

            for (Entity ent: (ArrayList<Entity>) GameDraw.context.entities.clone()) {
                ent.Tick();
            }

            Canvas canvas = surfaceHolder.lockCanvas();
            if (canvas == null) {
                continue;
            }

            Paint p = new Paint();
            p.setColor(Color.rgb(0, 0, 64));
            p.setAntiAlias(true);
            canvas.drawPaint(p);

            for (Entity ent : GameDraw.context.entities) {
                ent.Draw(canvas);
            }

            p.setColor(Color.WHITE);
            p.setTextSize(cp(16));
            canvas.drawText(Game.formatMoney(Stages.getMoney()), cp(10), cp(40), p);
            p.setTextAlign(Paint.Align.RIGHT);
            canvas.drawText((int) (((float) Stages.stageTime() - Stages.completion)/Stages.stageTime()*100) + "% Complete", GameDraw.context.ScrW - cp(10), cp(40), p);

            p.setColor(Color.GREEN);

            try {
                canvas.drawRect(
                        0f,
                        (float) GameDraw.context.ScrH - (cp(16)),
                        (GameDraw.context.ScrW * ((float) Game.getHealth() / (float) Game.getMaxHealth())),
                        (float) GameDraw.context.ScrH, p);
            }
            catch (ArithmeticException e) {

            }

            if (Stages.completion % 10 == 0) {
                sysTime2 = (int) (1f / ((System.currentTimeMillis() - sysTime) / 1000f));
            }
            System.out.println(sysTime2);

            p.setTextAlign(Paint.Align.CENTER);
            p.setTextSize(cp(64));
            canvas.drawText(sysTime2 + " fps", GameDraw.context.ScrW / 2, cp(50), p);
            surfaceHolder.unlockCanvasAndPost(canvas);

            if (Stages.completion == 0) {
                Game.addMoney(Stages.getMoney());
                Game.nextStep();

                FightActivity.context.runOnUiThread(new Runnable() {

                    @Override
                    public void run() {
                        new AlertDialog.Builder(FightActivity.context)
                                .setIcon(android.R.drawable.ic_dialog_alert)
                                .setTitle("Congrats!")
                                .setMessage("You have completed this stage.")
                                .setPositiveButton("Continue", new DialogInterface.OnClickListener()
                                {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        FightActivity.context.finish();
                                    }

                                })
                                .show();
                    }
                });

                kill();
            }

            long deltaTime = System.currentTimeMillis() - sysTime;

            if (deltaTime < interval) {
                /*try {
                    sleep(interval - deltaTime);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }*/
            }
        }
    }


    public void kill() {
        running = false;
    }
}