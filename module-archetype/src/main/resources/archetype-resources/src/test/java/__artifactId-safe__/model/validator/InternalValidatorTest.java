#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
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
package ${package}.${artifactId-safe}.model.validator;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.apache.commons.lang.StringUtils;
import org.junit.Before;
import org.junit.Test;

import se.inera.certificate.modules.support.api.dto.ValidateDraftResponse;
import se.inera.certificate.modules.support.api.dto.ValidationStatus;
import ${package}.${artifactId-safe}.model.internal.Utlatande;
import ${package}.${artifactId-safe}.utils.Scenario;
import ${package}.${artifactId-safe}.utils.ScenarioFinder;
import ${package}.${artifactId-safe}.validator.Validator;

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
            ValidateDraftResponse validationResponse = validator.validateInternal(utlatande);

            assertEquals(
                    "Error in scenario " + scenario.getName() + "${symbol_escape}n"
                            + StringUtils.join(validationResponse.getValidationErrors(), ", "),
                    ValidationStatus.VALID, validationResponse.getStatus());

            assertTrue(
                    "Error in scenario " + scenario.getName() + "${symbol_escape}n"
                            + StringUtils.join(validationResponse.getValidationErrors(), ", "), validationResponse
                            .getValidationErrors().isEmpty());
        }
    }

    @Test
    public void testValidateWithErrors() throws Exception {
        for (Scenario scenario : ScenarioFinder.getInternalScenarios("invalid-*")) {

            Utlatande utlatande = scenario.asInternalModel();
            ValidateDraftResponse validationResponse = validator.validateInternal(utlatande);

            assertEquals(ValidationStatus.INVALID, validationResponse.getStatus());
        }
    }

    // TODO: Add more tests here
}
