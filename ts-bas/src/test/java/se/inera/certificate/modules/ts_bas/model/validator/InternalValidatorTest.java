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
package se.inera.certificate.modules.ts_bas.model.validator;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.apache.commons.lang.StringUtils;
import org.junit.Before;
import org.junit.Test;

import se.inera.certificate.modules.ts_bas.model.internal.Utlatande;
import se.inera.certificate.modules.ts_bas.rest.dto.ValidateDraftResponseHolder;
import se.inera.certificate.modules.ts_bas.rest.dto.ValidationStatus;
import se.inera.certificate.modules.ts_bas.utils.Scenario;
import se.inera.certificate.modules.ts_bas.utils.ScenarioFinder;
import se.inera.certificate.modules.ts_bas.validator.Validator;

public class InternalValidatorTest {

    private Validator validator;

    @Before
    public void setUp() throws Exception {
        validator = new Validator();
    }

    @Test
    public void testValidate() throws Exception {
        for (Scenario scenario : ScenarioFinder.getInternalScenarios("valid-*")) {
            Utlatande utlatande = scenario.asInternalModel();
            ValidateDraftResponseHolder validationResponse = validator.validateInternal(utlatande);

            assertEquals(
                    "Error in scenario " + scenario.getName() + "\n"
                            + StringUtils.join(validationResponse.getValidationErrors(), ", "),
                    ValidationStatus.COMPLETE, validationResponse.getStatus());

            assertTrue(
                    "Error in scenario " + scenario.getName() + "\n"
                            + StringUtils.join(validationResponse.getValidationErrors(), ", "), validationResponse
                            .getValidationErrors().isEmpty());

        }
    }

    @Test
    public void testValidateWithErrors() throws Exception {
        for (Scenario scenario : ScenarioFinder.getInternalScenarios("invalid-*")) {

            Utlatande utlatande = scenario.asInternalModel();
            ValidateDraftResponseHolder validationResponse = validator.validateInternal(utlatande);

            assertEquals(ValidationStatus.INCOMPLETE, validationResponse.getStatus());
        }
    }

    @Test
    public void testInvalidDiabetesTyp2MissingBehandling() throws Exception {
        Utlatande utlatande = ScenarioFinder.getInternalScenario("invalid-diabetes-typ2-missing-behandling")
                .asInternalModel();
        ValidateDraftResponseHolder validationResponse = validator.validateInternal(utlatande);

        assertTrue("Expecting one error!", validationResponse.getValidationErrors().size() == 1);

        assertEquals("diabetes.diabetesTyp", validationResponse.getValidationErrors().get(0).getField());

        assertEquals("Minst en behandling måste anges", validationResponse.getValidationErrors().get(0).getMessage());
    }

    @Test
    public void testInvalidSynskarpa() throws Exception {
        Utlatande utlatande = ScenarioFinder.getInternalScenario("invalid-korrigerad-synskarpa").asInternalModel();
        ValidateDraftResponseHolder validationResponse = validator.validateInternal(utlatande);

        assertTrue("Expected one and only one error", validationResponse.getValidationErrors().size() == 1);
        assertEquals("vansterOga.utanKorrektion", validationResponse.getValidationErrors().get(0).getField());
        assertEquals("ErrorCode", validationResponse.getValidationErrors().get(0).getMessage());
    }

    @Test
    public void testFunktionshinderBeskrivningMissing() throws Exception {
        Utlatande utlatande = ScenarioFinder.getInternalScenario("invalid-funktionshinder-beskrivning-missing")
                .asInternalModel();
        ValidateDraftResponseHolder validationResponse = validator.validateInternal(utlatande);

        assertTrue("Expected one and only one error", validationResponse.getValidationErrors().size() == 1);
        assertEquals("funktionsnedsattning.beskrivning", validationResponse.getValidationErrors().get(0).getField());
        assertEquals("Error", validationResponse.getValidationErrors().get(0).getMessage());
    }
}
