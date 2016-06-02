package se.inera.intyg.intygstyper.luae_fs.model.validator;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import se.inera.intyg.intygstyper.fkparent.model.validator.InternalValidatorUtil;
import se.inera.intyg.intygstyper.luae_fs.model.internal.LuaefsUtlatande;
import se.inera.intyg.intygstyper.luae_fs.model.utils.ScenarioFinder;
import se.inera.intyg.intygstyper.luae_fs.model.utils.ScenarioNotFoundException;
import se.inera.intyg.intygstyper.luae_fs.validator.InternalDraftValidatorImpl;
import se.inera.intyg.common.support.modules.support.api.dto.ValidateDraftResponse;

public class InternalValidatorTest {

    @Test
    public void test() throws ScenarioNotFoundException {
        int numErrors = 0;
        LuaefsUtlatande utlatandeFromJson = ScenarioFinder.getInternalScenario("pass-minimal").asInternalModel();
        InternalDraftValidatorImpl internalValidator = new InternalDraftValidatorImpl(new InternalValidatorUtil());
        ValidateDraftResponse internalValidationResponse = internalValidator.validateDraft(utlatandeFromJson);
        assertEquals(numErrors, getNumberOfInternalValidationErrors(internalValidationResponse));
    }

    @Test
    public void failsWhenNoDiagnosis() throws ScenarioNotFoundException {
        int numErrors = 1;
        LuaefsUtlatande utlatandeFromJson = ScenarioFinder.getInternalScenario("fail-diagnos-saknas").asInternalModel();
        InternalDraftValidatorImpl internalValidator = new InternalDraftValidatorImpl(new InternalValidatorUtil());
        ValidateDraftResponse internalValidationResponse = internalValidator.validateDraft(utlatandeFromJson);
        assertEquals(numErrors, getNumberOfInternalValidationErrors(internalValidationResponse));
    }

    @Test
    public void failsWhenNoFunktionsnedsattningDebut() throws ScenarioNotFoundException {
        int numErrors = 1;
        LuaefsUtlatande utlatandeFromJson = ScenarioFinder.getInternalScenario("fail-funktionsnedsattning-debut-saknas").asInternalModel();
        InternalDraftValidatorImpl internalValidator = new InternalDraftValidatorImpl(new InternalValidatorUtil());
        ValidateDraftResponse internalValidationResponse = internalValidator.validateDraft(utlatandeFromJson);
        assertEquals(numErrors, getNumberOfInternalValidationErrors(internalValidationResponse));
    }

    @Test
    public void failsWhenNoFunktionsnedsattningPaverkan() throws ScenarioNotFoundException {
        int numErrors = 1;
        LuaefsUtlatande utlatandeFromJson = ScenarioFinder.getInternalScenario("fail-funktionsnedsattning-paverkan-saknas").asInternalModel();
        InternalDraftValidatorImpl internalValidator = new InternalDraftValidatorImpl(new InternalValidatorUtil());
        ValidateDraftResponse internalValidationResponse = internalValidator.validateDraft(utlatandeFromJson);
        assertEquals(numErrors, getNumberOfInternalValidationErrors(internalValidationResponse));
    }

    @Test
    public void failsWhenNoKannedomOmPatient() throws ScenarioNotFoundException {
        int numErrors = 1;
        LuaefsUtlatande utlatandeFromJson = ScenarioFinder.getInternalScenario("fail-baseratpa-kannedom-om-patient-saknas").asInternalModel();
        InternalDraftValidatorImpl internalValidator = new InternalDraftValidatorImpl(new InternalValidatorUtil());
        ValidateDraftResponse internalValidationResponse = internalValidator.validateDraft(utlatandeFromJson);
        assertEquals(numErrors, getNumberOfInternalValidationErrors(internalValidationResponse));
    }

    @Test
    public void failsWhenBaseratPaSaknas() throws ScenarioNotFoundException {
        int numErrors = 1;
        LuaefsUtlatande utlatandeFromJson = ScenarioFinder.getInternalScenario("fail-baseratpa-saknas").asInternalModel();
        InternalDraftValidatorImpl internalValidator = new InternalDraftValidatorImpl(new InternalValidatorUtil());
        ValidateDraftResponse internalValidationResponse = internalValidator.validateDraft(utlatandeFromJson);
        assertEquals(numErrors, getNumberOfInternalValidationErrors(internalValidationResponse));
    }

