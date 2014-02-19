package se.inera.certificate.modules.ts_diabetes.model.internal;

/**
 * Class encapsulating the values for synskarpa for a single eye
 * 
 * @author erik
 * 
 */
public class Synskarpevarden {

    Double utanKorrektion;
    Double medKorrektion;

    public double getUtanKorrektion() {
        return utanKorrektion;
    }

    public void setUtanKorrektion(Double utanKorrektion) {
        this.utanKorrektion = utanKorrektion;
    }

    public Double getMedKorrektion() {
        return medKorrektion;
    }

    public void setMedKorrektion(Double medKorrektion) {
        this.medKorrektion = medKorrektion;
    }

}
