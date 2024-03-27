package studiplayer.audio;

import studiplayer.basic.WavParamReader;

/**
 * WavFile for storing extra data
 */
public class WavFile extends SampledFile {

    public WavFile() {
        super();
        //this.readAndSetDurationFromFile();
    }

    public WavFile(String path) throws NotPlayableException {
        super(path);
        this.readAndSetDurationFromFile();
    }

    /**
     * Reads the duration from the wav file and sets it
     */
    public void readAndSetDurationFromFile() throws NotPlayableException {
        try {
            WavParamReader.readParams(this.pathname);
            float frameRate = WavParamReader.getFrameRate();
            long numberOfFrames = WavParamReader.getNumberOfFrames();
            this.duration = WavFile.computeDuration(numberOfFrames, frameRate);
        } catch (RuntimeException e) {
            throw new NotPlayableException(this.pathname, e);
        }
    }

    /**
     * Returns song as string with duration considered.
     *
     * @return Duration as string
     */
    @Override
    public String toString() {
        return super.toString() +  " - " + this.formatDuration();
    }

    /**
     * Computes the duration from values.
     *
     * @param numberOfFrames Total number of wav frames in the audio
     * @param frameRate The frame rate (frames / sec)
     * @return The total duration in Microsecs
     */
    public static long computeDuration(long numberOfFrames, float frameRate) {
        return (long)(numberOfFrames / frameRate * 1000000L);
    }
}
