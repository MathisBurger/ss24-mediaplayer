package Player;

import java.util.Arrays;

public class AudioFile {

    private String pathname;

    private String filename;

    private String author;
    private String title;

    public AudioFile()  {}

    public AudioFile(String path) {
        this.parsePathname(path);
        this.setConstructorFilename();
    }


    public void parsePathname(String path) {
        String os = System.getProperty("os.name");
        if (!os.contains("Windows") && path.startsWith("C:\\")) {
            path = path.replace("C:\\", "/");
        }
        while (path.contains("\\\\") || path.contains("//")) {
            path = path
                    .replace("\\\\", "\\")
                    .replace("//", "/");
        }
        if (os.contains("Windows")) {
            path = path.replace("/", "\\");
        } else {
            path = path.replace("\\", "/");
        }
        this.pathname = this.reduceSurroundingWhitespaces(path);
        System.out.println("pn: " +  this.pathname);
        this.setConstructorFilename();
    }

    public void parseFilename(String filename) {
        this.filename = filename;
        String[] split = filename.split(" - ");
        if (split.length == 0) {
            this.author = "";
            this.title = "";
            return;
        }
        if (split.length == 1 && split[0].startsWith(".")) {
            this.author = "";
            this.title = "";
            return;
        }
        if (split.length >= 2) {
            this.author = this.reduceSurroundingWhitespaces(split[0]);
            this.saveSongTitle(split[1]);
            return;
        }
        this.author = "";
        this.saveSongTitle(split[0]);
    }

    public String getPathname() {
        return this.pathname;
    }

    public String getFilename() {
        return this.filename;
    }

    public String getAuthor() {
        return this.author;
    }

    public String getTitle() {
        return this.title;
    }

    public String toString() {
        if (!this.author.isEmpty()) {
            return this.author + " - " + this.title;
        }
        return this.title;
    }


    private String reduceSurroundingWhitespaces(String str) {
        while (str.startsWith(" ")) {
            str = str.substring(1);
        }
        while (str.endsWith(" ")) {
            str = str.substring(0, str.length()-1);
        }
        return str;
    }

    private void saveSongTitle(String title) {
        String[] split = title.split("\\.");
        if (split.length == 1) {
            this.title = this.reduceSurroundingWhitespaces(split[0]);
            return;
        }
        title = String.join(".", Arrays.copyOfRange(split, 0, split.length-1));
        this.title = this.reduceSurroundingWhitespaces(title);
    }

    private void setConstructorFilename() {
        if (this.pathname.endsWith("/") || this.pathname.endsWith("\\")) {
            this.filename = "";
            return;
        }
        String sep = "/";
        if (this.pathname.contains("\\")) {
            String[] split = this.pathname.split("\\\\");
            this.parseFilename(split[split.length-1]);
        } else {
            String[] split = this.pathname.split("/");
            this.parseFilename(split[split.length-1]);
        }

    }
}
