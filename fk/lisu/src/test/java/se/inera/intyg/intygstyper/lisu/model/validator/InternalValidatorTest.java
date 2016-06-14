package se.inera.intyg.intygstyper.lisu.model.validator;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Spy;
import org.mockito.runners.MockitoJUnitRunner;

import se.inera.intyg.common.support.modules.support.api.dto.ValidateDraftResponse;
import se.inera.intyg.intygstyper.fkparent.model.validator.InternalValidatorUtil;
import se.inera.intyg.intygstyper.lisu.model.internal.LisuUtlatande;
import se.inera.intyg.intygstyper.lisu.model.utils.ScenarioFinder;
import se.inera.intyg.intygstyper.lisu.model.utils.ScenarioNotFoundException;
import se.inera.intyg.intygstyper.lisu.validator.InternalDraftValidatorImpl;

@RunWith(MockitoJUnitRunner.class)
public class InternalValidatorTest {

    @Spy
    private InternalValidatorUtil validatorUtil = new InternalValidatorUtil();

    @InjectMocks
    private InternalDraftValidatorImpl internalValidator;

    @Test
    public void test() throws ScenarioNotFoundException {
        final int numErrors = 3;
        LisuUtlatande utlatandeFromJson = ScenarioFinder.getInternalScenario("sjukskrivningOverlappandePerioder").asInternalModel();
        ValidateDraftResponse internalValidationResponse = internalValidator.validateDraft(utlatandeFromJson);
        assertEquals(String.format("Expected %s validation errors", numErrors), numErrors, getNumberOfInternalValidationErrors(internalValidationResponse));
    }

    private static int getNumberOfInternalValidationErrors(ValidateDraftResponse internalValidationResponse) {
        return internalValidationResponse.getValidationErrors().size();
    }
}
