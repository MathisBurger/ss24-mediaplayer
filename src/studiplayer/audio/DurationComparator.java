package studiplayer.audio;

import java.util.Comparator;

public class DurationComparator implements Comparator<AudioFile> {
    @Override
    public int compare(AudioFile o1, AudioFile o2) {
        if (o1 == null || o2 == null) {
            throw new RuntimeException("Values are null");
        }
        return Long.compare(o1.duration, o2.duration);
    }
}
