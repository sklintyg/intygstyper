package se.inera.certificate.modules.ts_diabetes.model.external;

import org.joda.time.LocalDate;
import org.joda.time.Partial;

import se.inera.certificate.model.Id;
import se.inera.certificate.model.Kod;


public class Observation extends se.inera.certificate.model.Observation {

    private Id id;

    private Boolean forekomst;

    private Kod lateralitet;
    
    private String ostruktureradTid;
    
    private LocalDate observationstidDate;

    private Partial observationstidPartialDate;


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

    public String getOstruktureradTid() {
        return ostruktureradTid;
    }

    public void setOstruktureradTid(String ostruktureradTid) {
        this.ostruktureradTid = ostruktureradTid;
    }

    public LocalDate getObservationstidDate() {
        return observationstidDate;
    }

    public void setObservationstidDate(LocalDate observationstidDate) {
        this.observationstidDate = observationstidDate;
    }

    public Partial getObservationstidPartialDate() {
        return observationstidPartialDate;
    }

    public void setObservationstidPartialDate(Partial observationstidPartialDate) {
        this.observationstidPartialDate = observationstidPartialDate;
    }
}
