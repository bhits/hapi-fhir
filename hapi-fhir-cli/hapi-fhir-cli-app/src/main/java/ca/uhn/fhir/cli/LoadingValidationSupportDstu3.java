package ca.uhn.fhir.cli;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.rest.client.api.IGenericClient;
import ca.uhn.fhir.rest.client.api.ServerValidationModeEnum;
import ca.uhn.fhir.rest.server.exceptions.BaseServerResponseException;
import org.hl7.fhir.dstu3.hapi.ctx.IValidationSupport;
import org.hl7.fhir.dstu3.model.CodeSystem;
import org.hl7.fhir.dstu3.model.StructureDefinition;
import org.hl7.fhir.dstu3.model.ValueSet.ConceptSetComponent;
import org.hl7.fhir.dstu3.model.ValueSet.ValueSetExpansionComponent;
import org.hl7.fhir.instance.model.api.IBaseResource;

import java.util.Collections;
import java.util.List;

public class LoadingValidationSupportDstu3 implements IValidationSupport {

	private FhirContext myCtx = FhirContext.forDstu3();

	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(LoadingValidationSupportDstu3.class);

	@Override
	public ValueSetExpansionComponent expandValueSet(FhirContext theContext, ConceptSetComponent theInclude) {
		return null;
	}

	@Override
	public List<IBaseResource> fetchAllConformanceResources(FhirContext theContext) {
		return null;
	}

	@Override
	public CodeSystem fetchCodeSystem(FhirContext theContext, String theSystem) {
		return null;
	}

	@Override
	public <T extends IBaseResource> T fetchResource(FhirContext theContext, Class<T> theClass, String theUri) {
		String resName = myCtx.getResourceDefinition(theClass).getName();
		ourLog.info("Attempting to fetch {} at URL: {}", resName, theUri);
		
		myCtx.getRestfulClientFactory().setServerValidationMode(ServerValidationModeEnum.NEVER);
		IGenericClient client = myCtx.newRestfulGenericClient("http://example.com");
		
		T result;
		try {
			result = client.read(theClass, theUri);
		} catch (BaseServerResponseException e) {
			throw new CommandFailureException("FAILURE: Received HTTP " + e.getStatusCode() + ": " + e.getMessage());
		}
		ourLog.info("Successfully loaded resource");
		return result;
	}

	@Override
	public StructureDefinition fetchStructureDefinition(FhirContext theCtx, String theUrl) {
		return null;
	}

	@Override
	public boolean isCodeSystemSupported(FhirContext theContext, String theSystem) {
		return false;
	}

	@Override
	public CodeValidationResult validateCode(FhirContext theContext, String theCodeSystem, String theCode, String theDisplay) {
		return null;
	}

	@Override
	public List<StructureDefinition> fetchAllStructureDefinitions(FhirContext theContext) {
		return Collections.emptyList();
	}

}
