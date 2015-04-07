package se.inera.certificate.modules.ts_bas.model.validator;

import static org.junit.Assert.assertTrue;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.junit.Before;
import org.junit.Test;

import se.inera.certificate.modules.ts_bas.utils.Scenario;
import se.inera.certificate.modules.ts_bas.utils.ScenarioFinder;
import se.inera.certificate.modules.ts_bas.utils.ScenarioNotFoundException;
import se.inera.certificate.modules.ts_bas.validator.TsBasValidator;
import se.inera.intygstjanster.ts.services.v1.TSBasIntyg;

public class TransportValidatorTest {
    private TsBasValidator validator;

    @Before
    public void setUp() throws Exception {
        validator = new TsBasValidator();
    }

    @Test
    public void testValidate() throws ScenarioNotFoundException {
        for (Scenario scenario : ScenarioFinder.getTransportScenarios("valid-*")) {
            TSBasIntyg utlatande = scenario.asTransportModel();
            List<String> validationResponse = validator.validateTransport(utlatande);

            assertTrue(
                    "Error in scenario " + scenario.getName() + "\n"
                            + StringUtils.join(validationResponse, ", "), validationResponse.isEmpty());
        }
    }

    @Test
    public void testValidateWithErrors() throws Exception {
        for (Scenario scenario : ScenarioFinder.getTransportScenarios("programmatic-invalid-*")) {

            TSBasIntyg utlatande = scenario.asTransportModel();
            List<String> validationResponse = validator.validateTransport(utlatande);

            assertTrue("Expected validation error in test " + scenario.getName(), !validationResponse.isEmpty());
        }
    }

}
