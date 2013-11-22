package se.inera.certificate.modules.rli.rest.dto;

import se.inera.certificate.model.HosPersonal;
import se.inera.certificate.model.Patient;

public class CreateNewDraftCertificateHolder {

    private String certificateId;

    private HosPersonal skapadAv;

    private Patient patientInfo;

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
}
