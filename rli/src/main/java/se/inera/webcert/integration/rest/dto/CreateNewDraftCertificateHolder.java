package se.inera.webcert.integration.rest.dto;

import se.inera.certificate.model.HosPersonal;
import se.inera.certificate.model.Patient;

public class CreateNewDraftCertificateHolder {

    private String certificateId;
    
    private HosPersonal skapadAv;
    
    private Patient patientInfo;
    
    private Object somethingOnlyWebcertKnowsAbout;

    public String getCertificateId() {
        return certificateId;
    }

    public void setCertificateId(String certificateId) {
        this.certificateId = certificateId;
    }

    public HosPersonal getSkapadAv() {
        return skapadAv;
    }

    public void setSkapadAv(HosPersonal skapadAv) {
        this.skapadAv = skapadAv;
    }

    public Patient getPatientInfo() {
        return patientInfo;
    }

    public void setPatientInfo(Patient patientInfo) {
        this.patientInfo = patientInfo;
    }

    public Object getSomethingOnlyWebcertKnowsAbout() {
        return somethingOnlyWebcertKnowsAbout;
    }

    public void setSomethingOnlyWebcertKnowsAbout(Object somethingOnlyWebcertKnowsAbout) {
        this.somethingOnlyWebcertKnowsAbout = somethingOnlyWebcertKnowsAbout;
    }
}
