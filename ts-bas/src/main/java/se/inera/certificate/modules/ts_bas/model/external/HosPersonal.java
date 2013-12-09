package se.inera.certificate.modules.ts_bas.model.external;

import java.util.ArrayList;
import java.util.List;

import se.inera.certificate.model.Kod;

public class HosPersonal extends se.inera.certificate.model.HosPersonal {

    private List<Kod> specialiteter;

    public List<Kod> getSpecialiteter() {
        if (specialiteter == null) {
            specialiteter = new ArrayList<>();
        }
        return specialiteter;
    }
}
