package ca.uhn.fhir.jpa.term;

/*
 * #%L
 * HAPI FHIR JPA Server
 * %%
 * Copyright (C) 2014 - 2017 University Health Network
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */

import java.util.List;

import ca.uhn.fhir.rest.api.server.RequestDetails;

public interface IHapiTerminologyLoaderSvc {

	String LOINC_URL = "http://loinc.org";
	String SCT_URL = "http://snomed.info/sct";

	UploadStatistics loadLoinc(List<byte[]> theZipBytes, RequestDetails theRequestDetails);

	UploadStatistics loadSnomedCt(List<byte[]> theZipBytes, RequestDetails theRequestDetails);

	public static class UploadStatistics {
		private final int myConceptCount;

		public UploadStatistics(int theConceptCount) {
			myConceptCount = theConceptCount;
		}

		public int getConceptCount() {
			return myConceptCount;
		}

	}

}
