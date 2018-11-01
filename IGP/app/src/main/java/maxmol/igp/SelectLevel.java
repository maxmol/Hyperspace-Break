package maxmol.igp;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.View;
import android.view.WindowManager;
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

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        context = this;

        GridLayout selectLevels = (GridLayout) findViewById(R.id.select_levels);

        int btnSize = ((Math.min(getResources().getDisplayMetrics().widthPixels, getResources().getDisplayMetrics().heightPixels) * 140) / 768);
        int margin = btnSize / 10;

        final SoundPool soundPool = new SoundPool(5, AudioManager.STREAM_MUSIC, 0);
        final int button10 = soundPool.load(getApplicationContext(), R.raw.button10, 1);
        final int blip1 = soundPool.load(getApplicationContext(), R.raw.blip1, 1);

        for (int i = 1; i <= Stages.COUNT; i++) {
            final Integer finalI = i;
            int style = android.R.style.Widget_Material_Button_Colored;
            Button continue_button;

            if (i < Game.getStep()) {
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP)
                    continue_button = new Button(this, null, style, style);
                else
                    continue_button = new Button(this, null, style);
            }
            else
                continue_button = new Button(this);


            continue_button.setText(finalI.toString());
            continue_button.setTextSize(TypedValue.COMPLEX_UNIT_SP, 40);
            continue_button.setTypeface(ResourcesCompat.getFont(this, R.font.unlearn2));

            GridLayout.LayoutParams layoutParams = new GridLayout.LayoutParams();
            layoutParams.width = btnSize;
            layoutParams.height = btnSize;
            layoutParams.setMargins(margin, margin, margin, margin);
            continue_button.setLayoutParams(layoutParams);

            if (i > Game.getStep()) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    continue_button.setBackgroundTintList(ContextCompat.getColorStateList(SelectLevel.this, R.color.button_material_dark));
                }
                else {
                    continue_button.setBackgroundColor(Color.rgb(64, 64, 64));
                }
            }

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

            selectLevels.addView(continue_button);
            selectLevels.bringToFront();
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
