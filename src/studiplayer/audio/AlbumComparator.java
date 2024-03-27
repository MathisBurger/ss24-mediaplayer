package studiplayer.audio;

import java.lang.reflect.Field;
import java.util.Comparator;

/**
 * Compares audio files by album
 */
public class AlbumComparator implements Comparator<AudioFile> {
    @Override
    public int compare(AudioFile o1, AudioFile o2) {
        if (o1 == null || o2 == null) {
             throw new RuntimeException("Values are null");
        }
        if (o1.getAlbum() == null && o2.getAlbum() == null) {
            return 0;
        }
        if (o1.getAlbum() == null) {
            return -1;
        }
        if (o2.getAlbum() == null) {
            return 1;
        }
        return o1.getAlbum().compareTo(o2.getAlbum());
    }
}
