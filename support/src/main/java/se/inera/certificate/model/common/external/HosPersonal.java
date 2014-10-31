package se.inera.certificate.model.common.external;

import java.util.ArrayList;
import java.util.List;

import se.inera.certificate.model.Kod;

public class HosPersonal extends se.inera.certificate.model.HosPersonal {

    private Vardenhet vardenhet;

    private List<Kod> specialiteter;

    public List<Kod> getSpecialiteter() {
        if (specialiteter == null) {
            specialiteter = new ArrayList<>();
        }
        return specialiteter;
    }

    @Override
    public Vardenhet getVardenhet() {
        return vardenhet;
    }

    public void setVardenhet(Vardenhet vardenhet) {
        this.vardenhet = vardenhet;
    }
}
