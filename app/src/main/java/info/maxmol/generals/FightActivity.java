package info.maxmol.generals;

import android.app.Activity;
import android.content.DialogInterface;
import android.media.MediaPlayer;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Window;
import android.widget.TextView;

import info.maxmol.generals.Drawing.GameDraw;
import info.maxmol.generals.classes.Game;

public class FightActivity extends Activity {
    private GameDraw gameDraw;
    public static FightActivity context;
    public static MediaPlayer stage_music;

    public static void playMusic(int id) {
        if (stage_music != null) {
            stage_music.stop();
        }

        stage_music = MediaPlayer.create(context, id);
        stage_music.setLooping(true);
        stage_music.start();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        context = this;
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);

        if (MainMenu.menu_theme != null) {
            MainMenu.menu_theme.stop();
            MainMenu.menu_theme = null;
        }

        gameDraw = new GameDraw(getApplicationContext());
        setContentView(gameDraw);
    }

    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setTitle("Are you sure you want to quit?")
                .setMessage("No progress will be saved.")
                .setPositiveButton("Quit", new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }

                })
                .setNegativeButton("Close", null)
                .show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        GameDraw.context.stageScript.running = false;
        try {
            GameDraw.context.stageScript.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        if (stage_music != null) stage_music.stop();
        MainMenu.initMenuTheme();
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (stage_music != null && !stage_music.isPlaying()) {
            stage_music.start();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();

        GameDraw.context.paused = true;

        if (stage_music != null) stage_music.pause();
    }
}
