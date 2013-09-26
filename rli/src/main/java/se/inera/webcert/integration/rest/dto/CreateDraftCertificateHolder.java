package se.inera.webcert.integration.rest.dto;

import java.util.HashMap;
import java.util.Map;

public class CreateDraftCertificateHolder {

    private String certificateType;

    private Map<String, Object> initialParameters;

    public String getCertificateType() {
        return certificateType;
    }

    public void setCertificateType(String certificateType) {
        this.certificateType = certificateType;
    }

    public Map<String, Object> getInitialParameters() {
        if (initialParameters == null) {
            initialParameters = new HashMap<>();
        }
        return initialParameters;
    }
}
