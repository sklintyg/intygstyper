/*
 * Copyright (C) 2015 Inera AB (http://www.inera.se)
 *
 * This file is part of sklintyg (https://github.com/sklintyg).
 *
 * sklintyg is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * sklintyg is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package se.inera.intyg.intygstyper.ts_diabetes.model.validator;

import static org.junit.Assert.*;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.junit.Before;
import org.junit.Test;

import se.inera.intyg.intygstyper.ts_diabetes.utils.Scenario;
import se.inera.intyg.intygstyper.ts_diabetes.utils.ScenarioFinder;
import se.inera.intyg.intygstyper.ts_diabetes.utils.ScenarioNotFoundException;
import se.inera.intyg.intygstyper.ts_diabetes.validator.Validator;
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
            TSDiabetesIntyg utlatande = scenario.asTransportModel().getIntyg();
            List<String> validationResponse = validator.validateTransport(utlatande);

            assertTrue(
                    "Error in scenario " + scenario.getName() + "\n"
                            + StringUtils.join(validationResponse, ", "), validationResponse.isEmpty());
        }
    }

    @Test
    public void testValidateWithErrors() throws Exception {
        for (Scenario scenario : ScenarioFinder.getTransportScenarios("programmatic-invalid-*")) {

            TSDiabetesIntyg utlatande = scenario.asTransportModel().getIntyg();
            List<String> validationResponse = validator.validateTransport(utlatande);

            assertTrue("Expected validation error in test " + scenario.getName(), !validationResponse.isEmpty());
        }
    }
}
