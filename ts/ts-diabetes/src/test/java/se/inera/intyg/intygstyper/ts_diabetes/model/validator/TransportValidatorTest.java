/*
 * Copyright (C) 2016 Inera AB (http://www.inera.se)
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

import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.Test;

import com.google.common.base.Joiner;

import se.inera.intyg.intygstyper.ts_diabetes.utils.Scenario;
import se.inera.intyg.intygstyper.ts_diabetes.utils.ScenarioFinder;
import se.inera.intyg.intygstyper.ts_diabetes.utils.ScenarioNotFoundException;
import se.inera.intyg.intygstyper.ts_diabetes.validator.transport.TransportValidatorInstance;
import se.inera.intygstjanster.ts.services.v1.TSDiabetesIntyg;

public class TransportValidatorTest {

    private TransportValidatorInstance validator = new TransportValidatorInstance();

    @Test
    public void testValidate() throws ScenarioNotFoundException {
        for (Scenario scenario : ScenarioFinder.getTransportScenarios("valid-*")) {
            TSDiabetesIntyg utlatande = scenario.asTransportModel().getIntyg();
            List<String> validationResponse = validator.validate(utlatande);

            assertTrue(
                    "Error in scenario " + scenario.getName() + "\n"
                            + Joiner.on(", ").join(validationResponse), validationResponse.isEmpty());
        }
    }

    @Test
    public void testValidateWithErrors() throws Exception {
        for (Scenario scenario : ScenarioFinder.getTransportScenarios("programmatic-invalid-*")) {

            TSDiabetesIntyg utlatande = scenario.asTransportModel().getIntyg();
            List<String> validationResponse = validator.validate(utlatande);

            assertTrue("Expected validation error in test " + scenario.getName(), !validationResponse.isEmpty());
        }
    }
}
