package studiplayer.audio;

import java.util.Comparator;

public class TitleComparator implements Comparator<AudioFile> {
    @Override
    public int compare(AudioFile o1, AudioFile o2) {
        if (o1 == null || o2 == null) {
            throw new RuntimeException("Values are null");
        }
        return o1.getTitle().compareTo(o2.getTitle());
    }
}
