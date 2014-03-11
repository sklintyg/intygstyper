package se.inera.certificate.modules.rli.model.external;

public class Observation extends se.inera.certificate.model.Observation {

    private Utforarroll utforsAv;
    
    @Override
    public Utforarroll getUtforsAv() {
        return utforsAv;
    }

    public void setUtforsAv(Utforarroll utforsAv) {
        this.utforsAv = utforsAv;
    }
}
