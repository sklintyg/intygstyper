package se.inera.certificate.modules.ts_bas.model.external;

import se.inera.certificate.model.Id;
import se.inera.certificate.model.Kod;


public class Observation extends se.inera.certificate.model.Observation {

    private Id id;

    private Boolean forekomst;

    private Kod lateralitet;


    public Kod getLateralitet() {
        return lateralitet;
    }

    public void setLateralitet(Kod lateralitet) {
        this.lateralitet = lateralitet;
    }

    public Id getId() {
        return id;
    }

    public void setId(Id id) {
        this.id = id;
    }

    public Boolean getForekomst() {
        return forekomst;
    }

    public void setForekomst(Boolean forekomst) {
        this.forekomst = forekomst;
    }
}
