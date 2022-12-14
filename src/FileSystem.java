import java.io.File;

public class FileSystem {
    private File directory;

    protected FileSystem(File directory) {
        this.directory = directory;
    }

    public File getDirectory() { return directory; }
}
