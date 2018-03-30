package info.maxmol.generals.Drawing;

import android.content.DialogInterface;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Region;
import android.support.v7.app.AlertDialog;
import android.view.MotionEvent;

import java.util.ArrayList;

import info.maxmol.generals.FightActivity;
import info.maxmol.generals.classes.BulletGenerator;
import info.maxmol.generals.classes.Game;
import info.maxmol.generals.classes.Stages;
import info.maxmol.generals.classes.Vec2D;

public class Ship extends Entity {
    private boolean moveStarted = false;
    private Vec2D lastTouch;
    private BulletGenerator bulletGenerator;
    private int bulletCountDown = DrawThread.interval * 10;

    public Ship() {
        setPos(new Vec2D(GameDraw.context.ScrW/2, (int) (GameDraw.context.ScrH * 0.8)));
        setPointsMesh(new Vec2D[] {
                new Vec2D(cp(40), cp(50)),
                new Vec2D(cp(-40), cp(50)),
                new Vec2D(cp(0), cp(-50)),
        });

        bulletGenerator = new BulletGenerator(this);
        bulletGenerator.currentSpin = 180;
        bulletGenerator.bulletSpeed = 20;
        bulletGenerator.bulletAcceleration = 0.01;
        bulletGenerator.bulletRate = 7;
        bulletGenerator.bulletDamage = 1;
    }

    @Override
    public void Tick() {

        bulletGenerator.update();

        Region clip = new Region(0, 0, GameDraw.context.ScrW, GameDraw.context.ScrH);

        for (Entity e: (ArrayList<Entity>) GameDraw.context.entities.clone()) {
            if (!(e instanceof Coin)) continue;

            if (e.getPos().Distance(this.getPos()) > cp(100)) { // FPS Fix
                continue;
            }

            Region eReg = new Region();
            eReg.setPath(e.generatePath(), clip);

            Region thisReg = new Region();
            thisReg.setPath(generatePath(), clip);
            if (eReg.op(thisReg, Region.Op.INTERSECT))
            {
                Stages.collectMoney(((Coin) e).getCount());
                e.Remove();
            }
        }
    }

    @Override
    public void OnTouch(MotionEvent event) {
        super.OnTouch(event);

        switch(event.getAction()) {
            case MotionEvent.ACTION_MOVE:
                if (!moveStarted) {
                    lastTouch = new Vec2D(event.getX(), event.getY());
                    moveStarted = true;
                }
                else {
                    Vec2D touch = new Vec2D(event.getX(), event.getY());
                    Move(touch.minus(lastTouch));
                    setPos(Vec2D.Clamp(getPos(), 0, GameDraw.context.ScrW, 0, GameDraw.context.ScrH));
                    lastTouch = touch;
                }

                break;
            case MotionEvent.ACTION_UP:
                moveStarted = false;
        }
    }

    @Override
    public void Draw(Canvas canvas) {
        Paint p = new Paint();
        p.setColor(Color.GREEN);

        drawByPoints(canvas, p);
    }

    @Override
    public int getZPos() {
        return 10;
    }

    @Override
    public void takeDamage(int hp) {
        Game.takeDamage(hp);

        if (Game.getHealth() <= 0) {

            GameDraw.context.drawThread.kill();

            FightActivity.context.runOnUiThread(new Runnable() {

                @Override
                public void run() {
                    new AlertDialog.Builder(FightActivity.context)
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .setTitle("You lose!")
                            .setMessage("Your ship is destroyed.")
                            .setPositiveButton("Close", new DialogInterface.OnClickListener()
                            {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    FightActivity.context.finish();
                                }

                            })
                            .setOnCancelListener(new DialogInterface.OnCancelListener() {
                                @Override
                                public void onCancel(DialogInterface dialogInterface) {
                                    FightActivity.context.finish();
                                }
                            })
                            .show();
                }
            });
        }
    }

    @Override
    public boolean isPhysicsObject() {
        return true;
    }
}
