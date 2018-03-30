package info.maxmol.generals.classes;

import android.util.Log;

public class  GameDebug {
    private String name;
    private long startTime;

    public GameDebug(String name) {
        this.name = name;
        startTime = System.nanoTime();
    }

    public void done() {
        Log.i(name, "Was running for " + (System.nanoTime() - startTime)/1000 + " ms");
    }
}
