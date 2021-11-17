package maxmol.igp;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.res.ResourcesCompat;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import maxmol.igp.Classes.Game;
import maxmol.igp.Classes.SimpleFile;
import maxmol.igp.Classes.SaveLoad;
import maxmol.igp.Classes.Stages;

/**
 * The first activity created after we launch the application.
 * We init basic SAVE/LOAD data, start menu theme and init buttons for starting, continuing or resetting the game.
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

        SimpleFile f = new SimpleFile();

        String filePath = context.getFilesDir() + "/save.txt";

        f.open(filePath);

        try {
            SaveLoad.setSaveFile(filePath);
            System.out.println(SaveLoad.getSaveFile().getPath());
        } catch (Exception e) {
            e.printStackTrace();
        }

        Game.reset();

        try {
            if (f.getFile().exists()) {
                SaveLoad.load();
            } else {
                SaveLoad.save();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        Button button_arcade = (Button) findViewById(R.id.mainmenu_arcade);
        Button button_campaign = (Button) findViewById(R.id.mainmenu_campaign);
        Button button_settings = (Button) findViewById(R.id.mainmenu_settings);

        button_arcade.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Game.setStage(Stages.COUNT + 1);
                pressedButton = true;
                Intent intent = new Intent(context, FlightActivity.class);
                startActivityForResult(intent, 0);
            }
        });

        button_campaign.setOnClickListener(new View.OnClickListener() {
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

        if (android.os.Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
            Typeface font = ResourcesCompat.getFont(this, R.font.sabofilled);
            button_arcade.setTypeface(font);
            button_campaign.setTypeface(font);
            button_settings.setTypeface(font);
            Typeface font2 = ResourcesCompat.getFont(this, R.font.unlearn2);
            moneyView.setTypeface(font2);
            ((TextView)findViewById(R.id.mainmenu_appname)).setTypeface(font2);
        }
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

        try {
            SaveLoad.save();
        } catch (SaveLoad.SaveLoad_NoFileSpecified saveLoad_noFileSpecified) {
            saveLoad_noFileSpecified.printStackTrace();
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
