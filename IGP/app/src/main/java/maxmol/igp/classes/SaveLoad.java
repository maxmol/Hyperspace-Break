package maxmol.igp.classes;

import java.io.File;

// @ Storing game information in a file
public class SaveLoad {
    public static class SaveLoad_NoFileSpecified extends Exception {
        SaveLoad_NoFileSpecified() {
            super("Save file has not been set with SaveLoad.SetSaveFile");
        }
    }

    private static IFile SaveFile;
    private static String splitter = "\n";

    public static void SetSaveFile(String filename) throws IFile.IFileExistanceException {
        SaveFile = new IFile(filename);
    }

    public static IFile GetSaveFile() {
        return SaveFile;
    }

    public static void Save() throws SaveLoad_NoFileSpecified {
        if (SaveFile == null) {
            throw new SaveLoad_NoFileSpecified();
        }

        try {
            SaveFile.Write("");
        } catch (Exception e) {
            e.printStackTrace();
        }

        for (String o : Game.GetTable()) {
            try {
                SaveFile.Append(o + splitter);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static void Load() {
        try {
            String fcontent = SaveFile.Read();
            Game.SetTable(fcontent.split(splitter));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String GetSaveName() {
        try {
            return SaveLoad.GetSaveName(SaveFile.GetFile());
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    public static String GetSaveName(File f) {
        String strtext = f.getName();
        return strtext.substring(0, strtext.length() - 4);
    }
}
