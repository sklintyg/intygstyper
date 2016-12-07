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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.time.LocalDate;
import java.util.Collection;

import org.junit.Before;
import org.junit.Test;

import com.google.common.base.Joiner;

import se.inera.intyg.common.support.model.InternalDate;
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
            ValidateDraftResponse validationResponse = validator.validateDraft(utlatande);

            assertEquals(
                    "Error in scenario " + scenario.getName() + "\n"
                            + Joiner.on(", ").join(validationResponse.getValidationErrors()),
                    ValidationStatus.VALID,
                    validationResponse.getStatus());

            assertTrue(
                    "Error in scenario " + scenario.getName() + "\n"
                            + Joiner.on(", ").join(validationResponse.getValidationErrors()),
                    validationResponse
                            .getValidationErrors().isEmpty());

        }
    }

    @Test
    public void testValidateWithErrors() throws Exception {
        for (Scenario scenario : ScenarioFinder.getInternalScenarios("invalid-*")) {

            Utlatande utlatande = scenario.asInternalModel();
            ValidateDraftResponse validationResponse = validator.validateDraft(utlatande);

            assertEquals(String.format("Error in test: %s", scenario.getName()), ValidationStatus.INVALID, validationResponse.getStatus());
        }
    }

    @Test
    public void testInvalidSynskarpa() throws Exception {
        Utlatande utlatande = ScenarioFinder.getInternalScenario("invalid-korrigerad-synskarpa").asInternalModel();
        ValidateDraftResponse validationResponse = validator.validateDraft(utlatande);

        assertEquals("syn.vanster.medKorrektion", getSingleElement(validationResponse.getValidationErrors()).getField());
    }

    @Test
    public void testInvalidOgonlakarintygSaknasCorrectSortOrder() throws Exception {
        Utlatande utlatande = ScenarioFinder.getInternalScenario("invalid-missing-ogonlakarintyg").asInternalModel();
        ValidateDraftResponse validationResponse = validator.validateDraft(utlatande);
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
        ValidateDraftResponse validationResponse = validator.validateDraft(utlatande);
        assertEquals(3, validationResponse.getValidationErrors().size());
        int index = 0;
        assertEquals("diabetes.observationsperiod", validationResponse.getValidationErrors().get(index++).getField());
        assertEquals("diabetes.diabetesTyp", validationResponse.getValidationErrors().get(index++).getField());
        assertEquals("diabetes.behandling", validationResponse.getValidationErrors().get(index).getField());
    }

    /*
     * Since the validation of this field (Hypoglykemier.AllvarligForekomstVakenTidObservationstid) depends on the actual date,
     * this must be done programmatically and can thus not be tested with the scenario based approach used above.
     */
    @Test
    public void testValidDateHypoglykemi() throws Exception {
        Utlatande utlatande = ScenarioFinder.getInternalScenario("invalid-date-format-hypoglykemi").asInternalModel();
        utlatande.getHypoglykemier().setAllvarligForekomstVakenTidObservationstid(new InternalDate(LocalDate.now().minusMonths(6)));
        ValidateDraftResponse validationResponse = validator.validateDraft(utlatande);

        assertTrue(validationResponse.getValidationErrors().isEmpty());
    }

    @Test
    public void testInvalidDateFormatHypoglykemi() throws Exception {
        Utlatande utlatande = ScenarioFinder.getInternalScenario("invalid-date-format-hypoglykemi").asInternalModel();
        ValidateDraftResponse validationResponse = validator.validateDraft(utlatande);

        assertEquals("hypoglykemier.allvarligForekomstVakenTidObservationstid",
                getSingleElement(validationResponse.getValidationErrors()).getField());
    }

    @Test
    public void testDateHypoglykemiMoreThenOneYearInThePast() throws Exception {
        Utlatande utlatande = ScenarioFinder.getInternalScenario("invalid-date-format-hypoglykemi").asInternalModel();
        utlatande.getHypoglykemier().setAllvarligForekomstVakenTidObservationstid(new InternalDate(LocalDate.now().minusYears(1).minusDays(1)));
        ValidateDraftResponse validationResponse = validator.validateDraft(utlatande);

        assertEquals("hypoglykemier.allvarligForekomstVakenTidObservationstid",
                getSingleElement(validationResponse.getValidationErrors()).getField());
    }

    @Test
    public void testInvalidDiabetesMissing() throws Exception {
        Utlatande utlatande = ScenarioFinder.getInternalScenario("invalid-missing-diabetes").asInternalModel();
        ValidateDraftResponse validationResponse = validator.validateDraft(utlatande);

        assertEquals(T3, validationResponse.getValidationErrors().size());
    }

    @Test
    public void testInvalidDiabetesInsulinperiod() throws Exception {
        Utlatande utlatande = ScenarioFinder.getInternalScenario("invalid-diabetes-insulinperiod").asInternalModel();
        ValidateDraftResponse validationResponse = validator.validateDraft(utlatande);

        assertEquals("diabetes.insulin",
                getSingleElement(validationResponse.getValidationErrors()).getField());
    }

    @Test
    public void testInvalidMutationsDiabetesInsulinperiod() throws Exception {
        Utlatande utlatande = ScenarioFinder.getInternalScenario("invalid-diabetes-insulinperiod").asInternalModel();
        ValidateDraftResponse validationResponse;

        utlatande.getDiabetes().setInsulinBehandlingsperiod("1111");
        validationResponse = validator.validateDraft(utlatande);
        assertEquals("diabetes.insulin",
                getSingleElement(validationResponse.getValidationErrors()).getField());

        utlatande.getDiabetes().setInsulinBehandlingsperiod("");
        validationResponse = validator.validateDraft(utlatande);
        assertEquals("diabetes.insulin",
                getSingleElement(validationResponse.getValidationErrors()).getField());

        utlatande.getDiabetes().setInsulinBehandlingsperiod("aaaaaaaaaaaaaaa");
        validationResponse = validator.validateDraft(utlatande);
        assertEquals("diabetes.insulin",
                getSingleElement(validationResponse.getValidationErrors()).getField());

    }

    @Test
    public void testInvalidHypoglykemierMissing() throws Exception {
        Utlatande utlatande = ScenarioFinder.getInternalScenario("invalid-missing-hypoglykemier-kunskap")
                .asInternalModel();
        ValidateDraftResponse validationResponse = validator.validateDraft(utlatande);

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
}
