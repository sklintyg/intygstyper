package se.inera.certificate.modules.fk7263.model.external;

import java.util.ArrayList;
import java.util.List;

import se.inera.certificate.model.Aktivitet;
import se.inera.certificate.model.Utforarroll;
import se.inera.certificate.model.Vardenhet;

/**
 * @author andreaskaltenbach
 */
public class Fk7263Aktivitet extends Aktivitet {

    private List<Utforarroll> beskrivsAv;
    private Vardenhet utforsVid;

    public List<Utforarroll> getBeskrivsAv() {
        if (beskrivsAv == null) {
            beskrivsAv = new ArrayList<Utforarroll>();
        }
        return this.beskrivsAv;
    }

    public Vardenhet getUtforsVid() {
        return utforsVid;
    }

    public void setUtforsVid(Vardenhet utforsVid) {
        this.utforsVid = utforsVid;
    }
}
