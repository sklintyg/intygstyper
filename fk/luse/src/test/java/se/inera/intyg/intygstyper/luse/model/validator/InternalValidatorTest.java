package se.inera.intyg.intygstyper.luse.model.validator;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import se.inera.intyg.intygstyper.fkparent.model.validator.InternalValidatorUtil;
import se.inera.intyg.intygstyper.luse.model.internal.LuseUtlatande;
import se.inera.intyg.intygstyper.luse.model.utils.ScenarioFinder;
import se.inera.intyg.intygstyper.luse.model.utils.ScenarioNotFoundException;
import se.inera.intyg.intygstyper.luse.validator.InternalDraftValidatorImpl;
import se.inera.intyg.common.support.modules.support.api.dto.ValidateDraftResponse;

public class InternalValidatorTest {

    @Test
    public void testValidateMinimaltUtkast() throws ScenarioNotFoundException {
        final int numErrors = 0;
        LuseUtlatande utlatandeFromJson = ScenarioFinder.getInternalScenario("pass-minimal").asInternalModel();
        InternalDraftValidatorImpl internalValidator = new InternalDraftValidatorImpl(new InternalValidatorUtil());
        ValidateDraftResponse internalValidationResponse = internalValidator.validateDraft(utlatandeFromJson);
        assertEquals(String.format("Expected %s validation errors", numErrors), numErrors, getNumberOfInternalValidationErrors(internalValidationResponse));
    }

    @Test
    public void testUnderlagSkolhalsovardGerFel() throws ScenarioNotFoundException {
        final int numErrors = 1;
        LuseUtlatande utlatandeFromJson = ScenarioFinder.getInternalScenario("underlagSkolhalsovard").asInternalModel();
        InternalDraftValidatorImpl internalValidator = new InternalDraftValidatorImpl(new InternalValidatorUtil());
        ValidateDraftResponse internalValidationResponse = internalValidator.validateDraft(utlatandeFromJson);
        assertEquals(String.format("Expected %s validation errors", numErrors), numErrors, getNumberOfInternalValidationErrors(internalValidationResponse));
    }

    private static int getNumberOfInternalValidationErrors(ValidateDraftResponse internalValidationResponse) {
        return internalValidationResponse.getValidationErrors().size();
    }
}
