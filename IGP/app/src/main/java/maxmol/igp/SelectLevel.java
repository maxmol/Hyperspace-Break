package maxmol.igp;

import android.content.Intent;
import android.graphics.Color;
import android.media.AudioManager;
import android.media.SoundPool;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.LinearLayout;

import maxmol.igp.classes.Game;
import maxmol.igp.classes.Stages;

/*
@ An activity where we can chose what level to start. (also freemode)
 */
public class SelectLevel extends AppCompatActivity {
    public static SelectLevel context;
    public boolean pressedButton = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_level);

        context = this;

        GridLayout selectLevels = (GridLayout) findViewById(R.id.select_levels);

        int btnSize = ((Math.min(getResources().getDisplayMetrics().widthPixels, getResources().getDisplayMetrics().heightPixels) * 140) / 768);
        int margin = btnSize / 10;

        final SoundPool soundPool = new SoundPool(5, AudioManager.STREAM_MUSIC, 0);
        final int button10 = soundPool.load(getApplicationContext(), R.raw.button10, 1);
        final int blip1 = soundPool.load(getApplicationContext(), R.raw.blip1, 1);

        for (int i = 1; i <= Stages.COUNT + 1; i++) {
            final Integer finalI = i;
            Button continue_button = new Button(this);

            continue_button.setText(finalI == Stages.COUNT + 1 ? "FreeMode" : finalI.toString());

            GridLayout.LayoutParams layoutParams = new GridLayout.LayoutParams();
            layoutParams.width = finalI == Stages.COUNT + 1 ? btnSize * 2 : btnSize;
            layoutParams.height = btnSize;
            layoutParams.setMargins(margin, margin, margin, margin);
            continue_button.setLayoutParams(layoutParams);

            int bgColor;

            if (i < Game.getStep()) {
                bgColor = Color.rgb(0, 255, 64);
            }
            else if (i == Game.getStep()) {
                if (i == Stages.COUNT + 1)
                    bgColor = Color.rgb(255, 255, 64);
                else
                    bgColor = Color.rgb(196, 196, 196);
            }
            else {
                bgColor = Color.rgb(64, 64, 64);
            }

            continue_button.setBackgroundColor(bgColor);

            continue_button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (finalI <= Game.getStep()) {
                        Game.setStage(finalI);

                        pressedButton = true;

                        Intent intent = new Intent(context, GameActivity.class);
                        startActivityForResult(intent, 0);

                        context.finish();
                    }
                    else {
                        soundPool.play(button10, 0.75f, 0.75f, 0, 0, 1);
                    }
                }
            });

            if (finalI != Stages.COUNT + 1 ) {
                selectLevels.addView(continue_button);
                selectLevels.bringToFront();
            }
            else {
                LinearLayout ly = ((LinearLayout) findViewById(R.id.select_levels_layout));
                ly.addView(continue_button);
                ly.bringToFront();
            }
        }
    }

    @Override
    protected void onPause() {
        super.onPause();

        if (!this.isFinishing() && !pressedButton) {
            MainMenu.menu_theme.pause();
        }

        pressedButton = false;
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (!MainMenu.menu_theme.isPlaying()) {
            MainMenu.menu_theme.start();
        }
    }
}
