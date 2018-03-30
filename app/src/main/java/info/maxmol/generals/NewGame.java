package info.maxmol.generals;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import java.text.SimpleDateFormat;
import java.util.Date;

import info.maxmol.generals.classes.Game;
import info.maxmol.generals.classes.SaveLoad;


public class NewGame extends Activity {

    private Context context = this;
    private String savename;
    private EditText newgame_filename;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_game);

        newgame_filename = (EditText) findViewById(R.id.newgame_filename);

        long msTime = System.currentTimeMillis();
        Date curDateTime = new Date(msTime);
        SimpleDateFormat formatter = new SimpleDateFormat("MM'-'dd'-'y'_'hh-mm");
        savename = formatter.format(curDateTime);

        newgame_filename.setText(savename);

        Button newgame_start = (Button) findViewById(R.id.newgame_start);

        newgame_start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    Game.reset();
                    SaveLoad.SetSaveFile(context.getFilesDir() + "/savefiles/" + newgame_filename.getText() + ".txt");
                    SaveLoad.Save();
                    System.out.println(SaveLoad.GetSaveFile().GetPath());

                    finish();
                    Intent intent = new Intent(context, GameActivity.class);
                    startActivityForResult(intent, 0);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
