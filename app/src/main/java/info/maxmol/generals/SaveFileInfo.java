package info.maxmol.generals;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import info.maxmol.generals.classes.Game;
import info.maxmol.generals.classes.SaveLoad;

public class SaveFileInfo extends Activity {
    private Context context = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_save_file_info);

        TextView filename = (TextView) findViewById(R.id.savefileinfo_filename);
        filename.setText(SaveLoad.GetSaveName());

        Button savefileinfo_continue = (Button) findViewById(R.id.savefileinfo_continue);
        Button savefileinfo_delete = (Button) findViewById(R.id.savefileinfo_delete);
        TextView savefileinfo_info = (TextView) findViewById(R.id.savefileinfo_info);

        try {
            SaveLoad.Load();
        } catch (Exception e) {
            e.printStackTrace();
        }

        savefileinfo_continue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ContinueActivity.continueActivity.finish();
                finish();
                Intent intent = new Intent(context, GameActivity.class);
                startActivity(intent);
            }
        });

        savefileinfo_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    SaveLoad.GetSaveFile().Delete();
                } catch (Exception e) {
                    e.printStackTrace();
                }

                finish();
            }
        });

        Object[] tbl = Game.GetTable();
        String saveinfo_str = "";
        for (int i = 0; i < tbl.length; i++) {
            saveinfo_str += ": " + tbl[i] + "\n";
        }
        savefileinfo_info.setText(saveinfo_str);
    }
}
