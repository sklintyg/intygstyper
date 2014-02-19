package se.inera.certificate.modules.ts_diabetes.model.external;

import java.util.ArrayList;
import java.util.List;

import se.inera.certificate.model.Kod;

public class Rekommendation extends se.inera.certificate.model.Rekommendation {

    private List<Kod> varde;

    public List<Kod> getVarde() {
        if (varde == null) {
            varde = new ArrayList<Kod>();
        }
        return varde;
    }
}
