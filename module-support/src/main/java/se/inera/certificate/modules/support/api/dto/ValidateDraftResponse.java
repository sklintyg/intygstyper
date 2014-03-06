package se.inera.certificate.modules.support.api.dto;

import java.util.ArrayList;
import java.util.List;

public class ValidateDraftResponse {

    private ValidationStatus status;

    private List<ValidationMessage> validationErrors;

    public ValidateDraftResponse() {
        this.validationErrors = new ArrayList<>();
    }

    public void addErrorMessage(ValidationMessage msg) {
        this.validationErrors.add(msg);
    }

    public boolean hasErrorMessages() {
        return !(this.validationErrors.isEmpty());
    }

    public List<ValidationMessage> getValidationErrors() {
        return validationErrors;
    }

    public ValidationStatus getStatus() {
        return status;
    }

    public void setStatus(ValidationStatus status) {
        this.status = status;
    }

}