    @Test
    public void failsWhenAnhorigBeskrivningDatumIsTidigareAnKannedomOmPatientDatum() throws ScenarioNotFoundException {
        int numErrors = 1;
        LuaefsUtlatande utlatandeFromJson = ScenarioFinder.getInternalScenario("fail-baseratpa-anhorigbeskrivning-datum-tidigare-an-kannedom")
                .asInternalModel();
        InternalDraftValidatorImpl internalValidator = new InternalDraftValidatorImpl(new InternalValidatorUtil());
        ValidateDraftResponse internalValidationResponse = internalValidator.validateDraft(utlatandeFromJson);
        assertEquals(numErrors, getNumberOfInternalValidationErrors(internalValidationResponse));
    }

    @Test
    public void failsWhenUndersokningsDatumIsTidigareAnKannedomOmPatientDatum() throws ScenarioNotFoundException {
        int numErrors = 1;
        LuaefsUtlatande utlatandeFromJson = ScenarioFinder.getInternalScenario("fail-baseratpa-undersokning-datum-tidigare-an-kannedom")
                .asInternalModel();
        InternalDraftValidatorImpl internalValidator = new InternalDraftValidatorImpl(new InternalValidatorUtil());
        ValidateDraftResponse internalValidationResponse = internalValidator.validateDraft(utlatandeFromJson);
        assertEquals(numErrors, getNumberOfInternalValidationErrors(internalValidationResponse));
    }

    @Test
    public void failsWhenAnnatDatumMissingButBeskrivningExists() throws ScenarioNotFoundException {
        int numErrors = 1;
        LuaefsUtlatande utlatandeFromJson = ScenarioFinder.getInternalScenario("fail-baseratpa-annat-datum-finns-beskrivning-saknas")
                .asInternalModel();
        InternalDraftValidatorImpl internalValidator = new InternalDraftValidatorImpl(new InternalValidatorUtil());
        ValidateDraftResponse internalValidationResponse = internalValidator.validateDraft(utlatandeFromJson);
        assertEquals(numErrors, getNumberOfInternalValidationErrors(internalValidationResponse));
    }

    @Test
    public void failsWhenDatesHaveInvalidFormats() throws ScenarioNotFoundException {
        int numErrors = 1;
        LuaefsUtlatande utlatandeFromJson = ScenarioFinder.getInternalScenario("fail-baseratpa-journaluppgift-felaktigt-datumformat")
                .asInternalModel();
        InternalDraftValidatorImpl internalValidator = new InternalDraftValidatorImpl(new InternalValidatorUtil());
        ValidateDraftResponse internalValidationResponse = internalValidator.validateDraft(utlatandeFromJson);
        assertEquals(numErrors, getNumberOfInternalValidationErrors(internalValidationResponse));
    }

    @Test
    public void failsWhenFourDiagnosis() throws ScenarioNotFoundException {
        int numErrors = 1;
        LuaefsUtlatande utlatandeFromJson = ScenarioFinder.getInternalScenario("fail-diagnos-fyradiagnoser").asInternalModel();
        InternalDraftValidatorImpl internalValidator = new InternalDraftValidatorImpl(new InternalValidatorUtil());
        ValidateDraftResponse internalValidationResponse = internalValidator.validateDraft(utlatandeFromJson);
        assertEquals(numErrors, getNumberOfInternalValidationErrors(internalValidationResponse));
    }

    @Test
    public void failsWhenKontaktWithFkIsFalseButReasonStated() throws ScenarioNotFoundException {
        int numErrors = 1;
        LuaefsUtlatande utlatandeFromJson = ScenarioFinder.getInternalScenario("fail-motiveringkontaktangivet-men-onskas-ej").asInternalModel();
        InternalDraftValidatorImpl internalValidator = new InternalDraftValidatorImpl(new InternalValidatorUtil());
        ValidateDraftResponse internalValidationResponse = internalValidator.validateDraft(utlatandeFromJson);
        assertEquals(numErrors, getNumberOfInternalValidationErrors(internalValidationResponse));
    }

