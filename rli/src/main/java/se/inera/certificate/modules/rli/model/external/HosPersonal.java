package se.inera.certificate.modules.rli.model.external;

public class HosPersonal extends se.inera.intyg.common.support.model.HosPersonal {

    private Vardenhet vardenhet;

    @Override
    public Vardenhet getVardenhet() {
        return vardenhet;
    }

    public void setVardenhet(Vardenhet vardenhet) {
        this.vardenhet = vardenhet;
    }
}
