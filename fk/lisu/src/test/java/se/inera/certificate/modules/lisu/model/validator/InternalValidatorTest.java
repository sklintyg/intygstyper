package se.inera.certificate.modules.lisu.model.validator;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import se.inera.certificate.modules.fkparent.model.validator.InternalValidatorUtil;
import se.inera.certificate.modules.lisu.model.internal.LisuUtlatande;
import se.inera.certificate.modules.lisu.model.utils.ScenarioFinder;
import se.inera.certificate.modules.lisu.model.utils.ScenarioNotFoundException;
import se.inera.certificate.modules.lisu.validator.InternalDraftValidator;
import se.inera.intyg.common.support.modules.support.api.dto.ValidateDraftResponse;

public class InternalValidatorTest {

    @Test
    public void test() throws ScenarioNotFoundException {
        final int numErrors = 3;
        LisuUtlatande utlatandeFromJson = ScenarioFinder.getInternalScenario("sjukskrivningOverlappandePerioder").asInternalModel();
        InternalDraftValidator internalValidator = new InternalDraftValidator(new InternalValidatorUtil());
        ValidateDraftResponse internalValidationResponse = internalValidator.validateDraft(utlatandeFromJson);
        assertEquals(String.format("Expected %s validation errors", numErrors), numErrors, getNumberOfInternalValidationErrors(internalValidationResponse));
    }

    private static int getNumberOfInternalValidationErrors(ValidateDraftResponse internalValidationResponse) {
        return internalValidationResponse.getValidationErrors().size();
    }
}
