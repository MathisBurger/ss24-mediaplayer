package studiplayer.test;

import org.junit.Test;
import studiplayer.audio.*;

import java.util.Arrays;
import java.util.List;

public class OwnTest {

    @Test
    public void test() throws Exception {
        List<AudioFile> list = Arrays.asList(
                new TaggedFile("audiofiles/kein.wav.sondern.ogg"),
              new WavFile("audiofiles/wellenmeister - tranquility.wav"),
                new TaggedFile("audiofiles/Rock 812.mp3"),
                new TaggedFile("audiofiles/wellenmeister_awakening.ogg")

        );
        PlayList pl = new PlayList();
        for (AudioFile af : list) {
            System.out.println("Album: " + af.getAlbum());
            pl.add(af);
        }
        pl.setSortCriterion(SortCriterion.ALBUM);
        System.out.println(pl.toString());
    }
}
