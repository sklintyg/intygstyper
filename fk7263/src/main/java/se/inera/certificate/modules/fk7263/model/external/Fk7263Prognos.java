package se.inera.certificate.modules.fk7263.model.external;

import se.inera.certificate.model.Kod;

public class Fk7263Prognos {
    private Kod prognoskod;
    private String beskrivning;

    public final Kod getPrognoskod() {
        return prognoskod;
    }

    public final void setPrognoskod(Kod prognoskod) {
        this.prognoskod = prognoskod;
    }

    public final String getBeskrivning() {
        return beskrivning;
    }

    public final void setBeskrivning(String beskrivning) {
        this.beskrivning = beskrivning;
    }
}
