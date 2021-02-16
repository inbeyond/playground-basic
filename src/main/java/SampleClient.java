import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Date;
import java.util.stream.Stream;

import org.hl7.fhir.r4.model.Bundle;
import org.hl7.fhir.r4.model.Patient;
import org.hl7.fhir.r4.model.StringType;

public class SampleClient {

	public static void main(String[] theArgs) {

		try {
			// prints the first and last name, and birth date of each Patient with the name
			// "Smith" to the screen
			printNameBirthDate("SMITH");

			Path path = createTextFile();

			searchLastNamesFromFile(path);

			searchLastNamesFromFileThreeTimes(path);

		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	/**
	 * 
	 * Run this loop three times, printing the average response time for each loop.
	 * The first two times the loop should run as described above. The third time
	 * the loop of 20 searches is run, the searches should be performed with caching
	 * disabled.
	 * 
	 * @param path Path of the file with the last names
	 * @throws IOException
	 */
	private static void searchLastNamesFromFileThreeTimes(Path path) throws IOException {
		for (int i = 0; i < 3; i++) {
			if (i == 2) {
				PatientResourceService.setCacheable(false);
			}
			searchLastNamesFromFile(path);
		}
	}

	/**
	 * 
	 * Reads in the contents of the file and for each last name queries for patients
	 * with that last name
	 * 
	 * @param path Path of the file with the last names
	 * @throws IOException
	 */
	private static void searchLastNamesFromFile(Path path) throws IOException {

		try (Stream<String> stream = Files.lines(path)) {
			PatientResourceService.restartStopWatch();
			stream.forEach(lastName -> PatientResourceService.getPatientResource(lastName, Patient.NAME, 20));
			System.out.println("Average response time = " + PatientResourceService.getAverageResponseTime());
		}
	}

	/**
	 * Create a text file containing 20 different last names
	 * 
	 * @throws IOException
	 */
	private static Path createTextFile() throws IOException {

		Path path = Paths.get("lastNames.txt").toAbsolutePath();

		try (BufferedWriter writer = Files.newBufferedWriter(path)) {
			Bundle response = PatientResourceService.getPatientResource(null, Patient.NAME, 20);

			StringBuffer lastNames = new StringBuffer();
			response.getEntry().forEach(entry -> {
				Patient patient = (Patient) entry.getResource();
				if (patient.getName().listIterator().hasNext()) {
					lastNames.append(patient.getName().listIterator().next().getFamily() + "\r\n");

				}
			});

			writer.write(lastNames.toString());
		}

		return path;

	}

	/**
	 * 
	 * prints the first and last name, and birth date of each Patient with the last
	 * name entered
	 * 
	 * @param lastName
	 */
	private static void printNameBirthDate(String lastName) {
		// Search for Patient resources Sorted by the patient's first name
		Bundle response = PatientResourceService.getPatientResource(lastName, Patient.NAME, 10);

		response.getEntry().forEach(entry -> {
			Patient patient = (Patient) entry.getResource();
			if (patient.getName().listIterator().hasNext()) {
				StringType nameGiven = patient.getName().listIterator().next().getGiven().iterator().next();
				String nameFamily = patient.getName().listIterator().next().getFamily();
				Date birthDate = patient.getBirthDate();

				System.out.println(nameGiven + ", " + nameFamily + ", " + birthDate);
			}
		});

	}

}
