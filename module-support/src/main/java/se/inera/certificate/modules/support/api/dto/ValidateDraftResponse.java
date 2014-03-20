package se.inera.certificate.modules.support.api.dto;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.springframework.util.Assert;

public class ValidateDraftResponse {

    private final ValidationStatus status;

    private final List<ValidationMessage> validationErrors;

    public ValidateDraftResponse(ValidationStatus status, List<ValidationMessage> validationErrors) {
        Assert.notNull(status, "'status' must not be null");
        Assert.notNull(validationErrors, "'validationErrors' must not be null");
        this.status = status;
        this.validationErrors = new ArrayList<>(validationErrors);
    }

    public boolean hasErrorMessages() {
        return !(this.validationErrors.isEmpty());
    }

    public List<ValidationMessage> getValidationErrors() {
        return Collections.unmodifiableList(validationErrors);
    }

    public ValidationStatus getStatus() {
        return status;
    }
}
