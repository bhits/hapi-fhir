package ca.uhn.fhir.parser;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.util.TestUtil;
import org.hl7.fhir.r4.model.Bundle;
import org.hl7.fhir.r4.model.Patient;
import org.junit.AfterClass;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashSet;
import java.util.Set;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.core.IsNot.not;
import static org.junit.Assert.*;

public class JsonParserR4Test {
	private static final Logger ourLog = LoggerFactory.getLogger(JsonParserR4Test.class);
	private static FhirContext ourCtx = FhirContext.forR4();

	@Test
	public void testExcludeNothing() {
		IParser parser = ourCtx.newJsonParser().setPrettyPrint(true);
		Set<String> excludes = new HashSet<>();
//		excludes.add("*.id");
		parser.setDontEncodeElements(excludes);

		Bundle b = createBundleWithPatient();

		String encoded = parser.encodeResourceToString(b);
		ourLog.info(encoded);

		assertThat(encoded, containsString("BUNDLEID"));
		assertThat(encoded, containsString("http://FOO"));
		assertThat(encoded, containsString("PATIENTID"));
		assertThat(encoded, containsString("http://BAR"));
		assertThat(encoded, containsString("GIVEN"));

		b = parser.parseResource(Bundle.class, encoded);

		assertEquals("BUNDLEID", b.getIdElement().getIdPart());
		assertEquals("Patient/PATIENTID", ((Patient) b.getEntry().get(0).getResource()).getId());
		assertEquals("GIVEN", ((Patient) b.getEntry().get(0).getResource()).getNameFirstRep().getGivenAsSingleString());
	}

	@Test
	public void testExcludeStarDotStuff() {
		IParser parser = ourCtx.newJsonParser().setPrettyPrint(true);
		Set<String> excludes = new HashSet<>();
		excludes.add("*.id");
		excludes.add("*.meta");
		parser.setDontEncodeElements(excludes);

		Bundle b = createBundleWithPatient();

		String encoded = parser.encodeResourceToString(b);
		ourLog.info(encoded);

		assertThat(encoded, not(containsString("BUNDLEID")));
		assertThat(encoded, not(containsString("http://FOO")));
		assertThat(encoded, not(containsString("PATIENTID")));
		assertThat(encoded, not(containsString("http://BAR")));
		assertThat(encoded, containsString("GIVEN"));

		b = parser.parseResource(Bundle.class, encoded);

		assertNotEquals("BUNDLEID", b.getIdElement().getIdPart());
		assertNotEquals("Patient/PATIENTID", ((Patient) b.getEntry().get(0).getResource()).getId());
		assertEquals("GIVEN", ((Patient) b.getEntry().get(0).getResource()).getNameFirstRep().getGivenAsSingleString());
	}

	@Test
	public void testExcludeRootStuff() {
		IParser parser = ourCtx.newJsonParser().setPrettyPrint(true);
		Set<String> excludes = new HashSet<>();
		excludes.add("id");
		excludes.add("meta");
		parser.setDontEncodeElements(excludes);

		Bundle b = createBundleWithPatient();

		String encoded = parser.encodeResourceToString(b);
		ourLog.info(encoded);

		assertThat(encoded, not(containsString("BUNDLEID")));
		assertThat(encoded, not(containsString("http://FOO")));
		assertThat(encoded, (containsString("PATIENTID")));
		assertThat(encoded, (containsString("http://BAR")));
		assertThat(encoded, containsString("GIVEN"));

		b = parser.parseResource(Bundle.class, encoded);

		assertNotEquals("BUNDLEID", b.getIdElement().getIdPart());
		assertEquals("Patient/PATIENTID", ((Patient) b.getEntry().get(0).getResource()).getId());
		assertEquals("GIVEN", ((Patient) b.getEntry().get(0).getResource()).getNameFirstRep().getGivenAsSingleString());
	}

	private Bundle createBundleWithPatient() {
		Bundle b = new Bundle();
		b.setId("BUNDLEID");
		b.getMeta().addProfile("http://FOO");

		Patient p = new Patient();
		p.setId("PATIENTID");
		p.getMeta().addProfile("http://BAR");
		p.addName().addGiven("GIVEN");
		b.addEntry().setResource(p);
		return b;
	}

	@AfterClass
	public static void afterClassClearContext() {
		TestUtil.clearAllStaticFieldsForUnitTest();
	}

}
