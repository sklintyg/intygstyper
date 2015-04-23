package se.inera.certificate.modules.fk7263.validator;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatcher;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import se.inera.certificate.modules.fk7263.model.internal.Utlatande;
import se.inera.certificate.modules.fk7263.utils.Scenario;
import se.inera.certificate.modules.fk7263.utils.ScenarioFinder;
import se.inera.certificate.modules.service.WebcertModuleService;
import se.inera.certificate.modules.support.api.dto.ValidateDraftResponse;
import se.inera.certificate.modules.support.api.dto.ValidationStatus;

@RunWith(MockitoJUnitRunner.class)
public class InternalDraftValidatorTest {

    @Mock
    private WebcertModuleService mockModuleService;

    @InjectMocks
    private InternalDraftValidator validator = new InternalDraftValidator();

    @Before
    public void setUpModuleServiceExpectation() throws Exception {
        Mockito.when(mockModuleService.validateDiagnosisCode(Mockito.argThat(new DiagnosKodArgmentMatcher()), Mockito.anyString())).thenReturn(true);
    }

    @Test
    public void testValidate() throws Exception {
        for (se.inera.certificate.modules.fk7263.utils.Scenario scenario : ScenarioFinder.getInternalScenarios("valid-*")) {
            Utlatande utlatande = scenario.asInternalModel();
            ValidateDraftResponse validationResponse = validator.validateDraft(utlatande);
            assertEquals(
                    "Error in scenario " + scenario.getName() + "\n"
                            + StringUtils.join(validationResponse.getValidationErrors(), ", "),
                    ValidationStatus.VALID, validationResponse.getStatus());

            assertTrue(
                    "Error in scenario " + scenario.getName() + "\n"
                            + StringUtils.join(validationResponse.getValidationErrors(), ", "), validationResponse
                            .getValidationErrors().isEmpty());

        }
    }

    @Test
    public void testValidateWithErrors() throws Exception {
        for (Scenario scenario : ScenarioFinder.getInternalScenarios("invalid-*")) {

            Utlatande utlatande = scenario.asInternalModel();
            ValidateDraftResponse validationResponse = validator.validateDraft(utlatande);

            assertEquals("Error in scenario " + scenario.getName() + "\n",
                    ValidationStatus.INVALID, validationResponse.getStatus());
        }
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

    /**
     * ArgumentMatcher that validates a supplied diagnos code argument. If its not in the list, it is invalid!
     *
     * @author npet
     *
     */
    class DiagnosKodArgmentMatcher extends ArgumentMatcher<String> {

        private List<String> ALLOWED_CODES = Arrays.asList("S47", "TEST1", "TEST2", "TEST3", "Z233", "A000");

        @Override
        public boolean matches(Object arg) {
            if (arg instanceof String) {
                return ALLOWED_CODES.contains(arg);
            }
            return false;
        }
    }
}
