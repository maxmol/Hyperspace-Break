package maxmol.igp;

import android.app.Activity;
import android.content.DialogInterface;
import android.media.MediaPlayer;
import android.support.v7.app.AlertDialog;
import android.os.Bundle;
import android.view.Window;
import maxmol.igp.Drawing.GameDraw;

/*
@ Activity class on which the game canvas is created
 */
public class FlightActivity extends Activity {
    private GameDraw gameDraw;
    public static FlightActivity context;
    public static MediaPlayer stage_music;

    /*
    @ Intialize and play background music
    @params
        int id: Music resource ID
     */
    public static void playMusic(int id) {
        if (stage_music != null) {
            stage_music.stop();
        }

        stage_music = MediaPlayer.create(context, id);
        stage_music.setLooping(true);
        stage_music.start();
    }

    /*
    @ Activity init. Create GameDraw, stop menu music
     */
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

    /*
    @ This runs when player presses the "Back" button. We ask /user/ if they really want to
    */
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

    /*
    @ This runs after the game is stopped. We make sure that draw thread is closed.
     */
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

    /*
    @ Play background music again after the app was opened
     */
    @Override
    protected void onResume() {
        super.onResume();

        if (stage_music != null && !stage_music.isPlaying()) {
            stage_music.start();
        }
    }

    /*
    @ If the application was minimized we pause the game and music
     */
    @Override
    protected void onPause() {
        super.onPause();

        GameDraw.context.paused = true;

        if (stage_music != null) stage_music.pause();
    }
}
