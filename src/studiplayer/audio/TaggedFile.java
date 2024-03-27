package studiplayer.audio;

import studiplayer.basic.TagReader;

import java.util.Map;

/**
 * Tagged file that can be used to read tags.
 */
public class TaggedFile extends SampledFile {

    /**
     * Album of the song
     */
    protected String album;

    public TaggedFile() {
        super();
        this.album = "";
        //this.readAndStoreTags();
    }

    public TaggedFile(String path) throws NotPlayableException {
        super(path);
        this.album = "";
        this.readAndStoreTags();
    }

    /**
     * Gets the album of the song
     *
     * @return The album
     */
    @Override
    public String getAlbum() {
        return this.album;
    }

    /**
     * Reads and stores the tags to the attributes.
     */
    public void readAndStoreTags() throws NotPlayableException {
        try {
            Map<String, Object> tags = TagReader.readTags(this.pathname);
            if (tags.containsKey("title")) {
                this.title = this.reduceSurroundingWhitespaces(tags.get("title").toString());
            }
            if (tags.containsKey("author")) {
                this.author = this.reduceSurroundingWhitespaces(tags.get("author").toString());
            }
            if (tags.containsKey("album")) {
                this.album = this.reduceSurroundingWhitespaces(tags.get("album").toString());
            }
            if (tags.containsKey("duration")) {
                this.duration = (long)tags.get("duration");
            }
        } catch (RuntimeException e) {
            throw new NotPlayableException(this.pathname, e);
        }
    }

    /**
     * Overwritten to string function to consider duration and album.
     *
     * @return Song as string
     */
    @Override
    public String toString() {
        if (this.album != null && !this.album.isEmpty()) {
            return super.toString() +  " - " + this.album + " - " + this.formatDuration();
        }
        return super.toString() +  " - " + this.formatDuration();
    }
}
