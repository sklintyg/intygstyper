package se.inera.certificate.modules.ts_bas.model.external;

import java.util.ArrayList;
import java.util.List;

import se.inera.certificate.model.Kod;

public class HosPersonal extends se.inera.certificate.model.HosPersonal {

    private Vardenhet vardenhet;

    private List<Kod> specialiteter;

    private List<Kod> befattningar;

    public List<Kod> getSpecialiteter() {
        if (specialiteter == null) {
            specialiteter = new ArrayList<>();
        }
        return specialiteter;
    }

    public List<Kod> getBefattningar() {
        if (befattningar == null) {
            befattningar = new ArrayList<>();
        }
        return befattningar;
    }

    @Override
    public Vardenhet getVardenhet() {
        return vardenhet;
    }

    public void setVardenhet(Vardenhet vardenhet) {
        this.vardenhet = vardenhet;
    }
}
