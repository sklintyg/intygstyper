package se.inera.certificate.modules.fk7263.model.external;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonTypeInfo;

import se.inera.certificate.model.Observation;
import se.inera.certificate.model.Utforarroll;

/**
 * @author andreaskaltenbach
 */
public class Fk7263Observation extends Observation {

    @JsonTypeInfo(use = JsonTypeInfo.Id.CLASS)
    private List<Object> varde;

    private Utforarroll utforsAv;

    private String kommentar;

    @Override
    public List<Object> getVarde() {
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

    public String getKommentar() {
        return kommentar;
    }

    public void setKommentar(String kommentar) {
        this.kommentar = kommentar;
    }
}
