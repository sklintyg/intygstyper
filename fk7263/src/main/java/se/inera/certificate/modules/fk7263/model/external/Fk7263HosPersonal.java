package se.inera.certificate.modules.fk7263.model.external;

import se.inera.certificate.model.HosPersonal;

public class Fk7263HosPersonal extends HosPersonal {

    private Fk7263Vardenhet vardenhet;

    @Override
    public Fk7263Vardenhet getVardenhet() {
        return vardenhet;
    }

    public void setVardenhet(Fk7263Vardenhet vardenhet) {
        this.vardenhet = vardenhet;
    }
}
