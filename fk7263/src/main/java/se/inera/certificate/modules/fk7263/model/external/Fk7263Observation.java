package se.inera.certificate.modules.fk7263.model.external;

import java.util.ArrayList;
import java.util.List;

import se.inera.certificate.model.Kod;
import se.inera.certificate.model.LocalDateInterval;
import se.inera.certificate.model.PhysicalQuantity;
import se.inera.certificate.model.Utforarroll;

/**
 * @author andreaskaltenbach
 */
public class Fk7263Observation {

    private Kod observationskategori;
    private Kod observationskod;
    private LocalDateInterval observationsperiod;
    private String beskrivning;
    private List<Fk7263Prognos> prognoser;
    private List<PhysicalQuantity> varde;
    private Utforarroll utforsAv;

    public Kod getObservationskategori() {
        return observationskategori;
    }

    public void setObservationskategori(Kod observationskategori) {
        this.observationskategori = observationskategori;
    }

    public Kod getObservationskod() {
        return observationskod;
    }

    public void setObservationskod(Kod observationskod) {
        this.observationskod = observationskod;
    }

    public LocalDateInterval getObservationsperiod() {
        return observationsperiod;
    }

    public void setObservationsperiod(LocalDateInterval observationsperiod) {
        this.observationsperiod = observationsperiod;
    }

    public String getBeskrivning() {
        return beskrivning;
    }

    public void setBeskrivning(String beskrivning) {
        this.beskrivning = beskrivning;
    }

    public List<Fk7263Prognos> getPrognoser() {
        if (prognoser == null) {
            prognoser = new ArrayList<>();
        }
        return prognoser;
    }

    public List<PhysicalQuantity> getVarde() {
        if (varde == null) {
            varde = new ArrayList<>();
        }
        return varde;
    }

    public Utforarroll getUtforsAv() {
        return utforsAv;
    }

    public void setUtforsAv(Utforarroll utforsAv) {
        this.utforsAv = utforsAv;
    }
}
