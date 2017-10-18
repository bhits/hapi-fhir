package ca.uhn.fhir.jpa.dao.r4;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import javax.persistence.EntityManager;

import org.hl7.fhir.instance.model.api.IIdType;
import org.hl7.fhir.r4.hapi.ctx.IValidationSupport;
import org.hl7.fhir.r4.model.*;
import org.junit.*;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.Transactional;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.jpa.config.TestR4WithoutLuceneConfig;
import ca.uhn.fhir.jpa.dao.*;
import ca.uhn.fhir.jpa.provider.r4.JpaSystemProviderR4;
import ca.uhn.fhir.jpa.search.ISearchCoordinatorSvc;
import ca.uhn.fhir.jpa.sp.ISearchParamPresenceSvc;
import ca.uhn.fhir.rest.param.StringParam;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;
import ca.uhn.fhir.util.TestUtil;

// @RunWith(SpringJUnit4ClassRunner.class)
// @ContextConfiguration(classes= {TestR4WithoutLuceneConfig.class})
// @SuppressWarnings("unchecked")
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { TestR4WithoutLuceneConfig.class })
public class FhirResourceDaoR4SearchWithLuceneDisabledTest extends BaseJpaTest {
	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(FhirResourceDaoR4SearchWithLuceneDisabledTest.class);

	@Autowired
	@Qualifier("myAllergyIntoleranceDaoR4")
	private IFhirResourceDao<AllergyIntolerance> myAllergyIntoleranceDao;
	@Autowired
	@Qualifier("myAppointmentDaoR4")
	private IFhirResourceDao<Appointment> myAppointmentDao;
	@Autowired
	@Qualifier("myAuditEventDaoR4")
	private IFhirResourceDao<AuditEvent> myAuditEventDao;
	@Autowired
	@Qualifier("myBundleDaoR4")
	private IFhirResourceDao<Bundle> myBundleDao;
	@Autowired
	@Qualifier("myCarePlanDaoR4")
	private IFhirResourceDao<CarePlan> myCarePlanDao;
	@Autowired
	@Qualifier("myCodeSystemDaoR4")
	private IFhirResourceDao<CodeSystem> myCodeSystemDao;
	@Autowired
	@Qualifier("myCompartmentDefinitionDaoR4")
	private IFhirResourceDao<CompartmentDefinition> myCompartmentDefinitionDao;
	@Autowired
	@Qualifier("myConceptMapDaoR4")
	private IFhirResourceDao<ConceptMap> myConceptMapDao;
	@Autowired
	@Qualifier("myConditionDaoR4")
	private IFhirResourceDao<Condition> myConditionDao;
	@Autowired
	protected DaoConfig myDaoConfig;
	@Autowired
	@Qualifier("myDeviceDaoR4")
	private IFhirResourceDao<Device> myDeviceDao;
	@Autowired
	@Qualifier("myDiagnosticReportDaoR4")
	private IFhirResourceDao<DiagnosticReport> myDiagnosticReportDao;
	@Autowired
	@Qualifier("myEncounterDaoR4")
	private IFhirResourceDao<Encounter> myEncounterDao;
	// @PersistenceContext()
	@Autowired
	private EntityManager myEntityManager;
	@Autowired
	private FhirContext myFhirCtx;
	@Autowired
	@Qualifier("myImmunizationDaoR4")
	private IFhirResourceDao<Immunization> myImmunizationDao;
	@Autowired
	@Qualifier("myLocationDaoR4")
	private IFhirResourceDao<Location> myLocationDao;
	@Autowired
	@Qualifier("myMediaDaoR4")
	private IFhirResourceDao<Media> myMediaDao;
	@Autowired
	@Qualifier("myMedicationDaoR4")
	private IFhirResourceDao<Medication> myMedicationDao;
	@Autowired
	@Qualifier("myMedicationRequestDaoR4")
	private IFhirResourceDao<MedicationRequest> myMedicationRequestDao;
	@Autowired
	@Qualifier("myNamingSystemDaoR4")
	private IFhirResourceDao<NamingSystem> myNamingSystemDao;
	@Autowired
	@Qualifier("myObservationDaoR4")
	private IFhirResourceDao<Observation> myObservationDao;
	@Autowired
	@Qualifier("myOperationDefinitionDaoR4")
	private IFhirResourceDao<OperationDefinition> myOperationDefinitionDao;
	@Autowired
	@Qualifier("myOrganizationDaoR4")
	private IFhirResourceDao<Organization> myOrganizationDao;
	@Autowired
	@Qualifier("myPatientDaoR4")
	private IFhirResourceDaoPatient<Patient> myPatientDao;
	@Autowired
	@Qualifier("myPractitionerDaoR4")
	private IFhirResourceDao<Practitioner> myPractitionerDao;
	@Autowired
	@Qualifier("myQuestionnaireDaoR4")
	private IFhirResourceDao<Questionnaire> myQuestionnaireDao;
	@Autowired
	@Qualifier("myQuestionnaireResponseDaoR4")
	private IFhirResourceDao<QuestionnaireResponse> myQuestionnaireResponseDao;
	@Autowired
	@Qualifier("myResourceProvidersR4")
	private Object myResourceProviders;
	@Autowired
	@Qualifier("myStructureDefinitionDaoR4")
	private IFhirResourceDao<StructureDefinition> myStructureDefinitionDao;
	@Autowired
	@Qualifier("mySubscriptionDaoR4")
	private IFhirResourceDaoSubscription<Subscription> mySubscriptionDao;
	@Autowired
	@Qualifier("mySubstanceDaoR4")
	private IFhirResourceDao<Substance> mySubstanceDao;
	@Autowired
	@Qualifier("mySystemDaoR4")
	private IFhirSystemDao<Bundle, Meta> mySystemDao;
	@Autowired
	@Qualifier("mySystemProviderR4")
	private JpaSystemProviderR4 mySystemProvider;

