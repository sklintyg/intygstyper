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
package se.inera.intyg.intygstyper.ts_diabetes.model.validator;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Collection;

import org.apache.commons.lang.StringUtils;
import org.junit.Before;
import org.junit.Test;

import se.inera.intyg.common.support.modules.support.api.dto.ValidateDraftResponse;
import se.inera.intyg.common.support.modules.support.api.dto.ValidationStatus;
import se.inera.intyg.intygstyper.ts_diabetes.model.internal.Utlatande;
import se.inera.intyg.intygstyper.ts_diabetes.utils.Scenario;
import se.inera.intyg.intygstyper.ts_diabetes.utils.ScenarioFinder;
import se.inera.intyg.intygstyper.ts_diabetes.validator.Validator;

public class InternalValidatorTest {

    private static final int T3 = 3;
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
            ValidateDraftResponse validationResponse = validator.validateInternal(utlatande);

            assertEquals(String.format("Error in test: %s", scenario.getName()), ValidationStatus.INVALID, validationResponse.getStatus());
        }
    }

    @Test
    public void testInvalidSynskarpa() throws Exception {
        Utlatande utlatande = ScenarioFinder.getInternalScenario("invalid-korrigerad-synskarpa").asInternalModel();
        ValidateDraftResponse validationResponse = validator.validateInternal(utlatande);

        assertEquals("syn.vanster.medKorrektion", getSingleElement(validationResponse.getValidationErrors()).getField());
    }

    @Test
    public void testInvalidOgonlakarintygSaknasCorrectSortOrder() throws Exception {
        Utlatande utlatande = ScenarioFinder.getInternalScenario("invalid-missing-ogonlakarintyg").asInternalModel();
        ValidateDraftResponse validationResponse = validator.validateInternal(utlatande);
        assertEquals(5, validationResponse.getValidationErrors().size());
        int index = 0;
        assertEquals("syn.provningUtanAnmarkning", validationResponse.getValidationErrors().get(index++).getField());
        assertEquals("syn.hoger.utanKorrektion", validationResponse.getValidationErrors().get(index++).getField());
        assertEquals("syn.vanster.utanKorrektion", validationResponse.getValidationErrors().get(index++).getField());
        assertEquals("syn.binokulart.utanKorrektion", validationResponse.getValidationErrors().get(index++).getField());
        assertEquals("syn.diplopi", validationResponse.getValidationErrors().get(index++).getField());
    }

    @Test
    public void testInvalidAllmanDiabetesSaknasCorrectSortOrder() throws Exception {
        Utlatande utlatande = buildUtlatandeWithoutDiabetesFieldsSet();
        ValidateDraftResponse validationResponse = validator.validateInternal(utlatande);
        assertEquals(3, validationResponse.getValidationErrors().size());
        int index = 0;
        assertEquals("diabetes.observationsperiod", validationResponse.getValidationErrors().get(index++).getField());
        assertEquals("diabetes.diabetesTyp", validationResponse.getValidationErrors().get(index++).getField());
        assertEquals("diabetes", validationResponse.getValidationErrors().get(index).getField());
    }

    private Utlatande buildUtlatandeWithoutDiabetesFieldsSet() throws se.inera.intyg.intygstyper.ts_diabetes.utils.ScenarioNotFoundException {
        Utlatande utlatande = ScenarioFinder.getInternalScenario("invalid-diabetes-insulinperiod").asInternalModel();
        utlatande.getDiabetes().setAnnanBehandlingBeskrivning(null);
        utlatande.getDiabetes().setDiabetestyp(null);
        utlatande.getDiabetes().setEndastKost(null);
        utlatande.getDiabetes().setInsulin(null);
        utlatande.getDiabetes().setInsulinBehandlingsperiod(null);
        utlatande.getDiabetes().setObservationsperiod(null);
        utlatande.getDiabetes().setTabletter(null);
        return utlatande;
    }

    @Test
    public void testInvalidDateFormatHypoglykemi() throws Exception {
        Utlatande utlatande = ScenarioFinder.getInternalScenario("invalid-date-format-hypoglykemi").asInternalModel();
        ValidateDraftResponse validationResponse = validator.validateInternal(utlatande);

        assertEquals("hypoglykemier.allvarligForekomstVakenTidObservationstid",
                getSingleElement(validationResponse.getValidationErrors()).getField());
    }

    @Test
    public void testInvalidDiabetesMissing() throws Exception {
        Utlatande utlatande = ScenarioFinder.getInternalScenario("invalid-missing-diabetes").asInternalModel();
        ValidateDraftResponse validationResponse = validator.validateInternal(utlatande);

        assertEquals(T3, validationResponse.getValidationErrors().size());
    }

    @Test
    public void testInvalidDiabetesInsulinperiod() throws Exception {
        Utlatande utlatande = ScenarioFinder.getInternalScenario("invalid-diabetes-insulinperiod").asInternalModel();
        ValidateDraftResponse validationResponse = validator.validateInternal(utlatande);

        assertEquals("diabetes.insulin",
                getSingleElement(validationResponse.getValidationErrors()).getField());
    }

    @Test
    public void testInvalidMutationsDiabetesInsulinperiod() throws Exception {
        Utlatande utlatande = ScenarioFinder.getInternalScenario("invalid-diabetes-insulinperiod").asInternalModel();
        ValidateDraftResponse validationResponse;

        utlatande.getDiabetes().setInsulinBehandlingsperiod("1111");
        validationResponse = validator.validateInternal(utlatande);
        assertEquals("diabetes.insulin",
                getSingleElement(validationResponse.getValidationErrors()).getField());

        utlatande.getDiabetes().setInsulinBehandlingsperiod("");
        validationResponse = validator.validateInternal(utlatande);
        assertEquals("diabetes.insulin",
                getSingleElement(validationResponse.getValidationErrors()).getField());

        utlatande.getDiabetes().setInsulinBehandlingsperiod("aaaaaaaaaaaaaaa");
        validationResponse = validator.validateInternal(utlatande);
        assertEquals("diabetes.insulin",
                getSingleElement(validationResponse.getValidationErrors()).getField());

    }


    @Test
    public void testInvalidHypoglykemierMissing() throws Exception {
        Utlatande utlatande = ScenarioFinder.getInternalScenario("invalid-missing-hypoglykemier-kunskap")
                .asInternalModel();
        ValidateDraftResponse validationResponse = validator.validateInternal(utlatande);

        assertEquals("hypoglykemier.kunskapOmAtgarder", getSingleElement(validationResponse.getValidationErrors())
                .getField());
    }

    /**
     * Utility method for getting a single element from a collection.
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
