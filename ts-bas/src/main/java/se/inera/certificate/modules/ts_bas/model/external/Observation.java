package se.inera.certificate.modules.ts_bas.model.external;

import se.inera.certificate.model.Id;

public class Observation extends se.inera.certificate.model.Observation {

    private Id id;

    private Boolean forekonst;

    public Id getId() {
        return id;
    }

    public void setId(Id id) {
        this.id = id;
    }

    public Boolean getForekonst() {
        return forekonst;
    }

    public void setForekonst(Boolean forekonst) {
        this.forekonst = forekonst;
    }
}
