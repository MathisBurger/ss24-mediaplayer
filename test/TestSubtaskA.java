import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import Player.TaggedFile;
import Player.WavFile;
import org.junit.Before;
import org.junit.Test;

import studiplayer.basic.BasicPlayer;

public class TestSubtaskA {
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
		// sep = Utils.emulateWindows();
		// Linux: uncomment the next line
		sep = Utils.emulateLinux();

		String osname = System.getProperty("os.name");
		if (!messageShown) {
			System.out.printf("TestParsePathname: os name is %s, using path separator '%c'\n", osname, sep);
			messageShown = true;
		}
		if (Utils.isWindows()) {
			root = "C:" + sep;
		}
	}

	@SuppressWarnings("unused")
	@Test
	public void testReadablePathname() {
		try {
			WavFile wavFile = new WavFile("should.not.be.readable.file");
			// Error: no RuntimeException thrown!
			fail("File is not readable because it does not exist - you should throw a Runtime Exception!");
		} catch (Exception e) {
			// no error, we should catch a Runtime Exception!
		}
		try {
			TaggedFile tf1 = new TaggedFile("audiofiles////beethoven-ohne-album.mp3");
			TaggedFile tf2 = new TaggedFile("audiofiles\\\\\\beethoven-ohne-album.mp3");
		} catch (Exception e) {
			fail("File is readable!");
		}
	}

	@Test
	public void testPlaySong() throws InterruptedException {
		TaggedFile tf1 = new TaggedFile("audiofiles/Rock 812.mp3");
		// call play() in its own thread since it would otherwise block until
		// the song is played to the end
		new Thread(() -> tf1.play()).start();
		Thread.sleep(3000);
		BasicPlayer.stop();
		long position = BasicPlayer.getPosition();
		assertTrue("Song did not play for 3 seconds!", 3000000L < position);
	}

	@Test
	public void testPauseSong() throws InterruptedException {
		TaggedFile tf1 = new TaggedFile("audiofiles/Rock 812.mp3");
		// call play() in its own thread since it would otherwise block until
		// the song is played to the end
		new Thread(() -> tf1.play()).start();
		Thread.sleep(2000);
		tf1.togglePause();
		Thread.sleep(1000);
		tf1.togglePause();
		Thread.sleep(2000);
		long position = BasicPlayer.getPosition();
		// System.out.println(position);
		assertTrue("Song did not play for 4 seconds!", 4000000L < position);
	}

	@Test
	public void testStopSong() throws InterruptedException {
		TaggedFile tf1 = new TaggedFile("audiofiles/Rock 812.mp3");
		// call play() in its own thread since it would otherwise block until
		// the song is played to the end
		new Thread(() -> tf1.play()).start();
		Thread.sleep(3000);
		tf1.stop();
		long position = BasicPlayer.getPosition();
		// System.out.println(position);
		assertTrue("Song did not play for 3 seconds!", 3000000L < position);
	}
}
