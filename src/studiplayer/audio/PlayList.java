package studiplayer.audio;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

/**
 * Playlist object that contains playlist data
 */
public class PlayList implements Iterable<AudioFile> {

    private LinkedList<AudioFile> playList;

    private String search;

    private SortCriterion sortCriterion;
    private int current;

    private AudioFile currentAudioFile;

    private ControllablePlayListIterator iterator;

    /**
     * Default constructor
     */
    public PlayList() {
        this.current = 0;
        this.playList = new LinkedList<AudioFile>();
        this.sortCriterion = SortCriterion.DEFAULT;
    }

    /**
     * Constructor for file initialization
     *
     * @param m3uPathname The pathname that is used for init.
     */
    public PlayList(String m3uPathname) {
        this.current = 0;
        this.sortCriterion = SortCriterion.DEFAULT;
        this.loadFromM3U(m3uPathname);
    }

    /**
     * Adds a new element to the playlist
     *
     * @param file The file that should be added
     */
    public void add(AudioFile file) {
        this.playList.add(file);
        this.iterator = new ControllablePlayListIterator(this.playList, this.search, this.sortCriterion);
        if (this.currentAudioFile == null) {
            this.currentAudioFile = this.iterator.next();
        } else {
            this.iterator.jumpToAudioFile(this.currentAudioFile);
        }
    }

    /**
     * Removes a file from the playlist.
     *
     * @param file The file that should be removed.
     */
    public void remove(AudioFile file) {
        this.playList.remove(file);
        this.iterator = new ControllablePlayListIterator(this.playList, this.search, this.sortCriterion);
        this.currentAudioFile = this.iterator.next();
    }

    /**
     * Gives the size of the playlist
     *
     * @return The size of the playlist
     */
    public int size() {
        return this.playList.size();
    }

    /**
     * Gets the current playing index
     *
     * @return The current playing index
     */
    public int getCurrent() {
        return this.current;
    }

    /**
     * Sets the current playing index
     *
     * @param current The new index
     */
    public void setCurrent(int current) {
        this.current = current;
    }

    /**
     * Gets the current audio file that is playing
     *
     * @return The audio file
     */
    public AudioFile currentAudioFile() {
        if (this.playList.isEmpty()) {
            return null;
        }
        if (this.iterator == null) {
            this.iterator = new ControllablePlayListIterator(this.playList, this.search, this.sortCriterion);
            this.currentAudioFile = this.iterator.next();
        }
        return this.currentAudioFile;
    }

    /**
     * Plays the next song
     */
    public void nextSong() {
        if (this.playList.isEmpty()) return;
        if (this.iterator.hasNext()) {
            this.currentAudioFile = this.iterator.next();
        } else {
            this.iterator = new ControllablePlayListIterator(this.playList, this.search, this.sortCriterion);
            this.currentAudioFile = this.iterator.next();
        }
    }

    /**
     * Loads a playlist from m3u.
     *
     * @param pathname Pathname to playlist
     */
    public void loadFromM3U(String pathname) {
        Scanner scanner = null;
        try {
            scanner = new Scanner(new File(pathname));
            this.playList = new LinkedList<AudioFile>();
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                if (!line.startsWith("#") && !line.isBlank()) {
                    try {
                        AudioFile af = AudioFileFactory.createAudioFile(line);
                        this.playList.add(af);
                    } catch (NotPlayableException e) {
                        e.printStackTrace();
                    }
                }
            }
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } finally {
            try {
                assert scanner != null;
                scanner.close();
                this.current = 0;
                this.iterator = new ControllablePlayListIterator(this.playList, this.search, this.sortCriterion);
                this.currentAudioFile = this.iterator.next();
            } catch (Exception|AssertionError ignored) {}
        }
    }

    /**
     * Saves a playlist to m3u
     *
     * @param pathname The location of the saved playlist
     */
    public void saveAsM3U(String pathname) {
        FileWriter writer = null;
        String sep = System.getProperty("line.separator");
        try {
            writer = new FileWriter(pathname);
            for (AudioFile audioFile : this.playList) {
                writer.write(audioFile.getPathname() + sep);
            }
        } catch (IOException e) {
            throw new RuntimeException("Unable to write file " + pathname + "!");
        } finally {
            try {
                assert writer != null;
                writer.close();
            } catch (Exception|AssertionError ignored) {}
        }
    }

    /**
     * Gets the full playlist
     *
     * @return The playlist
     */
    public List<AudioFile> getList() {
        return this.playList;
    }

    public SortCriterion getSortCriterion() {
        return this.sortCriterion;
    }

    public void setSortCriterion(SortCriterion sortCriterion) {
        this.sortCriterion = sortCriterion;
        this.iterator = new ControllablePlayListIterator(this.playList, this.search, this.sortCriterion);
        this.currentAudioFile = this.iterator.next();
    }

    public String getSearch() {
        return this.search;
    }

    public void setSearch(String search) {
        this.search = search;
        this.iterator = new ControllablePlayListIterator(this.playList, this.search, this.sortCriterion);
        this.currentAudioFile = this.iterator.next();
    }

    public Iterator<AudioFile> iterator() {
        return new ControllablePlayListIterator(this.playList, this.search, this.sortCriterion);
    }

    public void jumpToAudioFile(AudioFile file) {
        this.iterator.jumpToAudioFile(file);
        this.currentAudioFile = file;
    }


}
