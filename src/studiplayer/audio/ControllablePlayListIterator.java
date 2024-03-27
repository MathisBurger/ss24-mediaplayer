package studiplayer.audio;

import java.util.Iterator;
import java.util.List;

public class ControllablePlayListIterator implements Iterator<AudioFile> {

    private List<AudioFile> fileList;
    private int position;

    public ControllablePlayListIterator(List<AudioFile> list) {
        this.fileList = list;
        this.position = -1;
    }

    public ControllablePlayListIterator(List<AudioFile> list, String search, SortCriterion sort) {
        this.fileList = list;
        this.position = -1;
    }

    @Override
    public boolean hasNext() {
        return this.fileList.size()-1 > this.position;
    }

    @Override
    public AudioFile next() {
        return this.fileList.get(++this.position);
    }

    public Object jumpToAudioFile(AudioFile file) {
        if (this.fileList.contains(file)) {
            this.position = this.fileList.indexOf(file);
            return file;
        }
        return null;
    }
}
