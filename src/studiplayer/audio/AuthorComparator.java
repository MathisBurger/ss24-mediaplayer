package studiplayer.audio;

import java.util.Comparator;

/**
 * Compares audio files by author
 */
public class AuthorComparator implements Comparator<AudioFile> {
    @Override
    public int compare(AudioFile o1, AudioFile o2) {
        if (o1 == null || o2 == null) {
            throw new RuntimeException("Values are null");
        }
        return o1.getAuthor().compareTo(o2.getAuthor());
    }
}
