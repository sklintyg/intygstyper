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
package se.inera.intyg.intygstyper.luae_fs.model.validator;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Spy;
import org.mockito.runners.MockitoJUnitRunner;

import se.inera.intyg.common.support.modules.support.api.dto.ValidateDraftResponse;
import se.inera.intyg.intygstyper.fkparent.model.validator.ValidatorUtilFK;
import se.inera.intyg.intygstyper.luae_fs.model.internal.LuaefsUtlatande;
import se.inera.intyg.intygstyper.luae_fs.utils.ScenarioFinder;
import se.inera.intyg.intygstyper.luae_fs.utils.ScenarioNotFoundException;
import se.inera.intyg.intygstyper.luae_fs.validator.InternalDraftValidatorImpl;

@RunWith(MockitoJUnitRunner.class)
public class InternalValidatorTest {

    @Spy
    private ValidatorUtilFK validatorUtil = new ValidatorUtilFK();

    @InjectMocks
    private InternalDraftValidatorImpl internalValidator;

    @Test
    public void test() throws ScenarioNotFoundException {
        int numErrors = 0;
        LuaefsUtlatande utlatandeFromJson = ScenarioFinder.getInternalScenario("pass-minimal").asInternalModel();
        ValidateDraftResponse internalValidationResponse = internalValidator.validateDraft(utlatandeFromJson);
        assertEquals(numErrors, getNumberOfInternalValidationErrors(internalValidationResponse));
    }

    @Test
    public void failsWhenNoDiagnosis() throws ScenarioNotFoundException {
        int numErrors = 1;
        LuaefsUtlatande utlatandeFromJson = ScenarioFinder.getInternalScenario("fail-diagnos-saknas").asInternalModel();
        ValidateDraftResponse internalValidationResponse = internalValidator.validateDraft(utlatandeFromJson);
        assertEquals(numErrors, getNumberOfInternalValidationErrors(internalValidationResponse));
    }

    @Test
    public void failsWhenNoFunktionsnedsattningDebut() throws ScenarioNotFoundException {
        int numErrors = 1;
        LuaefsUtlatande utlatandeFromJson = ScenarioFinder.getInternalScenario("fail-funktionsnedsattning-debut-saknas").asInternalModel();
        ValidateDraftResponse internalValidationResponse = internalValidator.validateDraft(utlatandeFromJson);
        assertEquals(numErrors, getNumberOfInternalValidationErrors(internalValidationResponse));
    }

    @Test
    public void failsWhenNoFunktionsnedsattningPaverkan() throws ScenarioNotFoundException {
        int numErrors = 1;
        LuaefsUtlatande utlatandeFromJson = ScenarioFinder.getInternalScenario("fail-funktionsnedsattning-paverkan-saknas").asInternalModel();
        ValidateDraftResponse internalValidationResponse = internalValidator.validateDraft(utlatandeFromJson);
        assertEquals(numErrors, getNumberOfInternalValidationErrors(internalValidationResponse));
    }

    @Test
    public void failsWhenNoKannedomOmPatient() throws ScenarioNotFoundException {
        int numErrors = 1;
        LuaefsUtlatande utlatandeFromJson = ScenarioFinder.getInternalScenario("fail-baseratpa-kannedom-om-patient-saknas").asInternalModel();
        ValidateDraftResponse internalValidationResponse = internalValidator.validateDraft(utlatandeFromJson);
        assertEquals(numErrors, getNumberOfInternalValidationErrors(internalValidationResponse));
    }

    @Test
    public void failsWhenBaseratPaSaknas() throws ScenarioNotFoundException {
        int numErrors = 1;
        LuaefsUtlatande utlatandeFromJson = ScenarioFinder.getInternalScenario("fail-baseratpa-saknas").asInternalModel();
        ValidateDraftResponse internalValidationResponse = internalValidator.validateDraft(utlatandeFromJson);
        assertEquals(numErrors, getNumberOfInternalValidationErrors(internalValidationResponse));
    }

    @Test
    public void failsWhenAnhorigBeskrivningDatumIsTidigareAnKannedomOmPatientDatum() throws ScenarioNotFoundException {
        int numErrors = 1;
        LuaefsUtlatande utlatandeFromJson = ScenarioFinder.getInternalScenario("fail-baseratpa-anhorigbeskrivning-datum-tidigare-an-kannedom")
                .asInternalModel();
        ValidateDraftResponse internalValidationResponse = internalValidator.validateDraft(utlatandeFromJson);
        assertEquals(numErrors, getNumberOfInternalValidationErrors(internalValidationResponse));
    }

    @Test
    public void failsWhenUndersokningsDatumIsTidigareAnKannedomOmPatientDatum() throws ScenarioNotFoundException {
        int numErrors = 1;
        LuaefsUtlatande utlatandeFromJson = ScenarioFinder.getInternalScenario("fail-baseratpa-undersokning-datum-tidigare-an-kannedom")
                .asInternalModel();
        ValidateDraftResponse internalValidationResponse = internalValidator.validateDraft(utlatandeFromJson);
        assertEquals(numErrors, getNumberOfInternalValidationErrors(internalValidationResponse));
    }

