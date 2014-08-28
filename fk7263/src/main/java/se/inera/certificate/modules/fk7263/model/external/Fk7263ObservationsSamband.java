package se.inera.certificate.modules.fk7263.model.external;

import se.inera.certificate.model.Id;
import se.inera.certificate.modules.support.api.exception.ModuleException;

public class Fk7263ObservationsSamband {
    private Id observationsidEtt;
    private Id observationsidTva;

    public Fk7263ObservationsSamband() {
    }

    public Fk7263ObservationsSamband(Id observationsidEtt, Id observationsidTva) throws ModuleException {
        if (observationsidEtt == null || observationsidTva == null) {
            throw new ModuleException("Unable to create Fk7273ObservationsSamband, incorrect input");
        }
        this.observationsidEtt = observationsidEtt;
        this.observationsidTva = observationsidTva;
    }

    public Id getObservationsidEtt() {
        return observationsidEtt;
    }
    public void setObservationsidEtt(Id observationsidEtt) {
        this.observationsidEtt = observationsidEtt;
    }
    public Id getObservationsidTva() {
        return observationsidTva;
    }
    public void setObservationsidTva(Id observationsidTva) {
        this.observationsidTva = observationsidTva;
    }
}
