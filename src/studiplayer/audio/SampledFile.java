package studiplayer.audio;

import studiplayer.basic.BasicPlayer;

import java.util.concurrent.TimeUnit;

/**
 * Sampled file that implements functionality to
 * play song via basic player
 */
public abstract class SampledFile extends AudioFile {

    /**
     * Inherited constructor
     */
    public SampledFile() {
        super();
    }

    /**
     * Inherited constructor for initialization
     *
     * @param path The path to a file
     */
    public SampledFile(String path) throws NotPlayableException {
        super(path);
    }

    /**
     * Plays the song
     */
    @Override
    public void play() throws NotPlayableException {
        try {
            BasicPlayer.play(this.pathname);
        } catch (RuntimeException e) {
            throw new NotPlayableException(this.pathname, e);
        }
    }

    /**
     * Toggles pause of the song
     */
    @Override
    public void togglePause() {
        BasicPlayer.togglePause();
    }

    /**
     * Stops the song
     */
    @Override
    public void stop() {
        BasicPlayer.stop();
    }

    /**
     * Formats the duration.
     *
     * @return Duration in format mm:ss
     */
    @Override
    public String formatDuration() {
        return SampledFile.timeFormatter(this.duration);
    }

    /**
     * Formats the position.
     *
     * @return Position in format mm:ss
     */
    @Override
    public String formatPosition() {
        return SampledFile.timeFormatter(BasicPlayer.getPosition());
    }

    /**
     * Formats microsecs to format mm:ss
     *
     * @param timeInMicroSeconds The amount of microsecs
     * @return Time format as mm:ss
     */
    public static String timeFormatter(long timeInMicroSeconds) {
        if (timeInMicroSeconds < 0 || timeInMicroSeconds >= TimeUnit.SECONDS.toMicros(100*60)) {
            throw new RuntimeException("Invalid time");
        }
        long minutes = TimeUnit.MICROSECONDS.toMinutes(timeInMicroSeconds);
        long seconds = TimeUnit.MICROSECONDS.toSeconds(timeInMicroSeconds) - minutes*60;
        return String.format(
                "%02d:%02d",
                minutes,
                seconds
        );
    }

    /**
     * Gets the total duration of the song
     *
     * @return Total duration
     */
    public long getDuration() {
        return this.duration;
    }
}
