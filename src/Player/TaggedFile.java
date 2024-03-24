package Player;

import studiplayer.basic.TagReader;

import java.util.Map;

public class TaggedFile extends SampledFile {

    private String album;

    public TaggedFile() {
        super();
        this.readAndStoreTags();
    }

    public TaggedFile(String path) {
        super(path);
        this.readAndStoreTags();
    }

    public String getAlbum() {
        return this.album;
    }

    public void readAndStoreTags() {
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
    }

    @Override
    public String toString() {
        if (this.album != null) {
            return super.toString() +  " - " + this.album + " - " + this.formatDuration();
        }
        return super.toString() +  " - " + this.formatDuration();
    }
}
