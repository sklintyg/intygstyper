package se.inera.certificate.modules.rli.model.external;

import se.inera.intyg.common.support.model.Vardgivare;

public class Vardenhet extends se.inera.intyg.common.support.model.Vardenhet {

    private Vardgivare vardgivare;

    @Override
    public Vardgivare getVardgivare() {
        return vardgivare;
    }

    public void setVardgivare(Vardgivare vardgivare) {
        this.vardgivare = vardgivare;
    }

}
