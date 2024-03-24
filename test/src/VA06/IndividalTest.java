package src.VA06;

import Player.AudioFile;
import VA06.Utils;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class IndividalTest {

    private char sep;
    private String root = "/";
    private static boolean messageShown = false;

    @Before
    /**
     * Setup operating system name and path separator.
     * Choose either Windows or Linux - see comments below!
     */
    public void setUp() {
        // to test Windows or Linux just use the corresponding lines in comments below
        // Windows: uncomment the next line
        //sep = Utils.emulateWindows();
        // Linux: uncomment the next line
        sep = Utils.emulateLinux();

        String osname = System.getProperty("os.name");
        if (!messageShown) {
            System.out.printf("VA06.TestParsePathname: os name is %s, using path separator '%c'\n", osname, sep);
            messageShown = true;
        }
        if (Utils.isWindows()) {
            root = "C:" + sep;
        }
    }
    @Test
    public void test1() {

        AudioFile af = new AudioFile("//your-tmp/part1//file.mp3/");
        System.out.println(af.getAuthor());
        Assert.assertEquals("", af.getAuthor());
    }

    @Test
    public void test2() {
        AudioFile af = new AudioFile("[]-");
        System.out.println(af.getFilename());
        Assert.assertEquals("[]-", af.getFilename());
    }
}
