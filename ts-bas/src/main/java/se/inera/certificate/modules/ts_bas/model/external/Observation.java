package se.inera.certificate.modules.ts_bas.model.external;

import java.util.ArrayList;
import java.util.List;

import se.inera.certificate.model.Kod;
import se.inera.certificate.model.PhysicalQuantity;

public class Observation extends se.inera.certificate.model.Observation {

    private List<PhysicalQuantity> varde;

    private Kod lateralitet;

    @Override
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
}
