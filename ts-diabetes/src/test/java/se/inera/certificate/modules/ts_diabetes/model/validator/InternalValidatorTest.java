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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Collection;

import org.apache.commons.lang.StringUtils;
import org.junit.Before;
import org.junit.Test;

import se.inera.certificate.modules.ts_diabetes.model.internal.Utlatande;
import se.inera.certificate.modules.ts_diabetes.rest.dto.ValidateDraftResponseHolder;
import se.inera.certificate.modules.ts_diabetes.rest.dto.ValidationStatus;
import se.inera.certificate.modules.ts_diabetes.utils.Scenario;
import se.inera.certificate.modules.ts_diabetes.utils.ScenarioFinder;
import se.inera.certificate.modules.ts_diabetes.validator.Validator;

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
                            + StringUtils.join(validationResponse.getValidationErrors(), ", "), ValidationStatus.VALID,
                    validationResponse.getStatus());

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

            assertEquals(ValidationStatus.INVALID, validationResponse.getStatus());
        }
    }

    @Test
    public void testInvalidSynskarpa() throws Exception {
        Utlatande utlatande = ScenarioFinder.getInternalScenario("invalid-korrigerad-synskarpa").asInternalModel();
        ValidateDraftResponseHolder validationResponse = validator.validateInternal(utlatande);

        assertEquals("syn.vanster.medKorrektion", getSingleElement(validationResponse.getValidationErrors()).getField());
    }

    @Test
    public void testInvalidDateFormatHypoglykemi() throws Exception {
        Utlatande utlatande = ScenarioFinder.getInternalScenario("invalid-date-format-hypoglykemi").asInternalModel();
        ValidateDraftResponseHolder validationResponse = validator.validateInternal(utlatande);

        assertEquals("hypoglykemier.allvarligForekomstVakenTidObservationstid",
                getSingleElement(validationResponse.getValidationErrors()).getField());
    }

    @Test
    public void testInvalidDiabetesMissing() throws Exception {
        Utlatande utlatande = ScenarioFinder.getInternalScenario("invalid-missing-diabetes").asInternalModel();
        ValidateDraftResponseHolder validationResponse = validator.validateInternal(utlatande);

        assertEquals(3, validationResponse.getValidationErrors().size());
    }

    @Test
    public void testInvalidHypoglykemierMissing() throws Exception {
        Utlatande utlatande = ScenarioFinder.getInternalScenario("invalid-missing-hypoglykemier-kunskap")
                .asInternalModel();
        ValidateDraftResponseHolder validationResponse = validator.validateInternal(utlatande);

        assertEquals("hypoglykemier.kunskapOmAtgarder", getSingleElement(validationResponse.getValidationErrors())
                .getField());
    }

    /**
     * Utility method for getting a single element from a collection
     * 
     * @param collection
     *            the collection
     * @return a single element, throws IllegalArgumentException in case the collection contains more than one element
     */
    public static <T> T getSingleElement(Collection<T> collection) {
        if (collection.size() != 1) {
            throw new java.lang.IllegalArgumentException("Expected collection with exactly one element");
        }
        return collection.iterator().next();
    }
}
