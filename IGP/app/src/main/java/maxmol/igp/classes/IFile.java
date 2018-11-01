package maxmol.igp.classes;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Scanner;

/**
 * A simple class i made to make working with filesystem simpler
 */
public class IFile {
    private String filename;
    private File file;

    /**
     * Why not make useless dedicated exceptions for our new class
     */
    public class IFileExistenceException extends Exception {
        IFileExistenceException(String filename) {
            super("File does not exist (" + filename + ")");
        }
    }

    /**
     * Same story
     */
    public class IFileNotInitialized extends Exception {
        IFileNotInitialized() {
            super("File has not been initialized");
        }
    }

    /**
     * ... empty generator ...
     */
    public IFile() {

    }

    /**
     * @param filename: path to file
     */
    public IFile(String filename) {
        this.open(filename);
    }

    /**
     * @param filename: path to file
     */
    public void open(String filename) {
        this.file = new File(filename);

        this.file.getParentFile().mkdirs();

        this.filename = filename;
    }

    /**
     * @return file contents
     * @throws FileNotFoundException: no such file '~'
     * @throws IFileNotInitialized: we forgot to open the file
     * @throws IFileExistenceException:  file doesn't exist
     */
    public String read() throws FileNotFoundException, IFileNotInitialized, IFileExistenceException {
        if (file == null) {
            throw new IFileNotInitialized();
        }

        if (!file.exists()) {
            throw new IFileExistenceException(filename);
        }

        Scanner scanner = new Scanner(this.file);
        String content = "";
        while (scanner.hasNext()) {
            content = content + scanner.next() + "\n";
        }
        scanner.close();
        return content;
    }

    /**
     * @param content: what to write to file
     * @throws IOException: default input/output exception
     * @throws IFileNotInitialized: we forgot to open the file
     */
    public void write(String content) throws IOException, IFileNotInitialized {
        if (file == null) {
            throw new IFileNotInitialized();
        }

        PrintWriter writer = new PrintWriter(new FileWriter(this.file));
        writer.printf(content);
        writer.close();
    }

    /**
     * @param content
     * @throws IOException
     * @throws IFileNotInitialized
     * @throws IFileExistenceException
     */
    public void append(String content) throws IOException, IFileNotInitialized, IFileExistenceException {
        String fileContent = this.read();

        PrintWriter writer = new PrintWriter(new FileWriter(this.file));
        writer.printf(fileContent + content);
        writer.close();
    }

    /**
     * @return our current path
     * @throws IFileNotInitialized: we forgot to open the file
     * @throws IFileExistenceException: file doesn't exist
     */
    public String getPath() throws IFileNotInitialized, IFileExistenceException {
        if (file == null) {
            throw new IFileNotInitialized();
        }

        if (!file.exists()) {
            throw new IFileExistenceException(filename);
        }

        return file.getAbsolutePath();
    }

    /**
     * @return current file's size
     * @throws IFileNotInitialized
     * @throws IFileExistenceException
     */
    public long getSize() throws IFileNotInitialized, IFileExistenceException {
        if (file == null) {
            throw new IFileNotInitialized();
        }

        if (!file.exists()) {
            throw new IFileExistenceException(filename);
        }

        return file.length();
    }

    /**
     * @return our oppened file
     * @throws IFileNotInitialized: we forgot to open it before
     */
    public File getFile() throws IFileNotInitialized {
        if (file == null) {
            throw new IFileNotInitialized();
        }
        return file;
    }

    /**
     * @return current file's name
     * @throws IFileNotInitialized: we forgot to open it before
     */
    public String getFileName() throws IFileNotInitialized {
        if (file == null) {
            throw new IFileNotInitialized();
        }
        return filename;
    }

    /**
     * delete the file
     * @throws IFileNotInitialized: we didn't open it
     */
    public void delete() throws IFileNotInitialized {
        if (file == null) {
            throw new IFileNotInitialized();
        }

        file.delete();
    }

    /**
     * @param filename: the path to file we want to quickly read
     * @return file contents
     * @throws IFileExistenceException
     * @throws FileNotFoundException
     * @throws IFileNotInitialized
     */
    public static String readFile(String filename) throws IFileExistenceException, FileNotFoundException, IFileNotInitialized {
        IFile f = new IFile(filename);
        return f.read();
    }

    /**
     * quickly write to file
     * @param filename: path to file we want to write to
     * @param content: what to write
     * @throws IOException
     * @throws IFileNotInitialized
     */
    public static void writeFile(String filename, String content) throws IOException, IFileNotInitialized {
        IFile f = new IFile(filename);
        f.write(content);
    }

    /**
     * quickly append to file
     * @param filename: path to file
     * @param content: what to add to file
     * @throws IFileExistenceException
     * @throws IOException
     * @throws IFileNotInitialized
     */
    public static void appendFile(String filename, String content) throws IFileExistenceException, IOException, IFileNotInitialized {
        IFile f = new IFile(filename);
        f.append(content);
    }

    /**
     * @param dirName: path to directory to list files from
     * @return all files found in the directory
     */
    public static File[] find(String dirName){
        File dir = new File(dirName);
        return dir.listFiles();
    }
}
