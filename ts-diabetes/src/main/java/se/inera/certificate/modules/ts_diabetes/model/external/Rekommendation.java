package se.inera.certificate.modules.ts_diabetes.model.external;

import java.util.ArrayList;
import java.util.List;

import se.inera.certificate.model.Kod;

public class Rekommendation extends se.inera.certificate.model.Rekommendation {

    private List<Kod> varde;

    private Boolean boolean_varde;

    public List<Kod> getVarde() {
        if (varde == null) {
            varde = new ArrayList<Kod>();
        }
        return varde;
    }

    public Boolean getBoolean_varde() {
        return boolean_varde;
    }

    public void setBoolean_varde(Boolean boolean_varde) {
        this.boolean_varde = boolean_varde;
    }

    public void setVarde(List<Kod> varde) {
        this.varde = varde;
    }
}
