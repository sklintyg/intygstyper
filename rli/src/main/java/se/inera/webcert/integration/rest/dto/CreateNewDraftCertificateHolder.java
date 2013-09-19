package se.inera.webcert.integration.rest.dto;

import java.util.HashMap;
import java.util.Map;

public class CreateNewDraftCertificateHolder {

    private String certificateId;
    
    private Map<String, Object> certificateData = new HashMap<String, Object>();

    public String getCertificateId() {
        return certificateId;
    }

    public void setCertificateId(String certificateId) {
        this.certificateId = certificateId;
    }

    public Map<String, Object> getCertificateData() {
        return certificateData;
    }

    public void setCertificateData(Map<String, Object> certificateData) {
        this.certificateData = certificateData;
    }
}
