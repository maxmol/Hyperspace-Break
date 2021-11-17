package maxmol.igp.Drawing;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import maxmol.igp.R;
import maxmol.igp.Classes.Vec2D;

/**
 * Our character icon. It can talk!
 */
public class Avatar extends SuperVGUI {
    private Bitmap bitmap;
    private int size;
    private String saying;
    private String text;
    private long charCooldown = 0;

    public Avatar(Vec2D pos, int size) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inScaled = false;
        options.outWidth = 64;
        options.outHeight = 64;

        setPos(pos);
        this.size = size;

        bitmap = BitmapFactory.decodeResource(GameDraw.context.getResources(), R.drawable.ava, options);
    }

    /**
     * @param text: what will it say
     */
    public void say(String text) {
        saying = text;
    }

    /**
     * @return if the avatar is currently speaking
     */
    public boolean isTalking() {
        return text != null || saying != null;
    }

    @Override
    public void draw(Canvas canvas) {
        Paint p = new Paint();
        p.setAntiAlias(false);
        p.setFilterBitmap(false);
        p.setDither(true);

        canvas.drawBitmap(bitmap, null, new Rect((int) getPos().x, (int) getPos().y, (int) getPos().x + size, (int) getPos().y + size), p);

        if (text != null) {
            p.setColor(Color.WHITE);
            p.setTextSize(GameDraw.cp(32));
            p.setTypeface(DrawThread.sabofilled);
            int y = 0;
            for (String line: text.split("\n")) {
                canvas.drawText(line, (int) (getPos().x + size + GameDraw.cp(10)), (int) (getPos().y + GameDraw.cp(30)) + y, p);
                y += (int) GameDraw.cp(32);
            }
        }

        if (saying != null) {
            if (charCooldown <= 0) {
                charCooldown = 2;

                if (text == null) {
                    text = "" + saying.charAt(0);
                }
                else if (text.length() < saying.length()) {
                    text += saying.charAt(text.length());
                }
                else {
                    if (saying.length() > 0) {
                        charCooldown = 100;
                        saying = "";
                    }
                    else {
                        saying = null;
                        text = null;
                    }
                }
            }
            else {
                charCooldown--;
            }
        }
    }
}
