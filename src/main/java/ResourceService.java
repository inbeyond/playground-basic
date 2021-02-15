import org.hl7.fhir.r4.model.Bundle;
import org.hl7.fhir.r4.model.Patient;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.rest.client.api.IGenericClient;
import ca.uhn.fhir.rest.client.interceptor.LoggingInterceptor;
import ca.uhn.fhir.rest.gclient.StringClientParam;

/**
 * Search for Client resources
 */
public class ResourceService {
	
	/**
	 * 
	 * Search for client resources
	 * 
	 * @param resourceType
	 * @param familyName The name of the type of the resource
	 * @param sortParam The name of the parameter to be sorted
	 * @return Return a Bundle of Client Resource of the ResourceType informed
	 */
	public static Bundle getClientResource(String resourceType, String familyName, StringClientParam sortParam) {
		// Create a FHIR client
        FhirContext fhirContext = FhirContext.forR4();
        IGenericClient client = fhirContext.newRestfulGenericClient("http://hapi.fhir.org/baseR4");
        client.registerInterceptor(new LoggingInterceptor(false));

        // Search for resources
        Bundle response = client
                .search()
                .forResource(resourceType)
                .where(Patient.FAMILY.matches().value(familyName))
                .sort().ascending(sortParam)
                .returnBundle(Bundle.class)
                .execute();
        
		return response;
	}

}
