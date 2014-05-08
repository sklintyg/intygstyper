package se.inera.certificate.model;

import java.util.Collections;
import java.util.List;

public class Aktivitet {

    private Id id;

    private Kod aktivitetskod;

    private String beskrivning;

    private PartialInterval aktivitetstid;

    private Boolean forekomst;

    public final Id getId() {
        return id;
    }

    public final void setId(Id id) {
        this.id = id;
    }

    public final Kod getAktivitetskod() {
        return aktivitetskod;
    }

    public final void setAktivitetskod(Kod aktivitetskod) {
        this.aktivitetskod = aktivitetskod;
    }

    public final String getBeskrivning() {
        return beskrivning;
    }

    public final void setBeskrivning(String beskrivning) {
        this.beskrivning = beskrivning;
    }

    public final PartialInterval getAktivitetstid() {
        return aktivitetstid;
    }

    public final void setAktivitetstid(PartialInterval aktivitetstid) {
        this.aktivitetstid = aktivitetstid;
    }

    public final Boolean getForekomst() {
        return forekomst;
    }

    public final void setForekomst(Boolean forekomst) {
        this.forekomst = forekomst;
    }

    public List<? extends Utforarroll> getBeskrivsAv() {
        return Collections.emptyList();
    }

    public Vardenhet getUtforsVid() {
        return null;
    }
}
