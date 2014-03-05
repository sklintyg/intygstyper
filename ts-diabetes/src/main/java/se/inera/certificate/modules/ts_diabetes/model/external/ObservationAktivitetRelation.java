package se.inera.certificate.modules.ts_diabetes.model.external;

import se.inera.certificate.model.Id;

public class ObservationAktivitetRelation {

    private Id observationsid;

    private Id aktivitetsid;

    public Id getObservationsid() {
        return observationsid;
    }

    public void setObservationsid(Id observationsid) {
        this.observationsid = observationsid;
    }

    public Id getAktivitetsid() {
        return aktivitetsid;
    }

    public void setAktivitetsid(Id aktivitetsid) {
        this.aktivitetsid = aktivitetsid;
    }

}
