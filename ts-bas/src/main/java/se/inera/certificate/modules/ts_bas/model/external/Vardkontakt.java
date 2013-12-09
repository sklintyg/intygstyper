package se.inera.certificate.modules.ts_bas.model.external;

import se.inera.certificate.model.Kod;

public class Vardkontakt extends se.inera.certificate.model.Vardkontakt {

    private Kod idkontroll;

    public Kod getIdkontroll() {
        return idkontroll;
    }

    public void setIdkontroll(Kod idkontroll) {
        this.idkontroll = idkontroll;
    }
}
