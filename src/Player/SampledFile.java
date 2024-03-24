package Player;

import studiplayer.basic.BasicPlayer;

import java.util.concurrent.TimeUnit;

/**
 * Sampled file abstract class
 */
public abstract class SampledFile extends AudioFile {

    protected long duration;

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
    public SampledFile(String path) throws Exception {
        super(path);
    }

    @Override
    public void play() {
        BasicPlayer.play(this.pathname);
    }

    @Override
    public void togglePause() {
        BasicPlayer.togglePause();
    }

    @Override
    public void stop() {
        BasicPlayer.stop();
    }

    @Override
    public String formatDuration() {
        return SampledFile.timeFormatter(this.duration);
    }

    @Override
    public String formatPosition() {
        return SampledFile.timeFormatter(BasicPlayer.getPosition());
    }

    public static String timeFormatter(long timeInMicroSeconds) {
        if (timeInMicroSeconds < 0) {
            throw new RuntimeException("Invalid time");
        }
        long minutes = TimeUnit.MICROSECONDS.toMinutes(timeInMicroSeconds);
        long seconds = TimeUnit.MICROSECONDS.toSeconds(timeInMicroSeconds) - minutes*60;
        return String.format(
                "%d:%d",
                minutes,
                seconds
        );
    }

    public long getDuration() {
        return this.duration;
    }
}
