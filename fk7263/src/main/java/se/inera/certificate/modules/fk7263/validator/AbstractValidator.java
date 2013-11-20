package se.inera.certificate.modules.fk7263.validator;

import java.util.ArrayList;
import java.util.List;

/**
 * Abstract base class for validators
 * @author marced
 *
 */
public abstract class AbstractValidator {
    
    protected List<String> validationErrors = new ArrayList<>();
    
    protected static final String VALIDATION_ERROR_PREFIX = "Validation Error:";
    
    protected void addValidationError(String errorDesc) {
        validationErrors.add(VALIDATION_ERROR_PREFIX + errorDesc);
    }
}
