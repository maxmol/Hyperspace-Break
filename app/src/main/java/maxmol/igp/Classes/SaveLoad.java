package maxmol.igp.Classes;

/**
 * Storing game information in a file
 */
public class SaveLoad {

    /**
     * Some other useless exception
     */
    public static class SaveLoad_NoFileSpecified extends Exception {
        SaveLoad_NoFileSpecified() {
            super("save file has not been set with SaveLoad.setSaveFile");
        }
    }

    private static SimpleFile saveFile;
    private static String splitter = "\n";

    /**
     * Sets the file in which everything is saved
     * @param filename: path to file
     */
    public static void setSaveFile(String filename) {
        saveFile = new SimpleFile(filename);
    }

    /**
     * @return our set save file
     */
    public static SimpleFile getSaveFile() {
        return saveFile;
    }

    /**
     * Save everything
     * @throws SaveLoad_NoFileSpecified: we forgot to set the file
     */
    public static void save() throws SaveLoad_NoFileSpecified {
        if (saveFile == null) {
            throw new SaveLoad_NoFileSpecified();
        }

        try {
            saveFile.write("");
        } catch (Exception e) {
            e.printStackTrace();
        }

        for (String o : Game.getTable()) {
            try {
                saveFile.append(o + splitter);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Get all saved contents and set them to the game
     */
    public static void load() {
        try {
            String fcontent = saveFile.read();
            Game.setTable(fcontent.split(splitter));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