    @Test
    public void failsWhenAnnatDatumMissingButBeskrivningExists() throws ScenarioNotFoundException {
        int numErrors = 1;
        LuaefsUtlatande utlatandeFromJson = ScenarioFinder.getInternalScenario("fail-baseratpa-annat-datum-finns-beskrivning-saknas")
                .asInternalModel();
        ValidateDraftResponse internalValidationResponse = internalValidator.validateDraft(utlatandeFromJson);
        assertEquals(numErrors, getNumberOfInternalValidationErrors(internalValidationResponse));
    }

    @Test
    public void failsWhenDatesHaveInvalidFormats() throws ScenarioNotFoundException {
        int numErrors = 1;
        LuaefsUtlatande utlatandeFromJson = ScenarioFinder.getInternalScenario("fail-baseratpa-journaluppgift-felaktigt-datumformat")
                .asInternalModel();
        ValidateDraftResponse internalValidationResponse = internalValidator.validateDraft(utlatandeFromJson);
        assertEquals(numErrors, getNumberOfInternalValidationErrors(internalValidationResponse));
    }

    @Test
    public void failsWhenKontaktWithFkIsFalseButReasonStated() throws ScenarioNotFoundException {
        int numErrors = 1;
        LuaefsUtlatande utlatandeFromJson = ScenarioFinder.getInternalScenario("fail-motiveringkontaktangivet-men-onskas-ej").asInternalModel();
        ValidateDraftResponse internalValidationResponse = internalValidator.validateDraft(utlatandeFromJson);
        assertEquals(numErrors, getNumberOfInternalValidationErrors(internalValidationResponse));
    }

    @Test
    public void failsDiagnosCodeLessThan3Chars() throws ScenarioNotFoundException {
        int numErrors = 1;
        LuaefsUtlatande utlatandeFromJson = ScenarioFinder.getInternalScenario("fail-diagnos-kod-med-mindre-an-tre-positioner").asInternalModel();
        ValidateDraftResponse internalValidationResponse = internalValidator.validateDraft(utlatandeFromJson);
        assertEquals(numErrors, getNumberOfInternalValidationErrors(internalValidationResponse));
    }

    @Test
    public void failsPshychatricDiagnosCodeLessThan4Chars() throws ScenarioNotFoundException {
        int numErrors = 1;
        LuaefsUtlatande utlatandeFromJson = ScenarioFinder.getInternalScenario("fail-diagnos-psykisk-diagnoskod-fel-antal-tecken").asInternalModel();
        ValidateDraftResponse internalValidationResponse = internalValidator.validateDraft(utlatandeFromJson);
        assertEquals(numErrors, getNumberOfInternalValidationErrors(internalValidationResponse));
    }

    @Test
    public void failsWhenUnderlagFinnesIsTrueAndHamtasFranHasOnlyWhitespaces() throws ScenarioNotFoundException {
        int numErrors = 1;
        LuaefsUtlatande utlatandeFromJson = ScenarioFinder.getInternalScenario("fail-underlagfinnes-whitespace").asInternalModel();
        ValidateDraftResponse internalValidationResponse = internalValidator.validateDraft(utlatandeFromJson);
        assertEquals(numErrors, getNumberOfInternalValidationErrors(internalValidationResponse));
    }

    @Test
    public void failsWhenUnderlagFinnesMenEjAngivet() throws ScenarioNotFoundException {
        int numErrors = 1;
        LuaefsUtlatande utlatandeFromJson = ScenarioFinder.getInternalScenario("fail-underlagfinnes-ej-men-angivet").asInternalModel();
        ValidateDraftResponse internalValidationResponse = internalValidator.validateDraft(utlatandeFromJson);
        assertEquals(numErrors, getNumberOfInternalValidationErrors(internalValidationResponse));
    }

    @Test
    public void failsWhenUnderlagFinnesIsFalseAndUnderlagExists() throws ScenarioNotFoundException {
        int numErrors = 1;
        LuaefsUtlatande utlatandeFromJson = ScenarioFinder.getInternalScenario("fail-underlagfinnes-men-ej-angivet").asInternalModel();
        ValidateDraftResponse internalValidationResponse = internalValidator.validateDraft(utlatandeFromJson);
        assertEquals(numErrors, getNumberOfInternalValidationErrors(internalValidationResponse));
    }

    @Test
    public void failsWhenFunktionsnedsattningHasOnlyWhitespaces() throws ScenarioNotFoundException {
        int numErrors = 2;
        LuaefsUtlatande utlatandeFromJson = ScenarioFinder.getInternalScenario("fail-funktionsnedsattning-whitespace").asInternalModel();
        ValidateDraftResponse internalValidationResponse = internalValidator.validateDraft(utlatandeFromJson);
        assertEquals(numErrors, getNumberOfInternalValidationErrors(internalValidationResponse));
    }

    private static int getNumberOfInternalValidationErrors(ValidateDraftResponse internalValidationResponse) {
        return internalValidationResponse.getValidationErrors().size();
    }
}
