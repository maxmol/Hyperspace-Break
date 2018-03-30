package info.maxmol.generals.classes;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Scanner;

class IFileExistanceException extends Exception {
    IFileExistanceException(String filename) {
        super("File does not exist (" + filename + ")");
    }
}

class IFileNotInitialized extends Exception {
    IFileNotInitialized() {
        super("File has not been initialized");
    }
}

public class IFile {
    private String filename;
    private File file;

    public IFile() {

    }

    public IFile(String filename) throws IFileExistanceException {
        this.Open(filename);
    }

    public void Open(String filename) throws IFileExistanceException {
        this.file = new File(filename);

        this.file.getParentFile().mkdirs();

        this.filename = filename;
    }

    public String Read() throws FileNotFoundException, IFileNotInitialized, IFileExistanceException {
        if (file == null) {
            throw new IFileNotInitialized();
        }

        if (!file.exists()) {
            throw new IFileExistanceException(filename);
        }

        Scanner scanner = new Scanner(this.file);
        String content = "";
        while (scanner.hasNext()) {
            content = content + scanner.next() + "\n";
        }
        scanner.close();
        return content;
    }

    public void Write(String content) throws IOException, IFileNotInitialized {
        if (file == null) {
            throw new IFileNotInitialized();
        }

        PrintWriter writer = new PrintWriter(new FileWriter(this.file));
        writer.printf(content);
        writer.close();
    }

    public void Append(String content) throws IOException, IFileNotInitialized, IFileExistanceException {
        String filecontent = this.Read();

        PrintWriter writer = new PrintWriter(new FileWriter(this.file));
        writer.printf(filecontent + content);
        writer.close();
    }

    public String GetPath() throws IFileNotInitialized, IFileExistanceException {
        if (file == null) {
            throw new IFileNotInitialized();
        }

        if (!file.exists()) {
            throw new IFileExistanceException(filename);
        }

        return file.getAbsolutePath();
    }

    public long GetSize() throws IFileNotInitialized, IFileExistanceException {
        if (file == null) {
            throw new IFileNotInitialized();
        }

        if (!file.exists()) {
            throw new IFileExistanceException(filename);
        }

        return file.length();
    }

    public File GetFile() throws IFileNotInitialized {
        if (file == null) {
            throw new IFileNotInitialized();
        }
        return file;
    }

    public String GetFileName() throws IFileNotInitialized {
        if (file == null) {
            throw new IFileNotInitialized();
        }
        return filename;
    }

    public void Delete() throws IFileNotInitialized {
        if (file == null) {
            throw new IFileNotInitialized();
        }

        file.delete();
    }

    public static String ReadFile(String filename) throws IFileExistanceException, FileNotFoundException, IFileNotInitialized {
        IFile f = new IFile(filename);
        return f.Read();
    }

    public static void WriteFile(String filename, String content) throws IFileExistanceException, IOException, IFileNotInitialized {
        IFile f = new IFile(filename);
        f.Write(content);
    }

    public static void AppendFile(String filename, String content) throws IFileExistanceException, IOException, IFileNotInitialized {
        IFile f = new IFile(filename);
        f.Append(content);
    }

    public static File[] Find(String dirName){
        File dir = new File(dirName);
        return dir.listFiles();
    }
}