    @Test
    public void failsDiagnosCodeLessThan3Chars() throws ScenarioNotFoundException {
        int numErrors = 1;
        LuaefsUtlatande utlatandeFromJson = ScenarioFinder.getInternalScenario("fail-diagnos-kod-med-mindre-an-tre-positioner").asInternalModel();
        InternalDraftValidatorImpl internalValidator = new InternalDraftValidatorImpl(new InternalValidatorUtil());
        ValidateDraftResponse internalValidationResponse = internalValidator.validateDraft(utlatandeFromJson);
        assertEquals(numErrors, getNumberOfInternalValidationErrors(internalValidationResponse));
    }

    @Test
    public void failsPshychatricDiagnosCodeLessThan4Chars() throws ScenarioNotFoundException {
        int numErrors = 1;
        LuaefsUtlatande utlatandeFromJson = ScenarioFinder.getInternalScenario("fail-diagnos-psykisk-diagnoskod-fel-antal-tecken").asInternalModel();
        InternalDraftValidatorImpl internalValidator = new InternalDraftValidatorImpl(new InternalValidatorUtil());
        ValidateDraftResponse internalValidationResponse = internalValidator.validateDraft(utlatandeFromJson);
        assertEquals(numErrors, getNumberOfInternalValidationErrors(internalValidationResponse));
    }

    @Test
    public void failsWhenUnderlagFinnesIsTrueAndHamtasFranHasOnlyWhitespaces() throws ScenarioNotFoundException {
        int numErrors = 1;
        LuaefsUtlatande utlatandeFromJson = ScenarioFinder.getInternalScenario("fail-underlagfinnes-whitespace").asInternalModel();
        InternalDraftValidatorImpl internalValidator = new InternalDraftValidatorImpl(new InternalValidatorUtil());
        ValidateDraftResponse internalValidationResponse = internalValidator.validateDraft(utlatandeFromJson);
        assertEquals(numErrors, getNumberOfInternalValidationErrors(internalValidationResponse));
    }

    @Test
    public void failsWhenUnderlagFinnesMenEjAngivet() throws ScenarioNotFoundException {
        int numErrors = 1;
        LuaefsUtlatande utlatandeFromJson = ScenarioFinder.getInternalScenario("fail-underlagfinnes-ej-men-angivet").asInternalModel();
        InternalDraftValidatorImpl internalValidator = new InternalDraftValidatorImpl(new InternalValidatorUtil());
        ValidateDraftResponse internalValidationResponse = internalValidator.validateDraft(utlatandeFromJson);
        assertEquals(numErrors, getNumberOfInternalValidationErrors(internalValidationResponse));
    }

    @Test
    public void failsWhenUnderlagFinnesIsFalseAndUnderlagExists() throws ScenarioNotFoundException {
        int numErrors = 1;
        LuaefsUtlatande utlatandeFromJson = ScenarioFinder.getInternalScenario("fail-underlagfinnes-men-ej-angivet").asInternalModel();
        InternalDraftValidatorImpl internalValidator = new InternalDraftValidatorImpl(new InternalValidatorUtil());
        ValidateDraftResponse internalValidationResponse = internalValidator.validateDraft(utlatandeFromJson);
        assertEquals(numErrors, getNumberOfInternalValidationErrors(internalValidationResponse));
    }

    @Test
    public void failsWhenFunktionsnedsattningHasOnlyWhitespaces() throws ScenarioNotFoundException {
        int numErrors = 2;
        LuaefsUtlatande utlatandeFromJson = ScenarioFinder.getInternalScenario("fail-funktionsnedsattning-whitespace").asInternalModel();
        InternalDraftValidatorImpl internalValidator = new InternalDraftValidatorImpl(new InternalValidatorUtil());
        ValidateDraftResponse internalValidationResponse = internalValidator.validateDraft(utlatandeFromJson);
        assertEquals(numErrors, getNumberOfInternalValidationErrors(internalValidationResponse));
    }

    private static int getNumberOfInternalValidationErrors(ValidateDraftResponse internalValidationResponse) {
        return internalValidationResponse.getValidationErrors().size();
    }
}
