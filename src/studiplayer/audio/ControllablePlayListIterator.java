package studiplayer.audio;

import java.util.Iterator;
import java.util.List;

public class ControllablePlayListIterator implements Iterator<AudioFile> {

    public ControllablePlayListIterator(List<AudioFile> list) {
        throw new RuntimeException("Not implemented");
    }

    public ControllablePlayListIterator(List<AudioFile> list, String search, SortCriterion sort) {
        throw new RuntimeException("Not implemented");
    }

    @Override
    public boolean hasNext() {
        throw new RuntimeException("Not implemented");
    }

    @Override
    public AudioFile next() {
        throw new RuntimeException("Not implemented");
    }

    public Object jumpToAudioFile(AudioFile file) {
        throw new RuntimeException("Not implemented");
    }
}
