import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;

import org.junit.Test;

public class SampleClientTest {

	@Test
	public void testPrintNameBirthDate() {

		SampleClient.printNameBirthDate("SMITH");

	}

	@Test
	public void testCreateTextFile() {

		try {
			Path path = SampleClient.createTextFile("lastNames.txt");
			assertTrue(path.toFile().exists());
		} catch (IOException e) {
			fail("IOException: " + e);
		}
	}

	@Test
	public void testSearchLastNamesFromFile() {
		try {
			File file = new File("lastNames.txt");
			SampleClient.searchLastNamesFromFile(file.toPath());
		} catch (IOException e) {
			fail("IOException: " + e);
		}
	}

	@Test
	public void testSearchLastNamesFromFileThreeTimes() {
		try {
			File file = new File("lastNames.txt");
			SampleClient.searchLastNamesFromFileThreeTimes(file.toPath());
		} catch (IOException e) {
			fail("IOException: " + e);
		}
	}

}
