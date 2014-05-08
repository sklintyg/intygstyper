package se.inera.certificate.modules.ts_diabetes.model.external;

import java.util.ArrayList;
import java.util.List;

import se.inera.certificate.model.Kod;
import se.inera.certificate.model.PhysicalQuantity;

public class Observation extends se.inera.certificate.model.Observation {

    private List<PhysicalQuantity> varde;

    private Kod lateralitet;

    private String ostruktureradTid;

    public List<PhysicalQuantity> getVarde() {
        if (varde == null) {
            varde = new ArrayList<>();
        }
        return varde;
    }

    public Kod getLateralitet() {
        return lateralitet;
    }

    public void setLateralitet(Kod lateralitet) {
        this.lateralitet = lateralitet;
    }

    public String getOstruktureradTid() {
        return ostruktureradTid;
    }

    public void setOstruktureradTid(String ostruktureradTid) {
        this.ostruktureradTid = ostruktureradTid;
    }
}
