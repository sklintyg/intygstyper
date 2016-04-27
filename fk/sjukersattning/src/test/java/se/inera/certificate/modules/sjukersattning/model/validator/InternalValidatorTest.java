package se.inera.certificate.modules.sjukersattning.model.validator;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import se.inera.certificate.modules.fkparent.model.validator.InternalValidatorUtil;
import se.inera.certificate.modules.sjukersattning.model.internal.SjukersattningUtlatande;
import se.inera.certificate.modules.sjukersattning.model.utils.ScenarioFinder;
import se.inera.certificate.modules.sjukersattning.model.utils.ScenarioNotFoundException;
import se.inera.certificate.modules.sjukersattning.validator.InternalDraftValidator;
import se.inera.intyg.common.support.modules.support.api.dto.ValidateDraftResponse;

public class InternalValidatorTest {

    @Test
    public void testValidateMinimaltUtkast() throws ScenarioNotFoundException {
        final int numErrors = 0;
        SjukersattningUtlatande utlatandeFromJson = ScenarioFinder.getInternalScenario("pass-minimal").asInternalModel();
        InternalDraftValidator internalValidator = new InternalDraftValidator(new InternalValidatorUtil());
        ValidateDraftResponse internalValidationResponse = internalValidator.validateDraft(utlatandeFromJson);
        assertEquals(String.format("Expected %s validation errors", numErrors), numErrors, getNumberOfInternalValidationErrors(internalValidationResponse));
    }

    @Test
    public void testUnderlagSkolhalsovardGerFel() throws ScenarioNotFoundException {
        final int numErrors = 1;
        SjukersattningUtlatande utlatandeFromJson = ScenarioFinder.getInternalScenario("underlagSkolhalsovard").asInternalModel();
        InternalDraftValidator internalValidator = new InternalDraftValidator(new InternalValidatorUtil());
        ValidateDraftResponse internalValidationResponse = internalValidator.validateDraft(utlatandeFromJson);
        assertEquals(String.format("Expected %s validation errors", numErrors), numErrors, getNumberOfInternalValidationErrors(internalValidationResponse));
    }

    private static int getNumberOfInternalValidationErrors(ValidateDraftResponse internalValidationResponse) {
        return internalValidationResponse.getValidationErrors().size();
    }
}
