package se.inera.certificate.model;

import java.util.Collections;
import java.util.List;

/**
 * @author andreaskaltenbach
 */
public final class Valideringsresultat {

    private List<String> fel;

    public Valideringsresultat() {
        fel = Collections.emptyList();
    }

    public Valideringsresultat(List<String> fel) {
        this.fel = fel;
    }

    public List<String> getFel() {
        return fel;
    }

    public void setFel(List<String> fel) {
        this.fel = fel;
    }
}
