package info.maxmol.generals;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import info.maxmol.generals.classes.Game;
import info.maxmol.generals.classes.IFile;
import info.maxmol.generals.classes.SaveLoad;

public class MainMenu extends Activity {

    public static MainMenu context;
    public static MediaPlayer menu_theme;
    public boolean pressedButton = false;

    public static void initMenuTheme() {

        if (menu_theme != null) {
            menu_theme.stop();
        }

        menu_theme = MediaPlayer.create(context, R.raw.menu_theme);
        menu_theme.setLooping(true);
        menu_theme.start();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);

        context = this;

        IFile f = new IFile();

        String filePath = context.getFilesDir() + "/save.txt";

        try {
            f.Open(filePath);
        } catch (IFile.IFileExistanceException e) {
            e.printStackTrace();
        }

        try {
            SaveLoad.SetSaveFile(filePath);
            System.out.println(SaveLoad.GetSaveFile().GetPath());
        } catch (Exception e) {
            e.printStackTrace();
        }

        Game.reset();

        try {
            if (f.GetFile().exists()) {
                SaveLoad.Load();
            }
            else {
                SaveLoad.Save();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        Button button_newgame = (Button) findViewById(R.id.mainmenu_newgame);
        Button button_continue = (Button) findViewById(R.id.mainmenu_continue);

        button_newgame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Game.setStage(Game.getStep());
                pressedButton = true;
                Intent intent = new Intent(context, GameActivity.class);
                startActivity(intent);
            }
        });

        button_continue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pressedButton = true;
                Intent intent = new Intent(context, SelectLevel.class);
                startActivity(intent);
            }
        });

        final TextView moneyView = (TextView) findViewById(R.id.mainmenu_money);
        moneyView.setText(Game.formatMoney(Game.getMoney()));

        Button button_reset = (Button) findViewById(R.id.mainmenu_resetprogress);
        button_reset.setBackgroundColor(Color.rgb(255, 32, 32));
        button_reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new AlertDialog.Builder(context)
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setTitle("Remove all progress")
                        .setMessage("Are you sure you want to reset all progress you made?")
                        .setPositiveButton("Reset", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Game.reset();
                                try {
                                    SaveLoad.Save();
                                } catch (SaveLoad.SaveLoad_NoFileSpecified saveLoad_noFileSpecified) {
                                    saveLoad_noFileSpecified.printStackTrace();
                                }

                                moneyView.setText(Game.formatMoney(Game.getMoney()));
                            }

                        })
                        .setNegativeButton("Close", null)
                        .show();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        TextView moneyView = (TextView) findViewById(R.id.mainmenu_money);
        moneyView.setText(Game.formatMoney(Game.getMoney()));

        if (menu_theme == null)
            initMenuTheme();

        if (!menu_theme.isPlaying()) {
            menu_theme.start();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();

        if (!pressedButton) {
            menu_theme.pause();
        }

        pressedButton = false;
    }
}
