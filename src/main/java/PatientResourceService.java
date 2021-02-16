import java.time.Instant;

import org.hl7.fhir.r4.model.Bundle;
import org.hl7.fhir.r4.model.Patient;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.rest.api.CacheControlDirective;
import ca.uhn.fhir.rest.client.api.IGenericClient;
import ca.uhn.fhir.rest.client.interceptor.LoggingInterceptor;
import ca.uhn.fhir.rest.gclient.StringClientParam;
import ca.uhn.fhir.util.StopWatch;

/**
 * Search for Patient resources
 */
public class PatientResourceService {
	
	private static StopWatch stopWatch = new StopWatch(Instant.now().toEpochMilli());
	private static long operationsCount;
	private static boolean isCacheable = true;
	
	/**
	 * 
	  * Search for patients resources
	 * 
	 * @param familyName Search for patients with the same family name
	 * @param sortParam The name of the parameter to be sorted
	 * @param count how many resources should be returned on a single page.
	 * @param isCahccheEnable 
	 * @return Return a Bundle of Patient Resource of the ResourceType informed
	 */
	public static Bundle getPatientResource(String familyName, StringClientParam sortParam, int count) {
		// Create a FHIR client
        FhirContext fhirContext = FhirContext.forR4();
        IGenericClient client = fhirContext.newRestfulGenericClient("http://hapi.fhir.org/baseR4");
        client.registerInterceptor(new LoggingInterceptor(false));
        
        //register StopWatch Interceptor
        client.registerInterceptor(stopWatch);
        
        // Search for resources
        Bundle response = client
                .search()
                .forResource(Patient.class.getSimpleName())
                .where(Patient.FAMILY.matches().value(familyName))
                .sort().ascending(sortParam)
                .returnBundle(Bundle.class)
                .count(count)
                .cacheControl(new CacheControlDirective().setNoCache(!isCacheable))
                .execute();
        
        operationsCount++;
        
        return response;
        
	}

	public static long getAverageResponseTime() {
		return stopWatch.getMillisPerOperation(operationsCount);
	}

	public static void restartStopWatch() {
		stopWatch.restart();
		operationsCount = 0;
		
	}

	public static void setCacheable(boolean b) {
		isCacheable = b;
		
	}
	
	public static boolean isCacheable() {
		return isCacheable;
	}

}
