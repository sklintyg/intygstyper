package se.inera.webcert.integration.rest.dto;

import se.inera.certificate.model.HosPersonal;
import se.inera.certificate.model.Patient;

public class CreateDraftCertificateHolder {

    private String certificateType;

    private HosPersonal skapadAv;
    
    private Patient patientInfo;
    
    private Object somethingOnlyWebcertKnowsAbout;

    public String getCertificateType() {
        return certificateType;
    }

    public void setCertificateType(String certificateType) {
        this.certificateType = certificateType;
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
