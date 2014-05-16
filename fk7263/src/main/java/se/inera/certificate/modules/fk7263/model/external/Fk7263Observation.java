package se.inera.certificate.modules.fk7263.model.external;

import java.util.ArrayList;
import java.util.List;

import se.inera.certificate.model.Observation;
import se.inera.certificate.model.PhysicalQuantity;
import se.inera.certificate.model.Utforarroll;

/**
 * @author andreaskaltenbach
 */
public class Fk7263Observation extends Observation {

    private List<PhysicalQuantity> varde;

    private List<Fk7263Prognos> prognoser;

    private Utforarroll utforsAv;

    public List<Fk7263Prognos> getPrognoser() {
        if (prognoser == null) {
            prognoser = new ArrayList<>();
        }
        return prognoser;
    }

    @Override
    public List<PhysicalQuantity> getVarde() {
        if (varde == null) {
            varde = new ArrayList<>();
        }
        return varde;
    }

    public Utforarroll getUtforsAv() {
        return utforsAv;
    }

    public void setUtforsAv(Utforarroll utforsAv) {
        this.utforsAv = utforsAv;
    }
}