package Player;

import studiplayer.basic.WavParamReader;

public class WavFile extends SampledFile {

    public WavFile() {
        super();
        this.readAndSetDurationFromFile();
    }

    public WavFile(String path) throws Exception {
        super(path);
        this.readAndSetDurationFromFile();
    }

    public void readAndSetDurationFromFile() {
        WavParamReader.readParams(this.pathname);
        float frameRate = WavParamReader.getFrameRate();
        long numberOfFrames = WavParamReader.getNumberOfFrames();
        this.duration = WavFile.computeDuration(numberOfFrames, frameRate);
    }

    @Override
    public String toString() {
        return super.toString() +  " - " + this.formatDuration();
    }

    public static long computeDuration(long numberOfFrames, float frameRate) {
        return (long) (numberOfFrames * frameRate);
    }
}
