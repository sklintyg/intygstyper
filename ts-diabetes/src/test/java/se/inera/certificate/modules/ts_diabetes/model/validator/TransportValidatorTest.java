/**
 * Copyright (C) 2013 Inera AB (http://www.inera.se)
 *
 * This file is part of Inera Certificate Modules (http://code.google.com/p/inera-certificate-modules).
 *
 * Inera Certificate Modules is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * Inera Certificate Modules is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package se.inera.certificate.modules.ts_diabetes.model.validator;

import static org.junit.Assert.assertTrue;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.junit.Before;
import org.junit.Test;

import se.inera.certificate.modules.ts_diabetes.utils.Scenario;
import se.inera.certificate.modules.ts_diabetes.utils.ScenarioFinder;
import se.inera.certificate.modules.ts_diabetes.validator.Validator;
import se.inera.certificate.ts_diabetes.model.v1.Utlatande;

public class TransportValidatorTest {

    private Validator validator;

    @Before
    public void setUp() throws Exception {
        validator = new Validator();
    }

    @Test
    public void testValidate() throws Exception {
        for (Scenario scenario : ScenarioFinder.getTransportScenarios("valid-*")) {
            Utlatande utlatande = scenario.asTransportModel();
            List<String> validationErrors = validator.validateTransport(utlatande);

            assertTrue("Error in scenario " + scenario.getName() + "\n" + StringUtils.join(validationErrors, ", "),
                    validationErrors.isEmpty());
        }
    }

    @Test
    public void testValidateWithErrors() throws Exception {
        for (Scenario scenario : ScenarioFinder.getTransportScenarios("programatic-invalid-*")) {
            List<String> validationErrors = validator.validateTransport(scenario.asTransportModel());

            assertTrue("Expected error in scenario " + scenario.getName() + "\n" + StringUtils.join(validationErrors, ", "),
                    !validationErrors.isEmpty());
        }
    }
}
