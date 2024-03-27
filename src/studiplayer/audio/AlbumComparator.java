package studiplayer.audio;

import java.util.Comparator;

public class AlbumComparator implements Comparator<AudioFile> {
    @Override
    public int compare(AudioFile o1, AudioFile o2) {
        if (o1 == null || o2 == null) {
             throw new RuntimeException("Values are null");
        }
        if (o1.album == null || o1.album.isEmpty()) {
            return -1;
        }
        if (o2.album == null || o2.album.isEmpty()) {
            return 1;
        }
        return o1.album.compareTo(o2.album);
    }
}
