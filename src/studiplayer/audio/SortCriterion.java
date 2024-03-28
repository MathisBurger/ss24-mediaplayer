package studiplayer.audio;

/**
 * Sorting criterias
 */
public enum SortCriterion {
    DEFAULT("Keine"),
    AUTHOR("Author"),
    TITLE("Titel"),
    ALBUM("Album"),
    DURATION("Dauer");

    private String name;

    SortCriterion(String name) {
        this.name = name;
    }

    public String toString() {
        return this.name;
    }
}
