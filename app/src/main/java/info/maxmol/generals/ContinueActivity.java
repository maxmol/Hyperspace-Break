package info.maxmol.generals;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import java.io.File;

import info.maxmol.generals.classes.IFile;
import info.maxmol.generals.classes.SaveLoad;

public class ContinueActivity extends Activity {
    private Context context = this;
    public static Activity continueActivity;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_continue);

        continueActivity = this;
    }

    @Override
    protected void onResume() {
        super.onResume();

        LinearLayout continue_scroll = (LinearLayout) findViewById(R.id.continue_scroll);

        File[] files = IFile.Find(context.getFilesDir() + "/savefiles/");

        if (files.length == 0) {
            finish();
            return;
        }

        for (final File f: files) {
            Button continue_button = new Button(context);
            continue_button.setLayoutParams(new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT));

            continue_button.setText(SaveLoad.GetSaveName(f));
            continue_button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    try {
                        SaveLoad.SetSaveFile(f.getAbsolutePath());
                        System.out.println(SaveLoad.GetSaveFile().GetPath());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    Intent intent = new Intent(context, SaveFileInfo.class);
                    startActivityForResult(intent, 0);
                }
            });

            continue_scroll.addView(continue_button);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();

        LinearLayout continue_scroll = (LinearLayout) findViewById(R.id.continue_scroll);
        continue_scroll.removeAllViews();
    }
}
