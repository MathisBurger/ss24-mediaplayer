package studiplayer.audio;

import java.util.*;

public class ControllablePlayListIterator implements Iterator<AudioFile> {

    private List<AudioFile> fileList;
    private int position;

    public ControllablePlayListIterator(List<AudioFile> list) {
        this.fileList = list;
        this.position = -1;
    }

    public ControllablePlayListIterator(List<AudioFile> list, String search, SortCriterion sort) {
        this.fileList = ControllablePlayListIterator.applySearchAndSort(list, search, sort);
        this.position = -1;
    }

    @Override
    public boolean hasNext() {
        return this.fileList.size()-1 > this.position;
    }

    @Override
    public AudioFile next() {
        try {
            return this.fileList.get(++this.position);
        } catch (IndexOutOfBoundsException e) {
            return null;
        }
    }

    public Object jumpToAudioFile(AudioFile file) {
        if (this.fileList.contains(file)) {
            this.position = this.fileList.indexOf(file);
            return file;
        }
        return null;
    }

    private static List<AudioFile> applySearchAndSort(List<AudioFile> list, String search, SortCriterion sort) {
        List<AudioFile> filtered = new ArrayList<>();
        for (AudioFile element : list) {
            if (
                    search == null
                    || search.isEmpty()
                    || element.getAuthor().contains(search)
                    || element.getTitle().contains(search)
                    || element.album.contains(search)
            ) {
                filtered.add(element);
            }
        }
        if (sort == null ||sort == SortCriterion.DEFAULT) {
            return filtered;
        }
        Comparator<AudioFile> comp = new AuthorComparator();
        switch (sort) {
            case AUTHOR -> comp = new AuthorComparator();
            case TITLE -> comp = new TitleComparator();
            case ALBUM -> comp = new AuthorComparator();
            case DURATION -> comp = new DurationComparator();
        }
        filtered.sort(comp);
        return filtered;
    }
}
