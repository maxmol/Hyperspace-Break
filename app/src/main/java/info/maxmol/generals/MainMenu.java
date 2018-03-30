package info.maxmol.generals;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.io.File;

import info.maxmol.generals.classes.IFile;

public class MainMenu extends Activity {

    private Context context = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);
        Button button_newgame = (Button) findViewById(R.id.mainmenu_newgame);
        Button button_continue = (Button) findViewById(R.id.mainmenu_continue);

        button_newgame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, NewGame.class);
                startActivityForResult(intent, 0);
            }
        });

        button_continue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                File[] files = IFile.Find(context.getFilesDir() + "/savefiles/");
                if (files != null && files.length > 0) {
                    Intent intent = new Intent(context, ContinueActivity.class);
                    startActivityForResult(intent, 0);
                } else {
                    Toast toast = Toast.makeText(context, R.string.menu_nosavedfiles, Toast.LENGTH_SHORT);
                    toast.show();
                }
            }
        });

    }
}
