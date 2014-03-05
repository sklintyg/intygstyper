package se.inera.certificate.modules.ts_diabetes.model.external;

import se.inera.certificate.model.Kod;

public class Bilaga {

    private Kod bilagetyp;
    private Boolean forekomst;

    public Kod getBilagetyp() {
        return bilagetyp;
    }

    public void setBilagetyp(Kod bilagetyp) {
        this.bilagetyp = bilagetyp;
    }

    public Boolean getForekomst() {
        return forekomst;
    }

    public void setForekomst(Boolean forekomst) {
        this.forekomst = forekomst;
    }

}
