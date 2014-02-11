package se.inera.certificate.modules.ts_bas.rest.dto;

import java.util.ArrayList;
import java.util.List;

public class ValidateDraftResponseHolder {
    
    private ValidationStatus status;
    
    private List<ValidationMessage> validationErrors;
    
    public ValidateDraftResponseHolder() {
        this.validationErrors = new ArrayList<>();
    }
    
    public void addErrorMessage(ValidationMessage msg) {
        validationErrors.add(msg);
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
