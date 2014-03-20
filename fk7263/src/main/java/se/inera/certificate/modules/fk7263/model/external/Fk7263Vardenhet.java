package se.inera.certificate.modules.fk7263.model.external;

import se.inera.certificate.model.Vardenhet;
import se.inera.certificate.model.Vardgivare;

public class Fk7263Vardenhet extends Vardenhet {

    private Vardgivare vardgivare;

    @Override
    public Vardgivare getVardgivare() {
        return vardgivare;
    }

    public void setVardgivare(Vardgivare vardgivare) {
        this.vardgivare = vardgivare;
    }
}
