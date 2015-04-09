package se.inera.certificate.modules.ts_diabetes.model.validator;

import static org.junit.Assert.*;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.junit.Before;
import org.junit.Test;

import se.inera.certificate.modules.ts_diabetes.utils.Scenario;
import se.inera.certificate.modules.ts_diabetes.utils.ScenarioFinder;
import se.inera.certificate.modules.ts_diabetes.utils.ScenarioNotFoundException;
import se.inera.certificate.modules.ts_diabetes.validator.Validator;
import se.inera.intygstjanster.ts.services.v1.TSDiabetesIntyg;

public class TransportValidatorTest {
    private Validator validator;

    @Before
    public void setUp() throws Exception {
        validator = new Validator();
    }

    @Test
    public void testValidate() throws ScenarioNotFoundException {
        for (Scenario scenario : ScenarioFinder.getTransportScenarios("valid-*")) {
            TSDiabetesIntyg utlatande = scenario.asTransportModel();
            List<String> validationResponse = validator.validateTransport(utlatande);

            assertTrue(
                    "Error in scenario " + scenario.getName() + "\n"
                            + StringUtils.join(validationResponse, ", "), validationResponse.isEmpty());
        }
    }

    @Test
    public void testValidateWithErrors() throws Exception {
        for (Scenario scenario : ScenarioFinder.getTransportScenarios("programmatic-invalid-*")) {

            TSDiabetesIntyg utlatande = scenario.asTransportModel();
            List<String> validationResponse = validator.validateTransport(utlatande);

            assertTrue("Expected validation error in test " + scenario.getName(), !validationResponse.isEmpty());
        }
    }
}
