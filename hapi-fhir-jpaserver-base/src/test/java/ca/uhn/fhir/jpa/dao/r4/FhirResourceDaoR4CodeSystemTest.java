package ca.uhn.fhir.jpa.dao.r4;

import static org.junit.Assert.assertNotEquals;

import java.nio.charset.StandardCharsets;

import org.apache.commons.io.IOUtils;
import org.hl7.fhir.r4.model.CodeSystem;
import org.junit.AfterClass;
import org.junit.Test;

import ca.uhn.fhir.jpa.term.BaseHapiTerminologySvc;
import ca.uhn.fhir.util.TestUtil;

public class FhirResourceDaoR4CodeSystemTest extends BaseJpaR4Test {
	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(FhirResourceDaoR4CodeSystemTest.class);

	@AfterClass
	public static void afterClassClearContext() {
		TestUtil.clearAllStaticFieldsForUnitTest();
		BaseHapiTerminologySvc.setForceSaveDeferredAlwaysForUnitTest(false);
	}

	
	@Test
	public void testIndexContained() throws Exception {
		BaseHapiTerminologySvc.setForceSaveDeferredAlwaysForUnitTest(true);
		
		String input = IOUtils.toString(getClass().getResource("/r4/codesystem_complete.json"), StandardCharsets.UTF_8);
		CodeSystem cs = myFhirCtx.newJsonParser().parseResource(CodeSystem.class, input);
		myCodeSystemDao.create(cs, mySrd);

		
		mySystemDao.markAllResourcesForReindexing();

		int outcome = mySystemDao.performReindexingPass(100);
		assertNotEquals(-1, outcome); // -1 means there was a failure
		
		myTermSvc.saveDeferred();
		
	}

	
}
