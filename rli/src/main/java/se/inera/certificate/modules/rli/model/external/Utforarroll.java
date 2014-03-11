package se.inera.certificate.modules.rli.model.external;

public class Utforarroll extends se.inera.certificate.model.Utforarroll {

    private HosPersonal antasAv;

    @Override
    public HosPersonal getAntasAv() {
        return antasAv;
    }

    public void setAntasAv(HosPersonal antasAv) {
        this.antasAv = antasAv;
    }
}
