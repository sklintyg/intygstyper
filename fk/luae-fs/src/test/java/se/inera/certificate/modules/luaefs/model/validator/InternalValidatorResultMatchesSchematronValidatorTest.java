package se.inera.certificate.modules.luaefs.model.validator;

import com.google.common.base.Charsets;
import com.helger.schematron.svrl.SVRLHelper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.oclc.purl.dsdl.svrl.SchematronOutputType;
import se.inera.certificate.modules.fkparent.integration.RegisterCertificateValidator;
import se.inera.certificate.modules.fkparent.model.validator.InternalValidatorUtil;
import se.inera.certificate.modules.luaefs.model.internal.LuaefsUtlatande;
import se.inera.certificate.modules.luaefs.model.utils.Scenario;
import se.inera.certificate.modules.luaefs.model.utils.ScenarioFinder;
import se.inera.certificate.modules.luaefs.model.utils.ScenarioNotFoundException;
import se.inera.certificate.modules.luaefs.validator.InternalDraftValidator;
import se.inera.intyg.common.support.modules.service.WebcertModuleService;
import se.inera.intyg.common.support.modules.support.api.dto.ValidateDraftResponse;
import se.inera.intyg.common.support.modules.support.api.dto.ValidationStatus;
import se.riv.clinicalprocess.healthcond.certificate.registerCertificate.v2.RegisterCertificateType;

import javax.xml.transform.stream.StreamSource;
import java.io.ByteArrayInputStream;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;
import static se.inera.certificate.modules.fkparent.model.validator.InternalToSchematronValidatorTestUtil.getInternalValidationErrorString;
import static se.inera.certificate.modules.fkparent.model.validator.InternalToSchematronValidatorTestUtil.getNumberOfInternalValidationErrors;
import static se.inera.certificate.modules.fkparent.model.validator.InternalToSchematronValidatorTestUtil.getNumberOfTransportValidationErrors;
import static se.inera.certificate.modules.fkparent.model.validator.InternalToSchematronValidatorTestUtil.getTransportValidationErrorString;
import static se.inera.certificate.modules.fkparent.model.validator.InternalToSchematronValidatorTestUtil.getXmlFromModel;

/**
 * Data driven test that uses Scenario and ScenarioFinder along with the JUnit Parameterized test runner,
 * uses test data from internal/scenarios and transport/scenarios, so in order to create new tests, just add
 * corresponding json- and XML-files in these directories.
 * @author erik
 *
 */
@RunWith(Parameterized.class)
public class InternalValidatorResultMatchesSchematronValidatorTest {

    private static final String CORRECT_DIAGNOSKODSYSTEM = "ICD_10_SE";
    private static final String CORRECT_DIAGNOSKOD1 = "S47";
    private static final String CORRECT_DIAGNOSKOD2 = "Z731";

    private Scenario scenario;

    private boolean shouldFail;

    // Used for labeling tests.
    private static String name;

    @Mock
    private static WebcertModuleService mockModuleService;

    @InjectMocks
    private InternalValidatorUtil validatorUtil;

    private static InternalDraftValidator internalValidator;

    public InternalValidatorResultMatchesSchematronValidatorTest(String name, Scenario scenario, boolean shouldFail) {
        this.scenario = scenario;
        this.shouldFail = shouldFail;
        InternalValidatorResultMatchesSchematronValidatorTest.name = name;
    }

    /**
     * Process test data and supply it to the test.
     * The format for the test data needs to be: {name to display for current test, the scenario to test, expected outcome of the test}.
     * @return Collection<Object[]>
     * @throws ScenarioNotFoundException
     */
    @Parameters(name = "{index}: Scenario: {0}")
    public static Collection<Object[]> data() throws ScenarioNotFoundException {
        List<Object[]> retList = ScenarioFinder.getInternalScenarios("fail-*").stream()
                .map(u -> new Object[] { u.getName(), u, true })
                .collect(Collectors.toList());

        retList.addAll(
                ScenarioFinder.getInternalScenarios("pass-*").stream()
                        .map(u -> new Object[] { u.getName(), u, false })
                        .collect(Collectors.toList()));
        return retList;
    }

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        internalValidator = new InternalDraftValidator(validatorUtil);
    }

    @Test
    public void testScenarios() throws Exception {
        when(mockModuleService.validateDiagnosisCode(CORRECT_DIAGNOSKOD1, CORRECT_DIAGNOSKODSYSTEM)).thenReturn(true);
        when(mockModuleService.validateDiagnosisCode(CORRECT_DIAGNOSKOD2, CORRECT_DIAGNOSKODSYSTEM)).thenReturn(true);
        doInternalAndSchematronValidation(scenario, shouldFail);
    }

    /**
     * Perform internal and schematron validation on the supplied Scenario.
     * @param scenario
     * @param fail Whether the test should expect validation errors or not.
     * @throws Exception
     */
    private static void doInternalAndSchematronValidation(Scenario scenario, boolean fail) throws Exception {
        LuaefsUtlatande utlatandeFromJson = scenario.asInternalModel();

        ValidateDraftResponse internalValidationResponse = internalValidator.validateDraft(utlatandeFromJson);

        RegisterCertificateType intyg = scenario.asTransportModel();
        String convertedXML = getXmlFromModel(intyg);

        RegisterCertificateValidator validator = new RegisterCertificateValidator("aktivitetsersattning-fs.sch");
        SchematronOutputType result = validator.validateSchematron(new StreamSource(new ByteArrayInputStream(convertedXML.getBytes(Charsets.UTF_8))));

        String internalValidationErrors = getInternalValidationErrorString(internalValidationResponse);

        String transportValidationErrors = getTransportValidationErrorString(result);

        doAssertions(fail, internalValidationResponse, result, internalValidationErrors, transportValidationErrors);
    }

    private static void doAssertions(boolean fail, ValidateDraftResponse internalValidationResponse, SchematronOutputType result,
            String internalValidationErrors, String transportValidationErrors) {
        if (fail) {
            assertEquals(String.format("Scenario: %s\n Transport: %s \n Internal: %s\n Expected number of validation-errors to be the same.",
                    name, transportValidationErrors, internalValidationErrors),
                    getNumberOfTransportValidationErrors(result), getNumberOfInternalValidationErrors(internalValidationResponse));
            assertTrue(String.format("File: %s, Internal validation, expected ValidationStatus.INVALID",
                    name),
                    internalValidationResponse.getStatus().equals(ValidationStatus.INVALID));

            assertTrue(String.format("File: %s, Schematronvalidation, expected errors > 0",
                    name),
                    SVRLHelper.getAllFailedAssertions(result).size() > 0);

            System.out.println(String.format("Test: %s", name));
            System.out.println(String.format("InternalValidation-errors: %s",  internalValidationErrors));
            System.out.println(String.format("TransportValidation-errors: %s", transportValidationErrors));

        } else {
            assertTrue(String.format("File: %s, Internal validation, expected ValidationStatus.VALID \n Validation-errors: %s",
                    name, internalValidationErrors),
                    internalValidationResponse.getStatus().equals(ValidationStatus.VALID));
            assertTrue(String.format("File: %s, Schematronvalidation, expected 0 errors \n Validation-errors: %s",
                    name, transportValidationErrors),
                    SVRLHelper.getAllFailedAssertions(result).size() == 0);
        }
    }

}