	@Autowired
	protected PlatformTransactionManager myTxManager;
	@Autowired
	protected ISearchParamPresenceSvc mySearchParamPresenceSvc;

	@Autowired
	@Qualifier("myJpaValidationSupportChainR4")
	private IValidationSupport myValidationSupport;
	@Autowired
	protected ISearchCoordinatorSvc mySearchCoordinatorSvc;
	@Autowired
	protected ISearchParamRegistry mySearchParamRegistry;

	@Before
	@Transactional()
	public void beforePurgeDatabase() {
		final EntityManager entityManager = this.myEntityManager;
		purgeDatabase(entityManager, myTxManager, mySearchParamPresenceSvc, mySearchCoordinatorSvc, mySearchParamRegistry);
	}

	@Before
	public void beforeResetConfig() {
		myDaoConfig.setHardSearchLimit(1000);
		myDaoConfig.setHardTagListLimit(1000);
		myDaoConfig.setIncludeLimit(2000);
	}

	@Override
	protected FhirContext getContext() {
		return myFhirCtx;
	}

	@Test
	public void testSearchWithRegularParam() throws Exception {
		String methodName = "testEverythingIncludesBackReferences";

		Organization org = new Organization();
		org.setName(methodName);
		IIdType orgId = myOrganizationDao.create(org, mySrd).getId().toUnqualifiedVersionless();

		SearchParameterMap map = new SearchParameterMap();
		map.add(Organization.SP_NAME, new StringParam(methodName));
		myOrganizationDao.search(map);
		
	}

	@Test
	public void testSearchWithContent() throws Exception {
		String methodName = "testEverythingIncludesBackReferences";

		Organization org = new Organization();
		org.setName(methodName);
		IIdType orgId = myOrganizationDao.create(org, mySrd).getId().toUnqualifiedVersionless();

		SearchParameterMap map = new SearchParameterMap();
		map.add(ca.uhn.fhir.rest.api.Constants.PARAM_CONTENT, new StringParam(methodName));
		try {
			myOrganizationDao.search(map).size();
			fail();
		} catch (InvalidRequestException e) {
			assertEquals("Fulltext search is not enabled on this service, can not process parameter: _content", e.getMessage());
		}
	}

	@Test
	public void testSearchWithText() throws Exception {
		String methodName = "testEverythingIncludesBackReferences";

		Organization org = new Organization();
		org.setName(methodName);
		IIdType orgId = myOrganizationDao.create(org, mySrd).getId().toUnqualifiedVersionless();

		SearchParameterMap map = new SearchParameterMap();
		map.add(ca.uhn.fhir.rest.api.Constants.PARAM_TEXT, new StringParam(methodName));
		try {
			myOrganizationDao.search(map).size();
			fail();
		} catch (InvalidRequestException e) {
			assertEquals("Fulltext search is not enabled on this service, can not process parameter: _text", e.getMessage());
		}
	}


	@AfterClass
	public static void afterClassClearContext() {
		TestUtil.clearAllStaticFieldsForUnitTest();
	}

}
