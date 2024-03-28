package studiplayer.ui;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import studiplayer.audio.AudioFile;
import studiplayer.audio.NotPlayableException;
import studiplayer.audio.PlayList;
import studiplayer.audio.SortCriterion;

import java.io.File;
import java.net.URL;
import java.util.Arrays;
import java.util.Objects;

/**
 * The player UI class
 */
public class Player extends Application {

    // TODO: Change before APA
    public static final String DEFAULT_PLAYLIST = "../playlists/DefaultPlayList.m3u";
    private static final String PLAYLIST_DIRECTORY = "";
    private static final String INITIAL_PLAY_TIME_LABEL = "00:00";
    private static final String NO_CURRENT_SONG = "-";

    private PlayList playList;
    private boolean useCertPlayList = false;
    private Button playButton;
    private Button pauseButton;
    private Button stopButton;
    private Button nextButton;
    private Label playListLabel;
    private Label playTimeLabel;
    private Label currentSongLabel;
    private ChoiceBox sortChoiceBox;
    private TextField searchTextField;
    private Button filterButton;
    private String playlistPath = DEFAULT_PLAYLIST;
    protected SongTable songTable;
    private PlayerThread playerThread;
    private TimerThread timerThread;

    /**
     * Default constructor that sets all parameters
     */
    public Player() {
        this.useCertPlayList = false;
        this.playList = new PlayList();
        this.playerThread = new PlayerThread();
        this.timerThread = new TimerThread();
    }

    /**
     * Entry point
     */
    public static void main(String[] args) {
        launch();
    }

    /**
     * Starts the UI application
     */
    @Override
    public void start(Stage stage) throws Exception {
        stage.setTitle("APA Player");
        BorderPane mainPane = new BorderPane();
        Scene scene = new Scene(mainPane, 600, 400);
        stage.setScene(scene);
        stage.show();

        // File reading
        if (!this.useCertPlayList) {
            FileChooser playlistChooser = new FileChooser();
            playlistChooser.setTitle("Playlist auswählen");
            File selectedFile = playlistChooser.showOpenDialog(stage);
            this.playList = new PlayList(selectedFile.getAbsolutePath());
            this.playlistPath = selectedFile.getAbsolutePath();
        }

        // Init content
        this.songTable = this.createSongTable();
        mainPane.setTop(this.createFilterPane());
        mainPane.setCenter(this.songTable);
        mainPane.setBottom(this.createBottom());

        // Handle table clicks
        this.songTable.setRowSelectionHandler(e -> {
            TableView.TableViewSelectionModel<Song> sm = this.songTable.getSelectionModel();
            this.stopCurrentSong();
            this.playList.jumpToAudioFile(sm.getSelectedItem().getAudioFile());
            this.playCurrentSong();
        });
    }

    /**
     * Sets the useCertPlayList flag that is required for testing
     *
     * @param useCertPlayList The value useCertPlayList
     */
    public void setUseCertPlayList(boolean useCertPlayList) {
        this.useCertPlayList = useCertPlayList;
    }

    /**
     * Loads a playlist from path
     *
     * @param pathname The path
     */
    public void loadPlayList(String pathname) {
        this.playList = new PlayList(Objects.requireNonNullElse(pathname, DEFAULT_PLAYLIST));
    }

    /**
     * Player thread class that plays music
     */
    class PlayerThread extends Thread {

        private volatile boolean stopped = false;

        @Override
        public void run() {
            while (!stopped) {
                AudioFile af = playList.currentAudioFile();
                try {
                    af.play();
                } catch (NotPlayableException e) {
                    throw new RuntimeException(e);
                }
                if (!stopped) {
                    playList.nextSong();
                }
            }
        }

        public void terminate() {
            this.stopped = true;
        }
    }

    /**
     * Timer thread that increases timer
     */
    class TimerThread extends Thread {

        private boolean stopped = false;

        @Override
        public void run() {
            while (!stopped) {
                AudioFile af = playList.currentAudioFile();
                if (af != null) {
                    updateSongInfo(af);
                }
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }

        public void terminate() {
            this.stopped = true;
        }
    }

    /**
     * Plays the current song
     */
    private void playCurrentSong() {
        AudioFile af = this.playList.currentAudioFile();
        if (af != null) {
            this.playerThread = new PlayerThread();
            this.playerThread.start();
            this.timerThread = new TimerThread();
            this.timerThread.start();
            this.playTimeLabel.setText(INITIAL_PLAY_TIME_LABEL);
            this.currentSongLabel.setText(af.toString());
            this.playButton.setDisable(true);
            this.nextButton.setDisable(false);
            this.stopButton.setDisable(false);
            this.pauseButton.setDisable(false);
        }
    }

    /**
     * Pauses the current song
     */
    private void pauseCurrentSong() {
        if (this.timerThread.isAlive()) {
            this.timerThread.terminate();
        } else {
            this.timerThread = new TimerThread();
            this.timerThread.start();
        }
        this.playList.currentAudioFile().togglePause();
    }

