package se.inera.certificate.model.common.external;

import se.inera.certificate.model.Vardgivare;

public class Vardenhet extends se.inera.certificate.model.Vardenhet {

    private Vardgivare vardgivare;

    @Override
    public Vardgivare getVardgivare() {
        return vardgivare;
    }

    public void setVardgivare(Vardgivare vardgivare) {
        this.vardgivare = vardgivare;
    }
}
