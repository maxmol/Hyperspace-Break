package info.maxmol.generals;

import android.app.Activity;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Window;

import info.maxmol.generals.Drawing.GameDraw;

public class FightActivity extends Activity {
    private GameDraw gameDraw;
    public static FightActivity context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        context = this;
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
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
}
