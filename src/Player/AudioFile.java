package Player;

import java.io.File;
import java.util.Arrays;

/**
 * Audio file that contains information about a file
 */
public abstract class AudioFile {

    protected String pathname;

    protected String filename;

    protected String author;
    protected String title;

    private final String osSep;

    /**
     * Default constructor
     */
    public AudioFile()  {
        this.osSep =  isWindows() ? "\\" : "/";
    }

    /**
     * Constructor that initializes everything
     *
     * @param path The path to the file
     */
    public AudioFile(String path) {
        this.osSep =  isWindows() ? "\\" : "/";
        this.parsePathname(path);
        //this.setConstructorFilename();

    }


    /**
     * Parses the provided pathname and sets the filename, as well as
     * the author and title.
     *
     * @param path The path to the audio file
     */
    public void parsePathname(String path) {
        if (!isWindows()) {
            int index = path.indexOf(':');
            StringBuilder sb = new StringBuilder();
            char[] ca = path.toCharArray();
            for (int i=0; i<ca.length; i++) {
                if (index == (i+1)) {
                    sb.append('/');
                }
                if (i != index) {
                    sb.append(ca[i]);
                }
            }
            path = sb.toString();
        }
        if (isWindows()) {
            path = path.replace("/", "\\");
        } else {
            path = path.replace("\\", "/");
        }
        while (path.contains(this.osSep +  this.osSep)) {
            path = path
                    .replace(this.osSep + this.osSep, this.osSep);
        }
        this.pathname = this.reduceSurroundingWhitespaces(path);
        this.checkCanRead();
        this.setConstructorFilename();
    }

    /**
     * Parses the filename and generates title and author from it
     *
     * @param filename The provided file name
     */
    public void parseFilename(String filename) {
        this.filename = this.reduceSurroundingWhitespaces(filename);
        String[] split = filename.split(" - ");
        if (split.length >= 2) {
            this.author = this.reduceSurroundingWhitespaces(split[0]);
            this.saveSongTitle(split[1]);
            return;
        }
        if (split.length == 0) {
            this.title = "";
        } else {
            this.saveSongTitle(split[0]);
        }
        this.author = "";
    }

    /**
     * Gets the pathname
     *
     * @return The path name
     */
    public String getPathname() {
        return this.pathname;
    }

    /**
     * Gets the file name
     *
     * @return The file name
     */
    public String getFilename() {
        return this.filename;
    }

    /**
     * Gets the author
     *
     * @return The author
     */
    public String getAuthor() {
        return this.author;
    }

    /**
     * Gets the title
     *
     * @return The title
     */
    public String getTitle() {
        return this.title;
    }

    /**
     * Converts the object to a string
     *
     * @return The string
     */
    public String toString() {
        if (!this.author.isEmpty()) {
            return this.author + " - " + this.title;
        }
        return this.title;
    }

    /**
     * Abstract function to play song
     */
    public abstract void play();

    /**
     * Abstract function to toggle pause
     */
    public abstract void togglePause();

    /**
     * Abstract function to stop song
     */
    public abstract void stop();

    /**
     * Abstract function to format duration
     */
    public abstract String formatDuration();

    /**
     * Abstract function to format position
     */
    public abstract String formatPosition();


    /**
     * Removes all whitespaces at the end and beginning of the text
     *
     * @param str The string
     * @return The updated string
     */
    protected String reduceSurroundingWhitespaces(String str) {
        while (str.startsWith(" ")) {
            str = str.substring(1);
        }
        while (str.endsWith(" ")) {
            str = str.substring(0, str.length()-1);
        }
        return str;
    }

    /**
     * Saves the title of a song after formatting it.
     *
     * @param title The title
     */
    private void saveSongTitle(String title) {
        String[] split = title.split("\\.");
        if (split.length == 1) {
            this.title = this.reduceSurroundingWhitespaces(split[0]);
            return;
        }
        title = String.join(".", Arrays.copyOfRange(split, 0, split.length-1));
        this.title = this.reduceSurroundingWhitespaces(title);
    }

    /**
     * Sets the filename from the pathname.
     */
    private void setConstructorFilename() {
        if (this.pathname.endsWith(this.osSep)) {
            this.filename = "";
            this.author = "";
            this.title = "";
            return;
        }
        String[] split = this.pathname.split(getSplitSep());
        this.parseFilename(split[split.length-1]);

    }

    /**
     * Checks if the system runs on windows
     *
     * @return If the OS is windows
     */
    private boolean isWindows() {
        return System.getProperty("os.name").toLowerCase().contains("win");
    }

    /**
     * Gets the OS sep for splitting actions
     *
     * @return The splitting sep
     */
    private String getSplitSep() {
        return isWindows() ? "\\\\" : "/";
    }

    /**
     * Checks if the file can be read
     */
    protected void checkCanRead() {
        File file = new File(this.pathname);
        if (!file.canRead()) {
            throw new RuntimeException("Cannot read file");
        }
    }
}
