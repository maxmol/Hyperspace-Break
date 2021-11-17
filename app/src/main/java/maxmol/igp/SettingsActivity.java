package maxmol.igp;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;

import maxmol.igp.Classes.Game;
import maxmol.igp.Classes.SaveLoad;

public class SettingsActivity extends AppCompatActivity {

    public static SettingsActivity context = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        context = this;

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        final Button button_difficulty = findViewById(R.id.settings_difficulty);
        button_difficulty.setText("difficulty: " + Game.difficulties[Game.difficulty]);
        button_difficulty.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Game.difficulty = (Game.difficulty + 1) % Game.difficulties.length;
                button_difficulty.setText("difficulty: " + Game.difficulties[Game.difficulty]);
                try {
                    SaveLoad.save();
                } catch (SaveLoad.SaveLoad_NoFileSpecified saveLoad_noFileSpecified) {
                    saveLoad_noFileSpecified.printStackTrace();
                }
            }
        });

        Button button_reset = findViewById(R.id.settings_resetprogress);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            button_reset.setBackgroundTintList(ContextCompat.getColorStateList(SettingsActivity.this, R.color.colorAccent));
        }
        button_reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new AlertDialog.Builder(context)
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setTitle("remove all progress")
                        .setMessage("Are you sure you want to reset all progress you made?")
                        .setPositiveButton("Reset", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Game.reset();
                                try {
                                    SaveLoad.save();
                                } catch (SaveLoad.SaveLoad_NoFileSpecified saveLoad_noFileSpecified) {
                                    saveLoad_noFileSpecified.printStackTrace();
                                }
                            }

                        })
                        .setNegativeButton("Close", null)
                        .show();
            }
        });
    }
}
