package se.inera.intyg.intygstyper.luse.model.validator;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Spy;
import org.mockito.runners.MockitoJUnitRunner;

import se.inera.intyg.common.support.modules.support.api.dto.ValidateDraftResponse;
import se.inera.intyg.intygstyper.fkparent.model.validator.InternalValidatorUtil;
import se.inera.intyg.intygstyper.luse.model.internal.LuseUtlatande;
import se.inera.intyg.intygstyper.luse.model.utils.ScenarioFinder;
import se.inera.intyg.intygstyper.luse.model.utils.ScenarioNotFoundException;
import se.inera.intyg.intygstyper.luse.validator.InternalDraftValidatorImpl;

@RunWith(MockitoJUnitRunner.class)
public class InternalValidatorTest {

    @Spy
    private InternalValidatorUtil validatorUtil = new InternalValidatorUtil();

    @InjectMocks
    private InternalDraftValidatorImpl internalValidator;

    @Test
    public void testValidateMinimaltUtkast() throws ScenarioNotFoundException {
        final int numErrors = 0;
        LuseUtlatande utlatandeFromJson = ScenarioFinder.getInternalScenario("pass-minimal").asInternalModel();
        ValidateDraftResponse internalValidationResponse = internalValidator.validateDraft(utlatandeFromJson);
        assertEquals(String.format("Expected %s validation errors", numErrors), numErrors, getNumberOfInternalValidationErrors(internalValidationResponse));
    }

    @Test
    public void testUnderlagSkolhalsovardGerFel() throws ScenarioNotFoundException {
        final int numErrors = 1;
        LuseUtlatande utlatandeFromJson = ScenarioFinder.getInternalScenario("underlagSkolhalsovard").asInternalModel();
        ValidateDraftResponse internalValidationResponse = internalValidator.validateDraft(utlatandeFromJson);
        assertEquals(String.format("Expected %s validation errors", numErrors), numErrors, getNumberOfInternalValidationErrors(internalValidationResponse));
    }

    private static int getNumberOfInternalValidationErrors(ValidateDraftResponse internalValidationResponse) {
        return internalValidationResponse.getValidationErrors().size();
    }
}