    /**
     * Stops the current song
     */
    private void stopCurrentSong() {
        this.playerThread.terminate();
        this.timerThread.terminate();
        this.playList.currentAudioFile().stop();
        this.playList.jumpToAudioFile(this.playList.iterator().next());
        this.playTimeLabel.setText(this.playList.currentAudioFile().formatPosition());
        this.playButton.setDisable(false);
        this.stopButton.setDisable(true);
        this.pauseButton.setDisable(true);
        this.nextButton.setDisable(false);
        this.playTimeLabel.setText(INITIAL_PLAY_TIME_LABEL);
        this.currentSongLabel.setText( NO_CURRENT_SONG);
    }

    /**
     * Plays the next song
     */
    private void playNextSong() {
        this.playerThread.terminate();
        this.timerThread.terminate();
        this.playList.currentAudioFile().stop();
        this.playList.nextSong();
        this.playCurrentSong();
    }

    /**
     * Creates an icon button
     * @param iconfile Filename of icon
     * @return The Button
     */
    public Button createButton(String iconfile) {
        Button button = null;
        try {
            URL url = getClass().getResource("/icons/" + iconfile);
            Image icon = new Image(url.toString());
            ImageView imageView = new ImageView(icon);
            imageView.setFitHeight(20);
            imageView.setFitWidth(20);
            button = new Button("", imageView);
            button.setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
            button.setStyle("-fx-background-color: #fff;");
        } catch (Exception e) {
            System.out.println("Image " + "icons/"
                    + iconfile + " not found!");
            System.exit(-1);
        }
        return button;
    }

    /**
     * Creates the filter pane
     *
     * @return The filter pane
     */
    private TitledPane createFilterPane() {
        TitledPane pane = new TitledPane();
        VBox vBox = new VBox();

        HBox searchBox = new HBox();
        Label searchLabel = new Label("Suchen:");
        this.searchTextField = new TextField();
        searchBox.getChildren().addAll(searchLabel, this.searchTextField);
        searchBox.setSpacing(20);

        HBox sortBox = new HBox();
        Label sortLabel = new Label("Sortieren:");
        this.sortChoiceBox = new ChoiceBox<SortCriterion>();
        this.sortChoiceBox.getItems().setAll(Arrays.asList(SortCriterion.values()));
        this.filterButton = new Button();
        this.filterButton.setText("Anzeigen");
        this.filterButton.setOnAction(e -> {
            this.playList.setSortCriterion((SortCriterion) this.sortChoiceBox.getValue());
            this.playList.setSearch(this.searchTextField.getText());
            this.songTable.refreshSongs();
        });
        sortBox.getChildren().addAll(sortLabel, this.sortChoiceBox, this.filterButton);
        sortBox.setSpacing(20);


        vBox.getChildren().addAll(searchBox, sortBox);
        vBox.setSpacing(10);
        pane.setContent(vBox);
        pane.setText("Filtern");
        return pane;
    }

    /**
     * Creates the song table
     *
     * @return The song table
     */
    private SongTable createSongTable() {
        return new SongTable(this.playList);
    }

    /**
     * Creates the bottom section
     *
     * @return The bottom section
     */
    private VBox createBottom() {
        VBox box = new VBox();
        box.setSpacing(20);
        box.getChildren().addAll(
                this.createSongInfo(),
                this.createPlayButtons()
        );
        return box;
    }

    /**
     * Creates the song info
     *
     * @return The song info
     */
    private VBox createSongInfo() {
        VBox box = new VBox();
        box.setSpacing(2);
        this.playListLabel = new Label();
        this.playListLabel.setText("Playlist: " + this.playlistPath);
        this.currentSongLabel = new Label();
        this.currentSongLabel.setText(NO_CURRENT_SONG);
        this.playTimeLabel = new Label();
        this.playTimeLabel.setText(INITIAL_PLAY_TIME_LABEL);
        box.getChildren().addAll(this.playListLabel, this.currentSongLabel, this.playTimeLabel);
        return box;
    }

    /**
     * Creates the action buttons
     *
     * @return The action buttons
     */
    private HBox createPlayButtons() {
        HBox box = new HBox();
        this.playButton = this.createButton("play.jpg");
        this.playButton.setOnAction(e -> {
            this.playCurrentSong();
        });
        this.pauseButton = this.createButton("pause.jpg");
        this.pauseButton.setDisable(true);
        this.pauseButton.setOnAction(e -> {
            this.pauseCurrentSong();
        });
        this.stopButton = this.createButton("stop.jpg");
        this.stopButton.setDisable(true);
        this.stopButton.setOnAction(e -> {
            this.stopCurrentSong();
        });
        this.nextButton = this.createButton("next.jpg");
        this.nextButton.setOnAction(e -> {
            this.playNextSong();
        });
        box.getChildren().addAll(
                this.playButton,
                this.pauseButton,
                this.stopButton,
                this.nextButton
        );
        return box;
    }

    /**
     * Updates the song info within Java FX Thread
     *
     * @param af The Audiofile that contains all information
     */
    private void updateSongInfo(AudioFile af) {
        Platform.runLater(() -> {
            this.playTimeLabel.setText(af.formatPosition());
        });
    }
}
