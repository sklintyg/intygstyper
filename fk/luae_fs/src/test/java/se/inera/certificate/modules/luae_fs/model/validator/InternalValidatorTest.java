package se.inera.certificate.modules.luae_fs.model.validator;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import se.inera.certificate.modules.fkparent.model.validator.InternalValidatorUtil;
import se.inera.certificate.modules.luae_fs.model.internal.LuaefsUtlatande;
import se.inera.certificate.modules.luae_fs.model.utils.ScenarioFinder;
import se.inera.certificate.modules.luae_fs.model.utils.ScenarioNotFoundException;
import se.inera.certificate.modules.luae_fs.validator.InternalDraftValidator;
import se.inera.intyg.common.support.modules.support.api.dto.ValidateDraftResponse;

public class InternalValidatorTest {

    @Test
    public void test() throws ScenarioNotFoundException {
        int numErrors = 0;
        LuaefsUtlatande utlatandeFromJson = ScenarioFinder.getInternalScenario("pass-minimal").asInternalModel();
        InternalDraftValidator internalValidator = new InternalDraftValidator(new InternalValidatorUtil());
        ValidateDraftResponse internalValidationResponse = internalValidator.validateDraft(utlatandeFromJson);
        assertEquals(numErrors, getNumberOfInternalValidationErrors(internalValidationResponse));
    }

    @Test
    public void failsWhenNoDiagnosis() throws ScenarioNotFoundException {
        int numErrors = 1;
        LuaefsUtlatande utlatandeFromJson = ScenarioFinder.getInternalScenario("fail-diagnossaknas").asInternalModel();
        InternalDraftValidator internalValidator = new InternalDraftValidator(new InternalValidatorUtil());
        ValidateDraftResponse internalValidationResponse = internalValidator.validateDraft(utlatandeFromJson);
        assertEquals(numErrors, getNumberOfInternalValidationErrors(internalValidationResponse));
    }

    @Test
    public void failsWhenFourDiagnosis() throws ScenarioNotFoundException {
        int numErrors = 1;
        LuaefsUtlatande utlatandeFromJson = ScenarioFinder.getInternalScenario("fail-fyradiagnoser").asInternalModel();
        InternalDraftValidator internalValidator = new InternalDraftValidator(new InternalValidatorUtil());
        ValidateDraftResponse internalValidationResponse = internalValidator.validateDraft(utlatandeFromJson);
        assertEquals(numErrors, getNumberOfInternalValidationErrors(internalValidationResponse));
    }

    @Test
    public void failsWhenKontaktWithFkIsFalseButReasonStated() throws ScenarioNotFoundException {
        int numErrors = 1;
        LuaefsUtlatande utlatandeFromJson = ScenarioFinder.getInternalScenario("fail-motiveringkontaktsaknas").asInternalModel();
        InternalDraftValidator internalValidator = new InternalDraftValidator(new InternalValidatorUtil());
        ValidateDraftResponse internalValidationResponse = internalValidator.validateDraft(utlatandeFromJson);
        assertEquals(numErrors, getNumberOfInternalValidationErrors(internalValidationResponse));
    }

    @Test
    public void failsWhenUnderlagFinnesIsTrueAndHamtasFranHasOnlyWhitespaces() throws ScenarioNotFoundException {
        int numErrors = 1;
        LuaefsUtlatande utlatandeFromJson = ScenarioFinder.getInternalScenario("fail-underlagfinnes-whitespace").asInternalModel();
        InternalDraftValidator internalValidator = new InternalDraftValidator(new InternalValidatorUtil());
        ValidateDraftResponse internalValidationResponse = internalValidator.validateDraft(utlatandeFromJson);
        assertEquals(numErrors, getNumberOfInternalValidationErrors(internalValidationResponse));
    }

    @Test
    public void failsWhenFunktionsnedsattningHasOnlyWhitespaces() throws ScenarioNotFoundException {
        int numErrors = 2;
        LuaefsUtlatande utlatandeFromJson = ScenarioFinder.getInternalScenario("fail-funktionsnedsattning-whitespace").asInternalModel();
        InternalDraftValidator internalValidator = new InternalDraftValidator(new InternalValidatorUtil());
        ValidateDraftResponse internalValidationResponse = internalValidator.validateDraft(utlatandeFromJson);
        assertEquals(numErrors, getNumberOfInternalValidationErrors(internalValidationResponse));
    }

//    @Test
//    public void test() throws ScenarioNotFoundException {
//        int numErrors = 3;
//        LuaefsUtlatande utlatandeFromJson = ScenarioFinder.getInternalScenario("sjukskrivningOverlappandePerioder").asInternalModel();
//        InternalDraftValidator internalValidator = new InternalDraftValidator(new InternalValidatorUtil());
//        ValidateDraftResponse internalValidationResponse = internalValidator.validateDraft(utlatandeFromJson);
//        assertEquals(String.format("Expected %s validation errors", numErrors), numErrors, getNumberOfInternalValidationErrors(internalValidationResponse));
//    }

    private static int getNumberOfInternalValidationErrors(ValidateDraftResponse internalValidationResponse) {
        return internalValidationResponse.getValidationErrors().size();
    }
}
