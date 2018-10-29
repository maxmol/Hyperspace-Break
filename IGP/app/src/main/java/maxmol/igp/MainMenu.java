package maxmol.igp;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import maxmol.igp.classes.Game;
import maxmol.igp.classes.IFile;
import maxmol.igp.classes.SaveLoad;

/*
@ The first activity created after we launch the application.

We init basic SAVE/LOAD data, start menu theme and init buttons for starting, continuing or resetting the game.
 */
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

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

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
        Button button_settings = (Button) findViewById(R.id.mainmenu_settings);

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

        button_settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pressedButton = true;
                Intent intent = new Intent(context, SettingsActivity.class);
                startActivity(intent);
            }
        });

        final TextView moneyView = findViewById(R.id.mainmenu_money);
        moneyView.setText(Game.formatMoney(Game.getMoney()));
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
