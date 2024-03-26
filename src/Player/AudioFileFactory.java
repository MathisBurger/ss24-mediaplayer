package Player;

/**
 * Handles AudioFile factory activities.
 */
public class AudioFileFactory {

    /**
     * Creates the correct type of audio file from the pathname.
     *
     * @param path The given path
     * @return An AudioFile instance.
     */
    public static AudioFile createAudioFile(String path) {
        String[] split = path.split("\\.");
        String extension = split[split.length-1];
        if (extension.equalsIgnoreCase("wav")) {
            return new WavFile(path);
        }
        if (extension.equalsIgnoreCase("ogg") || extension.equalsIgnoreCase("mp3")) {
            return new TaggedFile(path);
        }
        throw new RuntimeException("Unknown suffix for AudioFile \"" + path + "\"");
    }
}
