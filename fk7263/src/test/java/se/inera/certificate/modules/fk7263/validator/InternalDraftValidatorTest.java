package se.inera.certificate.modules.fk7263.validator;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Collection;

import org.apache.commons.lang.StringUtils;
import org.junit.Before;
import org.junit.Test;

import se.inera.certificate.modules.fk7263.model.internal.Fk7263Intyg;
import se.inera.certificate.modules.fk7263.utils.Scenario;
import se.inera.certificate.modules.fk7263.utils.ScenarioFinder;
import se.inera.certificate.modules.support.api.dto.ValidateDraftResponse;
import se.inera.certificate.modules.support.api.dto.ValidationStatus;

public class InternalDraftValidatorTest {
    private InternalDraftValidator validator;

    @Before
    public void setUp() throws Exception {
        validator = new InternalDraftValidator();
    }

    @Test
    public void testValidate() throws Exception {
        for (se.inera.certificate.modules.fk7263.utils.Scenario scenario : ScenarioFinder.getInternalScenarios("valid-*")) {
            Fk7263Intyg utlatande = scenario.asInternalModel();
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

            Fk7263Intyg utlatande = scenario.asInternalModel();
            ValidateDraftResponse validationResponse = validator.validateDraft(utlatande);
            System.out.println(validationResponse.getValidationErrors());

            assertEquals(ValidationStatus.INVALID, validationResponse.getStatus());
        }
    }

    /**
     * Utility method for getting a single element from a collection
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
