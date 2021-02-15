import java.util.Date;

import org.hl7.fhir.r4.model.Bundle;
import org.hl7.fhir.r4.model.Patient;
import org.hl7.fhir.r4.model.StringType;

public class SampleClient {
	

    public static void main(String[] theArgs) {
    	
    	//// Search for Patient resources Sorted by the patient's first name
    	Bundle response = ResourceService.getClientResource(Patient.class.getSimpleName(), "SMITH", Patient.NAME);
    	
    	response.getEntry().forEach(entry -> {
    		Patient patient = (Patient) entry.getResource();
    		printName(patient);
    	});
    	
    }
    
    /**
     * prints the first and last name, and birth date of each Patient to the screen
     */
    private static void printName(Patient patient) {
    		
		if(patient.getName().listIterator().hasNext()) {
			StringType nameGiven = patient.getName().listIterator().next().getGiven().iterator().next();
			String nameFamily = patient.getName().listIterator().next().getFamily();
			Date birthDate = patient.getBirthDate();
			
			System.out.println(nameGiven+", "+nameFamily+", "+ birthDate);
		}
    	
    }

	

}
