package se.inera.certificate.model.common.internal;

import org.joda.time.LocalDateTime;

public class IntygMetadata {
    private LocalDateTime signeringsdatum;
    private LocalDateTime skickatdatum;
    private HoSPersonal skapadAv;
    private Patient patient;

    public LocalDateTime getSigneringsdatum() {
        return signeringsdatum;
    }

    public void setSigneringsdatum(LocalDateTime signeringsdatum) {
        this.signeringsdatum = signeringsdatum;
    }

    public LocalDateTime getSkickatdatum() {
        return skickatdatum;
    }

    public void setSkickatdatum(LocalDateTime skickatdatum) {
        this.skickatdatum = skickatdatum;
    }

    public HoSPersonal getSkapadAv() {
        return skapadAv;
    }

    public void setSkapadAv(HoSPersonal skapadAv) {
        this.skapadAv = skapadAv;
    }

    public Patient getPatient() {
        return patient;
    }

    public void setPatient(Patient patient) {
        this.patient = patient;
    }
}
