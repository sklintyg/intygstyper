package se.inera.certificate.modules.ts_diabetes.rest.dto;

import java.util.ArrayList;
import java.util.List;

public class ValidateDraftResponseHolder {

    private ValidationStatus status;

    private List<ValidationMessage> validationErrors;

    public ValidateDraftResponseHolder() {
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
