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
package se.inera.intyg.intygstyper.ts_bas.model.validator;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Collection;

import org.junit.Before;
import org.junit.Test;

import com.google.common.base.Joiner;

import se.inera.intyg.common.support.modules.support.api.dto.ValidateDraftResponse;
import se.inera.intyg.common.support.modules.support.api.dto.ValidationStatus;
import se.inera.intyg.intygstyper.ts_bas.model.internal.Utlatande;
import se.inera.intyg.intygstyper.ts_bas.utils.Scenario;
import se.inera.intyg.intygstyper.ts_bas.utils.ScenarioFinder;
import se.inera.intyg.intygstyper.ts_bas.utils.ScenarioNotFoundException;
import se.inera.intyg.intygstyper.ts_bas.validator.TsBasValidator;

public class InternalValidatorTest {

    private TsBasValidator validator;

    @Before
    public void setUp() throws Exception {
        validator = new TsBasValidator();
    }

    @Test
    public void testValidate() throws Exception {
        for (Scenario scenario : ScenarioFinder.getInternalScenarios("valid-*")) {
            Utlatande utlatande = scenario.asInternalModel();
            ValidateDraftResponse validationResponse = validator.validateDraft(utlatande);

            assertEquals(
                    "Error in scenario " + scenario.getName() + "\n"
                            + Joiner.on(", ").join(validationResponse.getValidationErrors()),
                    ValidationStatus.VALID, validationResponse.getStatus());

            assertTrue(
                    "Error in scenario " + scenario.getName() + "\n"
                            + Joiner.on(", ").join(validationResponse.getValidationErrors()), validationResponse
                            .getValidationErrors().isEmpty());

        }
    }

    @Test
    public void testValidateWithErrors() throws Exception {
        for (Scenario scenario : ScenarioFinder.getInternalScenarios("invalid-*")) {

            Utlatande utlatande = scenario.asInternalModel();
            ValidateDraftResponse validationResponse = validator.validateDraft(utlatande);

            assertEquals(ValidationStatus.INVALID, validationResponse.getStatus());
        }
    }

    @Test
    public void testInvalidDiabetesTyp2MissingBehandling() throws Exception {
        Utlatande utlatande = ScenarioFinder.getInternalScenario("invalid-diabetes-typ2-missing-behandling")
                .asInternalModel();
        ValidateDraftResponse validationResponse = validator.validateDraft(utlatande);

        assertEquals("diabetes.diabetesTyp.behandlingsTyp", getSingleElement(validationResponse.getValidationErrors()).getField());
    }

    @Test
    public void testInvalidSynskarpa() throws Exception {
        Utlatande utlatande = ScenarioFinder.getInternalScenario("invalid-korrigerad-synskarpa").asInternalModel();
        ValidateDraftResponse validationResponse = validator.validateDraft(utlatande);

        assertEquals("syn.vansterOga.utanKorrektion", getSingleElement(validationResponse.getValidationErrors()).getField());
    }

    @Test
    public void testFunktionshinderBeskrivningMissing() throws Exception {
        Utlatande utlatande = ScenarioFinder.getInternalScenario("invalid-funktionshinder-beskrivning-missing")
                .asInternalModel();
        ValidateDraftResponse validationResponse = validator.validateDraft(utlatande);

        assertEquals("funktionsnedsattning.funktionsnedsattningBeskrivning", getSingleElement(validationResponse.getValidationErrors())
                .getField());
    }

    @Test
    public void testIdentitetMissing() throws Exception {
        Utlatande utlatande = ScenarioFinder.getInternalScenario("invalid-missing-identitet")
                .asInternalModel();
        ValidateDraftResponse validationResponse = validator.validateDraft(utlatande);

        assertEquals("identitet", getSingleElement(validationResponse.getValidationErrors())
                .getField());
    }

    @Test
    public void testSjukhusvardValidationOrder() throws ScenarioNotFoundException {
        Utlatande utlatande = ScenarioFinder.getInternalScenario("valid-sjukhusvard").asInternalModel();
        utlatande.getSjukhusvard().setAnledning(null);
        utlatande.getSjukhusvard().setVardinrattning(null);
        utlatande.getSjukhusvard().setTidpunkt(null);

        ValidateDraftResponse validationResponse = validator.validateDraft(utlatande);
        int index = 0;
        assertEquals("sjukhusvard.tidpunkt", validationResponse.getValidationErrors().get(index++).getField());
        assertEquals("sjukhusvard.vardinrattning", validationResponse.getValidationErrors().get(index++).getField());
        assertEquals("sjukhusvard.anledning", validationResponse.getValidationErrors().get(index++).getField());
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
