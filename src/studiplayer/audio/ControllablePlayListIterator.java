package studiplayer.audio;

import java.util.*;

/**
 * List iterator for playlists
 */
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

    /**
     * Checks if there is a next song
     *
     * @return Gets status of next song
     */
    @Override
    public boolean hasNext() {
        return this.fileList.size()-1 > this.position;
    }

    /**
     * Gets the next song
     *
     * @return Next song
     */
    @Override
    public AudioFile next() {
        try {
            return this.fileList.get(++this.position);
        } catch (IndexOutOfBoundsException e) {
            return null;
        }
    }

    /**
     * Jumps to a specific audio file
     *
     * @param file The file
     * @return The file
     */
    public Object jumpToAudioFile(AudioFile file) {
        if (this.fileList.contains(file)) {
            this.position = this.fileList.indexOf(file);
            return file;
        }
        return null;
    }

    public List<AudioFile> getFileList() {
        return this.fileList;
    }

    /**
     * Applies search and sort params to playlist
     *
     * @param list The list
     * @param search The search text
     * @param sort The sort
     * @return The searched and sorted list
     */
    private static List<AudioFile> applySearchAndSort(List<AudioFile> list, String search, SortCriterion sort) {
        List<AudioFile> filtered = new ArrayList<>();
        for (AudioFile element : list) {
            if (
                    search == null
                    || search.isEmpty()
                    || element.getAuthor().contains(search)
                    || element.getTitle().contains(search)
                    || (element instanceof TaggedFile && ((TaggedFile) element).album.contains(search))
            ) {
                filtered.add(element);
            }
        }
        if (sort == null ||sort == SortCriterion.DEFAULT) {
            return filtered;
        }
        Comparator<AudioFile> comp = new AuthorComparator();
        switch (sort) {
            case AUTHOR:
                comp = new AuthorComparator();
                break;
            case TITLE:
                comp = new TitleComparator();
                break;
            case ALBUM:
                comp = new AuthorComparator();
                break;
            case DURATION:
                comp = new DurationComparator();
                break;
        }
        filtered.sort(comp);
        return filtered;
    }
}
