package se.inera.certificate.modules.fk7263.model.external;

import java.util.ArrayList;
import java.util.List;

import se.inera.certificate.model.Kod;
import se.inera.certificate.model.LocalDateInterval;
import se.inera.certificate.model.Utforarroll;
import se.inera.certificate.model.Vardenhet;

/**
 * @author andreaskaltenbach
 */
public class Fk7263Aktivitet {
    private Kod aktivitetskod;
    private String beskrivning;
    private LocalDateInterval aktivitetstid;
    private List<Utforarroll> beskrivsAv;
    private Vardenhet utforsVid;

    public Kod getAktivitetskod() {
        return aktivitetskod;
    }

    public void setAktivitetskod(Kod aktivitetskod) {
        this.aktivitetskod = aktivitetskod;
    }

    public String getBeskrivning() {
        return beskrivning;
    }

    public void setBeskrivning(String beskrivning) {
        this.beskrivning = beskrivning;
    }

    public LocalDateInterval getAktivitetstid() {
        return aktivitetstid;
    }

    public void setAktivitetstid(LocalDateInterval aktivitetstid) {
        this.aktivitetstid = aktivitetstid;
    }

    public List<Utforarroll> getBeskrivsAv() {
        if (beskrivsAv == null) {
            beskrivsAv = new ArrayList<Utforarroll>();
        }
        return this.beskrivsAv;
    }

    public Vardenhet getUtforsVid() {
        return utforsVid;
    }

    public void setUtforsVid(Vardenhet utforsVid) {
        this.utforsVid = utforsVid;
    }
}
